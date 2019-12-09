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
import com.nuoze.cctower.service.ParkingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.nio.cs.US_ASCII;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.nuoze.cctower.common.constant.Constant.*;
import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;

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
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private BillingDAO billingDAO;

    @Override
    public List<CarDTO> list(Map<String, Object> map) {
        return mapList(carDAO.list(map));
    }

    @Override
    public List<CarDTO> listLike(Map<String, Object> map) {
        List<Long> roleIds = userRoleDAO.listRoleByUserId(idComponent.getUserId());
        if (roleIds.contains(BUSINESS_ROLE_ID)) {
            map.put("createId", idComponent.getUserId());
        }
        return mapList(carDAO.listLike(map));
    }

    private List mapList(List<Car> list) {
        List<CarDTO> carDTOList = new CopyOnWriteArrayList<>();
        for (Car car : list) {
            CarDTO carDTO = new CarDTO();
            BeanUtils.copyProperties(car, carDTO);
            if (car.getMonthlyParkingStart() != null && car.getMonthlyParkingEnd() != null) {
                carDTO.setBeginDate(DateUtils.toTimeString(car.getMonthlyParkingStart())).setEndDate(DateUtils.toTimeString(car.getMonthlyParkingEnd()));
            }
            if (car.getParkingId() != null) {
                carDTO.setParkingName(parkingDAO.selectByPrimaryKey(car.getParkingId()).getName());
            }
            if (car.getFreeTime() != null && car.getParkingType() == BUSINESS_CAR) {
                carDTO.setFreeTime(car.getFreeTime() / 60);
            }
            if (car.getCreateId() != null) {
                User user = userDAO.selectByPrimaryKey(car.getCreateId());
                carDTO.setUserName(user != null ? user.getName() : "");
            }
            carDTOList.add(carDTO);
        }
        return carDTOList;
    }

    @Override
    public int countLike(Map<String, Object> map) {
        return carDAO.countLike(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return carDAO.countLike(map);
    }

    @Override
    public CarDTO findById(Long id) {
        CarDTO carDTO = new CarDTO();
        Car car = carDAO.selectByPrimaryKey(id);
        BeanUtils.copyProperties(car, carDTO);
        if (car.getMonthlyParkingStart() != null && car.getMonthlyParkingEnd() != null) {
            carDTO.setBeginDate(DateUtils.toTimeString(car.getMonthlyParkingStart())).setEndDate(DateUtils.toTimeString(car.getMonthlyParkingEnd()));
        }
        Parking parking = parkingDAO.selectByPrimaryKey(car.getParkingId());
        if (parking != null) {
            carDTO.setParkingName(parking.getName());
        }
        if (car.getFreeTime() != null && car.getParkingType() == BUSINESS_CAR) {
            carDTO.setFreeTime(car.getFreeTime() / 60);
        }
        if (car.getCreateId() != null) {
            User user = userDAO.selectByPrimaryKey(car.getCreateId());
            carDTO.setUserName(user != null ? user.getName() : "");
        }
        return carDTO;
    }

    @Override
    public int save(CarDTO dto) {
        Long userId = getUserId();
        Car car = dtoToCar(dto).setCreateId(userId).setCreateTime(new Date()).setUpdateTime(new Date()).setUuid(UUID.randomUUID().toString().replace("-", ""));
        int i = carDAO.insert(car);
        if (1 == car.getParkingType()) {
            Car vo = carDAO.findByParkingIdAndCarNumber(car.getParkingId(), car.getNumber());
            mqSendComponent.sendRentCar(ApiDataEnum.ADD, vo);
        }
        return i;
    }

    @Override
    public int update(CarDTO dto) {
        Car car = dtoToCar(dto);
        int i = carDAO.updateByPrimaryKeySelective(car.setUpdateTime(new Date()));
        if (1 == car.getParkingType()) {
            mqSendComponent.sendRentCar(ApiDataEnum.UPDATE, carDAO.selectByPrimaryKey(car.getId()));
        }
        return i;
    }

    @Override
    public int remove(Long id) {
        Car car = carDAO.selectByPrimaryKey(id);
        if (car != null) {
            if (MONTHLY_CAR == car.getParkingType()) {
                mqSendComponent.sendRentCar(ApiDataEnum.DELETE, car);
            }
            if (BUSINESS_CAR == car.getParkingType()) {
                User user = userDAO.selectByPrimaryKey(car.getCreateId());
                BigDecimal balance = user.getBalance().add(car.getCost());
                userDAO.updateByPrimaryKeySelective(user.setBalance(balance).setUpdateTime(new Date()));
                businessTransactionRecordDAO.insert(new BusinessTransactionRecord().setUserId(getUserId()).setAmount(car.getCost()).setBalance(balance).setCarNumber(car.getNumber()).setType(2).setCreateTime(new Date()).setFreeTime(car.getFreeTime() / 60).setStatus(0).setParkingId(car.getParkingId()));
            }
            if (TIMECOUPON_CAR == car.getParkingType()) {
                User user = userDAO.selectByPrimaryKey(car.getCreateId());
                Integer timeCoupon = user.getTimeCoupon() + car.getTimeCoupon();
                userDAO.updateByPrimaryKeySelective(user.setTimeCoupon(timeCoupon).setUpdateTime(new Date()));
                businessTransactionRecordDAO.insert(new BusinessTransactionRecord().setUserId(getUserId()).setAmount(BigDecimal.valueOf(car.getTimeCoupon())).setBalance(BigDecimal.valueOf(timeCoupon)).setCarNumber(car.getNumber()).setType(2).setCreateTime(new Date()).setFreeTime(car.getFreeTime() / 60).setStatus(1).setParkingId(car.getParkingId()));
            }
        }
        return carDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return carDAO.batchRemove(ids);
    }

    @Override
    public ParkingRecordVO parkingRecordDetail(String carNumber) {
        carNumber = carNumber.toUpperCase();
        ParkingRecord parkingRecord = parkingRecordService.findByCarNumberAndStatus(carNumber);
        ParkingRecordVO parkingRecordVO = new ParkingRecordVO();
        if (parkingRecord != null) {
            Long parkingId = parkingRecord.getParkingId();
            Billing billing = billingService.findByParkingId(parkingId);
            Integer takeMinutes = DateUtils.diffMin(parkingRecord.getInTime());
            BigDecimal cost = parkingRecord.getCost();
            Car car = carDAO.findByParkingIdAndCarNumber(parkingId, carNumber);
            if (car != null && car.getStatus() == BUSINESS_NORMAL_CAR) {
                if (MONTHLY_CAR == car.getParkingType() || TEMPORARY_CAR == car.getParkingType() || SINGLEVIP_CAR == car.getParkingType() || parkingRecord.getPayStatus() == 1) {
                    cost = EMPTY_MONEY;
                } else if (SPECIAL_CAR == car.getParkingType()) {
                    cost = car.getCost() == null ? new BigDecimal(5) : car.getCost();
                } else if (BUSINESS_CAR == car.getParkingType()) {
                    int freeTime = car.getFreeTime();
                    if (freeTime >= takeMinutes) {
                        cost = EMPTY_MONEY;
                    }
                }
            }else {
                Integer freeTime = billing.getFreeTime();
                if (freeTime != null && freeTime >= takeMinutes) {
                    cost = EMPTY_MONEY;
                } else {
                    cost = billingComponent.cost(takeMinutes, parkingId, null);
                }
            }
            Parking parking = parkingService.findById(parkingRecord.getParkingId());
            parkingRecordVO
                    .setParkingName(parking != null ? parking.getName() : "")
                    .setCarNumber(parkingRecord.getCarNumber())
                    .setRecordId(parkingRecord.getId())
                    .setInTime(DateUtils.formatDateTime(parkingRecord.getInTime()))
                    .setOutTime(DateUtils.formatDateTime(new Date()))
                    .setTakeMinutes(takeMinutes)
                    .setCost(String.valueOf(cost));
        } else {
            parkingRecordVO.setTakeMinutes(PARKING_TRADING_RECORD_EXPEND_TYPE);
        }
        return parkingRecordVO;
    }

    @Override
    public Result costByNumber(String carNumber) {
        carNumber = carNumber.toUpperCase();
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
                        return ResponseResult.fail(201, "你是月租车辆，无需付款");
                    }
                    break;
                case VIP_CAR:
                    return ResponseResult.fail(201, "你是长租车辆，无需付款");
                case SINGLEVIP_CAR:
                    if (BUSINESS_NORMAL_CAR == car.getStatus()) {
                        return ResponseResult.fail(201, "你是VIP车辆，无需付款");
                    }
                    break;
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
        List<String> strings = new CopyOnWriteArrayList<>();
        if (member != null) {
            List<Long> list = memberCarDAO.selectCarIdByMemberId(member.getId());
            if (list != null && !list.isEmpty()) {
                for (Long carId : list) {
                    strings.add(carDAO.selectByPrimaryKey(carId).getNumber());
                }
            }
        }
        return strings.toArray(new String[0]);
    }

    @Override
    public void addWxCar(Member member, Car car) {
        car.setNumber(car.getName().toUpperCase());
        carDAO.insert(car.setParkingType(0)
                .setCreateTime(new Date())
                .setUpdateTime(new Date()));
        Long carId = carDAO.selectByNumberAndOpenId(car.getNumber(), car.getOpenId());
        Long memberId = member.getId();
        memberCarDAO.insert(new MemberCar()
                .setCarId(carId)
                .setMemberId(memberId)
                .setCreateTime(new Date())
                .setUpdateTime(new Date()));
    }

    @Override
    public void deleteWxCar(Car car) {
        car.setNumber(car.getName().toUpperCase());
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
        dto.setNumber(dto.getNumber().toUpperCase()).setParkingType(BUSINESS_CAR);
        Car car = new Car();
        BeanUtils.copyProperties(dto, car);
        Integer freeTime = dto.getFreeTime() * 60;
        Long parkingId = dto.getParkingId();
        Billing billing = billingDAO.selectByParkingId(parkingId);
        if (billing != null) {
            freeTime = freeTime + billing.getFreeTime();
        }
        BigDecimal cost = billingComponent.cost(freeTime, parkingId, null);
        Long userId = getUserId();
        User user = userDAO.selectByPrimaryKey(userId).setUpdateTime(new Date());
        BigDecimal balance = user.getBalance().subtract(cost);
        userDAO.updateByPrimaryKeySelective(user.setBalance(balance));
        businessTransactionRecordDAO.insert(new BusinessTransactionRecord().setUserId(userId).setAmount(cost).setBalance(balance).setType(0).setCreateTime(new Date()).setCarNumber(dto.getNumber()).setFreeTime(dto.getFreeTime()).setStatus(0).setParkingId(parkingId));
        return carDAO.insert(car.setCreateId(userId).setCost(cost).setCreateTime(new Date()).setUpdateTime(new Date()).setFreeTime(dto.getFreeTime() * 60).setCreateId(userId));
    }

    @Override
    public int saveTimeCouponCar(CarDTO dto) {
        dto.setNumber(dto.getNumber().toUpperCase()).setParkingType(TIMECOUPON_CAR);
        Car car = new Car();
        BeanUtils.copyProperties(dto, car);
        Long parkingId = dto.getParkingId();
        Billing billing = billingDAO.selectByParkingId(parkingId);
        if (billing != null && billing.getCouponTime() != null) {
            Long userId = getUserId();
            User user = userDAO.selectByPrimaryKey(userId).setUpdateTime(new Date());
            Integer timeCoupon = user.getTimeCoupon() - dto.getTimeCoupon();
            userDAO.updateByPrimaryKeySelective(user.setTimeCoupon(timeCoupon));

            Integer freeTime = billing.getCouponTime() * dto.getTimeCoupon();
            businessTransactionRecordDAO.insert(new BusinessTransactionRecord().setUserId(userId).setAmount(BigDecimal.valueOf(dto.getTimeCoupon())).setBalance(BigDecimal.valueOf(timeCoupon)).setType(0).setCreateTime(new Date()).setCarNumber(dto.getNumber()).setFreeTime(freeTime).setStatus(1).setParkingId(parkingId));
            return carDAO.insert(car.setCreateId(userId).setCreateTime(new Date()).setUpdateTime(new Date()).setFreeTime(freeTime).setTimeCoupon(dto.getTimeCoupon()));
        } else {
            return 0;
        }
    }

    @Override
    public Result renewByNumber(String carNumber) {
        carNumber = carNumber.toUpperCase();
        //判断是不是月租车
        Car car = carDAO.findByCarNumberAndParkingType(carNumber, MONTHLY_CAR);
        if (car == null) {
            return ResponseResult.fail(201, "此车辆未开通长租服务，请联系停车场管理员开通");
        }
        return ResponseResult.success();
    }

    @Override
    public RenewCarVO renewCarDetail(String renewCarNumber) {
        renewCarNumber = renewCarNumber.toUpperCase();
        Car car = carDAO.findByCarNumberAndParkingType(renewCarNumber, MONTHLY_CAR);
        return new RenewCarVO()
                .setCarId(car.getId())
                .setParkingId(car.getParkingId())
                .setParkingName(parkingDAO.selectByPrimaryKey(car.getParkingId()).getName())
                .setMonthlyParkingStart(DateUtils.formatMonthly(car.getMonthlyParkingStart()))
                .setMonthlyParkingEnd(DateUtils.formatMonthly(car.getMonthlyParkingEnd()));
    }

    @Override
    public RenewCarVO findMonthlyPriceByParkingId(Long parkingId) {
        Billing billing = billingService.findByParkingId(parkingId);
        BigDecimal monthlyPrice = billing.getMonthlyPrice();
        RenewCarVO vo = new RenewCarVO();
        if (monthlyPrice != null) {
            vo.setMonthlyPrice(String.valueOf(monthlyPrice));
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
            renewRecordDAO.insertSelective(new RenewRecord().setOpenId(dto.getOpenId()).setCost(actualPrice).setParkingId(dto.getParkingId()).setCarNumber(carDAO.selectByPrimaryKey(dto.getCarId()).getNumber()).setOrderSn(orderRequest.getOutTradeNo()).setPrepayId(prepayId).setPayStatus(0).setMonthCount(dto.getMonthCount()).setCreateTime(new Date()));
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
            renewCarRecordVOS = new CopyOnWriteArrayList<>();
            for (RenewRecord record : list) {
                String parkingName = parkingDAO.selectByPrimaryKey(record.getParkingId()).getName();
                ;
                renewCarRecordVOS.add(new RenewCarRecordVO()
                        .setParkingCar(parkingName + ": " + record.getCarNumber())
                        .setCost(String.valueOf(record.getCost()))
                        .setTime(DateUtils.formatDateTime(record.getCreateTime()))
                );
            }
        }
        return renewCarRecordVOS;
    }

    private Car dtoToCar(CarDTO dto) {
        dto.setNumber(dto.getNumber().toUpperCase());
        Car car = new Car();
        BeanUtils.copyProperties(dto, car);
        car.setNumber(dto.getNumber().toUpperCase().toUpperCase());
        if (dto.getParkingType() == MONTHLY_CAR && dto.getBeginDate() != null && dto.getEndDate() != null) {
            try {
                Date monthlyParkingStart = DateUtils.toDateTime(dto.getBeginDate());
                Date monthlyParkingEnd = DateUtils.toDateTime(dto.getEndDate());
                car.setMonthlyParkingStart(monthlyParkingStart);
                car.setMonthlyParkingEnd(monthlyParkingEnd);
            } catch (Exception e) {
                log.error("Time format error: {}", e.getMessage());
            }
        }
        return car;
    }

    @Override
    public Car findByParkingIdAndCarNumber(Long parkingId, String carNumber) {
        return carDAO.findByParkingIdAndCarNumber(parkingId, carNumber.toUpperCase());
    }
}
