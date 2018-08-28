package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.cn.common.entity.User;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月6日
 * @describe 用户模块
 */
public interface UserService {

	
	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * userName 用户名，password 用户密码，account 用户账号
	 * @return  HashMap<String, String>
	 * @describe 校验密码是否正确
	 */
	public HashMap<String, String> checkoutLogginQuery(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 *  account 用户账号;
	 *  userName 用户姓名;
	 * @return JSONObject
	 * @describe  查询所有用户，与用户直接查询都采用此函数
	 */
	public List<Map> queryAllUser(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param user 用户模型
	 * 		 userName 用户姓名;
	 *       phone 用户电话;
	 *       email 用户邮件;
	 *       account 用户账号(不可以重复);
	 *       passwrod 密码;
	 *       sex  性别 F（女）M（男）;
	 *       empName 部门名字;
	 *       userState 状态 (0=注销，1=正常);
	 * @param   roleId 角色id;
	 * @return JSONObject
	 * @describe 添加用户
	 */
	public void addUser(HashMap<String,String> user,String roleId);

	/**
	 * 
	 * @author yangjing 
	 * @param map userId 用户id,roleId 角色id
	 * @return JSONObject
	 * @describe 通过用户的id来删除此用户
	 */
	public void delete(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param account 用户账号
	 * @return JSONObject
	 * @describe 判读用户的账号有没有重复
	 */
	public JSONObject isRepeatAcount(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param  map userId 用户的id
 		password 用户的密码 如果是重置密码，不用传password直接默认为666666
	 * @return JSONObject
	 * @describe 用于修改用户的密码
	 */
	public JSONObject updatePassword(HashMap<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param user 用户模型
	 * 		userName 用户姓名;
	 *       phone 用户电话;
	 *       email 用户邮件;
	 *       account 用户账号(不可以重复);
	 *       sex  性别 F（女）M（男）;
	 *       empName 部门名字;
	 *       userState 状态 (0=注销，1=正常);
	 * @param    roleId 角色id;
	 * @return JSONObject
	 * @describe 用于修改用户的基本资料
	 */
	public void updateUser(HashMap<String, String> user,String roleId);

	/**
	 * 
	 * @author yangjing 
	 * @param userId 用户id
	 * @return JSONObject
	 * @describe 通过用id来查询改用户的基本信息
	 */
	public JSONObject queryUserById(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param account 用户名或者账号
	 * @return JSONObject
	 * @describe 校验用户名或者账号是否存在
	 */
	public JSONObject checkAccount(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param list 
	 * @return void
	 * @describe 批量删除用户
	 */
	public void deleteBatchUser(List<HashMap<String,String>> list);
}
