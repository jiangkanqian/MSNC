package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cn.common.dao.AppInterfaceMapper;
import com.cn.common.service.AppInterfaceService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年12月30日
 * @describe 应用接口授权管理
 */
@Service
public class AppInterfaceServiceImpl implements AppInterfaceService {

	@Resource
	AppInterfaceMapper appInterfaceMapper;

	@Override
	public List<Map> queryAllAppAndInterfaceAmount(HashMap<String,String> map) {

 
		return appInterfaceMapper.queryAllAppAndInterfaceAmount();
	}

	@Override
	public List<HashMap<String,String>> queryInterfaceInApp(HashMap<String,String> map) {


		return appInterfaceMapper.queryInterfaceInApp( map);
	}

	@Override
	public List<HashMap<String, String>> queryInterfaceNotInApp(HashMap<String,String> map) {
		
		return appInterfaceMapper.queryInterfaceNotInApp(map);
	}

	@Transactional
	@Override
	public void addAllAppInterface(List<HashMap<String, String>> list,String appId) {
	
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("appId", appId);
		if(null==list||list.isEmpty()){
			appInterfaceMapper.deleteAllAppInterface(map);			
			return ;
		}
		appInterfaceMapper.deleteAllAppInterface(map);
		appInterfaceMapper.addAllAppInterface(list);		
	}

	@Override
	public JSONObject deleteAllAppInterface(HashMap<String,String> map) {
		
		
		JSONObject responce = null;
		try {
			appInterfaceMapper.deleteAllAppInterface(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除该应用的接口权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject isExistAppAtInterface(HashMap<String,String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=appInterfaceMapper.isExistAppAtInterface(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该接口已有应用在使用");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该接口没有没有应用在使用");
		}
		return responce;
	}

	@Override
	public JSONObject addAppInterface(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			appInterfaceMapper.addAppInterface(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加该应用的接口权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
		
	}

	@Override
	public JSONObject deleteAppInterface(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			appInterfaceMapper.deleteAppInterface(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除该应用的接口权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();

	}

}
