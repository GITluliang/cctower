package com.nuoze.cctower.pojo.dto;

import com.nuoze.cctower.pojo.entity.Passageway;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-03-17 21:56
 */
@Data
@Accessors(chain = true)
public class PassagewayDTO extends Passageway {

    private String parkingName;

}
