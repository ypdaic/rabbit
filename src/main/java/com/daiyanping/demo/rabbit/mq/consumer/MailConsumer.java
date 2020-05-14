package com.daiyanping.demo.rabbit.mq.consumer;

import com.daiyanping.demo.rabbit.email.MailUtil;
import com.daiyanping.demo.rabbit.entity.Mail;
import com.daiyanping.demo.rabbit.mq.BaseConsumer;
import com.daiyanping.demo.rabbit.util.MessageHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class MailConsumer implements BaseConsumer {

    @Autowired
    private MailUtil mailUtil;

    public void consume(Message message, Channel channel) {
        Mail mail = MessageHelper.msgToObj(message, Mail.class);
        log.info("收到消息: {}", mail.toString());

        boolean success = mailUtil.send(mail);
        if (!success) {
            throw new RuntimeException("send mail error");
        }
    }

}
