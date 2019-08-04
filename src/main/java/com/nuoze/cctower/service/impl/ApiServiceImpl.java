package com.nuoze.cctower.service.impl;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nuoze.cctower.common.enums.ApiDataEnum;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.IpUtil;
import com.nuoze.cctower.component.BillingComponent;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.dto.ApiDTO;
import com.nuoze.cctower.pojo.entity.*;
import com.nuoze.cctower.pojo.vo.ApiCheckCarVO;
import com.nuoze.cctower.pojo.vo.ApiOutVO;
import com.nuoze.cctower.pojo.vo.ApiPayStatusVO;
import com.nuoze.cctower.service.ApiService;
import com.nuoze.cctower.service.PassagewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.nuoze.cctower.common.constant.Constant.*;

/**
 * @author JiaShun
 * @date 2019-03-19 23:36
 */
@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ParkingRecordDAO recordDAO;
    @Autowired
    private PassagewayDAO passagewayDAO;
    @Autowired
    private BillingDAO billingDAO;
    @Autowired
    private CarDAO carDAO;
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private TopUpRecordDAO topUpRecordDAO;
    @Autowired
    private PassagewayService passagewayService;
    @Autowired
    private MqSendComponent mqSendComponent;
    @Autowired
    private PaymentComponent paymentComponent;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private BillingComponent billingComponent;
    @Autowired
    private BillingDetailDAO detailDAO;

    @Override
    public boolean in(ApiDTO apiDTO) {
        log.info("[API CAR IN] carNumber: {}, entrance ip: {}, enter parking id: {} ", apiDTO.getCarNumber(), apiDTO.getIp(), apiDTO.getParkingId());
        ParkingRecord record = new ParkingRecord();
        Passageway passageway = passagewayDAO.findByParkingIdAndIp(apiDTO.getParkingId(), apiDTO.getIp());
        if (passageway != null) {
            Long entranceId = passageway.getId();
            record.setEntranceId(entranceId);
        }
        record.setParkingId(apiDTO.getParkingId());
        record.setCarNumber(apiDTO.getCarNumber());
        record.setStatus(NOT_LEAVE);
        record.setInTime(new Date());
        record.setPayStatus(0);
        recordDAO.insert(record);
        return true;
    }

    @Override
    public ApiOutVO out(ApiDTO apiDTO) {
        log.info("[API CAR OUT] carNumber: {}, exit ip: {}, exit parking id: {} ", apiDTO.getCarNumber(), apiDTO.getIp(), apiDTO.getParkingId());
        Long parkingId = apiDTO.getParkingId();
        String carNumber = apiDTO.getCarNumber();
        ParkingRecord record = recordDAO.findByParkingIdAndCarNumberAndStatus(parkingId, carNumber);
        if (record != null) {
            BigDecimal cost = record.getCost();
            Passageway passageway = passagewayDAO.findByParkingIdAndIp(apiDTO.getParkingId(), apiDTO.getIp());
            Car car = carDAO.findByParkingIdAndCarNumber(parkingId, carNumber);
            if (passageway != null) {
                Long exitId = passageway.getId();
                record.setExitId(exitId);
            }
            record.setOutTime(new Date());
            ApiOutVO apiVO = new ApiOutVO();
            //如果已支付，paid设为1：已支付，status设为已出门
            if (record.getPayStatus() == 1) {
                return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car != null ? car.getParkingType() : 0, carNumber, cost == null ? EMPTY_MONEY.toString() : cost.toString());
            }
            int costTime = DateUtils.diffMin(record.getInTime());
            //如果是月租车或VIP车或商户车辆免费时长未用完，同样paid=1，status：LEAVE_YET
            if (car != null) {
                if (MONTHLY_CAR == car.getParkingType() && new Date().before(car.getMonthlyParkingEnd())) {
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car.getParkingType(), carNumber, EMPTY_MONEY.toString());
                }
                if (VIP_CAR == car.getParkingType()) {
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car.getParkingType(), carNumber, EMPTY_MONEY.toString());
                }
                if (BUSINESS_CAR == car.getParkingType() && BUSINESS_NORMAL_CAR == car.getStatus()) {
                    if (car.getFreeTime() > costTime) {
                        car.setStatus(BUSINESS_FORBIDDEN_CAR);
                        car.setUpdateTime(new Date());
                        carDAO.updateByPrimaryKeySelective(car);
                        return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car.getParkingType(), carNumber, EMPTY_MONEY.toString());
                    }
                }
            }
            Billing billing = billingDAO.selectByParkingId(parkingId);
            Integer freeTime = billing.getFreeTime();
            if (freeTime != null) {
                //如果在停车场免费停车时间内，paid=1， status: LEAVE_YET
                if (freeTime >= costTime) {
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car != null ? car.getParkingType() : 0, carNumber, cost == null ? EMPTY_MONEY.toString() : cost.toString());
                }
            }
            //以上条件都不满足，判断car是不是微信会员临时车辆
            if (cost == null) {
                cost = billingComponent.cost(costTime, parkingId, carNumber);
                record.setCostTime(costTime);
                record.setCost(cost);
            }
            car = carDAO.findByCarNumberAndParkingType(carNumber, 0);
            if (car != null) {
                String openId = car.getOpenId();
                Member member = memberDAO.selectByOpenId(openId);
                //如果余额 > 车费，直接从余额扣除。
                if (member.getBalance().compareTo(cost) >= 0) {
                    BigDecimal balance = member.getBalance().subtract(cost);
                    TopUpRecord upRecord = new TopUpRecord();
                    upRecord.setParkingId(parkingId);
                    upRecord.setBalance(balance);
                    upRecord.setAmount(cost);
                    upRecord.setPayStatus(1);
                    upRecord.setBillingType(0);
                    upRecord.setCreateTime(new Date());
                    upRecord.setUpdateTime(new Date());
                    upRecord.setOpenId(openId);
                    topUpRecordDAO.insert(upRecord);
                    member.setBalance(balance);
                    member.setUpdateTime(new Date());
                    memberDAO.updateByPrimaryKeySelective(member);
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car.getParkingType(), carNumber, cost.toString());
                }
            }
            //如果依旧不是微信会员临时车辆或者微信余额不足，返回的apiOutVO中添加二维码url
            apiVO.setCodeUrl(getCodeUrl(record, cost));
            return buildApiOutVO(apiVO, record, 0, READY_TO_LEAVE, car != null ? car.getParkingType() : 0, carNumber, cost == null ? EMPTY_MONEY.toString() : cost.toString());
        }
        return null;
    }

    private String getCodeUrl(ParkingRecord record, BigDecimal cost) {
        WxPayUnifiedOrderRequest orderRequest = this.paymentComponent.buildWxPayReq(null, cost, null, IpUtil.getLocalHostIp());
        orderRequest.setBody("CCTower: 停车费");
        orderRequest.setTradeType("NATIVE");
        orderRequest.setProductId(UUID.randomUUID().toString().replace("-", ""));
        WxPayUnifiedOrderResult result;
        String codeUrl = null;
        try {
            result = wxPayService.unifiedOrder(orderRequest);
            String prepayId = result.getPrepayId();
            if (!StringUtils.isEmpty(prepayId)) {
                record.setPrepayId(prepayId);
            }
            record.setOrderSn(orderRequest.getOutTradeNo());
            codeUrl = result.getCodeURL();
        } catch (WxPayException e) {
            log.error("pre pay has exception: {}", e.getMessage());
        }
        return codeUrl;
    }

    private ApiOutVO buildApiOutVO(ApiOutVO apiVO, ParkingRecord record, int paid, int status, int parkingType, String carNumber, String cost) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        record.setStatus(status);
        record.setUuid(uuid);
        recordDAO.updateByPrimaryKeySelective(record);
        apiVO.setPaid(paid);
        apiVO.setCarNumber(carNumber);
        apiVO.setType(parkingType);
        apiVO.setCost(cost);
        apiVO.setUuid(uuid);
        return apiVO;
    }

    @Override
    public boolean paid(ApiDTO apiDTO) {
        ParkingRecord record = recordDAO.findByParkingIdAndCarNumberAndStatus(apiDTO.getParkingId(), apiDTO.getCarNumber());
        if (record != null) {
            record.setPayType(PAYMENT_OFFLINE);
            record.setPayStatus(1);
            record.setStatus(LEAVE_YET);
            record.setPayTime(new Date());
            record.setOutTime(new Date());
            return recordDAO.updateByPrimaryKeySelective(record) > 0;
        }
        return false;
    }

    @Override
    public ApiPayStatusVO checkPayStatus(String uuid) {
        ParkingRecord pr = recordDAO.findByUuid(uuid);
        if (pr != null) {
            Integer status = pr.getStatus();
            ApiPayStatusVO vo = new ApiPayStatusVO();
            vo.setPayStatus(0);
            if (status == 1) {
                vo.setPayStatus(status);
            } else {
                Integer payStatus = pr.getPayStatus();
                if (payStatus == 1) {
                    vo.setPayStatus(payStatus);
                }
            }
            vo.setUuid(uuid);
            return vo;
        }
        return null;
    }

    @Override
    public ApiCheckCarVO isRentFreeCar(ApiDTO apiDTO) {
        Car car = carDAO.findByParkingIdAndCarNumber(apiDTO.getParkingId(), apiDTO.getCarNumber());
        int status = 0;
        int type = 0;
        int infieldPermission = 0;
        if (car != null && car.getParkingType() == MONTHLY_CAR && car.getStatus() == 1) {
            if (MONTHLY_CAR == car.getParkingType()) {
                status = 1;
            }
            if (1 == car.getStatus()) {
                type = 1;
            }
            if (1 == car.getInfieldPermission()) {
                infieldPermission = 1;
            }
            if (new Date().after(car.getMonthlyParkingEnd())) {
                type = 2;
            }
        }
        return new ApiCheckCarVO(status, type, infieldPermission);
    }

    @Override
    public int addPassageway(Passageway passageway) {
        return passagewayService.save(passageway);
    }

    @Override
    public void deletePassageway(Long parkingId, String ip) {
        this.passagewayDAO.deleteByParkingIdAndIp(parkingId, ip);
    }

    @Override
    public boolean changeCarNumber(ApiDTO apiDTO) {
        ParkingRecord record = recordDAO.findByParkingIdAndCarNumberAndStatus(apiDTO.getParkingId(), apiDTO.getOldCarNumber());
        if (record != null) {
            record.setCarNumber(apiDTO.getCarNumber());
            recordDAO.updateByPrimaryKeySelective(record);
            return true;
        }
        return false;
    }

    @Override
    public boolean initData(ApiDTO apiDTO, String type) {
        switch (type) {
            case RENT_CAR_TYPE:
                List<Car> list = carDAO.findByParkingIdAndParkingType(apiDTO.getParkingId(), 1);
                if (!CollectionUtils.isEmpty(list)) {
                    for (Car car : list) {
                        if (new Date().before(car.getMonthlyParkingEnd())) {
                            mqSendComponent.sendRentCar(ApiDataEnum.ADD, car);
                        }
                    }
                }
                break;
            case BILLING_CAR_TYPE:
                Billing billing = billingDAO.selectByParkingId(apiDTO.getParkingId());
                BillingDetail detail = detailDAO.selectByParkingId(apiDTO.getParkingId());
                mqSendComponent.sendBilling(ApiDataEnum.ADD, billing, detail);
                break;
            default:
                break;
        }
        return true;
    }

}
