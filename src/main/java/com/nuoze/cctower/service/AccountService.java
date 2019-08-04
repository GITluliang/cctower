package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.pojo.vo.AccountVO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author JiaShun
 * @date 2019-04-27 16:34:21
 */
public interface AccountService {

	Account findByParkingId(Long parkingId);
	
	Account get(Long id);
	
	List<AccountVO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Account account);
	
	int update(Account account);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
