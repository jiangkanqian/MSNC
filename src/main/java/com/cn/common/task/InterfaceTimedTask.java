package com.cn.common.task;

import java.io.IOException;
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
import com.cn.common.service.InterfaceTimedTaskService;
import com.cn.common.sys.tools.RedisLock;
import com.cn.common.util.PoolingHttpClientUtil;

/**
 * 定时接口，每5分钟执行一次扫描
 * @author chen.kai
 * date:2017-03-08
 */
@Component
public class InterfaceTimedTask extends HomeTask{

	Logger logger=LoggerFactory.getLogger(InterfaceTimedTask.class);
//	cronExpression配置说：  
//	1）Cron表达式的格式：秒 分 时 日 月 周 年(可选)。 
//	  字段名     允许的值              允许的特殊字符            
//	      秒        0-59                , - * /            
//	      分        0-59                , - * /            
//	      时        0-23                , - * /            
//	      日        1-31                , - * ? / L W C            
//	      月        1-12 or JAN-DEC     , - * /            
//	      周几       1-7 or SUN-SAT      , - * ? / L C #            
//	     年(可选字段) empty, 1970-2099    , - * /  
//	  “?”字符：表示不确定的值 
//	  “,”字符：指定数个值   
//	  “-”字符：指定一个值的范围 
//	  “/”字符：指定一个值的增加幅度。n/m表示从n开始，每次增加m           
//	  “L”字符：用在日表示一个月中的最后一天，用在周表示该月最后一个星期X           
//	  “W”字符：指定离给定日期最近的工作日(周一到周五)           
//	  “#”字符：表示该月第几个周X。6#3表示该月第3个周五          
//	  2）Cron表达式范例： 
//	  每隔5秒执行一次：*/5 * * * * ?               
//	     每隔1分钟执行一次：0 */1 * * * ? 
//	     每天23点执行一次：0 0 23 * * ?               
//	     每天凌晨1点执行一次：0 0 1 * * ?               
//	     每月1号凌晨1点执行一次：0 0 1 1 * ?               
//	     每月最后一天23点执行一次：0 0 23 L * ?               
//	     每周星期天凌晨1点实行一次：0 0 1 ? * L 
//	     在26分、29分、33分执行一次：0 26,29,33 * * * ? 
//	     每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?
	
	  @Resource
	  private RedisTemplate<Serializable, Serializable>  redisTemplate;
	  
	  @Resource
	  private InterfaceTimedTaskService timedServie;
	  //任务队列私有，保持数据的纯净性和安全性
	  private volatile List<Map<String,String>> queueList=new ArrayList<Map<String,String>>();
	  
	  //获取定时接口的任务
	  public List<Map<String,String>> findTaskList(){
		 return  timedServie.findTimedTask();
	  }
	  
	  //执行发送任务
	  public void sendTimedTask() throws IOException{
		  //将查询出来的任务添加到任务队列里面
		  queueList=findTaskList();
		  for (int i = 0,len=queueList.size(); i < len; i++) {
			Map<String,String> map=queueList.get(i);
			map.put("dotype", "3");
			PoolingHttpClientUtil.post(map.get("interfaceurl").toString(), map);
		}
	  }
	  
	  /**
	   * 邮件定时任务，每5分钟发送一次
	   */
	   @Scheduled(fixedRate =1000*60*5)   
	    public void send(){  
		   //设置提前两秒释放锁，以防程序下次请求时，锁还未释放，获取不到锁
		   RedisLock lock = new RedisLock(redisTemplate, "timedTask", 298000, 298000);
	       try {
			if(lock.lock()){
				try {
					sendTimedTask();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
	   
//	   /**
//		   * 邮件定时任务，每5分钟发送一次
//		   */
//		   @Scheduled(fixedRate =1000*60*5)   
//		    public void send(){  
//			   //设置提前两秒释放锁，以防程序下次请求时，锁还未释放，获取不到锁
//			System.out.println("-------------------------------------------");
//		   }
}
