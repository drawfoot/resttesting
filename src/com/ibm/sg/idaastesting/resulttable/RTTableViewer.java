package com.ibm.sg.idaastesting.resulttable;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


public class RTTableViewer{
	@SuppressWarnings("unused")
	private RTSorter sorter;	
	CheckboxTableViewer viewer;

	public RTTableViewer(Composite parent, int style) {
		viewer = CheckboxTableViewer.newCheckList(parent, style);
		build();
	}

	public CheckboxTableViewer getViewer() {
		return viewer;
	}
	
	private void build() {
		final Table table = viewer.getTable();
	    CellEditor[] editors = new CellEditor[RTColumnInfo.COL_COUNT];
		for(int i = 0; i < RTColumnInfo.COL_COUNT; i++) {
			TableViewerColumn newTableViewerColumn = new TableViewerColumn(
					viewer, SWT.NONE);
			TableColumn tblclmnNewColumn = newTableViewerColumn.getColumn();
			tblclmnNewColumn.setWidth(100);
			tblclmnNewColumn.setText(RTColumnInfo.COL_PROPS[i]);
			if(i==RTColumnInfo.COL_IDX_ACTMSG) {
			    editors[i] = new RTLongTextCellEditor(table);
			} else {
				editors[i] = new TextCellEditor(table);
			}
		}
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		viewer.setColumnProperties(RTColumnInfo.COL_PROPS);
		viewer.setCellModifier(new RTCellModifier(viewer));
		viewer.setCellEditors(editors);
	    
		viewer.setContentProvider(new RTContentProvider());
		viewer.setLabelProvider(new RTLabelProvider());

		addCellNavigation(viewer);
		sorter = new RTSorter(viewer);
	}

	private void addCellNavigation(final CheckboxTableViewer tableViewer) {
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(
				tableViewer, new RTFocusCellHighlighter(tableViewer));

		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				tableViewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.TAB);
			}
		};

		TableViewerEditor.create(tableViewer, focusCellManager, actSupport,
				ColumnViewerEditor.KEYBOARD_ACTIVATION
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.TABBING_HORIZONTAL);

		// Used to override the default TAB behavior which focuses on the next
		// component
		tableViewer.getTable().addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.keyCode == SWT.TAB) {
					arg0.doit = false;
				}
			}
		});
	}

	public void adjustColumnWidth() {
        Table table = viewer.getTable();
        for (TableColumn tc : table.getColumns())
            tc.pack();      
	}
}
