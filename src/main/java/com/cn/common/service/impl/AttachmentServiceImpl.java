package com.cn.common.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.common.dao.AttachmentMapper;
import com.cn.common.service.AttachmentService;

/**
 * 附件接口表实现类
 * @author chenkai
 * date:2016-12-0-19
 */

@Service
public class AttachmentServiceImpl implements AttachmentService{

	@Resource
	private AttachmentMapper mapper;
	
	@Override
	public String insert(Map<String, String> map) {
		// TODO Auto-generated method stub
		try {
			mapper.insert(map);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "0";
	}

}
