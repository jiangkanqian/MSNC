package com.cn.common.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * 邮件接口任务Service类
 * @author chenkai
 * date:2016-12-1
 */
public interface InterfaceMailService {
	
	public List<Map<String,String>> selectByNameOject();
	
	public String insertInterfaceMailLog(Map<String,String> map);
	
	public void insertInterfaceMail(Map<String, String> map);
	
	public void sendMailQueue(JSONObject object);
	
	/**
	 * 如果发送异常，那么消息就重发三次
	 * @param to
	 * @param cc
	 * @param subject
	 * @param context
	 * @param templateText
	 * @param templateFile
	 * @param fileName
	 * @param filePath
	 * @param templateFileName 
	 * @return
	 * @throws Exception
	 */
	//@Resend(times=3)
	public  String sendMail(String to,String cc, String subject, String context,String templateText,File templateFile,String fileName,String filePath,JSONObject json, String templateFileName)throws Exception;
}
