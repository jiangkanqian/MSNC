package com.cn.common.util;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.common.sys.bean.Msnc;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 短信信息发送公共类
 * @author chenkai
 * date:2016-11-11
 * 备注：此处没有成员变量，所以不用考虑线程安全性。
 *
 */
public final class SendMobileCodeUtil extends HomeUtil{
	
	static Logger log=LoggerFactory.getLogger(SendMobileCodeUtil.class);
	/**
     * webService调用，多个手机号群发相同短信内容
	 * @param userId 梦网的登陆账号
	 * @param pass 梦网的密码
	 * @param phone 手机号，多个手机号之间以英文“,”隔开
	 * @param content 短信内容
	 * @param modecontent 模版内容
	 * @param count 发送手机号个数
	 * @return
	 */
	public static String sendEqualContent(String userId,String pass,String phone,String content,String modecontent,int count) throws Exception{
		 //先初始化短信内容
		  String text=initMobileCode(content, modecontent);
		  //如果模版初始化就直接返回
		  if(text.equals("-998"))return text;
		   // 直接引用远程的wsdl文件
		 	Service service = new Service();
			try {
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(Msnc.mw_url.concat("?wsdl"));
				call.setOperationName(Msnc.mw_equal_content_method);// WSDL里面描述的接口名称
				call.addParameter("userId",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
				call.addParameter("password",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
				call.addParameter("pszMobis",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
				call.addParameter("pszMsg",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
				call.addParameter("iMobiCount",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
				call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
				//发送短信
				return (String) call.invoke(new Object[] { userId,DesUtils.decode(pass),phone,text,count});	
			} catch (Exception e) {
				return "-999";// TODO Auto-generated catch block
				
			}
	}
	
	public static String initMultixmt(String modelcontent,JSONArray contents) throws Exception{
		 //multixmt=*|13012345678|suLK1Gh0dHDH68fzMQ==|SvrType|P1|P2|||0|0|0|0|1,*|13112345678|suLK1Gh0dHDH68fzMg==|SvrType|P1|P2|||0|0|0|0|1";
        String multixmt="";
        String phone="";
        String text="";
        String mobilecode="";
        //当模版内容为空时，contents当中content必须含有phone(手机号)和text(短信内容)两个参数
		if(modelcontent.equals("")){
			if(contents.size()>1){
	        	for (int i = 0; i < contents.size(); i++) {
	        		//取出每个短信包
	        		JSONObject content=contents.getJSONObject(i);
	        		//取出每个短信包中的手机号
	        		phone=StringUtil.getString(content.get("phone"));
	        		//取出每个短信包中的短信内容，并且用编码base64进行转码,字符集为gbk
	        		text=Base64Util.encrypt(StringUtil.getString(content.get("text")),"gbk");
	        		mobilecode="*|".concat(phone).concat("|").concat(text).concat("|SvrType|P1|P2|||0|0|0|0|1");
					if(i==contents.size()-1)
						multixmt+=mobilecode;
					else
						multixmt+=mobilecode.concat(",");	
				}
           }
		}
		else{
			 //将模版构建成一个文件
			File file=new File(Msnc.template_path.concat("/mobile_blank_template.ftl"));
			//如果两个都不为空，就先将模版内容写入响应的模版，并且替换
			if(contents.size()>1){
				//共用一个模版
				writeTemplateContent(modelcontent, file);
				for (int i = 0; i < contents.size(); i++) {
	        		//取出每个短信包
	        		JSONObject content=contents.getJSONObject(i);
	        		//取出每个短信包中的手机号
	        		phone=StringUtil.getString(content.get("phone"));
	        		if(content.size()==1){//如果每个content的长度为1，则说明模版当中没有替换的内容，只发送模版内容
	        			content=null;
	        			text=Base64Util.encrypt(modelcontent,"gbk");
	        		}else{
	        			//取出每个短信包中的短信内容，并且用编码base64进行转码,字符集为gbk  
	        			text=Base64Util.encrypt(freeMarkerToString("mobile_blank_template.ftl",content),"gbk");
	        		}	        		     		
	        		mobilecode="*|".concat(phone).concat("|").concat(text).concat("|SvrType|P1|P2|||0|0|0|0|1");
					multixmt+=mobilecode+',';
				}
				//干掉最后一个逗号
				multixmt=multixmt.substring(0, multixmt.length()-1);
        }
		}
		return multixmt;
	}
	/**
	 * 短信群发不同内容Webservice接口
	 * @param map  短信配置信息与内容
	 * @return
	 */
	public static String sendNotEqualContent(String userid,String pass,String modelcontent,JSONArray contents) throws Exception{
        //初始化短信包
		String multixmt="";
		try {
			multixmt=initMultixmt(StringUtil.getString(modelcontent), contents);
		} catch (Exception e) {
			return "-998";
		}		
		// 直接引用远程的wsdl文件
		Service service = new Service();
		try {
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(Msnc.mw_url.concat("?wsdl"));
			call.setOperationName(Msnc.mw_not_equal_content_method);// WSDL里面描述的接口名称
			call.addParameter("userId",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
			call.addParameter("password",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
			call.addParameter("multixmt",org.apache.axis.encoding.XMLType.XSD_DATE,javax.xml.rpc.ParameterMode.IN);// 接口的参数
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
			//发送短信
			return (String) call.invoke(new Object[] { userid, DesUtils.decode(pass),multixmt});	
		} catch (Exception e) {
			return "-999";// TODO Auto-generated catch block
			
		}
	}
	
	public static String initMobileCode(String content,String modecontent){
		 //将模版构建成一个文件
	    File file=null;
	    if(!"".equals(modecontent))
		file=new File(Msnc.template_path.concat("/mobile_blank_template.ftl"));
		//将模版内容构建为发送内容
		try {
			return initTemplate(content, modecontent, file,"mobile_blank_template.ftl");
		} catch (Exception e) {
			return "-998";
		}	
	}
	  /**
	   * 移动梦网群发相同内容http接口
	   * @param userId 梦网登陆账号
	   * @param pass 密码
	   * @param phone 手机号
	   * @param content 短信内容
	   * @param modecontent 模版内容
	   * @param count 手机号码数
	   * @return
	   */
	  public static String mwHttpSendEqualContent(String userId,String pass,String phone,String content,String modecontent,int count) throws Exception{
		  String str="-999";
		  //先初始化短信内容
		  String text=initMobileCode(content, modecontent);
		  //如果模版初始化就直接返回
		  if(text.equals("-998"))return text;
		  //构建梦网url
		  String url=Msnc.mw_url.concat("/").concat(Msnc.mw_equal_content_method);
		  //构建httpclient
		  CloseableHttpClient client=PoolingHttpClientUtil.getHttpClient(url) ;
		  //构建httppost
		  HttpPost post=new HttpPost(url);
		  //初始化头部
		  PoolingHttpClientUtil.config(post);
		  //构建参数队列
		  List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		  formparams.add(new BasicNameValuePair("userId",userId)); 
		  formparams.add(new BasicNameValuePair("password",DesUtils.decode(pass)));
		  formparams.add(new BasicNameValuePair("pszMobis",phone));
		  formparams.add(new BasicNameValuePair("pszMsg",text));
		  formparams.add(new BasicNameValuePair("iMobiCount",String.valueOf(count)));	
		  //构建传输对象
		  UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formparams, "UTF-8");  
		  post.setEntity(uefEntity);
		  CloseableHttpResponse response=null;
		  //执行get方法
		  try {
			  response =client.execute(post,HttpClientContext.create());
		      } catch (Exception e) {
			  //e.printStackTrace();
		      //当断网时，接口抛出的异常信息为：connect timed out
			  if(e.getMessage().indexOf("connect timed out")!=0){
				  str="-1000";
			  }
			  log.debug(ExceptionUtil.println(e));
		  }
		  int state=0;
		  if(null!=response)state=response.getStatusLine().getStatusCode();
		  if(state==200){
			  str=XmlUtil.getRootNodeText(EntityUtils.toString(response.getEntity()));	  
		  }
		  //释放response链接
		  if(response!=null){
			  response.close();
			  post.abort();
		  }	  
		  return str;
	   }
	  
	  /**
	   * 移动梦网群发相同内容http接口
	   * @param userId 梦网登陆账号
	   * @param pass 密码
	   * @param phone 手机号
	   * @param content 短信内容
	   * @param modecontent 模版内容
	   * @param count 手机号码数
	   * @return
	   */
	  public static String mwHttpSendNotEqualContent(String userid,String pass,String modelcontent,JSONArray contents) throws Exception{
		  //初始化短信包
		   String multixmt="";
		   try {
				multixmt=initMultixmt(StringUtil.getString(modelcontent), contents);
			} catch (Exception e) {
				e.printStackTrace();
				return "-998";
			}		
		  //构建梦网url
		  String url=Msnc.mw_url.concat("/").concat(Msnc.mw_not_equal_content_method);
		  //构建httpclient
		  CloseableHttpClient client=PoolingHttpClientUtil.getHttpClient(url) ;
		  //构建httppost
		  HttpPost post=new HttpPost(url);
		  //初始化头部
		  PoolingHttpClientUtil.config(post);
		  //构建参数队列
		  List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		  formparams.add(new BasicNameValuePair("userId",userid)); 
		  formparams.add(new BasicNameValuePair("password",DesUtils.decode(pass)));
		  formparams.add(new BasicNameValuePair("multixmt",multixmt));
		  //构建传输对象
		  UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formparams, "UTF-8");  
		  post.setEntity(uefEntity);
				//执行get方法
		  CloseableHttpResponse response=null;
		  String str="-999";
		  try {
			  response =client.execute(post,HttpClientContext.create());
		      } catch (Exception e) {
		      //当断网时，接口抛出的异常信息为：connect timed out
			  if(e.getMessage().indexOf("connect timed out")!=0){
				  str="-1000";
			  }
			  log.debug(ExceptionUtil.println(e));
		  }
		  int state=0;
		  if(null!=response)state=response.getStatusLine().getStatusCode();
		  if(state==200){
			  str=XmlUtil.getRootNodeText(EntityUtils.toString(response.getEntity()));	  
		  }
		  //释放response链接
		  if(response!=null){
			  response.close();
			  post.abort();
		  }	  
		  return str;
	   }
}	
