package com.cn.common.sys.tools;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.cn.common.util.DesUtils;


/***
 * 解析加密的properties配置文件，并且重新赋值到spring配置文件
 * @author chenkai
 * date:2016-11-8
 */
public class EncryptPropertyPlaceholderConfigurer extends  PropertyPlaceholderConfigurer implements InitializingBean{

	private List<String> encryptPropNames;

	public List<String> getEncryptPropNames() {
		return encryptPropNames;
	}

	public void setEncryptPropNames(List<String> encryptPropNames) {
		this.encryptPropNames = encryptPropNames;
	}
	
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		// TODO Auto-generated method stub
		if(encryptPropNames.contains(propertyName))return DesUtils.decode(propertyValue);
		return super.convertProperty(propertyName, propertyValue);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
