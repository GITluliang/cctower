package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.vo.RenewRecordVO;
import com.nuoze.cctower.service.RenewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.commons.lang3.tuple.Pair;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * @author JiaShun
 * @date 2019-05-15 00:35
 */
@Controller
@RequestMapping("/sys/renew")
public class RenewController {

    String prefix = "system/renew/";

    @Autowired
    private RenewService renewService;
    @Autowired
    private IdComponent idComponent;

    @GetMapping()
    @RequiresPermissions("sys:renew:renew")
    String record(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "renew";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:renew:renew")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        Query query = new Query(params);
        Pair<Integer, List<RenewRecordVO>> pair = renewService.list(query);
        return new PageUtils(pair.getRight(), pair.getLeft());
    }
}
