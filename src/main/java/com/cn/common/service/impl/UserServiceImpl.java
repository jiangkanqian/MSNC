package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cn.common.dao.UserMapper;
import com.cn.common.dao.UserRoleMapper;
import com.cn.common.entity.User;
import com.cn.common.service.UserService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.DesUtils;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月6日
 * @describe 用户模块
 */
@Service
public class UserServiceImpl implements UserService {

	@Resource
	UserMapper userMapper;

	@Resource
	UserRoleMapper userRoleMapper;

	@Override
	public List<Map> queryAllUser(HashMap<String, String> map) {

		return userMapper.queryAllUser(map);
	}

	@Transactional
	@Override
	public void addUser(HashMap<String, String> user, String roleId) {

		user.put("userId", "");
		user.put("password", DesUtils.encode(user.get("password")));
		int one = userMapper.addUser(user);
		HashMap<String, String> map = new HashMap<String, String>();
		System.out.println(one + "        " + user.get("userId"));
		map.put("userId", user.get("userId").toString());
		map.put("roleId", roleId);
		userRoleMapper.addUserRole(map);

	}

	@Transactional
	@Override
	public void delete(HashMap<String, String> map) {

		userMapper.deleteUser(map);
		userRoleMapper.deleteByUserId(map);

	}

	@Override
	public JSONObject isRepeatAcount(HashMap<String,String> map) {

		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed = userMapper.isRepeatAcount(map);
		if (Msnc.TRUE.equals(isUsed)) {
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该账号已重复");
		} else if (Msnc.FALSE.equals(isUsed)) {
			responce.put("retMsg", "该账号没有重复");
		}
		return responce;
	}

	@Override
	public JSONObject updatePassword(HashMap<String, String> map) {

		JSONObject responce = null;
		try {
			Object password=map.get("password");
			if(null!=password.toString()&&!password.toString().isEmpty()){
				map.put("password", DesUtils.encode(password.toString()));
			}
			userMapper.updatePassword(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "修改密码失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Transactional
	@Override
	public void updateUser(HashMap<String, String> user, String roleId) {

		userMapper.updateUser(user);
		HashMap<String, String> map = new HashMap<String, String>();
		Object userId=user.get("userId");
		map.put("userId", userId.toString());
		map.put("roleId", roleId);
		userRoleMapper.deleteByUserId(map);
		userRoleMapper.addUserRole(map);
	}

	@Override
	public JSONObject queryUserById(HashMap<String, String> map) {

		JSONObject responce = JsonObjectUtil.initSucceed();
		responce.put("data", userMapper.queryUserById(map));
		return responce;
	}

	@Override
	public HashMap<String, String> checkoutLogginQuery(HashMap<String, String> map) {

		map.put("password", DesUtils.encode(map.get("password")));
		return userMapper.checkoutLogginQuery(map);
	}

	@Override
	public JSONObject checkAccount(HashMap<String, String> map) {

		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed = userMapper.checkAccount(map);
		if (Msnc.TRUE.equals(isUsed)) {
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该账号存在");
		} else if (Msnc.FALSE.equals(isUsed)) {
			responce.put("retMsg", "该不账号存在");
		}
		return responce;
	}
	
	@Transactional
	@Override
	public void deleteBatchUser(List<HashMap<String, String>> list) {
		
		userMapper.deleteBatchUser(list);
		userRoleMapper.deleteBatchUserRole(list);
	}
}
