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

import com.cn.common.service.ModelService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月27日
 * @describe 模板管理
 */
@Controller
@RequestMapping(value="/manager/model")
public class ModelController {

	
	 Logger logger = LoggerFactory.getLogger(ModelController.class);
	
	@Resource
	private ModelService modelService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  modelId模板id， modelName模板名字
	 * @return JSONObject
	 * @describe 查询所有的模板
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllModel")
	public JSONObject queryAllModel(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list =modelService.queryAllModel(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;	
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param modelId 模板id
	 * @return JSONObject
	 * @describe 删除模板
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteModel")
	public JSONObject deleteModel(@RequestBody HashMap<String,String> map){
		
		return modelService.deleteModel(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map modelName模板名字, modelId模板id, content模板内容,
 	*   modelType模板类型, state模板状态, remark模板描述
	 * @return JSONObject
	 * @describe  添加模板
	 */
	@ResponseBody
	@RequestMapping(value = "/addModel")
	public JSONObject addModel(@RequestBody HashMap<String,String> map){
		
		return modelService.addModel(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map modelName 模板名字，content模板内容， modelType 模板类型
	 * ，state 模板状态 ，remark模板描述 ，modelId模板id
	 * @returnJSONObject
	 * @describe 修改模板
	 */
	@ResponseBody
	@RequestMapping(value = "/updateModel")
	public JSONObject updateModel(@RequestBody HashMap<String,String>map){

		return modelService.updateModel(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param modelId 模板id
	 * @return JSONObject
	 * @describe 判断模板id是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatModelId")
	public JSONObject isRepeatModelId(@RequestBody HashMap<String,String>map){

		return modelService.isRepeatModelId(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param modelName 模板名字
	 * @return JSONObject
	 * @describe 判断模板名字是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatModelName")
	public JSONObject isRepeatModelName(@RequestBody HashMap<String,String>map){

		return modelService.isRepeatModelName(map);
	}
	
}
