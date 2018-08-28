package com.cn.common.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.FlowMonitoringMapper;
import com.cn.common.service.FlowMonitoringService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.RedisCacheUtil;
import com.google.common.util.concurrent.RateLimiter;

import net.sf.json.JSONObject;


/**
 * 
 * @author yangjing
 * @date 2017年3月1日
 * @describe 流量监控
 */
@Service
public class FlowMonitoringServiceImpl implements FlowMonitoringService{
	
	@Resource
	FlowMonitoringMapper flowMonitoringMapper;
	
	@Resource 
	RedisCacheUtil<?> util;
	
	@Override
	public List<Map<String, String>> selectAllFlowMonitoring() {
		
		return flowMonitoringMapper.selectAllFlowMonitoring();
	}


	@Override
	public void setFlowRateLimiterList() {
		
		List<Map<String, String>> list = flowMonitoringMapper.selectAllFlowMonitoring();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map=list.get(i);
			String unit=map.get("unit");
			Object amount=map.get("amount");
			double permitsPerSecond=getPermitsPerSecond(unit,amount.toString().trim());
			util.setCacheObject(map.get("type"), RateLimiter.create(permitsPerSecond));
		}
		list=null;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param unit
	 * @param amount 数量
	 * @return double 速率
	 * @describe 换算以秒为单位的速率
	 */
	double getPermitsPerSecond(String unit,String amount){
		
		BigDecimal bigDecimal =null;
		BigDecimal count=new BigDecimal(amount);
		Double permitsPerSecond=0.0;
		if("h".equals(unit)){
			bigDecimal=new BigDecimal(Double.toString(60*60));
			permitsPerSecond=count.divide(bigDecimal,30,BigDecimal.ROUND_HALF_EVEN).doubleValue();
		}else if("min".equals(unit)){
			bigDecimal=new BigDecimal(Double.toString(60));
			permitsPerSecond=count.divide(bigDecimal,30,BigDecimal.ROUND_HALF_EVEN).doubleValue();
		}else if("day".equals(unit)){
			bigDecimal=new BigDecimal(Double.toString(60*60*24));
			permitsPerSecond=count.divide(bigDecimal,30,BigDecimal.ROUND_HALF_EVEN).doubleValue();
		}else if("month".equals(unit)){
			bigDecimal=new BigDecimal(Double.toString(60*60*24*getCurrentMonthLastDay()));
			permitsPerSecond=count.divide(bigDecimal,30,BigDecimal.ROUND_HALF_EVEN).doubleValue();
		}else if("s".equals(unit)){
			return Double.parseDouble(amount) ;
		}
		bigDecimal=null;
		count=null;
		return permitsPerSecond;
	}
	
	/** 
	 * 取得当月天数 
	 * */  
	public  int getCurrentMonthLastDay()  {  
	    Calendar a = Calendar.getInstance();  
	    a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
	    a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
	    int maxDate = a.get(Calendar.DATE);  
	    return maxDate;  
	}


	@Override
	public List<Map> queryAllFlowMonitoring(Map<String, String> map) {
		
		return flowMonitoringMapper.queryAllFlowMonitoring(map);
	}


	@Override
	public JSONObject addFlowMonitoring(Map<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			flowMonitoringMapper.addFlowMonitoring(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加接口配置失败！");
			return responce;
		}
		return responce;
	}


	@Override
	public JSONObject updateFlowMonitoring(Map<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			flowMonitoringMapper.updateFlowMonitoring(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改接口配置失败！");
			return responce;
		}
		return responce;
	}


	@Override
	public JSONObject deleteFlowMonitoring(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		try {
			flowMonitoringMapper.deleteFlowMonitoring(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除接口配置失败！");
			return responce;
		}
		return responce;
	}


	@Override
	public JSONObject isRepeatSetFlowMonitoring(Map<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=flowMonitoringMapper.isRepeatSetFlowMonitoring(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该接口流量配置已设置");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "该接口流量配置没有设置");
		}
		return responce;
	}


	@Override
	public JSONObject queryRemnantFlowMonitoring(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		Integer amount=null;
		try {
			amount=flowMonitoringMapper.queryRemnantFlowMonitoring(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "查询剩余接口流量失败");
			return responce;
		}
		responce.put("data", amount);
		return responce;
	}


	@Override
	public List<Map<String, String>> queryFlow(HashMap<String,String>map) {
		
		return flowMonitoringMapper.queryFlow(map);
	}
}
