package com.zluyuer.dt.app;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.zluyuer.dt.algo.Attribute;
import com.zluyuer.dt.algo.Class;
import com.zluyuer.dt.algo.DataType;
import com.zluyuer.dt.algo.DecisionTree;
import com.zluyuer.dt.algo.DecisionTreeNode;
import com.zluyuer.dt.algo.TreeHelper;
import com.zluyuer.dt.algo.Tuple;
import com.zluyuer.dt.algo.Value;
import com.zluyuer.dt.util.Prompter;
import com.zluyuer.dt.util.TextFileUtil;
import com.zluyuer.dt.util.UIToolkit;

public class DecisionTreeLogic {
	
	DecisionTreeApp app;
	Prompter prompter;
	String filePath;
	
	DataModel model = new DataModel();
	boolean modified = false;

	public DecisionTreeLogic(DecisionTreeApp app) {
		this.app = app;
		this.prompter = app.prompter;
	}
	
	public void displayTree() {
		try {
			if (!updateDataToModel())
				return ;
			
			DecisionTreeNode root = 
				DecisionTree.createTree(model.tupleList, model.attrList);
			TreeHelper.printTree(System.out, root);
			
			TreeGraphWindow graphWindow = new TreeGraphWindow(root);
			graphWindow.display();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateDataToModel() throws Exception {
		List<Tuple> tupleList = new ArrayList<Tuple>();
		TableItem[] tableItems = app.table.getItems();
		int colNum = app.table.getColumnCount();
		for (int i = 0; i < tableItems.length; i++) {
			TableItem tableItem = tableItems[i];
			Tuple tuple = new Tuple();
			for (int j = 1; j < colNum-1; j++) {
				String text = tableItem.getText(j).trim();
				if (text.equals("")) {
					prompter.MsgWarnBox("请补充第" + (i+1) + "行" + (j+1) + "列的空数据");
					return false;
				}
				Value value = new Value();
				Attribute attribute = model.attrList.get(j-1);
				value.value = TreeHelper.createValue(text, attribute.type);
				value.attribute = attribute;
				value.tuple = tuple;
				tuple.valueList.add(value);
			}
			String clazz = tableItem.getText(colNum-1);
			tuple.clazz = new Class(clazz, DataType.TYPE_TEXT);
			tuple.rowNum = i;
			tupleList.add(tuple);
		}
		model.tupleList = tupleList;
		return true;
	}
	
	public void loadData(String filePath) throws Exception {
		List<String> dataList = TextFileUtil.readLines(filePath);
		createModel(dataList);
		updateUI();
		this.filePath = filePath;
	}
	
	private void createModel(List<String> dataList) throws Exception {
		model.reset();
		
		if (dataList.size() < 3) 
			throw new Exception("数据不完整");
		
		//从第1,2行读取字段及分类信息,默认分类的类型为TEXT
		String[] colArr = dataList.get(0).split(",");
		String[] typeArr = dataList.get(1).split(",");
		int typeCount = getTypeCount(typeArr);
		if (typeCount != colArr.length)	//分类目前数据类型默认为TEXT
			throw new Exception("数据类型字段出错");
		for (int i = 0; i < colArr.length-1; i++)
			model.attrList.add(new Attribute(colArr[i].trim(), 
				new DataType(typeArr[i].trim().toLowerCase()), i));
		model.classColName = colArr[colArr.length-1].trim();
		model.classType = typeArr[colArr.length-1].trim().toLowerCase();
		
		//从第3行起读取训练数据
		for(int i = 2; i < dataList.size(); i++) {
			String[] dataArr = dataList.get(i).split(",");
			if (dataArr.length != typeCount)
				throw new Exception("在第" + (i+1) + "行发现非法数据");
			
			Tuple tuple = new Tuple();
			for (int j = 0; j < dataArr.length-1; j++) {
				Attribute attribute = model.attrList.get(j);
				Value value = new Value();
				value.value = TreeHelper.createValue(dataArr[j].trim(), attribute.type);
				value.attribute = attribute;
				value.tuple = tuple;
				tuple.valueList.add(value);
			}
			tuple.clazz = new Class(dataArr[colArr.length-1], new DataType(model.classType));
			tuple.rowNum = i;
			model.tupleList.add(tuple);
		}
	}
	
	private int getTypeCount(String[] arr) {
		int count = 0;
		for (String type : arr)
			if (type.equals(DataType.TYPE_TEXT.name) || 
				type.equals(DataType.TYPE_INT.name) ||
				type.equals(DataType.TYPE_FLOAT.name))
				count++;
		return count;
	}
	
	private void updateUI() {
		app.list.removeAll();
		app.combo.select(0);
		app.text.setText("");
		app.table.removeAll();
		while (app.table.getColumnCount() > 0)
			app.table.getColumns()[0].dispose(); 
		
		//表头数据
		final TableColumn rowColumn = new TableColumn(app.table, SWT.NONE);
		rowColumn.setText("[ROW]");
		rowColumn.setWidth(50);
		for (Attribute attr : model.attrList) {
			app.list.add(UIToolkit.getColText(attr.type.name, attr.name));
			final TableColumn newColumn = new TableColumn(app.table, SWT.NONE);
		    newColumn.setText(attr.name);
		    newColumn.setWidth(100);
		}
		final TableColumn classColumn = new TableColumn(app.table, SWT.NONE);
		classColumn.setText("[CLASS]" + model.classColName);
		classColumn.setWidth(120);
		
		//表身数据
		for (int i = 0; i < model.tupleList.size(); i++) {
			Tuple tuple = model.tupleList.get(i);
			int dataSize = tuple.valueList.size();
			String[] dataArr = new String[dataSize+2];
			dataArr[0] = String.valueOf(i+1);
			for (int j = 0; j < dataSize; j++)
				dataArr[j+1] = tuple.valueList.get(j).value.toString();
			dataArr[dataSize+1] = tuple.clazz.name;
			TableItem item = new TableItem(app.table, SWT.NONE);
			item.setText(dataArr);
		}
	}
	
	public void addOrUpdateField(String type, String name) {
		if (name.equals("")) {
			prompter.MsgWarnBox("请填写字段名");
			return ;
		}
		
		int attrIndex = -1;
		for (int i = 0; i < model.attrList.size(); ++i) {
			Attribute attr = model.attrList.get(i);
			if (attr.name.equals(name)) { //存在则更新数据类型
				attr.type = new DataType(type);
				attrIndex = i;
				break;
			}
		}
		if (attrIndex < 0) { //不存在则添加
			Attribute attribute = new Attribute(name, new DataType(type), model.attrList.size());
			model.attrList.add(attribute);
			for (int i = 0; i < model.attrList.size(); i++)
				model.attrList.get(i).index = i;
			//填充空白数据
			for (Tuple tuple : model.tupleList) {
				Value v = new Value();
				v.value = TreeHelper.createDefaultValue(attribute.type);
				v.attribute = attribute;
				v.tuple = tuple;
				tuple.valueList.add(v);
			}
		}
		
		updateUI();
	}
	
	public void removeField(String field) {
		String[] items = UIToolkit.getColOpt(field);
		String colName = items[1];
		
		int attrIndex = -1;
		for (int i = 0; i < model.attrList.size(); ++i) {
			Attribute attr = model.attrList.get(i);
			if (attr.name.equals(colName)) {
				model.attrList.remove(i);
				attrIndex = i;
				break;
			}
		}
		for (int i = 0; i < model.attrList.size(); i++)
			model.attrList.get(i).index = i;
		if (attrIndex >= 0)
			for (Tuple tuple : model.tupleList)
				tuple.valueList.remove(attrIndex);
		
		updateUI();
	}
	
	public void saveData() throws Exception {
		saveData(filePath);
	}
	
	public void saveData(String filePath) throws Exception {
		modified = false;
		PrintStream ps = new PrintStream(new File(filePath));
		model.print(ps);
		ps.close();
		this.filePath = filePath;
	}
	
	public void newData() {
		model.reset();
		updateUI();
	}
}
