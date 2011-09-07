package com.zluyuer.dt.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 划分规则
 * @author 陆遥
 *
 */
public class SplitCriterion {

	/**
	 * 划分属性
	 */
	public Attribute attribute;
	/**
	 * 划分值及元组集的映射表
	 */
	public Map<Value, List<Tuple>> valueTupleListMap = new HashMap<Value, List<Tuple>>();
	/**
	 * 划分值及子节点的映射表
	 */
	public Map<Value, DecisionTreeNode> valueNodeMap = new HashMap<Value, DecisionTreeNode>();
}
