package com.daiyanping.demo.rabbit.mq.listener;

import com.daiyanping.demo.rabbit.config.mq.RabbitConfig;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
//@Component
public class Mail2Listener {

//    @Autowired
//    private MailConsumer mailConsumer;

    @Autowired
    private IMsgLogService msgLogService;

    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
//        BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(mailConsumer, msgLogService);
//        BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
//        if (null != proxy) {
//            proxy.consume(message, channel);
//        }
        log.info("Mail2Listener消费者消费的消息，8081:{}", message);
//        try {
//            channel.close();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
