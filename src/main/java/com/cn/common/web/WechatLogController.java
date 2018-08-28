package com.cn.common.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.common.service.WechatLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @author yangjing
 * @date 2016年12月15日
 * @describe 微信的消息记录查询
 */
@Controller
@RequestMapping(value="/wechatLog")
public class WechatLogController {

	 Logger logger = LoggerFactory.getLogger(WechatLogController.class);

	@Resource
	WechatLogService wechatLogService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param map 
	 * 条件查询参数:（content）内容，（sysName）系统名称
	 * （state）状态（成功，失败，待发送），（addStartTime，addEndTime）提交时间，
	 * （executeStartTime，executeEndTime）执行时间
	 * @return List<HashMap<String,String>>
	 * @describe 查询所有的消息记录
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllWechatLog")
	public   PageInfo<Map> queryAllWechatLog(@RequestBody HashMap<String,String> map){
		
		
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list =wechatLogService.queryAllWechatLog(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
		return p;	
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysId 消息记录的id
	 * @return HashMap<String,String>
	 * @describe  插看消息记录的详情
	 */
	@ResponseBody
	@RequestMapping(value = "/queryDetailsWechatLog")
	public HashMap<String, String>  queryAllWechatLog(String sysId){
		
		logger.info(sysId);
		return wechatLogService.queryDetailsLog(sysId);
	}
	
}
