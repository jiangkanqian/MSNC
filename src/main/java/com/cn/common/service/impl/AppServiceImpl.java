package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.AppMapper;
import com.cn.common.service.AppService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.StringUtil;
import net.sf.json.JSONObject;


/**
 * 
 * @author yangjing
 * @date 2016年12月26日
 * @describe 应用管理
 */
@Service
public class AppServiceImpl implements AppService{

	@Resource
	AppMapper appMapper;

	@Override
	public List<Map> queryAllApp(HashMap<String, String> map) {
		return appMapper.queryAllApp(map);
	}

	@Override
	public JSONObject updateApp(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			appMapper.updateApp(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改应用失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject addApp(HashMap<String, String> map) {
		
		JSONObject responce=JsonObjectUtil.initSucceed();
		try {
			responce.put("appid", StringUtil.getUuId());
			responce.put("secret", StringUtil.getUuId());
			map.put("secret", responce.getString("secret"));
			appMapper.addApp(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加应用失败！");
			return responce;
		}
		return responce;
	}

	@Override
	public JSONObject deleteApp(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			appMapper.deleteApp(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除应用失败！");
			return responce;
		}
		return responce;
	}

	@Override
	public JSONObject isRepeatAppName(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=appMapper.isRepeatAppName(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该应用名字已重复");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该应用名字没有重复");
		}
		return responce;
	}

	@Override
	public List<Map> queryAppWithMAmountAndIAmount(HashMap<String,String> map) {
		
		return appMapper.queryAppWithMAmountAndIAmount( map);
	}

	@Override
	public List<Map> queryApp() {
		
		return appMapper.queryApp();
	}

	@Override
	public JSONObject isCanDeleteApp(HashMap<String, String> map) {
		JSONObject responce = null;
		List <HashMap<String,String>>list =appMapper.isCanDeleteApp(map);
		String msg="";
		if(list==null||list.isEmpty()){
			return JsonObjectUtil.initSucceed();
		}else {
			for (int i = 0; i < list.size(); i++) {
				Object count=list.get(i).get("count");
				msg+=list.get(i).get("msg")+count.toString()+"个，";
			}
		}
		responce = JsonObjectUtil.initError();
		responce.put("retMsg", msg.substring(0,msg.length()-1));
		return responce;
	}

	@Override
	public JSONObject isRepeatAppId(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=appMapper.isRepeatAppId(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该应用id已重复");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该应用id没有重复");
		}
		return responce;
	}

}
