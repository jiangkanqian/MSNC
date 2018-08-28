package com.cn.common.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.http.Consts;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cn.common.dao.InterfaceMobileMapper;
import com.cn.common.dao.InterfaceRepeatSendTaskMapper;
import com.cn.common.service.InterfaceMobileService;
import com.cn.common.sys.bean.Msnc;
import com.cn.common.util.Base64Util;
import com.cn.common.util.DesUtils;
import com.cn.common.util.ExceptionUtil;
import com.cn.common.util.HttpRequestUtil;
import com.cn.common.util.PoolingHttpClientUtil;
import com.cn.common.util.StringUtil;
import com.cn.common.util.XmlUtil;
import com.google.common.collect.Lists;

import freemarker.template.Template;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 手机定时器任务扫描Service实现类
 * 
 * @author chenkai
 *
 */
@Service
public class InterfaceMobileServiceImpl implements InterfaceMobileService {

	Logger log = LoggerFactory.getLogger(InterfaceMobileServiceImpl.class);

	@Resource
	private InterfaceMobileMapper mapper;

	@Resource
	private RabbitTemplate rabbit;

	@Resource(name = "repeatTaskMapper")
	private InterfaceRepeatSendTaskMapper repeatTaskMapper;

	@Resource
	private FreeMarkerConfigurer freeMarkerConfigurer;

	// 交换器名称，必须和spring-amqp.xml里面的name="mob.topicExchange"一致，否则会找不到交换器
	private final String exchange_name = "topicExchange";

	@Override
	public List<Map<String, String>> selectAllMobileQueueList() {
		// TODO Auto-generated method stub
		return mapper.selectByParameterObject(null);
	}

	@Override
	public String insertMobileLog(Map<String, String> map) {
		// TODO Auto-generated method stub
		try {
			mapper.insert(map);
			return "1";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "0";
		}

	}

	@Override
	public void insertInterfaceMobile(Map<String, String> map) {
		mapper.insertInterfaceMobile(map);
	}

	@Override
	public void sendMobileQueue(final JSONObject json) {
		String routingkey = "mob.async";
		rabbit.convertAndSend(exchange_name, routingkey, json);
	}

	@Override
	public String sendEqMessage(String userId, String pass, String phone, String content, String modecontent, int count,
			JSONObject data) throws Exception {
		String str = mwHttpSendEqualContent(userId, pass, phone, content, modecontent, count);
		// 如果是异步发送和定时发送就执行消息重发机制
		String type = data.getString("dotype");
		if (!type.equals("1")) {
			if (str.equals("-1000")) {
				// 执行插入
				// 插入重发数据，先去掉里面的bodystr
				Map<String, String> map = new HashMap<String, String>();
				map.put("messageId", data.getString("messageId"));
				map.put("interface_url", data.getString("interfaceurl"));
				map.put("content", data.getString("body"));
				// map.put("content", data.toString().replaceAll("\"",
				// "\\\\\""));
				try {
					repeatTaskMapper.insert(map);
				} catch (Exception e2) {
					log.debug(ExceptionUtil.println(e2));
				}			
			}
		}
		return str;
	}

	@Override
	public String sendNotEqMessage(String userid, String pass, String modelcontent, JSONArray contents, JSONObject data)
			throws Exception {
		// TODO Auto-generated method stub
		String str = mwHttpSendNotEqualContent(userid, pass, modelcontent, contents);
		String type = data.getString("dotype");
		if (!type.equals("1")) {
			if (str.equals("-1000")) {
				// 插入重发数据，先去掉里面的bodystr
				// data.remove("bodystr");
				Map<String, String> map = new HashMap<String, String>();
				map.put("messageId", data.getString("messageId"));
				map.put("interface_url", data.getString("interfaceurl"));
				map.put("content", data.getString("body"));
				try {
					repeatTaskMapper.insert(map);
				} catch (Exception e2) {
					log.debug(ExceptionUtil.println(e2));
				}
			}
		}
		return str;
	}

