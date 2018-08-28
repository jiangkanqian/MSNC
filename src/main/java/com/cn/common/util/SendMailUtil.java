package com.cn.common.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.codehaus.jackson.map.ObjectMapper;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.cn.common.service.impl.HomeServiceImpl;
import com.cn.common.sys.bean.Msnc;

import freemarker.template.Template;
import net.sf.json.JSONObject;



/**
 * 邮件发送公共类，因为发送数据有模版进行统一规范，故此类只发送html邮件
 * @author chenkai
 * date:2016-11-14
 */
public class SendMailUtil extends HomeUtil{
	
	
	static Logger loger=LoggerFactory.getLogger(SendMailUtil.class);
    
    /**
     * 初始化一个javaMailSender（邮件服务器）
     * @param host：邮件服务器地址
     * @param username：邮件登陆账号
     * @param password：邮件登陆密码
     * @return JavaMailSender
     */
    private static  JavaMailSender initJavaMailSender(Map<String,String> map){
    	//Spring的邮件工具类，实现了MailSender和JavaMailSender接口
    	JavaMailSenderImpl sender=new JavaMailSenderImpl();
    	//设置邮件服务器
    	sender.setHost(map.get("HOST"));
    	//设置邮件端口号，一般邮件默认为25
    	//sender.setPort(25);
    	//设置邮件发送人登陆账号
    	sender.setUsername(map.get("USER_NAME"));
    	//设置邮件发送人登陆密码,并且对密码进行解密
    	sender.setPassword(DesUtils.decode(map.get("PASSWORD")));
    	//设置编码
    	sender.setDefaultEncoding("UTF-8");
    	//给邮件服务系统设置属性
    	Properties props=System.getProperties();
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.timeout", 25000);
    	props.put("mail.smtp.starttls.enable", true);
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.socketFactory.port", "456");
    	props.put("mail.smtp.socketFactory.fallback", "false");
    	sender.setJavaMailProperties(props);
    	return sender;
    }

   
//    /**
//     * 邮件中的图片初始化，FileSystemResource针对本地文件，UrlResource针对网络文件
//     * @param helper
//     * @param imgsrc 图片地址:http://baidu.com/img/aa.jpg
//     * @throws MalformedURLException 
//     */
//    public void setImgs(MimeMessageHelper helper,String imgsrc) throws MalformedURLException,MessagingException{
//    	//将json对象转换为数组
//    	String[] array=imgsrc.split(";");
//    	//循环取出图片
//    	for (int i = 0; i < array.length; i++) {
//			//FileSystemResource file = new FileSystemResource(array[i]);	
//    		UrlResource file=new UrlResource(array[i]);
//			helper.addInline("Cid"+i, file);
//		}
//    }
    
    /**
     * 邮件中的附件初始化  FileSystemResource针对本地文件，UrlResource针对网络文件
     * @param helper
     * @param fileName 附件名称:a.png
     * @param filePath 附件地址:d:/a.png
     */
    public static void setFile(MimeMessageHelper helper,String fileName,String filePath) throws Exception{
    	if("".equals(filePath.trim())){
    		loger.info("没有附件发送！");
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
     * @param map 邮件相关配置信息：登陆账号，密码，邮件服务器host之类的
     * @return
     */
    public static String sendMail(String to,String cc, String subject, String context,String templateText,File templateFile,String fileName,String filePath,Map<String,String> map) throws Exception{
    	String retcode="0";
    	//获取邮箱服务器
    	JavaMailSender sender=initJavaMailSender(map);
    	//创建邮件信息
    	MimeMessage message=sender.createMimeMessage();
    	//创建邮件信息帮助
		//true-可以指定发送附件等，UTF-8设置邮件内容的编码（在收件人的邮箱中展示中文）
		MimeMessageHelper helper=new MimeMessageHelper(message, true,"UTF-8");
		//设置发送人
		helper.setFrom(Msnc.email_from);
		//设置邮件接收人,多个接收人之间，用“;”隔开
		if(!"".equals(to))helper.setTo(to.split(";"));
		//设置抄送人，多个抄送人之间以“;”隔开
		if(!"".equals(cc))helper.setCc(cc.split(";"));
		//设置邮件主题
		if(!"".equals(subject))helper.setSubject(subject);
		//设置邮件内容,当true时此处发送的是html邮件，
		//取出邮件文本内容
		if(!"".equals(context))helper.setText(initTemplate(context,templateText,templateFile,"email_blank_template.ftl"), true);
//			//设置图片内容
//			if(!"".equals(imgs)){
//				setImgs(helper, imgs);
//			}
		//设置附件
		if(!"".equals(filePath))setFile(helper, fileName, filePath);
		//发送邮件
		sender.send(message);
		retcode="1";
		return retcode;
    }
    
   
    
   
}
