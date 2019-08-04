package com.nuoze.cctower.service.applet;

import com.nuoze.cctower.pojo.entity.Member;

/**
 * @author JiaShun
 * @date 2019-01-18 22:53
 */
public interface MemberService {

    Member findByOpenId(String openId);

    void add(Member member);

    void update(Member member);
}
