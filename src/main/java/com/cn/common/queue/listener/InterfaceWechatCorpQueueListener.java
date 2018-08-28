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
 * @describe  微信企业号的异步消息发送
 */
public class InterfaceWechatCorpQueueListener extends HomeListener implements ChannelAwareMessageListener{

	Logger logger=LoggerFactory.getLogger(InterfaceWechatCorpQueueListener.class);

	/**
	 * 通过监听队列，发送邮件,交由消费者消耗掉
	 * @param map
	 * @throws Exception
	 */
	public void sendQueueWechatCorp(JSONObject map){
		
		//校验body数据
		JSONObject body= checkBody(map);
	    if(null==body)return;
		String retMap=body.toString().replaceAll("\"", "\\\\\"");
		map.put("bodystr", retMap);
		//企业id
		String corpid = StringUtil.getString(body.get("corpid"));
		//管理组密钥
		String corpsecret = StringUtil.getString(body.get("corpsecret"));
		//成员ID列表
		String touser=StringUtil.getString(body.get("touser"));
		// 部门ID列表
		String toparty=StringUtil.getString(body.get("toparty"));
		//标签ID列表
		String totag=StringUtil.getString(body.get("totag"));
		// 企业应用的id
		String agentid=StringUtil.getString(body.get("agentid"));
		//消息内容
		String content =StringUtil.getString(body.get("content"));
		//模板编号
		String modelcode=StringUtil.getString(body.get("modelcode")).trim();
		//校验模板编号及内容
		String modelcontent= checkModel(map,content,modelcode);
		if(null==modelcontent)return;
		String text="\"content\":";
		String []spilt=Msnc.WECHAT_CORPTEXTDATA.split(text);
		String data=String.format(spilt[0],touser,toparty,totag,agentid)+text+spilt[spilt.length-1];
		JSONObject responceJson=new JSONObject();
		String errCode="";
		try {
			responceJson=wechatservice.sendMessage(corpid, corpsecret,Msnc.WECHAT_CORPTOKENURL,Msnc.WECHAT_CORPSENDURL,content,data,modelcontent);
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
			//发送微信企业号消息
			sendQueueWechatCorp(map);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			state=true;
			logger.debug(ExceptionUtil.println(e)); 
		}finally {
			if(state)channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false); 
		}
	}
}
