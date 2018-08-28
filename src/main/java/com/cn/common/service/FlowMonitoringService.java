package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.util.concurrent.RateLimiter;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2017年3月1日
 * @describe 流量监控管理
 */
public interface FlowMonitoringService {


	public void  setFlowRateLimiterList();
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<Map<String,String>>
	 * @describe 查询所有的流量配置用来后台监控操作
	 */
	public List<Map<String, String>> selectAllFlowMonitoring();
	
	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType接口子类型
	 * @return List<Map<String,String>>
	 * @describe 查询所有的流量监控配置
	 */
	public List<Map> queryAllFlowMonitoring(Map<String, String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  interfaceSonType 接口子类型， systemid 系统id，amount 数量 ，state 状态
	 * @return JSONObject
	 * @describe 增加接口流量配置
	 */
	public JSONObject addFlowMonitoring(Map<String, String> map);


	/**
	 * 
	 * @author yangjing 
	 * @param map  interfaceSonType 接口子类型， systemid 系统id，amount 数量 ，state 状态 ，sysid 主键id
	 * @return JSONObject
	 * @describe 修改接口流量配置
	 */
	public JSONObject updateFlowMonitoring(Map<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param sysid  主键id
	 * @return JSONObject
	 * @describe 删除接口流量配置
	 */
	public JSONObject deleteFlowMonitoring(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType 接口子类型，systemid 系统id 
	 * @return JSONObject true 为是，false为否
	 * @describe 判断接口流量是否重复配置
	 */
	public JSONObject isRepeatSetFlowMonitoring(Map<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param interfaceSonType 接口的子类型
	 * @return JSONObject  
	 * @describe 查询子接口的剩余流量
	 */
	public JSONObject queryRemnantFlowMonitoring(HashMap<String, String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<Map<String,String>>
	 * @describe 查询流量柱状图显示
	 */
	public List<Map<String, String>> queryFlow(HashMap<String,String> map);
}
