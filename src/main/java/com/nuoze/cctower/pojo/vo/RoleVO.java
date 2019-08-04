package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-03 22:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends Role {

    private List<Long> menuIds;
}
