package com.cn.common.queue.listener;



import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.ExceptionUtil;
import com.cn.common.util.StringUtil;
import com.rabbitmq.client.Channel;

import net.sf.json.JSONObject;

/**
 * 邮件发送队列监听器
 * @author chen.kai
 * date:2016-12-28
 */
public class InterfaceMailQueueListener extends HomeListener implements ChannelAwareMessageListener{

	Logger log=LoggerFactory.getLogger(InterfaceMailQueueListener.class);
	/**
	 * 通过监听队列，发送邮件,交由消费者消耗掉
	 * @param map
	 * @throws Exception
	 */
	
	public void sendQueueEmail(JSONObject map) throws Exception{
		log.info("邮件数据为："+map);
		//获取邮件内
		JSONObject bodyMap=checkBody(map);
		if(null==bodyMap)return ;
		//接收人
		String to=StringUtil.getString(bodyMap.get("to"));
		//if(null==checkEmail(map,to))return ;
		//抄送人
		String cc=StringUtil.getString(bodyMap.get("cc"));
		//if(null==checkEmail(map,cc))return ;
		//邮件主题
		String subject=StringUtil.getString(bodyMap.get("subject"));
		//取出邮件的主题内容
		String context=StringUtil.getString(bodyMap.get("content"));
		//邮件模版编号
		String modelcode=StringUtil.getString(bodyMap.get("modelcode"));
		//附件名称
		String fileName=StringUtil.getString(bodyMap.get("filename"));
		//附件地址，此为网络地址，多个网络地址之间以英文“;”隔开
		String filePath=StringUtil.getString(bodyMap.get("filepath"));
		//获取邮件配置信息
		//Map<String,String> mailMap=getSysParameter("mail");
		//根据模版编号获取本地模版地址
		String templatePath="";
		String templateText="";
		String templateFileName="/"+StringUtil.getUuId()+".ftl";
		File file=null;
		try {
			System.out.println("======================"+templateFileName);
			if(!modelcode.equals("")){
				//"/email_blank_template.ftl"
				templatePath=Msnc.template_path.concat(templateFileName);
				file=new File(templatePath);
				file.createNewFile();
				templateText=StringUtil.getString(getTemplateContent(modelcode)).replaceAll("\\\\\"", "\"");
			}
			//获取模版内容
			log.info("邮件模内容为："+templateText);
		    //这里要进行特殊字符转移，否则存放数据时报错
			if(to.equals("")&&cc.equals("")){
				checkData(map, "请求失败，邮件接收人和抄送者不能同时为空！");
				return ;
			}	
			if(context.equals("")&&filePath.equals("")&&"".equals(templateText)){
				checkData(map, "请求失败，邮件内容为空！");
				return ;
			}
			if(!modelcode.equals("")){
				if("".equals(templateText)){
					checkData(map, "请求失败，模版编号不存在或模版内容为空!");
					return ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(ExceptionUtil.println(e));
		}
		String flag="";
		try {
			 flag=mailservice.sendMail(to, cc, subject, context,templateText,file, fileName, filePath,map,templateFileName);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	    //邮件发送
		if(flag.equals("1"))map.put("retmsg", "模版占位参数和替换参数不一致！");
		if(flag.equals("2"))map.put("retmsg", "附件地址不合法或不存在！");
		if(flag.equals("3"))map.put("retmsg", "邮件接收者、抄送者或服务器设置有误！");
		if(flag.equals("4"))map.put("retmsg", "发生未知错误，正在尝试重新连接，请10分钟后查询结果");
		if(flag.equals("5"))map.put("retmsg", "邮件接收者或抄送者地址错误！");
		if(flag.equals("0")){
			String retcode="1";
			 //将执行结果放入到Map,并且插入到数据库
		    map.put("retcode", retcode);
		    map.put("retmsg", "请求成功！");
		}
		//插入执行结果
		insertResult(map); 
		interfaceCallBack(map);
	}

    @Override 
	public void onMessage(Message message,Channel channel) throws Exception{
		JSONObject json=new JSONObject();
		boolean state=false;
		try {
			json=mapper.readValue(message.getBody(), JSONObject.class);
			sendQueueEmail(json);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			state=true;
			log.debug(ExceptionUtil.println(e));
		}finally {
			if(state)channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false); 
		}
	}
}
