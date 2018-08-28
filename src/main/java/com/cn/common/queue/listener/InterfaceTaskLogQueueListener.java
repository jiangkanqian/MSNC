package com.cn.common.queue.listener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import net.sf.json.JSONObject;

/**
 * 插入接口执行记录监听器（队列的消费者），只支持事物模式，继承普通的MessageListener
 * @author chenkai
 * date:2017-02-15
 */
public class InterfaceTaskLogQueueListener extends HomeListener implements MessageListener {

	@Override
	public void onMessage(Message message){
		//将message转为Map对象
		JSONObject map=new JSONObject();

		try {
			map = mapper.readValue(message.getBody(), JSONObject.class);
			//插入执行记录
			insertResult(map);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}

}
