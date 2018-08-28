package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author yangjing
 * @date 2016年12月9日
 * @describe 用户角色模块
 */
public interface UserRoleMapper {

	/**
	 * 
	 * @author yangjing 
	 * @param userId  用户id
	 * @return void
	 * @describe 通过用户id删除用户角色
	 */
	public void deleteByUserId(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param map roleId 角色id；userId 用户id；
	 * @return void
	 * @describe 给用户设置角色
	 */
	public void addUserRole(HashMap<String, String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return String （true有，false没有）
	 * @describe 判断该角色下是否有用户
	 */
	public String isExistUserAtRole(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map  roleId 角色id；userId 用户id；
	 * @return void
	 * @describe 删除用户角色
	 */
	public void deleteUserRole(HashMap<String, String> map);
	
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
