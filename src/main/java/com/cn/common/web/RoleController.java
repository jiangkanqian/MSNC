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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.common.service.RoleService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月8日
 * @describe 角色管理
 */
@Controller
@RequestMapping(value="/manager/role")
public class RoleController {

	
	 Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Resource
	private RoleService roleService;
	
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的角色  
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllRole")
	public JSONObject queryAllUser(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list =roleService.queryAllRole(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
      	return responce;
	}
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的角色  
	 */
	@ResponseBody
	@RequestMapping(value = "/allRole")
	public JSONObject allRole(){

        JSONObject responce=JsonObjectUtil.initSucceed();
        responce.put("data", roleService.queryAllRole(null));
		return responce;	
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleId 角色id
	 * @return JSONObject
	 * @describe 删除角色
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteRole")
	public JSONObject deleteRole(@RequestBody HashMap<String,String> map){
		
		return roleService.deleteRole(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleName 角色名称
	 * @return JSONObject
	 * @describe 添加角色
	 */
	@ResponseBody
	@RequestMapping(value = "/addRole")
	public JSONObject addRole(@RequestBody HashMap<String,String> map){
		
		return roleService.addRole(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param roleName 角色名称
	 * @return JSONObject
	 * @describe 判断角色名称有没有重复 
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatRoleName")
	public JSONObject isRepeatRoleName(@RequestBody HashMap<String,String> map){
		
		return roleService.isRepeatRoleName(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数:	roleName 角色名字,
	 * 		roleId 角色id
	 * @return JSONObject
	 * @describe 修改角色
	 */
	@ResponseBody
	@RequestMapping(value = "/updateRole", method = RequestMethod.POST ,produces = {"application/json;charset=utf-8" })
	public JSONObject updateRole(@RequestBody HashMap<String,String>map){
		
		return roleService.updateRole(map);
	}
	
}
