package com.ibm.sg.idaastesting.resulttable;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.ibm.sg.idaastesting.model.TestingRecordList;


public class RTTable {
	private Composite parent;
	private int style;
	private TestingRecordList tableModel;
	CheckboxTableViewer checkboxTableViewer;
	
	public RTTable(Composite parent, int style, TestingRecordList tableModel) {
		this.parent = parent;
		this.style = style;
		this.tableModel = tableModel;
		build();
	}

	public CheckboxTableViewer getViewer() {
		return checkboxTableViewer;
	}

	public void build() {

		checkboxTableViewer = CheckboxTableViewer
				.newCheckList(parent, style);
		Table table = checkboxTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		int columnNum = 9;
	    CellEditor[] editors = new CellEditor[columnNum];
		for(int i = 0; i < columnNum; i++) {
			TableViewerColumn newTableViewerColumn = new TableViewerColumn(
					checkboxTableViewer, SWT.NONE);
			TableColumn tblclmnNewColumn = newTableViewerColumn.getColumn();
			tblclmnNewColumn.setWidth(100);
			tblclmnNewColumn.setText(RTCellModifier.PROPS[i]);
		    editors[i] = new TextCellEditor(table);
		}

		checkboxTableViewer.setColumnProperties(RTCellModifier.PROPS);
		checkboxTableViewer.setCellModifier(new RTCellModifier(checkboxTableViewer));
		checkboxTableViewer.setCellEditors(editors);
	    
		checkboxTableViewer.setContentProvider(new RTContentProvider());
		checkboxTableViewer.setLabelProvider(new RTLabelProvider());
		checkboxTableViewer.setInput(tableModel.getModel());
	}
	
	public void adjustColumnWidth() {
        Table table = this.getViewer().getTable();
        for (TableColumn tc : table.getColumns())
            tc.pack();      
	}
}
