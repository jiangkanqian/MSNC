package com.cn.common.queue.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.ExceptionUtil;
import com.cn.common.util.StringUtil;

import com.rabbitmq.client.Channel;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年1月3日
 * @describe  微信订阅号类发送队列监听器
 */
public class InterfaceWechatAppQueueListener extends HomeListener implements ChannelAwareMessageListener{

	Logger logger=LoggerFactory.getLogger(InterfaceWechatAppQueueListener.class);
	/**
	 * 通过监听队列，发送微信订阅号类,交由消费者消耗掉
	 * @param map
	 * @throws Exception
	 */
	public void sendQueueWechatApp(JSONObject map){	
		
		//校验body数据
		JSONObject body= checkBody(map);
	    if(null==body)return ;
		String retMap=body.toString().replaceAll("\"", "\\\\\"");
		map.put("bodystr", retMap);
		// 用户唯一凭证；
		String appid = StringUtil.getString(body.get("appid")).trim();
		//用户唯一凭证密钥；
		String secret = StringUtil.getString(body.get("secret")).trim();
		// 发送消息文本内容
		String content = StringUtil.getString(body.get("content"));
		//模板编号
		String modelcode=StringUtil.getString(body.get("modelcode")).trim();
		//校验模板编号及内容
		String modelcontent= checkModel(map,content,modelcode);
		if(null==modelcontent)return;
		JSONObject responceJson=new JSONObject();
		String errCode="";
		try {
			responceJson = wechatservice.sendMessage(appid, secret,Msnc.WECHAT_APPTOKENURL,Msnc.WECHAT_APPSENDURL,content,Msnc.WECHAT_APPTEXTDATA,modelcontent);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(ExceptionUtil.println(e));
			if(e.getMessage().indexOf("connect timed out")!=0){
				 responceJson.put("errcode", "4");
					//插入重发数据，先去掉里面的bodystr
					Map<String, String> repeatSendData=new HashMap<String, String>();
					map.put("messageId", map.getString("messageId"));
					map.put("interface_url",map.getString("interfaceurl"));
					map.put("content", map.getString("body"));
					insertRepeatTaskMapper(repeatSendData);
			 }else{
				responceJson.put("errcode", "-999"); 
			 }
		}
		errCode=StringUtil.getString(responceJson.get("errcode"));
		if(errCode.equals("0")){
		    map.put("retcode", 1);
		}else{
			map.put("retcode", 0);
		}
		checkData(map, cache.getCacheObject("wechat"+errCode).toString());
		interfaceCallBack(map);
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		
		//将message转为Map对象
		JSONObject map=new JSONObject();
		boolean state=false;
		try {
			map = mapper.readValue(message.getBody(), JSONObject.class);
			//发送微信订阅号
			sendQueueWechatApp(map);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			state=true;
			logger.debug(ExceptionUtil.println(e));
		}finally {
			if(state)channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false); 
		}	
	}
}
