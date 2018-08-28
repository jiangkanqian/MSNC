package com.cn.common.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cn.common.dao.InterfaceMailMapper;
import com.cn.common.service.InterfaceMailService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.ExceptionUtil;
import com.cn.common.util.StringUtil;
import com.sun.mail.smtp.SMTPAddressFailedException;

import net.sf.json.JSONObject;
/**
 * 邮件接口任务Service实现类
 * @author chenkai
 * date:2016-12-1
 */
@Service
public class InterfaceMailServiceImpl extends HomeServiceImpl implements InterfaceMailService{
	
	private  final Logger logger=   LoggerFactory.getLogger(InterfaceMailServiceImpl.class);
	@Resource
	private InterfaceMailMapper mapper;
	   
	//交换器名称，必须和spring-amqp.xml里面的name="topicExchange"一致，否则会找不到交换器
	private  final String exchange_name="topicExchange";
		
	//获取未执行的任务
	@Override
	public List<Map<String,String>> selectByNameOject() {
		// TODO Auto-generated method stub
		return mapper.selectByParameterObject(null);
	}

	//插入执行记录
	@Transactional
	@Override
	public String insertInterfaceMailLog(Map map) {
		try {
		  mapper.insert(map);
		  return "1";
		} catch (Exception e) {
			e.printStackTrace();
		  return "0";	// TODO: handle exception
		}
	}

	//插入异步任务
	@Override
	public void insertInterfaceMail(Map<String, String> map){
		mapper.insertInterfaceMail(map);
	}

	/**
	 * 将数据插入到邮件异步队列
	 * @param exchangeName:交换机rotingkey
	 * @param roting binging的roting key 
	 */
	@Override
	public void sendMailQueue(JSONObject json) {
		// TODO Auto-generated method stub
		//routingkey 要与配置文件中的pattern="*.mail.*"进行配置，所以要注意
		String  routingkey="mail.async";
		rabbit.convertAndSend(exchange_name, routingkey, json);
	}
	
