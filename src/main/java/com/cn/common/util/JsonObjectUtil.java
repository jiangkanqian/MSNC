package com.cn.common.util;

import net.sf.json.JSONObject;

/**
 * 初始化json的返回数据
 * @author chenkai
 *
 */
public class JsonObjectUtil {

	
	public static JSONObject initSucceedJson(JSONObject json){
		String retcode="1";
		String retMsg="请求成功！";
		json.put("retCode", retcode);
		json.put("retMsg", retMsg);
		return json;
	}
	
	public static JSONObject initErrorJson(){
		JSONObject json=new JSONObject();
		String retcode="0";
		String retMsg="参数为空，请求失败！";
		json.put("retCode", retcode);
		json.put("retMsg", retMsg);
		return json;
	}
	
	public static JSONObject initSucceed(){
		
		JSONObject json=new JSONObject();
		String retcode="1";
		String retMsg="请求成功！";
		json.put("retCode", retcode);
		json.put("retMsg", retMsg);
		return json;
	}
	public static JSONObject initError(){
		
		JSONObject json=new JSONObject();
		String retcode="0";
		String retMsg="请求失败！";
		json.put("retCode", retcode);
		json.put("retMsg", retMsg);
		return json;
	}
}
