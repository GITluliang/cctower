package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.pojo.vo.BusinessTransactionRecordVO;
import com.nuoze.cctower.service.BusinessTransactionRecordService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.util.ShiroUtils.getSubject;
import static com.nuoze.cctower.common.util.ShiroUtils.getUser;

/**
 * @Author luliang
 * @Date 2019-12-03 10:29
 * 时长劵商户充值
 */
@Controller
@RequestMapping("/mobile/recharge")
    public class RechargeController {
    private String prefix = "h5/mobile/recharge/";

    @Autowired
    private BusinessTransactionRecordService businessTransactionRecordService;

    @RequestMapping()
    public String listRecharge() {
        return prefix + "listRecharge";
    }

    @RequestMapping("/addRecharge")
    public String addRecharge() {
        return prefix + "addRecharge";
    }

    @RequestMapping("/state")
    public String state() {
        return prefix + "state";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carBusinessAuthZ() {
        return getSubject().isPermitted("sys:car:business") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }

    /**
     * 充值记录
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping("rechargeStream")
    public Result rechargeStream(@RequestParam Map<String, Object> params) {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("user", getUser());
        return ResponseResult.success(map);
    }

}