	public String initMultixmt(String modelcontent, JSONArray contents) throws Exception {
		// multixmt=*|13012345678|suLK1Gh0dHDH68fzMQ==|SvrType|P1|P2|||0|0|0|0|1,*|13112345678|suLK1Gh0dHDH68fzMg==|SvrType|P1|P2|||0|0|0|0|1";
		String multixmt = "";
		String phone = "";
		String text = "";
		String mobilecode = "";
		// 当模版内容为空时，contents当中content必须含有phone(手机号)和text(短信内容)两个参数
		if (modelcontent.equals("")) {
			if (contents.size() > 0) {
				for (int i = 0; i < contents.size(); i++) {
					// 取出每个短信包
					JSONObject content = contents.getJSONObject(i);
					// 取出每个短信包中的手机号
					phone = StringUtil.getString(content.get("phone"));
					// 取出每个短信包中的短信内容，并且用编码base64进行转码,字符集为gbk
					text = Base64Util.encrypt(StringUtil.getString(content.get("text")), "gbk");
					mobilecode = "*|".concat(phone).concat("|").concat(text).concat("|SvrType|P1|P2|||0|0|0|0|1");
					if (i == contents.size() - 1)
						multixmt += mobilecode;
					else
						multixmt += mobilecode.concat(",");
				}
			}
		} else {
			// 将模版构建成一个文件
			File file = new File(Msnc.template_path.concat("/mobile_blank_template.ftl"));
			// 如果两个都不为空，就先将模版内容写入响应的模版，并且替换
			if (contents.size() > 0) {
				// 共用一个模版
				writeTemplateContent(modelcontent, file);
				for (int i = 0; i < contents.size(); i++) {
					// 取出每个短信包
					JSONObject content = contents.getJSONObject(i);
					// 取出每个短信包中的手机号
					phone = StringUtil.getString(content.get("phone"));
					if (content.size() == 1) {// 如果每个content的长度为1，则说明模版当中没有替换的内容，只发送模版内容
						content = null;
						text = Base64Util.encrypt(modelcontent, "gbk");
					} else {
						// 取出每个短信包中的短信内容，并且用编码base64进行转码,字符集为gbk new String
						// (context,"u")

						text = Base64Util.encrypt(freeMarkerToString("mobile_blank_template.ftl", content), "gbk");
					}
					mobilecode = "*|".concat(phone).concat("|").concat(text).concat("|SvrType|P1|P2|||0|0|0|0|1");
					multixmt += mobilecode + ',';
				}
				// 干掉最后一个逗号
				multixmt = multixmt.substring(0, multixmt.length() - 1);
			}
		}
		return multixmt;
	}

	public String initMobileCode(String content, String modecontent) {
		// 将模版构建成一个文件
		File file = null;
		if (!"".equals(modecontent))
			file = new File(Msnc.template_path.concat("/mobile_blank_template.ftl"));
		// 将模版内容构建为发送内容
		try {
			return initTemplate(content, modecontent, file, "mobile_blank_template.ftl");
		} catch (Exception e) {
			return "-998";
		}
	}

