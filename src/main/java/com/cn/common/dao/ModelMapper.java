package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月26日
 * @describe 模板管理
 */
public interface ModelMapper {

	/**
	 * 
	 * @author yangjing 
	 * @param map  modelId模板id， modelName模板名字
	 * @return List<HashMap<String,String>>
	 * @describe  查询所有的模板
	 */
	public List<Map> queryAllModel(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  map modelName 模板名字，content模板内容， modelType 模板类型
	 * ，state 模板状态 ，remark模板描述 ，modelId模板id
	 * @return void
	 * @describe  修改模板
	 */
	public void updateModel(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  modelName模板名字, modelId模板id, content模板内容,
 	 *   modelType模板类型, state模板状态, remark模板描述,userId模板创建人
 	 *   
	 * @return void
	 * @describe 添加模板
	 */
	public void addModel (HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param modelId  模板id
	 * @return void
	 * @describe 删除模板
	 */ 
	public void deleteModel (HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param modelId  模板id
	 * @return String （true为重复，false为没重复）
	 * @describe 判断模板id是否重复
	 */
	public String isRepeatModelId (HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param modelName  模板名字
	 * @return String  （true为重复，false为没重复）
	 * @describe  判断模板名字是否重复
	 */
	public String isRepeatModelName (HashMap<String,String> map);
	
	/**
	 * @author chen.kai
	 * @param String modelcode 模版id
	 * @return String content 
	 * 根据模版id获取模版内容
	 */
	public String  findModelContent(String modelcode);
}