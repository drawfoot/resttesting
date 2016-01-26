package com.ibm.sg.idaastesting.resulttable;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.ibm.sg.idaastesting.model.TestingRecord;

public class RTLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// no image to show
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// each element comes from the ContentProvider.getElements(Object)
		if (!(element instanceof TestingRecord)) {
			return "";
		}
		TestingRecord model = (TestingRecord) element;
		switch (columnIndex) {
		case 0:
			return model.getNo();
		case 1:
			return model.getMethod();
		default:
			break;
		}
		return "";
	}
}