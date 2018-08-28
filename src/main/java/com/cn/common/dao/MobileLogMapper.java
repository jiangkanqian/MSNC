package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月13日
 * @describe 短信的消息记录查询
 */
public interface MobileLogMapper {
	
	/**
	 * 
	 * @author yangjing 
	 * @param map
	 * 条件查询参数: （Phone）号码，（Context）内容，（sysName）系统名称
	 *	（state）状态（成功，失败，待发送），
	 *	（addStartTime，addEndTime）提交时间，
	 *	（executeStartTime，executeEndTime）执行时间
	 * @return List<HashMap<String,String>>
	 * @describe  查询所有的短信记录
	 */
	public List<Map> queryAllMobileLog(HashMap<String,String> map);
	/**
	 * 
	 * @author yangjing 
	 * @param sysId  短信任务的id
	 * @return HashMap<String,String>
	 * @describe 查询短信任务的详情
	 */
	public HashMap<String,String> queryDetailsLog(HashMap<String,String> map);
}
