package com.cn.common.queue.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

/**
 * 消息到达确认后回调监听
 * @author chen.kai
 * date:2017-02-15
 */
@Service("confirmCallBackListener")
public class ConfirmCallBackListener implements ConfirmCallback{
	
	Logger log=LoggerFactory.getLogger(ConfirmCallBackListener.class);

	@Override
	public void confirm(CorrelationData correlationData, boolean ack) {
		log.info("confirm--:correlationData:"+correlationData+",ack:"+ack);
	}

}
