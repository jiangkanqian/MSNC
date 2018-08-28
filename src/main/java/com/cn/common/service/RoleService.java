package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月8日
 * @describe 角色管理
 */
public interface RoleService {

	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的角色
	 */
	public List<Map> queryAllRole(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 删除角色
	 */
	public JSONObject deleteRole(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param roleName 角色名称
	 * @return JSONObject
	 * @describe 添加角色
	 */
	public JSONObject addRole(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param roleName 角色名称
	 * @return JSONObject
	 * @describe 判断角色名称有没有重复 
	 */
	public JSONObject isRepeatRoleName(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数:	roleName 角色名字,
	 * 		roleId 角色id
	 * @return JSONObject
	 * @describe 修改角色
	 */
	public JSONObject updateRole(HashMap<String,String>map);

}
