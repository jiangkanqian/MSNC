package com.cn.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;  
import javax.crypto.Cipher;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  
import org.apache.commons.codec.digest.DigestUtils;

import com.cn.common.sys.bean.Msnc;

/**
 * 3Des加密公共类,使用枚举的单利模式，保证线程的安全性，调用方式：DesUtils.INSTANCE.method();
 * @author chenkai
 * date:2016-11-08
 */

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;  
      
    /*字符串 DESede(3DES) 加密 
     * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位 
     * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的 
     * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加 
     * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的 
     * 密钥 
     * 3DES加密过程为：C=Ek3(Dk2(Ek1(P))) 
     * 3DES解密过程为：P=Dk1((EK2(Dk3(C))) 
     * */  
public class DesUtils {  
    	         
        //keybyte为加密密钥，长度为24字节      
        //src为被加密的数据缓冲区（源）  
        public static  byte[] encryptMode(byte[] keybyte,byte[] src){  
             try {  
                //生成密钥  
                SecretKey deskey = new SecretKeySpec(keybyte, Msnc.Algorithm);  
                //加密  
                Cipher c1 = Cipher.getInstance(Msnc.Algorithm);  
                c1.init(Cipher.ENCRYPT_MODE, deskey);  
                return c1.doFinal(src);//在单一方面的加密或解密  
            } catch (java.security.NoSuchAlgorithmException e1) {  
                // TODO: handle exception  
                 e1.printStackTrace();  
            }catch(javax.crypto.NoSuchPaddingException e2){  
                e2.printStackTrace();  
            }catch(java.lang.Exception e3){  
                e3.printStackTrace();  
            }  
            return null;  
        }  
          
        //keybyte为加密密钥，长度为24字节      
        //src为加密后的缓冲区  
        public static  byte[] decryptMode(byte[] keybyte,byte[] src){  
            try {  
                //生成密钥  
                SecretKey deskey = new SecretKeySpec(keybyte, Msnc.Algorithm);  
                //解密  
                Cipher c1 = Cipher.getInstance(Msnc.Algorithm);  
                c1.init(Cipher.DECRYPT_MODE, deskey);  
                return c1.doFinal(src);  
            } catch (java.security.NoSuchAlgorithmException e1) {  
                // TODO: handle exception  
                e1.printStackTrace();  
            }catch(javax.crypto.NoSuchPaddingException e2){  
                e2.printStackTrace();  
            }catch(java.lang.Exception e3){  
                e3.printStackTrace();  
            }  
            return null;          
        }  
        
        /**
         * 将秘钥以24字节的方式进行排列
         * @return
         */
        public static byte[] hex(){  
            String f = DigestUtils.md5Hex(Msnc.deskey);  
            byte[] bkeys = new String(f).getBytes();  
            byte[] enk = new byte[24];  
            for (int i=0;i<24;i++){  
                enk[i] = bkeys[i];  
            }  
            return enk;  
        }  
        
        /**
         * 加密
         * @param key:要加密的字符串
         * @return 返回要加密的字符
         */
        public static String encode(String src){
        	if(src==null||src.trim()=="")return "";
        	Security.addProvider(new com.sun.crypto.provider.SunJCE());
			try {
				byte[] 	encoded = encryptMode(hex(),src.getBytes("utf-8"));
				return new BASE64Encoder().encode(encoded);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "";
        }
        /**
         * 解密
         * @param key 待解密的字符串
         * @return 返回解密后的数据
         */
        public static String decode(String key){
        	if(key==null||key.trim()=="")return "";
        	try {
				byte[] req=new BASE64Decoder().decodeBuffer(key);
				byte[] srcBytes = decryptMode(hex(),req);
				return new String(srcBytes,"utf-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return "";
        }
        public static void main(String[] args) {  
              String src="jdbc:mysql://10.201.224.127:3306/msncdev?useUnicode=true&characterEncoding=UTF-8";
              String str=DesUtils.encode("123456");
              System.out.println("加密后的数字："+str);
              
//              String  key="SUP7iDTgzNYcrpt75+yrJQ==";
//              String pss=DesUtils.decode(key);
//              System.out.println("解密后的数据是："+pss);
//              String t="{\"appid\":\"wx8bdd2f24cea493e0\",\"secret\":\"db9c5e264aaf221a0e20405f5c99d7cc\",\"content\":\"测试jar\"}";
//              String one="jdbc:mysql://10.201.235.10:3306/msnctest?useUnicode=true&characterEncoding=UTF-8";
//        	System.out.println(DesUtils.encode(t));

        }  
    }  