package com.cn.common.queue.listener;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.binding.MapperMethod.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.cn.common.service.InterfaceRepeatSendTaskService;
import com.cn.common.util.ExceptionUtil;
import com.rabbitmq.client.Channel;

import net.sf.json.JSONObject;

public class InterfaceRepeatSendTaskListener extends HomeListener implements ChannelAwareMessageListener{
   
	Logger log=LoggerFactory.getLogger(InterfaceRepeatSendTaskListener.class);
	@Resource
	private InterfaceRepeatSendTaskService repeatService;
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		//将message转为Map对象
				JSONObject map=new JSONObject();
				boolean state=false;
				try {
					map = mapper.readValue(message.getBody(), JSONObject.class);
					//String datastr=map.getString("data").replaceAll("\\\\\"", "\"");
					//JSONObject json=mapper.readValue(datastr, JSONObject.class);;
					//插入执行记录
					String url=map.getString("interfaceurl");
					//url=url.substring(url.lastIndexOf("/")+1,url.length());
					//通过反射机制，获取当前类的相应方法
					Method method=repeatService.getClass().getMethod(url.substring(url.lastIndexOf("/")+1,url.length()), JSONObject.class);
                    //执行改方法
					method.invoke(repeatService, map);
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);  
				}  catch (Exception e) {
					state=true;
					log.debug(ExceptionUtil.println(e));
				}finally {
					if(state)channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false); 
				}
	}

}
