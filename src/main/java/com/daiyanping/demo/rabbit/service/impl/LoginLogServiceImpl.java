package com.daiyanping.demo.rabbit.service.impl;

import com.daiyanping.demo.rabbit.entity.LoginLog;
import com.daiyanping.demo.rabbit.mapper.LoginLogMapper;
import com.daiyanping.demo.rabbit.service.ILoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginLogServiceImpl implements ILoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void insert(LoginLog loginLog) {
        loginLogMapper.insert(loginLog);
    }

    @Override
    public LoginLog selectByMsgId(String msgId) {
        return loginLogMapper.selectByMsgId(msgId);
    }

}
