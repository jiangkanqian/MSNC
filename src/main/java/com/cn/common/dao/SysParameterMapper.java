package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
@Repository(value = "sysParameterDao") 
public interface SysParameterMapper extends BaseMapper<Map,String> {
	
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
	public void addSysParameter(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid  参数主键
	 * @return void
	 * @describe 删除系统参数
	 */
	public void deleteSysParameter(HashMap<String,String> map);
	
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
	 * @return void
	 * @describe 修改系统参数
	 */
	public void updateSysParameter(HashMap<String,String>  map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  
	 *  参数：
	 *  sysType 参数类型
	 * @return List<HashMap<String,String>>
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
	 * @return String (true重复，false没有重复)
	 * @describe 判断系统参数名字是否重复
	 */
	public String isRepeatSysName (HashMap<String,String> map);
	
	
}
