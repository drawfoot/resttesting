package com.ibm.sg.idaastesting.resulttable;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class RTLongTextCellEditor extends DialogCellEditor {

    public RTLongTextCellEditor(Composite parent) {
        super(parent);
    }

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		MessageDialog.openInformation(cellEditorWindow.getShell(), "Detail Information",
				doGetValue() == null ? "" : doGetValue().toString());
		return null;
	}

}
