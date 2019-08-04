package com.nuoze.cctower.common.enums;

/**
 * @author JiaShun
 * @date 2019-05-15 23:44
 */
public enum IncomeType {

    /**
     * 充值类型
     */
    PARKING_CHARGE("停车费"),

    RENT_RENEW("月租充值");

    private String value;

    IncomeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