	/**
	 * 移动梦网群发相同内容http接口
	 * 
	 * @param userId
	 *            梦网登陆账号
	 * @param pass
	 *            密码
	 * @param phone
	 *            手机号
	 * @param content
	 *            短信内容
	 * @param modecontent
	 *            模版内容
	 * @param count
	 *            手机号码数
	 * @return
	 */
	public String mwHttpSendEqualContent(String userId, String pass, String phone, String content, String modecontent,
			int count) throws Exception {
		String str = "-999";
		// 先初始化短信内容
		String text = initMobileCode(content, modecontent);
		// 如果模版初始化就直接返回
		if (text.equals("-998"))
			return text;
		// 构建梦网url
		String url = Msnc.mw_url.concat("/").concat(Msnc.mw_equal_content_method);
		// 构建httpclient
		CloseableHttpClient client = PoolingHttpClientUtil.getHttpClient(url);
		// 构建httppost
		HttpPost post = new HttpPost(url);
		// 初始化头部
		PoolingHttpClientUtil.config(post);
		// 构建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("userId", userId));
		formparams.add(new BasicNameValuePair("password", DesUtils.decode(pass)));
		formparams.add(new BasicNameValuePair("pszMobis", phone));
		formparams.add(new BasicNameValuePair("pszMsg", text));
		formparams.add(new BasicNameValuePair("iMobiCount", String.valueOf(count)));
		// 构建传输对象
		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		post.setEntity(uefEntity);
		CloseableHttpResponse response = null;
		// 执行get方法
		try {
			response = client.execute(post, HttpClientContext.create());
		} catch (Exception e) {
			// e.printStackTrace();
			// 当断网时，接口抛出的异常信息为：connect timed out
			if (e.getMessage().indexOf("connect timed out") != 0) {
				str = "-1000";
			}
			log.debug(ExceptionUtil.println(e));
		}
		int state = 0;
		if (null != response)
			state = response.getStatusLine().getStatusCode();
		if (state == 200) {
			str = XmlUtil.getRootNodeText(EntityUtils.toString(response.getEntity()));
		}
		// 释放response链接
		if (response != null) {
			response.close();
			post.abort();
		}
		return str;
	}
	/**
	 * 移动梦网群发相同内容http接口
	 * 
	 * @param userId
	 *            梦网登陆账号
	 * @param pass
	 *            密码
	 * @param phone
	 *            手机号
	 * @param content
	 *            短信内容
	 * @param modecontent
	 *            模版内容
	 * @param count
	 *            手机号码数
	 * @return
	 */
	public String mwHttpSendNotEqualContent(String userid, String pass, String modelcontent, JSONArray contents)
			throws Exception {
		// 初始化短信包
		String multixmt = "";
		try {
			multixmt = initMultixmt(StringUtil.getString(modelcontent), contents);
		} catch (Exception e) {
			e.printStackTrace();
			return "-998";
		}
		// 构建梦网url
		String url = Msnc.mw_url.concat("/").concat(Msnc.mw_not_equal_content_method);
		String params="userId=" + userid;
		params += "&password=" + DesUtils.decode(pass);
		params += "&multixmt=" + multixmt;
		String str = "-999";
		try {
			str=HttpRequestUtil.sendPost(url,params);
		} catch (Exception e) {
			log.debug(ExceptionUtil.println(e));
		}
		return str;
	}

//	/**
//	 * 移动梦网群发相同内容http接口
//	 * 
//	 * @param userId
//	 *            梦网登陆账号
//	 * @param pass
//	 *            密码
//	 * @param phone
//	 *            手机号
//	 * @param content
//	 *            短信内容
//	 * @param modecontent
//	 *            模版内容
//	 * @param count
//	 *            手机号码数
//	 * @return
//	 */
//	public String mwHttpSendNotEqualContent(String userid, String pass, String modelcontent, JSONArray contents)
//			throws Exception {
//		// 初始化短信包
//		String multixmt = "";
//		try {
//			multixmt = initMultixmt(StringUtil.getString(modelcontent), contents);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "-998";
//		}
//		// 构建梦网url
//		String url = Msnc.mw_url.concat("/").concat(Msnc.mw_not_equal_content_method);
//		url += "?userId=" + userid;
//		url += "&password=" + DesUtils.decode(pass);
//		url += "&multixmt=" + multixmt;
//		// String retStr=HttpUrlConnectionUtil.sendGet2(url);
//		// retStr=XmlUtil.getRootNodeText(retStr);
//		// //构建httpclient
////        List<NameValuePair> params = Lists.newArrayList();  
////        params.add(new BasicNameValuePair("userId",userid));  
////        params.add(new BasicNameValuePair("password",DesUtils.decode(pass))); 
////        params.add(new BasicNameValuePair("multixmt",multixmt)); 
////        String param=  EntityUtils.toString(new UrlEncodedFormEntity(params)); 
////        url=url+"?"+param;
//		
//		
//		CloseableHttpClient client = PoolingHttpClientUtil.getHttpClient(url);
//		
//		HttpGet get = new HttpGet(url);
//		
//	    PoolingHttpClientUtil.config(get);
//		
//		
//		
//		// 构建httppost
//		// HttpPost post=new HttpPost(url);
//		// 初始化头部
//		// PoolingHttpClientUtil.config(post);
//		// //构建参数队列
//		// List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//		// formparams.add(new BasicNameValuePair("userId",userid));
//		// formparams.add(new
//		// BasicNameValuePair("password",DesUtils.decode(pass)));
//		// formparams.add(new BasicNameValuePair("multixmt",multixmt));
//		// //构建传输对象
//		// UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formparams,
//		// "UTF-8");
//		// post.setEntity(uefEntity);
//		// 执行get方法
//		CloseableHttpResponse response = null;
//		String str = "-999";
//		try {
//			// response =client.execute(post,HttpClientContext.create());
//			response = client.execute(get, HttpClientContext.create());
//		} catch (Exception e) {
//			// 当断网时，接口抛出的异常信息为：connect timed out
//			if (e.getMessage().indexOf("connect timed out") != 0) {
//				str = "-1000";
//			}
//			log.debug(ExceptionUtil.println(e));
//		}
//		int state = 0;
//		if (null != response)
//			state = response.getStatusLine().getStatusCode();
//		if (state == 200) {
//			str = XmlUtil.getRootNodeText(EntityUtils.toString(response.getEntity()));
//		}
//		// 释放response链接
//		if (response != null) {
//			response.close();
//			// post.abort();
//			get.abort();
//		}
//		return str;
//	}

