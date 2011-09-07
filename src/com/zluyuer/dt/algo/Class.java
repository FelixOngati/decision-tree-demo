package com.zluyuer.dt.algo;

/**
 * 分类
 * @author 陆遥
 *
 */
public class Class {

	/**
	 * 数据类型
	 */
	public DataType type = DataType.TYPE_TEXT;
	/**
	 * 名称
	 */
	public String name = "[Unknown]";
	
	/**
	 * 构造方法
	 * @param name 名称
	 * @param type 数据类型
	 */
	public Class(String name, DataType type) {
		this.name = name;
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		Class c = (Class)o;
		return name.equals(c.name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}