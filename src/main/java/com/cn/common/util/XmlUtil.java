package com.cn.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 获取xml字符串节点的内容
 * @author chenkai
 * date:2017-01-11
 */
public final class XmlUtil {

	/**
	 * 获取xml字符串中节点的内容
	 * <?xml version="1.0" encoding="utf-8"?><string xmlns="http://tempuri.org/">1560254005601918414</string>
	 * @param xmlStr
	 * @return
	 */
	public static String getRootNodeText(String xmlStr) throws Exception{
		//将xml字符串内容转为xml 
		Document doc = DocumentHelper.parseText(xmlStr);
		Element rootElt = doc.getRootElement(); // 获取根节点
		return rootElt.getText();
	}
}
