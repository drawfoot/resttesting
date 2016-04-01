package com.ibm.sg.idaastesting.resulttable;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;

public class RTLongTextDialog extends Dialog {
	private Text textCtr;
	private String text;

	public RTLongTextDialog(Shell parentShell, String text) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE);
		this.text = text;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		textCtr = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textCtr.setText(text);

		return container;
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Long Text Dialog");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(546, 573);
	}
}

class RTLongTextEditor extends DialogCellEditor {

    public RTLongTextEditor(Composite parent) {
        super(parent);
    }

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		RTLongTextDialog dialog = new RTLongTextDialog(
				cellEditorWindow.getShell(), doGetValue() == null ? ""
						: doGetValue().toString());
		dialog.create();
		dialog.open();
		return null;
	}
}

