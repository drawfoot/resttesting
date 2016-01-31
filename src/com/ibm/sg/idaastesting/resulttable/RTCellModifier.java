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
	    if (RTColumnInfo.URL.equals(property))
	      return record.getUrl();
	    else if(RTColumnInfo.HEAD.equals(property))
	    	return record.getHead();
		return "";
	}

	@Override
	public void modify(Object element, String property, Object value) {
	    if (element instanceof Item) element = ((Item) element).getData();
	    
	    TestingRecord p = (TestingRecord) element;
	    if (RTColumnInfo.URL.equals(property))
	      p.setUrl((String) value);

	    viewer.refresh();
	}
}

