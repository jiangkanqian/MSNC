package com.cn.common.queue.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.cn.common.util.ExceptionUtil;
import com.rabbitmq.client.Channel;
import net.sf.json.JSONObject;

public class InterfaceTaskLogQueueConfirmListener extends HomeListener implements ChannelAwareMessageListener {

	Logger log=LoggerFactory.getLogger(InterfaceTaskLogQueueConfirmListener.class);
	@Override
	public void onMessage(Message message,Channel channel) throws Exception{
		// TODO Auto-generated method stub
		//将message转为Map对象
		JSONObject map=new JSONObject();
		boolean state=false;
		try {
			map = mapper.readValue(message.getBody(), JSONObject.class);
			//插入执行记录
			insertResult(map);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);  
		} catch (Exception e) {
			state=true;
			log.debug(ExceptionUtil.println(e));
		}finally {
			if(state)channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false); 
		}
	}

}
