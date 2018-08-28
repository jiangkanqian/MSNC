package com.cn.common.web;





import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.common.entity.User;
import com.cn.common.service.InterfaceMailService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONObject;
/**
 * 邮件发送
 * @author chenkai
 * date:2016-11-11
 */
@Controller
@RequestMapping(value="/interface/email")
public class EmailController extends HomeController{
	private  final Logger logger=LoggerFactory.getLogger(EmailController.class);
	
	@Resource
	private InterfaceMailService mailservice;

	
	@RequestMapping(value="/sendMail", method = RequestMethod.POST ,produces = {"application/json;charset=utf-8" })
	public @ResponseBody JSONObject sendMail(@RequestBody JSONObject data) throws Exception{
		JSONObject json=JsonObjectUtil.initErrorJson();
		//初始化执行记录
        data=initTaskLog(data, json, "email_html_sync", "1", "/interface/email/sendMail","");
        //如果参数为空直接返回
        if(data.getBoolean("flag"))return json;
        JSONObject body=new JSONObject();
        try{
        	//取出主要内容
        	body=data.getJSONObject("body");
        }catch (Exception e) {
        	e.printStackTrace();
        	//new RuntimeException("body数据包定义错误，非正常的JSON格式!");
        	return checkData(data, json, "body数据包定义错误，非正常的JSON格式!");
		}
		//获取接收人
		String To=StringUtil.getString(body.get("to"));
	//	if(null!=checkEmail(data, json,To))return json;
		//获取抄送人
		String Cc=StringUtil.getString(body.get("cc"));
	//	if(null!=checkEmail(data, json,Cc))return json;
		//获取邮件内容
		String Content=StringUtil.getString( body.get("content"));
		//获取邮件主题
		String Subject=StringUtil.getString(body.get("subject"));
		//获取附件名称
		String FileName=StringUtil.getString(body.get("filename"));
		//获取附件地址
		String FilePath=StringUtil.getString(body.get("filepath"));
		String modelcode=StringUtil.getString(body.get("modelcode"));
		if("".equals(To)&&"".equals(Cc)){
			return checkData(data, json, "邮件接收人和抄送人不能同时为空") ;
		}
		if("".equals(Content)&&"".equals(FilePath)&&"".equals(modelcode)){
			return checkData(data, json, "邮件内容和附件不能同时为空")  ;
		}
		String templateFileName="/"+StringUtil.getUuId()+".ftl";
		//处理邮件模版,将模版内容加载到空白模版中
		String templatePath=Msnc.template_path.concat(templateFileName);
		String templateText="";
		//将模版构建成一个文件
		File templateFile=null;
		if(!modelcode.equals("")){
			//获取相关模版内容
			templateText=StringUtil.getString(modelservice.findModelContent(modelcode)).replaceAll("\\\\\"", "\"");
			//如果模版内容为空
			if(templateText.equals(""))return checkData(data, json, "模版编号不存在或模版内容为空");
			//将模版构建成一个文件
			 templateFile=new File(templatePath);
			 templateFile.createNewFile();
		}		
		//获取邮件配置信息
		//Map<String,String> mailMap=getSysParameter("mail");
		//String flag=StringUtil.getString(proxy.getInstance(mailservice).sendMail(To, Cc, Subject, Content,templateText,templateFile, FileName, FilePath,data));
		String flag=mailservice.sendMail(To, Cc, Subject, Content,templateText,templateFile, FileName, FilePath,data,templateFileName);
		if(flag.equals("0"))JsonObjectUtil.initSucceedJson(json);
		if(flag.equals("1"))json.put("retMsg", "模版占位参数和替换参数不一致！");
		if(flag.equals("2"))json.put("retMsg", "附件地址不合法或不存在！");
		if(flag.equals("3"))json.put("retMsg", "邮件接收者、抄送者或服务器设置有误！");
		if(flag.equals("5"))json.put("retMsg", "邮件接收者或抄送者地址错误！");
		//map和json之间可以互转
		data.put("retmsg", json.get("retMsg"));
		data.put("retcode", json.get("retCode"));
		logger.info(data.toString());
		insertResultQueue(data);
		return json;
	}
	
	
    /**
     * 邮件异步发送，先存入到邮件队列，然后有队列进行发送
     * 邮件生成者：producer
     * @param jsonStr
     * @throws Exception
     */
	@RequestMapping(value="/asyncSendMail", method = RequestMethod.POST ,produces = {"application/json;charset=utf-8" })
	public @ResponseBody JSONObject asyncSendMail(@RequestBody JSONObject map) throws Exception{       
		//初始化返回值
		JSONObject retjson=JsonObjectUtil.initErrorJson();
		String messageId=StringUtil.getUuId();
		String dotype=StringUtil.getString(map.get("dotype"));
		if(dotype.equals(""))dotype="2";
		map=initTaskLog(map, retjson, "email_html_async", dotype, "/interface/email/asyncSendMail",messageId);
		//先判断参数是否为空
		boolean flag=map.getBoolean("flag");
		//如果参数不为空，则不必再交由队列执行，直接插入到数据库
		if(!flag){
			mailservice.sendMailQueue(map);	
	       }
		retjson.clear();
		retjson.put("messageId", messageId);
		return retjson;				
	}
	
