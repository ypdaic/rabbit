package com.daiyanping.demo.rabbit.controller;

import cn.hutool.core.lang.UUID;
import com.daiyanping.demo.rabbit.base.ResponseCode;
import com.daiyanping.demo.rabbit.base.ServerResponse;
import com.daiyanping.demo.rabbit.config.mq.RabbitConfig;
import com.daiyanping.demo.rabbit.entity.Mail;
import com.daiyanping.demo.rabbit.entity.MsgLog;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import com.daiyanping.demo.rabbit.util.MessageHelper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    IMsgLogService msgLogService;

    @PostMapping("/send")
    public ServerResponse send(@Validated Mail mail, Errors errors) {
        String msgId = UUID.fastUUID().toString();

        MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME);
        msgLogService.add(msgLog);// 消息入库

        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, MessageHelper.objToMsg(mail), correlationData);// 发送消息

        return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());

    }
}
