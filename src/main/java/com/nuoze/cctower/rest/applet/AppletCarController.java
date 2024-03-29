package com.nuoze.cctower.rest.applet;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.pojo.dto.RenewCarPayDTO;
import com.nuoze.cctower.pojo.vo.RenewCarVO;
import com.nuoze.cctower.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

import static com.nuoze.cctower.common.result.ResultEnum.INVALID_PARAM;

/**
 * @author JiaShun
 * @date 2019-03-15 01:23
 * 小程序车辆
 */
@Slf4j
@RestController
@RequestMapping("applet/car")
public class AppletCarController {

    @Autowired
    private CarService carService;
    @Autowired
    private PaymentComponent paymentComponent;

    /**
     * 根据车牌查询费用
     *
     * @param carNumber
     * @return
     */
    @GetMapping("cost-by-number")
    public Result costByNumber(@RequestParam("carNumber") String carNumber) {
        log.info("[Applet Car Controller] cost-by-number receive car number: {}", carNumber);
        return carService.costByNumber(carNumber);
    }

    /**
     * 获取停车记录详情
     *
     * @param carNumber
     * @return
     */
    @GetMapping("parking-record/detail")
    public Result parkingRecordDetail(@RequestParam("carNumber") String carNumber) {
        log.info("[Applet Car Controller] parking-record/detail receive car number: {}", carNumber);
        return ResponseResult.success(carService.parkingRecordDetail(carNumber));
    }

    /**
     * 更新车辆
     *
     * @param carNumber
     * @return
     */
    @GetMapping("renew-by-number")
    public Result renewCar(@RequestParam("carNumber") String carNumber) {
        return carService.renewByNumber(carNumber);
    }

    /**
     * 更新车辆详细信息
     *
     * @param renewCarNumber
     * @return
     */
    @GetMapping("renew-car/detail")
    public Result renewCarDetail(@RequestParam("renewCarNumber") String renewCarNumber) {
        return ResponseResult.success(carService.renewCarDetail(renewCarNumber));
    }

    /**
     * 价格信息
     *
     * @param parkingId
     * @return
     */
    @GetMapping("renew-car/price-info")
    public Result priceInfo(@RequestParam("parkingId") Long parkingId) {
        RenewCarVO renewCarVO = carService.findMonthlyPriceByParkingId(parkingId);
        if (renewCarVO.getMonthlyPrice() == null) {
            return ResponseResult.fail(201, "此停车场不对外提供月租付费");
        }
        return ResponseResult.success(renewCarVO);
    }

    /**
     * 计算
     *
     * @param json
     * @return
     */
    @PostMapping("renew-car/calculate")
    public Result calculate(@RequestBody JSONObject json) {
        Integer monthCount = null;
        String monthlyPrice = null;
        try {
            monthCount = json.getInteger("monthCount");
            monthlyPrice = json.getString("monthlyPrice");
        } catch (Exception e) {
            log.error("[APPLET CAR CONTROLLER] calculate has exception: monthCount:{}, exception: {}", monthCount, e.getMessage());
            return ResponseResult.fail(201, "月数只能为整数");
        }
        BigDecimal cost = new BigDecimal(monthlyPrice).multiply(new BigDecimal(monthCount));
        return ResponseResult.success(cost);
    }

    /**
     * 小程序月租车续费
     *
     * @param dto
     * @param request
     * @return
     */
    @PostMapping("renew-car/prepay")
    public WxPayMpOrderResult renewCarPrePay(@RequestBody RenewCarPayDTO dto, HttpServletRequest request) {
        return carService.renewCarPrePay(dto, request);
    }

    /**
     * 更新车辆记录
     *
     * @param json
     * @return
     */
    @PostMapping("renew-car/record")
    public Result renewCarRecord(@RequestBody JSONObject json) {
        Map<String, Object> map = paymentComponent.paymentRecord(json);
        if (map == null) {
            return ResponseResult.fail(INVALID_PARAM);
        }
        return ResponseResult.success(carService.renewCarRecord(map));
    }
}
