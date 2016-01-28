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
import org.eclipse.jface.text.JFaceTextUtil;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ibm.sg.idaastesting.model.TestingRecordList;
import com.ibm.sg.idaastesting.model.TestingRecord;
import com.ibm.sg.idaastesting.resulttable.RTTable;

public class AppWindow extends ApplicationWindow {
	private TestingRecordList recordList = new TestingRecordList();
	private TextViewer textViewer;
	private RTTable resultTable;

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

		SashForm sashForm = new SashForm(container, SWT.BORDER | SWT.VERTICAL);
		sashForm.setSashWidth(2);

		Group grpTestingScripts = new Group(sashForm, SWT.NONE);
		grpTestingScripts.setText("Testing Scripts");
		grpTestingScripts.setLayout(new FillLayout(SWT.HORIZONTAL));

		// Script Text Box
		textViewer = new TextViewer(grpTestingScripts, SWT.BORDER
				| SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		// Checked Table Viewer
		Group grpParsedTestCases = new Group(sashForm, SWT.NONE);
		grpParsedTestCases.setText("Parsed Scripts and Run Results");
		grpParsedTestCases.setLayout(new FillLayout(SWT.HORIZONTAL));
		resultTable = new RTTable(grpParsedTestCases, SWT.BORDER
				| SWT.FULL_SELECTION, recordList);

		// Action Buttons
		Group group = new Group(sashForm, SWT.NONE);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button btnParseTestingScripts = new Button(group, SWT.NONE);
		btnParseTestingScripts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getParseScriptsTask().start();				
			}
		});
		btnParseTestingScripts.setText("Parse");

		Button btnRunTestCases = new Button(group, SWT.NONE);
		btnRunTestCases.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnRunTestCases.setText("Run");

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

	public Thread getParseScriptsTask() {
		return new Thread() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// clear old data
						recordList.reset();
						resultTable.getViewer().refresh();

						StyledText recordsText = textViewer.getTextWidget();
						int totallines = recordsText.getLineCount();
						int index = 0;
						String line;						
						while (index <= totallines) {
								line = recordsText.getLine(index).trim();
								index++;
								if(line.equals(""))
									continue;
								TestingRecord record = new TestingRecord(null, null);
								parseRecord(line, record);
								recordList.addTestingRecord(record);
								resultTable.getViewer().add(record);
						}// while
						resultTable.adjustColumnWidth();
					} // run
				}); // Runnable
			}// run
		};// thread
	}// getParseScriptsTask
	
	public Thread getRunTestingTask() {
		return new Thread() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						//TODO
					} // run
				}); // Runnable
			}// run
		};// thread
	}// getRunTestingTask

	
	void parseRecord(String str, TestingRecord record) {
		String[] parts = str.split("\\|");
		if(parts[1].equals("DELETE")) {
			parseRecordDelete(parts, record);
		}
	}
	
// no.0|delete1|host2|lmi3|url4|user5|pass6|header7|expect_sc8|9|number(?)10|expect msg11
	void parseRecordDelete(String[] parts, TestingRecord record) {	
		record.setNo(parts[0]);
		record.setMethod(parts[1]);
		
		String url = parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",
				"com.ibm.security.access.idaas.rest.services");
		url = url.replaceAll("#", "|");
		String host = "http://localhost:9080";
		record.setUrl(String.format("%s/%s", host, url));
		
		record.setHead(parts[7]);
		record.setExpectedStatus(parts[8]);
		record.setExpectedMsg(parts[11]);
	}
	
	void parseRecordGET(String[] parts, TestingRecord record) {
		//TODO
	}
	
	void parseRecorPUT(String[] parts, TestingRecord record) {
		//TODO
	}
	
	void parseRecordPOST(String[] parts, TestingRecord record) {
		//TODO
	}
}
