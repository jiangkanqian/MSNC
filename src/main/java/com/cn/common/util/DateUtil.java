package com.cn.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间帮助公共类
 * @author chen.kai
 * date:2016-12-16
 */
public class DateUtil {

	public static final String yyyyMMdd="yyyyMMdd";
	
	public static final String yyyy_MM_dd="yyyy_MM_dd";
	
    public static final String yyyyMMdd_="yyyy-MM-dd";
    
    public static final String yyyyMMdd_1="yyyy/MM/dd";
	
	public static final String yyyyMMddHHmmss="yyyyMMdd HH:mm:ss";
		
	public static final String yyyy_MM_ddHHmmss="yyyy_MM_dd HH:mm:ss";
	
	public static final String yyyyMMddHHmmss_="yyyy-MM-dd HH:mm:ss";
	
	public static final String yyyyMMddHHmmss_1="yyyy/MM/dd HH:mm:ss";
	
	
	/**
	 * 返回格式化后的所传日期或者当前日期
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date,String format){
		if(null==date)date=new Date();
		return new SimpleDateFormat(format).format(date);
	}
	
	public static void main(String[] args) {
		String myFileName="aaa.txt";
		String aa=myFileName.substring(myFileName.lastIndexOf("."), myFileName.length());
		System.out.println(aa);
	}
}