	/**
	 * 将模版内容写入模版文件
	 * 
	 * @param templateText
	 * @param templateFile
	 * @throws IOException
	 */
	public void writeTemplateContent(String templateText, File templateFile) throws IOException {
		// 构建输出流，并且设定编码，防止中文乱码
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(templateFile), "UTF-8");
		// 将模版内容写到模版上面
		out.write(templateText);
		out.flush();
		out.close();
	}

	public String initTemplate(String context, String templateText, File templateFile, String ftl) throws Exception {
		// 如果模版内容为空据就发送 文本内容
		if (StringUtil.isNull(templateText))
			return context;
		// 如果文本内容为空，就发送模版内容
		if (StringUtil.isNull(context))
			return templateText;
		// 如果两个都不为空，就先将模版内容写入响应的模版，并且替换
		writeTemplateContent(templateText, templateFile);
		// 参数加载到模版当中
		JSONObject object = new JSONObject();
		// 先将字符串转为json对象，用于传输到模版，进行键值匹配
		ObjectMapper mapper = new ObjectMapper();
		// 先写模版
		object = mapper.readValue(context, JSONObject.class);
		// 模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
		return freeMarkerToString(ftl, object);

	}

	public String freeMarkerToString(String ftl, JSONObject json) throws Exception {
		// 从FreeMarker模板生成邮件内容
		Template template = freeMarkerConfigurer.getConfiguration().getTemplate(ftl);
		// 模板中用${XXX}占位，map中key为XXX的value会替换占位符内容。
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, json);

	}
	public static void main(String[] args) throws MalformedURLException, URISyntaxException {
		String strUrl = Msnc.mw_url.concat("/").concat(Msnc.mw_not_equal_content_method);

		String param="userId=SEND01&password=123456&multixmt=554498*";
		//url =url+"?"+URLEncoder.encode(param,"UTF-8");
		strUrl =strUrl+"?"+param;
		URL url = new URL(strUrl);
		URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		String one="http://114.67.57.57:5122/MWGate/wmgw.asmx/MongateCsSpMultixMtSend?userId=JKCS01&password=123456&multixmt=*|18682450312|y+a7+sr919ajuqG+MTExNqG/LGptZXRlcs2ssr3IureisrvNrMTayN3Tw7unNjIxNqOh|SvrType|P1|P2|||0|0|0|0|1,*|15989490621|y+a7+sr919ajuqG+NDM0OaG/LGptZXRlcs2ssr3IureisrvNrMTayN3Tw7unNzUzMqOh|SvrType|P1|P2|||0|0|0|0|1";
		one=one.substring(106);
		System.out.println(one);
		
	}
}
