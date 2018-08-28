package com.cn.common.web;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.common.service.UserRoleService;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;
/**
 * 
 * @author yangjing
 * @date 2016年12月9日
 * @describe 用户角色权限模块
 */
@Controller
@RequestMapping(value="/manager/userRole")
public class UserRoleController {
	
	Logger logger = LoggerFactory.getLogger(UserRoleController.class);
	
	@Resource
	private UserRoleService userRoleService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 判断该角色下是否有用户
	 */
	@ResponseBody
	@RequestMapping(value = "/isExistUserAtRole")
	public JSONObject  queryAllUser(@RequestBody HashMap<String,String> map){		
		
		return userRoleService.isExistUserAtRole(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map roleId 角色id；userId 用户id；
	 * @return JSONObject
	 * @describe 给用户设置角色
	 */
	@ResponseBody
	@RequestMapping(value = "/addUserRole")
	public JSONObject addUserRole(@RequestBody HashMap<String,String> map){
		JSONObject responce=JsonObjectUtil.initSucceed();
		try {
			userRoleService.addUserRole(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "增加用户角色权限失败！");
		}
		 return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param list userId
	 * @return JSONObject
	 * @describe 批量删除用户角色 权限
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteBatchUserRole")
	public JSONObject deleteBatchUser(HashMap<String,List<HashMap<String,String>>> map){
		
		JSONObject responce=JsonObjectUtil.initSucceed();
		List<HashMap<String,String>> list =map.get("data");
		logger.debug(list.toString());
		try {
			userRoleService.deleteBatchUserRole(list);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除用户角色权限失败！");
		}
		return	responce;
	}
}
