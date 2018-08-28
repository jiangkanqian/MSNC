package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author yangjing
 * @date 2017年3月8日
 * @describe 业务字典查询
 */
public interface DictionaryMapper {
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe 查询接口子类型
	 */
	public List<HashMap<String,String>> queryAllInterfaceSonType(HashMap<String,String> map);
}
