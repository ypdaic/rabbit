package com.daiyanping.demo.rabbit.service.impl;

import com.daiyanping.demo.rabbit.entity.MsgLog;
import com.daiyanping.demo.rabbit.mapper.MsgLogMapper;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MsgLogServiceImpl implements IMsgLogService {

    @Autowired
    MsgLogMapper msgLogMapper;

    @Override
    public void updateStatus(String msgId, Integer status) {

    }

    @Override
    public MsgLog selectByMsgId(String msgId) {
        return null;
    }

    @Override
    public List<MsgLog> selectTimeoutMsg() {
        return null;
    }

    @Override
    public void updateTryCount(String msgId, Date tryTime) {

    }

    @Override
    public void add(MsgLog msgLog) {
        msgLogMapper.insert(msgLog);
    }
}
