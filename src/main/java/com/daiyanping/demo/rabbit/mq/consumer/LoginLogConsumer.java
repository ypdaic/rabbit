package com.daiyanping.demo.rabbit.mq.consumer;

import com.daiyanping.demo.rabbit.entity.LoginLog;
import com.daiyanping.demo.rabbit.mq.BaseConsumer;
import com.daiyanping.demo.rabbit.service.ILoginLogService;
import com.daiyanping.demo.rabbit.util.MessageHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginLogConsumer implements BaseConsumer {

    @Autowired
    private ILoginLogService loginLogService;

    @Override
    public void consume(Message message, Channel channel) {
        log.info("收到消息: {}", message.toString());
        LoginLog loginLog = MessageHelper.msgToObj(message, LoginLog.class);
        loginLogService.insert(loginLog);
    }
}
