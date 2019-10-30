package com.nuoze.cctower.service;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.dto.RenewCarPayDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.pojo.vo.ParkingRecordVO;
import com.nuoze.cctower.pojo.vo.RenewCarRecordVO;
import com.nuoze.cctower.pojo.vo.RenewCarVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-15 00:23
 */
public interface CarService {
    List<CarDTO> list(Map<String, Object> map);

    List<CarDTO> listLike(Map<String, Object> map);

    int countLike(Map<String, Object> map);

    int count(Map<String, Object> map);

    CarDTO findById(Long id);

    int save(CarDTO dto);

    int update(CarDTO dto);

    int remove(Long id);

    int batchRemove(Long[] ids);

    /**
     * 根据车牌获取停车记录
     *
     * @param carNumber
     * @return
     */
    ParkingRecordVO parkingRecordDetail(String carNumber);

    /**
     * 根据车牌查询费用
     *
     * @param carNumber
     * @return
     */
    Result costByNumber(String carNumber);

    String[] wxCarList(String openId);

    void addWxCar(Member member, Car car);

    void deleteWxCar(Car car);

    int saveBusinessCar(CarDTO dto);

    Result renewByNumber(String carNumber);

    RenewCarVO renewCarDetail(String renewCarNumber);

    RenewCarVO findMonthlyPriceByParkingId(Long parkingId);

    /**
     * 小程序长租车续费
     * @param dto
     * @param request
     * @return
     */
    WxPayMpOrderResult renewCarPrePay(RenewCarPayDTO dto, HttpServletRequest request);

    List<RenewCarRecordVO> renewCarRecord(Map<String, Object> map);

    int saveVipCar(Car car);

    Car findByParkingIdAndCarNumber(@Param("parkingId") Long parkingId, @Param("carNumber") String carNumber);
}
