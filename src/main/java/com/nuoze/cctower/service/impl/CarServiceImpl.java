package com.nuoze.cctower.service.impl;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nuoze.cctower.common.enums.ApiDataEnum;
import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.component.BillingComponent;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.dto.RenewCarPayDTO;
import com.nuoze.cctower.pojo.entity.*;
import com.nuoze.cctower.pojo.vo.ParkingRecordVO;
import com.nuoze.cctower.pojo.vo.RenewCarRecordVO;
import com.nuoze.cctower.pojo.vo.RenewCarVO;
import com.nuoze.cctower.service.BillingService;
import com.nuoze.cctower.service.CarService;
import com.nuoze.cctower.service.ParkingRecordService;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.nuoze.cctower.common.constant.Constant.*;

/**
 * @author JiaShun
 * @date 2019-03-15 00:24
 */
@Slf4j
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarDAO carDAO;
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private IdComponent idComponent;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private ParkingRecordService parkingRecordService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private MemberCarDAO memberCarDAO;
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private MqSendComponent mqSendComponent;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private BusinessTransactionRecordDAO businessTransactionRecordDAO;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private RenewRecordDAO renewRecordDAO;
    @Autowired
    private PaymentComponent paymentComponent;
    @Autowired
    private BillingComponent billingComponent;

    @Override
    public List<CarDTO> list(Map<String, Object> map) {
        Long userId = idComponent.getUserId();
        List<Long> roleIds = userRoleDAO.listRoleByUserId(userId);
        if (roleIds.contains(BUSINESS_ROLE_ID)) {
            map.put("createId", userId);
        }
        List<Car> list = carDAO.list(map);
        List<CarDTO> carDTOList = new ArrayList<>();
        for (Car car : list) {
            CarDTO carDTO = new CarDTO();
            BeanUtils.copyProperties(car, carDTO);
            if (car.getMonthlyParkingStart() != null && car.getMonthlyParkingEnd() != null) {
                String beginDate = DateUtils.toTimeString(car.getMonthlyParkingStart());
                String endDate = DateUtils.toTimeString(car.getMonthlyParkingEnd());
                carDTO.setBeginDate(beginDate);
                carDTO.setEndDate(endDate);
            }
            if (car.getParkingId() != null) {
                String parkingName = parkingDAO.selectByPrimaryKey(car.getParkingId()).getName();
                carDTO.setParkingName(parkingName);
            }
            carDTOList.add(carDTO);
        }
        return carDTOList;
    }

    @Override
    public int count(Map<String, Object> map) {
        return carDAO.count(map);
    }

    @Override
    public Car findById(Long id) {
        return carDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(CarDTO dto) {
        Car car = dtoToCar(dto);
        Long userId = ShiroUtils.getUserId();
        car.setCreateId(userId);
        car.setCreateTime(new Date());
        car.setUpdateTime(new Date());
        int i =  carDAO.insert(car);
        if (1 == car.getParkingType()) {
            Car vo = carDAO.findByParkingIdAndCarNumber(car.getParkingId(), car.getNumber());
            mqSendComponent.sendRentCar(ApiDataEnum.ADD, vo);
        }
        return i;
    }

    @Override
    public int update(CarDTO dto) {
        Car car = dtoToCar(dto);
        car.setUpdateTime(new Date());
        if (1 == car.getParkingType()) {
            mqSendComponent.sendRentCar(ApiDataEnum.UPDATE, carDAO.selectByPrimaryKey(car.getId()));
        }
        return carDAO.updateByPrimaryKeySelective(car);
    }

    @Override
    public int remove(Long id) {
        Car car = carDAO.selectByPrimaryKey(id);
        if (1 == car.getParkingType()) {
            mqSendComponent.sendRentCar(ApiDataEnum.DELETE, car);
        }
        return carDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return carDAO.batchRemove(ids);
    }

    @Override
    public ParkingRecordVO parkingRecordDetail(String carNumber) {
        ParkingRecord parkingRecord = parkingRecordService.findByCarNumberAndStatus(carNumber);
        ParkingRecordVO parkingRecordVO = new ParkingRecordVO();
        String inTime = DateUtils.formatDateTime(parkingRecord.getInTime());
        String outTime = DateUtils.formatDateTime(new Date());
        Long parkingId = parkingRecord.getParkingId();
        Billing billing = billingService.findByParkingId(parkingId);
        Integer takeMinutes = parkingRecord.getCostTime();
        BigDecimal cost = parkingRecord.getCost();
        if (cost == null) {
            takeMinutes = (int) ChronoUnit.MINUTES.between(parkingRecord.getInTime().toInstant(), Instant.now());
            Car car = carDAO.findByParkingIdAndCarNumber(parkingId, carNumber);
            if (car != null && BUSINESS_CAR == car.getParkingType() && BUSINESS_NORMAL_CAR == car.getStatus()) {
                int freeTime = car.getFreeTime();
                if (freeTime >= takeMinutes) {
                    cost = EMPTY_MONEY;
                } else {
                    cost = billingComponent.cost(takeMinutes, parkingId, carNumber);
                    if (billing.getWechatDiscount() != null) {
                        cost = paymentComponent.wechatDiscounted(cost, billing.getWechatDiscount());
                    }
                }
            } else {
                Integer freeTime = billing.getFreeTime();
                if (freeTime != null && freeTime >= takeMinutes) {
                    cost = EMPTY_MONEY;
                } else {
                    cost = billingComponent.cost(takeMinutes, parkingId, null);
                }
            }
        }
        parkingRecordVO.setRecordId(parkingRecord.getId());
        parkingRecordVO.setInTime(inTime);
        parkingRecordVO.setOutTime(outTime);
        parkingRecordVO.setTakeMinutes(takeMinutes);
        parkingRecordVO.setCost(cost.toString());
        return parkingRecordVO;
    }

    @Override
    public Result costByNumber(String carNumber) {
        ParkingRecord parkingRecord = parkingRecordService.findByCarNumberAndStatus(carNumber);
        if (parkingRecord == null) {
            return ResponseResult.fail(201, "未检测到车辆进入CCTower停车场");
        }
        Car car = carDAO.findByParkingIdAndCarNumber(parkingRecord.getParkingId(), carNumber);
        if (car != null) {
            int parkingType = car.getParkingType();
            switch (parkingType) {
                case MONTHLY_CAR:
                    if (new Date().before(car.getMonthlyParkingEnd())) {
                        return ResponseResult.fail(201, "你是长租车辆，无需付款");
                    }
                    break;
                case VIP_CAR:
                    return ResponseResult.fail(201, "你是VIP车辆，无需付款");
                case BUSINESS_CAR:
                    if (BUSINESS_NORMAL_CAR == car.getStatus()) {
                        int takeMinutes = (int) ChronoUnit.MINUTES.between(parkingRecord.getInTime().toInstant(), Instant.now());
                        if (car.getFreeTime() > takeMinutes) {
                            return ResponseResult.fail(201, "你是商户车辆，免费时长未用完，无需付款");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        //付款时间不等于空并且支付成功    支付状态：0：未支付 1：支付成功
        if (parkingRecord.getPayTime() != null && parkingRecord.getPayStatus() == 1) {
            Long parkingId = parkingRecord.getParkingId();
            Billing billing = billingService.findByParkingId(parkingId);
            int min = billing.getPaidFreeTime();
            int remainingMin = (int) ChronoUnit.MINUTES.between(parkingRecord.getPayTime().toInstant(), Instant.now());
            if (remainingMin < min) {
                return ResponseResult.fail(201, "已付款，你还有" + (min - remainingMin) + "分钟的时间离开停车场");
            }
        }
        return ResponseResult.success();
    }

    @Override
    public String[] wxCarList(String openId) {
        Member member = memberDAO.selectByOpenId(openId);
        List<String> strings = new ArrayList<>();
        if (member != null) {
            List<Long> list = memberCarDAO.selectCarIdByMemberId(member.getId());
            if (list != null && !list.isEmpty()) {
                for (Long carId : list) {
                    String carNumber = carDAO.selectByPrimaryKey(carId).getNumber();
                    strings.add(carNumber);
                }
            }
        }
        return strings.toArray(new String[0]);
    }

    @Override
    public void addWxCar(Member member, Car car) {
        car.setParkingType(0);
        car.setCreateTime(new Date());
        car.setUpdateTime(new Date());
        carDAO.insert(car);
        Long carId = carDAO.selectByNumberAndOpenId(car.getNumber(), car.getOpenId());
        Long memberId =member.getId();
        MemberCar memberCar = new MemberCar();
        memberCar.setCarId(carId);
        memberCar.setMemberId(memberId);
        memberCar.setCreateTime(new Date());
        memberCar.setUpdateTime(new Date());
        memberCarDAO.insert(memberCar);
    }

    @Override
    public void deleteWxCar(Car car) {
        Long carId = carDAO.selectByNumberAndOpenId(car.getNumber(), car.getOpenId());
        List<MemberCar> list = memberCarDAO.selectByCarId(carId);
        if (list != null && !list.isEmpty()) {
            for (MemberCar mc : list) {
                this.memberCarDAO.deleteByPrimaryKey(mc.getId());
            }
        }
        this.carDAO.deleteByPrimaryKey(carId);
    }

    @Override
    public int saveBusinessCar(CarDTO dto) {
        Car car = new Car();
        BeanUtils.copyProperties(dto, car);
        Integer freeTime = dto.getFreeTime();
        Long parkingId = dto.getParkingId();
        BigDecimal cost = billingComponent.cost(freeTime, parkingId, null);
        Long userId = ShiroUtils.getUserId();
        User user = userDAO.selectByPrimaryKey(userId);
        BigDecimal balance = user.getBalance().subtract(cost);
        user.setBalance(balance);
        user.setUpdateTime(new Date());
        userDAO.updateByPrimaryKeySelective(user);
        BusinessTransactionRecord businessTransactionRecord = new BusinessTransactionRecord();
        businessTransactionRecord.setUserId(userId);
        businessTransactionRecord.setAmount(cost);
        businessTransactionRecord.setBalance(balance);
        businessTransactionRecord.setCarNumber(dto.getNumber());
        businessTransactionRecord.setType(0);
        businessTransactionRecord.setCreateTime(new Date());
        businessTransactionRecordDAO.insert(businessTransactionRecord);
        car.setCreateId(userId);
        car.setCost(cost);
        car.setCreateTime(new Date());
        car.setUpdateTime(new Date());
        return carDAO.insert(car);
    }

    @Override
    public Result renewByNumber(String carNumber) {
        //判断是不是月租车
        Car car = carDAO.findByCarNumberAndParkingType(carNumber, MONTHLY_CAR);
        if (car == null) {
            return ResponseResult.fail(201, "此车辆未开通长租服务，请联系停车场管理员开通");
        }
        return ResponseResult.success();
    }

    @Override
    public RenewCarVO renewCarDetail(String renewCarNumber) {
        Car car = carDAO.findByCarNumberAndParkingType(renewCarNumber, MONTHLY_CAR);
        RenewCarVO vo = new RenewCarVO();
        Long parkingId = car.getParkingId();
        String parkingName = parkingDAO.selectByPrimaryKey(parkingId).getName();
        vo.setCarId(car.getId());
        vo.setParkingId(parkingId);
        vo.setParkingName(parkingName);
        String startDate = DateUtils.formatMonthly(car.getMonthlyParkingStart());
        String endDate = DateUtils.formatMonthly(car.getMonthlyParkingEnd());
        vo.setMonthlyParkingStart(startDate);
        vo.setMonthlyParkingEnd(endDate);
        return vo;
    }

    @Override
    public RenewCarVO findMonthlyPriceByParkingId(Long parkingId) {
        Billing billing = billingService.findByParkingId(parkingId);
        BigDecimal monthlyPrice = billing.getMonthlyPrice();
        RenewCarVO vo = new RenewCarVO();
        if (monthlyPrice != null) {
            vo.setMonthlyPrice(monthlyPrice.toString());
        }
        return vo;
    }

    @Override
    public WxPayMpOrderResult renewCarPrePay(RenewCarPayDTO dto, HttpServletRequest request) {
        String openId = dto.getOpenId();
        BigDecimal actualPrice = new BigDecimal(dto.getCost());
        WxPayUnifiedOrderRequest orderRequest = paymentComponent.buildWxPayReq(openId, actualPrice, request, null);
        orderRequest.setBody("长租车续费");
        WxPayMpOrderResult result = null;
        try {
            result = wxPayService.createOrder(orderRequest);
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");
            RenewRecord renewRecord = new RenewRecord();
            renewRecord.setOpenId(dto.getOpenId());
            renewRecord.setCost(actualPrice);
            renewRecord.setParkingId(dto.getParkingId());
            renewRecord.setCarNumber(carDAO.selectByPrimaryKey(dto.getCarId()).getNumber());
            renewRecord.setOrderSn(orderRequest.getOutTradeNo());
            renewRecord.setPrepayId(prepayId);
            renewRecord.setPayStatus(0);
            renewRecord.setMonthCount(dto.getMonthCount());
            renewRecord.setCreateTime(new Date());
            renewRecordDAO.insertSelective(renewRecord);
        } catch (WxPayException e) {
            log.error("[RENEW CAR ] pre pay has exception: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public List<RenewCarRecordVO> renewCarRecord(Map<String, Object> map) {
        List<RenewRecord> list = renewRecordDAO.findByOpenId(map);
        List<RenewCarRecordVO> renewCarRecordVOS = null;
        if (!CollectionUtils.isEmpty(list)) {
            renewCarRecordVOS = new ArrayList<>();
            for (RenewRecord record : list) {
                RenewCarRecordVO vo = new RenewCarRecordVO();
                String parkingName = parkingDAO.selectByPrimaryKey(record.getParkingId()).getName();
                vo.setParkingCar(parkingName + ": " + record.getCarNumber());
                vo.setCost(record.getCost().toString());
                vo.setTime(DateUtils.formatDateTime(record.getCreateTime()));
                renewCarRecordVOS.add(vo);
            }
        }
        return renewCarRecordVOS;
    }

    private Car dtoToCar(CarDTO dto) {
        Car car = new Car();
        BeanUtils.copyProperties(dto, car);
        if (dto.getBeginDate() != null && dto.getEndDate() != null) {
            try {
                Date monthlyParkingStart = DateUtils.toDateTime(dto.getBeginDate());
                Date monthlyParkingEnd = DateUtils.toDateTime(dto.getEndDate());
                car.setMonthlyParkingStart(monthlyParkingStart);
                car.setMonthlyParkingEnd(monthlyParkingEnd);
            } catch (Exception e){
                log.error("Time format error: {}", e.getMessage());
            }
        }
        return car;
    }

    @Override
    public int saveVipCar(Car car) {
        car.setParkingType(2);
        car.setStatus(1);
        car.setInfieldPermission(1);
        car.setCreateTime(new Date());
        car.setUpdateTime(new Date());
        return carDAO.insert(car) ;
    }
}
