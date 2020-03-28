package com.daiyanping.demo.rabbit.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

//@Component
public class MessageListener {

//    @RabbitListener(
//            queuesToDeclare = {@Queue(name = "dai")},
//            bindings = {@QueueBinding(
//                            value = @Queue(name = "dai"),
//                            exchange = @Exchange())})
    @RabbitListener(queues = "test")
    public void receive(org.springframework.amqp.core.Message amqpMessage, Channel channel,
                        @Header("test") String header, @DestinationVariable("test") String des,
                        @Payload String pay, Principal principal, @Headers Map<String, Object> map,
                        Message message) {

    }

    @RabbitListener(queues = "test2")
    public void receive2(org.springframework.amqp.core.Message amqpMessage, Channel channel,
                        @Header("test") String header, @DestinationVariable("test") String des,
                        @Payload String pay, Principal principal, MessageHeaders messageHeaders,
                        Message message) {

    }

    @RabbitListener(queues = "test3")
    public void receive3(org.springframework.amqp.core.Message amqpMessage, Channel channel,
                         @Header("test") String header, @DestinationVariable("test") String des,
                         @Payload String pay, Principal principal, MessageHeaderAccessor messageHeaderAccessor,
                         Message message) {

    }
}
