package com.cn.common.queue.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.stereotype.Service;

/**
 * 消息失败后，调用
 * @author chen.kai
 * date:2017/02/15
 */
@Service("returnCallBackListener")
public class ReturnCallBackListener implements ReturnCallback{

	Logger log=LoggerFactory.getLogger(ReturnCallBackListener.class);
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		// TODO Auto-generated method stub
		log.info(message.getBody()+",replyCode:"+replyCode+",replyText:"+replyText+",exchange:"+exchange+",routingKey:"+routingKey);
	}

}
