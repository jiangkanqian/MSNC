package com.cn.common.dao;

import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月6日
 * @describe 微信接口dao
 */
public interface InterfaceWechatMapper extends BaseMapper{


	public void insertWechat(Map<String, String> map);

}
