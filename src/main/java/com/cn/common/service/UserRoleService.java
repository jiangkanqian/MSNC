package com.cn.common.service;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月9日
 * @describe 用户角色权限模块
 */
public interface UserRoleService {

	/**
	 * 
	 * @author yangjing 
	 * @param map roleId 角色id；userId 用户id；
	 * @return JSONObject
	 * @describe 给用户设置角色
	 */
	public void addUserRole(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 判断该角色下是否有用户
	 */
	public JSONObject isExistUserAtRole(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map  roleId 角色id；userId 用户id；
	 * @return JSONObject
	 * @describe 删除用户角色
	 */
	public JSONObject deleteUserRole(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param list 
	 * @return void
	 * @describe 批量删除用户的角色权限
	 */
	public void deleteBatchUserRole(List<HashMap<String,String>> list);
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return List<HashMap<String,String>>
	 * @describe 查询说有的用户id
	 */
	public List<HashMap<String,String>> queryUserByRole(HashMap<String,String> map);
}
