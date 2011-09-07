package com.zluyuer.dt.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UIToolkit {
	
	public static void centerShell(Shell shell) {
		Display display = Display.getCurrent();
		Rectangle rect = display.getClientArea();
		shell.setLocation(rect.width / 2 - shell.getSize().x / 2, 
				rect.height / 2 - shell.getSize().y / 2); 
	}
	
	public static void centerFrame(JFrame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		double width = dimension.getWidth(); 
        double height = dimension.getHeight(); 
        frame.setLocation((int)(width - frame.getWidth()) / 2, 
        		(int)(height - frame.getHeight()) / 2); 
	}
	
	public static String[] getColOpt(String text) {
		String[] items = new String[2];
		int typeBegin = text.indexOf("[");
		int typeEnd = text.indexOf("]");
		items[0] = text.substring(typeBegin+1, typeEnd);
		items[1] = text.substring(typeEnd+1);
		return items;
	}
	
	public static String getColText(String type, String col) {
		return "[" + type.toUpperCase() + "]" + col;
	}
	
}
