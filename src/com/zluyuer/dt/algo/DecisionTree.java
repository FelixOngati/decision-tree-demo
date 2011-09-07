package com.zluyuer.dt.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zluyuer.dt.util.TextFileUtil;

/**
 * 决策树
 * @author 陆遥
 *
 */
public class DecisionTree {
	
	/**
	 * 训练数据列数常量
	 */
	public static int DATA_ROW_NUM = 5;
	
	/**
	 * 程序入口
	 * @param args 程序入口参数
	 */
	public static void main(String[] args) {
		
		try {
			System.out.println("开始训练决策树...");
			
			//默认所有数据类型都为STRING
			List<Attribute> attrList = new ArrayList<Attribute>();
			attrList.add(new Attribute("age", DataType.TYPE_TEXT, 0));
			attrList.add(new Attribute("income", DataType.TYPE_TEXT, 1));
			attrList.add(new Attribute("student", DataType.TYPE_TEXT, 2));
			attrList.add(new Attribute("credit_rating", DataType.TYPE_TEXT, 3));
			
			List<String> dataList = TextFileUtil.readLines("data/training.csv");
			List<Tuple> tupleList = new ArrayList<Tuple>();
			
			//忽略第1行标题及第2行数据类型
			for(int i = 2; i < dataList.size(); i++) {
				String data = dataList.get(i);
				String[] colArr = data.split(",");
				if (colArr.length != DATA_ROW_NUM) {
					System.out.println("在第" + (i+1) + "行发现非法数据!");
					continue;
				}
				
				//依次创建各行元组信息
				Tuple tuple = new Tuple();
				for (int j = 0; j < colArr.length-1; j++) {
					Attribute attribute = attrList.get(j);
					Value value = new Value();
					value.value = TreeHelper.createValue(colArr[j], attribute.type);
					value.attribute = attribute;
					value.tuple = tuple;
					tuple.valueList.add(value);
				}
				tuple.clazz = new Class(colArr[colArr.length-1], DataType.TYPE_TEXT);
				tuple.rowNum = i;
				tupleList.add(tuple);
			}
			
			//计算决策树
			DecisionTreeNode root = createTree(tupleList, attrList);
			
			System.out.println("训练完毕,打印决策树:");
			System.out.println();
			TreeHelper.printTree(System.out, root);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算决策树
	 * @param tupleList 训练元组列表
	 * @param attrList 数据属性列表
	 * @return 决策树
	 */
	public static DecisionTreeNode createTree(List<Tuple> tupleList, 
			List<Attribute> attrList) {
		
		List<Attribute> tmpAttrList = new ArrayList<Attribute>();
		tmpAttrList.addAll(attrList);
		return create(tupleList, tmpAttrList);
	}
	
	/**
	 * 递归计算决策树
	 * @param tupleList 训练元组列表
	 * @param attrList 数据属性列表
	 * @return 决策树
	 */
	public static DecisionTreeNode create(List<Tuple> tupleList, 
			List<Attribute> attrList) {
		
		DecisionTreeNode node = new DecisionTreeNode();

		//若元组都属同一类，则返回叶子节点，以此类标记
		List<Class> classList = getClassList(tupleList);
		if (classList.size() == 1) {
			node.clazz = classList.get(0);
			return node;
		}
		
		//如果待判断属性列表为空，则返回叶子节点，多数表决分类
		if (attrList.size() == 0) {
			node.clazz = getMajorityClass(tupleList);
			return node;
		}
		
		//划分best criterion并在节点上标记
		SplitCriterion sc = getBestSplitCriterion(tupleList, attrList);
		node.critierion = sc;
		
		//移除已使用的attribute
		attrList.remove(sc.attribute);
		System.out.println("Splitting attribute: " + sc.attribute);
		
		//取得best criterion的每个输出并判决
		Set<Value> valueSet = sc.valueTupleListMap.keySet();
		for (Value value : valueSet) {
			List<Tuple> subTupleList = sc.valueTupleListMap.get(value);
			
			if (subTupleList.size() == 0) { 
				//若该输出无元组，生成叶子节点，多数表决分类
				DecisionTreeNode subNode = new DecisionTreeNode();//叶子节点
				subNode.clazz = getMajorityClass(tupleList);
				node.addSubNode(value, subNode);
				
			} else { 
				//否则递归生成子树
				DecisionTreeNode subNode = create(subTupleList, attrList);
				node.addSubNode(value, subNode);
			}
		}
		
		return node;
	}
	
	/**
	 * 计算最佳分裂规则
	 * @param tupleList 元组列表
	 * @param attrList 数据属性列表
	 * @return 最佳分裂规则
	 */
	public static SplitCriterion getBestSplitCriterion(List<Tuple> tupleList, 
			List<Attribute> attrList) {
		
		double bestGain = 0.0D;
		SplitCriterion sc = new SplitCriterion();
		
		//依次计算每个属性的信息增益，选择最佳增益结果为分裂属性
		for (Attribute attr : attrList) {
			double gain = getInfoGain(tupleList, attr);
			if (gain > bestGain) {
				bestGain = gain;
				sc.attribute = attr;
			}
		}
		System.out.println("Best gain: " + bestGain);

		//根据分裂规则划分元组集合
		for (Tuple tuple : tupleList) {
			Value value = tuple.valueList.get(sc.attribute.index);
			if (!sc.valueTupleListMap.containsKey(value)) 
				sc.valueTupleListMap.put(value, new ArrayList<Tuple>());
			List<Tuple> subTupleList = sc.valueTupleListMap.get(value);
			subTupleList.add(tuple);
		}
		
		return sc;
	}
	
	/**
	 * 计算信息增益
	 * @param tupleList 元组列表
	 * @param attr 数据属性
	 * @return 信息增益
	 */
	public static double getInfoGain(List<Tuple> tupleList, Attribute attr) {
		double classInfoAmount = getClassInfoAmount(tupleList);
		double attrInfoAmount = getAttrInfoAmount(tupleList, attr);
		return classInfoAmount - attrInfoAmount;
	}
	
	/**
	 * 计算分类信息增益
	 * @param tupleList 训练元组列表
	 * @return 分类信息增益
	 */
	public static double getClassInfoAmount(List<Tuple> tupleList) {
		Map<Class, Integer> classCountMap = getClassCountMap(tupleList);
		
		double totalInfoAmount = 0.0D;
		Collection<Integer> classCounts = classCountMap.values();
		for (Integer classCount : classCounts) {
			double classPercent = (double)classCount / tupleList.size();
			double infoAmount = Math.log(classPercent) / Math.log(2.0D);
			infoAmount *= classPercent;
			totalInfoAmount -= infoAmount;
		}
		
		return totalInfoAmount;
	}
	
	/**
	 * 计算属性信息增益
	 * @param tupleList 元组列表
	 * @param attribute 数据属性
	 * @return 属性信息增益
	 */
	public static double getAttrInfoAmount(List<Tuple> tupleList, Attribute attribute) {
		Map<Value, List<Tuple>> subTupleListMap = new HashMap<Value, List<Tuple>>();
		for (Tuple tuple : tupleList) {
			Value value = tuple.valueList.get(attribute.index);
			if (!subTupleListMap.containsKey(value)) 
				subTupleListMap.put(value, new ArrayList<Tuple>());
			List<Tuple> subTupleList = subTupleListMap.get(value);
			subTupleList.add(tuple);
		}
		
		double totalInfoAmount = 0.0D;
		Collection<List<Tuple>> subTupleListCol = subTupleListMap.values();
		for (List<Tuple> subTupleList : subTupleListCol) {
			double subListPercent = (double)subTupleList.size() / tupleList.size();
			double subListInfoAmount = 0.0D;
			Map<Class, Integer> classCountMap = getClassCountMap(subTupleList);
			Collection<Integer> countCol = classCountMap.values();
			for (Integer count : countCol) {
				double valuePercent = (double)count / subTupleList.size();
				double valueInfoAmount = Math.log(valuePercent) / Math.log(2.0D);
				valueInfoAmount *= valuePercent;
				subListInfoAmount -= valueInfoAmount;
			}
			subListInfoAmount *= subListPercent;
			totalInfoAmount += subListInfoAmount;
		}
		
		return totalInfoAmount;
	}
	
	/**
	 * 返回元组列表中的非重复分类集合
	 * @param tupleList 元组列表
	 * @return 分类集合
	 */
	public static List<Class> getClassList(List<Tuple> tupleList) {
		Map<Class, Integer> classCountMap = getClassCountMap(tupleList);	
		List<Class> classList = new ArrayList<Class>();
		classList.addAll(classCountMap.keySet());
		return classList;
	}
	
	/**
	 * 返回元组列表中占最大比例的分类
	 * @param tupleList 元组列表
	 * @return 最大比例分类
	 */
	public static Class getMajorityClass(List<Tuple> tupleList) {
		Map<Class, Integer> classCountMap = getClassCountMap(tupleList);
		Integer maxCount = 0;
		Class majorityClass = null;
		Set<Class> classSet = classCountMap.keySet();
		for (Class clazz : classSet) {
			int count = classCountMap.get(clazz);
			if (count >= maxCount) {
				maxCount = count;
				majorityClass = clazz;
			}
		}
		return majorityClass;
	}
	
	/**
	 * 返回分类计数映射表
	 * @param tupleList 元组列表
	 * @return 分类计数映射表
	 */
	public static Map<Class, Integer> getClassCountMap(List<Tuple> tupleList) {
		Map<Class, Integer> classCountMap = new HashMap<Class, Integer>();
		for (Tuple tuple : tupleList) {
			Class clazz = tuple.clazz;
			if (!classCountMap.containsKey(clazz))
				classCountMap.put(clazz, 0);
			Integer count = classCountMap.get(clazz);
			classCountMap.put(clazz, count+1);
		}
		return classCountMap;
	}
}