	 /**
     * 邮件发送公共类
     * @author chen.kai
     * @param to 邮件接收者
     * @param cc 邮件抄送者
     * @param subject 邮件主题
     * @param context 邮件内容
     * @param templateText 模版内容
     * @param templateFile 将模版文件
     * @param fileName 附件名称
     * @param filePath 附件地址
     * @return
     */
	@Override
    public  String sendMail(final String to,final String cc,final String subject,final String context,final String templateText,final File templateFile,final String fileName,final String filePath,final JSONObject json,final String templateFileName) throws Exception{	
		//创建一个长度为10的线程池
		
		ExecutorService executor=Executors.newFixedThreadPool(10);
		//构建CompletionService，使其获取返回值不再等待，以防线程阻塞
		CompletionService<String> completionService=new ExecutorCompletionService<String>(executor);
		completionService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				  return sendAsyncMail(to,cc,subject,context,templateText,templateFile,fileName,filePath,json,templateFileName);
			}
		   }
		);
	   executor.shutdown();
       return completionService.take().get(1000,TimeUnit.MILLISECONDS);
    }
	
    //利用线程吃异步发送
    private  String sendAsyncMail(String to,String cc, String subject, String context,String templateText,File templateFile,String fileName,String filePath,JSONObject data, String templateFileName) throws Exception{	
    	//创建邮件信息
    	MimeMessage message=sender.createMimeMessage();
    	//创建邮件信息帮助
		//true-可以指定发送附件等，UTF-8设置邮件内容的编码（在收件人的邮箱中展示中文）
		MimeMessageHelper helper=new MimeMessageHelper(message, true,"UTF-8");
		//设置发送人,发送人一定要和登陆人账号一样，否则邮件会发送失败
		helper.setFrom(Msnc.email_from);
		//设置邮件接收人,多个接收人之间，用“;”隔开
		if(!"".equals(to))helper.setTo(to.split(";"));
		//设置抄送人，多个抄送人之间以“;”隔开
		if(!"".equals(cc))helper.setCc(cc.split(";"));
		//设置邮件主题
		if(!"".equals(subject))helper.setSubject(subject);
		//设置邮件内容,当true时此处发送的是html邮件，
		//取出邮件文本内容
		if(!"".equals(context)||!"".equals(templateText)){
			String emailtext="";
			try {
				 //emailtext=initTemplate(context,templateText,templateFile,"email_blank_template.ftl");
				 emailtext=initTemplate(context,templateText,templateFile,templateFileName);
				 System.out.println("======================"+templateFileName);
			} catch (Exception e) {
				// TODO: handle exception
				logger.debug(ExceptionUtil.println(e));
				return "1";//模版内容设置失败
			}
			helper.setText(initTableContent(context, emailtext),true);
		}else if("".equals(context)&&"".equals(templateText)){
			helper.setText(subject);
		}else helper.setText(subject,true);
		//设置附件
		try {
			if(!"".equals(filePath))setFile(helper, fileName, filePath);
		} catch (Exception e) {
			// TODO: handle exception
			logger.debug(ExceptionUtil.println(e));
			return "2";//附件设置失败
		}
		//发送邮件
		try {
			//long n=System.currentTimeMillis();a
			sender.send(message);
		}catch (Exception e) {
			if(e instanceof MailSendException){
				String error=e.getMessage();
				MailSendException es=(MailSendException) e;
			//	SendFailedException es=(SendFailedException) e;
				if(error.indexOf("SMTPAddressFailedException")!=-1){
//					 Address[] unsend = es.getValidUnsentAddresses();   
//	                 if(null!=unsend)  {    
//	                     String validAddress = "";  
//	                     for(int i=0;i<unsend.length;i++){  
//	                         validAddress += unsend[i] + ";";  
//	                     }  
//	                     validAddress = validAddress.substring(0,validAddress.length()-1);  
//	                     
//	                     System.out.println(validAddress);
//	                 }  
	               
					return "5";					
				}		
			}
			logger.debug(ExceptionUtil.println(e));
			String type=data.getString("dotype");
			if(!type.equals("1")){
				//当网络断了之后，发生的异常信息为：Unknown SMTP host
				if(e.getMessage().indexOf("Unknown SMTP host")!=0){
					//插入重发数据，先去掉里面的bodystr
					Map<String, String> map=new HashMap<String, String>();
					map.put("messageId", data.getString("messageId"));
	                map.put("interface_url",data.getString("interfaceurl"));
					map.put("content", data.getString("body"));
					try {
						repeatTaskMapper.insert(map);
					} catch (Exception e2) {
						logger.debug(ExceptionUtil.println(e2));
					}
				return "4";
				}
			}
			//throw new RuntimeException(e);
			return "3";
		}finally{
			if(null!=templateFile){
				templateFile.delete();
			}
		}
		return "0";
    }
	
    /**
     * 邮件中的附件初始化  FileSystemResource针对本地文件，UrlResource针对网络文件
     * @param helper
     * @param fileName 附件名称:a.png
     * @param filePath 附件地址:http://www.baidu.com/imgs/a.png
     */
    private void setFile(MimeMessageHelper helper,String fileName,String filePath) throws Exception{
    	if("".equals(filePath.trim())){
    		return ;
    	} 
    	//对多个附件进行分割
    	String[] filepaths=filePath.split(";");
    	String[] filenames=fileName.split(";");
    	for (int i = 0,len=filepaths.length; i <len; i++) {
			//加载网路附件，并且对附件的中文名称进行转码，免得乱码
			//helper.addAttachment(MimeUtility.encodeWord(filenames[i]), new FileSystemResource(file));
			helper.addAttachment(MimeUtility.encodeWord(filenames[i]), new UrlResource(filepaths[i]));
		}
    }

	private String initTableContent(String context,String emailtext) throws Exception{
		//如果邮件内容当中含有表格则需要循环表格的内容
		if(context.indexOf("tables")>-1){
			//去掉数据中的空格
			emailtext=emailtext.replaceAll(" ", "");
			//截取最后到最后一个tr结束
			String left=emailtext.substring(0, emailtext.lastIndexOf("</tr>")+5);
			//从最后一个tr截取到结束
			String right=emailtext.substring(emailtext.lastIndexOf("</tr>")+5,emailtext.length());
			JSONObject json=JSONObject.fromObject(context);
			String[] trs=StringUtil.getString(json.get("tables")).split(",");
			int tr_len=trs.length;
			for (int i = 0; i < tr_len; i++) {
				left+="<tr>";
				String[] tds=trs[i].split("\\|");
				int td_len=tds.length;
				for (int j = 0; j < td_len; j++) {
					left+="<td>"+(tds[j]==null?"":tds[j])+"</td>";
				}
				left+="</tr>";
				tds=null;
			}
			left+=right;
			emailtext=left;
			System.out.println(emailtext);
			//内存收回
			left=null;right=null;trs=null;json=null;
		}
		return emailtext;
	}
   
    
    
    
    
//  private  JavaMailSender initJavaMailSender(Map<String,String> map){
//	//设置发送人
//	//Spring的邮件工具类，实现了MailSender和JavaMailSender接口
//	JavaMailSenderImpl sender=new JavaMailSenderImpl();
//	//设置邮件服务器
//	sender.setHost("smtp.126.com");
//	//设置邮件端口号，一般邮件默认为25
//	sender.setPort(25);
//	//设置邮件发送人登陆账号
//	sender.setUsername("");
//	//设置邮件发送人登陆密码,并且对密码进行解密
//	sender.setPassword("");
//	//设置编码
//	sender.setDefaultEncoding("UTF-8");
//	//给邮件服务系统设置属性
//	Properties props=System.getProperties();
//	props.put("mail.smtp.auth", "true");
//	props.put("mail.smtp.timeout", 25000);
//	props.put("mail.smtp.starttls.enable", true);
//	//props.put("mail.transport.protocol", "smtp");
//	props.put("mail.smtp.socketFactory.port", "456");
//	props.put("mail.smtp.socketFactory.fallback", "false");
//	sender.setJavaMailProperties(props);
//	return sender;
//}

///**
// * 邮件中的图片初始化，FileSystemResource针对本地文件，UrlResource针对网络文件
// * @param helper
// * @param imgsrc 图片地址:http://baidu.com/img/aa.jpg
// * @throws MalformedURLException 
// */
//public void setImgs(MimeMessageHelper helper,String imgsrc) throws MalformedURLException,MessagingException{
//	//将json对象转换为数组
//	String[] array=imgsrc.split(";");
//	//循环取出图片
//	for (int i = 0; i < array.length; i++) {
//		//FileSystemResource file = new FileSystemResource(array[i]);	
//		UrlResource file=new UrlResource(array[i]);
//		helper.addInline("Cid"+i, file);
//	}
//}
}
