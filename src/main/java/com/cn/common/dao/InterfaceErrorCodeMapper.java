package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository(value = "interfaceErrorCodeMapper")
public interface InterfaceErrorCodeMapper {

	/**
	 * 
	 * @author yangjing
	 * @param map
	 * 
	 *通过接口子类型(interfaceSonType) 和 调用接口后的回码(errcode)来查找错误描述(errmsg)
	 * 
	 * @return Map<String,Object>
	 * @describe
	 */
	public Map<String, Object> findErrMsgByInterfaceSonTypeAndErrcode(Map<String, String> map);

	public List<Map<String,String>> selectAllErrorCode();
	
	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType接口类型，errCode 错误码，errMsg 错误描述
	 * @return void
	 * @describe 增加错误回码
	 */
	public void addErrorCode(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid 记录id
	 * @return void
	 * @describe 删除错误回码
	 */
	public void deleteErrorCode(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  sysid 记录id，interfaceSonType接口类型，errCode 错误码，errMsg 错误描述
	 * @return void
	 * @describe 修改错误回码
	 */
	public void updateErrorCode(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType接口类型，errCode 错误码
	 * @return List<Map>
	 * @describe 查询错误回码
	 */
	public  List<Map> queryAllErrorCode (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * @return String 
	 * @describe 判断是否重复的设置了错误回码
	 */
	public String  isRepeatSetErrorCode(HashMap<String,String> map);
}
