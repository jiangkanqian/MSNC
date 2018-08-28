package com.cn.common.queue.listener;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cn.common.service.FlowMonitoringService;
import com.cn.common.service.InterfaceErrorCodeService;
import com.cn.common.service.InterfaceService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.RedisCacheUtil;


/**
 * Servlet容器监听器，当服务器启动或者关闭的时候改监听会自动启动
 * @author chen.kai
 * date:2017-01-10
 */
public class ServletListener implements ServletContextListener {

	private Logger log=LoggerFactory.getLogger(ServletListener.class);
	
	private ServletContext context = null;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		this.context=sce.getServletContext();
		Msnc.template_path=context.getRealPath("/").concat("WEB-INF/template");
	    log.info("template_path的地址为："+Msnc.template_path);
	  //  initFlowRateLimiter(sce);
	   // initInterfaceSonType(sce);
	    initInterErrorCode(sce);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//Msnc.flow_rate_limiter=null;
		Msnc.template_path=null;
		//Msnc.err_code=null;
		this.context=null;
	}

	/**
	 * 初始化错误码
	 * @param sce
	 */
	@SuppressWarnings(value="unchecked")
	void initInterErrorCode(ServletContextEvent sce){
		//获取service
		InterfaceErrorCodeService Service = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(InterfaceErrorCodeService.class);
		RedisCacheUtil<Map<String,String>> redis = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(RedisCacheUtil.class);
		List<Map<String, String>> list = Service.selectAllErrorCode();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map=list.get(i);
			redis.setCacheObject(map.get("code"), map.get("msg"));
			//Msnc.err_code.put(map.get("code"), map.get("msg"));
			//log.info("错误码为："+map.get("code")+"---错误描述为："+map.get("msg"));
		}
		list=null;
		//log.info(SystemProperty.err_code.size()+"----------------");
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param sce 
	 * @return void
	 * @describe 初始化流量监控
	 */
	void initFlowRateLimiter(ServletContextEvent sce){
		//获取service
		FlowMonitoringService service =  WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(FlowMonitoringService.class);
		service.setFlowRateLimiterList();
	}
	/**
	 * 
	 * @author yangjing 
	 * @param sce 
	 * @return void
	 * @describe 初始化接口子类型
	 */
	@SuppressWarnings("unchecked")
	void initInterfaceSonType(ServletContextEvent sce){
		//获取service
		RedisCacheUtil<Map<String,String>> redis = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(RedisCacheUtil.class);
		InterfaceService service =  WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(InterfaceService.class);
		List<Map<String, String>> list=service.getUrlAndInterfaceSonType();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map=list.get(i);
			//redis.setCacheObject(map.get("url"),map.get("type"));
			redis.setCacheObject(map.get("url"), map.get("type"));
		}
		list=null;
	}
}
