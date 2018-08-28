package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月26日
 * @describe 应用管理
 */
public interface AppMapper {
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数: appName（应用名称，json）
	 * @return List<HashMap<String,String>>
	 * @describe   查询所有的应用
	 */
	public List<Map> queryAllApp(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  参数: appName应用名字， state 状态，remark 描述，sysid 主键id
	 * @return void
	 * @describe  修改应用
	 */
	public void updateApp(HashMap<String,String> map);
	

	/**
	 * 
	 * @author yangjing 
	 * @param map 参数: appName应用名称, state应用状态, remark应用描述，appid应用id， appName应用名字
	 * @return void
	 * @describe  添加应用
	 */
	public void addApp (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid  应用id
	 * @return void
	 * @describe 删除应用
	 */
	public void deleteApp  (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param appName 应用名称
	 * @return String   （true为重复，false为没重复）
	 * @describe 判断应用名字是否重复
	 */
	public String isRepeatAppName (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param appid 主键id
	 * @return String   （true为重复，false为没重复）
	 * @describe  判断应id是否重复
	 */
	public String isRepeatAppId (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe 所有的应用以及对应的应用拥有的模板数和接口数
	 */
	public List<Map> queryAppWithMAmountAndIAmount(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @return List<Map>
	 * @describe 查询引用名称，应用id
	 */
	public List<Map> queryApp();
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid
	 * @return List<HashMap<String,String>>
	 * @describe 判断该应用是否可以删除
	 */
	public List<HashMap<String,String>> isCanDeleteApp(HashMap<String,String> map);
}
