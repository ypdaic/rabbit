package com.daiyanping.demo.rabbit.mq;

import com.daiyanping.demo.rabbit.entity.MsgLog;
import com.daiyanping.demo.rabbit.service.IMsgLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.lang.reflect.Proxy;
import java.util.Map;

@Slf4j
public class BaseConsumerProxy {

    private Object target;

    private IMsgLogService msgLogService;

    public BaseConsumerProxy(Object target, IMsgLogService msgLogService) {
        this.target = target;
        this.msgLogService = msgLogService;
    }

    public Object getProxy() {
        ClassLoader classLoader = target.getClass().getClassLoader();
        Class[] interfaces = target.getClass().getInterfaces();

        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, (proxy1, method, args) -> {
            Message message = (Message) args[0];
            Channel channel = (Channel) args[1];

            String correlationId = getCorrelationId(message);

            if (isConsumed(correlationId)) {// 消费幂等性, 防止消息被重复消费
                log.info("重复消费, correlationId: {}", correlationId);
                return null;
            }

            MessageProperties properties = message.getMessageProperties();
            long tag = properties.getDeliveryTag();

            try {
                Object result = method.invoke(target, args);// 真正消费的业务逻辑
                msgLogService.updateStatus(correlationId, MsgLog.MsgLogStatus.CONSUMED_SUCCESS.getValue());
                channel.basicAck(tag, false);// 消费确认
                return result;
            } catch (Exception e) {
                log.error("getProxy error", e);
                channel.basicNack(tag, false, true);
                return null;
            }
        });

        return proxy;
    }

    /**
     * 获取CorrelationId
     *
     * @param message
     * @return
     */
    private String getCorrelationId(Message message) {
        String correlationId = null;

        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        for (Map.Entry entry : headers.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.equals("spring_returned_message_correlation")) {
                correlationId = value;
            }
        }

        return correlationId;
    }

    /**
     * 消息是否已被消费
     *
     * @param correlationId
     * @return
     */
    private boolean isConsumed(String correlationId) {
        MsgLog msgLog = msgLogService.selectByMsgId(correlationId);
        if (null == msgLog || msgLog.getStatus().equals(MsgLog.MsgLogStatus.CONSUMED_SUCCESS.getValue())) {
            return true;
        }

        return false;
    }

}
