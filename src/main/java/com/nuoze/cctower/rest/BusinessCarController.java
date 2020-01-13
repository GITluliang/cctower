package com.nuoze.cctower.rest;

import java.util.List;
import java.util.Map;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.dao.UserDAO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.service.UserService;
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
import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;


/**
 * 普通商户车辆
 *
 * @author JiaShun
 * @date 2019-03-15 00:19:28
 */

@Controller
@RequestMapping("/sys/car/business")
public class BusinessCarController {

    private String prefix = "system/car/business/";

    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;
    @Autowired
    private IdComponent idComponent;
    @Autowired
    private CarDAO carDAO;

    @GetMapping()
    @RequiresPermissions("sys:car:business")
    String car() {
        return "system/car/business/car";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:business")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params.put("createId", idComponent.getUserId());
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        params.put("parkingType", 3);
        Query query = new Query(params);
        List<CarDTO> carList = carService.list(query);
        int total = carService.count(query);
        return new PageUtils(carList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:car:business:add")
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
    @RequiresPermissions("sys:car:business:add")
    public R save(CarDTO dto) {
        Long userId = ShiroUtils.getUserId();
        User user = userService.findById(userId);
        if (EMPTY_MONEY.compareTo(user.getBalance()) >= 0 ) {
            return R.error(201, "账户余额" + user.getBalance() + "元 ,请充值");
        }
        dto.setParkingId(user.getParkingId());
        if (dto.getNumber() != null && dto.getParkingId() != null) {
            Car carNumber = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
            if (carNumber != null) {
                carService.remove(carNumber.getId());
            }
        }
        return carService.saveBusinessCar(dto) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:car:business:edit")
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
    @RequiresPermissions("sys:car:business:remove")
    public R remove(Long id) {
        return carService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:car:business:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        return carService.batchRemove(ids) > 0 ? R.ok() : R.error();
    }

}
