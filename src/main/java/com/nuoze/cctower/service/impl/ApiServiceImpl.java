package com.nuoze.cctower.service.impl;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nuoze.cctower.common.enums.ApiDataEnum;
import com.nuoze.cctower.common.enums.IncomeType;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.IpUtil;
import com.nuoze.cctower.component.BillingComponent;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.dto.ApiCarLongDTO;
import com.nuoze.cctower.pojo.dto.ApiDTO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.*;
import com.nuoze.cctower.pojo.vo.ApiCheckCarVO;
import com.nuoze.cctower.pojo.vo.ApiOutVO;
import com.nuoze.cctower.pojo.vo.ApiPayStatusVO;
import com.nuoze.cctower.service.AccountService;
import com.nuoze.cctower.service.ApiService;
import com.nuoze.cctower.service.PassagewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.nuoze.cctower.common.constant.Constant.*;
import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;

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
    @Autowired
    private OrderNumberDAO orderNumberDAO;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ParkingDAO parkingDAO;

    @Override
    public boolean in(ApiDTO apiDTO) {
        log.info("[API CAR IN] carNumber: {}, entrance ip: {}, enter parking id: {} ", apiDTO.getCarNumber(), apiDTO.getIp(), apiDTO.getParkingId());
        ParkingRecord record = new ParkingRecord();
        Passageway passageway = passagewayDAO.findByParkingIdAndIp(apiDTO.getParkingId(), apiDTO.getIp());
        if (passageway != null) {
            Long entranceId = passageway.getId();
            record.setEntranceId(entranceId);
        }
        recordDAO.insert(record.setParkingId(apiDTO.getParkingId()).setCarNumber(apiDTO.getCarNumber()).setStatus(NOT_LEAVE).setInTime(new Date()).setPayStatus(0));
        return true;
    }

    @Override
    public ApiOutVO out(ApiDTO apiDTO) {
        log.info("[API CAR OUT] carNumber: {}, exit ip: {}, exit parking id: {} ", apiDTO.getCarNumber(), apiDTO.getIp(), apiDTO.getParkingId());
        //parkingId：停车场id、carNumber：车牌号、record：停车记录
        Long parkingId = apiDTO.getParkingId();
        String carNumber = apiDTO.getCarNumber();
        ParkingRecord record = recordDAO.findByParkingIdAndCarNumberAndStatus(parkingId, carNumber);
        Car car = carDAO.findByParkingIdAndCarNumber(parkingId, carNumber);
        Passageway passageway = passagewayDAO.findByParkingIdAndIp(apiDTO.getParkingId(), apiDTO.getIp());
        ApiOutVO apiVO = new ApiOutVO();
        Account account = accountService.findByParkingId(parkingId);

        //解决月租车与永久月租重复出厂
        Parking parking = parkingDAO.selectByPrimaryKey(parkingId);
        if (parking != null && car != null && car.getStatus() == BUSINESS_NORMAL_CAR) {
            boolean vipStatic = parking.getVipStatic() == 0 && VIP_CAR == car.getParkingType();
            boolean rentStatic = parking.getRentStatic() == 0 && MONTHLY_CAR == car.getParkingType() && new Date().before(car.getMonthlyParkingEnd());
            if (vipStatic || rentStatic) {
                if (record != null) {
                    record.setOutTime(new Date()).setPayType(PAYMENT_MONTHLY);
                    if (passageway != null) {
                        record.setExitId(passageway.getId());
                    }
                }
                record.setPayStatus(PAY_STATUS_NORMAL);
                return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car.getParkingType(), carNumber, String.valueOf(EMPTY_MONEY), String.valueOf(EMPTY_MONEY));
            }
        }

        if (record != null) {
            //cost：车费、passageway：车场通道、Car：车辆表
            BigDecimal cost = record.getCost();
            if (passageway != null) {
                Long exitId = passageway.getId();
                record.setExitId(exitId);
            }
            record.setOutTime(new Date());

            //如果已支付，paid设为1：已支付，status设为已出门
            if (record.getPayStatus() == 1) {
                return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car != null ? car.getParkingType() : 0, carNumber, cost == null ? String.valueOf(EMPTY_MONEY) : String.valueOf(cost), String.valueOf(record.getServiceCharge()));
            }
            //停车时长（分钟）
            int costTime = DateUtils.diffMin(record.getInTime());
            log.info(costTime + "****");
            //如果是月租车或VIP车或商户车辆免费时长未用完，同样paid=1，status：LEAVE_YET
            if (car != null && car.getStatus() == BUSINESS_NORMAL_CAR) {
                //SINGLEVIP_CAR：单次VIP
                if (SINGLEVIP_CAR == car.getParkingType()) {
                    record.setPayType(PAYMENT_VIP).setPayStatus(PAY_STATUS_NORMAL).setCostTime(costTime);
                    carDAO.updateByPrimaryKey(car.setStatus(BUSINESS_FORBIDDEN_CAR).setUpdateTime(new Date()));
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, SINGLEVIP_CAR, carNumber, String.valueOf(EMPTY_MONEY), String.valueOf(EMPTY_MONEY));
                }
                //VIP_CAR：永久月租
                if (VIP_CAR == car.getParkingType()) {
                    record.setPayType(PAYMENT_MONTHLY).setPayStatus(PAY_STATUS_NORMAL).setCostTime(costTime);
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, VIP_CAR, carNumber, String.valueOf(EMPTY_MONEY), String.valueOf(EMPTY_MONEY));
                }
                //SPECIAL_CAR：特殊车辆
                if (SPECIAL_CAR == car.getParkingType()) {
                    record.setCostTime(costTime).setCost(new BigDecimal(5)).setCostTime(costTime);
                    apiVO.setCodeUrl(getCodeUrl(record, car.getCost() == null ? new BigDecimal(5) : car.getCost()));
                    return buildApiOutVO(apiVO, record, 0, READY_TO_LEAVE, SPECIAL_CAR, carNumber, String.valueOf(car.getCost() == null ? 5 : car.getCost()), String.valueOf(paymentComponent.getServiceCharge(car.getCost() == null ? new BigDecimal(5) : car.getCost(), account.getServiceCharge())));
                }
                //MONTHLY_CAR:包月
                if (MONTHLY_CAR == car.getParkingType() && new Date().before(car.getMonthlyParkingEnd())) {
                    record.setPayType(PAYMENT_MONTHLY).setPayStatus(PAY_STATUS_NORMAL).setCostTime(costTime);
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, MONTHLY_CAR, carNumber, String.valueOf(EMPTY_MONEY), String.valueOf(EMPTY_MONEY));
                }
                //BUSINESS_CAR：商户车辆 TIMECOUPON_CAR：时长劵商户车辆
                if (BUSINESS_CAR == car.getParkingType() || TIMECOUPON_CAR == car.getParkingType()) {
                    if (car.getFreeTime() >= costTime) {
                        carDAO.deleteByPrimaryKey(car.getId());
                        record.setPayType(PAYMENT_VBUSINESS).setPayStatus(PAY_STATUS_NORMAL).setCostTime(costTime);
                        return buildApiOutVO(apiVO, record, 1, LEAVE_YET, BUSINESS_CAR, carNumber, String.valueOf(EMPTY_MONEY), String.valueOf(EMPTY_MONEY));
                    } else {
                        costTime = costTime - car.getFreeTime();
                    }
                }
            }
            //收费规则
            Billing billing = billingDAO.selectByParkingId(parkingId);
            //免费停车时间
            Integer freeTime = billing.getFreeTime();
            if (freeTime != null && freeTime >= costTime) {
                 if (car != null) {
                     if (BUSINESS_CAR == car.getParkingType() && BUSINESS_NORMAL_CAR == car.getStatus()) {
                         carDAO.deleteByPrimaryKey(car.getId());
                         record.setPayType(PAYMENT_VBUSINESS);
                     }
                 }
                 record.setPayType(PAYMENT_NOT).setPayStatus(PAY_STATUS_NORMAL).setCostTime(costTime);
                 return buildApiOutVO(apiVO, record, 1, LEAVE_YET,0, carNumber, String.valueOf(EMPTY_MONEY), String.valueOf(EMPTY_MONEY));
            }

            //计算车费
            cost = billingComponent.cost(costTime, parkingId, null);
            record.setCostTime(costTime).setCost(cost).setCostTime(costTime);
            //计算服务费
            BigDecimal serviceCharge = EMPTY_MONEY;
            if (account != null) {
                serviceCharge = paymentComponent.getServiceCharge(cost, account.getServiceCharge());
            }
            //以上条件都不满足，判断car是不是微信会员临时车辆
            car = carDAO.findByCarNumberAndParkingType(carNumber, 0);
            if (car != null) {
                String openId = car.getOpenId();
                Member member = memberDAO.selectByOpenId(openId);
                //如果余额 > 车费，直接从余额扣除。
                if (member.getBalance().compareTo(cost) >= 0) {
                    BigDecimal balance = member.getBalance().subtract(cost);
                    topUpRecordDAO.insert(new TopUpRecord().setParkingId(parkingId).setBalance(balance).setAmount(cost).setPayStatus(1).setBillingType(0).setCreateTime(new Date()).setUpdateTime(new Date()).setOpenId(openId)
                    );
                    memberDAO.updateByPrimaryKeySelective(member.setBalance(balance).setUpdateTime(new Date())
                    );
                    record.setPayType(PAYMENT_WECHAT);
                    billingComponent.addTradingRecord(cost.subtract(serviceCharge), parkingId, IncomeType.PARKING_CHARGE, car.getNumber());
                    billingComponent.addAccountBalance(cost.subtract(serviceCharge), parkingId);
                    return buildApiOutVO(apiVO, record, 1, LEAVE_YET, car != null ? car.getParkingType() : 0, carNumber, String.valueOf(cost), String.valueOf(serviceCharge));
                }
            }
            //如果依旧不是微信会员临时车辆或者微信余额不足，返回的apiOutVO中添加二维码url
            apiVO.setCodeUrl(getCodeUrl(record, cost));
            return buildApiOutVO(apiVO, record, 0, READY_TO_LEAVE, car != null ? car.getParkingType() : 0, carNumber, cost == null ? String.valueOf(EMPTY_MONEY) : String.valueOf(cost), String.valueOf(serviceCharge));
        }
        return null;
    }

    /**
     * 获得付款码url
     *
     * @param record 停车记录
     * @param cost   车费
     * @return String
     */
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
            //*** 付款码支付订单 ***
            if (StringUtils.isEmpty(record.getQrCodeOrderSn())) {
                record.setQrCodeOrderSn(orderRequest.getOutTradeNo());
            } else {
                orderNumberDAO.insert(new OrderNumber().setParkingRecordId(record.getId()).setOrderSn(orderRequest.getOutTradeNo()).setCreateTime(new Date()));
            }
            codeUrl = result.getCodeURL();
        } catch (WxPayException e) {
            log.error("pre pay has exception: {}", e.getMessage());
        }
        return codeUrl;
    }

    /**
     * 创建ApiOutVO
     *
     * @param apiVO
     * @param record      停车记录
     * @param paid        支付状态：0：未支付 1：支付成功
     * @param status      是否出场 0：否 1：是 2：待出场
     * @param parkingType 车辆类型 0:临时车 1:包月 2:VIP 3:商户车辆
     * @param carNumber   车牌号
     * @param cost        费用
     * @return ApiOutVO
     */
    private ApiOutVO buildApiOutVO(ApiOutVO apiVO, ParkingRecord record, int paid, int status, int parkingType, String carNumber, String cost, String serviceCharge) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (record != null) {
            recordDAO.updateByPrimaryKeySelective(record.setStatus(status).setUuid(uuid).setServiceCharge(new BigDecimal(serviceCharge)));
        }
        return apiVO.setPaid(paid).setCarNumber(carNumber).setType(parkingType).setCost(cost).setServiceCharge(serviceCharge).setUuid(uuid);
    }

    /**
     * 线下支付回调
     *
     * @param apiDTO
     * @return
     */
    @Override
    public boolean paid(ApiDTO apiDTO) {
        Car car = carDAO.findByParkingIdAndCarNumber(apiDTO.getParkingId(), apiDTO.getCarNumber());
        if (car != null) {
            if (BUSINESS_CAR == car.getParkingType() || BUSINESS_NORMAL_CAR == car.getStatus()) {
                carDAO.deleteByPrimaryKey(car.getId());
            }
        }
        ParkingRecord record = recordDAO.findByParkingIdAndCarNumberAndStatus(apiDTO.getParkingId(), apiDTO.getCarNumber());
        if (record != null) {
            return recordDAO.updateByPrimaryKeySelective(record.setPayType(PAYMENT_OFFLINE).setPayStatus(1).setStatus(LEAVE_YET).setPayTime(new Date()).setOutTime(new Date())) > 0;
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
            return vo.setUuid(uuid);
        }
        return null;
    }

    @Override
    public int saveCarLong(ApiCarLongDTO dto) {
        return carDAO.insert(dtoToCar(dto).setCreateId(0L).setCreateTime(new Date()).setUpdateTime(new Date()));
    }

    @Override
    public int updateCarLong(ApiCarLongDTO dto) {
        return carDAO.updateByUuid(dtoToCar(dto).setUpdateTime(new Date()));
    }

    @Override
    public int batchRemoveCarLong(String[] uuids) {
        return carDAO.batchRemoveByUuid(uuids);
    }

    @Override
    public Car findByUuid(String uuid) {
        return carDAO.findByUuid(uuid);
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
            return recordDAO.updateByPrimaryKeySelective(record.setCarNumber(apiDTO.getCarNumber())) > 0;
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
    private Car dtoToCar(ApiCarLongDTO dto) {
        dto.setCarNumber(dto.getCarNumber().toUpperCase());
        Car car = new Car();
        BeanUtils.copyProperties(dto, car);
        car.setNumber(dto.getCarNumber()).setParkingType(1).setStatus(1).setInfieldPermission(1);
        try {
            car.setMonthlyParkingStart(DateUtils.toDateTime(dto.getBeginDate())).setMonthlyParkingEnd(DateUtils.toDateTime(dto.getEndDate()));
        } catch (Exception e) {
            log.error("Time format error: {}", e.getMessage());
        }
        return car;
    }
}
