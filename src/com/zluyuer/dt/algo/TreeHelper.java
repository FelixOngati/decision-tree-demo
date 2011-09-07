package com.zluyuer.dt.algo;

import java.io.PrintStream;
import java.util.Set;

/**
 * 决策树辅助类
 * @author 陆遥
 *
 */
public class TreeHelper {

	/**
	 * 根据文本和目标数据类型创建值
	 * @param data 文本
	 * @param type 目标数据类型
	 * @return 值
	 */
	public static Object createValue(String data, DataType type) {
		if (type.equals(DataType.TYPE_INT))
			return new Integer(data);
		else if (type.equals(DataType.TYPE_FLOAT))
			return new Double(data);
		return new String(data);
	}
	
	/**
	 * 根据目标数据类型创建值
	 * @param type 目标数据类型
	 * @return 值
	 */
	public static Object createDefaultValue(DataType type) {
		if (type.equals(DataType.TYPE_INT))
			return new Integer(0);
		else if (type.equals(DataType.TYPE_FLOAT))
			return new Double(0.0D);
		return new String("");
	}
	
	/**
	 * 打印决策树到控制台
	 * @param ps 打印流
	 * @param node 决策树节点
	 */
	public static void printTree(PrintStream ps, DecisionTreeNode node) {
		_printTree(ps, node, 0);
	}
	
	public static void _printTree(PrintStream ps, DecisionTreeNode node, int parentIndent) {
		if (node.subNodeList.size() == 0) {
			if (node.clazz != null)
				ps.print("[" + node.clazz.name + "]\n");
			return ;
		}
		
		String attribute = node.critierion.attribute.name;
		int indent = attribute.length()+parentIndent+2;
		ps.print(attribute + "? ");
		Set<Value> valueSet = node.critierion.valueNodeMap.keySet();
		
		int count = 0;
		for (Value value : valueSet) {
			String question = (String)value.value;
			if (count++ == 0)	
				ps.print(question + ": ");
			else	
				ps.print(getIndent(indent) + question + ": ");
			DecisionTreeNode subNode = node.critierion.valueNodeMap.get(value);
			_printTree(ps, subNode, indent+question.length()+2);
		}
	}
	
	public static String getIndent(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(" ");
		return sb.toString();
	}
}
