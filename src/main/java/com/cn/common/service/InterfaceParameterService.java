package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年1月5日
 * @describe 接口参数管理
 */
public interface InterfaceParameterService {
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<Map> 
	 * @describe 查询所有的接口参数
	 */
	public List<Map> queryAllInterfaceParameter(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 参数：
	 * 	parameterName 参数名字,
		dataType 参数数据类型,
		parameterType 参数类型（0.输入1.输出2.输入/输出）,
		isMust 是否必须（0.否，1.是）,
		parameterMsg 参数描述,
		interfaceId 接口id
	 * @return JSONObject
	 * @describe 增加接口参数
	 */
	public JSONObject addInterfaceParameter(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 参数：
	 * 	parameterName 参数名字,
		dataType 参数数据类型,
		parameterType 参数类型（0.输入1.输出2.输入/输出）,
		isMust 是否必须（0.否，1.是）,
		parameterMsg 参数描述,
		interfaceId 接口id 
	 * parameterId 接口参数id
	 * @return JSONObject
	 * @describe 修改接口参数
	 */
	public JSONObject updateInterfaceParameter(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param parameterId  接口参数id
	 * @return JSONObject
	 * @describe 删除接口参数
	 */
	public JSONObject deleteInterfaceParameter(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map 参数：interfaceId 接口id, parameterName 参数名字
	 * @return JSONObject
	 * @describe 判读该接口参数名字是否重复
	 */
	public JSONObject isRepeatParameterName(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param interfaceId 接口id
	 * @return List<HashMap<String, String>>
	 * @describe 查询该接口的参数
	 */
	public List<HashMap<String, String>> queryInterfaceParametersBySysId(HashMap<String,String> map);
}
