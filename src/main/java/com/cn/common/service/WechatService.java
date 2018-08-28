package com.cn.common.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.sf.json.JSONObject;

public interface WechatService {

	//	private String getAppTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
	//	private String sendAppMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token={0}";
	//https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=id&corpsecret=secrect
	/**
	 * 
	 * @author yangjing 
	 * @param id  企业号(公众号)
	 * @param secret 公众号的密钥
	 * @param getTokenUrl
	 * 	(获取企业号token的url)：
	 *  https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1}；
	 *  (微信公众号的获取token的url)：
	 *  https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}
	 * @throws IOException 
	 * @return JSONObject
	 * @describe 利用id与secret调用微信接口获取公众号的access_token（凭证）
	 */
	JSONObject getAccessToken(String id, String secret, String getTokenUrl) throws IOException;

	/**
	 * 
	 * @author yangjing
	 * @param idAndsecret
	 *            用户唯一凭证
	 * @return boolean
	 * @describe 判读token是否失效
	 */
	boolean isAccessTokenValid(String idAndsecret);

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
	 * @param jsonData 发送消息的json数据包
	 *	(企业号发送文本消息的数据包)：
	 *	{"touser": "%s","toparty": "%s ","totag": "%s ","msgtype": "text","agentid": %s,"text": {"content": "%s"},"safe":0};
	 *	(公众号发送消息的json包)：
	 *	{"filter":{"is_to_all":true},"text":{"content":"%s"},"msgtype":"text"}
	 * @param modelcode 模板编号
	 * @throws IOException 
	 * @return JSONObject
	 * @describe 利用id与secret调用微信接口获取公众号的access_token并发送content
	 */

	JSONObject sendMessage(String id, String secret, String getTokenUrl, String sendMsgUrl, String jsonData)
			throws IOException;

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
	 * @throws IOException 
	 * @return JSONObject 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @describe 用id与secret调用微信接口获取公众号的access_token并发送content
	 */
	JSONObject sendMessage(String id, String secret, String getTokenUrl, String sendMsgUrl, String content,
			String sendJsonData, String modelcontent) throws IOException, InterruptedException, ExecutionException;

	String initWechatContent(String content, String modecontent);

}