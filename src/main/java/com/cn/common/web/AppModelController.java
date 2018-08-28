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

import com.cn.common.service.AppModelService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;


/**
 * 
 * @author yangjing
 * @date 2016年12月28日
 * @describe 应用模板权限管理
 */
@Controller
@RequestMapping(value="/manager/appModel")
public class AppModelController {

	
	 Logger logger = LoggerFactory.getLogger(AppModelController.class);
	
	@Resource
	private AppModelService appModelService;
	
	
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的应用以及该应用拥有的模板数量
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllAppAndModelAmount")
	public  JSONObject queryAllAppAndModelAmount(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = appModelService.queryAllAppAndModelAmount();
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return JSONObject
	 * @describe 查询该应用没有的模板
	 */
	@ResponseBody
	@RequestMapping(value = "/queryModelNotInApp")
	public JSONObject queryModelNotInApp(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		 responce.put("data", appModelService.queryModelNotInApp(map));
		return responce;
	}
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return JSONObject
	 * @describe 查询该应用拥有的模板
	 */
	@ResponseBody
	@RequestMapping(value = "/queryModelInApp")
	public JSONObject queryModelInApp(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
	    responce.put("data", appModelService.queryModelInApp(map));
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param list appId，modelId,其中这个appId是同一个。格式如下：

				[{"appId":"1","modelId":"2"},
				{"appId":"1","modelId ":"3"},
				{"appId":"1"," modelId ":"8"}]

	 * @return JSONObject
	 * @describe 增加应用模板
	 */ 
	@ResponseBody
	@RequestMapping(value = "/addAllAppModel")
	public JSONObject addAllAppModel(@RequestBody HashMap<String,List<HashMap<String, String>>> map){
		List<HashMap<String,String>> list= map.get("data");
		Object appId = map.get("appId");
		JSONObject responce=JsonObjectUtil.initSucceed();
		try {
			appModelService.addAllAppModel(list,appId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加该应用的模板权限失败！");
		}
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id
	 * @return JSONObject
	 * @describe 增加应用模板
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAllAppModel")
	public JSONObject deleteAllAppModel(@RequestBody HashMap<String,String> map){
		
		return appModelService.deleteAllAppModel(map);
	}
	
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id,modelId模板id
	 * @return JSONObject
	 * @describe 增加单个应用模板
	 */
	@ResponseBody 
	@RequestMapping(value = "/addAppModel")
	public JSONObject addAppModel(@RequestBody HashMap<String, String> map){
		
		return appModelService.addAppModel(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param appId 应用id,modelId模板id
	 * @return JSONObject
	 * @describe 删除单个应用模板
	 */ 
	@ResponseBody
	@RequestMapping(value = "/deleteAppModel")
	public JSONObject deleteAppModel(@RequestBody HashMap<String, String> map){
		
		return appModelService.deleteAppModel(map);
	}
	/**
	 * 
	 * @author yangjing 
	 * @param modelId 模板id
	 * @return JSONObject
	 * @describe 判断该模板是否有应用使用
	 */
	@ResponseBody
	@RequestMapping(value = "/isExistAppAtModel")
	public JSONObject isExistAppAtModel(@RequestBody HashMap<String,String> map){
		
		return appModelService.isExistAppAtModel(map);
	}

}
