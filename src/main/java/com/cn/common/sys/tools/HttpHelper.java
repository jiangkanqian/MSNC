package com.cn.common.sys.tools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.servlet.ServletRequest;
import org.codehaus.jackson.map.ObjectMapper;

import com.cn.common.util.DesUtils;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 获取http请求Post报文帮助类，因为POST的报文是一次性流，
 * 所以将数据读取出来，解密之后要再发送出去
 * @author chen.kai
 * date:2016-11-28
 */


public class HttpHelper{
	
	private static final ObjectMapper mapper=new ObjectMapper();
	 /**
	  * 用buffer缓冲流读取数据，提升读写性能
      * 获取请求Body
      * @param request
      * @return
      */
    public static String getBodyString(ServletRequest request) throws Exception{
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //将报文解密
        String buffer=StringUtil.getString(DesUtils.decode(sb.toString()));
        //将解密后的数据重新封装为json字符串
        JSONObject json= mapper.readValue(buffer, JSONObject.class);
        return json.toString();
    }
    public static String getBodyString(ServletRequest request, int i) throws Exception{  
    	
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
        	inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                	inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String buffer=sb.toString();
        JSONObject json= mapper.readValue(buffer, JSONObject.class);
        return json.toString();
    }
}