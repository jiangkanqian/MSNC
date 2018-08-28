package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.WechatLogMapper;
import com.cn.common.service.WechatLogService;

/**
 * 
 * @author yangjing
 * @date 2016年12月15日
 * @describe 微信的消息记录查询
 */
@Service
public class WechatLogServiceImpl implements WechatLogService{
	
	@Resource
	WechatLogMapper wechatLogDao;
	@Override
	public List<Map> queryAllWechatLog(HashMap<String, String> map) {
		
		System.out.println(map.toString());
		return wechatLogDao.queryAllWechatLog(map);
	}
	@Override
	public HashMap<String, String> queryDetailsLog(String sysId) {
		
		return wechatLogDao.queryDetailsLog(sysId);
	}
	
}
