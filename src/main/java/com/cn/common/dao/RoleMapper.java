package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月8日
 * @describe 角色管理
 */
public interface RoleMapper {

	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe 查询所有的角色
	 */
	public List<Map> queryAllRole(HashMap<String, String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleName 角色名称
	 * @return void
	 * @describe 添加角色
	 */
	public void addRole(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return void
	 * @describe 删除角色
	 */
	public void deleteRole(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param roleName 角色名称
	 * @return String （true为重复，false为没重复）
	 * @describe 判断角色名称有没有重复 
	 */
	public String isRepeatRoleName(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数:	roleName 角色名字,
	 * 		roleId 角色id
	 * @return void
	 * @describe 修改角色
	 */
	public void updateRole(HashMap<String,String> map);
}
