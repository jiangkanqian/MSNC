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

import com.cn.common.service.InterfaceService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年1月3日
 * @describe 接口管理
 */
@Controller
@RequestMapping(value="/manager/interfaceService")
public class InterfaceController {
	
	 Logger logger = LoggerFactory.getLogger(InterfaceController.class);
	
	@Resource
	private InterfaceService interfaceService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 参数: interfaceType 接口类型， interface_name接口名字
	 * @return JSONObject
	 * @describe 查询所有的接口
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllInterface")
	public JSONObject queryAllInterface(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = interfaceService.queryAllInterface(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	
	/**
	 *
	 * @author yangjing 
	 * @param map
	 * @return JSONObject
	 * @describe 查询所有的接口名称
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllInterfaceName")
	public JSONObject queryAllInterfaceName(){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
        List<Map<String, String>> list = interfaceService.queryAllInterfaceName();
        responce.put("data", list);
		return responce;
	}

	/**
	 * 
	 * @author yangjing 
	 * @param interfaceId 接口id
	 * @return JSONObject
	 * @describe 接口的删除
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteInterface")
	public JSONObject deleteInterface(@RequestBody HashMap<String,String> map){
		
		return interfaceService.deleteInterface(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 *  参数: interfaceName 接口名字，
		interfaceUrl 接口url,getPost 接口请求方式,interfaceType 接口类型,
		interfaceSonType 接口子类型,interfaceMsg 接口描述，callMode 接口调用实例
	 * @return JSONObject
	 * @describe 接口的增加
	 */
	@ResponseBody
	@RequestMapping(value = "/addInterface")
	public JSONObject addInterface(@RequestBody HashMap<String,String> map){
		
		return interfaceService.addInterface(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 
		 参数: interfaceName 接口名字，
		interfaceUrl 接口url,getPost 接口请求方式,interfaceType 接口类型,
		interfaceSonType 接口子类型,interfaceMsg 接口描述，callMode 接口调用实例
		，interfaceId 接口id
	 * @return JSONObject
	 * @describe 修改接口
	 */
	@ResponseBody
	@RequestMapping(value = "/updateInterface")
	public JSONObject updateInterface(@RequestBody HashMap<String,String> map){
	
		return interfaceService.updateInterface(map);
	}
	
}
