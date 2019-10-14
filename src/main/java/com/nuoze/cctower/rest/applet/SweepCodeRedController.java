package com.nuoze.cctower.rest.applet;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.service.ParkingRecordService;
import com.nuoze.cctower.service.ParkingService;
import com.nuoze.cctower.service.PassagewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 扫码支付
 * @Author luliang
 * @Date 2019-10-14 16:10
 */
@Slf4j
@Controller
@RequestMapping("applet/watchPay")
public class SweepCodeRedController {
    private String prefix = "h5/sweepCodeRed/";
    @Autowired
    private ParkingRecordService parkingRecordService ;
    @Autowired
    private ParkingService parkingService ;
    @Autowired
    private PassagewayService passagewayService ;

    @RequestMapping("sweepCodeRed")
    public String sweepCodeRed() {
        return prefix + "index2" ;
    }

    @RequestMapping("get-car")
    public Result getByParkingIdAndIpCar(Long parkingId, Long exitId) {
        if(parkingService.findById(parkingId) == null) {
            return ResponseResult.fail(201, "此停车场不存在");
        }
        if(passagewayService.findById(exitId) == null) {
            return ResponseResult.fail(201, "此出口通道不存在");
        }
        return ResponseResult.success(parkingRecordService.findByParkingIdAndIp(parkingId, exitId));
    }
}
