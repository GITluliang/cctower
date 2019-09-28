package com.nuoze.cctower.rest;

import java.util.List;
import java.util.Map;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.vo.ParkingTradingRecordVO;
import com.nuoze.cctower.service.ParkingTradingRecordService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * 停车场交易记录
 *
 * @author JiaShun
 * @date 2019-05-15 23:57:18
 */

@Controller
@RequestMapping("/sys/trading")
public class ParkingTradingRecordController {

    @Autowired
    private ParkingTradingRecordService parkingTradingRecordService;
    @Autowired
    private IdComponent idComponent;

    @GetMapping()
    @RequiresPermissions("sys:parkingTradingRecord:parkingTradingRecord")
    String parkingTradingRecord(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return "system/parkingTradingRecord/parkingTradingRecord";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:parkingTradingRecord:parkingTradingRecord")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        Query query = new Query(params);
        Pair<Integer, List<ParkingTradingRecordVO>> pair = parkingTradingRecordService.list(query);
        return new PageUtils(pair.getRight(), pair.getLeft());
    }

}
