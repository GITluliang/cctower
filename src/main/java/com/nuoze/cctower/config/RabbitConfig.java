package com.nuoze.cctower.config;

import com.nuoze.cctower.common.constant.Constant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JiaShun
 * @date 2019-04-04 01:22
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue zhongLiang() {
        return new Queue(Constant.ZHONG_LIANG_QUEUE);
    }

    @Bean
    public Queue zhubaocheng() {
        return new Queue(Constant.CS_ZHUBAOCHENG_QUEUE);
    }

}
