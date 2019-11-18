package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * mq_config
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class MqConfig implements Serializable {
    private Integer id;

    private Long parkingId;

    private String queue;

    private static final long serialVersionUID = 1L;

}