package com.cn.common.web;

import java.util.HashMap;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.common.service.DictionaryService;
import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年3月8日
 * @describe 业务字典
 */
@Controller
@RequestMapping(value="/manager/dictionary")
public class DictionaryController {

	
	 Logger logger = LoggerFactory.getLogger(DictionaryController.class);
	
	@Resource
	private DictionaryService dictionaryService;

	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * @return JSONObject
	 * @describe 查询所有的接口子类型
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllInterfaceSonType")
	public JSONObject queryAllInterfaceSonType(@RequestBody HashMap<String,String> map){
		return dictionaryService.queryAllInterfaceSonType(map);
	}
	
}
