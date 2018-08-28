package com.cn.common.sys.tools;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 重写servletRequestWrapper，获取http请求的POST报文
 * @author chen.kai
 * Date: 2016-11-28
 */
public class HttpIdentityWrapper extends HttpServletRequestWrapper {
	
	Logger log=LoggerFactory.getLogger(this.getClass());

    private final byte[] body;
    
    private  byte[] data;

    public HttpIdentityWrapper(HttpServletRequest request) throws IOException,Exception {
        super(request);
        data=HttpHelper.getBodyString(request,0).getBytes(Charset.forName("UTF-8"));
        body = data;
        log.info("获取报文："+new String(body,"utf-8"));
    }
    public byte[] getData(){
    	
		return data;
    }
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

        };
    }

}