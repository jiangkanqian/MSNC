package com.cn.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年11月11日
 * @describe 缓存调用微信接口获取的唯一凭证（access_token）,凭证用于公众号消息群发。
 */
public enum AccessTokenMapUtil {
	
	INSTANCE;
	private static HashMap<String,Map<String,Object>> accessTokenMap=new HashMap<String,Map<String,Object>>();
	
	public static HashMap<String, Map<String, Object>> getMap(){
		
		return accessTokenMap;
	}
	
}
