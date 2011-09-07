package com.zluyuer.dt.app;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.zluyuer.dt.util.UIToolkit;

public class TreeGraphDemo extends JFrame {

	private static final long serialVersionUID = -2764911804288120883L;

	public TreeGraphDemo() {
		super("TreeGraphDemo");
		
		final mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		
		try {
		   Object age = graph.insertVertex(parent, null, "age?", 1, 1, 80, 30);
		   Object agey = graph.insertVertex(parent, null, "yes", 1, 1, 80, 30);
		   Object student = graph.insertVertex(parent, null, "student?", 1, 1, 80, 30);
		   Object studenty = graph.insertVertex(parent, null, "yes", 1, 1, 80, 30);
		   Object studentn = graph.insertVertex(parent, null, "no", 1, 1, 80, 30);
		   Object credit_rating = graph.insertVertex(parent, null, "credit_rating?", 1, 1, 80, 30);
		   Object credit_ratingy = graph.insertVertex(parent, null, "yes", 1, 1, 80, 30);
		   Object credit_ratingn = graph.insertVertex(parent, null, "no", 1, 1, 80, 30);
		   
		   graph.insertEdge(parent, null, "youth", age, student);
		   graph.insertEdge(parent, null, "middle_aged", age, agey);
		   graph.insertEdge(parent, null, "senior", age, credit_rating);
		   graph.insertEdge(parent, null, "yes", student, studenty);
		   graph.insertEdge(parent, null, "no", student, studentn);
		   graph.insertEdge(parent, null, "excellent", credit_rating, credit_ratingy);
		   graph.insertEdge(parent, null, "fair", credit_rating, credit_ratingn);
		   
		   mxIGraphLayout layout = new mxCompactTreeLayout(graph, false);
		   layout.execute(graph.getDefaultParent());
		   
		} finally {
		   graph.getModel().endUpdate();
		}
		
		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}
	
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() != WindowEvent.WINDOW_CLOSING) {
			super.processWindowEvent(e); 
		} else {
			this.dispose();
		}
	}

	public static void main(String[] args) {
		TreeGraphDemo frame = new TreeGraphDemo();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		UIToolkit.centerFrame(frame);
		frame.setVisible(true);
		
	}
}