package com.cn.common.util;

import java.util.UUID;
/**
 * 获取随机的uuid
 * @author chenkai
 * date:2016-12-1
 */
public final class StringUtil {
	

	
	public static String getUuId(){
		return UUID.randomUUID().toString().replaceAll("-",""); 
	}
	
	public static String getString(Object object){
		return object==null?"":object.toString().trim();
	}
	/**
	 * 将数字字符转为int类型 ，当str==null或者str.equals("") 返回0
	 * @param str
	 * @return
	 */
	public static int parseInt(String str){
		return (str==null||str.equals(""))?0:Integer.parseInt(str);
	}
	
	public static int objectParseInt(Object object){
		String str=getString(object);
		return str.equals("")?0:Integer.parseInt(str);
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		return (null==str||"".equals(str))?true:false;
	}
}
