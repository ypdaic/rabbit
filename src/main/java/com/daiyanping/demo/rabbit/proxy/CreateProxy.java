package com.daiyanping.demo.rabbit.proxy;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CreateProxy implements SmartLifecycle {

    @Autowired
    MyMethodInterceptor myMethodInterceptor;

    @Bean
    public Test getTest() {
        Test test = new Test("test");
        ProxyFactory factory = new ProxyFactory();
        factory.addAdvisor(new DefaultPointcutAdvisor(myMethodInterceptor));
//        factory.addInterface(Test.class);
        factory.setTarget(test);
        test = (Test) factory.getProxy(Test.class.getClassLoader());
        return test;
    }

    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\");
        Test test = new Test("test");
        ProxyFactory factory = new ProxyFactory();
        NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor = new NameMatchMethodPointcutAdvisor(new MyMethodInterceptor());
        nameMatchMethodPointcutAdvisor.setMappedName("say");
        factory.addAdvisor(nameMatchMethodPointcutAdvisor);
        // 设置为true后，就不会走我们业务指定的代理，而是走StaticDispatcher，
        factory.setFrozen(true);
//        factory.addInterface(Test.class);
        factory.setTarget(test);
        test = (Test) factory.getProxy(Test.class.getClassLoader());
        test.getName();
    }

    @Override
    public void start() {
        Test test = getTest();
        test.say();
//        test.hashCode();
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
