package com.ibm.sg.idaastesting.resulttable;

import com.ibm.sg.idaastesting.model.TestingRecord;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;



public class RTCellModifier implements ICellModifier {

	private Viewer viewer;

	public static final String NUM = "num";
	public static final String ACTION = "action";
	public static final String EXPECTSTATUS = "expectsc";
	public static final String EXPECTMSG = "expectmsg";
	public static final String ACTUALSTATUS = "actualsc";
	public static final String ACTUALMSG = "actualmsg";
	public static final String URL = "url";
	public static final String HEAD = "header";	
	public static final String DATA = "data";	
	public static final String[] PROPS = { NUM, ACTION, EXPECTSTATUS,
			EXPECTMSG, ACTUALSTATUS, ACTUALMSG, URL, HEAD, DATA };
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
	    if (URL.equals(property))
	      return record.getUrl();
	    else if(HEAD.equals(property))
	    	return record.getHead();
		return "";
	}

	@Override
	public void modify(Object element, String property, Object value) {
	    if (element instanceof Item) element = ((Item) element).getData();
	    
	    /*
	    TestingRecord p = (TestingRecord) element;
	    if (URL.equals(property))
	      p.setUrl((String) value);

	    viewer.refresh();
		*/
	}

}