	/**
	 * 发送表格内容需要循环的接口
	 * @param data 接口参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sendSingleTableHtmlMail", method = RequestMethod.POST ,produces = {"application/json;charset=utf-8" })
	public @ResponseBody JSONObject sendSingleTableHtmlMail(@RequestBody JSONObject data) throws Exception{
		JSONObject json=JsonObjectUtil.initErrorJson();
		//初始化执行记录
        data=initTaskLog(data, json, "email_table_sync", "1", "/interface/email/sendSingleTableHtmlMail","");
        //如果参数为空直接返回
        if(data.getBoolean("flag"))return json;
        String bodystr=StringUtil.getString(data.get("body")).replaceAll("\"", "\\\\\"");
        data.put("bodystr", bodystr);
        JSONObject body=new JSONObject();
        try{
        	//取出主要内容
        	body=data.getJSONObject("body");
        }catch (Exception e) {
        	e.printStackTrace();
        	//new RuntimeException("body数据包定义错误，非正常的JSON格式!");
        	return checkData(data, json, "body数据包定义错误，非正常的JSON格式!");
		}
		//获取接收人
		String To=StringUtil.getString(body.get("to"));
		//if(null!=checkEmail(data, json,To))return json;
		//获取抄送人
		String Cc=StringUtil.getString(body.get("cc"));
		//if(null!=checkEmail(data, json,Cc))return json;
		//获取邮件内容
		String Content=StringUtil.getString( body.get("content"));
		//获取邮件主题
		String Subject=StringUtil.getString(body.get("subject"));
		//获取附件名称
		String FileName=StringUtil.getString(body.get("filename"));
		//获取附件地址
		String FilePath=StringUtil.getString(body.get("filepath"));
		String modelcode=StringUtil.getString(body.get("modelcode"));
		if("".equals(To)&&"".equals(Cc)){
			return checkData(data, json, "邮件接收人和抄送人不能同时为空！") ;
		}
		if("".equals(modelcode)){
			return checkData(data, json, "模版编号不能为空！")  ;
		}
		String templateFileName="/"+StringUtil.getUuId()+".ftl";
		//处理邮件模版,将模版内容加载到空白模版中
		String templatePath=Msnc.template_path.concat(templateFileName);
		String templateText="";
		//将模版构建成一个文件
		File templateFile=null;
		if(!modelcode.equals("")){
			//获取相关模版内容
			templateText=StringUtil.getString(modelservice.findModelContent(modelcode)).replaceAll("\\\\\"", "\"");
			//如果模版内容为空
			if(templateText.equals(""))return checkData(data, json, "模版编号不存在或模版内容为空");
			//将模版构建成一个文件
			 templateFile=new File(templatePath);
			 templateFile.createNewFile();
		}		
		//获取邮件配置信息
		//Map<String,String> mailMap=getSysParameter("mail");
		String flag=mailservice.sendMail(To, Cc, Subject, Content,templateText,templateFile, FileName, FilePath,data,templateFileName);
		//SendMailUtil.sendMail(sender,To, Cc, Subject, Content,templateText,templateFile, FileName, FilePath,mailMap);
		if(flag.equals("0"))JsonObjectUtil.initSucceedJson(json);
		if(flag.equals("1"))json.put("retMsg", "模版占位参数和替换参数不一致！");
		if(flag.equals("2"))json.put("retMsg", "附件地址不合法或不存在！");
		if(flag.equals("3"))json.put("retMsg", "邮件接收者、抄送者或服务器设置有误！");
		//map和json之间可以互转
		data.put("retmsg", json.get("retMsg"));
		data.put("retcode", json.get("retCode"));
		logger.info(data.toString());
		insertResultQueue(data);
		return json;
	}
	
	/**
     * 邮件异步发送，先存入到邮件队列，然后有队列进行发送
     * @param jsonStr
     * @throws Exception
     */
	@RequestMapping(value="/sendAsyncSingleTableHtmlMail", method = RequestMethod.POST ,produces = {"application/json;charset=utf-8" })
	public @ResponseBody JSONObject sendAsyncSingleTableHtmlMail(@RequestBody JSONObject map) throws Exception{       
		//初始化返回值
		JSONObject retjson=JsonObjectUtil.initErrorJson();
		String messageId=StringUtil.getUuId();
		String dotype=StringUtil.getString(map.get("dotype"));
		if(dotype.equals(""))dotype="2";
		map=initTaskLog(map, retjson, "email_table_sync", dotype, "/interface/email/sendAsyncSingleTableHtmlMail",messageId);
		//先判断参数是否为空
		boolean flag=map.getBoolean("flag");
		//如果参数不为空，则不必再交由队列执行，直接插入到数据库
		if(!flag){
			mailservice.sendMailQueue(map);	
	       }
		retjson.clear();
		retjson.put("messageId", messageId);
		return retjson;				
	}
	
