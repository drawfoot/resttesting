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
		case RTColumnInfo.COL_IDX_NO:
			return model.getNo();
		case RTColumnInfo.COL_IDX_METHOD:
			return model.getMethod();
		case RTColumnInfo.COL_IDX_STATUS:
			return model.getStatus();
		case RTColumnInfo.COL_IDX_SEVERITY:
			return model.getSeverity();
		case RTColumnInfo.COL_IDX_EXPCODE:
			return model.getExpectedStatus();
		case RTColumnInfo.COL_IDX_EXPMSG:
			return model.getExpectedMsg();
		case RTColumnInfo.COL_IDX_ACTCODE:			
			return model.getActualStatus();
		case RTColumnInfo.COL_IDX_ACTMSG:
			return model.getActualMsg();
		case RTColumnInfo.COL_IDX_URL:
			return model.getUrl();
		case RTColumnInfo.COL_IDX_HEAD:
			return model.getHead();			
		case RTColumnInfo.COL_IDX_DATA:
			return model.getData();	
		default:
			break;
		}
		return "";
	}
}