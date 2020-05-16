package com.daiyanping.demo.rabbit.mq.listener;

import com.daiyanping.demo.rabbit.config.mq.RabbitConfig;
import com.daiyanping.demo.rabbit.mq.BaseConsumer;
import com.daiyanping.demo.rabbit.mq.BaseConsumerProxy;
import com.daiyanping.demo.rabbit.mq.consumer.MailConsumer;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class MailListener {

//    @Autowired
//    private MailConsumer mailConsumer;

    @Autowired
    private IMsgLogService msgLogService;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    Jackson2JsonMessageConverter jackson2JsonMessageConverter;

    /**
     * unack数量等于PrefetchCount时, 不会再收到消息
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
//        BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(mailConsumer, msgLogService);
//        BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
//        if (null != proxy) {
//            proxy.consume(message, channel);
//        }
//        log.info("Mail2Listener消费者消费的消息:{}", new String(message.getBody()));
        int andIncrement = atomicInteger.getAndIncrement();
        String o = (String) jackson2JsonMessageConverter.fromMessage(message);
        String substring = o.substring(4);
        Integer integer = Integer.valueOf(substring);
        if (andIncrement != integer) {
            System.out.println("顺序发生了不一致: 本身的顺序: " + andIncrement + " 消息的顺序: " + integer);
        }
//        System.out.println("Mail2Listener消费者消费的消息:" + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        if (andIncrement == 4999) {
            atomicInteger.set(0);
        }
    }

}
