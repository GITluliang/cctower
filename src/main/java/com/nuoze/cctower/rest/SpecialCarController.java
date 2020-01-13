package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;
import static com.nuoze.cctower.common.constant.Constant.SPECIAL_CAR;

/**
 * 特殊车辆
 *
 * @Authror luliang
 * @Date 2019-09-18 16:22
 */
@Slf4j
@Controller
@RequestMapping("/sys/car/special")
public class SpecialCarController {
    private String prefix = "system/car/special/";

    @Autowired
    private CarService carService;
    @Autowired
    private IdComponent idComponent;
    @Autowired
    private CarDAO carDAO;

    @GetMapping()
    @RequiresPermissions("sys:car:special")
    String car(Model model) {
        model.addAttribute("parkingList", idComponent.getParkingList());
        return prefix + "car";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:special")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        log.info("[LONG CAR CONTROLLER] check special car list, the params: {}", String.valueOf(params));
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        params.put("parkingType", SPECIAL_CAR);
        Query query = new Query(params);
        List<CarDTO> carList = carService.listLike(query);
        int total = carService.count(query);
        return new PageUtils(carList, total);
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:car:special:edit")
    String edit(@PathVariable Long id, Model model) {
        Car car = carService.findById(id);
        model.addAttribute("car", car);
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "edit";
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:car:special:add")
    String add(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "add";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:car:special:add")
    public R save(CarDTO dto) {
        if (dto.getNumber() != null) {
            Car carNumber = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
            if (carNumber != null) {
                carService.remove(carNumber.getId());
            }
        }
        return carService.save(dto) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:car:special:edit")
    public R update(CarDTO dto) {
        Car car = carDAO.selectByPrimaryKey(dto.getId());
        if (car != null) {
            if (dto.getNumber() != null) {
                if (!dto.getNumber().equalsIgnoreCase(car.getNumber())) {
                    Car carNumber = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
                    if (carNumber != null) {
                        carService.remove(carNumber.getId());
                    }
                }
            }
        }
        return carService.update(dto) > 0? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:car:special:remove")
    public R remove(Long id) {
        return carService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:car:special:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        carService.batchRemove(ids);
        return R.ok();
    }
}
