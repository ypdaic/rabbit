package com.daiyanping.demo.rabbit.mq.listener;

import com.daiyanping.demo.rabbit.config.mq.RabbitConfig;
import com.daiyanping.demo.rabbit.mq.BaseConsumer;
import com.daiyanping.demo.rabbit.mq.BaseConsumerProxy;
import com.daiyanping.demo.rabbit.mq.consumer.LoginLogConsumer;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class LoginLogListener {

    @Autowired
    private LoginLogConsumer loginLogConsumer;

    @Autowired
    private IMsgLogService msgLogService;

    @RabbitListener(queues = RabbitConfig.LOGIN_LOG_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(loginLogConsumer, msgLogService);
        BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
        if (null != proxy) {
            proxy.consume(message, channel);
        }
    }

}
