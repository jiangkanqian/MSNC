package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2017年12月30日
 * @describe 应用接口授权管理
 */
public interface AppInterfaceMapper {
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe  查询所有的应用以及该应用拥有的接口数
	 */
	public List<Map> queryAllAppAndInterfaceAmount();

	/**
	 * 
	 * @author yangjing 
	 * @param appId  应用id
	 * @return List<HashMap<String,String>>
	 * @describe  查询该应用拥有的接口
	 */
	public List<HashMap<String,String>> queryInterfaceInApp(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId  应用id
	 * @return List<HashMap<String,String>>
	 * @describe   查询该应用没有的接口
	 */
	public List<HashMap<String,String>> queryInterfaceNotInApp(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param list  参数：appId，interfaceId,其中这个appId是同一个。格式如下：
				[{"appId":"1"," interfaceId":"2"},
				{"appId":"1"," interfaceId":"3"},
				{"appId":"1"," interfaceId":"8"}]
	 * @return void
	 * @describe  增加应用接口
	 */
	public void addAllAppInterface(List<HashMap<String,String>> list);
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId   应用id
	 * @return void 
	 * @describe  删除应用接口
	 */
	public void deleteAllAppInterface(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param interfaceId  接口id
	 * @return String  （true为有，false为无）
	 * @describe  判断该接口是否有模板使用
	 */
	public String isExistAppAtInterface(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map  参数：appId，interfaceId
	 *  @return void
	 * @describe 增加单个的应用接口权限
	 */
	public void addAppInterface(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map   参数：appId，interfaceId
	 * @return void
	 * @describe 删除单个的应用接口权限
	 */
	public void deleteAppInterface(HashMap<String, String> map);
}
