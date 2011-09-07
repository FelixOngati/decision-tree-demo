package com.zluyuer.dt.app;

import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.zluyuer.dt.algo.Attribute;
import com.zluyuer.dt.algo.DataType;
import com.zluyuer.dt.algo.DecisionTreeNode;
import com.zluyuer.dt.algo.Value;
import com.zluyuer.dt.util.UIToolkit;

public class TreeGraphWindow extends JFrame {
	
	private static final long serialVersionUID = -2764911804288120883L;
	
	private DecisionTreeNode root;
	private mxGraph graph;
	private Object parent;
	
	private int wndWidth = 800;
	private int wndHeight = 600;
	private int drawXPoint;
	private int objWidth = 80;
	private int objHeight = 30;


	public TreeGraphWindow(DecisionTreeNode root) {
		super("决策树绘制");
		this.root = root;
	}
	
	public void display() {
		prepareGraph();
		showWindow();
	}
	
	private void prepareGraph() {
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		
		try {
			if (root.critierion.attribute == null)
					root.critierion.attribute = 
						new Attribute("nothing", DataType.TYPE_TEXT, 0);
			
			drawXPoint = wndWidth / 2 - objWidth;
			Object obj  = graph.insertVertex(parent, null, 
					root.critierion.attribute.name+"?", 
					drawXPoint, 1, objWidth, objHeight, "fontColor=#ff0000");
			buildGraph(root, obj);
			
			mxIGraphLayout layout = new mxCompactTreeLayout(graph, false);
			layout.execute(parent);
			
		} finally {
		   graph.getModel().endUpdate();
		}
		
		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}
	
	private void buildGraph(DecisionTreeNode node, Object parentObj) {
		Map<Value, DecisionTreeNode> valueNodeMap = node.critierion.valueNodeMap;
		Set<Value> valueSet = valueNodeMap.keySet();
		for (Value value : valueSet) {
			DecisionTreeNode subNode = valueNodeMap.get(value);
			if (subNode.isLeaf()) {
				Object obj  = graph.insertVertex(parent, null, 
						subNode.clazz.name, 
						drawXPoint, 1, objWidth, objHeight);
				graph.insertEdge(parent, null, value.value, parentObj, obj);
			} else {
				Object obj = graph.insertVertex(parent, null, 
						subNode.critierion.attribute.name+"?", 
						drawXPoint, 1, objWidth, objHeight, "fontColor=#ff0000");
				graph.insertEdge(parent, null, value.value, parentObj, obj);
				buildGraph(subNode, obj);
			}
		}
	}
	
	private void showWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(wndWidth, wndHeight);
		UIToolkit.centerFrame(this);
		setVisible(true);
	}
	
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() != WindowEvent.WINDOW_CLOSING) {
			super.processWindowEvent(e); 
		} else {
			this.dispose();
		}
	}

	public int getWndWidth() {
		return wndWidth;
	}

	public void setWndWidth(int wndWidth) {
		this.wndWidth = wndWidth;
	}

	public int getWndHeight() {
		return wndHeight;
	}

	public void setWndHeight(int wndHeight) {
		this.wndHeight = wndHeight;
	}

	public int getObjWidth() {
		return objWidth;
	}

	public void setObjWidth(int objWidth) {
		this.objWidth = objWidth;
	}

	public int getObjHeight() {
		return objHeight;
	}

	public void setObjHeight(int objHeight) {
		this.objHeight = objHeight;
	}
	
}
