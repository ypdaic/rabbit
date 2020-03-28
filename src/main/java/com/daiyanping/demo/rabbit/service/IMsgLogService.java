package com.daiyanping.demo.rabbit.service;

import com.daiyanping.demo.rabbit.entity.MsgLog;

import java.util.Date;
import java.util.List;

public interface IMsgLogService {

    void updateStatus(String msgId, Integer status);

    MsgLog selectByMsgId(String msgId);

    List<MsgLog> selectTimeoutMsg();

    void updateTryCount(String msgId, Date tryTime);

    void add(MsgLog msgLog);
}
