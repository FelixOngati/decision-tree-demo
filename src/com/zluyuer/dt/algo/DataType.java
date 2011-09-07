package com.zluyuer.dt.algo;

/**
 * 数据类型
 * @author 陆遥
 *
 */
public class DataType {

	/**
	 * 数据类型常量：文本
	 */
	public static DataType TYPE_TEXT = new DataType("text");
	/**
	 * 数据类型常量：整数
	 */
	public static DataType TYPE_INT = new DataType("int");
	/**
	 * 数据类型常量：浮点数
	 */
	public static DataType TYPE_FLOAT = new DataType("float");
	
	/**
	 * 名称
	 */
	public String name = "[Unknown]";
	
	/**
	 * 构造方法
	 * @param name 名称
	 */
	public DataType(String name) {
		this.name = name;
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
		DataType c = (DataType)o;
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
