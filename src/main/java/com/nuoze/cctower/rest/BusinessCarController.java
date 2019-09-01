package com.nuoze.cctower.rest;

import java.util.List;
import java.util.Map;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.UserDAO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
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


/**
 * 车辆表
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
    private UserDAO userDAO;
	
	@GetMapping()
	@RequiresPermissions("sys:car:car")
	String car(){
	    return "system/car/business/car";
	}

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:car")
    public PageUtils list(@RequestParam Map<String, Object> params){
        //查询列表数据
        params.put("parkingType", 3);
        Query query = new Query(params);
        List<CarDTO> carList = carService.list(query);
        int total = carService.count(query);
        return new PageUtils(carList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:car:add")
    String add(){
        return prefix + "add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:car:edit")
    String edit(@PathVariable Long id, Model model){
        Car car = carService.findById(id);
        model.addAttribute("car", car);
        return prefix + "edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:car:add")
    public R save(CarDTO dto){
        Long userId = ShiroUtils.getUserId();
        Long parkingId = userDAO.selectByPrimaryKey(userId).getParkingId();
        dto.setParkingId(parkingId);
        return carService.saveBusinessCar(dto) > 0 ? R.ok() : R.error();
    }
    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:car:edit")
    public R update( CarDTO dto){
        carService.update(dto);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping( "/remove")
    @ResponseBody
    @RequiresPermissions("sys:car:remove")
    public R remove( Long id){
        return carService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping( "/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:car:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids){
        carService.batchRemove(ids);
        return R.ok();
    }
	
}
