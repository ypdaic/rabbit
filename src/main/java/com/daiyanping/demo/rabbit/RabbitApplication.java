package com.daiyanping.demo.rabbit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@EnableRabbit
@MapperScan("com.daiyanping.demo.rabbit.mapper.**")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitApplication.class, args);
    }

}
