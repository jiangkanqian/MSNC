package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月28日
 * @describe 应用模板权限管理
 */
public interface AppModelMapper {
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe  查询所有的应用以及该应用拥有的模板数量
	 */
	public List<Map> queryAllAppAndModelAmount();

	/**
	 * 
	 * @author yangjing 
	 * @param appId   应用id
	 * @return List<HashMap<String,String>>
	 * @describe   查询该应用没有的模板
	 */
	public List<HashMap<String,String>> queryModelNotInApp(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return List<HashMap<String,String>>
	 * @describe  查询该应用拥有的模板
	 */
	public List<HashMap<String,String>> queryModelInApp(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param list appId，modelId,其中这个appId是同一个。格式如下：

				[{"appId":"1","modelId":"2"},
				{"appId":"1","modelId ":"3"},
				{"appId":"1"," modelId ":"8"}]
	 * @return void
	 * @describe 增加应用模板
	 */
	public void addAllAppModel(List<HashMap<String,String>> list);
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return void 
	 * @describe 删除应用模板
	 */
	public void deleteAllAppModel(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param modelId 模板id
	 * @return String （true操作成功，false操作失败）
	 * @describe 判断该模板是否有应用使用
	 */
	public String isExistAppAtModel(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map  appId 应用id,modelId模板id
	 * @return void
	 * @describe  删除单个应用模板
	 */
	public void deleteAppModel(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map  appId 应用id,modelId模板id
	 * @return void
	 * @describe 增加单个应用模板
	 */
	public void addAppModel(HashMap<String, String> map);

}
