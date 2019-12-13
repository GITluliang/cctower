package com.nuoze.cctower.component;

import com.alibaba.fastjson.JSON;
import com.nuoze.cctower.common.enums.ApiDataEnum;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.dao.MqConfigDAO;
import com.nuoze.cctower.pojo.entity.Billing;
import com.nuoze.cctower.pojo.entity.BillingDetail;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.pojo.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.nuoze.cctower.common.constant.Constant.*;

/**
 * mq消息发送组件
 *
 * @author JiaShun
 * @date 2019-04-27 00:52
 */
@Slf4j
@Component
public class MqSendComponent {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MqConfigDAO mqConfigDAO;

    /**
     * 发送出厂车辆
     *
     * @param parkingId
     * @param goOutVO
     */
    public void sendGoOutCar(Long parkingId, GoOutVO goOutVO) {
        ApiMqVO mqVO = new ApiMqVO();
        mqVO.setType(GO_OUT_CAR_TYPE);
        mqVO.setGoOutVO(goOutVO);
        sendMq(mqVO, parkingId);
    }

    /**
     * 发送收費规则
     *
     * @param dataEnum
     * @param billing
     * @param detail
     */
    public void sendBilling(ApiDataEnum dataEnum, Billing billing, BillingDetail detail) {
        BillingVO billingVO = new BillingVO();
        BillingApiEntity apiEntity = new BillingApiEntity();
        apiEntity.setCloudId(billing.getId());
        apiEntity.setFreeTime(billing.getFreeTime());
        apiEntity.setWechatDiscount(billing.getWechatDiscount());
        apiEntity.setAlipayDiscount(billing.getAlipayDiscount());
        apiEntity.setDayCost(billing.getDayCost());
        apiEntity.setDetail(detail);
        billingVO.setBillingApiEntity(apiEntity);
        billingVO.setType(dataEnum.name());
        ApiMqVO mqVO = new ApiMqVO();
        mqVO.setType(BILLING_CAR_TYPE);
        mqVO.setBillingVO(billingVO);
        sendMq(mqVO, billing.getParkingId());
    }

    /**
     * 发送月租车
     *
     * @param dataEnum
     * @param vo
     */
    public void sendRentCar(ApiDataEnum dataEnum, Car vo) {
        RentCarVO rentCarVO = new RentCarVO();
        rentCarVO.setRentCarApiEntity(new RentCarApiEntity().setCloudId(vo.getId()).setNumber(vo.getNumber()).setRentCarStart(DateUtils.formatDateTime(vo.getMonthlyParkingStart()))
                .setRentCarEnd(DateUtils.formatDateTime(vo.getMonthlyParkingEnd())).setStatus(vo.getStatus()).setInfieldPermission(vo.getInfieldPermission()).setUuid(vo.getUuid()));
        rentCarVO.setType(dataEnum.name());
        ApiMqVO mqVO = new ApiMqVO().setType(RENT_CAR_TYPE).setRentCarVO(rentCarVO);
        sendMq(mqVO, vo.getParkingId());
    }

    /**
     * 通过RabbitTemplate发送mq指令
     *
     * @param mqVO
     * @param parkingId
     */
    public void sendMq(ApiMqVO mqVO, Long parkingId) {
        String queue = mqConfigDAO.selectQueueByParkingId(parkingId);
        log.info("[Mq Send Component] mq send to queue: {} , parkingId: {}, message: {}", queue, parkingId, JSON.toJSONString(mqVO));
        this.rabbitTemplate.convertAndSend(queue, JSON.toJSONString(mqVO));
        this.jmsMessagingTemplate.convertAndSend(queue == null ? "ceshi" : queue, JSON.toJSONString(mqVO));
    }

}
