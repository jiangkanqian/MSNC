package com.cn.common.sys.bean;

public class SystemProperty {
	
	public static final String Algorithm = "DESede"; // 定义 加密算法,可用 DES,DESede,Blowfish
	
	public static final String deskey="12345678abba87654321";//加密秘钥
	//本地节点模版地址
	public static String template_path="";
	
	//邮件发送人
	//public static String email_from="cjItsvc@cjhxfund.com";
	public static String email_from="cjItsvc@cjhxfund.com";
	//移动梦网短信群发接口URL
	public static final String mw_url="http://114.67.48.66:5122/MWGate/wmgw.asmx";
	
	//移动梦网短信群发相同内容接口方法 
	public static final String mw_equal_content_method="MongateCsSpSendSmsNew";
	
	//移动梦网短信群发不同内容接口方法MongateCsSpMultixMtSend
	public static final String mw_not_equal_content_method="MongateCsSpMultixMtSend";
}
