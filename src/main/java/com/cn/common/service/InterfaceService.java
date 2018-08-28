package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年1月3日
 * @describe 接口管理
 */
public interface InterfaceService {

	/**
	 * 
	 * @author yangjing 
     * @param map 参数: interfaceType 接口类型， interface_name接口名字
	 * @return List<Map>
	 * @describe 查询所有的接口
	 */
	public  List<Map> queryAllInterface(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 *  参数: interfaceName 接口名字，
		interfaceUrl 接口url,getPost 接口请求方式,interfaceType 接口类型,
		interfaceSonType 接口子类型,interfaceMsg 接口描述，callMode 接口调用实例
	 * @return JSONObject
	 * @describe  接口的增加
	 */
	public JSONObject addInterface(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param interfaceId 接口id
	 * @return JSONObject
	 * @describe  接口的删除
	 */
	public JSONObject deleteInterface(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  参数: interfaceName 接口名字，
		interfaceUrl 接口url,getPost 接口请求方式,interfaceType 接口类型,
		interfaceSonType 接口子类型,interfaceMsg 接口描述，callMode 接口调用实例
		，interfaceId 接口id
	 * @return JSONObject
	 * @describe  修改接口
	 */
	public JSONObject updateInterface(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param url 接口的路径
	 * @return void
	 * @describe 查询该接口的子类型
	 */
	public String queryInterfaceSonType(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @return List<Map<String, String>>
	 * @describe 获取接口子类型与url
	 */
	public List<Map<String, String>> getUrlAndInterfaceSonType();
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<Map<String,String>>
	 * @describe 查询所有接口的名字
	 */
	public List<Map<String, String>> queryAllInterfaceName();
}
