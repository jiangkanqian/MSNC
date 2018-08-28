package com.cn.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.InterfaceTimedTaskMapper;
import com.cn.common.service.InterfaceTimedTaskService;

@Service
public class InterfaceTimedTaskServiceImpl implements InterfaceTimedTaskService {

	
	@Resource
	private InterfaceTimedTaskMapper taskMapper;

	@Override
	public List<Map<String, String>> findTimedTask() {
		// TODO Auto-generated method stub
		return taskMapper.findTimedTask();
	}
}
