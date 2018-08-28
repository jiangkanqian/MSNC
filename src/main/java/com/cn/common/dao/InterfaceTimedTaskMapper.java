package com.cn.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository(value="taskMapper")
public interface InterfaceTimedTaskMapper {

	List<Map<String, String>> findTimedTask();
}
