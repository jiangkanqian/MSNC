package com.cn.common.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.common.service.InterfaceWechatService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONObject;
import java.util.regex.Pattern;
/**
 * 
 * @author yangjing
 * @date 2016年12月21日
 * @describe 微信接口管理
 */
@Controller
@RequestMapping(value = "/interface/wechat")
public class WechatController extends HomeController {
	
	private  final Logger logger=   LoggerFactory.getLogger(WechatController.class);
	private static int appCount=1;
	private static int corpCount=1;
	@Resource
	InterfaceWechatService wechatService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param data 
	 * appid 公众号唯一凭证；
	 * secret  公众号唯一凭证密钥；
	 * content  发送消息文本内容
	 * @throws IOException 
	 * @return JSONObject
	 * @describe 微信公众号发送text消息
	 */
	@ResponseBody
	@RequestMapping(value = "/sendAppMessage", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json; charset=utf-8" })
	public JSONObject sendAppMessage(@RequestBody JSONObject data){
		
		//初始化返回值码和返回描述
		JSONObject json=JsonObjectUtil.initErrorJson();
		//初始化执行记录
        data=initTaskLog(data, json, "wechat_app_txt_sync", "1", "/msnc/interface/wechat/sendAppMessage","");        
        json.put("messageId", data.get("messageId"));
        JSONObject body=checkBody(data,json);
        if(null==body) return json;
		// 用户唯一凭证；
		String appid =StringUtil.getString(body.get("appid")).trim();
		//用户唯一凭证密钥；
		String secret = StringUtil.getString(body.get("secret")).trim();
		// 发送消息文本内容
		String content = StringUtil.getString(body.get("content"));
		//模板编号
		String modelcode=StringUtil.getString(body.get("modelcode")).trim();
		String modelcontent=checkModel(data,json,content,modelcode);
		if(null==modelcontent){
			return json;
		}
		JSONObject responceJson=sendMessage(appid, secret,Msnc.WECHAT_APPTOKENURL,Msnc.WECHAT_APPSENDURL,content,Msnc.WECHAT_APPTEXTDATA,modelcontent);
		setResultMsg(data,json,responceJson);
		logger.info("第"+WechatController.appCount+"次，"+json.toString());
		WechatController.appCount++;
		return json;
	}
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 *   corpid 企业id
	 *   corpsecret 管理组密钥
	 *   touser 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送 
	 *   toparty 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 
	 *   totag 标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 
	 *   agentid 企业应用的id，整型。可在应用的设置页面查看 
	 *   content 消息内容，最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
	 * @throws IOException 
	 * @return JSONObject
	 * @describe 微信企业号发送text消息
	 */
	@ResponseBody
	@RequestMapping(value = "/sendCorpMessage", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json; charset=utf-8" })
	public JSONObject sendCorpMessage(@RequestBody JSONObject data) {

		//初始化返回值码和返回描述
		JSONObject json=JsonObjectUtil.initErrorJson();
		//初始化执行记录
        data=initTaskLog(data, json, "wechat_corp_txt_sync", "1", "/msnc/interface/wechat/sendCorpMessage","");        
        json.put("messageId", data.get("messageId"));
        JSONObject body=checkBody(data,json);
        if(null==body) return json;
		//企业id
		String corpid = StringUtil.getString(body.get("corpid")).trim();
		//管理组密钥
		String corpsecret = StringUtil.getString(body.get("corpsecret")).trim();
		//成员ID列表
		String touser=StringUtil.getString(body.get("touser"));
		// 部门ID列表
		String toparty=StringUtil.getString(body.get("toparty"));
		//标签ID列表
		String totag=StringUtil.getString(body.get("totag"));
		// 企业应用的id
		String agentid=StringUtil.getString(body.get("agentid"));
		//消息内容
		String content = StringUtil.getString(body.get("content"));
		//模板编号
		String modelcode=StringUtil.getString(body.get("modelcode")).trim();
		//校验模板
		String modelcontent=checkModel(data,json,content,modelcode);
		if(null==modelcontent){
			return json;
		}
		String text="\"content\":";
		String []spilt=Msnc.WECHAT_CORPTEXTDATA.split(text);
		String jsonData=String.format(spilt[0],touser,toparty,totag,agentid)+text+spilt[spilt.length-1];
		JSONObject responceJson=sendMessage(corpid, corpsecret,Msnc.WECHAT_CORPTOKENURL,Msnc.WECHAT_CORPSENDURL,content,jsonData,modelcontent);
		setResultMsg(data,json,responceJson);
		logger.info("第"+WechatController.corpCount+"次，"+json.toString());
		WechatController.corpCount++;
		return json;
	}
	/**
	 * 
	 * @author yangjing 
	 * @param json
	 * @return JSONObject
	 * @describe 订阅号异步消息发送
	 */
	@ResponseBody
	@RequestMapping(value = "/asyncAppSendMessage", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json; charset=utf-8" })
	public JSONObject asyncAppSendMessage(@RequestBody JSONObject json) {
		
		return executWechatQueue(json,"wechat_app_txt_async","/msnc/interface/wechat/asyncAppSendMessage","app");
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param json
	 * @return JSONObject
	 * @describe 企业号异步消息发送
	 */
	@ResponseBody
	@RequestMapping(value = "/asyncCorpSendMessage", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json; charset=utf-8" })
	public JSONObject asyncCorpSendMessage(@RequestBody JSONObject json) {
		
		return executWechatQueue(json,"wechat_corp_txt_async","/msnc/interface/wechat/asyncCorpSendMessage","corp");
	}
	
	JSONObject executWechatQueue(JSONObject json,String interfacesontype,String interfaceurl,String type){
		//初始化返回值
		JSONObject retjson=JsonObjectUtil.initErrorJson();
		//先生成messageID，供用户查看，异步调用查看本次请求结果
		String messageId=StringUtil.getUuId();
		//如果当前执行类型为空，则插入异步执行类型（1=同步，2=异步，3=定时）
		String dotype=StringUtil.getString(json.get("dotype"));
		if(dotype.equals(""))dotype="2";
		json=initTaskLog(json, retjson, interfacesontype, dotype, interfaceurl,messageId);
		//先判断参数是否为空
		boolean flag=json.getBoolean("flag");
		//如果参数不为空，则不必再交由队列执行，直接插入到数据库
		if(!flag){
			if("app".equals(type)){
				wechatService.sendAppQueue(json); 
			}else if("corp".equals(type)){
				wechatService.sendCorpQueue(json); 
			}		
	     }
		retjson.clear();
		retjson.put("messageId", messageId);
		return retjson;				
	}
	/**
	 * 
	 * @author yangjing 
	 * @param id 公众号的id
	 * @param secret 公众号的密钥
	 * @param getTokenUrl 获取token的url
	 * 	(获取企业号token的url)：
	 *  https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1};
	 *  (微信公众号的获取token的url)：
	 *  https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}
	 * @param sendMsgUrl   发送消息的url
	 *  (微信公众号发送消息的url)：
	 *	https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token={0};
	 *	( 发送企业号消息的url)：
	 *	https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}
	 * @param sendJsonData 发送消息的json数据包
	 *	(企业号发送文本消息的数据包)：
	 *	{"touser": "%s","toparty": "%s ","totag": "%s ","msgtype": "text","agentid": %s,"text": {"content": "%s"},"safe":0};
	 *	(公众号发送消息的json包)：
	 *	{"filter":{"is_to_all":true},"text":{"content":"%s"},"msgtype":"text"}
	 * @param content 消息内容
	 * @param modelcontent  模板内容
	 * @return JSONObject 
	 * @describe 用id与secret调用微信接口获取公众号的access_token并发送content
	 */
	private JSONObject sendMessage(String id, String secret, String getTokenUrl, String sendMsgUrl,
			String content, String sendJsonData, String modelcontent) {
		
		JSONObject responceJson=new JSONObject();
		try {
			responceJson = wechatService.sendMessage(id, secret,getTokenUrl,sendMsgUrl,content,sendJsonData,modelcontent);
		} catch (Exception e) {
			responceJson.put("errcode", "-999");
			logger.info(e.toString());
			e.printStackTrace();
		}
		return responceJson;
	}
	/**
	 * 
	 * @author yangjing 
	 * @param data 入参接口时的数据
	 * @param responce 调用接口的后的回码与描述
	 * @param responceJson 第三方接口的回码数据
	 * @return void
	 * @describe 
	 */
	public void setResultMsg(JSONObject data,JSONObject responce,JSONObject responceJson){
		
		String errCode=StringUtil.getString(responceJson.get("errcode"));
		if(errCode.equals("0")){
			responce.put("retCode", 1);
			data.put("retcode", 1);
		}else{
			responce.put("retCode", 0);
			data.put("retcode", 0);
		}
		String Msg=cache.getCacheObject("wechat"+errCode);
		responce.put("retMsg", Msg);
		data.put("retmsg",Msg);
		insertResultQueue(data);
	}
}