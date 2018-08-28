package com.cn.common.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author yangjing
 * @date 2017年3月1日
 * @describe 流量监控
 */
public interface FlowMonitoringMapper {

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
	 * @return void
	 * @describe 增加接口流量配置
	 */
	public void addFlowMonitoring(Map<String, String> map);


	/**
	 * 
	 * @author yangjing 
	 * @param map  interfaceSonType 接口子类型， systemid 系统id，amount 数量 ，state 状态 ，sysid 主键id
	 * @return void
	 * @describe 修改接口流量配置
	 */
	public void updateFlowMonitoring(Map<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param sysid  主键id
	 * @return void
	 * @describe 删除接口流量配置
	 */
	public void deleteFlowMonitoring(HashMap<String,String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param map interfaceSonType 接口子类型，systemid 系统id 
	 * @return String true 为是，false为否
	 * @describe 判断接口流量是否重复配置
	 */
	public String isRepeatSetFlowMonitoring(Map<String, String> map);

	/**
	 * 
	 * @author yangjing 
	 * @param interfaceSonType 接口的子类型
	 * @return int  
	 * @describe 查询子接口的剩余流量
	 */
	public int queryRemnantFlowMonitoring(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @return List<Map<String,String>>
	 * @describe 查询流量柱状图显示
	 */
	public List<Map<String, String>> queryFlow(HashMap<String,String> map);
}
