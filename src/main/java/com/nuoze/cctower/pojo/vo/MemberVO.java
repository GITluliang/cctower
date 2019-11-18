package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MemberVO {

    private Long id;
    private BigDecimal balance;
    private String openId;
    private String phone;
    private String nickName;
    private String avatarUrl;
    private String province;
    private String city;
    private String createTime;
    private String updateTime;

}
