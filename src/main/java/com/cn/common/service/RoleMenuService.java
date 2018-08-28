package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月22日
 * @describe 角色菜单权限管理
 */
public interface RoleMenuService {

	/**
	 * 
	 * @author yangjing 
	 * @param menuId 菜单id
	 * @return JSONObject
	 * @describe 判断该菜单下是否有角色存在
	 */
	public JSONObject isExistRoleAtMenu(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param list
	 * 参数： roleId 角色id，menuId 菜单id，isAll 是否拥有所有的权限（0=非，1=是）
	 * ，其中这个roleId是同一个。格式如下：
		[{"roleId":"1","menuId":"2","isAll":"0"},
		{"roleId":"1","menuId":"3","isAll":"0"},
		{"roleId":"1","menuId":"8","isAll":"0"}]
		
		isAll是代表所有的菜单，所以，当所有的菜单都勾上的时候只需要roleId跟isAll
		格式如下:[{"roleId":"1","isAll":"0"}]
	 * @return JSONObject
	 * @describe 增加角色的菜单权限
	 */
	public 	void addRoleMenu(List<HashMap<String,String>> list,String roleId);
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 删除该角色的所有菜单
	 */
	public JSONObject deleteRoleMenu(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的 角色跟该角色拥有的菜单数量
	 */
	public List<Map> queryAllRoleAndMenuAmount();

	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 查询改角色所有的菜单
	 */
	public List<HashMap<String, String>> queryMenuForRole(HashMap<String,String> map);
}
