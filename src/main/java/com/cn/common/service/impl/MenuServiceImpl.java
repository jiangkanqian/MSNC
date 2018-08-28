package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.MenuMapper;
import com.cn.common.service.MenuService;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月21日
 * @describe 菜单管理
 */
@Service
public class MenuServiceImpl implements MenuService{

	@Resource
	MenuMapper menuMapper;

	@Override
	public List<Map> queryAllMenu(HashMap<String,String> map) {
		
		return menuMapper.queryAllMenu(map);
	}
	
	@Override
	public JSONObject deleteMenu(HashMap<String,String> map){
		
		JSONObject responce = null;
		try {
			menuMapper.deleteMenu(map);			
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除菜单失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject updateMenu(HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			 menuMapper.updateMenu(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改菜单失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject addMenu(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			menuMapper.addMenu(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加菜单失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public List<Map> queryUserMenu(HashMap<String,String> map) {
		
		return menuMapper.queryUserMenu(map);
	}
}
