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

import com.cn.common.service.EmailLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @author yangjing
 * @date 2016年12月14日
 * @describe 邮件的消息记录查询
 */
@Controller
@RequestMapping(value="/emailLog")
public class EmailLogController {

	private static final Logger logger = LoggerFactory.getLogger(EmailLogController.class);

	@Resource
	EmailLogService emailLogService;
	
	/**
	 * 
	 * @author yangjing 
	 * @param map  条件查询参数: （To）接收人，（Cc）抄送人，（Text）文本内容，（User）用户，（sysName）系统名称
	 * （state）状态（成功，失败，待发送），（addStartTime，addEndTime）提交时间，
	 * （executeStartTime，executeEndTime）执行时间
	 * @return List<HashMap<String,String>>
	 * @describe 查询所有的邮件记录
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllEmailLog")
	public  PageInfo<Map>  queryAllEmailLog(@RequestBody HashMap<String,String> map){
		
		Integer page=map.get("page")==null?1:Integer.parseInt(map.get("page"));
		Integer pageSize=map.get("pageSize")==null?10:Integer.parseInt(map.get("pageSize"));
        PageHelper.startPage(page, pageSize);
        List<Map> list = emailLogService.queryAllEmailLog(map);
        //初始化分页对象的数据，包括总条数，上一页，下一页等等
        PageInfo<Map> p=new PageInfo<Map>(list);
		return p;
	}
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysId 该任务记录的sysid
	 * @return HashMap<String,String>
	 * @describe 该方法只是查询邮件的异步任务的详情
	 */
	@ResponseBody
	@RequestMapping(value = "/queryDetailsEmailLog")
	public HashMap<String, String>  queryDetailsEmailLog(String sysId){
		
		logger.info(sysId);
		return emailLogService.queryDetailsLog(sysId);
	}
	
}
