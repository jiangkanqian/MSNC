package com.cn.common.service.impl;


import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cn.common.dao.DictionaryMapper;
import com.cn.common.service.DictionaryService;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年12月30日
 * @describe 应用接口授权管理
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

	@Resource
	DictionaryMapper dictionaryMapper;

	@Override
	public JSONObject queryAllInterfaceSonType(HashMap<String, String> map) {
		
		JSONObject responce =JsonObjectUtil.initSucceed();
		responce.put("data", dictionaryMapper.queryAllInterfaceSonType(map));
		return responce;
	}

}
