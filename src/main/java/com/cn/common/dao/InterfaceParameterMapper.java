package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2017年1月5日
 * @describe 接口参数管理
 */
public interface InterfaceParameterMapper {

	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe 查询所有的接口参数
	 */
	public   List<Map> queryAllInterfaceParameter(HashMap<String, String> map);

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
	 * @return void
	 * @describe 增加接口参数
	 */
	public void addInterfaceParameter(HashMap<String, String> map);

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
	 * @return void
	 * @describe 修改接口参数
	 */
	public void updateInterfaceParameter(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param parameterId  接口参数id
	 * @return void
	 * @describe 删除接口参数
	 */
	public void deleteInterfaceParameter(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map 参数：interfaceId 接口id, parameterName 参数名字
	 * @return String (true重复，false没有)
	 * @describe 判读该接口参数名字是否重复
	 */
	public String isRepeatParameterName(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param interfaceId 接口id
	 * @return List<HashMap<String,String>>
	 * @describe 查询该接口的参数
	 */
	public List<HashMap<String, String>> queryInterfaceParametersBySysId(HashMap<String,String> map);
}
