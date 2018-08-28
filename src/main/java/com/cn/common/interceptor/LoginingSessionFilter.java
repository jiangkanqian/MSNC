package com.cn.common.interceptor;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cn.common.sys.tools.HttpIdentityWrapper;
import com.cn.common.util.DesUtils;

import net.sf.json.JSONObject;

public class LoginingSessionFilter implements Filter {

	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	Logger log=LoggerFactory.getLogger(LoginingSessionFilter.class);
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		HttpServletResponse httpResponse=(HttpServletResponse) response;
		//设置接收数据得编码，以防在编码过程中出现得乱码情况queryFlow
		String url=httpRequest.getRequestURI();
		if("/msnc/manager/logining/login".equals(url)||url.endsWith("/checkAccount")||url.endsWith("/initMenu")||url.endsWith("/queryFlow")){
			chain.doFilter(httpRequest, httpResponse);
			return ;
		}
		request.setCharacterEncoding("utf-8"); 
		log.info("请求得类型为："+request.getContentType());
		byte []body=null;
		try {
			ServletRequest requestWrapper = new HttpIdentityWrapper(httpRequest);
			request=(HttpServletRequest) requestWrapper;
			body= ((HttpIdentityWrapper) requestWrapper).getData();	
			String requestBody = new String(body);
			JSONObject data = mapper.readValue(requestBody, JSONObject.class);
			String identity = data.getString("identity");
			request.setAttribute("identity", identity);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		chain.doFilter(request, httpResponse);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}


	//HttpSession s=request.getSession();
//	Cookie[] cookies=request.getCookies();
//	String state=(String) s.getAttribute("state");
//	if(null!=state&&null!=cookies){
//		for (Cookie cookie : cookies) {
//			if("JSESSIONID".equals(cookie.getName())){
//				s.setMaxInactiveInterval(Msnc.SessionOutTime);
//				cookie.setMaxAge(Msnc.SessionOutTime);
//				response.addCookie(cookie);
//				return true;
//			}
//		}
//	}

}
