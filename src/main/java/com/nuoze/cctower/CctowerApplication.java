package com.nuoze.cctower;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author JiaShun
 * @date 2019-03-01 01:57
 */
@MapperScan("com.nuoze.cctower.dao")
@SpringBootApplication
public class CctowerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CctowerApplication.class, args);
    }

}
