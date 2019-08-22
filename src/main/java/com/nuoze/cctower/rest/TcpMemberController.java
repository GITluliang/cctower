package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.pojo.vo.FinanceVO;
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

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * ToC用户表
 * 
 * @author JiaShun
 * @email jiashun@outlook.com
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
	String Member(){
		return prefix + "member";
	}

	@ResponseBody
	@GetMapping("list")
	@RequiresPermissions("sys:member:member")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		Pair<Integer, List<MemberVO>> pair = tcpMemberService.list(params);
		return new PageUtils(pair.getRight(), pair.getLeft());
	}

}
