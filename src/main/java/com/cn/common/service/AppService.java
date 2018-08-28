package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月26日
 * @describe 应用管理
 */
public interface AppService {
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数: appName（应用名称，json）
	 * @return JSONObject
	 * @describe 查询所有的应用
	 */
	public  List<Map> queryAllApp(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数: appName（应用名称，json）
	 * @return JSONObject
	 * @describe 查询所有的应用
	 */
	public  List<Map> queryApp();
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  参数: appName应用名字， state 状态，remark 描述，sysid 主键id
	 * @return JSONObject
	 * @describe 修改应用
	 */
	public JSONObject updateApp(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map 参数: appName应用名称, state应用状态, remark应用描述
	 * @return JSONObject
	 * @describe 添加应用
	 */
	public JSONObject addApp (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid 应用id
	 * @return JSONObject
	 * @describe 删除应用
	 */
	public JSONObject deleteApp (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing  
	 * @param appName 应用名称
	 * @return JSONObject
	 * @describe   判断应用名字是否重复
	 */
	public JSONObject isRepeatAppName (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param appid 主键id
	 * @return JSONObject   （true为重复，false为没重复）
	 * @describe  判断应id是否重复
	 */
	public JSONObject isRepeatAppId (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 所有的应用以及对应的应用拥有的模板数和接口数
	 */
	public List<Map> queryAppWithMAmountAndIAmount(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid
	 * @return List<HashMap<String,String>>
	 * @describe 判断该应用是否可以删除
	 */
	public JSONObject isCanDeleteApp(HashMap<String,String> map);
}