	@RequestMapping(value="/testcache", method = RequestMethod.POST ,produces = {"application/json;charset=utf-8" })
	public @ResponseBody JSONObject testcache(@RequestBody JSONObject json) {
		List list=cache.getCacheList("list");
		if(null==list||list.size()==0){
			System.out.println("该数据没有放入缓存。。。。");
			list=new ArrayList<Map<String,String>>();
			for (int i = 0; i <2; i++) {
				Map<String,String> map=new HashMap<String,String>();
				map.put("A-"+i, "我是好人"+i+"号!");
				map.put("B="+i, System.currentTimeMillis()+"");
				list.add(map);
			}
			cache.setCacheList("list", list);
			//cache.setCacheObject("list",list, 1);
		}
		else{
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).toString());
				System.out.println("---------------------");
			}
		}
		return json;
	}

	public static void main(String[] args) {
		String  table="http://114.67.48.66:5122/MWGate/wmgw.asmx/MongateCsSpMultixMtSend?userId=JKCS01&password=123456&multixmt=*|13379396779|ob42MTU0ob8sam1ldGVyzayyvci6t6LTw7unODg0M6Oh|SvrType|P1|P2|||0|0|0|0|1,*|13522445553|M9TCNcjVz8LO56Osz7C9/Ma9ss6808qutv697MirufrIy7TzzuW0zrvh0unJz7qjtPqx7c3FtcTJ";
		String  left=table.substring(0, 106);
		String right=table.substring(106, table.length());
		
		System.out.println(left);
		System.out.println(right);
	}
}
