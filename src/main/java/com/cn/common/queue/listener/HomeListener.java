package com.cn.common.queue.listener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import com.cn.common.service.InterfaceErrorCodeService;
import com.cn.common.service.InterfaceMailService;
import com.cn.common.service.InterfaceService;
import com.cn.common.service.InterfaceMobileService;
import com.cn.common.service.InterfaceRepeatSendTaskService;
import com.cn.common.service.InterfaceTaskLogService;
import com.cn.common.service.InterfaceWechatService;
import com.cn.common.service.ModelService;
import com.cn.common.service.SysParameterService;
import com.cn.common.util.PoolingHttpClientUtil;
import com.cn.common.util.RedisCacheUtil;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 监听根类，实现了获取当前接口的配置以及错误码返回值
 * @author chen.kai
 * date:2017-01-03
 */

public class HomeListener {

	private static final Logger logger = LoggerFactory.getLogger(HomeListener.class);

	@Resource
	private InterfaceService interfaceService;
	
	@Resource
	InterfaceRepeatSendTaskService interfaceRepeatSendTaskService;
	
	@Resource
	private SysParameterService service;
	
	@Resource
	SysParameterService sysservice;
	
	@Resource
	InterfaceErrorCodeService InterfaceErrorCodeService;
	
	@Resource
	InterfaceTaskLogService tasklogService;
	
	@Resource
    ModelService modelservice;
	
	@Resource
	JavaMailSender sender;
	
	@Resource
	InterfaceMailService mailservice;
	
	@Resource
	InterfaceMobileService  mobservice;

	@Resource
	InterfaceWechatService wechatservice;
	
	@Resource
	RedisCacheUtil<String> cache;
	
	ObjectMapper mapper=new ObjectMapper();
	/**
	 * 通过错误编码和接口子类型，获取错误描述
	 * @param errCode 错误编码
	 * @param interfaceSonType 接口子类型
	 * @return 错误码代码的错误描述
	 */
	 String getResultErrMsg(String errCode,String interfaceSonType){
		//查找errcode的详细描述sql的parameter
		Map<String, String> paramMap= new HashMap<String, String>();
		//调用接口的回码
		paramMap.put("errcode",errCode);
		//接口的子类型
		paramMap.put("interfaceSonType", interfaceSonType);
		Map<String, Object> errMap = InterfaceErrorCodeService.findErrMsgByInterfaceSonTypeAndErrcode(paramMap);
		return errMap==null?"":errMap.get("errmsg").toString();
	}
	 public void insertRepeatTaskMapper(Map<String,String> map){
		 interfaceRepeatSendTaskService.insert(map);
	 }

		/**
		 * 获取各个接口的系统配置参数
		 * @param sysType 接口参数类型
		 * @return 返回配置信息的所有数据集合
		 */
		Map<String,String> getSysParameter(String sysType){
			//声明一个Map
			Map<String,String> mobMap=new HashMap<String,String>();
			//获取手机短信的配置文件
			List<Map> list=sysservice.findBySysTypeAll(sysType);
			//取出其配置信息
			for (Map<String,String> map : list) {
				mobMap.put(map.get("sysname"), map.get("sysvalue"));
			}
			return mobMap;
		}
		
		/**
		 * 插入接口执行后的执行结果
		 * @param map
		 */
		void insertResult(Map<String,String> map){
			tasklogService.insertInterfaceTaskLog(map);
		}
		

		/**
		 * 根据模版编号获取模版内容
		 * @param modelcode
		 * @return
		 */
		String getTemplateContent(String modelcode){
		return	modelservice.findModelContent(modelcode);
		}
		
