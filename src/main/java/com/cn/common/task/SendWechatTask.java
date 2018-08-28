package com.cn.common.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.cn.common.service.InterfaceWechatService;
import com.cn.common.util.StringUtil;


import net.sf.json.JSONObject;

/**
 * 微信定时器，每五分钟执行一次 定时扫描微信调用信息表，执行调用后没有执行的微信接口
 * 
 * @author yangjing date:2016-12-6
 */

public class SendWechatTask extends HomeTask {

	Logger logger = LoggerFactory.getLogger(SendWechatTask.class);
	// cronExpression配置说：  
	// 1）Cron表达式的格式：秒 分 时 日 月 周 年(可选)。 
	//   字段名     允许的值        允许的特殊字符            
	// 秒        0-59           , - * /            
	// 分        0-59           , - * /            
	// 时      0-23           , - * /            
	// 日       1-31            , - * ? / L W C            
	// 月        1-12 or JAN-DEC   , - * /            
	// 周几      1-7 or SUN-SAT     , - * ? / L C #            
	// 年(可选字段) empty, 1970-2099  , - * /  
	//   “?”字符：表示不确定的值 
	// “,”字符：指定数个值   
	// “-”字符：指定一个值的范围 
	//   “/”字符：指定一个值的增加幅度。n/m表示从n开始，每次增加m           
	// “L”字符：用在日表示一个月中的最后一天，用在周表示该月最后一个星期X           
	// “W”字符：指定离给定日期最近的工作日(周一到周五)           
	// “#”字符：表示该月第几个周X。6#3表示该月第3个周五          
	// 2）Cron表达式范例： 
	//   每隔5秒执行一次：*/5 * * * * ?               
	// 每隔1分钟执行一次：0 */1 * * * ? 
	//   每天23点执行一次：0 0 23 * * ?               
	// 每天凌晨1点执行一次：0 0 1 * * ?               
	// 每月1号凌晨1点执行一次：0 0 1 1 * ?               
	// 每月最后一天23点执行一次：0 0 23 L * ?               
	// 每周星期天凌晨1点实行一次：0 0 1 ? * L 
	// 在26分、29分、33分执行一次：0 26,29,33 * * * ? 
	// 每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?

	// 任务队列私有，保持数据的纯净性和安全性
	private List<Map<String, String>> wechatQueuelist;

	@Resource
	private InterfaceWechatService dao;

	/**
	 * 初始化任务队列，去掉重复项
	 * 
	 * @param mailList
	 * @return
	 */
	List<Map<String, String>> initWechatList(List<Map<String, String>> wechatList) {
		// 如果任务队列不为空，则执行以下操作
		if (null != wechatList) {
			if (!wechatList.isEmpty()) {
				// 如果任务队列为空，则直接将任务放入任务队列
				if (null == wechatQueuelist) {
					wechatQueuelist = new ArrayList<Map<String, String>>();
					wechatQueuelist.addAll(wechatList);
				} else {
					// 初始化任务队列
					wechatQueuelist.addAll(wechatList);
					// 去掉任务队列里面的重复任务
					HashSet set = new HashSet(wechatQueuelist);
					// 清空队列
					wechatQueuelist.clear();
					// 将去掉重复的数据放入到队列
					wechatQueuelist.addAll(set);
//					// 对list进行排序，按照Map中的sysid大小进行排序，小的说明任务在前，优先执行
//					ListComParator compara=new ListComParator("sysid");
//					Collections.sort(wechatQueuelist, compara);
				}
			}
		}
		return wechatQueuelist;
	}

	/**
	 * 扫描任务队列
	 * 
	 * @return 扫描的任务队列
	 */
	List<Map<String, String>> getWechatList() {
		List list = dao.selectAllWechatQueueList();
		return list;
	}

	/**
	 * 微信定时任务，每5分钟发送一次
	 */
	@Scheduled(fixedRate = 1000 * 60 * 5)
	public void sendWechatCode() {
		logger.info("进入微信定时任务.....");
		// 获取任务队列，并且初始化任务队列
		wechatQueuelist = initWechatList(getWechatList());
		// 如果没有任务，就停止任务
		if (null == wechatQueuelist)
			return;
		if (wechatQueuelist.isEmpty())
			return;
		// 如果队列不为空就进行任务发送
		// 获取邮件的配置文件信息
		Map<String, String> wechatMap = getSysParameter("wechat");
		for (int i = 0, len = wechatQueuelist.size(); i < len;) {
			// 线程池并发执行任务
			taskExecutor.execute(new SendWechatThread(wechatQueuelist.get(i), wechatMap));
			// 执行一个删除一个，因删掉当前索引会改变list结构，所以要去掉i++
			wechatQueuelist.remove(i);
			// 没删除一次任务，循环长度减1
			len--;
		}
	}

	/**
	 * 内部类，处理业务逻辑
	 * 
	 * @author chenkai
	 *
	 */
	class SendWechatThread implements Runnable {

		private Map<String, String> map;

		private Map<String, String> wechatMap;

		private SendWechatThread(Map<String, String> map, Map<String, String> wechatMap) {
			this.map = map;
			this.wechatMap = wechatMap;
		}

		@Override
		public void run() {
			// 先校验数据的合法性
			JSONObject json = new JSONObject();
			// 获取微信公众的唯一标识
			String appid = StringUtil.getString(map.get("appid"));
			// 微信公众的唯一标识秘银
			String secret = StringUtil.getString(map.get("secret"));
			// 消息内容
			String content = StringUtil.getString(map.get("content"));
			// 获取微信模版编号
			String modelcode = StringUtil.getString(map.get("modelcode"));
			// 消息发送
			if (content != null && content.trim().length() > 1000) {
				json.put("retCode", 45002);
				json.put("retMsg", getResultErrMsg("45002", "wechat"));
			}else{
				String getAppTokenUrl=wechatMap.get("APPTOKENURL");
				String sendAppMsgUrl =wechatMap.get("APPSENDURL");
				String data=wechatMap.get("APPTEXTDATA");
				data=String.format(data,content);
				json = send(appid, secret, getAppTokenUrl,sendAppMsgUrl,data, modelcode);
			}
			logger.info("微信发送完毕,发生情况为：" + json.toString());
			// 将执行记录插入到记录表
			map.put("retcode", json.getString("retCode"));
			map.put("retmsg", json.getString("retMsg"));
			map.put("messageid", StringUtil.getUuId());
			String flag = dao.insertWechatLog(map);
			if (flag.equals("0"))
				logger.info("微信发送记录初始化失败.....");
			else
				logger.info("微信发送记录初始化成功.....");
		}

		public JSONObject send(String appid, String secret,String getTokenUrl,String sendMsgUrl, String data,String modelcode) {

			JSONObject responce = new JSONObject();
//			JSONObject responceJson;
//			try {
//				//responceJson = WechatHandlerUtil.sendMessage(appid.trim(), secret.trim(),getTokenUrl,sendMsgUrl,data);
//				String errCode = responceJson.get("errcode") == null ? "" : responceJson.get("errcode").toString();
//				responce.put("retCode", errCode);
//				responce.put("retMsg", getResultErrMsg(errCode, "wechat"));
//			} catch (IOException e) {
//				logger.info(e.toString());
//			}
			return responce;
		}
	}
}
