package com.nuoze.cctower.rest.mp;

import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.AuthUtil;
import com.nuoze.cctower.common.util.WxUtils;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.service.ParkingRecordService;
import com.nuoze.cctower.service.ParkingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 扫码支付
 *
 * @Author luliang
 * @Date 2019-10-14 16:10
 */
@Slf4j
@Controller
@RequestMapping("mp/watchPay")
public class SweepCodeRedController {
    private String prefix = "h5/sweepCodeRed/";
    @Autowired
    private ParkingRecordService parkingRecordService;
    @Autowired
    private WxUtils wxUtils;
    @Autowired
    private ParkingService parkingService;

    /**
     * 通过停车场和通道，获取正在出厂车辆
     * 获取code
     *
     * @param parkingId
     * @param exitId
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("sweepCodeRed")
    public String sweepCodeRed(Long parkingId, Long exitId, String code, Model model) {
        if (StringUtils.isEmpty(code)) {
            return prefix + "code";
        }
        if (StringUtils.isNotEmpty(code)) {
            Map<String, String> map = wxUtils.getUserInfoAccessToken(code);
            model.addAttribute("openId", map.get("openid"));
        }
        model.addAttribute("parkingRecord", parkingRecordService.findByParkingIdAndIp(parkingId, exitId));
        return prefix + "index";
    }

    /**
     * 支付成功跳转
     *
     * @param recordId
     * @param model
     * @return
     */
    @RequestMapping("forward")
    public String forward(Long recordId, Model model) {
        log.info("[支付成功，查询跳转] Applet forward: {}", recordId);
        ParkingRecord parkingRecord = parkingRecordService.findById(recordId);
        model.addAttribute("parkingRecord", parkingRecord);
        model.addAttribute("payTime", DateUtils.formatDateTime(parkingRecord.getPayTime()));
        model.addAttribute("parking", parkingService.findById(parkingRecord.getParkingId()));
        return prefix + "success";
    }

    //校验access_token
    @RequestMapping("token")
    public void accessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();
        if (AuthUtil.checkSignature(signature, timestamp, nonce)) {
            // 如果校验成功，将得到的随机字符串原路返回
            out.print(echostr);
        }
    }
}
