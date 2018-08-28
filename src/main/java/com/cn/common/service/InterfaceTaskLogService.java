package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public interface InterfaceTaskLogService {

	public void insertInterfaceTaskLog(Map<String,String> map);
	
	
	public List<Map> queryTaskLog(Map<String, String> map);

	
	public void insertInterfaceTaskLogQueue(JSONObject json);

	
	public List<Map> queryLog(Map<String, String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * @return 
	 * @return Integer
	 * @describe 流量监控
	 */
	public Integer flowMonitoring(Map<String, String> map);
	
	void updateFromRepeatSendTask(String messageId);
}
