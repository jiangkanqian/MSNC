package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.ModelMapper;
import com.cn.common.service.ModelService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;


/**
 * 
 * @author yangjing
 * @date 2016年12月27日
 * @describe 模板管理
 */
@Service
public class ModelServiceImpl implements ModelService{

	@Resource
	ModelMapper modelMapper;

	@Override
	public List<Map> queryAllModel(HashMap<String, String> map) {
		
		return modelMapper.queryAllModel(map);
	}

	@Override
	public JSONObject updateModel(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			modelMapper.updateModel(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改模板失败");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}
	@Override
	public JSONObject addModel(HashMap<String, String> map) {
		JSONObject responce = null;
		try {
			modelMapper.addModel(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加模板失败!");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject deleteModel(HashMap<String,String> map) {
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			modelMapper.deleteModel(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除模板失败!");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject isRepeatModelId(HashMap<String,String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=modelMapper.isRepeatModelId(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该模板编号已经重复");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该模板编号没有重复");
		}
		return responce;
	}

	@Override
	public JSONObject isRepeatModelName(HashMap<String,String> map) {
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=modelMapper.isRepeatModelName(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该模板名字已经重复");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该模板名字已经重复");
		}
		return responce;
	}


	/**
	 * date:2017-01-04
	 * @author chen.kai
	 * @param String modelcode 模版id
	 * @return String content 
	 * 根据模版id获取模版内容
	 * 
	 */
	@Override
	public String findModelContent(String modelcode) {
		
		return modelMapper.findModelContent(modelcode);
	}
	
}
