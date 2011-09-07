package com.zluyuer.dt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Prompter {
	
	private Shell shell;
	
	public Prompter(Shell shell) {
		this.shell = shell;
	}
	
	public int MsgBox(String caption, String content, int style){
		MessageBox box = new MessageBox(shell, style);
		box.setText(caption);
		box.setMessage(content);
		return box.open();
	}
	
	public void MsgInfoBox(String content){
		MsgBox("提示", content, SWT.OK | SWT.ICON_INFORMATION);
	}
	
	public void MsgWarnBox(String content){
		MsgBox("警告", content, SWT.OK | SWT.ICON_WARNING);
	}
	
	public void MsgErrorBox(String content){
		MsgBox("错误", content, SWT.OK | SWT.ICON_ERROR);
	}
	
	public boolean MsgConfirmBox(String content){
		return (SWT.YES == MsgBox("确认", content, SWT.YES | SWT.NO));
	}

	public int MsgTripleBox(String content){
		return MsgBox("确认", content , SWT.YES | SWT.NO | SWT.CANCEL );
	}
}
