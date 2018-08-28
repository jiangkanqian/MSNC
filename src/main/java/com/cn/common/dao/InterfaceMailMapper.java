package com.cn.common.dao;

import java.util.Map;

/**
 * 邮件接口任务Dao类
 * @author chenkai
 * date:2016-12-1
 */
public interface InterfaceMailMapper extends BaseMapper{
	
	public void insertInterfaceMail(Map<String, String> map);

}
