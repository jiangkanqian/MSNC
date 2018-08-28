package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


public interface InterfaceErrorCodeService{

	/**
	 * 
	 * @author yangjing
	 * @param map
	 * 
	 * 通过接口子类型(interfaceSonType) 和 调用接口后的回码(errcode)来查找错误描述(errmsg)
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
	 * @return JSONObject
	 * @describe 增加错误回码
	 */
	public JSONObject addErrorCode(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid 记录id
	 * @return JSONObject
	 * @describe 删除错误回码
	 */
	public JSONObject deleteErrorCode(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  sysid 记录id，interfaceSonType接口类型，errCode 错误码，errMsg 错误描述
	 * @return JSONObject
	 * @describe 修改错误回码
	 */
	public JSONObject updateErrorCode(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType接口类型，errCode 错误码
	 * @return List<Map>
	 * @describe 查询错误回码
	 */
	public   List<Map> queryAllErrorCode (HashMap<String,String> map);
	
	
	/**
	 * 
	 * @author yangjing 
	 * @param interfaceSonType接口子类，errCode错误回码
	 * @return JSONObject
	 * @describe 判断是否重复的设置了错误回码
	 */
	public JSONObject isRepeatSetErrorCode(HashMap<String, String> map);
}