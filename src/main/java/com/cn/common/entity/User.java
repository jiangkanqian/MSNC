package com.cn.common.entity;

import java.io.Serializable;

/**
 * 
 * @author yangjing
 * @date 2016年12月6日
 * @describe 用户模型
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String userId;//用户id
	private String userName;//用户姓名
	private String phone;//用户电话
	private String email;//用户邮件
	private String account;//用户账号
	private String passwrod;//密码
	private String sex;//性别
	private String empName;//部门名字
	private String userState;//用户状态
	private String createTime;//创建时间
	private String roleId;//角色id

	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		
		return userId;
	}

	public void setUserId(String userId) {
		
		this.userId = userId;
	}
	

	public String getUserName() {
		
		return userName;
	}

	public void setUserName(String userName) {
		
		this.userName = userName;
	}

	public String getPhone() {
		
		return phone;
	}

	public void setPhone(String phone) {
		
		this.phone = phone;
	}

	public String getEmail() {
		
		return email;
	}

	public void setEmail(String email) {
		
		this.email = email;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		
		this.account = account;
	}

	public String getPasswrod() {
		
		return passwrod;
	}

	public void setPasswrod(String passwrod) {
		
		this.passwrod = passwrod;
	}

	public String getSex() {
		
		return sex;
	}

	public void setSex(String sex) {
		
		this.sex = sex;
	}

	public String getEmpName() {
		
		return empName;
	}

	public void setEmpName(String empName) {
		
		this.empName = empName;
	}

	public String getUserState() {
		
		return userState;
	}

	public void setUserState(String userState) {
		
		this.userState = userState;
	}

	public String getCreateTime() {
		
		return createTime;
	}

	public void setCreateTime(String createTime) {
		
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", phone=" + phone + ", email=" + email
				+ ", account=" + account + ", passwrod=" + passwrod + ", sex=" + sex + ", empName=" + empName
				+ ", userState=" + userState + ", createTime=" + createTime + "]";
	}
	
}
