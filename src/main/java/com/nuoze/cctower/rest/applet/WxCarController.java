package com.nuoze.cctower.rest.applet;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.service.CarService;
import com.nuoze.cctower.service.applet.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nuoze.cctower.common.result.ResultEnum.INVALID_PARAM;
import static com.nuoze.cctower.common.result.ResultEnum.PERMISSION_DENIED;

/**
 * @author JiaShun
 * @date 2019-04-13 16:32
 * 微信车辆
 */
@Slf4j
@RestController
@RequestMapping("applet/car")
public class WxCarController {

    @Autowired
    private CarService carService;
    @Autowired
    private CarDAO carDAO;
    @Autowired
    private MemberService memberService;

    @PostMapping("list")
    public Result list(@RequestBody Car car) {
        String openId = car.getOpenId();
        if (StringUtils.isEmpty(openId)) {
            return ResponseResult.fail(INVALID_PARAM);
        }
        return ResponseResult.success(carService.wxCarList(openId));
    }

    @PostMapping("add")
    public Result add(@RequestBody Car car) {
        if (checkParam(car) != null) {
            return checkParam(car);
        }
        Member member = memberService.findByOpenId(car.getOpenId());
        if (member != null) {
            if (carDAO.findByCarNumberAndParkingType(car.getNumber(), 0) != null) {
                return ResponseResult.fail(201, "此车牌号已存在");
            }
            this.carService.addWxCar(member, car);
            return ResponseResult.success();
        }
        return ResponseResult.fail(PERMISSION_DENIED);
    }

    @PostMapping("delete")
    public Result delete(@RequestBody Car car) {
        if (checkParam(car) != null) {
            return checkParam(car);
        }
        this.carService.deleteWxCar(car);
        return ResponseResult.success();
    }

    private Result checkParam(Car car) {
        String carNumber = car.getNumber();
        String openId = car.getOpenId();
        if (StringUtils.isEmpty(carNumber) || StringUtils.isEmpty(openId)) {
            return ResponseResult.fail(INVALID_PARAM);
        }
        return null;
    }
}
