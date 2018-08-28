package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月21日
 * @describe 菜单管理
 */
public interface MenuService {

	
	/**
	 * 
	 * @author yangjing 
	 * @param userId 用户id
	 * @return JSONObject
	 * @describe 根据用户id来查询该用户拥有的菜单权限
	 */
	public List<Map> queryUserMenu(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的菜单
	 */
	public List<Map> queryAllMenu(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数: menuName 菜单名字, 
	 * 		level 菜单级别, 
	 * 		parentId 父菜单id, 
	 * 		url 菜单链接, 
	 * 		isDisplay 是否可见,
	 *  	remark 备注
	 * @return JSONObject
	 * @describe 菜单的增加
	 */
	public JSONObject addMenu(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid    菜单的id
	 * @return JSONObject
	 * @describe 菜单删除
	 */
	public JSONObject deleteMenu(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 参数: menuName 菜单名字, 
	 * 		level 菜单级别, 
	 * 		parentId 父菜单id, 
	 * 		url 菜单链接, 
	 * 		isDisplay 是否可见,
	 *  	remark 备注
	 *      sysid 菜单id
	 * @return JSONObject
	 * @describe 菜单修改
	 */
	public JSONObject updateMenu(HashMap<String,String> map);
}
