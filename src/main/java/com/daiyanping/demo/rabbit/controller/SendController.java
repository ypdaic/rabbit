package com.daiyanping.demo.rabbit.controller;

import cn.hutool.core.lang.UUID;
import com.daiyanping.demo.rabbit.base.ResponseCode;
import com.daiyanping.demo.rabbit.base.ServerResponse;
import com.daiyanping.demo.rabbit.config.mq.RabbitConfig;
import com.daiyanping.demo.rabbit.entity.Mail;
import com.daiyanping.demo.rabbit.entity.MsgLog;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import com.daiyanping.demo.rabbit.util.MessageHelper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 当发送消息到不存在的exchange时，会触发shutdownCompleted的回调，进而触发ConfirmCallback的回调
     * @return
     */
    @PostMapping("/test")
    public ServerResponse test() {
        String msgId = UUID.fastUUID().toString();

        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend("xxxxxxx", RabbitConfig.MAIL_ROUTING_KEY_NAME, "test", correlationData);// 发送消息

        return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());

    }

    /**
     * 当发送的消息无法路由到队列时，会触发handleReturn的回调，进而触发ReturnCallback的回调，但是要设置mandatory为true
     *
     *
     * @return
     */
    @PostMapping("/test2")
    public ServerResponse test2() {
        String msgId = UUID.fastUUID().toString();

        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME + ".test", "test", correlationData);// 发送消息

        return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());

    }

    /**
     * rabbitTemplate 发送的消息默认是持久化的
     * @return
     */
    @PostMapping("/test3")
    @Transactional(transactionManager = "getRabbitTransactionManager")
    public ServerResponse test3() {
        String msgId = UUID.fastUUID().toString();

        CorrelationData correlationData = new CorrelationData(msgId);
//        // 设置消息不是持久化的
//        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
//                return message;
//
//            }
//        };
//        for (int i = 0; i < 1000; i++) {
//
//            rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, "test" + i, messagePostProcessor, correlationData);// 发送消息
//        }


        for (int i = 0; i < 2000; i++) {
            String s = "test" + i;
            System.out.println("发送的消息: " + s);
            rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, s, correlationData);// 发送消息
        }

        return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());

    }
}
