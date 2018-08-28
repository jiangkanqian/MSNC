package com.cn.common.web;


import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.JsonNode;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cn.common.entity.User;
import com.cn.common.service.InterfaceErrorCodeService;
import com.cn.common.service.InterfaceService;
import com.cn.common.service.InterfaceTaskLogService;
import com.cn.common.service.ModelService;
import com.cn.common.service.SysParameterService;
import com.cn.common.util.DateUtil;
import com.cn.common.util.DesUtils;
import com.cn.common.util.MobileUtil;
import com.cn.common.util.RedisCacheUtil;
import com.cn.common.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;


/**
 * Contorller根类
 * @author chenkai
 * date:2016-10-21
 */
@Controller
@RequestMapping(value="/home")
public class HomeController{
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static int appCount=1;
	//Spring  jackjson转换器
	ObjectMapper mapper=new ObjectMapper();
	
	@Resource
	SysParameterService service;
	
	@Resource
	InterfaceErrorCodeService InterfaceErrorCodeService;
	
	@Resource
	private  InterfaceTaskLogService tasklogService;
	
	@Resource
	private InterfaceService interfaceService;
	
	@Resource
    ModelService modelservice;
	
//	@Resource
//	ResendProxy proxy;
	
	
	@Resource
	RedisCacheUtil<String> cache;
	/**
	 * 获取各个接口的系统配置参数
	 * @param sysType 接口参数类型
	 * @return 返回配置信息的所有数据集合
	 */
	@ResponseBody
	@RequestMapping(value = "/testMobile")
	public JSONObject testMobile(@RequestBody JSONObject json){
		JSONObject one=new JSONObject();
		one.put("errcode", "0");
		 List list = json.getJSONArray("data");
//		    Map map = new HashMap();
//		    map.put("phone", "15989490621");
//		    map.put("content", "测试短信1");
//		    Map map1 = new HashMap();
//		    map1.put("phone", "15712094321");
//		    map1.put("content", "测试短信2");
//		    list.add(map);
//		    list.add(map1);
		    String date=null;
			try {
				date = MobileUtil.sendNotEqualsText(list);
			} catch (Exception e) {
			
				e.printStackTrace();
			}
		one.put("errmsg", date);
		return one;
	}
	@ResponseBody
	@RequestMapping(value = "/simulateWechat")
	public JSONObject test(@RequestBody JSONObject json){
		JSONObject one=new JSONObject();
		one.put("errcode", "0");
		one.put("errmsg", "请求成功");
		String data="";
		if(json!=null){
			data=json.toString().replaceAll("\"", "\\\\\"");
		}
		logger.info("第"+HomeController.appCount+"次，"+data);
		HomeController.appCount++;
		List<Map> list=service.findBySysTypeAll("wechat");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		return one;
	}
	/**
	 * 
	 * @author yangjing 
	 * @param url 接口的路径
	 * @return 
	 * @return String 接口的子类型
	 * @describe 查询该接口的子类型
	 */
	protected String getInterfaceSonType(String url){
		 HashMap<String,String> map=new HashMap<String,String>();
		 map.put("url", url);
		return interfaceService.queryInterfaceSonType(map);
		
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * @return 
	 * @return Integer
	 * @describe 流量监控
	 */
	protected Integer flowMonitoring(JSONObject map){
		return tasklogService.flowMonitoring(map);
	}
	
	Map<String,String> getSysParameter(String sysType){
		//声明一个Map
		Map<String,String> mobMap=new HashMap<String,String>();
		//获取手机短信的配置文件
		List<Map> list=service.findBySysTypeAll(sysType);
		//取出其配置信息
		for(int i = 0, len = list.size() ; i < len; i++){
			mobMap.put(list.get(i).get("sysname").toString(), list.get(i).get("sysvalue").toString());
		}
		logger.info(sysType+"的类型数据为："+mobMap.toString());
		return mobMap;
	}
	
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
	
	/**
	 * 分页案例实现,以供参考
	 * @param model 模型
	 * @param page 页码，默认从第一页开始
	 * @param pageSize 条数，显示条数
	 * @param List 分页对象
	 * @return
	 */
	@RequestMapping("showcity")
    public ModelAndView showCityList(ModelAndView mv, @RequestParam(required=true,defaultValue="1") Integer page,
            @RequestParam(required=false,defaultValue="10") Integer pageSize){
		//开始分页，这一行代码必须写在查询语句之上才有效果
        PageHelper.startPage(page, pageSize);
        List<Map> list = service.findBySysTypeAll("");
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        mv.addObject("list", list);
        mv.addObject("page", p);
        mv.setViewName("weather/showCityList");
        return mv;
    }
	/**
	 * Simply selects the home view to render by returning its name.
	 */
/*	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Map home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return model.asMap();
	}*/
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody 
	public Map<String, Object> home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		return model.asMap();
		
		//return model;
		//return "index";
	}
	
