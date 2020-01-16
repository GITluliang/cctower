package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.constant.Constant;
import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.result.ResultEnum;
import com.nuoze.cctower.dao.ParkingRecordDAO;
import com.nuoze.cctower.dao.PassagewayDAO;
import com.nuoze.cctower.pojo.dto.ApiCarLongDTO;
import com.nuoze.cctower.pojo.dto.ApiDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.pojo.entity.Passageway;
import com.nuoze.cctower.pojo.vo.ApiOutVO;
import com.nuoze.cctower.pojo.vo.ApiPayStatusVO;
import com.nuoze.cctower.service.ApiService;
import com.nuoze.cctower.service.CarService;
import com.nuoze.cctower.service.ParkingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author JiaShun
 * @date 2019-03-19 23:06
 */
@Slf4j
@RequestMapping("api")
@RestController
public class ApiController {

    private static final Integer API_CHECK = 0;
    private static final Integer API_CHECK_IS_FREE_CAR = 1;
    private static final Integer API_CHECK_CHANGE_CAR_NUMBER = 2;
    private static final Integer API_INIT = 3;


    @Value("${auth.token}")
    private String token;

    @Autowired
    private ApiService apiService;
    @Autowired
    private PassagewayDAO passagewayDAO;
    @Autowired
    private ParkingRecordDAO parkingRecordDAO;
    @Autowired
    private CarService carService;
    @Autowired
    private ParkingService parkingService;

