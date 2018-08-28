package com.cn.common.service;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 手机定时器任务扫描接口
 * @author chenkai
 * date:2016-12-5
 */
public interface InterfaceMobileService {
  
	/**
	 * 查询所有的短信任务
	 * @return
	 */
	public List<Map<String,String>> selectAllMobileQueueList();
	
	/**
	 * 插入短信任务记录
	 * @param map
	 * @return
	 */
	public String insertMobileLog(Map<String,String> map);
	
	/**
	 * 插入短信异步任务
	 * @param map
	 */
	public void insertInterfaceMobile(Map<String, String> map);
	/**
	 * 插入短信异步队列
	 * @param map
	 * @return
	 */
	public void sendMobileQueue(JSONObject json);
	
	/**
	 * 群发相同内容消息
	 */
	//@Resend(times=3)
	String sendEqMessage(String userId,String pass,String phone,String content,String modecontent,int count,JSONObject json)throws Exception;
    
	/**
	 * 群发不同消息
	 */
	String sendNotEqMessage(String userid,String pass,String modelcontent,JSONArray contents,JSONObject json)throws Exception;
}
