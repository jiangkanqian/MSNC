package com.cn.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author chen.kai
 * @date   2016-11-12
 * @deprecated:登陆后调用httpclient接口使用，为防止同时并发数据量太大，
 *             特别使用httpclient连接池，以减少损耗，加大吞吐量,同时可
 *             以启用多线程与增加上传与下载功能
 * @see:  调用时  PlooingHttpClientUtil.方法（）；
 */
public final class  PoolingHttpClientUtil {
	
	static Logger logger=LoggerFactory.getLogger(PoolingHttpClientUtil.class);
	
	private static CloseableHttpClient   httpClient =null;
	
	private static int timeOut=3000;//链接超时时间，5秒
	
	private static int socket_time_out=2000;//设置数据等待超时时间
	
	private static int connect_time_out=2000;//设置请求超时时间
	
	private static int maxTotal=400;//设置最大连接数增加
	
	private static int maxPerRoute=20;//设置增加路由器基础的链接
	
	private static int maxRoute=50;//设置增加目标的主机最大连接数
	
	
	
	public static void config(HttpRequestBase request){
		//设置头部
		 request.setHeader("User-Agent", "Mozilla/5.0");
	     request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	     request.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
	     request.setHeader("Accept-Encoding", "gzip, deflate");
	     request.setHeader("Connection", "keep-alive");
	     request.setHeader("Pragram", "no-cache");
	     request.setHeader("Cache-Control", "no-cache");
		 request.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");
		//设置请求的超时设置
		RequestConfig config=RequestConfig.custom().setConnectionRequestTimeout(timeOut).
				setConnectTimeout(connect_time_out).setSocketTimeout(socket_time_out).build();	
		request.setConfig(config);
	}
	
	
	/**
	 * @param url必须是完整的路径，必须以http开头
	 * @return httpClient
	 * @deprecated:获取httpClient对象
	 */
	public  static CloseableHttpClient  getHttpClient(String url) { 
        //创建httpclient对象
        if(httpClient==null){
        	httpClient=createHttpClient(url);	
        }
        return httpClient;   
    }  
	
