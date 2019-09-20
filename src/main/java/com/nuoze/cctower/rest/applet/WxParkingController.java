package com.nuoze.cctower.rest.applet;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.applet.WxParkingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.nuoze.cctower.common.result.ResultEnum.INVALID_PARAM;

/**
 * 小程序停车场
 * @Authror luliang
 * @Date 2019-09-19 20:57
 */
@RestController
@RequestMapping("applet/parking")
public class WxParkingController {

    @Autowired
    private WxParkingService parkingService ;

    @PostMapping("list")
    public Result listByUser(@RequestBody Parking parking) {
        Long userId = parking.getUserId();
        if (userId == 0) {
            return ResponseResult.fail(INVALID_PARAM);
        }
        return ResponseResult.success(parkingService.findParkingByUser(  userId));
    }
}
