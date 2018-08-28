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

import com.cn.common.service.AppService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月26日
 * @describe 应用管理
 */
@Controller
@RequestMapping(value="/manager/app")
public class AppController {

	
	 Logger logger = LoggerFactory.getLogger(AppController.class);
	
	@Resource
	private AppService AppService;

	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数: appName（应用名称，json）
	 * @return JSONObject
	 * @describe 查询所有的应用
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllApp")
	public JSONObject queryAllApp(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = AppService.queryAllApp(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 参数: appName（应用名称，json）
	 * @return JSONObject
	 * @describe 查询所有的应用
	 */
	@ResponseBody
	@RequestMapping(value = "/queryApp")
	public JSONObject queryApp(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
        List<Map> list = AppService.queryApp();
        responce.put("data", list);
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return String （true操作成功，false操作失败）
	 * @describe 删除应用
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteApp")
	public JSONObject deleteApp(@RequestBody HashMap<String,String> map){

		return AppService.deleteApp(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 参数: appName应用名称, state应用状态, remark应用描述
	 * @return HashMap<String,String> appid应用id appName应用名字
	 * @describe 添加应用
	 */
	@ResponseBody
	@RequestMapping(value = "/addApp")
	public JSONObject addApp(@RequestBody HashMap<String,String> map){

		return AppService.addApp(map);
	}
	

	@ResponseBody
	@RequestMapping(value = "/updateApp")
	public JSONObject updateApp(@RequestBody HashMap<String,String>map){

		return AppService.updateApp(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param appName 应用名称
	 * @return String  （true为重复，false为没重复）
	 * @describe  判断应用名字是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatAppName")
	public JSONObject isRepeatAppName(@RequestBody HashMap<String,String> map){

		return AppService.isRepeatAppName(map);
	}

	/**
	 * 
	 * @author yangjing 
	 * @param appid 应用名称
	 * @return String  （true为重复，false为没重复）
	 * @describe  判断应用id是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatAppId")
	public JSONObject isRepeatAppId(@RequestBody HashMap<String,String> map){

		return AppService.isRepeatAppId(map);
	}
	/**
	 * 
	 * @author yangjing 
	 * @return List<HashMap<String,String>>
	 * @describe 所有的应用以及对应的应用拥有的模板数和接口数
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAppWithMAmountAndIAmount")
	public  JSONObject queryAppWithMAmountAndIAmount(@RequestBody HashMap<String,String> map){
			
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = AppService.queryAppWithMAmountAndIAmount(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid 应用id
	 * @return JSONObject
	 * @describe 判读该应用是否可以删除
	 */
	@ResponseBody
	@RequestMapping(value = "/isCanDeleteApp")
	public JSONObject isCanDeleteApp(@RequestBody HashMap<String,String> map){

		return AppService.isCanDeleteApp(map);
	}
}
