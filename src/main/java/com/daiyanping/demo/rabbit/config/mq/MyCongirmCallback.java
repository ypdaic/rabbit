package com.daiyanping.demo.rabbit.config.mq;

import com.daiyanping.demo.rabbit.entity.MsgLog;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 消息是否成功发送到Exchange
@Slf4j
@Component
public class MyCongirmCallback implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private IMsgLogService msgLogService;

    /**
     * 如果设置了消息持久化，那么ack= true是在消息持久化完成后，就是存到硬盘上之后再发送的
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        if (ack) {
////            log.info("消息成功发送到Exchange");
//            String msgId = correlationData.getId();
//            msgLogService.updateStatus(msgId, MsgLog.MsgLogStatus.CONSUMED_SUCCESS.getValue());
//        } else {
//            log.info("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
//        }
    }
}
