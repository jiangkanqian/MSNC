package com.cn.common.entity;

import java.io.Serializable;

/***
 * 系统参数表
 * @author chenkai
 * date:2016-11-8
 */
public class SysParameter implements Serializable{
	
	 private static final long serialVersionUID = 1L;  
	  //主键ID
      private Integer sysid;
      //参数名称
      private String name;
      //参数类型
      private String type;
      //参数值
      private String value;
      //参数描述
      private String msg;
	public Integer getSysid() {
		return sysid;
	}
	public void setSysid(Integer sysid) {
		this.sysid = sysid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
      
      
}
