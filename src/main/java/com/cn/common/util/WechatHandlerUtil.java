package com.cn.common.util;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.common.sys.bean.Msnc;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年11月16日
 * @describe 用于微信的公众号消息群发。限制文本字数为1000
 */
public class WechatHandlerUtil extends HomeUtil{

	private static final Logger logger = LoggerFactory.getLogger(WechatHandlerUtil.class);
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
	public static synchronized JSONObject getAccessToken(String id, String secret,String getTokenUrl) throws IOException {// 同步获取token避免并发

		JSONObject resultJson = new JSONObject();
		String token = null;
		// 再次避免请求获取token的并发，并从缓存中获取
		if (isAccessTokenValid(id + secret)) {
			token = (String) AccessTokenMapUtil.getMap().get(id + secret).get("token");
			resultJson.put("access_token", token);
			return resultJson;
		}
		String url = MessageFormat.format(getTokenUrl, id, secret);
		resultJson = PoolingHttpClientUtil.get(url);
		// 是否获取到token
		if (resultJson.containsKey("access_token")) {
			// 微信的凭证有效时间减去的30秒服务器的访问时间
			int expiresIn = (Integer) resultJson.get("expires_in") - 30;
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("token", resultJson.get("access_token"));
			map.put("expiresIn", expiresIn);
			map.put("time", System.currentTimeMillis());
			// 缓存access_token及有效时间
			AccessTokenMapUtil.getMap().put(id + secret, map);
		}
		logger.info(resultJson.toString());
		return resultJson;
	}

	/**
	 * 
	 * @author yangjing
	 * @param idAndsecret
	 *            用户唯一凭证
	 * @return boolean
	 * @describe 判读token是否失效
	 */
	public static boolean isAccessTokenValid(String idAndsecret) {

		if (AccessTokenMapUtil.getMap().containsKey(idAndsecret)) {
			Long oldTime = (Long) AccessTokenMapUtil.getMap().get(idAndsecret).get("time");
			// 计算当前时间与缓存access_token时间差
			Long time = System.currentTimeMillis() - oldTime;
			int expiresIn = (Integer) AccessTokenMapUtil.getMap().get(idAndsecret).get("expiresIn");
			// 时间差是否在access_token有效时间段内
			if (time < expiresIn * 1000) {
				return true;
			}
		}
		return false;
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

	public static JSONObject sendMessage(String id, String secret, String getTokenUrl,String sendMsgUrl,String jsonData) throws IOException {

		JSONObject responce = null;
		String token = null;
		logger.info("appid " + id + "  secret " + secret + "  jsonMessage " + jsonData);
		// 直接从缓存中获取token
		if (isAccessTokenValid(id + secret)) {
			token = (String) AccessTokenMapUtil.getMap().get(id + secret).get("token");
			sendMsgUrl = MessageFormat.format(sendMsgUrl, token);
			responce = PoolingHttpClientUtil.post(sendMsgUrl, jsonData);
			logger.info("get accesstoken from---> AccessTokenMap  " + token);
			return responce;
		}
		JSONObject accessToken = getAccessToken(id, secret,getTokenUrl);
		// 是否获取到 access_token
		if (!accessToken.containsKey("access_token")) {
			return accessToken;
		}
		token = (String) accessToken.get("access_token");
		sendMsgUrl = MessageFormat.format(sendMsgUrl, token);
		logger.info("get  accesstoken from---> httpclient " + token);
		responce = PoolingHttpClientUtil.post(sendMsgUrl, jsonData);
		return responce;
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
	 * @throws IOException 
	 * @return JSONObject 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @describe 用id与secret调用微信接口获取公众号的access_token并发送content
	 */
	public static JSONObject sendMessage(final String id, final String secret,final String getTokenUrl, final String sendMsgUrl,
			final String content, final String sendJsonData, final String modelcontent) throws IOException, InterruptedException, ExecutionException {
		
		JSONObject responce = new JSONObject();
		//先初始化微信的模板内容
		 String text=initWechatContent(content, modelcontent);
		 //如果模版初始化就直接返回
		if(text.equals("-998")){
			responce.put("errcode", "-998");
			return responce;
		}
		if (text.trim().length() > 1000) {
			responce.put("errcode", "45002");
			return responce;
		}
		final String data=String.format(sendJsonData, text);
		ExecutorService executor=Executors.newFixedThreadPool(1000);
		//构建CompletionService，使其获取返回值不再等待，以防线程阻塞
		CompletionService<JSONObject> completionService=new ExecutorCompletionService<JSONObject>(executor);
		completionService.submit(new Callable<JSONObject>() {
			@Override
			public JSONObject call() throws Exception {
				return sendMessage(id,secret,getTokenUrl,sendMsgUrl,data);
			}
		   }
		);
	   executor.shutdown();
       return completionService.take().get();
	}
	
	public static String initWechatContent(String content,String modecontent){
		 //将模版构建成一个文件
	    File file=null;
	    if(!"".equals(modecontent))
		file=new File(Msnc.template_path.concat("/wechat_blank_template.ftl"));
		//将模版内容构建为发送内容
		try {
			return initTemplate(content, modecontent, file,"wechat_blank_template.ftl");
		} catch (Exception e) {
			return "-998";
		}	
	}
}