    /**
     * 车辆入场
     *
     * @param apiDTO apiDTO
     * @param auth   token
     * @return Result
     */
    @PostMapping("in")
    public Result in(@RequestBody ApiDTO apiDTO, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER] 入场 receive carNumber: {} enter parking id: {}", apiDTO.getCarNumber(), apiDTO.getParkingId());
        Result result = checkParam(apiDTO, auth, API_CHECK);
        if (result != null) {
            return result;
        }
        ParkingRecord record = parkingRecordDAO.findByParkingIdAndCarNumberAndStatus(apiDTO.getParkingId(), apiDTO.getCarNumber());
        if (record != null) {
            parkingRecordDAO.deleteByPrimaryKey(record.getId());

        }
        return apiService.in(apiDTO) ? ResponseResult.success() : ResponseResult.fail(ResultEnum.SERVER_ERROR);
    }

    /**
     * 车辆出场
     *
     * @param apiDTO apiDTO
     * @param auth   token
     * @return Result
     */
    @PostMapping("out")
    public Result out(@RequestBody ApiDTO apiDTO, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER]出厂 carNumber: {} out parking id: {}", apiDTO.getCarNumber(), apiDTO.getParkingId());
        Result result = checkParam(apiDTO, auth, API_CHECK);
        if (result != null) {
            return result;
        }
        ApiOutVO apiOutVO = apiService.out(apiDTO);
        if (apiOutVO == null) {
            log.error("停车场内无此车辆：parking id:{},  car number:{}", apiDTO.getParkingId(), apiDTO.getCarNumber());
            return ResponseResult.fail(202, "停车场内无此车辆");
        }
        return ResponseResult.success(apiOutVO);
    }

    /**
     * 线下支付
     *
     * @param apiDTO apiDTO
     * @param auth   token
     * @return Result
     */
    @PostMapping("paid")
    public Result paid(@RequestBody ApiDTO apiDTO, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER]线下支付 carNumber: {} offline payment in parking id: {}", apiDTO.getCarNumber(), apiDTO.getParkingId());
        Result result = checkParam(apiDTO, auth, API_CHECK);
        if (result != null) {
            return result;
        }
        return apiService.paid(apiDTO) ? ResponseResult.success() : ResponseResult.fail(202, "停车场内无此车辆");
    }

    /**
     * 检查支付状态
     *
     * @param uuid
     * @param auth
     * @return
     */
    @GetMapping("check-pay-status")
    public Result checkPayStatus(@RequestParam("uuid") String uuid, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER] check pay status by uuid: {}", uuid);
        if (!token.equals(auth)) {
            return ResponseResult.fail(ResultEnum.PERMISSION_DENIED);
        }
        ApiPayStatusVO payStatus = apiService.checkPayStatus(uuid);
        return payStatus != null ? ResponseResult.success(payStatus) : ResponseResult.fail(202, "无此停车记录");
    }

    /**
     * 由于线下车牌识别识别不正确 改变carNumber
     *
     * @param apiDTO apiDTO
     * @param auth   token
     * @return Result
     */
    @PostMapping("change-car-number")
    public Result changeCarNumber(@RequestBody ApiDTO apiDTO, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER] carNumber: {} change to newCarNumber: {} in parking id: {}", apiDTO.getOldCarNumber(), apiDTO.getCarNumber(), apiDTO.getParkingId());
        Result result = checkParam(apiDTO, auth, API_CHECK_CHANGE_CAR_NUMBER);
        if (result != null) {
            return result;
        }
        return ResponseResult.success(apiService.changeCarNumber(apiDTO) ? ResponseResult.success() : ResponseResult.fail(202, "停车场内无此车辆"));
    }

    /**
     * 是否是月租车
     *
     * @param apiDTO
     * @param auth
     * @return
     */
    @PostMapping("is-free-car")
    public Result isFreeCar(@RequestBody ApiDTO apiDTO, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER] isFreeCar getOldCarNumber: {} CarNumber: {} parking id: {}", apiDTO.getOldCarNumber(), apiDTO.getCarNumber(), apiDTO.getParkingId());
        Result result = checkParam(apiDTO, auth, API_CHECK_IS_FREE_CAR);
        return result != null ? result : ResponseResult.success(apiService.isRentFreeCar(apiDTO));
    }

    /**
     * 初始化月租车
     *
     * @param apiDTO apiDTO
     * @param auth   token
     * @return Result
     */
    @PostMapping("init-rent-car")
    public Result initRentCar(@RequestBody ApiDTO apiDTO, @RequestHeader("authorization") String auth) {
        Result result = checkParam(apiDTO, auth, API_INIT);
        if (result != null) {
            return result;
        }
        return apiService.initData(apiDTO, Constant.RENT_CAR_TYPE) ? ResponseResult.success() : ResponseResult.fail(203, "初始化Rent Car失败");
    }

    /**
     * 初始化计费标准
     *
     * @param apiDTO apiDTO
     * @param auth   token
     * @return Result
     */
    @PostMapping("init-billing")
    public Result initBilling(@RequestBody ApiDTO apiDTO, @RequestHeader("authorization") String auth) {
        Result result = checkParam(apiDTO, auth, API_INIT);
        if (result != null) {
            return result;
        }
        return apiService.initData(apiDTO, Constant.BILLING_CAR_TYPE) ? ResponseResult.success() : ResponseResult.fail(203, "初始化Billing失败");
    }

    /**
     * 添加通道配置
     *
     * @param passageway passageway
     * @param auth       token
     * @return Result
     */
    @PostMapping("add-passageway")
    public Result addPassageway(@RequestBody Passageway passageway, @RequestHeader("authorization") String auth) {
        Result result = checkPassagewayParam(passageway, auth);
        if (result != null) {
            return result;
        }
        Integer type = passageway.getType();
        if (type == null) {
            return ResponseResult.fail(ResultEnum.INVALID_PARAM);
        }
        Passageway paw = passagewayDAO.findByParkingIdAndIp(passageway.getParkingId(), passageway.getIp());
        if (paw != null) {
            return ResponseResult.fail(201, "该摄像头IP已存在");
        }
        return this.apiService.addPassageway(passageway) > 0 ? ResponseResult.success() : ResponseResult.fail(ResultEnum.SERVER_ERROR);
    }

    /**
     * 删除通道配置
     *
     * @param passageway passageway
     * @param auth       token
     * @return Result
     */
    @PostMapping("delete-passageway")
    public Result removePassageway(@RequestBody Passageway passageway, @RequestHeader("authorization") String auth) {
        Result result = checkPassagewayParam(passageway, auth);
        if (result != null) {
            return result;
        }
        this.apiService.deletePassageway(passageway.getParkingId(), passageway.getIp());
        return ResponseResult.success();
    }

    private Result checkPassagewayParam(Passageway passageway, String auth) {
        if (!token.equals(auth)) {
            return ResponseResult.fail(ResultEnum.PERMISSION_DENIED);
        }
        Long parkingId = passageway.getParkingId();
        String ip = passageway.getIp();
        if (parkingId == null || StringUtils.isEmpty(ip)) {
            return ResponseResult.fail(ResultEnum.INVALID_PARAM);
        }
        return null;
    }

    private Result checkParam(ApiDTO apiDTO, String auth, Integer type) {
        if (!token.equals(auth)) {
            return ResponseResult.fail(ResultEnum.PERMISSION_DENIED);
        }
        boolean flag = false;
        switch (type) {
            case 0:
                flag = apiDTO.check();
                break;
            case 1:
                flag = apiDTO.freeCarCheck();
                break;
            case 2:
                flag = apiDTO.checkCarNumber();
                break;
            case 3:
                flag = apiDTO.checkInit();
                break;
            default:
                break;
        }
        return flag ? null : ResponseResult.fail(ResultEnum.INVALID_PARAM);
    }

    /**
     * 线下月租车更新
     *
     * @param dto
     * @param auth
     * @return
     */
    @PostMapping("carLong/update")
    public Result update(@RequestBody ApiCarLongDTO dto, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER] carLong/update auth: {} CarDTO:{}", auth, dto);
        Result result = checkParamLongCar(dto, auth);
        if (result != null) {
            return result;
        }
        Car byUuid = apiService.findByUuid(dto.getUuid());
        if (byUuid != null) { carService.remove(byUuid.getId());}
        if (StringUtils.isNotBlank(dto.getCarNumber())) {
            Car carNumber = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getCarNumber());
            if (carNumber != null) {
                carService.remove(carNumber.getId());
            }
        }
        if (StringUtils.isNotBlank(dto.getNumberOne())) {
            Car carOne = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberOne());
            if (carOne != null) {
                carService.remove(carOne.getId());
            }
        }
        if (StringUtils.isNotBlank(dto.getNumberTow())) {
            Car carTow = carService.findByParkingIdAndCarNumber(dto.getParkingId(), dto.getNumberTow());
            if (carTow != null) {
                carService.remove(carTow.getId());
            }
        }
        return apiService.saveCarLong(dto) > 0 ? ResponseResult.success() : ResponseResult.fail(203, "月租车更新失败");
    }

    /**
     * 线下月租车删除
     *
     * @param uuids
     * @param auth
     * @return
     */
    @PostMapping("carLong/batchRemove")
    public Result remove(@RequestBody String[] uuids, @RequestHeader("authorization") String auth) {
        log.info("[API CONTROLLER] carLong/batchRemove auth: {} uuids:{}", auth, uuids);
        if (!token.equals(auth)) {
            return ResponseResult.fail(ResultEnum.PERMISSION_DENIED);
        }
        if (uuids.length == 0) {
            return ResponseResult.fail(ResultEnum.INVALID_PARAM);
        }
        return apiService.batchRemoveCarLong(uuids) > 0 ? ResponseResult.success() : ResponseResult.fail(203, "月租车删除失败");
    }

    private Result checkParamLongCar(ApiCarLongDTO dto, String auth) {
        if (!token.equals(auth)) {
            return ResponseResult.fail(ResultEnum.PERMISSION_DENIED);
        }
        if (StringUtils.isBlank(dto.getUuid()) || dto.getParkingId() == null || StringUtils.isBlank(dto.getCarNumber()) ||
                StringUtils.isBlank(dto.getBeginDate()) || StringUtils.isBlank(dto.getEndDate())) {
            return ResponseResult.fail(ResultEnum.INVALID_PARAM);
        }
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (!(dto.getBeginDate().matches(regex) || dto.getEndDate().matches(regex))) {
            return ResponseResult.fail(ResultEnum.INVALID_PARAM);
        }
        return null;
    }
    
}
