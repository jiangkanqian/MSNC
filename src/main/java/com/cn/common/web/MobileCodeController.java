package com.cn.common.web;



import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.common.service.InterfaceMobileService;
import com.cn.common.service.InterfaceTaskLogService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 手机短信接口，引用SpringMVC+JSON，实现Resutful风格
 * @author chenkai
 * 
 */
@Controller
@RequestMapping(value="/interface/mobile")
public class MobileCodeController extends HomeController{

	private static final Logger logger = LoggerFactory.getLogger(MobileCodeController.class);
	
	@Resource
	private InterfaceMobileService  service;
	
	@Resource
	private InterfaceTaskLogService logservice;
	
	
	
	/**
	 * 群发手机短信相同内容，同步接口
	 * RequestBody：将json串转成Java对象 ；
       ResponseBody：将java对象转成json串；
	 * @param jsonStr 包含手机号和短信内容
	 * @return
	 */
	@RequestMapping(value="/sendEqualContent", method = { RequestMethod.GET, RequestMethod.POST },produces = {"application/json;charset=utf-8" })
	public @ResponseBody JSONObject sendEqualContent(@RequestBody JSONObject data) throws Exception{
		//初始化返回值码和返回描述
		JSONObject json=JsonObjectUtil.initErrorJson();
		//初始化执行记录
        data=initTaskLog(data, json, "mobile_equal_txt_sync", "1", "/interface/mobile/sendEqualContent","");        
        JSONObject body=new JSONObject();
        try{
        	//取出主要内容
        	body=data.getJSONObject("body");
        }catch (Exception e) {
        	e.printStackTrace();
        	//new RuntimeException("body数据包定义错误，非正常的JSON格式!");
        	return checkData(data, json, "body数据包定义错误，非正常的JSON格式!");
		}
        if(null==body) return checkData(data, json, "短信主题内容body为空！");
		//取出手机号
		String Phone=StringUtil.getString(body.get("phone"));
		//取出手机号个数
		int count=StringUtil.objectParseInt(body.get("count"));
		//防止count非法输入
		if(count==0)count=1000;
		//取出手机模版编号
		String modelcode=StringUtil.getString(body.get("modelcode"));
		//短信内容，此内容有当模版编号为空或者没有模版编号时此内容是个String,当不为空时为json格式包体
		String Content=StringUtil.getString(body.get("content"));
		String modelcontent="";
		if(!modelcode.equals("")){
			//获取模版内容
			modelcontent=StringUtil.getString(getTemplateContent(modelcode));
			if(modelcontent.equals("")){
				return checkData(data, json, "模版编号不存在或模版内容为空！");
			}
		}
	    if("".equals(Content)&&modelcontent.equals("")){
	    	return checkData(data, json, "短信内容为空！");
	    } 
	    if("".equals(Phone)){
	    	return checkData(data, json, "手机号码为空！");
	    }
	    //当模版编号为有效的模版编号时，content必须为合法的json包
	    if(!"".equals(Content)&&!modelcontent.equals("")){
	    	try {
	    		//先将字符串转为json对象，用于传输到模版，进行键值匹配
	   		 ObjectMapper mapper=new ObjectMapper();
	   		 mapper.readValue(Content, JSONObject.class);
			} catch (Exception e) {
				new RuntimeException("当模版编号有效时，content必须为合法的JSON数据");
				return checkData(data, json, "当模版编号有效时，content必须为合法的JSON数据");
			}
	    }

//		Map<String,String> mobMap=getSysParameter("mobile");	
//		long z=System.currentTimeMillis();
//		logger.info("本次数据库获取手机短信群发配置参数花费时间为："+(z-y));
//		//梦网的登陆账号
//		String userId=mobMap.get("USER_ID");
//		//梦网的密码
//		String pass=mobMap.get("PWD");
		//因为移动梦网每次最多发送1000个手机号码，所以如果接口方如果手机号码超过1000个，将对其进行分批发送	
		//String result=SendMobileCodeUtil.sendEqualContent(userId, pass, Phone, Content,modelcontent,count);
	    String result=service.sendEqMessage(Msnc.mobile_user, Msnc.mobile_pass, Phone, Content,modelcontent,count,data);
		//获取错误码
		//String msg=getResultErrMsg(result, "mobile");
		String msg=StringUtil.getString(cache.getCacheObject("mobile"+result));;
		if("".equals(msg))json=JsonObjectUtil.initSucceedJson(json);
		else json.put("retMsg", msg);
		logger.info(json.toString());
		data.put("retmsg", json.get("retMsg"));
		data.put("retcode", json.get("retCode"));
		insertResultQueue(data);
		return json;
	}
	
