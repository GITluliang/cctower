package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
import com.nuoze.cctower.service.BusinessTransactionRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-05-05 01:25
 */
@Controller
@RequestMapping("/sys/businessTransaction")
public class BusinessTransactionRecordController {

    @Autowired
    private IdComponent idComponent;
    @Autowired
    private BusinessTransactionRecordService businessTransactionRecordService;

    @GetMapping()
    @RequiresPermissions("sys:businessTransaction:businessTransaction")
    String businessTransaction(){
        return "system/businessTransaction/businessTransaction";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:businessTransaction:businessTransaction")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        Long userId = ShiroUtils.getUserId();
        if (idComponent.isNotAdmin(userId)) {
            params.put("userId", userId);
        }
        //查询列表数据
        Query query = new Query(params);
        Pair<Integer, List<BusinessTransactionRecord>> pair = businessTransactionRecordService.list(query);
        return new PageUtils(pair.getRight(), pair.getLeft());
    }
}
