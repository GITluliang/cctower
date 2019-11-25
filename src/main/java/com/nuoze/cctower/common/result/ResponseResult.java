package com.nuoze.cctower.common.result;

import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.pojo.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;

import static com.nuoze.cctower.common.constant.Constant.*;

/**
 * @author JiaShun
 * @date 2019-01-15 00:37
 */
public class ResponseResult {

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("SUCCESS");
        result.setData(data);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMessage());
        return result;
    }

    public static R addCarCheck(Car car) {
        if (MONTHLY_CAR == car.getParkingType()) {
            return R.error(201, "此车牌号月租车中已存在");
        } else if (VIP_CAR == car.getParkingType()) {
            return R.error(201, "此车牌号永久月租中已存在");
        }else if (BUSINESS_CAR == car.getParkingType()) {
            return R.error(201, "此车牌号商户车中已存在");
        }else if (SPECIAL_CAR == car.getParkingType()) {
            return R.error(201, "此车牌号特殊车俩中已存在");
        }else if (SINGLEVIP_CAR == car.getParkingType()) {
            return R.error(201, "此车牌号VIP车俩中已存在");
        }else if (TIMECOUPON_CAR == car.getParkingType()) {
            return R.error(201, "此车牌号时长劵商户车辆中已存在");
        } else {
            return R.error(201, "此停车场已有此车牌号，不能重复添加");
        }
    }

}
