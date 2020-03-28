package com.daiyanping.demo.rabbit.proxy;

import lombok.Data;

@Data
public class Test {

    private String name;

    public Test(String name) {
        this.name = name;
    }

    public void say() {
        System.out.println("xxxxx");
    }
}