	@RequestMapping(value = "/encode", method = RequestMethod.POST, produces = {"application/json; charset=utf-8" })
	public void encode(HttpServletRequest request,HttpServletResponse response,@RequestBody String str) {
		if(str!=null){
			String regStr=str.substring(str.indexOf(":")+2, str.lastIndexOf("\""));
			String encodeStr=DesUtils.encode(regStr);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			try {
				ServletOutputStream out=response.getOutputStream();
				out.print(encodeStr);
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value = "/decode", method = RequestMethod.POST, produces = {"application/json; charset=utf-8" })
	public void decode(HttpServletRequest request,HttpServletResponse response,@RequestBody String str) {
		if(str!=null){
			String regStr=str.substring(str.indexOf(":")+2, str.lastIndexOf("\""));
			String decode=DesUtils.decode(regStr);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			try {
				ServletOutputStream out=response.getOutputStream();
				out.write(decode.getBytes("utf-8"));
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 插入接口执行后的执行结果,用消息队列插入
	 * @param map
	 */
	protected void insertResultQueue(JSONObject json){
		tasklogService.insertInterfaceTaskLogQueue(json);
	}
	
	/**
	 * 插入接口执行后的执行结果,用消息队列插入
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
	
	protected JSONObject initTaskLog(JSONObject data,JSONObject retjson,String interfacesontype,String dotype,String interfaceurl,String messageId){	
		//初始化messageID
		boolean flag=checkJSONisNull(data);
		if(null==data)data=new JSONObject();
		//初始化参数
		String bodystr=StringUtil.getString(data).replaceAll("\"", "\\\\\"");
	    data.put("bodystr", bodystr);
		data.put("messageId",messageId.equals("")?StringUtil.getUuId():messageId);
		//初始化接口类型
		data.put("interfacesontype", interfacesontype);
		//如果当前执行类型为空，则插入异步执行类型（1=同步，2=异步，3=定时）
		data.put("dotype",dotype);
		data.put("retcode", retjson.get("retCode"));
		data.put("retmsg", retjson.get("retMsg"));
		data.put("fromtime", DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		data.put("interfaceurl",interfaceurl);//初始化当前接口url
		data.put("flag", flag);
		if(flag)insertResultQueue(data);
		return data;
	}
	
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * @return JSONObject
	 * @describe 校验body的内容
	 */
	protected JSONObject checkBody(JSONObject map,JSONObject json){
		//获取微信订阅号类内容
		 JSONObject body=null;
	     try{
	       //取出主要内容
	        body=map.getJSONObject("body");
	     }catch (Exception e) {
	        e.printStackTrace();
	     //new RuntimeException("body数据包定义错误，非正常的JSON格式!");
	        checkData(map,json,"body数据包定义错误，非正常的JSON格式!");
	        return null;
		}
	    return body;
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
	String checkModel(JSONObject map,JSONObject json,String content,String modelcode){
		String modelcontent="";
		if(!modelcode.equals("")){
			//获取模版内容
			modelcontent=StringUtil.getString(getTemplateContent(modelcode));
			if(modelcontent.equals("")){
				 checkData(map,json,"模版编号不存在或模版内容为空！");
				 return null;
			}
		}
		logger.info("模板编号："+modelcode+"微信内容为："+modelcontent);
	    //当模版编号为有效的模版编号时，content必须为合法的json包
	
	    if(!"".equals(content)&&null!=modelcontent&&!"".equals(modelcontent)){
	    	try {
	    		//先将字符串转为json对象，用于传输到模版，进行键值匹配
	   		 mapper.readValue(content, JSONObject.class);
			} catch (Exception e) {
				new RuntimeException("当模版编号有效时，content必须为合法的JSON数据");
				checkData(map,json,"当模版编号有效时，content必须为合法的JSON数据");
				return null;
			}
	    }
	    return modelcontent;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param email
	 * @return boolean
	 * @describe 校验邮箱是否正确
	 */
	 public  JSONObject checkEmail(JSONObject data,JSONObject retjson,String emailAddresss) {
		 String regexEmail = "^(\\w|\\.|-|\\+)+@(\\w|-)+(\\.(\\w|-)+)+$";
		 String [] emails=emailAddresss.split(";");
		 for (String email : emails) {
			 if(!Pattern.matches(regexEmail, email.trim())){
				retjson.put("retMsg", "“"+email+"”该邮箱地址为无效地址!");
			    data.put("retmsg", "“"+email+"”该邮箱地址为无效地址!");
			    insertResultQueue(data);
				return retjson;
			 }
		}
		return null;
	}
	//将不合法的数据插入数据库，并且返回给调用的客户端
		JSONObject checkData(JSONObject data,JSONObject retjson,String retmsg){
			retjson.put("retMsg", retmsg);
	    	data.put("retmsg", retmsg);
	    	insertResultQueue(data);
	    	return retjson;
		}
		
		boolean checkJSONisNull(JSONObject data){
			boolean flag=false;
			if(null==data){
				flag=true;
			}
			else{
				if(data.size()<1){
					flag=true;
				}
			}
			return flag;
		}
		
		//测试spring-session-redis缓存情况，此处为存值
		@RequestMapping(value="/testcache")
		public @ResponseBody JSONObject testcache(HttpServletRequest request) {
	        User user=new User();
	        user.setUserId("1");
	        user.setUserName("张三");
	        request.getSession().setAttribute("zhangsan_1", user);
			return null;
		}
		
		//测试spring-session-redis缓存情况，此处为取值
		@RequestMapping(value="/testcache1")
		public @ResponseBody User testcache1(HttpServletRequest request) {
			User user=(User)request.getSession().getAttribute("zhangsan_1");
			System.out.println(user.getUserId()+"------------"+user.getUserName());	
			return user;
		}
}
