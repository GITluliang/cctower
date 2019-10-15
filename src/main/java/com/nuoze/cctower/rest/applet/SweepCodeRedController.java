package com.nuoze.cctower.rest.applet;

import com.nuoze.cctower.service.ParkingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("sweepCodeRed")
    public String sweepCodeRed(Long parkingId, Long exitId, Model model) {
        model.addAttribute("parkingRecord",parkingRecordService.findByParkingIdAndIp(parkingId, exitId)) ;
        return prefix + "index" ;
    }

}
