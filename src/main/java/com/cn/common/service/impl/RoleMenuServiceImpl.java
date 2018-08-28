package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cn.common.dao.RoleMenuMapper;
import com.cn.common.service.RoleMenuService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月22日
 * @describe 角色菜单权限管理
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService{

	@Resource
	RoleMenuMapper roleMenuMapper;


	@Override
	public JSONObject isExistRoleAtMenu(HashMap<String,String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed= roleMenuMapper.isExistRoleAtMenu(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该菜单已有角色在使用");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该菜单没有角色在使用");
		}
		return responce;
	}

	@Transactional
	@Override
	public void  addRoleMenu(List<HashMap<String, String>> list,String roleId) {
		
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("roleId", roleId);
		if(null==list||list.isEmpty()){
			roleMenuMapper.deleteRoleMenu(map);	
			return ;
		}
		roleMenuMapper.deleteRoleMenu(map);
		roleMenuMapper.addRoleMenu(list);	
	}


	@Override
	public JSONObject deleteRoleMenu(HashMap<String,String> map) {
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			 roleMenuMapper.deleteRoleMenu(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除角色菜单权限失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}


	@Override
	public List<Map> queryAllRoleAndMenuAmount() {
		
		return roleMenuMapper.queryAllRoleAndMenuAmount();
	}


	@Override
	public List<HashMap<String, String>> queryMenuForRole(HashMap<String,String> map) {
		
		return roleMenuMapper.queryMenuForRole(map);
	}
}