	//创建HttpClient对象	
	public static CloseableHttpClient createHttpClient(String url){
		//设定socket工厂类可以和指定的协议（Http、Https）联系起来
		 ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		 LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		 Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", plainsf)
	                .register("https", sslsf).build();
		 //PoolingHttpClientConnectionManager连接池管理类，可以为多线程提供http链接请求
		 //当请求一个新的连接时，如果连接池有有可用的持久连接，连接管理器就会使用其中的一个，而不是再创建一个新的连接。
		 PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		 // 将最大连接数增加
	     cm.setMaxTotal(maxTotal);
	     //PoolingHttpClientConnectionManager维护的连接数在每个路由基础和总数上都有限制。默认，每个路由基础上的连接不超过2个，总连接数不能超过20
	     cm.setDefaultMaxPerRoute(maxPerRoute);
	     //创建host
	     HttpHost httpHost = new HttpHost(url);
	     // 将目标主机的最大连接数增加
	     cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
	     //请求重试处理与链接存活策略
	     HttpRequestRetryHandler handler=new HttpRequestRetryHandler() {	
			@Override
			public boolean retryRequest(IOException arg0, int arg1, HttpContext arg2) {
				if(arg1>2)return false;//如果链接重试了3次就放弃
				if(arg0 instanceof NoHttpResponseException) return true;// 如果服务器丢掉了连接，那么就重试
				if(arg0 instanceof SSLHandshakeException)return false;// 不要重试SSL握手异常
				if(arg0 instanceof InterruptedIOException)return false;// 超时
				if(arg0 instanceof UnknownHostException) return false;// 目标服务器不可达
				if(arg0 instanceof ConnectTimeoutException) return false;// 连接被拒绝
				if(arg0 instanceof SSLException)return false;// SSL握手异常
                
				HttpClientContext text=HttpClientContext.adapt(arg2);
				HttpRequest request=text.getRequest();
				//如果请求是幂等的，就再次尝试
				if(!(request instanceof HttpEntityEnclosingRequest)) return true;
				return false;
			}
		};
		//重定向处理
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy(); 
		//定义链接存活策略
		CloseableHttpClient httpclient=HttpClients.custom().setConnectionManager(cm).setRetryHandler(handler).setRedirectStrategy(redirectStrategy).build();
		return httpclient;
	}
	
	
	//关闭httpclient，此方法在连接池机制中不实用，如果请求调用此方法后，则该url连接池会处于永久失效状态
	public static void disconnect() {  
        try {
        	if(httpClient!=null)httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
	
	/**
	 * 处理返回值部分
	 * @param response
	 * @param json
	 * @throws IOException
	 */
	private static JSONObject executHttp(CloseableHttpResponse response,JSONObject object) throws IOException{
		int status = response.getStatusLine().getStatusCode();
		logger.info("本次请求返回码为："+status);
		//200-300说明程序执行成功
		if(status>199&&status<300){
			HttpEntity entity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),StandardCharsets.UTF_8));
            StringBuffer buf=new StringBuffer();
            String line;
			while (null != (line = reader.readLine())) {
				buf.append(line);
			}
			//关闭流
			reader.close();
			 EntityUtils.consume(entity);
			if(buf.length()!=0&&!buf.equals("")){
				object=JSONObject.fromObject(buf.toString());
			}
		}
		else
		{
			object.put("retCode","0");
			object.put("retMsg", "web路径、请求方式或者服务器内部错误..");
		}
		return object;
	}
	//get方法
	public static JSONObject get(String url) throws IOException{
		JSONObject object =new JSONObject();
		if(null!=url&&!"".equals(url)){
		//将url路径方法实体化
		HttpGet  get=new HttpGet(url);
		//设置请求超时
		config(get);
		//获取httpclient
		httpClient=getHttpClient(url);
        CloseableHttpResponse response =null;
		try {
			//执行get方法
			response =httpClient.execute(get,HttpClientContext.create());
			object=	executHttp(response, object);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		finally{
			//释放get链接
			if(response!=null)response.close();
			get.abort();
			
		  }		
		}
		return object;
	}
	
	/**
	 * @deprecated：post方法，当参数为json字符串时调用,此处既可以处理复杂的json格式，也可以处理简单的json格式
	 * @param url:接口路径
	 * @param jsonStr：参数，json字符串
	 * @return 返回json对象
	 */
	public static JSONObject post(String url,String jsonStr) throws IOException{
		JSONObject object =new JSONObject();	
		//如果url不空，就直接调用对象
		if(null!=url&&!"".equals(url)){	
		//将url路径方法实体化
		HttpPost  post=new HttpPost(url);
		//设置请求超时
		config(post);
		//获取httpclient
		httpClient=getHttpClient(url);
        CloseableHttpResponse response =null;
		try {
			post.addHeader("Content-type","application/json; charset=utf-8");  
			post.setHeader("Accept", "application/json"); 
			post.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
			//执行post方法
			response =httpClient.execute(post,HttpClientContext.create());
			object=executHttp(response, object);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}finally{
			//释放get链接
			if(response!=null)
				response.close();
			    post.abort();
		  }		
		}
		return object;	
	}
	
	/**
	 * @deprecated：post方法，当参数为Map集合时调用，处理简单map数据时使用
	 * @param url:接口路径
	 * @param map
	 * @return
	 */
		public static JSONObject post(String url,Map map) throws IOException{
			JSONObject object =new JSONObject();		
			//如果url不空，就直接调用对象
			if(null!=url&&!"".equals(url)){	
			//将url路径方法实体化
			HttpPost  post=new HttpPost(url);
			//设置请求超时
			config(post);
			//获取httpclient
			httpClient=getHttpClient(url);		
			// 创建参数队列
			Iterator it=map.entrySet().iterator();
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			//获取json里面的数据
			while (it.hasNext()) {
				Map.Entry entry=(Map.Entry) it.next();
				//对null值进行处理
				formparams.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue()+""));  
			}
			//构建传输对象
		    UrlEncodedFormEntity uefEntity;  
	        CloseableHttpResponse response =null;
			try {
				uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8"); 
				post.addHeader("Content-type","application/json; charset=utf-8");  
				post.setHeader("Accept", "application/json"); 
				post.setEntity(uefEntity);
				//执行get方法
				response =httpClient.execute(post,HttpClientContext.create());
				object=executHttp(response, object);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}finally{
				//释放response链接
				if(response!=null)response.close();
				post.abort();

			  }		
			}
			return object;	
		}
	
	/**
	 * @deprecated：post方法，当参数为JSONObject集合时调用，处理简单json时使用
	 * @param url:接口路径
	 * @param JSONObject
	 * @return
	 */
	public static JSONObject post(String url,JSONObject json) throws IOException{
		JSONObject object =new JSONObject();	
		//如果url不空，就直接调用对象
		if(null!=url&&!"".equals(url)){
		//将url路径方法实体化
		HttpPost  post=new HttpPost(url);
		//设置请求超时
		config(post);
		//获取httpclient
		httpClient=getHttpClient(url);
		// 创建参数队列
		Iterator it=json.keys();
		//StringEntity entity = new StringEntity(json.toString(), "utf-8");
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		//获取json里面的数据
		while (it.hasNext()) {
			String key=it.next().toString();
			formparams.add(new BasicNameValuePair(key,json.get(key)+""));  
		}
		//构建传输对象
	    UrlEncodedFormEntity uefEntity;  
        CloseableHttpResponse response =null;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8"); 
			post.addHeader("Content-type","application/json; charset=utf-8");  
			post.setHeader("Accept", "application/json");  
			post.setEntity(uefEntity);
			//执行get方法
			response =httpClient.execute(post,HttpClientContext.create());
			object=executHttp(response, object);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}finally{
			//释放get链接
			if(response!=null)response.close();
			post.abort();
		  }		
		}
		return object;	
	}
	
	/**
	 * @deprecated:上传文件
	 * @param url   文件服务器地址 
	 * @param file
	 * @return
	 */
	public static Object upload(String url,File file,String systemid) throws Exception{
		//如果文件不为空，就开始上传
		JSONObject object=new JSONObject();
		if(!url.isEmpty()&&file.exists()){
			HttpPost post=new HttpPost(url);
			// 把文件转换成流对象FileBody
			FileBody fb=new FileBody(file);
			HttpEntity entity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					// 相当于<input type="file" name="file"/>
					.addPart("file", fb)
					.addPart("systemid", new StringBody(systemid, ContentType.create("text/plain", Consts.UTF_8)))
					.setCharset(CharsetUtils.get("utf-8")).build();		
			//将对象放入到post请求中
			post.setEntity(entity);
			//获取httpclient对象
			httpClient=getHttpClient(url);
			//执行响应
			CloseableHttpResponse response=httpClient.execute(post, HttpClientContext.create());
			object=executHttp(response, object);
			if(response!=null)response.close();
			post.abort();	
		}
		return object;
	}
	
	/**
	 * @deprecated:批量上传文件
	 * @param url   文件服务器地址 
	 * @param file
	 * @return
	 */
	public static Object upload(String url,File[] files,String systemid) throws Exception{
		//如果文件不为空，就开始上传
		JSONObject object =new JSONObject();	
		if(!url.isEmpty()&&files.length!=0){
			HttpPost post=new HttpPost(url);
			// 把文件转换成流对象BinaryBody
			MultipartEntityBuilder builder=MultipartEntityBuilder.create();
			//对entity添加参数
			for (int i = 0; i < files.length; i++) {
				// 把文件转换成流对象FileBody
				builder.addBinaryBody("file"+i,files[i]);
			}
			StringBody body = new StringBody(systemid, ContentType.create("text/plain", Consts.UTF_8));
			builder.addPart("systemid", body);
//			//处理String参数
//			if(null!=map){
//				if(!map.isEmpty()){
//					Iterator it=map.entrySet().iterator();
//					while (it.hasNext()) {
//						Map.Entry entry=(Map.Entry) it.next();
//						//对null值进行处理
//						StringBody body = new StringBody(entry.getValue()+"", ContentType.create("text/plain", Consts.UTF_8));
//						builder.addPart(entry.getKey().toString(), body);
//					}
//				}
//			}			
			//设置浏览器兼容模式以及编码格式
			HttpEntity entity=builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(CharsetUtils.get("utf-8")).build();		
			//将对象放入到post请求中
			post.setEntity(entity);
			//获取httpclient对象
			httpClient=getHttpClient(url);
			//执行响应
			CloseableHttpResponse response=httpClient.execute(post, HttpClientContext.create());
			object=executHttp(response, object);
			if(response!=null)response.close();
			post.abort();	
		}
		return object;
	}
	/**
	 * @deprecated:下载文件
	 * @param url 网络文件路径：http://www.baidu.com/imgs/test.jpg
	 * @param localPath：下载好的文件存放的本地路径,路径格式为  D:/img/
	 * @return
	 */
	public static boolean download(String url,String localPath) throws IOException{
		if(!url.isEmpty()){
			HttpGet get=new HttpGet(url);
			//获取httpclient对象
			httpClient=getHttpClient(url);
			 HttpResponse response=null;
			try {
				response = httpClient.execute(get,HttpClientContext.create());
				int status = response.getStatusLine().getStatusCode();
				logger.info(url+"的请求的响应状态为："+status);
				//200-300说明程序执行成功
				if(status>199&&status<300){
					HttpEntity entity = response.getEntity();
					InputStream in = entity.getContent();
					//获取网络文件的文件名称
					String filename=url.substring(url.lastIndexOf("/")+1);
					File file=new File(localPath);
					if(!file.exists())file.mkdirs();
					FileOutputStream fos=new FileOutputStream(file+""+filename);
					int b=0;
					byte bt[]=new byte[1024];
					while ((b=in.read(bt))!=-1) {
						  // 注意这里如果用OutputStream.write(buff)的话，图片会失真
						 fos.write(bt,0,b);	
					}
					fos.flush();
					fos.close();
					in.close();
					return true;
				}			
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
			   get.abort();
			}
			
		}
		return false;
	}
	
	/**
	 * 普通post请求方式，Content-Type: application/x-www-form-urlencoded
	 * @param url 请求地址
	 * @param map 参数
	 * @return
	 * @throws IOException
	 */
	public static String httppost(String url,Map map) throws IOException{		
		//如果url不空，就直接调用对象
		if(null!=url&&!"".equals(url)){	
		//将url路径方法实体化
		HttpPost  post=new HttpPost(url);
		//设置请求超时
		config(post);
		//获取httpclient
		httpClient=getHttpClient(url);		
		// 创建参数队列
		Iterator it=map.entrySet().iterator();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		//获取json里面的数据
		while (it.hasNext()) {
			Map.Entry entry=(Map.Entry) it.next();
			//对null值进行处理
			formparams.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue()+""));  
		}
		//构建传输对象
	    UrlEncodedFormEntity uefEntity;  
        CloseableHttpResponse response =null;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8"); 
			post.setEntity(uefEntity);
			//执行get方法
			response =httpClient.execute(post,HttpClientContext.create());
			int status = response.getStatusLine().getStatusCode();
			//200-300说明程序执行成功
			if(status>199&&status<300){
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),StandardCharsets.UTF_8));
	            StringBuffer buf=new StringBuffer();
	            String line;
				while (null != (line = reader.readLine())) {
					buf.append(line);
				}
				//关闭流
				reader.close();
				EntityUtils.consume(entity);
				if(buf.length()!=0&&!buf.equals(""))
					return buf.toString();
			}
			else{
				return "请求失败！请求方式不对或者Web服务器内部错误！";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}finally{
			//释放response链接
			if(response!=null)response.close();
			post.abort();

		  }		
		}
		return "";	
	}
	
	//get方法
   public static String httpget(String url) throws IOException{
			if(null!=url&&!"".equals(url)){
			//将url路径方法实体化
			HttpGet  get=new HttpGet(url);
			//设置请求超时
			config(get);
			//获取httpclient
			httpClient=getHttpClient(url);
	        CloseableHttpResponse response =null;
			try {
				//执行get方法
				response =httpClient.execute(get,HttpClientContext.create());
				int status = response.getStatusLine().getStatusCode();
				//200-300说明程序执行成功
				if(status>199&&status<300){
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),StandardCharsets.UTF_8));
		            StringBuffer buf=new StringBuffer();
		            String line;
					while (null != (line = reader.readLine())) {
						buf.append(line);
					}
					//关闭流
					reader.close();
					EntityUtils.consume(entity);
					if(buf.length()!=0&&!buf.equals(""))
						return buf.toString();
				}
				else{
					return "-999";
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
			finally{
				//释放get链接
				if(response!=null)response.close();
				get.abort();	
			  }		
			}
			return "";
		}
 
   
   

	public static void testAsyncGorp(){
		
		long m=System.currentTimeMillis();
		JSONObject data=new JSONObject();
		JSONObject body=new JSONObject();
		JSONObject content=new JSONObject();
		content.put("user", "小丸子");
		body.put("touser", "@all");
		body.put("corpsecret", "Ni4iHTMVNmMV3eGNn5pn0ikVaI1XadEK4Xo2CPsjaS_FwWI3eiooh52V5TK2IacA");
		body.put("corpid", "wxf37238cff8a72479");
		body.put("toparty", "@all");
		body.put("totag", "@all");
		body.put("agentid", "1");
		body.put("content", content);
		body.put("modelcode", "msnc_wechat_001");
		data.put("seenumber", "205105f02d0f5s00202sdf");
		data.put("fromuser", "test");
		data.put("fromsystem", "plms");
		data.put("fromsystemid", "plms001");
		data.put("fromtime", "2016-1-26 09:02:30");
		data.put("interfaceurl", "http://localhost:8080/msnc/interface/wechat/sendCorpMessage");
		data.put("body", body);
		JSONObject object=new JSONObject();
		String aa=DesUtils.encode(data.toString());	
		//System.out.println(aa);
		object.put("data", aa);
		try {
			JSONObject json=PoolingHttpClientUtil.post("http://localhost:8081/msnc/interface/wechat/asyncCorpSendMessage",aa);
		System.out.println(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(data.toString());
		long n=System.currentTimeMillis();
		System.out.println(n-m);
	}
	public static void testAsyncApp(){
		long m=System.currentTimeMillis();
		JSONObject data=new JSONObject();
		JSONObject body=new JSONObject();
		JSONObject content=new JSONObject();
		content.put("user", "小丸子");
		body.put("appid", "wx8bdd2f24cea493e0");
		body.put("secret", "db9c5e264aaf221a0e20405f5c99d7cc");
		body.put("content",content );
		body.put("modelcode", "msnc_wechat_001");
		data.put("seenumber", "205105f02d0f5s00202sdf");
		data.put("fromuser", "test");
		data.put("fromsystem", "plms");
		data.put("fromsystemid", "plms001");
		data.put("fromtime", "2016-1-26 09:02:30");
		data.put("interfaceurl", "http://localhost:8080/msnc/interface/wechat/asyncAppSendMessage");
		data.put("body", body);
		data.put("callbackurl","http://localhost:8080/callback/Demo");
		JSONObject object=new JSONObject();
		String aa=DesUtils.encode(data.toString());	
		//System.out.println(aa);
		object.put("data", aa);
		try {
			JSONObject json=PoolingHttpClientUtil.post("http://localhost:8081/msnc/interface/wechat/sendAppSendMessage",aa);
		System.out.println(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(data.toString());
		long n=System.currentTimeMillis();
		System.out.println(n-m);
	}

//	
////	//邮件测试
	public static void testMail(String location) throws Exception {
		JSONObject data=new JSONObject();
		JSONObject context=new JSONObject();
		JSONObject body=new JSONObject();
		data.put("seenumber", "205105f02d0f5s00202sdf");
		data.put("fromuser", null);
		data.put("fromsystem", "plms");
		data.put("fromsystemid", "plms001");
		context.put("username", "张三");
		context.put("text", "测试邮件");context.put("month", "1");
		context.put("tables", "A001|电风扇|2017-01-01|世界地方就是看,|电视机||");
		body.put("content", context);
		body.put("to", "313633241@qq.com;ershijiulang@126.com");
		body.put("cc", "313633241@qq.com");
		body.put("subject", "11");
		body.put("filename", "日报2016年3月3日_项目通知中心组_陈凯_v1.0.xls");
		body.put("filepath", location);
		body.put("modelcode", "email_table_1");
		data.put("body", body);
		System.out.println(data.toString());
		JSONObject json=post("http://localhost:8080/msnc/interface/email/sendSingleTableHtmlMail",DesUtils.encode(data.toString()));
	}

	
	static void testAsyncMobile(){
		JSONObject data=new JSONObject();
		JSONObject body=new JSONObject();
		JSONArray array=new JSONArray();
		JSONObject content=new JSONObject();
		data.put("seenumber", "205105f02d0f5s00202sdf");
		data.put("fromuser", "test");
		data.put("fromsystem", "plms");
		data.put("fromsystemid", "plms001");
		body.put("modelcode", "msnc_mobile_001");
		content.put("phone", "13112345678");
		content.put("text", "你好这是一个测试短信...");
		content.put("user", "张三液");
		array.add(content);
		body.put("contents",array);
		data.put("body", body);
		System.out.println(data.toString());
		try {
			long m=System.currentTimeMillis();
			JSONObject json=post("http://localhost:8080/msnc/interface/mobile/sendNotEqualContent",DesUtils.encode(data.toString()));
			long n=System.currentTimeMillis();
			System.out.println("本次请求共耗费时间为："+(n-m));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void testMobile(String phone){
		JSONObject data=new JSONObject();
		JSONObject body=new JSONObject();
		JSONObject content=new JSONObject();
		data.put("seenumber", "205105f02d0f5s00202sdf");
		data.put("fromuser", "test");
		data.put("fromsystem", "plms");
		data.put("fromsystemid", "plms001");
		body.put("content", "你好这是一个测试短信...");
		body.put("phone", phone);
		body.put("count", "1");
		body.put("modelcode", "msnc_mobile_001");
		content.put("user", "张三");
		content.put("text", "你好这是一个测试短信...");
		body.put("content", content);
		data.put("body", body);
		System.out.println(data.toString());
		try {
			long m=System.currentTimeMillis();
			JSONObject json=post("http://localhost:8080/msnc/interface/mobile/sendAsyncEqualContent",DesUtils.encode(data.toString()));
			long n=System.currentTimeMillis();
			System.out.println("本次请求共耗费时间为："+(n-m));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
//	//文件上传
	public static JSONObject testfile() {
		String filepath="E:/日报2016年3月3日_项目通知中心组_陈凯_v1.0.xlsx";
		File f1=new File("E:/日报2016年3月3日_项目通知中心组_陈凯_v1.0.xlsx");
		File file=new File(filepath);
		File[] files=new File[]{file,f1}; 
		String systemid="msnc0101";
		String url="http://localhost:8080/msnc/file/uploadfile";
		try {
			JSONObject json=(JSONObject)upload(url, file, systemid);
			System.out.println(json);
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void testCache() throws IOException{
		JSONObject body=new JSONObject();
		body.put("aa", 1);
		post("http://localhost:8080/msnc/interface/email/testcache",DesUtils.encode(body.toString()));
	}
//	
//	
//	//文件上传
//	public static void main(String[] args) {
//		String filepath="d:/测试环境资料.txt";
//		File f1=new File("E:/web.xml");
//		File file=new File(filepath);
//		File[] files=new File[]{file,f1}; 
//		String systemid="msnc0101";
//		String url="http://localhost:8080/msnc/file/uploadfile";
//		try {
//			JSONObject json=(JSONObject)upload(url, file, systemid);
//			System.out.println(json);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) throws Exception{
//	testAsyncApp();
//		testAsyncGorp();
//		long m=System.currentTimeMillis();
//		for (int i = 0; i <10; i++) {
//			testMobile("13"+i+"12345670");
//		}	
//        long n=System.currentTimeMillis();
//        System.out.println(n-m);
//		//testMail();
	//testAsyncApp();
	//	testAsyncGorp();
//		long m=System.currentTimeMillis();
//		for (int i = 0; i <200; i++) {
//			testMobile("130"+(int)((Math.random()*9+1)*10000000));
//			System.out.println(i+"------------");
//		}	
//        long n=System.currentTimeMillis();
//        System.out.println(n-m);
		//testMobile("13012345678");
		//testAsyncMobile();
//		JSONObject json=testfile();
//		String location=json.getString("location");
//		testMail(location);
		testCache();
	}
	public static void test(){
		
		//select date_add('2008-08-08 12:00:00', interval -8 hour);计算小时分钟可以用到。
		
		//select date_add(@dt, interval 1 minute); 
		Calendar now = Calendar.getInstance();
		Date date=new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String da=dateFormat.format(date);
		System.out.println("年: " + now.get(Calendar.YEAR));
		System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
		String yearAndMonth=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH) + 1) + "-";
		String flowMonth=yearAndMonth+"01 00:00:00";
		String day=yearAndMonth+now.get(Calendar.DAY_OF_MONTH)+" 00:00:00";
		System.out.println(flowMonth+"-----"+da); 
		System.out.println(day+"-----"+da); 
		


	}
}
