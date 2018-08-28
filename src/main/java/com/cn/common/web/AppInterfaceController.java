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

import com.cn.common.service.AppInterfaceService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年12月30日
 * @describe 应用接口授权管理
 */
@Controller
@RequestMapping(value = "/manager/appInterface")
public class AppInterfaceController {

	Logger logger = LoggerFactory.getLogger(AppInterfaceController.class);

	@Resource
	private AppInterfaceService appInterfaceService;

	/**
	 * 
	 * @author yangjing
	 * @return JSONObject
	 * @describe 查询所有的应用以及该应用拥有的接口数
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllAppAndInterfaceAmount")
	public JSONObject queryAllAppAndInterfaceAmount(@RequestBody HashMap<String, String> map) {

		JSONObject responce = JsonObjectUtil.initSucceed();
		Integer page = map.get("page") == null ? 1 : Integer.parseInt(map.get("page"));
		Integer pageSize = map.get("pageSize") == null ? 10 : Integer.parseInt(map.get("pageSize"));
		PageHelper.startPage(page, pageSize);
		List<Map> list = appInterfaceService.queryAllAppAndInterfaceAmount(map);
		// 初始化分页对象的数据，包括总条数，上一页，下一页等等
		PageInfo<Map> p = new PageInfo<Map>(list);
		responce.put("data", p);
		return responce;
	}

	/**
	 * 
	 * @author yangjing
	 * @param appId
	 *            应用id
	 * @return JSONObject
	 * @describe 查询该应用没有的接口
	 */
	@ResponseBody
	@RequestMapping(value = "/queryInterfaceNotInApp")
	public JSONObject queryInterfaceNotInApp(@RequestBody HashMap<String,String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		responce.put("data", appInterfaceService.queryInterfaceNotInApp(map));
		return responce;
	}

	/**
	 * 
	 * @author yangjing
	 * @param appId
	 *            应用id
	 * @return JSONObject
	 * @describe 查询该应用拥有的接口
	 */
	@ResponseBody
	@RequestMapping(value = "/queryInterfaceInApp")
	public JSONObject queryInterfaceInApp(@RequestBody HashMap<String,String> map) {

		JSONObject responce = JsonObjectUtil.initSucceed();
		responce.put("data", appInterfaceService.queryInterfaceInApp(map));
		return responce;
	}

	/**
	 * 
	 * @author yangjing
	 * @param list
	 *            参数：appId，interfaceId,其中这个appId是同一个。格式如下： [{"appId":"1","
	 *            interfaceId":"2"}, {"appId":"1"," interfaceId":"3"},
	 *            {"appId":"1"," interfaceId":"8"}]
	 * @return JSONObject
	 * @describe 增加应用接口
	 */
	@ResponseBody
	@RequestMapping(value = "/addAllAppInterface")
	public JSONObject addAllAppInterface(@RequestBody HashMap<String,List<HashMap<String, String>>> map) {
		List<HashMap<String,String>> list= map.get("data");
		Object appId = map.get("appId");
		JSONObject responce=JsonObjectUtil.initSucceed();
		try {
			appInterfaceService.addAllAppInterface(list,appId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加该应用的接口权限失败！");
		}
		return responce;
	}

	/**
	 * 
	 * @author yangjing
	 * @param appId
	 *            应用id
	 * @return JSONObject
	 * @describe 删除应用接口
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAllAppInterface")
	public JSONObject deleteAllAppInterface(@RequestBody HashMap<String,String> map) {

		return appInterfaceService.deleteAllAppInterface(map);
	}

	/**
	 * 
	 * @author yangjing
	 * @param list
	 *            参数：appId，interfaceId
	 * @return JSONObject
	 * @describe 增加应用接口
	 */
	@ResponseBody
	@RequestMapping(value = "/addAppInterface")
	public JSONObject addAppInterface(@RequestBody HashMap<String, String> map) {

		return appInterfaceService.addAppInterface(map);
	}

	/**
	 * 
	 * @author yangjing
	 * @param appId 应用id,interfaceId接口id   
	 * @return JSONObject
	 * @describe 删除应用接口
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAppInterface")
	public JSONObject deleteAppInterface(@RequestBody HashMap<String, String> map) {

		return appInterfaceService.deleteAppInterface(map);
	}

	/**
	 * 
	 * @author yangjing
	 * @param interfaceId
	 *            接口id
	 * @return JSONObject
	 * @describe 判断该接口是否有模板使用
	 */
	@ResponseBody
	@RequestMapping(value = "/isExistAppAtInterface")
	public JSONObject isExistAppAtInterface(@RequestBody HashMap<String,String> map) {

		return appInterfaceService.isExistAppAtInterface(map);
	}

}
