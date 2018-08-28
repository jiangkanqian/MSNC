package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.EmailLogMapper;
import com.cn.common.service.EmailLogService;

@Service
public class EmailLogServiceImpl implements EmailLogService{
	
	@Resource
	EmailLogMapper emailLogDao;
	@Override
	public List<Map> queryAllEmailLog(HashMap<String, String> map) {
		
		System.out.println(map.toString());
		return emailLogDao.queryAllEmailLog(map);
	}
	@Override
	public HashMap<String, String> queryDetailsLog(String sysId) {
		
		return emailLogDao.queryDetailsLog(sysId);
	}
	
}
