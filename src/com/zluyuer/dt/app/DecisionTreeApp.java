package com.zluyuer.dt.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.zluyuer.dt.util.Prompter;
import com.zluyuer.dt.util.UIToolkit;

public class DecisionTreeApp {
	
	static String TITLE = "决策树演示程序";

	public Combo combo;
	public Text text;
	public List list;
	public Table table;
	public Shell shell;
	public Display display;
	
	DecisionTreeLogic logic;
	Prompter prompter;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DecisionTreeApp window = new DecisionTreeApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		UIToolkit.centerShell(shell);
		prompter = new Prompter(shell);
		logic = new DecisionTreeLogic(this);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText(TITLE);
		shell.setImage(display.getSystemImage(SWT.ICON_INFORMATION)); 
		shell.addShellListener(new ShellAdapter() {  
            public void shellClosed(ShellEvent e) {  
                System.exit(0);
            }  
        });  
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
	    shell.setLayout(layout);

		final Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText("文件(&F)");

		final Menu menu_1 = new Menu(menuItem);
		menuItem.setMenu(menu_1);

		final MenuItem menuItem_5 = new MenuItem(menu_1, SWT.NONE);
		menuItem_5.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				System.exit(0);
			}
		});
		menuItem_5.setText("退出(&Q)");

		final MenuItem menuItem_1 = new MenuItem(menu, SWT.CASCADE);
		menuItem_1.setText("帮助(&H)");

		final Menu menu_2 = new Menu(menuItem_1);
		menuItem_1.setMenu(menu_2);

		final MenuItem menuItem_2 = new MenuItem(menu_2, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				String about = "【决策树演示程序】\n\n";
				about += "中国传媒大学计算机学院  陆遥\n";
				about += "Email: zluyuer@gmail.com\n";
				about += "QQ: 361087546";
				prompter.MsgBox("作者信息", about, SWT.OK | SWT.ICON_INFORMATION);
			}
		});
		menuItem_2.setText("关于(&A)");

		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 200, 400);
		GridData data = new GridData(GridData.FILL_VERTICAL);
	    data.widthHint = 200;
	    data.heightHint = 400;
	    tabFolder.setLayoutData(data);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("决策树绘制");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);

		final Group group_1 = new Group(composite, SWT.NONE);
		group_1.setText("数据字段列表");
		group_1.setBounds(10, 120, 180, 321);

		list = new List(group_1, SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		list.setBounds(10, 25, 160, 216);
		
		list.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int sel = list.getSelectionIndex();
				if (sel < 0)	return ;
				String selString = list.getItems()[sel];
				String[] items = UIToolkit.getColOpt(selString);
				combo.setText(items[0]);
				text.setText(items[1]);
		    }

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				System.out.println("widgetDefaultSelected");
			}
		});

		text = new Text(group_1, SWT.BORDER);
		text.setBounds(82, 250, 88, 25);

		final Button button_4 = new Button(group_1, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				String type = combo.getText();
				String name = text.getText().trim();
				logic.addOrUpdateField(type, name);
			}
		});
		button_4.setText("增加/更新(&U)");
		button_4.setBounds(10, 281, 90, 27);

		final Button button_5 = new Button(group_1, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				int sel = list.getSelectionIndex();
				if (sel < 0) {
					prompter.MsgWarnBox("请选择一个字段");
					return ;
				}
				String field = list.getItems()[sel];
				logic.removeField(field);
				combo.setText("");
				text.setText("");
			}
		});
		button_5.setText("移除(&R)");
		button_5.setBounds(104, 281, 66, 27);

		combo = new Combo(group_1, SWT.READ_ONLY);
		combo.setBounds(10, 250, 66, 25);
		combo.add("TEXT");
		combo.add("INT");
		combo.add("FLOAT");
		combo.select(0);

		final Button button = new Button(composite, SWT.NONE);
		button.setBounds(10, 10,180, 30);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				shell.setText(TITLE);
				logic.newData();
			}
		});
		button.setText("新建训练数据(&N)");

		final Button button_1 = new Button(composite, SWT.NONE);
		button_1.setBounds(10, 44,180, 30);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				FileDialog dlg = new FileDialog(shell, SWT.OPEN);
				dlg.setFilterNames(new String[] {
					"CSV文件 (*.csv)",
					"文本文件 (*.txt)",
					"全部文件 (*.*)"});
				dlg.setFilterExtensions(new String[] {
					"*.csv", "*.txt", "*.*"});
				String filePath = dlg.open();
				if (filePath != null) {
					try {
						logic.loadData(filePath);
						shell.setText(TITLE + " - " + filePath);
					} catch (Exception e) {
						prompter.MsgErrorBox("数据读取错误:" + e.getMessage());
						e.printStackTrace();
						return ;
					}
				}	
			}
		});
		button_1.setText("打开训练数据(&O)");

		final Button button_3 = new Button(composite, SWT.NONE);
		button_3.setBounds(10, 78,180, 30);
		button_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				FileDialog dlg = new FileDialog(shell, SWT.SAVE);
				dlg.setFilterNames(new String[] {
					"CSV文件 (*.csv)",
					"文本文件 (*.txt)",
					"全部文件 (*.*)"});
				dlg.setFilterExtensions(new String[] {
					"*.csv", "*.txt", "*.*"});
				String filePath = dlg.open();
				if (filePath != null) {
					try {
						logic.saveData(filePath);
						shell.setText(TITLE + " - " + logic.getFilePath());
					} catch (Exception e) {
						prompter.MsgErrorBox("数据存储错误:" + e.getMessage());
						e.printStackTrace();
						return ;
					}
				}	
			}
		});
		button_3.setText("保存训练数据(&S)");

		final Button button_6 = new Button(composite, SWT.NONE);
		button_6.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				logic.displayTree();
			}
		});
		button_6.setBounds(10, 458, 180, 30);
		button_6.setText("计算并绘制决策树(&D)");

		table = new Table(shell, SWT.FULL_SELECTION | SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(216, 10, 400, 400);
		
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
	    data.widthHint = 429;
	    data.heightHint = 400;
	    table.setLayoutData(data);
	    
	    final TableEditor editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent event) {
	        	Control old = editor.getEditor();
	            if (old != null) old.dispose();

	            Point pt = new Point(event.x, event.y);
	            final TableItem item = table.getItem(pt);
	            if (item == null) {
	            	final TableItem newItemTableItem = new TableItem(table, SWT.BORDER);
	            	int columnCount = table.getColumnCount();
	            	for (int i = 1; i < columnCount; i++) 
	            		newItemTableItem.setText(i, "");
	            	newItemTableItem.setText(String.valueOf(table.getItemCount()));
	            	
	            } else {
	            	int column = -1;
	                for (int i = 0, n = table.getColumnCount(); i < n; i++) {
	                	Rectangle rect = item.getBounds(i);
	                	if (rect.contains(pt)) {
	                		column = i;
	                		break;
	                	}
	                }
	                
	                if (column == 0)	return ;
	     
	                final Text text = new Text(table, SWT.NONE);
	                text.setForeground(item.getForeground());
	                text.setText(item.getText(column));
	                text.setForeground(item.getForeground());
	                text.selectAll();
	                text.setFocus();

	                editor.minimumWidth = text.getBounds().width;
	                editor.setEditor(text, item, column);

	                final int col = column;
	                text.addModifyListener(new ModifyListener() {
	                	public void modifyText(ModifyEvent event) {
	                		item.setText(col, text.getText());
	                	}
	                });
	            }
	        }
		});

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(50);
		newColumnTableColumn.setText("[ROW]");

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(120);
		newColumnTableColumn_1.setText("[CLASS]");
	}

}
