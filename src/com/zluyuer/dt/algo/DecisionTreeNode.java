package com.zluyuer.dt.algo;

import java.util.ArrayList;
import java.util.List;

/**
 * 决策树节点
 * @author 陆遥
 *
 */
public class DecisionTreeNode {

	/**
	 * 节点分类（当且仅当节点为叶子节点时有效）
	 */
	public Class clazz; 
	/**
	 * 分裂规则
	 */
	public SplitCriterion critierion = new SplitCriterion();
	/**
	 * 子节点列表
	 */
	public List<DecisionTreeNode> subNodeList = new ArrayList<DecisionTreeNode>();

	/**
	 * 添加子节点
	 * @param value 子节点对应分裂值
	 * @param node 子节点
	 */
	public void addSubNode(Value value, DecisionTreeNode node) {
		critierion.valueNodeMap.put(value, node);
		subNodeList.add(node);
	}
	
	/**
	 * 是否为子节点
	 * @return 当节点为子节点时返回true，否则返回false
	 */
	public boolean isLeaf() {
		return subNodeList.size() == 0;
	}
}