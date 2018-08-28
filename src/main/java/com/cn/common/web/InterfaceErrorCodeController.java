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

import com.cn.common.service.InterfaceErrorCodeService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年02月14日
 * @describe 接口错误的回码管理
 */
@Controller
@RequestMapping(value="/manager/errorCode")
public class InterfaceErrorCodeController {
	
	 Logger logger = LoggerFactory.getLogger(InterfaceErrorCodeController.class);
	
	@Resource
	private InterfaceErrorCodeService errorCodeService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 参数: interfaceSonType接口类型，errCode 错误码
	 * @return JSONObject 
	 * @describe 查询错误回码
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllErrorCode")
	public JSONObject queryAllErrorCode(@RequestBody HashMap<String,String> map){
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = errorCodeService.queryAllErrorCode(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType接口类型，errCode 错误码，errMsg 错误描述
	 * @return JSONObject
	 * @describe 增加错误回码
	 */
	@ResponseBody
	@RequestMapping(value = "/addErrorCode")
	public JSONObject addErrorCode(@RequestBody HashMap<String,String> map){
      
		return errorCodeService.addErrorCode(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map sysid记录id,interfaceSonType接口类型，errCode 错误码，errMsg 错误描述
	 * @return JSONObject
	 * @describe 修改错误回码
	 */
	@ResponseBody
	@RequestMapping(value = "/updateErrorCode")
	public JSONObject updateErrorCode(@RequestBody HashMap<String,String> map){
      
		return errorCodeService.updateErrorCode(map);
	}
	/**
	 * 
	 * @author yangjing 
	 * @param sysid  记录id
	 * @return JSONObject
	 * @describe 删除错误回码
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteErrorCode")
	public JSONObject deleteErrorCode(@RequestBody HashMap<String,String> map){
      
		return errorCodeService.deleteErrorCode(map);
	}

	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType接口子类，errCode错误回码
	 * @return String 重复则为true 否则为false
	 * @describe 判断是否重复的设置了错误回码
	 */
	@RequestMapping(value = "/isRepeatSetErrorCode")
	@ResponseBody
	public JSONObject  isRepeatSetErrorCode(@RequestBody HashMap<String,String> map){
		
		return errorCodeService.isRepeatSetErrorCode(map);
	}
}
