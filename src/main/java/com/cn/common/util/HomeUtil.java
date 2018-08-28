package com.cn.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cn.common.sys.tools.SpringGetBean;

import freemarker.template.Template;
import net.sf.json.JSONObject;
/**
 * util根类
 * @author chen.kai
 * date:2017-01-10
 */
public class HomeUtil {

	//freemarker帮助类
    static FreeMarkerConfigurer freeMarkerConfigurer;
    
    static{
    	freeMarkerConfigurer=(FreeMarkerConfigurer)SpringGetBean.getBean("freeMarkerConfigurer");
    }
    
    
    /**
     * 将模版内容写入模版文件
     * @param templateText
     * @param templateFile
     * @throws IOException
     */
    public static void writeTemplateContent(String templateText,File templateFile)throws IOException{
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
    public static String freeMarkerToString(String ftl,JSONObject json) throws Exception{
    	 //从FreeMarker模板生成邮件内容
        Template template =freeMarkerConfigurer.getConfiguration().getTemplate(ftl);
        //模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, json);
         
    }
    
    /**
     * 将map数据初始化到模版当中
     */
    public static String freeMarkerToStringOfMap(String ftl,Map<String, String> map) throws Exception{
    	 //从FreeMarker模板生成邮件内容
        Template template =freeMarkerConfigurer.getConfiguration().getTemplate(ftl);
        //模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
         
    }
    /**
     * 模版初始化操作，当有
     * @param context 模版占位符替换内容
     * @param templateText 模版内容
     * @param templateFile 要写内容的模版文件
     * @return
     * @throws Exception
     */
    public static String initTemplate(String context,String templateText,File templateFile,String ftl) throws Exception{
    	 //如果模版内容为空据就发送 文本内容
    	 if(StringUtil.isNull(templateText)) return context;
    	 //如果文本内容为空，就发送模版内容
    	 if(StringUtil.isNull(context)) return templateText;
    	 //如果两个都不为空，就先将模版内容写入响应的模版，并且替换
	     writeTemplateContent(templateText, templateFile);
	     //参数加载到模版当中
    	 JSONObject object=new JSONObject();
    	 //先将字符串转为json对象，用于传输到模版，进行键值匹配
		 ObjectMapper mapper=new ObjectMapper();
		 //先写模版		
         object=mapper.readValue(context, JSONObject.class);
         //模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
         return  freeMarkerToString(ftl, object);
    
    }
    public static String initTemplate(File templateFile,String content,String templateText) throws Exception{
    	String text = templateText;
    	String ftl="wechat_blank_template.ftl";
    	//写模版内容到模版文件中
    	writeTemplateContent(templateText, templateFile);
    	//参数加载到模版当中
    	JSONObject object=new JSONObject();
    	//如果替换内容不为空，就将占位内容替换掉
    	if(!"".equals(content)){
    		//先将字符串转为json对象，用于传输到模版，进行键值匹配
    		ObjectMapper mapper=new ObjectMapper();
    		//先写模版		
	        object=mapper.readValue(content, JSONObject.class);
	        //从FreeMarker模板生成邮件内容
	         Template template =freeMarkerConfigurer.getConfiguration().getTemplate(ftl);
	         //模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
	         text = FreeMarkerTemplateUtils.processTemplateIntoString(template, object);
        }        
    	return text;
    }
}
