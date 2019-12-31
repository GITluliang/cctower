package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.vo.FinanceVO;
import com.nuoze.cctower.service.FinanceService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * @author JiaShun
 * @date 2019-05-01 17:13
 */
@Controller
@RequestMapping("/sys/record/out")
public class RecordController {

    @Autowired
    private FinanceService financeService;
    @Autowired
    private IdComponent idComponent;

    @GetMapping()
    @RequiresPermissions("sys:record:out")
    String finance(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return "system/record/recordOut";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:record:out")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        params.put("statues", new int[]{1});
        Query query = new Query(params);
        Pair<Integer, List<FinanceVO>> pair = financeService.list(query);
        return new PageUtils(pair.getRight(), pair.getLeft());
    }
}
