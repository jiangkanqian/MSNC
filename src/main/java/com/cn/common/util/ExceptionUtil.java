package com.cn.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 将程序中的异常输出到日志文件中
 * @author chen.kai
 * 2017-03-02
 */
public class ExceptionUtil {
	
     public static String println(Throwable t){
    	 StringWriter stringWriter= new StringWriter();   
    	 PrintWriter writer= new PrintWriter(stringWriter);   
    	 t.printStackTrace(writer);   
    	 StringBuffer buffer= stringWriter.getBuffer();   
    	 return buffer.toString();   
     }
}
