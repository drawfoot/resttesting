package com.ibm.sg.idaastesting.resulttable;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class RTContentProvider implements IStructuredContentProvider {
    @SuppressWarnings("rawtypes")
	@Override
    public Object[] getElements(Object inputElement) {
        // The inputElement comes from view.setInput()
        return ((List) inputElement).toArray();
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}
}