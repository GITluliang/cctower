package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.service.CarService;
import com.nuoze.cctower.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;
import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;
import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;


/**
 * 时长劵车辆
 *
 * @author JiaShun
 * @date 2019-03-15 00:19:28
 */

@Controller
@RequestMapping("/sys/car/timeCoupon")
public class TimeCouponCarController {

    private String prefix = "system/car/timeCoupon/";

    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;
    @Autowired
    private IdComponent idComponent;

    @GetMapping()
    @RequiresPermissions("sys:car:timeCoupon")
    String car() {
        return prefix + "car";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:timeCoupon")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params.put("createId", idComponent.getUserId());
        params.put("createId", idComponent.getUserId());
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        params.put("parkingType", 6);
        Query query = new Query(params);
        List<CarDTO> carList = carService.list(query);
        int total = carService.count(query);
        return new PageUtils(carList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:car:timeCoupon:add")
    String add() {
        return prefix + "add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:car:edit")
    String edit(@PathVariable Long id, Model model) {
        Car car = carService.findById(id);
        model.addAttribute("car", car);
        return prefix + "edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:car:timeCoupon:add")
    public R save(CarDTO dto) {
        User user = userService.findById(getUserId());
        if (user.getTimeCoupon() <= 0) {
            return R.error(201, "时长劵已用完 ,请联系物业充值");
        }
        if (user.getTimeCoupon() - dto.getTimeCoupon() < 0) {
            return R.error(201, "时长劵还剩" + user.getTimeCoupon() + "张 ,请重新输入");
        }
        dto.setParkingId(user.getParkingId());
        Car car = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
        if (car != null) {
            return ResponseResult.addCarCheck(car);
        }
        return carService.saveTimeCouponCar(dto) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:car:timeCoupon:edit")
    public R update(CarDTO dto) {
        return carService.update(dto) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:car:timeCoupon:remove")
    public R remove(Long id) {
        return carService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:car:timeCoupon:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        return carService.batchRemove(ids) > 0 ? R.ok() : R.error();
    }

}
