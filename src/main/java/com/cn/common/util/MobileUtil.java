package com.cn.common.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MobileUtil{
  static Logger logger = LoggerFactory.getLogger(PoolingHttpClientUtil.class);

  private static CloseableHttpClient httpClient = null;

  private static int timeOut = 3000;

  private static int socket_time_out = 2000;

  private static int connect_time_out = 2000;

  private static int maxTotal = 400;

  private static int maxPerRoute = 20;

  private static int maxRoute = 50;

  public static void config(HttpRequestBase request)
  {
    request.setHeader("User-Agent", "Mozilla/5.0");
    request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    request.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
    request.setHeader("Accept-Encoding", "gzip, deflate");
    request.setHeader("Connection", "keep-alive");
    request.setHeader("Pragram", "no-cache");
    request.setHeader("Cache-Control", "no-cache");
    request.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

    RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
      .setConnectTimeout(connect_time_out).setSocketTimeout(socket_time_out).build();
    request.setConfig(config);
  }

  public static CloseableHttpClient getHttpClient(String url)
  {
    if (httpClient == null) {
      httpClient = createHttpClient(url);
    }
    return httpClient;
  }

  public static CloseableHttpClient createHttpClient(String url)
  {
    ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
    LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
    Registry registry = RegistryBuilder.create().register("http", plainsf)
      .register("https", sslsf).build();

    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

    cm.setMaxTotal(maxTotal);

    cm.setDefaultMaxPerRoute(maxPerRoute);

    HttpHost httpHost = new HttpHost(url);

    cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

    HttpRequestRetryHandler handler = new HttpRequestRetryHandler()
    {
      public boolean retryRequest(IOException arg0, int arg1, HttpContext arg2) {
        if (arg1 > 2) return false;
        if ((arg0 instanceof NoHttpResponseException)) return true;
        if ((arg0 instanceof SSLHandshakeException)) return false;
        if ((arg0 instanceof InterruptedIOException)) return false;
        if ((arg0 instanceof UnknownHostException)) return false;
        if ((arg0 instanceof ConnectTimeoutException)) return false;
        if ((arg0 instanceof SSLException)) return false;

        HttpClientContext text = HttpClientContext.adapt(arg2);
        HttpRequest request = text.getRequest();

        if (!(request instanceof HttpEntityEnclosingRequest)) return true;
        return false;
      }
    };
    LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

    CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(handler).setRedirectStrategy(redirectStrategy).build();
    return httpclient;
  }

  public static String post(String phone, String content, int count) throws Exception
  {
    String url = "http://10.201.9.150:8082/emp/MWGate/wmgw.asmx/MongateCsSpSendSmsNew";

    CloseableHttpClient client = getHttpClient(url);

    HttpPost post = new HttpPost(url);

    PoolingHttpClientUtil.config(post);

    List formparams = new ArrayList();
    formparams.add(new BasicNameValuePair("userId", "SEND01"));
    formparams.add(new BasicNameValuePair("password", "123456"));
    formparams.add(new BasicNameValuePair("pszMobis", phone));
    formparams.add(new BasicNameValuePair("pszMsg", content));
    formparams.add(new BasicNameValuePair("iMobiCount", String.valueOf(count)));

    UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
    post.setEntity(uefEntity);

    CloseableHttpResponse response = client.execute(post, HttpClientContext.create());
    int state = response.getStatusLine().getStatusCode();
    String str = "-998";
    if (state == 200) {
      str = EntityUtils.toString(response.getEntity());
    }

    if (response != null) {
      response.close();
      post.abort();
    }
    return str;
  }

  public static String initMultixmt(List list) throws Exception {
    String phone = "";
    String text = "";
    String multixmt = "";
    for (int i = 0; i < list.size(); i++)
    {
      Map content = (Map)list.get(i);

      phone = StringUtil.getString(content.get("phone"));

      text = Base64Util.encrypt(content.get("content").toString(), "gbk");
      multixmt = "*|".concat(phone).concat("|").concat(text).concat("|SvrType|P1|P2|||0|0|0|0|1");
      multixmt = multixmt + multixmt.concat(",");
    }

    multixmt = multixmt.substring(0, multixmt.length() - 1);
    return multixmt;
  }

  public static String mwHttpSendNotEqualContent(List list) throws Exception
  {
    String multixmt = initMultixmt(list);

    String url = "http://10.201.9.150:8082/emp/MWGate/wmgw.asmx/MongateCsSpMultixMtSend";
    url =url+"?userId=SEND01&password=123456&multixmt="+multixmt;

    CloseableHttpClient client = PoolingHttpClientUtil.getHttpClient(url);


    HttpGet get = new HttpGet(url);

    PoolingHttpClientUtil.config(get);

//    List formparams = new ArrayList();
//    formparams.add(new BasicNameValuePair("userId", "SEND01"));
//    formparams.add(new BasicNameValuePair("password", "123456"));
//    formparams.add(new BasicNameValuePair("multixmt", multixmt));
//    get.set
//    UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
//    post.setEntity(uefEntity);

    CloseableHttpResponse response = client.execute(get, HttpClientContext.create());
    int state = response.getStatusLine().getStatusCode();
    String str = "-998";
    if (state == 200) {
      str = EntityUtils.toString(response.getEntity());
    }

    if (response != null) {
      response.close();
      get.abort();
    }
    return str;
  }

  public static void sendEqualsText(String phone, String content, int count) throws Exception {
    if ((phone == null) || ("".equals(phone.trim()))) System.out.println("手机号为空!");
    if ((content == null) || ("".equals(content.trim()))) System.out.println("短信内容为空！"); else
      System.out.println(post(phone, content, count));
  }

  public static String sendNotEqualsText(List list) throws Exception {
    if (list.size() > 0)
      return mwHttpSendNotEqualContent(list);
    else return "数据为空，发送失败！";
  }

  public static String encrypt(String key, String charset)
    throws Exception
  {
    if ((key.trim().isEmpty()) || (key == null)) return "";
    return new BASE64Encoder().encode(key.getBytes(charset));
  }

  public static String encode(String key, String charset)
    throws Exception
  {
    if ((key.trim().isEmpty()) || (key == null)) return "";
    return new String(new BASE64Decoder().decodeBuffer(key), charset);
  }

  public static void main(String[] args)
    throws Exception
  {
    List list = new ArrayList();
    Map map = new HashMap();
    map.put("phone", "13012345678");
    map.put("content", "测试短信1");
    Map map1 = new HashMap();
    map1.put("phone", "13112345678");
    map1.put("content", "测试短信2");
    list.add(map);
    list.add(map1);
    String one=sendNotEqualsText(list);
  }
}