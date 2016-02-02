package com.ibm.sg.idaastesting.resulttable;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.ibm.sg.idaastesting.model.TestingRecord;

public class RTFilter extends ViewerFilter {
	private String searchString;

	  public void setSearchText(String s) {
	    // ensure that the value can be used for matching 
	    this.searchString = ".*" + s + ".*";
	  }

	  @Override
	  public boolean select(Viewer viewer, 
	      Object parentElement, 
	      Object element) {
	    if (searchString == null || searchString.length() == 0) {
	      return true;
	    }
	    TestingRecord rec = (TestingRecord) element;
	    if (rec.getNo().matches(searchString)) {
	      return true;
	    }
	    if (rec.getStatus().matches(searchString)) {
		      return true;
		    }	    
	    if (rec.getUrl().matches(searchString)) {
	      return true;
	    }
	    if (rec.getHead().matches(searchString)) {
		      return true;
		}	    
	    return false;
	  }
}
