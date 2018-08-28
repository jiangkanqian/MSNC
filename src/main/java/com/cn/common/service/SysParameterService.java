package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 系统公共参数接口
 * @author chenkai
 *
 */

public interface SysParameterService {

	/**
	 * 根据条件获取相应的数据
	 * @param Map
	 * @return
	 */
	public List<Map> findBySysTypeAll(String sysType);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 参数：
	 * 		sysName 参数名字,
			sysType 参数类型,
			sysValue 参数值,
			sysMsg 参数描述
	 * @return void
	 * @describe 增加系统参数
	 */
	public JSONObject addSysParameter(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid  参数主键
	 * @return JSONObject
	 * @describe 删除系统参数
	 */
	public JSONObject deleteSysParameter(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 	  参数：
	  		sysName 参数名字,
			sysType 参数类型,
			sysValue 参数值,
			sysMsg 参数描述，
	        sysid 参数主键
	 * @return JSONObject
	 * @describe 修改系统参数
	 */
	public JSONObject updateSysParameter(HashMap<String,String>  map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  
	 *  参数：
	 *  sysType 参数类型
	 * @return JSONObject
	 * @describe 查询所有的系统参数
	 */
	public List<Map> queryAllSysParameter (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数：
	 * sysName 参数名字，
	 * sysType 参数类型
	 * @return JSONObject
	 * @describe 判断系统参数名字是否重复
	 */
	public JSONObject isRepeatSysName (HashMap<String,String> map);

		
}