		//将不合法的数据插入数据库，并且返回给调用的客户端
		void checkData(JSONObject data,String retmsg){
	    	data.put("retmsg", retmsg);
	    	//插入不合法数据
	    	insertResult(data);
	    	//执行回调函数
	    	interfaceCallBack(data);
		}
//		/**
//		 * 
//		 * @author yangjing 
//		 * @param map
//		 * @return JSONObject
//		 * @describe 校验body的内容
//		 */
//		JSONObject checkBody(JSONObject map){
//			//获取微信订阅号类内容
//			 JSONObject body=null;
//		     try{
//		       //取出主要内容
//		        body=map.getJSONObject("body");
//		     }catch (Exception e) {
//		        e.printStackTrace();
//		     //new RuntimeException("body数据包定义错误，非正常的JSON格式!");
//		        checkData(map,"body数据包定义错误，非正常的JSON格式!");
//		    	interfaceCallBack(map);
//			}
//			if(null==body) {
//			     checkData(map,"主题内容body为空！"); 
//			     interfaceCallBack(map); 
//			}
//		    return body;
//		}
		/**
		 * 
		 * @author yangjing 
		 * @param map
		 * @return JSONObject
		 * @describe 校验body的内容
		 */
		JSONObject checkBody(JSONObject map){
			//获取微信订阅号类内容
			 JSONObject body=null;
		     try{
		       //取出主要内容
		        body=map.getJSONObject("body");
		     }catch (Exception e) {   
		        body=null;
		     //new RuntimeException("body数据包定义错误，非正常的JSON格式!");
		        checkData(map,"body数据包定义错误，非正常的JSON格式!");
		    	e.printStackTrace();
			}
		    return body;
		}
		/**
		 * 
		 * @author yangjing 
		 * @param email
		 * @return boolean
		 * @describe 校验邮箱是否正确
		 */
		 public  JSONObject checkEmail(JSONObject data,String emailAddresss) {
			 String regexEmail = "^(\\w|\\.|-|\\+)+@(\\w|-)+(\\.(\\w|-)+)+$";
			 String [] emails=emailAddresss.split(";");
			 for (String email : emails) {
				 if(!Pattern.matches(regexEmail, email.trim())){
					checkData(data,"“"+email+"”该邮箱地址为无效地址！");
					return null;
				 }
			}
			return data;
		}
		/**
		 * 
		 * @author yangjing 
		 * @param map
		 * @param content
		 * @param modelcode
		 * @return String
		 * @describe 校验模板编号及其内容
		 */
		String checkModel(JSONObject map,String content,String modelcode){
			String modelcontent="";
			if(!modelcode.equals("")){
				//获取模版内容
				modelcontent=StringUtil.getString(getTemplateContent(modelcode));
				if(modelcontent.equals("")){
					 checkData(map,"模版编号不存在或模版内容为空！");
					 return null;
				}
			}
			logger.info("模板编号："+modelcode+"微信内容为："+modelcontent);
		    //当模版编号为有效的模版编号时，content必须为合法的json包
		    if(!"".equals(content)&&!modelcontent.equals("")){
		    	try {
		    		//先将字符串转为json对象，用于传输到模版，进行键值匹配
		   		 ObjectMapper mapper=new ObjectMapper();
		   		 mapper.readValue(content, JSONObject.class);
				} catch (Exception e) {
					new RuntimeException("当模版编号有效时，content必须为合法的JSON数据");
					checkData(map,"当模版编号有效时，content必须为合法的JSON数据");
					return null;
				}
		    }
		    return modelcontent;
		}
		
		JSONArray checkJSONArray(JSONObject json,String arrname){
			try {
				//获取短信信息主题
				JSONArray contents=json.getJSONArray(arrname);
				 if(contents.size()<1){
					   checkData(json, arrname+"数组内容为空！");
					   return null; 
				   }
				return contents;
			} catch (Exception e) {
				checkData(json, arrname+"为非合法的JSONArray数组！");
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * 
		 * @author yangjing 
		 * @param data  接口的参数
		 * @return void
		 * @describe 用于回调
		 */
		void interfaceCallBack(JSONObject data) {
			
			try {
				String callbackurl=StringUtil.getString(data.get("callbackurl"));
				if(!"".equals(callbackurl)){
					data.remove("bodystr");
					data.remove("body");
					logger.info("回调参数:" +data);
					data=PoolingHttpClientUtil.post(callbackurl, data.toString());
					logger.info("回调接口了:" +data);
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("回调接口失败了");
			}	
			logger.info("回调接口了:" +data);
		}


}
