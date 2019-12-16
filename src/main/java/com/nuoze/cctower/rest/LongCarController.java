package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.ResultEnum;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.common.util.excel.CommUtil;
import com.nuoze.cctower.common.util.excel.ExcelUtil;
import com.nuoze.cctower.component.IdComponent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.nuoze.cctower.service.CarService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;
import static com.nuoze.cctower.common.constant.Constant.MONTHLY_CAR;
import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;


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
    private ParkingDAO parkingDAO;

    @GetMapping()
    @RequiresPermissions("sys:car:car")
    String car(Model model) {
        model.addAttribute("parkingList", idComponent.getParkingList());
        return "system/car/long/car";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:car:car")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        log.info("[LONG CAR CONTROLLER] check long car list, the params: {}", String.valueOf(params));
        params.put(String.valueOf(params.get("query")), "%" + params.get("value") + "%");
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //停车场查询
        if ("parkingName".equals(params.get("query"))) {
            Parking value = parkingDAO.findByParkingName(String.valueOf(params.get("value")));
            params.put("parkingId", value == null ? 0 : value.getId());

        }
        //查询列表数据
        params.put("parkingType", MONTHLY_CAR);
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
            return ResponseResult.addCarCheck(car);
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
        String keys[] = {"parkingId","parkingName","number","beginDate","endDate","name","phone","corporateName","remarks","classification"};
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
                CarDTO dto = new CarDTO().setEndDate(dataMap.get("endDate")).setBeginDate(dataMap.get("beginDate")).setParkingName(dataMap.get("parkingName"));
                if(StringUtils.isBlank(dataMap.get("number"))|| StringUtils.isBlank(dataMap.get("parkingId")) || StringUtils.isBlank(dto.getEndDate()) || StringUtils.isBlank(dto.getBeginDate())) {
                    map.put("status",-1);
                    map.put("message","上传失败，上传数据必填字段不能为空");
                    return map;
                }
                String regex = "\\d{4}-\\d{2}-\\d{2}";
                if(!(dto.getBeginDate().matches(regex) || dto.getEndDate().matches(regex))) {
                    map.put("status",-1);
                    map.put("message","上传失败，日期格式错误");
                    return map;
                }
                dto.setParkingId(Long.valueOf(dataMap.get("parkingId"))).setNumber(dataMap.get("number")).setName(dataMap.get("name")).setPhone(dataMap.get("phone")).setCorporateName(dataMap.get("corporateName")).setRemarks(dataMap.get("remarks")).setClassification(dataMap.get("classification")).setParkingType(1).setStatus(1).setInfieldPermission(1);
                Car car = carDAO.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumber());
                if (car != null) {carService.remove(car.getId());}
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

        String columnNames[] = {"停车场ID(必填)","停车场名称(必填)","车牌号(必填)","生效日期(必填)","失效日期(必填)","车主姓名(选填)","联系方式(选填)","公司名称(选填)","备注(选填)","月租车分类(选填)"};
        String keys[] = {"parkingId","parkingName","number","beginDate","endDate","name","phone","corporateName","remarks","classification"};

        List<Map<String,Object>> listMap = new ArrayList<>();
        Map<String,Object> map = new HashMap<>(16);
        List<Parking> parkingByUser = parkingDAO.findParkingByUser(getUserId());
        map.put("parkingId", parkingByUser.get(0).getId());
        map.put("parkingName", parkingByUser.get(0).getName());
        map.put("number", "京A11111");
        map.put("beginDate", "2019-11-11");
        map.put("endDate", "2019-12-12");
        map.put("name", "张三");
        map.put("phone", "17155556666");
        map.put("corporateName", "XXX");
        map.put("remarks", "XXX");
        map.put("classification", "XXX");
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
