package com.nuoze.cctower.pojo.dto;

import com.nuoze.cctower.pojo.entity.Member;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-04-07 00:23
 */
@Data
@Accessors(chain = true)
public class MemberDTO extends Member {

    private String code;
}
