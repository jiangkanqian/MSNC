package com.cn.common.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.web.HomeController;
import com.google.common.util.concurrent.RateLimiter;

import net.sf.json.JSONObject;

public  class FlowMonitoringInterceptor extends HomeController implements HandlerInterceptor{
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	Logger log=LoggerFactory.getLogger(FlowMonitoringInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		
//		StringBuffer key = new StringBuffer();
//		String requestBody = (String) request.getAttribute("body");
//		String url=request.getRequestURI();
//		String interfaceSontype= Msnc.interface_son_type.get(url);
//		JSONObject data = mapper.readValue(requestBody, JSONObject.class);
//		key.append(interfaceSontype.trim()+"_");
//		key.append(data.get("fromsystemid").toString().trim());
//		log.info("流量监控到key："+key.toString()+Msnc.flow_rate_limiter.toString());
//		
//		RateLimiter limiter=Msnc.flow_rate_limiter.get(key.toString());
//		if(null!=limiter&&!limiter.tryAcquire()){
//			//初始化返回值码和返回描述
//			JSONObject json=JsonObjectUtil.initErrorJson();
//			json.put("retMsg", "接口调用超过限制");
//			response.getWriter().write(json.toString());
//			System.out.println("进来了。");
//			return false;
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		
	}
//	JSONObject map = new JSONObject();
//	String requestBody = (String) request.getAttribute("body");
//	String url=request.getRequestURI();
//	String interfaceSontype= getInterfaceSonType(url);
//	JSONObject data = mapper.readValue(requestBody, JSONObject.class);
//	map.put("systemid", data.get("fromsystemid"));
//	map.put("interfaceSontype", interfaceSontype);
//	setTime(map);
//	log.info("流量监控内容："+map.toString());
//	int acount=flowMonitoring(map);
//	if(acount>0){
//		//初始化返回值码和返回描述
//		JSONObject json=JsonObjectUtil.initErrorJson();
//		json.put("retMsg", "接口调用超过限制");
//		//初始化执行记录
//        data=initTaskLog(data, json, interfaceSontype, "1", url,"");
//        insertResultQueue(data);
//        response.getWriter().write(json.toString());
//        return false;
//	}

}
