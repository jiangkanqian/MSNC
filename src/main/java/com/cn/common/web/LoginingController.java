package com.cn.common.web;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.common.service.MenuService;
import com.cn.common.service.UserService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.DesUtils;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.RedisCacheUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 * @author yangjing
 * @date 2017年2月8日
 * @describe 登录
 */
@Controller
@RequestMapping(value="/manager/logining")
public class LoginingController{
	
	private static final Logger logger = LoggerFactory.getLogger(LoginingController.class);
	//Spring  jackjson转换器
	ObjectMapper mapper=new ObjectMapper();
	@Resource
	private UserService userService;
	@Resource
	private MenuService menuService;
	
	@Resource 
	RedisCacheUtil<?> util;
	
	@ResponseBody
	@RequestMapping("/login")
	public JSONObject login(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		Map<String,String> user=userService.checkoutLogginQuery(map);
		Integer userState=0;
		if(null==user||user.isEmpty()){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "用户名或者密码错误");
			return responce;
		}
		if(null!=user&&userState.equals(user.get("userState"))){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该用户已注销");
			return responce;
		}
		Object userId=user.get("userId");
		Object account=user.get("account");
		user.put("identity", DesUtils.encode(userId.toString()+"-"+account.toString()));
		responce.put("data", user);
		return responce;
	}
	
	@ResponseBody
	@RequestMapping("/initMenu")
	public JSONObject initMenu(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		Object userId=map.get("userId");
		List<Map> menu=menuService.queryUserMenu(map);
		if(null==menu||menu.isEmpty()){
			//util.setCacheObject(userId, menu, Msnc.SessionOutTime);
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该用户没有权限！");
		}else{
			util.removeCacheObject(map.get("identity").toString());
			util.setCacheObject(map.get("identity").toString(), JSONArray.fromObject(menu).toString(), Msnc.SessionOutTime);
		}
		responce.put("data", menu);
		return responce;	
	}
}
