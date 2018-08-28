package com.cn.common.service;

import java.util.HashMap;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年3月8日
 * @describe 业务字典查询
 */
public interface DictionaryService {
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe 查询接口子类型
	 */
	public JSONObject queryAllInterfaceSonType(HashMap<String,String> map);
}
