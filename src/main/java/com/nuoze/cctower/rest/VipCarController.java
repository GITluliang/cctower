package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.common.util.excel.CommUtil;
import com.nuoze.cctower.common.util.excel.ExcelUtil;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static com.nuoze.cctower.common.constant.Constant.*;
import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;

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
    private ParkingDAO parkingDAO;
    @Autowired
    private CarDAO carDAO;

    @GetMapping()
    @RequiresPermissions("sys:car:vip")
    String car(Model model) {
        model.addAttribute("parkingList", idComponent.getParkingList());
        return "system/car/vip/car";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:vip")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        log.info("[LONG CAR CONTROLLER] check vip car list, the params: {}", String.valueOf(params));
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        params.put("parkingType", VIP_CAR);
        Query query = new Query(params);
        List<CarDTO> carList = carService.listLike(query);
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
        if (dto.getNumber() != null) {
            Car carNumber = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
            if (carNumber != null) {
                carService.remove(carNumber.getId());
            }
        }
        if (dto.getNumberOne() != null) {
            Car carOne = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberOne());
            if (carOne != null) {
                carService.remove(carOne.getId());
            }

        }
        if (dto.getNumberTow() != null) {
            Car carTow = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberTow());
            if (carTow != null) {
                carService.remove(carTow.getId());
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
            if (dto.getNumberOne() != null) {
                if (!dto.getNumberOne().equalsIgnoreCase(car.getNumberOne())) {
                    Car carOne = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberOne());
                    if (carOne != null) {
                        carService.remove(carOne.getId());
                    }
                }
            }
            if (dto.getNumberTow() != null) {
                if (!dto.getNumberTow().equalsIgnoreCase(car.getNumberTow())) {
                    Car carTow = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberTow());
                    if (carTow != null) {
                        carService.remove(carTow.getId());
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

    /**
     * Excel数据导入
     * @param request
     * @param response
     * @param filePro
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/importExcel")
    public Map<String, Object> importExcel(HttpServletRequest request, HttpServletResponse response, String filePro){
        Map<String, Object> map = new HashMap<>(16);
        String keys[] = {"parkingId","parkingName","infieldPermission","number","numberOne","numberTow","name","phone","corporateName"};
        try {
            List<Map<String,String>> listData = ExcelUtil.getExcelData(request, "file",keys);
            log.info("[月租车导入的数据]：" + listData);
            if(listData.size() == 0){
                map.put("status",-1);
                map.put("message","上传失败，上传数据必须大于一条");
                return map;
            }
            int save = 0 ;
            for (Map<String, String> dataMap : listData) {
                CarDTO dto = new CarDTO().setParkingName(dataMap.get("parkingName"));
                String infieldPermission = dataMap.get("infieldPermission");
                if(StringUtils.isBlank(dataMap.get("number"))|| StringUtils.isBlank(dataMap.get("parkingId")) || StringUtils.isBlank(infieldPermission)) {
                    map.put("status",-1);
                    map.put("message","上传失败，上传数据必填字段不能为空");
                    return map;
                }
                if (!(Integer.valueOf(infieldPermission) == 1 || Integer.valueOf(infieldPermission) == 0)) {
                    map.put("status",-1);
                    map.put("message","上传失败，上传数据字段错误");
                    return map;
                }
                dto.setParkingId(Long.valueOf(dataMap.get("parkingId"))).setNumber(dataMap.get("number")).setName(dataMap.get("name")).setPhone(dataMap.get("phone")).setCorporateName(dataMap.get("corporateName"))
                        .setNumberOne(dataMap.get("numberOne")).setNumberTow(dataMap.get("numberTow")).setParkingType(2).setStatus(1).setInfieldPermission(Integer.valueOf(dataMap.get("infieldPermission")));
                if (dto.getNumber() != null) {
                    Car car = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
                    if (car != null) { carService.remove(car.getId()); }
                }
                if (dto.getNumberOne() != null) {
                    Car carOne = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberOne());
                    if (carOne != null) { carService.remove(carOne.getId()); }
                }
                if (dto.getNumberTow() != null) {
                    Car carTow = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberTow());
                    if (carTow != null) { carService.remove(carTow.getId()); }
                }
                save += carService.save(dto);
            }
            map.put("code", 1);
            map.put("message", "导入成功,更新" + save + "记录");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 数据导出Excel
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/excelExport", method = { RequestMethod.GET, RequestMethod.POST })
    public Map<String,Object> excelExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,Object> data = new HashMap<>(16);
        String fileName = CommUtil.formatTime("yyyyMMddHHmmss", new Date()) +".xls";

        String columnNames[] = {"停车场ID(必填)","停车场名称(必填)","内场权限(必填,0:禁用 1:正常)","车牌号(必填)","替换车牌1(选填)","替换车牌2(选填)","车主姓名(选填)","联系方式(选填)","公司名称(选填)"};
        String keys[] = {"parkingId","parkingName","infieldPermission","number","numberOne","numberTow","name","phone","corporateName"};
        List<Map<String,Object>> listMap = new ArrayList<>();
        Map<String,Object> map = new HashMap<>(16);
        List<Parking> parkingByUser = parkingDAO.findParkingByUser(getUserId());
        map.put("parkingId", parkingByUser.get(0).getId());
        map.put("parkingName", parkingByUser.get(0).getName());
        map.put("infieldPermission", 1);
        map.put("number", "京A11111");
        map.put("numberOne", "京A11112");
        map.put("numberTow", "京A11113");
        map.put("name", "张三");
        map.put("phone", "17155556666");
        map.put("corporateName", "XXX");
        listMap.add(map);
        try {
            //创建Workbook
            Workbook wb = ExcelUtil.createWorkBook(listMap, keys, columnNames);
            //保存路径
            String savePath = request.getServletContext().getRealPath("/") + File.separator + fileName;
            // 创建文件流
            OutputStream stream = new FileOutputStream(savePath);
            // 写入数据
            wb.write(stream);
            // 关闭文件流
            stream.close();

            //返回结果
            data.put("code", 1);
            String downloadUrl = request.getScheme() + "://"+request.getServerName() + ":" +
                    request.getServerPort() + "/" + fileName;
            data.put("download", downloadUrl);
            data.put("message", "文件流输出成功");

            log.info("\n模板导出成功，下载路劲：" + downloadUrl);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            log.info("\n模板下载异常：" + e);
            data.put("code", -1);
            data.put("message", "下载出错");
            return data;
        }
        return data;
    }
}
