package com.nuoze.cctower.component;

import com.nuoze.cctower.common.enums.IncomeType;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

import static com.nuoze.cctower.common.constant.Constant.*;


/**
 * BillingComponent 计费组件类
 * @author JiaShun
 * @date 2019-07-07 09:14
 */
@Component
public class BillingComponent {


    @Autowired
    private BillingDAO billingDAO;
    @Autowired
    private BillingDetailDAO billingDetailDAO;
    @Autowired
    private CarDAO carDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private ParkingTradingRecordDAO tradingRecordDAO;

    /**
     * 增加交易流水
     * @param money 流水金额
     * @param parkingId 停车场ID
     * @param type 收入类型
     */
    public void addTradingRecord(BigDecimal money, Long parkingId, IncomeType type) {
        ParkingTradingRecord tradingRecord = new ParkingTradingRecord();
        tradingRecord.setAmount(money);
        tradingRecord.setType(PARKING_TRADING_RECORD_INCOME_TYPE);
        tradingRecord.setParkingId(parkingId);
        tradingRecord.setPayTime(new Date());
        tradingRecord.setIncomeType(type.name());
        tradingRecordDAO.insert(tradingRecord);

    }

    /**
     * 更新停车场账户余额
     * @param money 金额
     * @param parkingId 停车场ID
     */
    public void addAccountBalance(BigDecimal money, Long parkingId) {
        Account account = accountDAO.selectByParkingId(parkingId);
        account.setBalance(account.getBalance().add(money));
        accountDAO.updateByPrimaryKeySelective(account);
    }

    public R billingParkingIdCheck(Long parkingId, Boolean isBasicBilling) {
        if (isBasicBilling && billingDAO.selectByParkingId(parkingId) != null) {
            return R.error(201, "此停车场已有对应的基础计费，请编辑或删除后添加");
        }
        if (!isBasicBilling && billingDetailDAO.selectByParkingId(parkingId) != null) {
            return R.error(201, "此停车场已有对应的计费方式，请编辑或删除后添加");
        }
        if (!isBasicBilling && billingDAO.selectByParkingId(parkingId) == null) {
            return R.error(201, "请先完善此停车场对应的基础计费信息");
        }
        return null;
    }

    public R billingParamsCheck(BillingDetail detail) {
        switch (detail.getType()) {
            case 1:
                if (detail.getParkingId() == null || detail.getUnitType() == null || detail.getUnitPrice() == null ) {
                    return R.error(201, "参数不能为空");
                }
                break;
            case 2:
                if (detail.getParkingId() == null || detail.getOne() == null || detail.getTwo() == null || detail.getThree() == null || detail.getFour() == null ||
                        detail.getFive() == null || detail.getSix() == null || detail.getSeven() == null || detail.getEight()== null || detail.getNine() == null ||
                        detail.getTen() == null || detail.getEleven() == null || detail.getTwelve() == null || detail.getThirteen() == null || detail.getFourteen() == null ||
                        detail.getFifteen() == null || detail.getSixteen() == null || detail.getSeventeen() == null || detail.getEighteen() == null ||
                        detail.getNineteen() == null || detail.getTwenty() == null || detail.getTwentyOne() == null || detail.getTwentyTwo() == null ||
                        detail.getTwentyThree() == null || detail.getTwentyFour() == null) {
                    return R.error(201, "参数不能为空");
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 计费费用
     * @param costTime 停车时长（分钟）
     * @param parkingId 停车场id
     * @param carNumber 车牌号
     * @return BigDecimal 停车费
     */
    public BigDecimal cost(int costTime, Long parkingId, String carNumber) {
        if (carNumber != null) {
            Car car = carDAO.findByParkingIdAndCarNumber(parkingId, carNumber);
            if (car != null && BUSINESS_CAR == car.getParkingType() && BUSINESS_NORMAL_CAR == car.getStatus()) {
                costTime = costTime - car.getFreeTime();
            }
        }
        BillingDetail detail = billingDetailDAO.selectByParkingId(parkingId);
        Billing billing = billingDAO.selectByParkingId(parkingId);
        if (detail == null) {
            return EMPTY_MONEY;
        }
        //1:分时计费 2：分段计费
        switch (detail.getType()) {
            case 1:
                if (billing.getFreeTime() != null) {
                    costTime = costTime - billing.getFreeTime();
                }
                //0：分钟 1：小时
                if (detail.getUnitType() == 0) {
                    return detail.getUnitPrice().multiply(BigDecimal.valueOf(costTime));
                } else {
                    return detail.getUnitPrice().multiply(BigDecimal.valueOf(DateUtils.minToHour(costTime)));
                }
            case 2:
                int hours = DateUtils.minToHour(costTime);
                int day = (int) Math.floor(hours / 24);
                int hour = hours - (24 * day);
                BigDecimal dayCost;
                if (billing != null && billing.getDayCost() != null) {
                    dayCost = billing.getDayCost().multiply(BigDecimal.valueOf(day));
                } else {
                    dayCost = detail.getTwentyFour().multiply(BigDecimal.valueOf(day));
                }
                BigDecimal hourCost = hourToCost(detail, hour);
                return dayCost.add(hourCost);
            default:
                break;
        }
        return EMPTY_MONEY;
    }

    /**
     * 获取小时费用
     * @param detail BillingDetail  收费规则详情表
     * @param hour int 停车时长（小时）
     * @return
     */
    private BigDecimal hourToCost(BillingDetail detail, int hour) {
        switch (hour) {
            case 1:
                return detail.getOne();
            case 2:
                return detail.getTwo();
            case 3:
                return detail.getThree();
            case 4:
                return detail.getFour();
            case 5:
                return detail.getFive();
            case 6:
                return detail.getSix();
            case 7:
                return detail.getSeven();
            case 8:
                return detail.getEight();
            case 9:
                return detail.getNine();
            case 10:
                return detail.getTen();
            case 11:
                return detail.getEleven();
            case 12:
                return detail.getTwelve();
            case 13:
                return detail.getThirteen();
            case 14:
                return detail.getFourteen();
            case 15:
                return detail.getFifteen();
            case 16:
                return detail.getSixteen();
            case 17:
                return detail.getSeventeen();
            case 18:
                return detail.getEighteen();
            case 19:
                return detail.getNineteen();
            case 20:
                return detail.getTwenty();
            case 21:
                return detail.getTwentyOne();
            case 22:
                return detail.getTwentyTwo();
            case 23:
                return detail.getTwentyThree();
            case 24:
                return detail.getTwentyFour();
            default:
                break;
        }
        return EMPTY_MONEY;
    }
}

