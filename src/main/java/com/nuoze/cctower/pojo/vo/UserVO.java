package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-03 17:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {

    private List<Long> roleIds;

    private String oldPwd;

    private String newPwd;

    private String parkingName;
}
