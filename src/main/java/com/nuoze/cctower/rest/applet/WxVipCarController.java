package com.nuoze.cctower.rest.applet;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.service.CarService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nuoze.cctower.common.result.ResultEnum.INVALID_PARAM;

/**
 * 微信VIP车辆
 *
 * @Author luliang
 * @Date 2019-09-20 11:11
 */
@RestController
@RequestMapping("applet/car/vip")
public class WxVipCarController {
    @Autowired
    private CarDAO carDAO;

    @Autowired
    private CarService carService;

    @PostMapping("add")
    public Result add(@RequestBody Car car) {
        if (StringUtils.isEmpty(car.getNumber()) || car.getParkingId() == 0 || car.getCreateId() == 0) {
            return ResponseResult.fail(INVALID_PARAM);
        }
        Car carOld = carDAO.findByParkingIdAndCarNumber(car.getParkingId(), car.getNumber());
        if (carOld != null) {
            if (carOld.getParkingType() == 1) {
                return ResponseResult.fail(201, "此车牌号月租车中已存在");
            } else if (carOld.getParkingType() == 2) {
                return ResponseResult.fail(201, "此车牌号VIP车中已存在");
            } else {
                return ResponseResult.fail(201, "此车牌号已存在");
            }
        }
        return carService.saveVipCar(car) > 0 ? ResponseResult.success() : ResponseResult.fail(201, "添加失败");
    }
}
