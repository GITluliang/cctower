package com.nuoze.cctower.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-04-15 11:47
 */
@Data
@AllArgsConstructor
public class ApiCheckCarVO {

    /**
     * 是否月租：0：否 1：是
     */
    private Integer status;
    /**
     * 车辆状态：0：禁用 1：正常 2：过期
     */
    private Integer type;
    /**
     * 内场权限：0：无 1：有
     */
    private Integer infieldPermission;

}
