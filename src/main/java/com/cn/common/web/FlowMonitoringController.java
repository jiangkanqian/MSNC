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
import com.cn.common.service.FlowMonitoringService;
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
@RequestMapping(value="/manager/flowMonitoring")
public class FlowMonitoringController {

	
	 Logger logger = LoggerFactory.getLogger(FlowMonitoringController.class);
	
	@Resource
	private FlowMonitoringService flowMonitoringService;

	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType接口子类型
	 * @return JSONObject
	 * @describe 查询所有的流量监控配置
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllFlowMonitoring")
	public JSONObject queryAllFlowMonitoring(@RequestBody HashMap<String,String> map){
		
        JSONObject responce=JsonObjectUtil.initSucceed();
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = flowMonitoringService.queryAllFlowMonitoring(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
        responce.put("data", p);
		return responce;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param interfaceSonType 接口子类型
	 * @return JSONObject
	 * @describe  查询子接口的剩余流量
	 */
	@ResponseBody
	@RequestMapping(value = "/queryRemnantFlowMonitoring")
	public JSONObject queryRemnantFlowMonitoring(@RequestBody HashMap<String,String> map){
		
		return flowMonitoringService.queryRemnantFlowMonitoring(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysid 主键id
	 * @return JSONObject
	 * @describe  删除接口流量配置
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteFlowMonitoring")
	public JSONObject deleteFlowMonitoring(@RequestBody HashMap<String,String> map){

		return flowMonitoringService.deleteFlowMonitoring(map);
	}
	

	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType 接口子类型， systemid 系统id，amount 数量 ，state 状态
	 * @return JSONObject
	 * @describe 增加接口流量配置
	 */
	@ResponseBody
	@RequestMapping(value = "/addFlowMonitoring")
	public JSONObject addFlowMonitoring(@RequestBody HashMap<String,String> map){

		return flowMonitoringService.addFlowMonitoring(map);
	}
	

	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType 接口子类型， systemid 系统id，amount 数量 ，state 状态 ，sysid 主键id
	 * @return JSONObject
	 * @describe 修改接口流量配置
	 */
	@ResponseBody
	@RequestMapping(value = "/updateFlowMonitoring")
	public JSONObject updateFlowMonitoring(@RequestBody HashMap<String,String>map){

		return flowMonitoringService.updateFlowMonitoring(map);
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType 接口子类型，systemid 系统id
	 * @return JSONObject
	 * @describe 判断接口流量是否重复配置
	 */
	@ResponseBody
	@RequestMapping(value = "/isRepeatSetFlowMonitoring")
	public JSONObject isRepeatSetFlowMonitoring(@RequestBody HashMap<String,String>map){

		return flowMonitoringService.isRepeatSetFlowMonitoring(map);
	}

	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * @return JSONObject
	 * @describe 柱状图的显示
	 */
	@ResponseBody
	@RequestMapping(value = "/queryFlow")
	public JSONObject queryFlow(@RequestBody HashMap<String,String>map){

		 JSONObject responce=JsonObjectUtil.initSucceed();
		 responce.put("data", flowMonitoringService.queryFlow(map));
		return responce;
	}
	
}
