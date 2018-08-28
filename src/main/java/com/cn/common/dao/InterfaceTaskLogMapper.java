package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InterfaceTaskLogMapper extends BaseMapper{

	public List<Map> queryTaskLog(Map<String, String>  map);

	public List<Map> queryLog(Map<String, String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * @return 
	 * @return String
	 * @describe 流量监控
	 */
	public Integer flowMonitoring(Map<String, String> map);
	
	public void updateFromRepeatSendTask(String messageId);
}
