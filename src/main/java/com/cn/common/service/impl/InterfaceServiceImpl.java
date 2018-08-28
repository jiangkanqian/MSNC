package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cn.common.dao.InterfaceMapper;
import com.cn.common.service.InterfaceService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.RedisCacheUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年1月3日
 * @describe 接口管理
 */
@Service
public class InterfaceServiceImpl implements InterfaceService{

	@Resource
	InterfaceMapper interfaceMapper;

	
	@Resource 
	RedisCacheUtil<?> util;
	
	@Override
	public  List<Map> queryAllInterface(HashMap<String,String> map) {
		
		return interfaceMapper.queryAllInterface(map);
	}
	
	@Transactional
	@Override
	public JSONObject deleteInterface(HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			interfaceMapper.deleteInterface(map);
			initInterfaceSonType();
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加接口失败！");
			return responce;
		}
		return responce;
	}

	@Override
	public JSONObject updateInterface(HashMap<String,String> map){
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			 interfaceMapper.updateInterface(map);
			 initInterfaceSonType();
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改接口失败！");
			return responce;
		}
		return responce;

	}

	@Override
	public JSONObject addInterface(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			interfaceMapper.addInterface(map);
			initInterfaceSonType();
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加接口失败！");
			return responce;
		}
		return responce;
	}

	@Override
	public String queryInterfaceSonType(HashMap<String,String> map) {
		
		return interfaceMapper.queryInterfaceSonType(map);
	}

	@Override
	public List<Map<String, String>> getUrlAndInterfaceSonType() {

		return interfaceMapper.getUrlAndInterfaceSonType();
	}
	
	void initInterfaceSonType(){
	
		List<Map<String, String>> list=interfaceMapper.getUrlAndInterfaceSonType();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map=list.get(i);
			util.setCacheObject(map.get("url"), map.get("type"));
		}
		list=null;
	}

	@Override
	public List<Map<String, String>> queryAllInterfaceName() {
		
		return interfaceMapper.queryAllInterfaceName();
	}
}
