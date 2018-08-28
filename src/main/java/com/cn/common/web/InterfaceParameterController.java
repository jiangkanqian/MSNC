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
import com.cn.common.service.InterfaceParameterService;
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
@RequestMapping(value="/manager/interfaceParameter")
public class InterfaceParameterController {

	
	 Logger logger = LoggerFactory.getLogger(InterfaceParameterController.class);
	
	@Resource
	private InterfaceParameterService interfaceParameterService;
	
	/**
	 * 
	 * @author yangjing 
	 * @return JSONObject
	 * @describe 查询所有的接口参数
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllInterfaceParameter")
	public JSONObject queryAllInterfaceParameter(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = interfaceParameterService.queryAllInterfaceParameter(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;		
	}

	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 参数：
	 * 	parameterName 参数名字,
		dataType 参数数据类型,
		parameterType 参数类型（0.输入1.输出2.输入/输出）,
		isMust 是否必须（0.否，1.是）,
		parameterMsg 参数描述,
		interfaceId 接口id
	 * @return JSONObject
	 * @describe 增加接口参数
	 */
	@ResponseBody
	@RequestMapping(value = "/addInterfaceParameter")
	public JSONObject addInterfaceParameter(@RequestBody HashMap<String, String> map){
		
		return interfaceParameterService.addInterfaceParameter(map);
	}

	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 参数：
	 * 	parameterName 参数名字,
		dataType 参数数据类型,
		parameterType 参数类型（0.输入1.输出2.输入/输出）,
		isMust 是否必须（0.否，1.是）,
		parameterMsg 参数描述,
		interfaceId 接口id 
	 * parameterId 接口参数id
	 * @return JSONObject
	 * @describe 修改接口参数
	 */
	@ResponseBody
	@RequestMapping(value = "/updateInterfaceParameter")
	public JSONObject updateInterfaceParameter(@RequestBody HashMap<String, String> map){
		
		return interfaceParameterService.updateInterfaceParameter(map);
	}

	/**
	 * 
	 * @author yangjing 
	 * @param parameterId  接口参数id
	 * @return JSONObject
	 * @describe 删除接口参数
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteInterfaceParameter")
	public JSONObject deleteInterfaceParameter(@RequestBody HashMap<String,String> map){
		
		return interfaceParameterService.deleteInterfaceParameter(map);
	}

	/**
	 * 
	 * @author yangjing 
	 * @param map 参数：interfaceId 接口id, parameterName 参数名字
	 * @return JSONObject
	 * @describe 判读该接口参数名字是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatParameterName")
	public JSONObject isRepeatParameterName(@RequestBody HashMap<String, String> map){
		
		return interfaceParameterService.isRepeatParameterName(map);
	}

	/**
	 * 
	 * @author yangjing 
	 * @param interfaceId 接口id
	 * @return JSONObject
	 * @describe 查询该接口的参数
	 */
	@ResponseBody
	@RequestMapping(value = "/queryInterfaceParametersBySysId")
	public JSONObject queryInterfaceParametersBySysId(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		responce.put("data", interfaceParameterService.queryInterfaceParametersBySysId(map));
		return responce;
	}
	
}
