package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cn.common.dao.UserRoleMapper;
import com.cn.common.service.UserRoleService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

@Service
public class UserRoleServiceImpl implements UserRoleService{

	@Resource
	UserRoleMapper userRoleMapper;

	@Transactional
	@Override
	public void addUserRole(HashMap<String,String> map) {
	
		userRoleMapper.deleteByUserId(map);
		userRoleMapper.addUserRole(map);
	}

	@Override
	public JSONObject isExistUserAtRole(HashMap<String,String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed = userRoleMapper.isExistUserAtRole(map);
		if (Msnc.TRUE.equals(isUsed)) {
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该角色已有用户使用");
		} else if (Msnc.FALSE.equals(isUsed)) {
			responce.put("retMsg", "该角色无用户使用");
		}
		return responce;
	}
	
	@Override
	public JSONObject deleteUserRole(HashMap<String,String> map) {
		
		JSONObject responce = null;
		try {
			userRoleMapper.deleteUserRole(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "删除用户权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}
	
	@Transactional
	@Override
	public void deleteBatchUserRole(List<HashMap<String, String>> list) {
		
		userRoleMapper.deleteBatchUserRole(list);
	}

	@Override
	public List<HashMap<String, String>> queryUserByRole(HashMap<String,String> map) {
		
		return userRoleMapper.queryUserByRole(map);
	}
	

}
