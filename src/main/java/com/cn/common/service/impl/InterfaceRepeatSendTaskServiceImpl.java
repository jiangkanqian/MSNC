package com.cn.common.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cn.common.dao.InterfaceRepeatSendTaskMapper;
import com.cn.common.dao.ModelMapper;
import com.cn.common.service.InterfaceRepeatSendTaskService;
import com.cn.common.service.ModelService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.ExceptionUtil;
import com.cn.common.util.SendMobileCodeUtil;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 接口消息重发任务实现类
 * @author chen.kai
 * date:2017-02-28
 */
@Service
public class InterfaceRepeatSendTaskServiceImpl extends HomeServiceImpl  implements InterfaceRepeatSendTaskService {

	private  final Logger logger=   LoggerFactory.getLogger(InterfaceMailServiceImpl.class);

	
	@Resource
	private ModelMapper modelmapper;
	
	/**
	 * 插入未发送成功的任务
	 */
	@Override
	public void insert(Map<String, String> map) {
		// TODO Auto-generated method stub
		try {
			repeatTaskMapper.insert(map);
		} catch (Exception e2) {
			e2.printStackTrace();
			logger.debug(ExceptionUtil.println(e2));
		}
	}

	/**
	 * 获取未成功的任务
	 */
	@Override
	public List<Map<String, String>> getNotSucceedTask() {
		// TODO Auto-generated method stub
		return repeatTaskMapper.getNotSucceedTask();
	}

	/**
	 * 修改执行过的任务记录
	 */
	@Override
	public void updateNotSuccedTask(Map<?, ?> map) {
		// TODO Auto-generated method stub
		repeatTaskMapper.updateNotSuccedTask(map);
	}

	/**
	 * 将未发送成功的记录插入到任务队列
	 */
	@Override
	public void insertRepeatSendQueues(Map<?, ?> map) {	
	   rabbit.convertAndSend("topicExchange", "repeat.send.task", map);
	}

	//通过模版编号获取模版内容
	private String getTemplateContent(String modelcode){

		return StringUtil.getString(modelmapper.findModelContent(modelcode));
	}
	
	//初始化邮件内容
	private String initMailContent(String templatetext,String mailtext) throws Exception{
		
		if(!templatetext.equals("")||!mailtext.equals("")){
			//把模版构建成一个file文件
			File templateFile=new File(Msnc.template_path+"/email_blank_template.ftl");
			//初始化模版内容
			String modeltext="";//initTemplate(mailtext,templatetext,templateFile,"email_blank_template.ftl");
			//如果邮件内容当中含有表格则需要循环表格的内容
			if(mailtext.indexOf("tables")>-1){
				String html=modeltext.substring(0, modeltext.indexOf("</tbody>"));
				JSONObject json=JSONObject.fromObject(mailtext);
				String[] trs=StringUtil.getString(json.get("tables")).split(",");
				for (int i = 0,tr_len=trs.length; i < tr_len; i++) {
					html+="<tr>";
					String[] tds=trs[i].split("\\|");
					for (int j = 0,td_len=tds.length; j < td_len; j++) {
						html+="<td>"+(tds[j]==null?"":tds[j])+"</td>";
					}
					html+="</tr>";
					tds=null;
				}
				html+="</tbody></table><p><br/></p>";
				System.out.println(html);
				trs=null;json=null;
				return html;
			}
			return modeltext;
		}
		return "";
	}
	//初始化邮件内容
	private String initSendMail(JSONObject body) throws Exception{
		 //创建邮件信息
    	MimeMessage message=sender.createMimeMessage();
    	//创建邮件信息帮助
		//true-可以指定发送附件等，UTF-8设置邮件内容的编码（在收件人的邮箱中展示中文）
		MimeMessageHelper helper=new MimeMessageHelper(message, true,"UTF-8");
		//设置发送人,发送人一定要和登陆人账号一样，否则邮件会发送失败
		helper.setFrom(Msnc.email_from);
		//添加接收者
		String to=StringUtil.getString(body.get("to"));
		if(!"".equals(to))helper.setTo(to.split(";"));
		//添加抄送者
		String cc=StringUtil.getString(body.get("cc"));
		if(!"".equals(cc))helper.setTo(cc.split(";"));
		//设置邮件主题
		String subject=StringUtil.getString(body.get("subject"));
		if(!"".equals(subject))helper.setSubject(subject);
		//获取模版内容
		String modelcode=StringUtil.getString(body.get("modelcode"));
		String templatetext="";
		if(!modelcode.equals(""))templatetext=getTemplateContent(modelcode);
		//获取邮件内容
		String mailtext=StringUtil.getString(body.get("content"));
		//初始化邮件内容
		String  text=initMailContent(templatetext, mailtext);
		if(text.equals(""))helper.setText(subject, true);
		else helper.setText(text, true);
		//设置附件
		String filepath=StringUtil.getString(body.get("filepath"));
		if(!"".equals(filepath))setFile(helper, StringUtil.getString(body.get("filename")), filepath);
		try {
			sender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return "1";
	}
	  /**
     * 邮件中的附件初始化  FileSystemResource针对本地文件，UrlResource针对网络文件
     * @param helper
     * @param fileName 附件名称:a.png
     * @param filePath 附件地址:http://www.baidu.com/imgs/a.png
     */
    private  void setFile(MimeMessageHelper helper,String fileName,String filePath) throws Exception{
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
	
    //邮件消息重发
	private void sendMail(JSONObject json) throws Exception{
		// TODO Auto-generated method stub
        JSONObject body=json.getJSONObject("data");
        String flag=initSendMail(body);
        json.put("state", flag);
        //修改任务执行结果
        updateNotSuccedTask(json);
        //修改执行记录
        updateFromRepeatSendTask(json.getString("messageId"));
	}

	//邮件异步发送
	public void asyncSendMail(JSONObject json) throws Exception{
		sendMail(json);
	}
	
	//表格内容异步发送
	public void sendAsyncSingleTableHtmlMail(JSONObject json) throws Exception{
		sendMail(json);
	}
	
	/**
	 * 短信发送相同内容
	 * @param json
	 * @throws Exception
	 */
	public void sendAsyncEqualContent(JSONObject data)throws Exception{ 
		JSONObject json=data.getJSONObject("data");
		//执行短信发送
		String result=SendMobileCodeUtil.mwHttpSendEqualContent(Msnc.mobile_user, Msnc.mobile_pass, json.getString("phone"), StringUtil.getString(json.get("content")), getTemplateContent(StringUtil.getString(json.get("modelcode"))),1000);
		//获取短信错误码
		String msg=StringUtil.getString(Msnc.err_code.get("mobile"+result));
		if("".equals(msg))data.put("state", 1);
		else data.put("state", 0);
		//修改任务执行结果
        updateNotSuccedTask(data);
        //修改执行记录
        updateFromRepeatSendTask(data.getString("messageId"));
	}
	
	public void sendAsyncNotEqualContent(JSONObject data)throws Exception{ 
		JSONObject json=data.getJSONObject("data");
		//执行短信发送
		String result=SendMobileCodeUtil.mwHttpSendNotEqualContent(Msnc.mobile_user, Msnc.mobile_pass, getTemplateContent(StringUtil.getString(json.get("modelcode"))), json.getJSONArray("contents"));
		//获取短信错误码
		String msg=StringUtil.getString(Msnc.err_code.get("mobile"+result));
		if("".equals(msg))data.put("state", 1);
		else data.put("state", 0);
		//修改任务执行结果
        updateNotSuccedTask(data);
        //修改执行记录
        updateFromRepeatSendTask(data.getString("messageId"));
	}
}
