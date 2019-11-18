package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-03 22:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RoleVO extends Role {

    private List<Long> menuIds;
}
