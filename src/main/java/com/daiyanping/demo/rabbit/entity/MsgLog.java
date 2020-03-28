package com.daiyanping.demo.rabbit.entity;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.demo.rabbit.util.JodaTimeUtil;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MsgLog {

    private String msgId;
    private String msg;
    private String exchange;
    private String routingKey;
    private Integer status;
    private Integer tryCount;
    private Date nextTryTime;
    private Date createTime;
    private Date updateTime;

    public MsgLog(String msgId, Object msg, String exchange, String routingKey) {
        this.msgId = msgId;
        this.msg = JSONObject.toJSONString(msg);
        this.exchange = exchange;
        this.routingKey = routingKey;

        this.status = MsgLogStatus.DELIVERING.getValue();
        this.tryCount = 0;

        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        // 下次重试时间增加一分钟
        this.nextTryTime = (JodaTimeUtil.plusMinutes(date, 1));
    }

    public enum  MsgLogStatus {
        // 消息投递中
        DELIVERING(0),
        // 投递成功
        DELIVER_SUCCESS(1),
        // 投递失败
        DELIVER_FAIL(2),
        // 已消费
        CONSUMED_SUCCESS(3);

        private Integer value;

        MsgLogStatus(Integer value) {
            this.value = value;

        }

        public Integer getValue() {
            return this.value;
        }
    }
}