	 /**
     * 群发手机短信相同内容，异步接口
     * @param jsonStr
     * @throws Exception
     */
	@RequestMapping(value="/sendAsyncEqualContent", method = RequestMethod.POST,produces = {"application/json;charset=utf-8" } )
	public @ResponseBody JSONObject asyncSendMobileCode(@RequestBody JSONObject json) throws Exception{		
		
		return executMobileQueue(json,"mobile_equal_txt_async", "/interface/mobile/sendAsyncEqualContent");				
	}	
	
	/**
	 * 群发不通内容短信同步接口
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/sendNotEqualContent", method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	public @ResponseBody JSONObject sendNotEqualMobileCode(@RequestBody JSONObject data) throws Exception{
		//初始化返回值码和返回描述
		JSONObject retjson=JsonObjectUtil.initErrorJson();
		//初始化执行记录
        data=initTaskLog(data, retjson, "mobile_not_equal_txt_sync", "1", "/interface/mobile/sendNotEqualContent","");
        if(data.getBoolean("flag"))return retjson;   
        JSONObject body=new JSONObject();
        try{
        	//取出主要内容
        	body=data.getJSONObject("body");
        }catch (Exception e) {
            e.printStackTrace();
			return checkData(data, retjson, "body数据包定义错误，非正常的JSON格式!");

		}
      //获取contents短信包
        JSONArray contents=new JSONArray();
        try {
//        	System.out.println(body.get("contents").toString());
//        	byte []content=body.get("contents").toString().getBytes();
//        	body.put("contents", new String (content,"ISO-8859-1"));
        	contents=body.getJSONArray("contents");
		} catch (Exception e) {
			e.printStackTrace();
        	return checkData(data, retjson, "contents数组包定义错误，非正常的JSON数组格式!");
		}
        if(contents.size()<1){
        	return checkData(data, retjson, "contents为空!");
        }
		//获取modelcode,body.getString()当获取参数为没有时会报空指针异常
		String modelcode=StringUtil.getString(body.get("modelcode"));
		//获取模版内容
		String modelcontent="";
		if(!"".equals(modelcode)){//
			modelcontent=StringUtil.getString(getTemplateContent(modelcode));
			if("".equals(modelcontent)){
				return checkData(data, retjson, "模版编号不存在或模版内容为空！");
			}
		}
//		//获取梦网配置信息
//		Map<String,String> mobMap=getSysParameter("mobile");
//		//梦网的登陆账号
//		String userId=mobMap.get("USER_ID");
//		//梦网的密码
//		String pass=mobMap.get("PWD");	
		String result=service.sendNotEqMessage(Msnc.mobile_user, Msnc.mobile_pass,modelcontent,contents,data);
		//String msg=getResultErrMsg(result, "mobile");
		String msg=StringUtil.getString(cache.getCacheObject("mobile"+result));
		if("".equals(msg))retjson=JsonObjectUtil.initSucceedJson(data);
		else retjson.put("retMsg", msg);
		logger.info(retjson.toString());
		//map和json之间可以互转
		data.put("retmsg", retjson.get("retMsg"));
		data.put("retcode", retjson.get("retCode"));
		insertResultQueue(data);
		return retjson;
	}
	
	 /**
     * 群发手机短信相同内容，异步接口
     * @param jsonStr
     * @throws Exception
     */
	@RequestMapping(value="/sendAsyncNotEqualContent", method = RequestMethod.POST,produces = {"application/json;charset=utf-8" } )
	public @ResponseBody JSONObject sendAsyncNotEqualContent(@RequestBody JSONObject json) throws Exception{
		
		return executMobileQueue(json, "mobile_not_equal_txt_async", "/interface/mobile/sendAsyncNotEqualContent");
	}	
	
	JSONObject executMobileQueue(JSONObject json,String interfacesontype,String interfaceurl){
		//初始化返回值
		JSONObject retjson=JsonObjectUtil.initErrorJson();
		String messageId=StringUtil.getUuId();
		String dotype=StringUtil.getString(json.get("dotype"));
		if(dotype.equals(""))dotype="2";
		json=initTaskLog(json, retjson, interfacesontype, dotype, interfaceurl,messageId);
		//先判断参数是否为空
		boolean flag=json.getBoolean("flag");
		//如果参数不为空，则不必再交由队列执行，直接插入到数据库
		if(!flag){
			service.sendMobileQueue(json);	
	       }
		retjson.clear();
		retjson.put("messageId", messageId);
		return retjson;				
	}
//	public static void main(String[] args) {
//		
//		JSONObject json=new JSONObject();
//		JSONObject a=new JSONObject();
//		a.put("aa", "");
//	    json.put("a", a);
//		System.out.println(json.isNullObject());
//	}

}
