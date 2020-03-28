package com.daiyanping.demo.rabbit.service;


import com.daiyanping.demo.rabbit.entity.LoginLog;

public interface ILoginLogService {

    void insert(LoginLog loginLog);

    LoginLog selectByMsgId(String msgId);

}
