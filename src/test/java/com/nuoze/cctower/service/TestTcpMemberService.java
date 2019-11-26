package com.nuoze.cctower.service;

import com.nuoze.cctower.component.BillingComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTcpMemberService {

    @Autowired
    private BillingComponent billingComponent;

    @Test
    public void test() {
        BigDecimal cost = billingComponent.cost(7, 18L, null);
        log.info(String.valueOf(cost));
        log.info(2%3 + "");
    }
}
