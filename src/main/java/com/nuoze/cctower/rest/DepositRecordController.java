package com.nuoze.cctower.rest;

import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.service.AccountService;
import com.nuoze.cctower.service.BusinessService;
import java.math.BigDecimal;
import java.util.Map;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.dto.DepositRecordDTO;
import com.nuoze.cctower.pojo.entity.DepositRecord;
import com.nuoze.cctower.service.DepositRecordService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.nuoze.cctower.common.constant.Constant.*;
import static com.nuoze.cctower.common.constant.Constant.ONE_HUNDRED;

/**
 * 
 * 
 * @author JiaShun
 * @email jiashun@outlook.com
 * @date 2019-04-30 00:00:58
 */
 
@Controller
@RequestMapping("/sys/depositRecord")
public class DepositRecordController {

	private String prefix = "system/depositRecord/";
	@Autowired
	private DepositRecordService depositRecordService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private PaymentComponent paymentComponent;
	@Autowired
	private IdComponent idComponent;
	
	@GetMapping()
	@RequiresPermissions("sys:depositRecord:depositRecord")
	String depositRecord(){
	    return "system/depositRecord/depositRecord";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:depositRecord:depositRecord")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		params = idComponent.buildParams(params);
		if (params.isEmpty()) {
			return new PageUtils(EMPTY_LIST, 0);
		}
		//查询列表数据
        Query query = new Query(params);
		return new PageUtils(depositRecordService.list(query), depositRecordService.count(query));
	}
	
	@GetMapping("/add")
	@RequiresPermissions("sys:depositRecord:add")
	String add(){
		return prefix + "add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("sys:depositRecord:edit")
	String edit(@PathVariable Long id,Model model){
		DepositRecord depositRecord = depositRecordService.get(id);
		model.addAttribute("depositRecord", depositRecord);
	    return prefix + "edit";
	}

	/**
	 * 完成转账
	 */
	@ResponseBody
	@PostMapping("/finishDeposit")
	@RequiresPermissions("sys:depositRecord:finish")
	public R finishDeposit(Long id){
		if(depositRecordService.finishDeposit(id) > 0){
			return R.ok();
		}
		return R.error();
	}

	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("sys:depositRecord:add")
	public R save(DepositRecordDTO depositRecordDTO){
		if (StringUtils.isBlank(depositRecordDTO.getBankAddress()) || StringUtils.isBlank(depositRecordDTO.getCard())) {
			return R.error(201, "提现开户行或银行卡不能为空");
		}
		BigDecimal amount = depositRecordDTO.getAmount();
		if (amount.compareTo(EMPTY_MONEY) <= 0) {
			return R.error(202, "提现金额必须大于零");
		}
		Account account = accountService.get(depositRecordDTO.getAccountId());
		BigDecimal balance = account.getBalance();
		if (amount.compareTo(balance) > 0) {
			return R.error(203, "提现金额不能超过当前余额");
		}
		if(depositRecordService.save(depositRecordDTO) > 0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sys:depositRecord:edit")
	public R update( DepositRecord depositRecord){
		depositRecordService.update(depositRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("sys:depositRecord:remove")
	public R remove( Long id){
		if(depositRecordService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("sys:depositRecord:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		depositRecordService.batchRemove(ids);
		return R.ok();
	}
	
}
