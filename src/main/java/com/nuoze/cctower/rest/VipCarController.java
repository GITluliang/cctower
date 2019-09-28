package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.dao.CarDAO;
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

/**
 * VIP车辆
 *
 * @Authror luliang
 * @Date 2019-09-18 16:22
 */
@Slf4j
@Controller
@RequestMapping("/sys/car/vip")
public class VipCarController {
    private String prefix = "system/car/vip/";

    @Autowired
    private CarService carService;

    @Autowired
    private IdComponent idComponent;

    @Autowired
    private CarDAO carDAO;

    @GetMapping()
    @RequiresPermissions("sys:car:vip")
    String car() {
        return "system/car/vip/car";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:vip")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        log.info("[LONG CAR CONTROLLER] check long car list, the params: {}", params.toString());
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        params.put("parkingType", 2);
        Query query = new Query(params);
        List<CarDTO> carList = carService.list(query);
        int total = carService.count(query);
        return new PageUtils(carList, total);
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:car:vip:edit")
    String edit(@PathVariable Long id, Model model) {
        Car car = carService.findById(id);
        model.addAttribute("car", car);
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "edit";
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:car:vip:add")
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
    @RequiresPermissions("sys:car:vip:add")
    public R save(CarDTO dto) {
        Long parkingId = dto.getParkingId();
        String carNumber = dto.getNumber();
        Car car = carDAO.findByParkingIdAndCarNumber(parkingId, carNumber);
        if (car != null) {
            if (1 == car.getParkingType()) {
                return R.error(201, "此车牌号月租车中已存在");
            } else if (2 == car.getParkingType()) {
                return R.error(201, "此车牌号VIP车中已存在");
            } else {
                return R.error(201, "此停车场已有此车牌号，不能重复添加");
            }
        }
        return carService.save(dto) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:car:vip:edit")
    public R update(CarDTO dto) {
        carService.update(dto);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:car:vip:remove")
    public R remove(Long id) {
        return carService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:car:vip:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        carService.batchRemove(ids);
        return R.ok();
    }
}
