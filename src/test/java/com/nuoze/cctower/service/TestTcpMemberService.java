//package com.nuoze.cctower.service;
//
//import com.nuoze.cctower.component.PaymentComponent;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigDecimal;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Slf4j
//public class TestTcpMemberService {
//
//    @Autowired
//    private PaymentComponent paymentComponent;
//
//    @Test
//    public void test() {
//        BigDecimal serviceCharge = paymentComponent.getServiceCharge(new BigDecimal(100), 0);
//        log.info(String.valueOf(serviceCharge));
//    }
//}
