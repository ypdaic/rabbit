package com.daiyanping.demo.rabbit.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitResourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 将channel绑定到当前线程中
 */
@Component
@Aspect
public class RabbitChannel {

    @Autowired
    ConnectionFactory connectionFactory;

    @Pointcut("@annotation(com.daiyanping.demo.rabbit.annotation.RabbitChannel)")
    public void channelPointcut() {

    }

    @Around("channelPointcut()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        RabbitResourceHolder transactionalResourceHolder = MyConnectionFactoryUtils.
                getTransactionalResourceHolder(connectionFactory, false, false);
        try {
            return point.proceed();
        } finally {
            TransactionSynchronizationManager.unbindResource(connectionFactory);
            transactionalResourceHolder.setSynchronizedWithTransaction(false);
            MyConnectionFactoryUtils.releaseResources(transactionalResourceHolder);
        }

    }
}
