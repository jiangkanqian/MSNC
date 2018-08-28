package com.cn.common.interceptor;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cn.common.util.RedisCacheUtil;

public class LoginingSessionInterceptor implements HandlerInterceptor {
	Logger log = LoggerFactory.getLogger(LoginingSessionInterceptor.class);

	private String identity;

	@Resource
	RedisCacheUtil<?> util;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 设置接收数据得编码，以防在编码过程中出现得乱码情况
		String url = request.getRequestURI();
		if ("/msnc/manager/logining/login".equals(url) || url.endsWith("/checkAccount") || url.endsWith("/initMenu")||url.endsWith("/queryFlow")) {
			return true;
		}
		request.setCharacterEncoding("utf-8");
		identity = (String) request.getAttribute("identity");
		Object object=util.getCacheObject(identity);
		if (object == null ) {
			response.getWriter().write("{\"retCode\":\"-1\"}");
			return false;
		}
		Cookie data = new Cookie("data", object.toString());
		response.addCookie(data);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
