package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cn.common.dao.RoleMapper;
import com.cn.common.service.RoleService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月8日
 * @describe 角色管理
 */
@Service
public class RoleServiceImpl implements RoleService{

	@Resource
	RoleMapper roleMapper;

	@Override
	public List<Map> queryAllRole(HashMap<String, String> map) {
		return roleMapper.queryAllRole(map);
	}
	
	@Transactional
	@Override
	public JSONObject deleteRole(HashMap<String,String> map){
		
		JSONObject responce = null;
		try {
			roleMapper.deleteRole(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除角色失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}
	
	@Override
	public JSONObject addRole(HashMap<String,String> map){
		
		JSONObject responce = null;
		try {
			roleMapper.addRole(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加角色失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}
	
	@Override
	public JSONObject isRepeatRoleName(HashMap<String,String> map){

		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=roleMapper.isRepeatRoleName(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该角色名称已经重复");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该角色名称没有重复");
		}
		return responce;
	}

	@Override
	public JSONObject updateRole(HashMap<String,String> map){
		JSONObject responce = null;
		try {
			 roleMapper.updateRole(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改角色失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();

	}
}
