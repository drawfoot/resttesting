package com.ibm.sg.idaastesting;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import swing2swt.layout.BoxLayout;
import swing2swt.layout.BorderLayout;
import swing2swt.layout.FlowLayout;

public class MultiRunDialog extends Dialog {

	public MultiRunDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ListViewer listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setText("New Button");
		
		ListViewer listViewer_1 = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		List list_1 = listViewer_1.getList();
		
		Composite composite_2 = new Composite(container, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.setText("New Button");
		
		ListViewer listViewer_2 = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		List list_2 = listViewer_2.getList();
		
		return container;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Multiple Runner Config");
	}
	protected Point getInitialSize() {
		return new Point(560, 591);
	}
}

