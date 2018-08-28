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

import com.cn.common.service.InterfaceTaskLogService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年1月17日
 * @describe  消息记录查询
 */
@Controller
public class InterfaceTaskLogController {

	
	 Logger logger = LoggerFactory.getLogger(InterfaceTaskLogController.class);
	
	@Resource
	private InterfaceTaskLogService interfaceTaskLogService;

	/**
	 * 
	 * @author yangjing 
	 * @param map 条件的参数
	 * @return JSONObject 分页的结果
	 * @describe
	 */
	@ResponseBody
	@RequestMapping(value = "/manager/taskLog/queryTaskLog")
	public JSONObject queryTaskLog(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = interfaceTaskLogService.queryTaskLog(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 条件的参数
	 * @return  JSONObject
	 * @describe
	 */
	@ResponseBody
	@RequestMapping(value = "/manager/taskLog/taskLog")
	public JSONObject taskLog(@RequestBody HashMap<String,String> map){

		JSONObject responce = JsonObjectUtil.initSucceed();
        responce.put("data", interfaceTaskLogService.queryTaskLog(map));
		return responce;
	}
	@ResponseBody
	@RequestMapping(value = "/interface/taskLog/queryLog", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
	"application/json; charset=utf-8" })
	public List<Map> queryLog(@RequestBody HashMap<String,String> map){
		
        List<Map> list = interfaceTaskLogService.queryLog(map);
		return list;
	}

}
