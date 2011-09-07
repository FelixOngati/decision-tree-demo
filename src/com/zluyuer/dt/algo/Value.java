package com.zluyuer.dt.algo;

/**
 * 值容器
 * @author 陆遥
 *
 */
public class Value {

	/**
	 * 值
	 */
	public Object value = new Object();
	/**
	 * 数据属性
	 */
	public Attribute attribute;
	/**
	 * 所属元组
	 */
	public Tuple tuple;	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return value.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return (o instanceof Value) && (value.equals(((Value)o).value));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}