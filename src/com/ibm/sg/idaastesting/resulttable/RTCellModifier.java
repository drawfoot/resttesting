package com.ibm.sg.idaastesting.resulttable;

import com.ibm.sg.idaastesting.model.TestingRecord;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

public class RTCellModifier implements ICellModifier {

	private Viewer viewer;

	public RTCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		TestingRecord record = (TestingRecord) element;
		switch (property) {
		case RTColumnInfo.NUM:
			return record.getNo();
		case RTColumnInfo.METHOD:
			return record.getMethod();
		case RTColumnInfo.STATUS:
			return record.getStatus();
		case RTColumnInfo.SEVERITY:
			return record.getSeverity();
		case RTColumnInfo.EXPECTSTATUS:
			return record.getExpectedStatus();
		case RTColumnInfo.EXPECTMSG:
			return record.getExpectedMsg();
		case RTColumnInfo.ACTUALSTATUS:
			return record.getActualStatus();
		case RTColumnInfo.ACTUALMSG:
			return record.getActualMsg();			
		case RTColumnInfo.URL:
			return record.getUrl();
		case RTColumnInfo.HEAD:
			return record.getHead();
		case RTColumnInfo.DATA:
			return record.getData();
		default:
			return "";
		}
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();
		TestingRecord p = (TestingRecord) element;
		switch (property) {
		case RTColumnInfo.URL:
			p.setUrl((String) value);
			break;
		case RTColumnInfo.HEAD:
			p.setHead((String) value);
			break;
		case RTColumnInfo.DATA:
			p.setData((String) value);
			break;
		case RTColumnInfo.NUM:
		case RTColumnInfo.METHOD:
		case RTColumnInfo.STATUS:
		case RTColumnInfo.SEVERITY:
		case RTColumnInfo.EXPECTSTATUS:
		case RTColumnInfo.EXPECTMSG:
		case RTColumnInfo.ACTUALSTATUS:
		case RTColumnInfo.ACTUALMSG:
		default:
		}   
	    viewer.refresh();
	}
}

