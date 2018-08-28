package com.cn.common.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.cn.common.dao.InterfaceWechatMapper;
import com.cn.common.service.InterfaceWechatService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.PoolingHttpClientUtil;
import com.cn.common.util.RedisCacheUtil;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月6日
 * @describe 微信的订阅号定时扫描所有的数据
 */
@Service
public class InterfaceWechatServiceImpl extends HomeServiceImpl implements InterfaceWechatService{

	private Logger logger=LoggerFactory.getLogger(InterfaceWechatServiceImpl.class);
	
	@Resource
	private InterfaceWechatMapper mapper;
	
	@Resource
    private RabbitTemplate rabbit;
	
	//交换器名称，必须和spring-amqp.xml里面的name="topicExchange"一致，否则会找不到交换器
	private  final String exchange_name="topicExchange";
	
	@Resource
	RedisCacheUtil<?> util;
	
	@Override
	public List<Map<String, String>> selectAllWechatQueueList() {
		// TODO Auto-generated method stub
		return mapper.selectByParameterObject(null);
	}

	@Override
	public String insertWechatLog(Map<String, String> map) {
		// TODO Auto-generated method stub
		try {
			mapper.insert(map);
			return "1";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "0";
		} 
				
	}

	//插入异步任务
	@Override
	public void insertInterfaceWechatApp(Map<String, String> map) {
		mapper.insertWechat(map);
	}
	
	@Override
	public void sendAppQueue(Map<String, Object> map) {
	
		//routingkey 要与配置文件中的pattern="wechatApp.*"进行配置，所以要注意
		String  routingkey="wechatApp.async";
		rabbit.convertAndSend(exchange_name, routingkey, map);
	}

	@Override
	public void sendCorpQueue(Map<String, Object> map) {
		//routingkey 要与配置文件中的pattern="wechatCorp.*"进行配置，所以要注意
		String  routingkey="wechatCorp.async";
		rabbit.convertAndSend(exchange_name, routingkey, map);
	}
	@Override
	public  synchronized JSONObject getAccessToken(String id, String secret,String getTokenUrl) throws IOException {// 同步获取token避免并发

		JSONObject resultJson = new JSONObject();
		String token = null;
		// 再次避免请求获取token的并发，并从缓存中获取
		if (isAccessTokenValid(id + secret)) {
			token = (String) util.getCacheMap(id + secret).get("token");
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
			util.setCacheMap(id + secret, map);
		}
		logger.info(resultJson.toString());
		return resultJson;
	}

	/* (non-Javadoc)
	 * @see com.cn.common.service.impl.WechatService#isAccessTokenValid(java.lang.String)
	 */
	@Override
	public  boolean isAccessTokenValid(String idAndsecret) {

		if (util.getCacheMap(idAndsecret).containsKey(idAndsecret)) {
			Long oldTime = (Long) util.getCacheMap(idAndsecret).get("time");
			// 计算当前时间与缓存access_token时间差
			Long time = System.currentTimeMillis() - oldTime;
			int expiresIn = (Integer) util.getCacheMap(idAndsecret).get("expiresIn");
			// 时间差是否在access_token有效时间段内
			if (time < expiresIn * 1000) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.cn.common.service.impl.WechatService#sendMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */

	@Override
	public  JSONObject sendMessage(String id, String secret, String getTokenUrl,String sendMsgUrl,String jsonData) throws IOException {

		JSONObject responce = null;
		String token = null;
		logger.info("appid " + id + "  secret " + secret + "  jsonMessage " + jsonData);
		// 直接从缓存中获取token
		if (isAccessTokenValid(id + secret)) {
			token = (String) util.getCacheMap(id + secret).get("token");
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
	/* (non-Javadoc)
	 * @see com.cn.common.service.impl.WechatService#sendMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public  JSONObject sendMessage(final String id, final String secret,final String getTokenUrl, final String sendMsgUrl,
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
	
	/* (non-Javadoc)
	 * @see com.cn.common.service.impl.WechatService#initWechatContent(java.lang.String, java.lang.String)
	 */
	@Override
	public  String initWechatContent(String content,String modecontent){
		 //将模版构建成一个文件
	    File file=null;
		String templateFileName="/"+StringUtil.getUuId()+".ftl";
	    if(!"".equals(modecontent))
	    //	"/wechat_blank_template.ftl"
		file=new File(Msnc.template_path.concat(templateFileName));
		//将模版内容构建为发送内容
		try {
			return initTemplate(content, modecontent, file,templateFileName);
		} catch (Exception e) {
			return "-998";
		}	
	}
}
