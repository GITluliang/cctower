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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ToC用户表
 *
 * @author luliang
 * @date 219-8-23 01:57
 */
@Service
public class TcpMemberServiceImpl implements TcpMemberService {
    @Autowired
    private MemberDAO memberDao;

    @Override
    public Pair<Integer, List<MemberVO>> list(Map<String, Object> map) {
        if (map.get("nickName") != null) {
            map.put("nickName", "%" + map.get("nickName") + "%");
        }
        List<MemberVO> memberVOS = new CopyOnWriteArrayList<>();
        int count = memberDao.count(map);
        List<Member> list = memberDao.list(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (Member member : list) {
                MemberVO memberVO = new MemberVO()
                        .setAvatarUrl(member.getAvatarUrl())
                        .setBalance(member.getBalance())
                        .setCity(member.getCity())
                        .setId(member.getId())
                        .setNickName(member.getNickName())
                        .setOpenId(member.getOpenId())
                        .setPhone(member.getPhone())
                        .setProvince(member.getProvince());

                Date createTime = member.getCreateTime();
                if (createTime != null) {
                    memberVO.setCreateTime(DateUtils.formatDateTime(createTime));
                }
                Date updateTime = member.getUpdateTime();
                if (updateTime != null) {
                    memberVO.setUpdateTime(DateUtils.formatDateTime(updateTime));
                }
                memberVOS.add(memberVO);
            }
        }
        return new MutablePair<>(count, memberVOS);
    }

}
