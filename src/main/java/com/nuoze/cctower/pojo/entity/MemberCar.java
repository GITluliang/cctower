package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * member_car
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class MemberCar implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * customer_user_id
     */
    private Long memberId;

    /**
     * car_id
     */
    private Long carId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}