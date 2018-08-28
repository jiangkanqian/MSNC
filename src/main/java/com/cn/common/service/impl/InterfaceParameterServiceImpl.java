package com.cn.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.cn.common.dao.InterfaceParameterMapper;
import com.cn.common.service.InterfaceParameterService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.JsonObjectUtil;

import net.sf.json.JSONObject;


/**
 * 
 * @author yangjing
 * @date 2017年1月5日
 * @describe 接口参数管理
 */
@Service
public class InterfaceParameterServiceImpl implements InterfaceParameterService{

	@Resource
	InterfaceParameterMapper interfaceParameterMapper;

	@Override
	public  List<Map> queryAllInterfaceParameter(HashMap<String, String> map) {
		return interfaceParameterMapper.queryAllInterfaceParameter(map);
	}

	@Override
	public JSONObject addInterfaceParameter(HashMap<String, String> map) {
		
		JSONObject responce = null;
		 try {
			 interfaceParameterMapper.addInterfaceParameter(map);	
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "增加接口参数失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject updateInterfaceParameter(HashMap<String, String> map) {
		
		JSONObject responce = null;
		 try {
			 interfaceParameterMapper.updateInterfaceParameter(map);	
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "修改接口参数失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject deleteInterfaceParameter(HashMap<String, String> map) {
		
		JSONObject responce =  null;
		 try {
			 interfaceParameterMapper.deleteInterfaceParameter(map);
		} catch (Exception e) {
			e.printStackTrace();
			responce=JsonObjectUtil.initError();
			responce.put("retMsg", "删除接口参数失败！");
			return responce;
		}
		return JsonObjectUtil.initSucceed();
	}

	@Override
	public JSONObject isRepeatParameterName(HashMap<String, String> map) {
		
		JSONObject responce = JsonObjectUtil.initSucceed();
		String isUsed=interfaceParameterMapper.isRepeatParameterName(map);
		if(Msnc.TRUE.equals(isUsed)){
			responce = JsonObjectUtil.initError();
			responce.put("retMsg", "该参数名字已经重复");
		}else if(Msnc.FALSE.equals(isUsed)){
			responce.put("retMsg", "改参数名字没有重复");
		}
		return responce;
	}

	@Override
	public List<HashMap<String, String>> queryInterfaceParametersBySysId(HashMap<String, String> map) {
		return interfaceParameterMapper.queryInterfaceParametersBySysId(map);
	}

}
