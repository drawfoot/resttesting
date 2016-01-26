package com.ibm.sg.idaastesting.view;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ibm.sg.idaastesting.model.TestingRecordList;
import com.ibm.sg.idaastesting.model.TestingRecord;
import com.ibm.sg.idaastesting.resulttable.RTContentProvider;
import com.ibm.sg.idaastesting.resulttable.RTLabelProvider;

public class AppWindow extends ApplicationWindow {
	private TestingRecordList tableModel;

	/**
	 * Create the application window,
	 */
	public AppWindow() {
		super(null);
		setShellStyle(SWT.SHELL_TRIM);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.EMBEDDED);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(container, SWT.BORDER | SWT.SMOOTH
				| SWT.VERTICAL);

		Group grpTestingScripts = new Group(sashForm, SWT.NONE);
		grpTestingScripts.setText("Testing Scripts");
		grpTestingScripts.setLayout(new FillLayout(SWT.HORIZONTAL));

		// Script Text Box
		TextViewer textViewer = new TextViewer(grpTestingScripts, SWT.BORDER
				| SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		StyledText styledText = textViewer.getTextWidget();

		// Checked Table Viewer
		Group grpParsedTestCases = new Group(sashForm, SWT.NONE);
		grpParsedTestCases.setText("Parsed Scripts and Run Results");
		grpParsedTestCases.setLayout(new FillLayout(SWT.HORIZONTAL));

		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer
				.newCheckList(grpParsedTestCases, SWT.BORDER
						| SWT.FULL_SELECTION);
		Table table = checkboxTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tableColumnNo = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableColumnNo.getColumn();
		tblclmnNewColumn.setWidth(44);
		tblclmnNewColumn.setText("No.");

		TableViewerColumn tableColumnMethod = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_2 = tableColumnMethod.getColumn();
		tblclmnNewColumn_2.setWidth(71);
		tblclmnNewColumn_2.setText("Method");

		TableViewerColumn tableColumnExpStatus = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnExpectStatus = tableColumnExpStatus.getColumn();
		tblclmnExpectStatus.setWidth(100);
		tblclmnExpectStatus.setText("Expect Status");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_4 = tableViewerColumn_5.getColumn();
		tblclmnNewColumn_4.setWidth(94);
		tblclmnNewColumn_4.setText("Expect Msg");

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnActualStatus = tableViewerColumn_6.getColumn();
		tblclmnActualStatus.setWidth(100);
		tblclmnActualStatus.setText("Actual Status");

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnActualMsg = tableViewerColumn_7.getColumn();
		tblclmnActualMsg.setWidth(100);
		tblclmnActualMsg.setText("Actual Msg");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_1.getColumn();
		tblclmnNewColumn_1.setWidth(59);
		tblclmnNewColumn_1.setText("URL");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_3 = tableViewerColumn_3.getColumn();
		tblclmnNewColumn_3.setWidth(100);
		tblclmnNewColumn_3.setText("Data");

		checkboxTableViewer.setContentProvider(new RTContentProvider());
		checkboxTableViewer.setLabelProvider(new RTLabelProvider());
		tableModel = new TestingRecordList();
		checkboxTableViewer.setInput(tableModel.getModel());

		// Action Buttons
		Group group = new Group(sashForm, SWT.NONE);
		group.setEnabled(false);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button btnParseTestingScripts = new Button(group, SWT.NONE);
		btnParseTestingScripts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getParseScriptsTask(checkboxTableViewer).start();
			}
		});
		btnParseTestingScripts.setText("Parse Testing Scripts");

		Button btnRunTestCases = new Button(group, SWT.NONE);
		btnRunTestCases.setText("Run Test Cases");

		Button btnConfiguration = new Button(group, SWT.NONE);
		btnConfiguration.setText("Configuration");
		sashForm.setWeights(new int[] { 3, 5, 1 });

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Idaas Testing Utility");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(705, 479);
	}

	public Thread getParseScriptsTask(final CheckboxTableViewer viewer) {
		return new Thread() {
			public void run() {
				// do parsing
				System.out.println("parsing");
				final TestingRecord element = new TestingRecord("3", "dynamics added");
				tableModel.addTestingRecord(element);
				
				// update viewer
				Display disp = Display.getDefault();
				disp.asyncExec(new Runnable() {
					public void run() {
						viewer.add(element);
					}
				});
			}

		};
	}
}
