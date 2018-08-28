package com.cn.common.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.common.service.UserService;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月6日
 * @describe 用户模块
 */
@Controller
@RequestMapping(value="/manager/user")
public class UserController extends HomeController{

	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Resource
	private UserService userService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 *  account 用户账号;
	 *  userName 用户姓名;
	 * @return JSONObject 
	 * @describe  查询所有用户，与用户直接查询都采用此函数
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllUser")
	public  JSONObject queryAllUser(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list =userService.queryAllUser(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	
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
	 *       roleId 角色id;
	 * @return String （true操作成功，false操作失败）
	 * @describe 添加用户
	 */
	@ResponseBody
	@RequestMapping(value = "/addUser")
	public JSONObject addUser(@RequestBody HashMap<String,String> user){
		logger.debug(user.toString());
		String roleId=user.get("roleId").toString();
		JSONObject responce=JsonObjectUtil.initSucceed();
		try {
			userService.addUser(user,roleId);
		} catch (Exception e) {
			e.printStackTrace();		
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加用户失败！");
		}
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param  map userId 用户id；roleId 角色id
	 * @return String  （true操作成功，false操作失败）
	 * @describe 通过用户的id来删除此用户
	 */
	@RequestMapping(value = "/deleteUser")
	@ResponseBody
	public 	JSONObject deleteUser(@RequestBody HashMap<String,String> map){
		JSONObject responce=JsonObjectUtil.initSucceed();
		try {
			userService.delete(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除用户失败！");
		}
		return	responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param account 用户账号
	 * @return String 重复则为true 否则为false
	 * @describe 判读用户的账号有没有重复
	 */
	@RequestMapping(value = "/isRepeatAcount")
	@ResponseBody
	public JSONObject  isRepeatAcount(@RequestBody HashMap<String,String> map){
		
		return userService.isRepeatAcount(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param  map userId 用户的id
 		password 用户的密码 如果是重置密码，不用传password直接默认为666666
	 * @return String （true操作成功，false操作失败）
	 * @describe 用于修改用户的密码
	 */
	@RequestMapping(value = "/updatePassword")
	@ResponseBody
	public JSONObject updatePassword(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initError();
        HashMap<String,String> checkPassword=new HashMap<String,String>();
        checkPassword.put("account", StringUtil.getString(map.get("account")));
        Object oldPassword=map.get("oldPassword");
        checkPassword.put("password",oldPassword.toString());
		 HashMap<String,String> user=userService.checkoutLogginQuery(checkPassword);
		 if(null==user||user.isEmpty()){
			 responce.put("retMsg", "密码错误");
			 return responce;
		 }
		return userService.updatePassword(map);
	}
	/**
	 * 
	 * @author yangjing 
	 * @param  map userId 用户的id
 		password 用户的密码 如果是重置密码，不用传password直接默认为666666
	 * @return String （true操作成功，false操作失败）
	 * @describe 用于修改用户的密码
	 */
	@RequestMapping(value = "/resetPassword")
	@ResponseBody
	public JSONObject resetPassword(@RequestBody HashMap<String,String> map){
		
		return userService.updatePassword(map);
	}
	
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
	 *       roleId 角色id;
	 * @return String （true操作成功，false操作失败）
	 * @describe 用于修改用户的基本资料
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUser")
	public JSONObject updateUser(@RequestBody HashMap<String,String> user){
		
		JSONObject responce=JsonObjectUtil.initSucceed();
		logger.debug(user.toString());
		try {
			Object roleId=user.get("roleId");
			userService.updateUser(user,roleId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改用户失败！");
		}
		return	responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param userId 用户id
	 * @return User 根用户的基本信息
	 * @describe 通过用id来查询改用户的基本信息44
	 */
	@ResponseBody
	@RequestMapping(value = "/queryUserById")
	public JSONObject queryUserById(@RequestBody HashMap<String,String> map){
		
		return userService.queryUserById(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param account 用户名或者账号
	 * @return String 
	 * @describe 校验用户名或者账号是否存在
	 */
	@ResponseBody
	@RequestMapping(value = "/checkAccount")
	public JSONObject  checkAccount(@RequestBody HashMap<String,String> map){
		
		return userService.checkAccount(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param list userId
	 * @return JSONObject
	 * @describe 批量删除用户。
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteBatchUser")
	public JSONObject deleteBatchUser(@RequestBody HashMap<String,List<HashMap<String,String>>> map){
		
		JSONObject responce=JsonObjectUtil.initSucceed();
		List<HashMap<String,String>> list =map.get("data");
		logger.debug(list.toString());
		try {
			userService.deleteBatchUser(list);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除用户失败！");
		}
		return	responce;
	}
}
