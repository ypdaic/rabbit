package com.daiyanping.demo.rabbit.mapper;

import com.daiyanping.demo.rabbit.entity.LoginLog;

public interface LoginLogMapper {

    void insert(LoginLog loginLog);

    LoginLog selectByMsgId(String msgId);

}
