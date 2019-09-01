package com.nuoze.cctower.rest;

import com.nuoze.cctower.component.BillingComponent;
import com.nuoze.cctower.component.IdComponent;
import java.util.List;
import java.util.Map;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.dao.BillingDetailDAO;
import com.nuoze.cctower.pojo.dto.BillingDTO;
import com.nuoze.cctower.pojo.entity.Billing;
import com.nuoze.cctower.pojo.entity.BillingDetail;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.BillingService;
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
import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;


/**
 * 停车计费逻辑表
 * 
 * @author JiaShun
 * @date 2019-03-31 10:47:55
 */
 
@Controller
@RequestMapping("/sys/billing")
public class BillingController {

	private String prefix = "system/billing/";

	@Autowired
	private BillingService billingService;
	@Autowired
	private BillingDetailDAO detailDAO;
	@Autowired
	private IdComponent idComponent;
	@Autowired
	private BillingComponent checkComponent;
	
	@GetMapping()
	@RequiresPermissions("sys:billing:billing")
	String billing(){
	    return prefix + "billing";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:billing:billing")
	public PageUtils list(@RequestParam Map<String, Object> params){
		params = idComponent.buildParams(params);
		if (params.isEmpty()) {
			return new PageUtils(EMPTY_LIST, 0);
		}
		//查询列表数据
        Query query = new Query(params);
		List<BillingDTO> billingList = billingService.list(query);
		int total = billingService.count(query);
		return new PageUtils(billingList, total);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("sys:billing:add")
	String add(Model model){
		List<Parking> parkingList = idComponent.getParkingList();
		model.addAttribute("parkingList", parkingList);
		return prefix + "add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("sys:billing:edit")
	String edit(@PathVariable Long id,Model model){
		Billing billing = billingService.findById(id);
		model.addAttribute("billing", billing);
		List<Parking> parkingList = idComponent.getParkingList();
		model.addAttribute("parkingList", parkingList);
	    return prefix + "edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("sys:billing:add")
	public R save( Billing billing){
		R r = checkComponent.billingParkingIdCheck(billing.getParkingId(), true);
		if (r != null) {
			return r;
		}
		return billingService.save(billing) > 0 ? R.ok() : R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sys:billing:edit")
	public R update(Billing billing){
		return billingService.update(billing) > 0 ? R.ok() : R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("sys:billing:remove")
	public R remove( Long id){
		BillingDetail detail = detailDAO.selectByParkingId(billingService.findById(id).getParkingId());
		if (detail != null) {
			return R.error(201, "此停车场已有计费方式详情信息，请先删除计费方式，再删除计费基础信息");
		}
		return billingService.remove(id) > 0 ? R.ok() : R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("sys:billing:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		billingService.batchRemove(ids);
		return R.ok();
	}
	
}
