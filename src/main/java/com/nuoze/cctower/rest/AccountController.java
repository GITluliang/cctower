package com.nuoze.cctower.rest;

import com.nuoze.cctower.component.IdComponent;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.vo.AccountVO;
import com.nuoze.cctower.service.AccountService;
import com.nuoze.cctower.service.ParkingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
 * @author JiaShun
 * @date 2019-04-27 16:34:21
 */

@Controller
@RequestMapping("/sys/account")
public class AccountController {

    private String prefix = "system/account/";
    private static final String SERVICE_CHEARGE_REGEX = "\\d{1,3}";
    private static final Pattern SERVICE_CHARGE_PATTERN = Pattern.compile(SERVICE_CHEARGE_REGEX);

    @Autowired
    private AccountService accountService;
    @Autowired
    private IdComponent idComponent;
    @Autowired
    private PaymentComponent paymentComponent;
    @Autowired
    private ParkingService parkingService;

    @GetMapping()
    @RequiresPermissions("sys:account:account")
    String account(Model model) {
        model.addAttribute("parkingList", idComponent.getParkingList());
        return prefix + "account";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:account:account")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        Query query = new Query(params);
        List<AccountVO> accountList = accountService.list(query);
        int total = accountService.count(query);
        return new PageUtils(accountList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:account:add")
    String add(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "add";
    }

    @GetMapping("/deposit/{id}")
    @RequiresPermissions("sys:account:deposit")
    String deposit(@PathVariable Long id, Model model) {
        Account account = accountService.get(id);
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account, accountVO);
        accountVO.setWithdrawalAmount(paymentComponent.balanceToWithdrawalAmount(account.getBalance(), account.getServiceCharge()));
        accountVO.setParkingName(parkingService.findById(account.getParkingId()).getName());
        model.addAttribute("account", accountVO);
        return prefix + "deposit";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:account:edit")
    String edit(@PathVariable Long id, Model model) {
        Account account = accountService.get(id);
        model.addAttribute("account", account);
        return prefix + "edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:account:add")
    public R save(Account account) {
        Long parkingId = account.getParkingId();
        if (accountService.findByParkingId(parkingId) != null) {
            return R.error(201, "此停车场已有绑定账户");
        }
        return accountService.save(account) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:account:edit")
    public R update(Account account) {
        Integer serviceCharge = account.getServiceCharge();
        if (serviceCharge != null && !SERVICE_CHARGE_PATTERN.matcher(String.valueOf(serviceCharge)).matches()) {
            return R.error(201, "服务费必须为0-1000之间的正整数");
        }
        accountService.update(account);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:account:remove")
    public R remove(Long id) {
        return accountService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:account:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        accountService.batchRemove(ids);
        return R.ok();
    }

}
