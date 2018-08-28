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

import com.cn.common.service.SysParameterService;
import com.cn.common.util.JsonObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年12月26日
 * @describe 系统参数管理
 */
@Controller
@RequestMapping(value="/manager/sysParameter")
public class SysParameterController {

	
	 Logger logger = LoggerFactory.getLogger(SysParameterController.class);
	
	@Resource
	private SysParameterService sysParameterService;


	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 参数：
	 * 		sysName 参数名字,
			sysType 参数类型,
			sysValue 参数值,
			sysMsg 参数描述
	 * @return JSONObject
	 * @describe 增加系统参数
	 */
	@ResponseBody
	@RequestMapping(value = "/addSysParameter")
	public JSONObject addSysParameter(@RequestBody HashMap<String,String> map){
		
		return sysParameterService.addSysParameter(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid  参数主键
	 * @return JSONObject
	 * @describe 删除系统参数
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteSysParameter")
	public JSONObject deleteSysParameter(@RequestBody HashMap<String,String> map){
		
		return sysParameterService.deleteSysParameter(map);
	}
	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 	  参数：
	  		sysName 参数名字,
			sysType 参数类型,
			sysValue 参数值,
			sysMsg 参数描述，
	        sysid 参数主键
	 * @return JSONObject
	 * @describe 修改系统参数
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSysParameter")
	public JSONObject updateSysParameter(@RequestBody HashMap<String,String>  map){
		
		return sysParameterService.updateSysParameter(map);
	}
	/**
	 * 
	 * @author yangjing 
	 * @param map  
	 *  参数：
	 *  sysType 参数类型
	 * @return JSONObject
	 * @describe 查询所有的系统参数
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllSysParameter")
	public  JSONObject queryAllSysParameter (@RequestBody HashMap<String,String> map){
		
		JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list =sysParameterService.queryAllSysParameter(map);
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
	 * sysName 参数名字，
	 * sysType 参数类型
	 * @return JSONObject
	 * @describe 判断系统参数名字是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatSysName")
	public JSONObject isRepeatSysName (@RequestBody HashMap<String,String> map){
		
		return sysParameterService.isRepeatSysName(map);
	}

}
