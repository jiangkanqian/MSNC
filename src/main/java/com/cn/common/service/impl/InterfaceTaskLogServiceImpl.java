package com.cn.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.cn.common.dao.InterfaceTaskLogMapper;
import com.cn.common.service.InterfaceTaskLogService;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 接口任务和接口执行记录表操作
 * @author chen.kai
 * date:2017-02-13
 */
@Service
public class InterfaceTaskLogServiceImpl implements InterfaceTaskLogService{

	@Resource
	private InterfaceTaskLogMapper mapper;
	
	@Resource
	private RabbitTemplate rabbit;
	
	//交换器名称，必须和spring-amqp.xml里面的name="mob.topicExchange"一致，否则会找不到交换器
	private final String exchange_name="topicExchange";
	
	@Override
	public void insertInterfaceTaskLog(Map<String, String> map) {
		
		try {
			mapper.insert(map);			
		} catch (Exception e) {
			map.put("bodystr", "参数内容过长或有特殊字符，导致插入数据失败！");
			mapper.insert(map);	
		}
	}
	@Override
	public List<Map> queryTaskLog(Map<String, String> map) {
		JSONObject responce = JsonObjectUtil.initSucceed();
		return mapper.queryTaskLog(map);
	}

	@Override
	public void insertInterfaceTaskLogQueue(final JSONObject json) {
		String routingkey="task.log";
		rabbit.convertAndSend(exchange_name, routingkey, json);
	}

	@Override
	public List<Map> queryLog(Map<String, String> map) {
		
		return mapper.queryLog(map);
	}
	@Override
	public Integer flowMonitoring(Map<String, String> map) {
		
		return mapper.flowMonitoring(map);
	}
	@Override
	public void updateFromRepeatSendTask(String messageId) {
		mapper.updateFromRepeatSendTask(messageId);
	}


}
