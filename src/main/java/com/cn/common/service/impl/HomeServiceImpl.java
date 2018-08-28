package com.cn.common.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cn.common.dao.InterfaceRepeatSendTaskMapper;
import com.cn.common.dao.InterfaceTaskLogMapper;
import com.cn.common.service.HomeService;
import com.cn.common.util.RedisCacheUtil;

import freemarker.template.Template;
import net.sf.json.JSONObject;

@Service
public class HomeServiceImpl implements HomeService{
  
	@Resource
	protected RedisCacheUtil<?> util;
	@Resource
	FreeMarkerConfigurer freeMarkerConfigurer;	
	@Resource(name="repeatTaskMapper")
	InterfaceRepeatSendTaskMapper repeatTaskMapper;
	
	@Resource
	JavaMailSender sender;//获取邮箱服务器
	
	@Resource
    RabbitTemplate rabbit;
	
	@Resource
	InterfaceTaskLogMapper logMapper;
		 
    /**
     * 将模版内容写入模版文件
     * @param templateText
     * @param templateFile
     * @throws IOException
     */
    public   void writeTemplateContent(String templateText,File templateFile)throws IOException{
    	//构建输出流，并且设定编码，防止中文乱码
    	OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(templateFile),"UTF-8"); 
		//将模版内容写到模版上面
    	out.write(templateText);  
    	out.flush();  
    	out.close();	
    }
    /**
     * 将json数据初始化到模版当中
     */
    public   String freeMarkerToString(String ftl,JSONObject json) throws Exception{
    	 //从FreeMarker模板生成邮件内容
        Template template =freeMarkerConfigurer.getConfiguration().getTemplate(ftl);
        //模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, json);
    }
    
    /**
     * 将map数据初始化到模版当中
     */
    public  String freeMarkerToStringOfMap(String ftl,Map<String, String> map) throws Exception{
    	 //从FreeMarker模板生成邮件内容
        Template template =freeMarkerConfigurer.getConfiguration().getTemplate(ftl);
        //模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
        return  FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
    }
    /**
     * 模版初始化操作，当有
     * @param context 模版占位符替换内容
     * @param templateText 模版内容
     * @param templateFile 要写内容的模版文件
     * @return
     * @throws Exception
     */
    public     String initTemplate(String context,String templateText,File templateFile,String ftl) throws Exception{
    	 //如果模版内容为空据就发送 文本内容
    	 if("".equals(templateText)) return context;
    	 //如果文本内容为空，就发送模版内容
    	 if("".equals(context)) return templateText;
    	 //如果两个都不为空，就先将模版内容写入响应的模版，并且替换
	     writeTemplateContent(templateText, templateFile);
	     //参数加载到模版当中
    	 JSONObject object=new JSONObject();
    	 //先将字符串转为json对象，用于传输到模版，进行键值匹配
		 ObjectMapper mapper=new ObjectMapper();
		 //先写模版		
         object=mapper.readValue(context, JSONObject.class);
         //模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
         return  freeMarkerToString(ftl,object);
    
    }
   
    void updateFromRepeatSendTask(String messageId){
    	logMapper.updateFromRepeatSendTask(messageId);
    }
}
