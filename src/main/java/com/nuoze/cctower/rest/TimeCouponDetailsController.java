package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.vo.BusinessTransactionRecordVO;
import com.nuoze.cctower.service.BusinessTransactionRecordService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * 时长劵商户流水
 * @author JiaShun
 * @date 2019-05-05 01:25
 */
@Controller
@RequestMapping("/sys/timeCouponDetails")
public class TimeCouponDetailsController {

    @Autowired
    private IdComponent idComponent;
    @Autowired
    private BusinessTransactionRecordService businessTransactionRecordService;

    @GetMapping()
    @RequiresPermissions("sys:timeCouponDetails:timeCouponDetails")
    String timeCouponDetails() {
        return "system/timeCoupon/timeCouponDetails";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:timeCouponDetails:timeCouponDetails")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        Query query = new Query(params);
        Pair<Integer, List<BusinessTransactionRecordVO>> pair = businessTransactionRecordService.list(query);
        return new PageUtils(pair.getRight(), pair.getLeft());
    }
}
