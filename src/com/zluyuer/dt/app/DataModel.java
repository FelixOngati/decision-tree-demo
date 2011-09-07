package com.zluyuer.dt.app;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.zluyuer.dt.algo.Attribute;
import com.zluyuer.dt.algo.Tuple;
import com.zluyuer.dt.algo.Value;

public class DataModel {

	public List<Attribute> attrList = new ArrayList<Attribute>();
	public List<Tuple> tupleList = new ArrayList<Tuple>();
	public String classColName = "";
	public String classType = "";
	
	public void print(PrintStream ps) {
		for (int i = 0; i < attrList.size(); i++) {
			Attribute attr = attrList.get(i);
			ps.print(attr.name);
			ps.print(",");
		}
		ps.println(classColName);
		
		for (int i = 0; i < attrList.size(); i++) {
			Attribute attr = attrList.get(i);
			ps.print(attr.type);
			ps.print(",");
		}
		ps.println(classType);
		
		for (Tuple tuple : tupleList) {
			for (int i = 0; i < tuple.valueList.size(); i++) {
				Value value = tuple.valueList.get(i);
				ps.print(value.value);
				ps.print(",");
			}
			ps.println(tuple.clazz.name);
		}
	}
	
	public void reset() {
		attrList.clear();
		tupleList.clear();
		classColName = "";
		classType = "";
	}
}
