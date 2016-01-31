package com.ibm.sg.idaastesting.resulttable;

import java.util.Arrays;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.ibm.sg.idaastesting.model.TestingRecord;

public class RTSorter {
	private final TableViewer tableViewer;
	private final RTViewerComparator comparator = new RTViewerComparator();

	public RTSorter(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		addColumnSelectionListeners(tableViewer);
		tableViewer.setComparator(comparator);
	}

	private void addColumnSelectionListeners(TableViewer tableViewer) {
		TableColumn[] columns = tableViewer.getTable().getColumns();
		for (int i = 0; i < columns.length; i++) {
			addColumnSelectionListener(columns[i]);
		}
	}

	private void addColumnSelectionListener(TableColumn column) {
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableColumnClicked((TableColumn) e.widget);
			}
		});
	}

	private void tableColumnClicked(TableColumn column) {
		Table table = tableViewer.getTable();
		int index = Arrays.asList(table.getColumns()).indexOf(column);
		comparator.setColumn(index);
		tableViewer.refresh();
	}
}

class RTViewerComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int ASCENDING = 1;
	private static final int DESCENDING = 0;
	private int direction = ASCENDING;

	public RTViewerComparator() {
		this.propertyIndex = 0;
		direction = ASCENDING;
	}

	public int getDirection() {
		return direction == ASCENDING ? SWT.UP : SWT.DOWN;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = ASCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TestingRecord p1 = (TestingRecord) e1;
		TestingRecord p2 = (TestingRecord) e2;
		int rc = 0;
		switch (propertyIndex) {
		case RTColumnInfo.COL_IDX_NO:
			if(notBlank(p1.getNo()))
				rc = p1.getNo().compareTo(p2.getNo());
			break;
		case RTColumnInfo.COL_IDX_METHOD:
			if(notBlank(p1.getMethod()))
				rc = p1.getMethod().compareTo(p2.getMethod());
			break;
		case RTColumnInfo.COL_IDX_STATUS:
			if(notBlank(p1.getStatus()))
				rc = p1.getStatus().compareTo(p2.getStatus());
			break;
		case RTColumnInfo.COL_IDX_SEVERITY:
			if(notBlank(p1.getSeverity()))
				rc = p1.getSeverity().compareTo(p2.getSeverity());
			break;
		case RTColumnInfo.COL_IDX_EXPCODE:
			if(notBlank(p1.getExpectedStatus()))
				rc = p1.getExpectedStatus().compareTo(p2.getExpectedStatus());
			break;
		case RTColumnInfo.COL_IDX_EXPMSG:
			if(notBlank(p1.getExpectedMsg()))
				rc = p1.getExpectedMsg().compareTo(p2.getExpectedMsg());
			break;
		case RTColumnInfo.COL_IDX_ACTCODE:
			if(notBlank(p1.getActualStatus()))
				rc = p1.getActualStatus().compareTo(p2.getActualStatus());
			break;
		case RTColumnInfo.COL_IDX_ACTMSG:
			if(notBlank(p1.getActualMsg()))
				rc = p1.getActualMsg().compareTo(p2.getActualMsg());
			break;
		case RTColumnInfo.COL_IDX_URL:
			if(notBlank(p1.getUrl()))
				rc = p1.getUrl().compareTo(p2.getUrl());
			break;
		case RTColumnInfo.COL_IDX_HEAD:
			if(notBlank(p1.getHead()))
				rc = p1.getHead().compareTo(p2.getHead());
			break;
		case RTColumnInfo.COL_IDX_DATA:
			if(notBlank(p1.getData()))
				rc = p1.getData().compareTo(p2.getData());
			break;			
		default:
			rc = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
	
	private boolean notBlank(String str) {
		return (str != null) && !str.trim().equals("");
	}
}
