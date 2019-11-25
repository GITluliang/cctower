package com.nuoze.cctower.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTcpMemberService {

    public static void main(String[] args) {
        String date = "2019-10-10";
        System.out.println(date.matches("\\d{4}-\\d{2}-\\d{2}"));
    }
}
