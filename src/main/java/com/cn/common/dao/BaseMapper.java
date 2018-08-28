package com.cn.common.dao;

import java.util.List;

/***
 * 公共dao的接口类，使用范型表示,使用Mapper代理的方式实现
 * 基本步骤为 ： 
 * 1)编写mapper.xml映射文件
   2)编写Mapper接口，相当于dao接口 
   3)mybatis可以自动生成mapper接口的实现类代理对象
 * @author chenkai
 * @param <T>
 */
public interface BaseMapper<Model,PK>{

	/**
	 * 插入一个对象，并且返回成功失败的标志
	 * @param t
	 * @return
	 */
	public void insert(Model m);
	
	/**
	 * 删除一个对象，并且返回成功失败的标志
	 * @param t
	 * @return
	 */
	public void delete(PK pk);
	
	/**
	 * 批量删除，并且返回成功失败的标志
	 * @param t
	 * @return
	 */
	public void deleteAll(PK pk);
	
	/**
	 * 批量删除，并且返回成功失败的标志
	 * @param t
	 * @return
	 */
	public void update(PK pk);
	
	/**
	 * 获取一个单独的对象
	 * @param t
	 * @return
	 */
	public Model find(PK pk);
	
	/**
	 * 获取所有的对象的总条数
	 * @param t
	 * @return
	 */
	public int count(Model m);
	/**
	 * 获取所有的对象
	 * @return
	 */
	public List<Model> selectByParameterObject(Model m);
	
	/**
	 * 获取所有对象
	 */
	public List<Model> selectByParameterName(PK pk);

}
