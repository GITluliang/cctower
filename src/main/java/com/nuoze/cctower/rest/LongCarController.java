package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.IdComponent;

import java.util.List;
import java.util.Map;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.Parking;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nuoze.cctower.service.CarService;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;


/**
 * 长租车
 *
 * @author JiaShun
 * @date 2019-03-15 00:19:28
 */
@Slf4j
@Controller
@RequestMapping("/sys/car/long")
public class LongCarController {

    private String prefix = "system/car/long/";

    @Autowired
    private CarService carService;

    @Autowired
    private IdComponent idComponent;
    @Autowired
    private CarDAO carDAO;
    @Autowired
    private ParkingDAO parkingDAO ;

    @GetMapping()
    @RequiresPermissions("sys:car:car")
    String car() {
        return "system/car/long/car";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:car")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        log.info("[LONG CAR CONTROLLER] check long car list, the params: {}", params.toString());
        params.put(params.get("query").toString(), "%" + params.get("value") + "%");
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //停车场查询
        if("parkingName".equals(params.get("query"))) {
            Parking value = parkingDAO.findByParkingName(params.get("value").toString());
            params.put("parkingId", value == null ? 0 : value.getId());

        }
        //查询列表数据
        params.put("parkingType", 1);
        Query query = new Query(params);
        List<CarDTO> carList = carService.listLike(query);
        int total = carService.countLike(query);
        return new PageUtils(carList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:car:add")
    String add(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:car:edit")
    String edit(@PathVariable Long id, Model model) {
        Car car = carService.findById(id);
        model.addAttribute("car", car);
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:car:add")
    public R save(CarDTO dto) {
        Car car = carDAO.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
        if (car != null) {
            return ResponseResult.addCarCheck(car) ;
        }
        return carService.save(dto) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:car:edit")
    public R update(CarDTO dto) {
        carService.update(dto);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:car:remove")
    public R remove(Long id) {
        return carService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:car:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        carService.batchRemove(ids);
        return R.ok();
    }

}
