package com.daiyanping.demo.rabbit.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    MyCongirmCallback myCongirmCallback;

    @Autowired
    MyReturnCallback myReturnCallback;

    /**
     * 不支持connection,channel绑定到当前线程上
     * @param connectionFactory
     * @return
     */
    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setConfirmCallback(myCongirmCallback);
        rabbitTemplate.setReturnCallback(myReturnCallback);
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    /**
     * 将connection，channel 绑定到当前线程上，而不开启rabbitmq的事务功能
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate channelRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setConfirmCallback(myCongirmCallback);
        rabbitTemplate.setReturnCallback(myReturnCallback);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    // 登录日志
    public static final String LOGIN_LOG_QUEUE_NAME = "login.log.queue";
    public static final String LOGIN_LOG_EXCHANGE_NAME = "login.log.exchange";
    public static final String LOGIN_LOG_ROUTING_KEY_NAME = "login.log.routing.key";

    @Bean
    public Queue logUserQueue() {
        return new Queue(LOGIN_LOG_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange logUserExchange() {
        return new DirectExchange(LOGIN_LOG_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding logUserBinding() {
        return BindingBuilder.bind(logUserQueue()).to(logUserExchange()).with(LOGIN_LOG_ROUTING_KEY_NAME);
    }

    // 发送邮件
    public static final String MAIL_QUEUE_NAME = "mail.queue";
    public static final String MAIL_EXCHANGE_NAME = "mail.exchange";
    public static final String MAIL_ROUTING_KEY_NAME = "mail.routing.key";


    @Bean
    public Queue mailQueue() {
        // 队列进行持久化
        return new Queue(MAIL_QUEUE_NAME, true, false, false);
    }

    @Bean
    public DirectExchange mailExchange() {
        // 交换器进行持久化，并且该exchange没有被使用时不自动删除
        return new DirectExchange(MAIL_EXCHANGE_NAME, true, false);
//        return new FanoutExchange(MAIL_EXCHANGE_NAME, false, true);
    }

    @Bean
    public Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTING_KEY_NAME);
//        return BindingBuilder.bind(mailQueue()).to(mailExchange());
    }

    /**
     * 当给AbstractRabbitListenerContainerFactory 指定事物管理器后，消费者就会开启事物进行消费，业务异常后会进行回滚
     * @param connectionFactory
     * @return
     */
    @Bean
    RabbitTransactionManager getRabbitTransactionManager(ConnectionFactory connectionFactory) {
        MyRabbitTransactionManager rabbitTransactionManager = new MyRabbitTransactionManager(connectionFactory);
        return rabbitTransactionManager;
    }

}
