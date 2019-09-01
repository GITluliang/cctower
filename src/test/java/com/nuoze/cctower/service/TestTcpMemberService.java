package com.nuoze.cctower.service;

import com.nuoze.cctower.dao.MemberDAO;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.pojo.vo.MemberVO;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTcpMemberService {

    @Autowired
    private TcpMemberService tcpMemberService ;

    @Autowired
    private MemberDAO memberDAO ;

    @Test
    public void testMemberDao() {
        Assert.assertNotNull(memberDAO.list(null)) ;
    }

    @Test
    public void testList() {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", 0) ;
        map.put("limit", 10) ;
        Assert.assertNotNull(tcpMemberService.list(map)) ;
    }

}
