package com.cn.common.sys.bean;

import java.util.HashMap;
import java.util.Map;
public class Msnc {
	
	public static final String TRUE="true";
	
	public static final String FALSE="false";
	
	public static final String Algorithm = "DESede"; // 定义 加密算法,可用 DES,DESede,Blowfish
	
	public static final String deskey="12345678abba87654321";//加密秘钥
	public static final int SessionOutTime=10;
	//本地节点模版地址
	public static String template_path="";

	//邮件发送人
	//public static String email_from="cjItsvc@cjhxfund.com";
	public static String email_from="shenjieliboy@163.com";
	
	//移动梦网短信群发相同内容接口方法 
	public static final String mw_equal_content_method="MongateCsSpSendSmsNew";
	
	//移动梦网短信群发不同内容接口方法MongateCsSpMultixMtSend
	public static final String mw_not_equal_content_method="MongateCsSpMultixMtSend";
   
	//第三方接口返回的错误码
	public static Map<String,String> err_code=new HashMap<String,String>();

	//第三方接口返回的错误码
	//public static Map<String,RateLimiter> flow_rate_limiter=new ConcurrentHashMap<String,RateLimiter>();
	
	//获取接口子类型
	//public static Map<String,String> interface_son_type=new HashMap<String,String>();
	//移动梦网账号//"JKCS01"
//	public static final String mobile_user="SEND01";
//
//	//移动梦网密码//"ztVuceqfcNA="
//	public static final String mobile_pass="ztVuceqfcNA=";
//	
//	//移动梦网短信群发接口URL//"http://114.67.48.66:5122/MWGate/wmgw.asmx"
//	public static final String mw_url="http://10.201.9.150:8082/MWGate/wmgw.asmx";
	public static final String mobile_user="JKCS01";
	public static final String mobile_pass="ztVuceqfcNA=";
	public static final String mw_url="http://114.67.48.66:5122/MWGate/wmgw.asmx";

	//微信订阅号获取token
	public static final String WECHAT_APPTOKENURL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
	
	//微信订阅号发送消息url
	public static final String WECHAT_APPSENDURL="https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token={0}";
	
	//微信订阅号数据包
	public static final String WECHAT_APPTEXTDATA="{\"filter\":{\"is_to_all\":true},\"text\":{\"content\":\"%s\"},\"msgtype\":\"text\"}";
		
	//微信企业号获取token
	public static final String WECHAT_CORPTOKENURL="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1}";
	
	//微信企业号发送消息url
	public static final String WECHAT_CORPSENDURL="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}";
	
	//微信企业号数据包
	public static final String WECHAT_CORPTEXTDATA="{\"touser\": \"%s\",\"toparty\": \"%s \",\"totag\": \"%s \",\"msgtype\": \"text\",\"agentid\": %s,\"text\": {\"content\": \"%s\"},\"safe\":0}";
}
