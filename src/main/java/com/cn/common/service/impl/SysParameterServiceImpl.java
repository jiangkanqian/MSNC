package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cn.common.dao.SysParameterMapper;
import com.cn.common.service.SysParameterService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;

@Service
public class SysParameterServiceImpl implements SysParameterService {

	// private static final Logger logger =
	// LoggerFactory.getLogger(SysParameterServiceImpl.class);

	// 注入Service依赖,dao
	@Resource
	private SysParameterMapper sysParameterMapper;

	/**
	 * 获取系统配置,通过sys_type或者别的参数获取相关的数据
	 * 
	 */
	@Override
	public List<Map> findBySysTypeAll(String sysType) {
		
		return sysParameterMapper.findBySysTypeAll(sysType);
	}

	@Override
	public JSONObject addSysParameter(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			sysParameterMapper.addSysParameter(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "增加系统参数失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject deleteSysParameter(HashMap<String,String> map) {
		
		JSONObject responce = null;
		try {
			sysParameterMapper.deleteSysParameter(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "删除系统参数失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject updateSysParameter(HashMap<String, String> map) {
		
		JSONObject responce = null;
		try {
			sysParameterMapper.updateSysParameter(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "修改系统参数失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public List<Map> queryAllSysParameter(HashMap<String, String> map) {

		return sysParameterMapper.queryAllSysParameter(map);
	}

	@Override
	public JSONObject isRepeatSysName(HashMap<String, String> map) {

		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed = sysParameterMapper.isRepeatSysName(map);
		if (Msnc.TRUE.equals(isUsed)) {
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该系统参数名称已经重复");
		} else if (Msnc.FALSE.equals(isUsed)) {
			responce.put("retMsg", "该系统参数名称没有重复");
		}
		return responce;
	}

}
