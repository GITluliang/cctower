package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.pojo.vo.MemberVO;
import com.nuoze.cctower.service.TcpMemberService;
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


/**
 * ToC用户表
 * @author luliang
 * @date 2019-08-21 22:05:15
 */
@Controller
@RequestMapping("/sys/member")
public class TcpMemberController {

    private String prefix = "system/member/";

    @Autowired
    private TcpMemberService tcpMemberService;

    @GetMapping()
    @RequiresPermissions("sys:member:member")
    String Member() {
        return prefix + "member";
    }

    @ResponseBody
    @GetMapping("list")
    @RequiresPermissions("sys:member:member")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Pair<Integer, List<MemberVO>> pair = tcpMemberService.list(params);
        return new PageUtils(pair.getRight(), pair.getLeft());
    }

}
