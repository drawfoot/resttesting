package com.ibm.sg.idaastesting;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ibm.sg.idaastesting.model.TestingRecordList;
import com.ibm.sg.idaastesting.model.TestingRecord;
import com.ibm.sg.idaastesting.resulttable.RTCellModifier;
import com.ibm.sg.idaastesting.resulttable.RTTable;
import com.ibm.sg.idaastesting.util.RestHttpClient;
import com.ibm.sg.idaastesting.util.RestRequest;
import com.ibm.sg.idaastesting.util.RestResponse;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class AppWindow extends ApplicationWindow {
	private TestingRecordList recordList = new TestingRecordList();
	private TextViewer textViewer;
	private RTTable resultTable;
	private RestHttpClient httpclient = new RestHttpClient();
	private Text text;

	//TODO: configuration
	private String DEFAULT_URL = "http://localhost:9080/com.ibm.security.access.idaas.rest.services";
	private String DEFAULT_TENANT = "1.wga1.ibmcloudsecurity.com";
	
	//TODO: support for method POST, PUT and GET
	//TODO: GUI no responding if the network hang when running test, 
	//TODO: filter and sorting
	//TODO: export xsl and report graph
	//TODO: scripts manager
	//TODO: failure status highlight in result table
	
	
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
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		sashForm.setSashWidth(2);

		Group grpTestingScripts = new Group(sashForm, SWT.NONE);
		grpTestingScripts.setText("Testing Scripts");
		grpTestingScripts.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		// upper
		SashForm sashForm_upper = new SashForm(grpTestingScripts, SWT.NONE);
		
				Button btnOpen = new Button(sashForm_upper, SWT.NONE);
				btnOpen.setText("Open");
		
				Button btnSave = new Button(sashForm_upper, SWT.NONE);
				btnSave.setText("Save");
		
		// upper left
		textViewer = new TextViewer(sashForm_upper, SWT.BORDER | SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		textViewer.getTextWidget().setText(
				"35.11.1.1|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=ACE25-description|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1\"}|204||1|(�$) \n" +
				"35.11.1.2|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=[ACE25-description]|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1\"}|204||1|(�$) \n" +
				"35.11.2.1|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=ACE5-description|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1,ACE-group2\"}|204||1|(�$)\n" +
				"35.11.2.2|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=[ACE5-description]|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1,ACE-group2\"}|204||1|(�$)\n" +
				"35.11.3.1|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=ACE13-description|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"randomUsr\",\"iv-groups\":\"ACE-group1\"}|204||1|(�$) \n" +
				"35.11.3.2|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=[ACE13-description]|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"randomUsr\",\"iv-groups\":\"ACE-group1\"}|204||1|(�$)");
				
				// upper right
				Button btnParseTestingScripts = new Button(sashForm_upper, SWT.NONE);
				btnParseTestingScripts.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						getParseScriptsTask().start();
					}
				});
				btnParseTestingScripts.setText("Parse");
		sashForm_upper.setWeights(new int[] {41, 39, 550, 40});
		// upper end
		
		// bottom
		Group grpParsedTestCases = new Group(sashForm, SWT.NONE);
		grpParsedTestCases.setText("Parsed Scripts and Run Results");
		grpParsedTestCases.setLayout(new FillLayout(SWT.VERTICAL));
		SashForm sashForm_lower = new SashForm(grpParsedTestCases, SWT.VERTICAL);

		// bottom upper
		//Group group_resulttable = new Group(sashForm_lower, SWT.NONE);
		resultTable = new RTTable(sashForm_lower, SWT.BORDER
				| SWT.FULL_SELECTION|SWT.MULTI, recordList);
		
		//resultTable.getViewer().addSelectionChangedListener(listener);

		// bottom lower
		SashForm sashFormBottomL = new SashForm(sashForm_lower, SWT.HORIZONTAL);
		final Button btnCheckAll = new Button(sashFormBottomL, SWT.BORDER | SWT.FLAT
				| SWT.CHECK | SWT.CENTER);
		btnCheckAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnCheckAll.getSelection() == true)
					resultTable.getViewer().setAllChecked(true);
				else
					resultTable.getViewer().setAllChecked(false);					
			}
		});
		btnCheckAll.setSelection(true);
		btnCheckAll.setText("Check All");
		
		SashForm sashForm_1 = new SashForm(sashFormBottomL, SWT.VERTICAL);
		
		Button btnCheckSelected = new Button(sashForm_1, SWT.NONE);
		btnCheckSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) resultTable
						.getViewer().getSelection();
				for(Object element: selection.toArray()) {
					resultTable.getViewer().setChecked(element, true);
				}
			}
		});
		btnCheckSelected.setText("Check Selected");
		
		Button btnUncheckSelected = new Button(sashForm_1, SWT.NONE);
		btnUncheckSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) resultTable
						.getViewer().getSelection();
				for(Object element: selection.toArray()) {
					resultTable.getViewer().setChecked(element, false);
				}
			}
		});
		btnUncheckSelected.setText("Uncheck Selected");
		sashForm_1.setWeights(new int[] {1, 1});
		
		SashForm sashFormBottomLFilter = new SashForm(sashFormBottomL, SWT.VERTICAL);

		text = new Text(sashFormBottomLFilter, SWT.BORDER);
		text.setToolTipText("filter text");

		Combo combo = new Combo(sashFormBottomLFilter, SWT.NONE);
		combo.setText("filter by");
		sashFormBottomLFilter.setWeights(new int[] { 1, 1 });
				
						Button btnRunTestCases = new Button(sashFormBottomL, SWT.NONE);
						btnRunTestCases.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								getRunTestingTask().start();
							}
						});
						btnRunTestCases.setText("Run");
		
				Button btnConfiguration = new Button(sashFormBottomL, SWT.NONE);
				btnConfiguration.setText("Configuration");
		sashFormBottomL.setWeights(new int[] {1, 1, 1, 1, 1});
		sashForm_lower.setWeights(new int[] {5, 1});
		// bottom end
		
		sashForm.setWeights(new int[] { 1, 4 });

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
						while (index < totallines) {
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
						resultTable.getViewer().setAllChecked(true);						
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
						//TableItem[] items = resultTable.getViewer().getTable().getItems();
						Object[] items = resultTable.getViewer().getCheckedElements();
						for(Object item : items) {
							TestingRecord data = (TestingRecord)item;
							if(data.getNo() == null)
								continue;
							//TODO: extract to utility function
							RestRequest restRequest = new RestRequest();
							restRequest.setEndpointUrl(data.getUrl());
							restRequest.setUsername(data.getUser());
							restRequest.setPassword(data.getPass());
							restRequest.setBody(data.getData());
							restRequest.setMethod(data.getMethod().toLowerCase());
							HashMap<String, String> headers = getHeader(data.getHead());
							restRequest.setHeaderMap(headers);
							RestResponse response;

							response = httpclient.doRequest(restRequest);
							data.setActualStatus(String.valueOf(response
									.getStatusCode()));
							data.setActualMsg(response.getBody());
							resultTable.getViewer().update(data,
									RTCellModifier.PROPS);						
						}
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
		record.setUrl(parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",DEFAULT_URL).replaceAll("#", "|"));
		record.setUser(parts[5]);
		record.setPass(parts[6]);
		record.setHead(parts[7].replaceAll("\\[tenant\\]", DEFAULT_TENANT));
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

	private HashMap<String, String> getHeader(String headerstr) {
		HashMap<String, String> headers = new HashMap<String, String>();
		String[] entries = trimFirstAndLast(headerstr, "{", "}").split(",");
		String prevKey = "notvalid";
		for (String entry : entries) {
			String[] parts = entry.split(":");
			String candidateKey = trimFirstAndLast(parts[0], "\"", "\"");
			// first part is a value for previous key			
			if(parts.length == 1) {				
				if(headers.containsKey(prevKey))
					headers.put(prevKey, headers.get(prevKey) + "," + candidateKey);
			// first part is a real key				
			} else {
				prevKey = candidateKey;				
				String value = trimFirstAndLast(parts[1], "\"", "\"");
				headers.put(candidateKey, value);
			}
		}
		return headers;
	}

	private String trimFirstAndLast(String str, String lch, String rch) {
		if(str == null)
			return null;
		
		if(str.startsWith(lch))
			str = str.substring(1, str.length());
		if(str.endsWith(rch))
			return str.substring(0, str.length()-1);

		return str;
	}
	
}
