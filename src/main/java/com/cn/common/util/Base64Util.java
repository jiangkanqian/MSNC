package com.cn.common.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

/**
 * @author chenkai
 * date 2016-10-27
 * base64加密和解密，Md5加密
 */
public class Base64Util {

	/**
	 * @param key 需要加密的值
	 * @return 返回加密的值
	 * @throws Exception
	 */
//	public static String encrypt(String key,String charset) throws Exception{
//		if(key.trim().isEmpty()||null==key) return "";
//		return new BASE64Encoder().encode(key.getBytes(charset));
//	}
	
	 public static String encrypt(String key,String charset) throws Exception{  
		 if(key.trim().isEmpty()||null==key) return "";
         return new String(Base64.encodeBase64(key.getBytes(charset)));  
     }  
	/**
	 * @param key 要解密的字符串
	 * @return 返回解密之后的数据
	 * @throws Exception
	 */
//	public static String encode(String key,String charset) throws Exception{
//		if(key.trim().isEmpty()||null==key) return "";
//		return new String(new BASE64Decoder().decodeBuffer(key),charset);
//	}
	 public static String encode(String key,String charset) throws Exception{  
         return new String(Base64.decodeBase64(key),charset);  
     }  
	
	/**
	 * @param key 要加密的字符串
	 * @return 返回加密的数据
	 * @throws Exception
	 */
//	public static String getByMd5Encrypt(String key) throws Exception{
//		if(key.trim().isEmpty()||null==key) return "";
//		 //确定计算方法
//        MessageDigest md5=MessageDigest.getInstance("MD5");
//        BASE64Encoder base64 = new BASE64Encoder();
//        //加密后的字符串
//        return base64.encode(md5.digest(key.getBytes("utf-8")));
//	}
	public static void main(String[] args) throws Exception{
		String key="你好";
		System.out.println(encrypt(key,"gbk"));//liIxdK1GlAs=
		
		System.out.println(encode("xOO6ww==","gbk"));
	}
}
