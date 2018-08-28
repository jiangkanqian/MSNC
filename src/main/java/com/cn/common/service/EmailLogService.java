package com.cn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月14日
 * @describe 邮件的消息记录查询
 */
public interface EmailLogService {

	/**
	 * 
	 * @author yangjing 
	 * @param map  条件查询参数: （To）接收人，（Cc）抄送人，（Text）文本内容，（User）用户，（sysName）系统名称
	 * （state）状态（成功，失败，待发送），（addStartTime，addEndTime）提交时间，
	 * （executeStartTime，executeEndTime）执行时间
	 * @return List<HashMap<String,String>>
	 * @describe 查询所有的邮件记录
	 */
	public List<Map> queryAllEmailLog(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param sysId 该任务记录的sysid
	 * @return HashMap<String,String>
	 * @describe 该方法只是查询邮件的异步任务的详情
	 */
	public HashMap<String,String> queryDetailsLog(String sysId);

}
