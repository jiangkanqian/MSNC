package com.cn.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository(value="repeatTaskMapper")
public interface InterfaceRepeatSendTaskMapper {
	
    void insert(Map<String, String> map);
    
    List<Map<String,String>> getNotSucceedTask();
	
	void updateNotSuccedTask(Map<?,?> map);
}
