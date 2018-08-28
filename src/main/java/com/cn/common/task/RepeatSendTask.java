package com.cn.common.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cn.common.service.InterfaceRepeatSendTaskService;
import com.cn.common.sys.tools.RedisLock;

/**
 * 消息重发定时器,每3分钟扫描一次
 * @author chen.kai
 * date:2017-02-28
 */
@Component
public class RepeatSendTask{

	Logger logger=LoggerFactory.getLogger(RepeatSendTask.class);
	
	@Resource
	InterfaceRepeatSendTaskService sendTaskService;
	
	@Resource
	private RedisTemplate<Serializable, Serializable>  redisTemplate;
	
	 //任务队列私有，保持数据的纯净性和安全性
	private volatile List<Map<String, String>> queueList=new ArrayList<Map<String,String>>();
	
	//获取要重复执行的任务
	public List<Map<String, String>> getNotSucceedTask(){
		return sendTaskService.getNotSucceedTask();
	}
	/**
	 * 将数据插入到队列
	 * @param map
	 */
	public void insertQueue(Map<?,?> map){
		sendTaskService.insertRepeatSendQueues(map);
	}
	
	//执行定时扫描
	@Scheduled(fixedRate =1000*60*3)
	void repeatSendMessage(){
	//设置提前两秒释放锁，以防程序下次请求时，锁还未释放，获取不到锁
	RedisLock lock = new RedisLock(redisTemplate, "repeatTask", 178000, 178000);
	       try {
			if(lock.lock()){
				logger.info("进入消息任务重新发送定时器.....");
				queueList=getNotSucceedTask();
				if(null!=queueList){
					if(queueList.size()>0){
						for (int j = 0,len=queueList.size(); j < len; j++)
							insertQueue(queueList.get(j));	
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//清空队列，进行下一轮的任务
			queueList.clear();
			lock.unlock();
		}      
	}
}
