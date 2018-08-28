package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cn.common.dao.AppModelMapper;
import com.cn.common.service.AppModelService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月28日
 * @describe 应用模板权限管理
 */
@Service
public class AppModelServiceImpl implements AppModelService{

	@Resource
	AppModelMapper appModelMapper;

	@Override
	public List<Map> queryAllAppAndModelAmount() {
		
		return appModelMapper.queryAllAppAndModelAmount();
	}

	@Override
	public List<HashMap<String, String>> queryModelNotInApp(HashMap<String, String> map) {
		
		return appModelMapper.queryModelNotInApp(map);
	}

	@Override
	public List<HashMap<String, String>> queryModelInApp(HashMap<String, String> map) {
	
		return appModelMapper.queryModelInApp(map);
	}

	@Transactional
	@Override
	public void addAllAppModel(List<HashMap<String, String>> list,String appId) {
		
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("appId", appId);
		if(null==list||list.isEmpty()){
			appModelMapper.deleteAllAppModel(map);
			return ;
		}
		appModelMapper.deleteAllAppModel(map);
		appModelMapper.addAllAppModel(list);

	}

	@Override
	public JSONObject deleteAllAppModel(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			appModelMapper.deleteAllAppModel(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除该应用的模板权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject isExistAppAtModel(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=appModelMapper.isExistAppAtModel(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该模板已有应用在使用");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该模板没有没有应用在使用");
		}
		return responce;
	}

	@Override
	public JSONObject addAppModel(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			appModelMapper.addAppModel(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加该应用的模板权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject deleteAppModel(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			appModelMapper.deleteAppModel(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除该应用的模板权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

}
