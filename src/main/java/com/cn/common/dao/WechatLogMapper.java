package com.cn.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangjing
 * @date 2016年12月15日
 * @describe 微信的消息记录查询
 */
public interface WechatLogMapper {
	
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
	public List<Map> queryAllWechatLog(HashMap<String,String> map);
	
	/**
	 * 
	 * @author yangjing 
	 * @param sysId 消息记录的id
	 * @return HashMap<String,String>
	 * @describe  插看消息记录的详情
	 */
	public HashMap<String,String> queryDetailsLog(String sysId);
}
