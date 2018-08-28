package com.cn.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import net.sf.json.JSONObject;

/**
 * 
 * @author yangjing
 * @date 2016年11月23日
 * @describe 用httpclient接口使用，为防止同时并发数据量太大， 特别使用httpclient连接池，以减少损耗，加大吞吐量。
 *           并且对数据进行加密处理
 */
public class HttpConnectionUtil {

	private static CloseableHttpClient httpClient = null;

	private static int timeOut = 10000;// 链接超时时间，10秒

	private static int maxTotal = 400;// 设置最大连接数增加

	private static int maxPerRoute = 20;// 设置增加路由器基础的链接

	private static int maxRoute = 50;// 设置增加目标的主机最大连接数

	private static void config(HttpRequestBase base) {

		// 设置头部
		base.setHeader("User-Agent", "Mozilla/5.0");
		base.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
		base.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
		base.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");
		// 设置请求的超时设置
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut)
				.setSocketTimeout(timeOut).build();
		base.setConfig(config);
	}

	/**
	 * @param url必须是完整的路径，必须以http开头，比如：http://open.kocla.com
	 * @return httpClient
	 * @deprecated:获取httpClient对象
	 */
	public static CloseableHttpClient getHttpClient(String url) {

		// 创建httpclient对象
		if (httpClient == null) {
			httpClient = createHttpClient(url);
		}
		return httpClient;
	}

	// 创建HttpClient对象
	public static CloseableHttpClient createHttpClient(String url) {

		// 设定socket工厂类可以和指定的协议（Http、Https）联系起来
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf).register("https", sslsf).build();
		// PoolingHttpClientConnectionManager连接池管理类，可以为多线程提供http链接请求
		// 当请求一个新的连接时，如果连接池有有可用的持久连接，连接管理器就会使用其中的一个，而不是再创建一个新的连接。
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// PoolingHttpClientConnectionManager维护的连接数在每个路由基础和总数上都有限制。默认，每个路由基础上的连接不超过2个，总连接数不能超过20
		cm.setDefaultMaxPerRoute(maxPerRoute);
		// 创建host
		HttpHost httpHost = new HttpHost(url);
		// 将目标主机的最大连接数增加
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
		// 请求重试处理与链接存活策略
		HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException arg0, int arg1, HttpContext arg2) {
				if (arg1 >= 5)
					return false;// 如果链接重试了3次就放弃
				if (arg0 instanceof NoHttpResponseException)
					return true;// 如果服务器丢掉了连接，那么就重试
				if (arg0 instanceof SSLHandshakeException)
					return false;// 不要重试SSL握手异常
				if (arg0 instanceof InterruptedIOException)
					return false;// 超时
				if (arg0 instanceof UnknownHostException)
					return false;// 目标服务器不可达
				if (arg0 instanceof ConnectTimeoutException)
					return false;// 连接被拒绝
				if (arg0 instanceof SSLException)
					return false;// SSL握手异常

				HttpClientContext text = HttpClientContext.adapt(arg2);
				HttpRequest request = text.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest))
					return true;
				return false;
			}
		};
		// 重定向处理
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		// 定义链接存活策略
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(handler)
				.setRedirectStrategy(redirectStrategy).build();
		return httpclient;
	}

	/**
	 * @deprecated：post方法，当参数为json字符串时调用
	 * @param url:接口路径
	 * @param jsonStr：参数，标准的json字符串
	 * @return 返回json对象
	 */
	public static JSONObject post(String url, String jsonStr) {

		System.out.println(jsonStr);
		JSONObject object = new JSONObject();
		// 如果url不空，就直接调用对象
		if (null != url && !"".equals(url)) {
			// 将url路径方法实体化
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-type", "application/json; charset=utf-8");
			// 设置请求超时
			config(post);
			// 获取httpclient
			httpClient = getHttpClient(url);
			CloseableHttpResponse response = null;
			try {
				post.setEntity(new StringEntity(DesUtils.encode(jsonStr), Charset.forName("UTF-8")));
				// 执行get方法
				response = httpClient.execute(post, HttpClientContext.create());
				System.out.println(response.toString());
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
				StringBuffer buf = new StringBuffer();
				String line;
				while (null != (line = reader.readLine())) {
					buf.append(line);
				}
				// 关闭流
				reader.close();
				if (buf.length() != 0 && !buf.equals("")) {
					object = JSONObject.fromObject(buf.toString());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 释放链接
				try {
					if (response != null)
						response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					post.abort();
				}
			}
		}
		return object;
	}

	/**
	 * @deprecated：post方法，当参数为Map集合时调用
	 * @param url:接口路径
	 * @param map
	 * @return
	 */
	public static JSONObject post(String url, Map map) {

		JSONObject object = new JSONObject();
		if (isParamEmpty(url, map)) {
			return post(url, JSONObject.fromObject(map));
		}
		return object;
	}

	/**
	 * @deprecated：post方法，当参数为JSONObject集合时调用
	 * @param url:接口路径
	 * @param JSONObject
	 * @return
	 */
	public static JSONObject post(String url, JSONObject json) {

		JSONObject object = new JSONObject();
		if (isParamEmpty(url, json)) {
			return post(url, json.toString());
		}
		return object;
	}
	
	/**
	 * @deprecated：判断参数是否为空
	 * @param url:接口路径
	 * @param obj：对象
	 * @return boolean
	 */
	private static boolean isParamEmpty(String url, Object obj) {

		if (null != url && !"".equals(url) && obj != null) {
			return true;
		}
		return false;
	}

		public static void main(String[] args) {
			String data="{\"touser\": \"@all\",\"toparty\": \"@all \",\"totag\": \"@all \",\"agentid\": \"1\",\"content\": \"接口测试拼接成功了呢\",\"corpid\":\"wxf37238cff8a72479\",\"corpsecret\":\"Ni4iHTMVNmMV3eGNn5pn0ikVaI1XadEK4Xo2CPsjaS_FwWI3eiooh52V5TK2IacA\"}";
		
			JSONObject reponceJson =  HttpConnectionUtil.post("http://10.201.235.11:8080/msnc/interface/wechat/sendCorpMessage",data);
			System.out.println(reponceJson.toString());
		}
	
}
