package com.cn.common.task;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

import javax.annotation.Resource;
import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.redis.core.RedisTemplate;

import com.cn.common.service.InterfaceErrorCodeService;
import com.cn.common.service.SysParameterService;



/**
 * 定时器帮助类
 * @author chenkai
 * date:2016-12-5
 */
public class HomeTask {

	  @Resource
	  private SysParameterService service;
	  
	  @Resource
	  private InterfaceErrorCodeService InterfaceErrorCodeService;
	  
	  @Resource
	  TaskExecutor taskExecutor;
	  
	  /**
		 * 获取各个接口的系统配置参数
		 * @param sysType 接口参数类型
		 * @return 返回配置信息的所有数据集合
		 */
	 Map<String,String> getSysParameter(String sysType){
			//声明一个Map
			Map<String,String> map=new HashMap<String,String>();
			//获取手机短信的配置文件
			List<Map> list=service.findBySysTypeAll(sysType);
			//取出其配置信息
			for(int i = 0, len = list.size() ; i < len; i++){
				map.put(list.get(i).get("sysname").toString(), list.get(i).get("sysvalue").toString());
			}
			return map;
		}
	 
	 /**
		 * 通过错误编码和接口子类型，获取错误描述
		 * @param errCode 错误编码
		 * @param interfaceSonType 接口子类型
		 * @return 错误码代码的错误描述
		 */
		String getResultErrMsg(String errCode,String interfaceSonType){
			//查找errcode的详细描述sql的parameter
			Map<String, String> paramMap= new HashMap<String, String>();
			//调用接口的回码
			paramMap.put("errcode",errCode);
			//接口的子类型
			paramMap.put("interfaceSonType", interfaceSonType);
			Map<String, Object> errMap = InterfaceErrorCodeService.findErrMsgByInterfaceSonTypeAndErrcode(paramMap);
			return errMap==null?"":errMap.get("errmsg").toString();
		}	
   
}
