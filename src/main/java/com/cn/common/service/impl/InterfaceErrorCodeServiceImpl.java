package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.InterfaceErrorCodeMapper;
import com.cn.common.service.InterfaceErrorCodeService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.RedisCacheUtil;

import net.sf.json.JSONObject;

@Service
public class InterfaceErrorCodeServiceImpl  implements InterfaceErrorCodeService{

	@Resource
	InterfaceErrorCodeMapper interfaceErrorCodeMapper;

	@Resource 
	RedisCacheUtil<?> util;
	
	@Override
	public Map<String, Object> findErrMsgByInterfaceSonTypeAndErrcode(Map<String, String> map) {
		return interfaceErrorCodeMapper.findErrMsgByInterfaceSonTypeAndErrcode(map);
	}

	@Override
	public List<Map<String, String>> selectAllErrorCode() {
		return interfaceErrorCodeMapper.selectAllErrorCode();
	}

	@Override
	public JSONObject addErrorCode(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			interfaceErrorCodeMapper.addErrorCode(map);
			refreshErrorCode();
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加错误描述失败！");
			return responce;
		}
		return responce;
	}

	@Override
	public JSONObject deleteErrorCode(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			interfaceErrorCodeMapper.deleteErrorCode(map);
			refreshErrorCode();
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除错误描述失败！");
			return responce;
		}
		return responce;
	}

	@Override
	public JSONObject updateErrorCode(HashMap<String, String> map) {
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			interfaceErrorCodeMapper.updateErrorCode(map);
			refreshErrorCode();
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改错误描述失败！");
			return responce;
		}
		return responce;
	}

	@Override
	public List<Map> queryAllErrorCode(HashMap<String, String> map) {
		return interfaceErrorCodeMapper.queryAllErrorCode(map);
	}
	
	/**
	 * 
	 * @author yangjing  
	 * @return void
	 * @describe 刷新缓存中的错误回码
	 */
	public void refreshErrorCode(){
		List<Map<String, String>> list = interfaceErrorCodeMapper.selectAllErrorCode();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map=list.get(i);
			util.setCacheObject(map.get("code"), map.get("msg"));
		}
		list=null;
	}

	@Override
	public JSONObject isRepeatSetErrorCode(HashMap<String, String> map) {

		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed = interfaceErrorCodeMapper.isRepeatSetErrorCode(map);
		if (Msnc.TRUE.equals(isUsed)) {
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该错误码已设置");
		} else if (Msnc.FALSE.equals(isUsed)) {
			responce.put("retMsg", "该错误码没有设置");
		}
		return responce;
	}

}
