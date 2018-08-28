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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.common.sys.tools.HttpWrapper;
 /**
  * http POST请求报文拦截器，只拦截/manager/*路径的url,具体配置请看web.xml
  * @author yangjing
  * date:2017-03-10
  */
public class HttpFilter implements Filter {
	
	Logger log=LoggerFactory.getLogger(HttpFilter.class);
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	    log.info("进入过滤器，过滤加密得json数据....");    
		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			//设置接收数据得编码，以防在编码过程中出现得乱码情况
			request.setCharacterEncoding("utf-8"); 
			log.info("请求得类型为："+httpServletRequest.getContentType());
			//获取post请求得报文，进行解码并且再输出出去
			if ("POST".equals(httpServletRequest.getMethod().toUpperCase())) {
				try {
					requestWrapper = new HttpWrapper((HttpServletRequest) request);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//设置发送数据头部为json，编码为utf-8
			HttpServletResponse responses = (HttpServletResponse) response;
			response.setCharacterEncoding("utf-8");
			responses.setContentType("application/json;charset=utf-8");
			if (requestWrapper == null) {
				chain.doFilter(request, responses);
			} else {
				chain.doFilter(requestWrapper, responses); 
			}
		}
	
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
       
	}

}
