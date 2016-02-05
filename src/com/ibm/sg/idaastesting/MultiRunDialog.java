package com.ibm.sg.idaastesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ibm.sg.idaastesting.model.TestingRecord;
import com.ibm.sg.idaastesting.model.TestingRecordList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MultiRunDialog extends Dialog {
	private ListViewer listViewerAllItems;
	private ListViewer listViewerRunners;
	private ListViewer listViewerRunnerItems;
	private Button btnAddToRunner;
	private Button btnDelFromRunner;
	private Button btnAddNewRunner;
	private static TestingRecordList allItems = new TestingRecordList();
	private static Map<String, TestingRecordList> runnersMap = new HashMap<String, TestingRecordList>();
	private Text textRepeat;
	private static int repeatTimes = 1;

	public MultiRunDialog(Shell parentShell, AppWindow app) {
		
		// pass to parent
		super(parentShell);
		
		// use old records
		if(!allItems.isEmpty())
			return;
		for (TestingRecordList runnerList : runnersMap.values())
			if (!runnerList.isEmpty())
				return;

		// initialize
		allItems.addTestingRecord(app.getTestingRecordList().getModel());
		runnersMap.put("runner1", new TestingRecordList());
		runnersMap.put("runner2", new TestingRecordList());	
	}

	public static TestingRecordList[] getRunnerRecords() {
		return runnersMap.values().toArray(
				new TestingRecordList[runnersMap.values().size()]);
	}

	public static void reset() {
		allItems.reset();
		for (TestingRecordList list : runnersMap.values()) {
			list.reset();
		}
	}

	public static int getRepeatTimes() {
		return repeatTimes;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(5, true));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("All Records");
		new Label(container, SWT.NONE);
		
		Label lblRunners = new Label(container, SWT.NONE);
		lblRunners.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblRunners.setText("Runners");
		new Label(container, SWT.NONE);
		
		Label lblRunnerRecords = new Label(container, SWT.NONE);
		lblRunnerRecords.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblRunnerRecords.setText("Runner Records");

		listViewerAllItems = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		List listAllItems = listViewerAllItems.getList();
		GridData gd_listAllItems = new GridData(SWT.LEFT, SWT.BOTTOM, false,
				false, 1, 1);
		gd_listAllItems.heightHint = 400;
		gd_listAllItems.widthHint = 80;
		listAllItems.setLayoutData(gd_listAllItems);

		Composite compositeAddToRunner = new Composite(container, SWT.NONE);
		compositeAddToRunner.setLayout(null);
		GridData gd_compositeAddToRunner = new GridData(SWT.CENTER, SWT.FILL,
				false, false, 1, 1);
		gd_compositeAddToRunner.widthHint = 80;
		compositeAddToRunner.setLayoutData(gd_compositeAddToRunner);

		btnAddToRunner = new Button(compositeAddToRunner, SWT.BORDER
				| SWT.ARROW | SWT.RIGHT);
		btnAddToRunner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection runnerSelection = (IStructuredSelection) listViewerRunners
						.getSelection();
				IStructuredSelection allitemSelection = (IStructuredSelection) listViewerAllItems
						.getSelection();
				if (!runnerSelection.isEmpty() && !allitemSelection.isEmpty()) {
					TestingRecordList runnerList = runnersMap
							.get(runnerSelection.getFirstElement());
					for(Object element: allitemSelection.toArray()) {
						allItems.removeTestingRecord((TestingRecord)element);
						runnerList.addTestingRecord((TestingRecord)element);
					}
					listViewerAllItems.refresh();
					listViewerRunnerItems.refresh();
				}
			}
		});
		btnAddToRunner.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_BLACK));
		btnAddToRunner.setBounds(5, 191, 70, 21);
		btnAddToRunner.setText("add to runner");
		
		Label lblAddRecords = new Label(compositeAddToRunner, SWT.WRAP);
		lblAddRecords.setBounds(5, 139, 65, 44);
		lblAddRecords.setText("Add Records to Runner");

		listViewerRunners = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		listViewerRunners
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						if (!selection.isEmpty()) {
							TestingRecordList runner = runnersMap
									.get(selection.getFirstElement());
							listViewerRunnerItems.setInput(runner.getModel());
							listViewerRunnerItems.refresh();
						}
					}
				});
		List listRunners = listViewerRunners.getList();
		GridData gd_listRunners = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_listRunners.widthHint = 80;
		gd_listRunners.heightHint = 400;
		listRunners.setLayoutData(gd_listRunners);

		Composite compositeDelFromRunner = new Composite(container, SWT.NONE);
		compositeDelFromRunner.setLayout(null);
		GridData gd_compositeDelFromRunner = new GridData(SWT.CENTER, SWT.FILL,
				false, false, 1, 1);
		gd_compositeDelFromRunner.widthHint = 80;
		compositeDelFromRunner.setLayoutData(gd_compositeDelFromRunner);

		btnDelFromRunner = new Button(compositeDelFromRunner, SWT.BORDER
				| SWT.ARROW);
		btnDelFromRunner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection runnerSelection = (IStructuredSelection) listViewerRunners
						.getSelection();
				IStructuredSelection runnerItemSelection = (IStructuredSelection) listViewerRunnerItems
						.getSelection();
				if (!runnerSelection.isEmpty() && !runnerItemSelection.isEmpty()) {
					TestingRecordList runnerList = runnersMap
							.get(runnerSelection.getFirstElement());
					for(Object element: runnerItemSelection.toArray()) {
						allItems.addTestingRecord((TestingRecord)element);
						runnerList.removeTestingRecord((TestingRecord)element);
					}
					listViewerAllItems.refresh();
					listViewerRunnerItems.refresh();
				}				
			}
		});
		btnDelFromRunner.setAlignment(SWT.LEFT);
		btnDelFromRunner.setBounds(5, 191, 70, 21);
		btnDelFromRunner.setText("remove from runner");
		
		Label lblNewLabel_1 = new Label(compositeDelFromRunner, SWT.WRAP);
		lblNewLabel_1.setBounds(5, 139, 70, 44);
		lblNewLabel_1.setText("Remove Records from Runner");

		listViewerRunnerItems = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		List listRunnerItems = listViewerRunnerItems.getList();
		GridData gd_listRunnerItems = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_listRunnerItems.heightHint = 400;
		gd_listRunnerItems.widthHint = 80;
		listRunnerItems.setLayoutData(gd_listRunnerItems);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
				btnAddNewRunner = new Button(container, SWT.NONE);
				btnAddNewRunner.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						InputDialog dlg = new InputDialog(getShell(), "",
								"Enter Runner Name", null, null);
						if (dlg.open() == Window.OK) {
							runnersMap.put(dlg.getValue(), new TestingRecordList());
							listViewerRunners.setInput(new ArrayList<String>(runnersMap
									.keySet()));
							listViewerRunners.refresh();
						}
					}
				});
				btnAddNewRunner.setText("Add New Runner");
		
		Button btnRemoveRunner = new Button(container, SWT.NONE);
		btnRemoveRunner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection runnerSelect = (IStructuredSelection)listViewerRunners.getSelection();
				if(!runnerSelect.isEmpty()) {
					String runner = (String)runnerSelect.getFirstElement();
					TestingRecordList runnerItems = runnersMap.get(runner);
					allItems.addTestingRecord(runnerItems.getModel());
					listViewerAllItems.refresh();					
					runnerItems.reset();
					listViewerRunnerItems.refresh();
					runnersMap.remove(runner);
					listViewerRunners.remove(runner);
				}
			}
		});
		btnRemoveRunner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemoveRunner.setText("Remove Runner");
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection runnerSelect = (IStructuredSelection)listViewerRunners.getSelection();
				if(!runnerSelect.isEmpty()) {
					String runner = (String)runnerSelect.getFirstElement();
					
					InputDialog dlg = new InputDialog(getShell(), "",
							"Enter Runner Name", runner, null);
					if (dlg.open() == Window.OK) {
						TestingRecordList oldList = runnersMap.get(runner);
						runnersMap.remove(runner);
						runnersMap.put(dlg.getValue(), oldList);
						listViewerRunners.setInput(new ArrayList<String>(runnersMap
								.keySet()));
						listViewerRunners.refresh();
					}
				}				
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Rename Runner");
		
		Label lblRepeatTimes = new Label(container, SWT.NONE);
		lblRepeatTimes.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRepeatTimes.setText("Repeat Times");
		
		textRepeat = new Text(container, SWT.BORDER);
		textRepeat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		initListViewerProviders(listViewerAllItems, allItems.getModel());
		initListViewerProviders(listViewerRunners, new ArrayList<String>(runnersMap.keySet()));
		initListViewerProviders(listViewerRunnerItems, null);
		Object element = listViewerRunners.getElementAt(0);
		if(element!=null) {
			listViewerRunners.setSelection(new StructuredSelection(
					listViewerRunners.getElementAt(0)), true);
		}
		return container;
	}

	@Override
	protected void okPressed() {
		try {
			repeatTimes = Integer.parseInt(textRepeat.getText());
			if(repeatTimes<1) {
				repeatTimes = 1;
				MessageDialog.openInformation(getShell(), "",
						"repeat number should be bigger than 0");
				return;
			}
		} catch(Exception e) {
			repeatTimes = 1;
			MessageDialog.openInformation(getShell(), "",
					"repeat number not valid");
			return;
		}
		super.okPressed();
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Multiple Runner Config");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(556, 563);
	}

	@SuppressWarnings("rawtypes")
	private void initListViewerProviders(ListViewer list, Collection input) {
		list.setContentProvider(new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				return ((java.util.List) inputElement).toArray();
			}

			@Override
			public void dispose() {
				// do nothing
			}
			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// do nothing
			}
		});

		list.setLabelProvider(new LabelProvider() {
			@Override
			public Image getImage(Object element) {
				return null;
			}
			@Override
			public String getText(Object element) {
				if(element instanceof TestingRecord)
					return ((TestingRecord)element).getNo();
				else if (element instanceof String)
					return (String) element;
				return "";
			}
		});
		list.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof TestingRecord)
					return ((TestingRecord) e1).getNo().compareTo(
							((TestingRecord) e2).getNo());
				else if (e1 instanceof String)
					return ((String)e1).compareTo((String)e2);
				else 
					return 0;
			}
		});	
		if (input != null)
			list.setInput(input);
	}
}
