package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.MobileLogMapper;
import com.cn.common.service.MobileLogService;

/**
 * 
 * @author yangjing
 * @date 2016年12月13日
 * @describe 短信的消息记录查询
 */
@Service
public class MobileLogServiceImpl implements MobileLogService{
	
	@Resource
	MobileLogMapper mobileLogDao;
	
	@Override
	public List<Map> queryAllMobileLog(HashMap<String,String> map) {
		
		System.out.println(map.toString());
		return mobileLogDao.queryAllMobileLog(map);
	}
	@Override
	public HashMap<String, String> queryDetailsLog(HashMap<String,String> map) {
		
		return mobileLogDao.queryDetailsLog(map);
	}
	
}
