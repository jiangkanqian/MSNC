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

import com.cn.common.service.MenuService;
import com.cn.common.service.RoleMenuService;
import com.cn.common.service.UserRoleService;
import com.cn.common.util.DesUtils;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.RedisCacheUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;


/**
 * 
 * @author yangjing
 * @date 2016年12月22日
 * @describe 角色菜单权限管理
 */
@Controller
@RequestMapping(value="/manager/roleMenu")
public class RoleMenuController {

	
	Logger logger = LoggerFactory.getLogger(RoleMenuController.class);
	
	@Resource
	private UserRoleService userRoleService;
	
	@Resource
	private RoleMenuService roleMenuService;
	
	@Resource 
	RedisCacheUtil<?> util;
	
	@Resource
	private MenuService menuService;
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的 角色跟该角色拥有的菜单数量
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllRoleAndMenuAmount")
	public  JSONObject queryAllRoleAndMenuAmount(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = roleMenuService.queryAllRoleAndMenuAmount();
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 删除该角色的所有菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteRoleMenu")
	public JSONObject deleteRoleMenu(@RequestBody HashMap<String,String> map){
		
		return roleMenuService.deleteRoleMenu(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param list
	 * 参数： roleId 角色id，menuId 菜单id，isAll 是否拥有所有的权限（0=非，1=是）
	 * ，其中这个roleId是同一个。格式如下：
		[{"roleId":"1","menuId":"2","isAll":"0"},
		{"roleId":"1","menuId":"3","isAll":"0"},
		{"roleId":"1","menuId":"8","isAll":"0"}]
		
		isAll是代表所有的菜单，所以，当所有的菜单都勾上的时候只需要roleId跟isAll
		格式如下:[{"roleId":"1","isAll":"0"}]
	 * @return JSONObject
	 * @describe 增加角色的菜单权限
	 */
	@ResponseBody
	@RequestMapping(value = "/addRoleMenu")
	public JSONObject addRoleMenu(@RequestBody HashMap<String,List<HashMap<String, String>>> map){
		logger.info(map.toString());
		List<HashMap<String,String>> list= map.get("data");
		Object roleId=map.get("roleId");
		logger.info(roleId.toString());
		JSONObject responce=JsonObjectUtil.initSucceed();
		try {
			roleMenuService.addRoleMenu(list,roleId.toString());
			refreshCache(roleId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加角色菜单权限失败！");
		}
		return responce;
	}
	
	private void refreshCache(String roleId) {
		HashMap<String, String> data= new HashMap<String, String>();
		data.put("roleId", roleId);
		List<HashMap<String, String>> list=userRoleService.queryUserByRole(data);
		if(null!=list&&!list.isEmpty()){
			for (int i = 0; i <list.size(); i++) {
				HashMap<String, String> map=list.get(i);		
				Object userId=map.get("identity");
				String key=DesUtils.encode(userId.toString());
				if(null==util.getCacheObject(key)){
					continue;	
				}else{	
					util.removeCacheObject(key);
//					List<Map> menu=menuService.queryUserMenu(map);
//					if(null==menu||menu.isEmpty()){
//						menu=new ArrayList<Map>();
//						JSONObject responce = JsonObjectUtil.initError();
//						responce.put("retMsg", "该用户没有权限！");
//						menu.add(responce);
//						util.setCacheObject(key, responce, Msnc.SessionOutTime);
//					}else{
//						util.setCacheObject(key, menu, Msnc.SessionOutTime);
//					}
				}
			}
		}
	}

	/**
	 * 
	 * @author yangjing 
	 * @param menuId 菜单id
	 * @return JSONObject
	 * @describe 判断该菜单下是否有角色存在
	 */
	@ResponseBody
	@RequestMapping(value = "/isExistRoleAtMenu")
	public JSONObject isExistRoleAtMenu(@RequestBody HashMap<String,String> map){
		
		return roleMenuService.isExistRoleAtMenu(map);
	}
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 查询改角色所有的菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/queryMenuForRole")
	public JSONObject queryMenuForRole(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
        responce.put("data", roleMenuService.queryMenuForRole(map));
		return responce;
	}
}
