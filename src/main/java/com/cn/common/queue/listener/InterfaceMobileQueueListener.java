package com.cn.common.queue.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.ExceptionUtil;
import com.cn.common.util.StringUtil;
import com.rabbitmq.client.Channel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 短信接听队列
 * @author chen.kai
 * date:2016-12-28
 */
public class InterfaceMobileQueueListener extends HomeListener implements ChannelAwareMessageListener{
  
	Logger log=LoggerFactory.getLogger(InterfaceMobileQueueListener.class);
	/**
	 * 发送邮件
	 * @param map
	 */
	public void sendQueueMobileCode(JSONObject map) throws Exception{
		//如果手机号为空则直接
		String retcode="0";
		String retmsg="请求成功！";
//		//获取短信配置信息
//   		Map<String,String> mobMap=getSysParameter("mobile");
//   		//取出配置信息
//		String userId=StringUtil.getString(mobMap.get("USER_ID"));
//		String pass=StringUtil.getString(mobMap.get("PWD"));
		//获取队列中的参数信息：手机号和短信内容
		JSONObject retMap=checkBody(map);
		if(null==retMap)return ;
		//取出手机模版编号
		String modelcode=StringUtil.getString(retMap.get("modelcode"));
		//校验模版编号是否合法
		String modelcontent="";
		if(!modelcode.equals("")){
			modelcontent=checkModelCode(map, modelcode);
			if(modelcontent.equals(""))return ;
		}	
		String code="";
		String type=map.getString("interfacesontype");
		if(type.equals("mobile_equal_txt_async")){
			//校验手机号是否为空
			String phone=StringUtil.getString(retMap.get("phone"));
			if("".equals(checkPhone(map, phone)))return;
			//校验短信内容是否为空
			String content=StringUtil.getString(retMap.get("content"));
			if("".equals(checkContent(retMap, content,modelcode)))return;
			code=executeEqualText(map,Msnc.mobile_user,Msnc.mobile_pass,modelcontent,retMap);
		}
		else{	
			try {
				//获取短信信息主题
				JSONArray contents=retMap.getJSONArray("contents");
				//校验信息主题是否为空
				if("".equals(checkContents(map, contents, modelcode)))return ;
				code=execteNotEqualText(contents,Msnc.mobile_user,Msnc.mobile_pass,modelcontent,map);
			} catch (Exception e) {
				insertResultLost(map, retcode, "contents数组包定义错误，非正常的JSON数组格式!");
				interfaceCallBack(map);
				e.printStackTrace();
	            return ;
			}
		}
		//获取返回码的结果
		String result=StringUtil.getString(cache.getCacheObject("mobile"+code));;
		if(result.equals("")){
			retcode="1";
			retmsg="请求成功！";
		}else retmsg=result;
		//插入执行记录
		insertResultLost(map, retcode, retmsg);
		interfaceCallBack(map);
	}

	/**
	 * 接收并且处理队列发送消息
	 */
	@Override
	public void onMessage(Message message,Channel channel) throws Exception{
		//将message转为Map对象
		JSONObject map=new JSONObject();
		boolean state=false;
		try {
			map = mapper.readValue(message.getBody(), JSONObject.class);
			//发送短信
			sendQueueMobileCode(map);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			state=true;
			log.debug(ExceptionUtil.println(e));
		}finally {
			if(state)channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false); 
		}
	}

   String  executeEqualText(JSONObject json,String userId,String pass,String modelcontent,JSONObject retmap) throws Exception{
	    int count=StringUtil.objectParseInt(retmap.get("count")) ;
	    if(count==0)count=1000;
		String phone=StringUtil.getString(retmap.get("phone"));
		String text=StringUtil.getString(retmap.get("content"));
		return mobservice.sendEqMessage(userId, pass, phone, text,modelcontent, count,json);

   } 
   
   
   String  execteNotEqualText(JSONArray contents,String userId,String pass,String modelcontent,JSONObject json) throws Exception{
		return mobservice.sendNotEqMessage(userId, pass, modelcontent, contents,json);
   }
   //校验模版编号是否合法
   String checkModelCode(JSONObject json,String modelcode){
	   //获取模版内容
	   String modelcontent=StringUtil.getString(getTemplateContent(modelcode));
		if(modelcontent.equals("")){
			checkData(json, "模版编号不存在或模版内容为空！");
		}
	   return modelcontent;
   }
   //校验手机号是否为空
   String checkPhone(JSONObject json,String phone){
	   if("".equals(phone)){
//		   insertResultLost(json, "0", "手机号为空！");
//		   interfaceCallBack(json);
		   checkData(json, "手机号为空！");
		   return "";
	   }
	   return "0";
   }
   //校验短信内容是否为空
   String checkContent(JSONObject json,String content,String modelcode){
	   if("".equals(content)&&"".equals(modelcode)){
		   checkData(json, "短信内容为空！");
		   return "";
	   }
	   return "0";
   }
   //校验短信内容包
   String checkContents(JSONObject json,JSONArray contents,String modelcode){
	   if(contents.size()<1&&"".equals(modelcode)){
		   checkData(json, "短信内容包为空！");
		   return ""; 
	   }
	   return "0";
   }
   //插入执行记录
  void insertResultLost(JSONObject json,String retcode,String retmsg){
		json.put("retcode", retcode);
		json.put("retmsg", retmsg);
		//将执行结果插入到数据库
		insertResult(json);
   }
}
