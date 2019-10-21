package com.nuoze.cctower.service;

import com.nuoze.cctower.dao.OrderNumberDAO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTcpMemberService {

    @Autowired
    private OrderNumberDAO orderNumberDAO ;

    @Test
    public void test() {
/*        OrderNumber orderNumber = new OrderNumber();
        orderNumber.setCreateTime(new Date());
        orderNumber.setOrderSn(UUID.randomUUID().toString());
        orderNumber.setParkingRecordId(1L);
        orderNumberDAO.insert(orderNumber) ;*/
        Assert.assertNotNull(orderNumberDAO.findByorderSn("ba0ce293-c3ba-429b-9db1-f00d2164978a"));
    }

}
