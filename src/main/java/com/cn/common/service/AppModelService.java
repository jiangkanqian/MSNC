package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 
 * @author yangjing
 * @date 2016年12月28日
 * @describe 应用模板权限管理
 */
public interface AppModelService {
	
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的应用以及该应用拥有的模板数量
	 */
	public List<Map> queryAllAppAndModelAmount();
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId  应用id
	 * @return List<HashMap<String, String>>
	 * @describe  查询该应用没有的模板
	 */
	public List<HashMap<String, String>> queryModelNotInApp(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return List<HashMap<String, String>>
	 * @describe 查询该应用拥有的模板
	 */
	public List<HashMap<String, String>> queryModelInApp(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param list appId，modelId,其中这个appId是同一个。格式如下：

				[{"appId":"1","modelId":"2"},
				{"appId":"1","modelId ":"3"},
				{"appId":"1"," modelId ":"8"}]
	 * @return JSONObject
	 * @describe 增加应用模板
	 */
	public void addAllAppModel(List<HashMap<String,String>> list,String appId);
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return JSONObject
	 * @describe 删除应用模板
	 */
	public JSONObject deleteAllAppModel(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param modelId 模板id
	 * @return JSONObject
	 * @describe 判断该模板是否有应用使用
	 */
	public JSONObject isExistAppAtModel(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id,modelId模板id
	 * @return JSONObject
	 * @describe 增加单个应用模板
	 */ 
	public JSONObject addAppModel(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id,modelId模板id
	 * @return JSONObject
	 * @describe 删除单个应用模板
	 */ 
	public JSONObject deleteAppModel(HashMap<String, String> map);

}
