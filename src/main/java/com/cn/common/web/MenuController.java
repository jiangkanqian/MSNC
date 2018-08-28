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
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月21日
 * @describe 菜单管理模块
 */
@Controller
@RequestMapping(value="/manager/menu")
public class MenuController {

	
	 Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@Resource
	private MenuService menuService;
	
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllMenu")
	public JSONObject queryAllMenu(@RequestBody HashMap<String,String> map){

		JSONObject responce = JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = menuService.queryAllMenu(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
      	return responce;
	}
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 动态菜单的显示
	 */
	@ResponseBody
	@RequestMapping(value = "/queryDynamicMenu")
	public JSONObject  queryDynamicMenu(){

		JSONObject responce = JsonObjectUtil.initSucceed();
        responce.put("data", menuService.queryAllMenu(null));
		return responce;	
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysId 菜单的sysid
	 * @return JSONObject
	 * @describe 删除菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteMenu")
	public JSONObject deleteMenu(@RequestBody HashMap<String,String> map){
		
		return menuService.deleteMenu(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数: menuName 菜单名字, 
	 * 		level 菜单级别, 
	 * 		parentId 父菜单id, 
	 * 		url 菜单链接, 
	 * 		isDisplay 是否可见,
	 *  	remark 备注
	 * @return JSONObject
	 * @describe 菜单的增加
	 */
	@ResponseBody
	@RequestMapping(value = "/addMenu")
	public JSONObject addMenu(@RequestBody HashMap<String,String> map){
		
		return menuService.addMenu(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 参数: menuName 菜单名字, 
	 * 		level 菜单级别, 
	 * 		parentId 父菜单id, 
	 * 		url 菜单链接, 
	 * 		isDisplay 是否可见,
	 *  	remark 备注
	 *      sysid 菜单id
	 * @return JSONObject
	 * @describe 修改菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/updateMenu")
	public JSONObject updateMenu(@RequestBody HashMap<String,String> map){
	
		return menuService.updateMenu(map);
	}
	
}
