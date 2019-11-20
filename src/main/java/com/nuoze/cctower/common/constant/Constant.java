package com.nuoze.cctower.common.constant;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 常量
 *
 * @author JiaShun
 * @date 2019-03-01 01:57
 */
public class Constant {
    public static final String CACHE_TYPE_REDIS = "redis";

    public static final int MAP_INIT_CAPACITY = 16;

    /**
     * 自动去除表前缀
     */
    public static final String AUTO_REMOVE_PRE = "true";

    /**
     * ccotwer mq config
     */
    public static final String CCTOWER_FANOUT_EXCHANGE = "cctowerExchange";
    public static final String ZHONG_LIANG_QUEUE = "zhongliang";
    public static final String CS_ZHUBAOCHENG_QUEUE = "cs_zhubaocheng";


    /**
     * 车辆出场状态
     * 是否出场 0：否 1：是 2：待出场
     */
    public static final int NOT_LEAVE = 0;
    public static final int LEAVE_YET = 1;
    public static final int READY_TO_LEAVE = 2;

    /**
     * 车辆类型
     * 0:临时车 1:包月 2:永久月租 3:商户车辆 4:特殊车辆 5:单次VIP
     */
    public static final int TEMPORARY_CAR = 0;
    public static final int MONTHLY_CAR = 1;
    public static final int VIP_CAR = 2;
    public static final int BUSINESS_CAR = 3;
    public static final int SPECIAL_CAR = 4;
    public static final int SINGLEVIP_CAR = 5;

    /**
     * 车辆状态
     * 状态: 0:禁用 1:正常
     */
    public static final int BUSINESS_FORBIDDEN_CAR = 0;
    public static final int BUSINESS_NORMAL_CAR = 1;
    /**
     * 支付状态
     * 0:未支付 1：支付成功
     */
    public static final int PAY_STATUS_FORBIDDEN = 0;
    public static final int PAY_STATUS_NORMAL = 1;

    /**
     * mq信息类型
     */
    public static final String GO_OUT_CAR_TYPE = "GO_OUT_CAR";
    public static final String RENT_CAR_TYPE = "RENT_CAR";
    public static final String BILLING_CAR_TYPE = "BILLING_CAR";

    public static final List EMPTY_LIST = new CopyOnWriteArrayList<>();

    /**
     * role id
     */
    public static final Long SUPER_ROLE_ID = 1L;
    public static final Long ADMIN_ROLE_ID = 2L;
    public static final Long CUSTORMER_ROLE_ID = 3L;
    public static final Long BUSINESS_ROLE_ID = 4L;

    public static final BigDecimal EMPTY_MONEY = new BigDecimal("0.00");
    public static final BigDecimal EMPTY_MONEY_FEN = new BigDecimal("0.01");
    public static final BigDecimal MINIMUM_SERVICE_CHARGE = new BigDecimal("0.10");
    public static final Integer ONE_HUNDRED = 100;
    public static final Integer PARKING_TRADING_RECORD_INCOME_TYPE = 1;
    public static final Integer PARKING_TRADING_RECORD_EXPEND_TYPE = 0;

    /**
     * 付款类型
     * pay type 0：线下 1：微信 2：支付宝 3:vip
     * 4：月租车、5：商户车辆、6：无需支付
     */
    public static final Integer PAYMENT_OFFLINE = 0;
    public static final Integer PAYMENT_WECHAT = 1;
    public static final Integer PAYMENT_ZHIFUBAO = 2;
    public static final Integer PAYMENT_VIP = 3;
    public static final Integer PAYMENT_MONTHLY = 4;
    public static final Integer PAYMENT_VBUSINESS = 5;
    public static final Integer PAYMENT_NOT = 6;
}
