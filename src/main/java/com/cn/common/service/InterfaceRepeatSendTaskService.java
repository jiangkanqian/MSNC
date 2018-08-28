package com.cn.common.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 消息重发任务接口
 * @author chen.kai
 * date:2017-02-27
 */
public interface InterfaceRepeatSendTaskService {

	void insert(Map<String,String> map);
	
	List<Map<String,String>> getNotSucceedTask();
	
	void updateNotSuccedTask(Map<?,?> map);
	
	void insertRepeatSendQueues(Map<?,?> map);
	
	void asyncSendMail(JSONObject json) throws Exception;
	
	void sendAsyncSingleTableHtmlMail(JSONObject json) throws Exception;
	
	void sendAsyncNotEqualContent(JSONObject json)throws Exception;
	
	void sendAsyncEqualContent(JSONObject json)throws Exception;
}
