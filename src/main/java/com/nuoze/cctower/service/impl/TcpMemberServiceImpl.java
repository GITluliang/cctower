package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.dao.MemberDAO;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.pojo.vo.MemberVO;
import com.nuoze.cctower.service.TcpMemberService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TcpMemberServiceImpl implements TcpMemberService {
	@Autowired
	private MemberDAO memberDao;

	@Override
	public Pair<Integer, List<MemberVO>> list(Map<String, Object> map){
		if(map.get("nickName") != null) {
			map.put("nickName", "%" +map.get("nickName") + "%") ;
		}
		List<MemberVO> memberVOS = new ArrayList<>();
		int count = memberDao.count(map) ;
		List<Member> list = memberDao.list(map);
		if (!CollectionUtils.isEmpty(list)) {
			for (Member member : list) {
				MemberVO memberVO = new MemberVO();
				memberVO.setAvatarUrl(member.getAvatarUrl());
				memberVO.setBalance(member.getBalance());
				memberVO.setCity(member.getCity());
				memberVO.setId(member.getId());
				memberVO.setNickName(member.getNickName());
				memberVO.setOpenId(member.getOpenId());
				memberVO.setPhone(member.getPhone());
				memberVO.setProvince(member.getProvince());

				Date createTime = member.getCreateTime();
				if ( createTime!= null) {
					memberVO.setCreateTime(DateUtils.formatDateTime(createTime));
				}
				Date updateTime = member.getUpdateTime();
				if(updateTime != null) {
					memberVO.setUpdateTime(DateUtils.formatDateTime(updateTime));
				}
				memberVOS.add(memberVO) ;
			}
		}
		return new MutablePair<>(count,memberVOS);
	}

}
