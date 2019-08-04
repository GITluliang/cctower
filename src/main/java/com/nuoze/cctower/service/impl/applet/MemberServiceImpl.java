package com.nuoze.cctower.service.impl.applet;

import com.nuoze.cctower.dao.MemberDAO;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.service.applet.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author JiaShun
 * @date 2019-01-18 22:54
 */

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDAO memberDAO;

    @Override
    public Member findByOpenId(String openId) {
        return this.memberDAO.selectByOpenId(openId);
    }

    @Override
    public void add(Member member) {
        member.setCreateTime(new Date());
        member.setUpdateTime(new Date());
        this.memberDAO.insert(member);
    }

    @Override
    public void update(Member member) {
        member.setUpdateTime(new Date());
        this.memberDAO.updateByPrimaryKeySelective(member);
    }
}
