package com.zluyuer.dt.algo;

/**
 * 数据属性
 * @author 陆遥
 *
 */
public class Attribute {
	
	/**
	 * 名称
	 */
	public String name = "[Unknown]";
	/**
	 * 数据类型
	 */
	public DataType type = DataType.TYPE_TEXT;
	/**
	 * 索引
	 */
	public int index;
	/**
	 * 是否离散
	 */
	public boolean discrete = true;
	
	/**
	 * 构造方法
	 * @param name 名称
	 * @param type 数据类型
	 * @param index 索引
	 */
	public Attribute(String name, DataType type, int index) {
		this.name = name;
		this.type = type;
		this.index = index;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
}