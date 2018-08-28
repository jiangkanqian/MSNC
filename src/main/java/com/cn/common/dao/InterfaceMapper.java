package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author yangjing
 * @date 2017年1月3日
 * @describe 接口管理
 */
public interface InterfaceMapper {

	/**
	 * 
	 * @author yangjing 
     * @param map 参数: interfaceType 接口类型， interface_name接口名字
	 * @return List<HashMap<String,String>>
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
	 * @return void
	 * @describe  接口的增加
	 */
	public void addInterface(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param interfaceId 接口id
	 * @return void
	 * @describe  接口的删除
	 */
	public void deleteInterface(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  参数: interfaceName 接口名字，
		interfaceUrl 接口url,getPost 接口请求方式,interfaceType 接口类型,
		interfaceSonType 接口子类型,interfaceMsg 接口描述，callMode 接口调用实例
		，interfaceId 接口id
	 * @return void
	 * @describe 接口修改
	 */
	public void updateInterface(HashMap<String,String> map);

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
