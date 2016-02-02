package com.ibm.sg.idaastesting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.jface.viewers.CheckboxTableViewer;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ibm.sg.idaastesting.config.ConfigDialog;
import com.ibm.sg.idaastesting.model.IdaasTestingConfig;
import com.ibm.sg.idaastesting.model.TestingRecordList;
import com.ibm.sg.idaastesting.model.TestingRecord;
import com.ibm.sg.idaastesting.resulttable.RTColumnInfo;
import com.ibm.sg.idaastesting.resulttable.RTTableViewer;
import com.ibm.sg.idaastesting.util.RestHttpClient;
import com.ibm.sg.idaastesting.util.RestRequest;
import com.ibm.sg.idaastesting.util.RestResponse;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.layout.TableColumnLayout;

public class AppWindow extends ApplicationWindow {
	private TestingRecordList recordList = new TestingRecordList();
	private TextViewer textViewer;
	private RTTableViewer resultTableViewer;
	private RestHttpClient httpclient = new RestHttpClient();

	// TODO: configuration
	private String DEFAULT_URL = "http://localhost:9080/com.ibm.security.access.idaas.rest.services";
	private String DEFAULT_TENANT = "1.wga1.ibmcloudsecurity.com";
	private Table table;

	// TODO: performance and user experience GUI no responding if the network
	// hang when running test,
	// TODO: failure status highlight in result table
	// TODO: configuration enhance and ids config
	// TODO: filtering
	// TODO: packaging

	// TODO: application state save
	// TODO: export xsl and report graph
	// TODO: scripts manager

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
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));

		Group grpTestingScripts = new Group(sashForm, SWT.NONE);
		grpTestingScripts.setText("Testing Scripts");
		grpTestingScripts.setLayout(new FillLayout(SWT.HORIZONTAL));

		// upper
		SashForm sashForm_upper = new SashForm(grpTestingScripts, SWT.NONE);
		
		// Script Viewer
		textViewer = new TextViewer(sashForm_upper, SWT.BORDER | SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		textViewer
				.getTextWidget()
				.setText(
						"35.11.1.1|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=ACE25-description|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1\"}|204||1|(ˆ$) \n"
								+ "35.11.1.2|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=[ACE25-description]|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1\"}|204||1|(ˆ$) \n"
								+ "35.11.3.1|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=ACE13-description|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"randomUsr\",\"iv-groups\":\"ACE-group1\"}|204||1|(ˆ$) \n"
								+ "35.11.2.1|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=ACE5-description|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1,ACE-group2\"}|204||1|(ˆ$)\n"
								+ "35.11.2.2|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=[ACE5-description]|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"ACE-group1,ACE-group2\"}|204||1|(ˆ$)\n"
								+ "17.1.1.2|GET|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=appid=subjectid=ACE1-user#access=allow#return=appid|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"admin\"}|200||1|ˆ\\[\\]$ \n"
								+ "17.3.3.3|GET|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=appid=[[appid],INVALID]#return=description|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"admin\"}|200||3|\\[{[ˆ}]*},{[ˆ}]*}\\]\n"
								+ "26.1|POST|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces|admin|wrongPswd|{\"content-type\":\"application/json\",\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"admin\"}|400|{}|1|{\"message\":\"Request body cannot be empty\"}\n"
								+ "26.2.2|POST|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces|admin|wrongPswd|{\"content-type\":\"application/json\",\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE26.2.2-user\",\"iv-groups\":\"admin\"}|400|{\"appid\":\"[id]\",\"description\": \"ACE26.2.2-description\",\"subjecttype\": \"user\",\"subjectiiiiiiiid\": \"ACE26.2.2-user\",\"isowner\": true,\"access\": \"allow\", \"comment\":\"ACE26.2.2-comment\"} |1|{\"message\":\"Malformed JSON. No such attribute: \\\"subjectiiiiiiiid\\\"\"}\n"
								+ "13.3.4|PUT|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces/[id]|admin|wrongPswd|{\"content-type\":\"application/json\",\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"admin\"}|400|{\"description\": \"ACE1-description\",\"isowner\": true,\"access\": \"allow1\",\"comment\": \"ACE1-comment-13.3.4\"}|3|{\"message\":\"Parameter is invalid: \\\"access\\\"\"}\n"
								+ "13.7.3|PUT|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces/[id]|admin|wrongPswd|{\"content-type\":\"application/json\",\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"ACE1-user\",\"iv-groups\":\"admin\"}|400|{\"appid\":\"[appid]\"}|3|{\"message\":\"The value for \\\"appid\\\" cannot be changed\"}\n"
								+ "35.11.3.2|DELETE|[lmi]|lmi|/v1/mgmt/idaas/sanctionedappaces?filter=description=[ACE13-description]|admin|wrongPswd|{\"accept\":\"application/json\",\"x-forwarded-host\":\"[tenant]\",\"iv-user\":\"randomUsr\",\"iv-groups\":\"ACE-group1\"}|204||1|(ˆ$)");
		sashForm_upper.setWeights(new int[] { 550 });
		
/*		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new TableColumnLayout());*/

		// Middle
//		Group grpParsedTestCases = new Group(sashForm, SWT.NONE);
//		grpParsedTestCases.setText("Parsed Scripts and Run Results");
//		grpParsedTestCases.setLayout(new FillLayout(SWT.VERTICAL));
		//grpParsedTestCases.setLayout(new TableColumnLayout());
		resultTableViewer = new RTTableViewer(sashForm, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		resultTableViewer.build();
		resultTableViewer.setInput(recordList.getModel());

		// Bottom
		SashForm sashForm_lower = new SashForm(sashForm, SWT.VERTICAL);
		SashForm sashFormBottomL = new SashForm(sashForm_lower, SWT.HORIZONTAL);

		Button btnParseTestingScripts = new Button(sashFormBottomL, SWT.NONE);
		btnParseTestingScripts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getParseScriptsTask().start();
			}
		});
		btnParseTestingScripts.setText("Parse");
		final Button btnCheckAll = new Button(sashFormBottomL, SWT.BORDER
				| SWT.FLAT | SWT.CHECK | SWT.CENTER);
		btnCheckAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckAll.getSelection() == true)
					resultTableViewer.setAllChecked(true);
				else
					resultTableViewer.setAllChecked(false);
			}
		});
		btnCheckAll.setSelection(true);
		btnCheckAll.setText("Check All");

		SashForm sashForm_1 = new SashForm(sashFormBottomL, SWT.VERTICAL);

		Button btnCheckSelected = new Button(sashForm_1, SWT.NONE);
		btnCheckSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) resultTableViewer
						.getSelection();
				for (Object element : selection.toArray()) {
					resultTableViewer.setChecked(element, true);
				}
			}
		});
		btnCheckSelected.setText("Check Selected");

		Button btnUncheckSelected = new Button(sashForm_1, SWT.NONE);
		btnUncheckSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) resultTableViewer
						.getSelection();
				for (Object element : selection.toArray()) {
					resultTableViewer.setChecked(element, false);
				}
			}
		});
		btnUncheckSelected.setText("Uncheck Selected");
		sashForm_1.setWeights(new int[] { 1, 1 });

		Button btnConfiguration = new Button(sashFormBottomL, SWT.NONE);
		btnConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConfigDialog dialog = new ConfigDialog(getShell());
				dialog.create();
				dialog.open();
			}
		});
		btnConfiguration.setText("Configuration");
		Button btnRunTestCases = new Button(sashFormBottomL, SWT.NONE);
		btnRunTestCases.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getRunTestingTask().start();
			}
		});
		btnRunTestCases.setText("Run");

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
		return new Point(682, 524);
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
						resultTableViewer.refresh();

						StyledText recordsText = textViewer.getTextWidget();
						int totallines = recordsText.getLineCount();
						int index = 0;
						String line;
						while (index < totallines) {
							line = recordsText.getLine(index).trim();
							index++;
							if (line.equals(""))
								continue;
							TestingRecord record = new TestingRecord(null, null);
							parseRecord(line, record);
							recordList.addTestingRecord(record);
							resultTableViewer.add(record);
						}// while
						resultTableViewer.adjustColumnWidth();
						resultTableViewer.setAllChecked(true);
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
						Object[] items = resultTableViewer.getCheckedElements();
						for (Object item : items) {
							TestingRecord data = (TestingRecord) item;
							if (data.getNo() == null)
								continue;
							// TODO: extract to utility function
							RestRequest restRequest = new RestRequest();
							restRequest.setEndpointUrl(data.getUrl());
							restRequest.setUsername(data.getUser());
							restRequest.setPassword(data.getPass());
							restRequest.setBody(data.getData());
							restRequest.setMethod(data.getMethod()
									.toLowerCase());
							HashMap<String, String> headers = getHeader(data
									.getHead());
							restRequest.setHeaderMap(headers);
							RestResponse response;
							try {
								response = httpclient.doRequest(restRequest);
								data.setActualStatus(String.valueOf(response
										.getStatusCode()));
								data.setActualMsg(response.getBody());
								// status update
								if (data.getExpectedStatus().equals(
										response.getStatusCode())) {
									data.setStatus(TestingRecord.STATUS_RUN_SUCCESS);
								} else {
									data.setStatus(TestingRecord.STATUS_RUN_FAILED);
								}
							} catch (Exception e) {
								data.setStatus(TestingRecord.STATUS_RUN_ERROR
										+ e.getMessage());
							}

							resultTableViewer.update(data,
									RTColumnInfo.COL_PROPS);
						}
					} // run
				}); // Runnable
			}// run
		};// thread
	}// getRunTestingTask

	void parseRecord(String str, TestingRecord record) {
		record.setStatus(TestingRecord.STATUS_PARSING);
		String[] parts = str.split("\\|");
		record.setNo(parts[0]);
		record.setMethod(parts[1]);
		record.setUser(parts[5]);
		record.setPass(parts[6]);
		record.setExpectedStatus(parts[8]);
		record.setExpectedMsg(parts[11]);

		Properties prop;
		try {
			prop = IdaasTestingConfig.getInstance().getProp();

			// host
			String hostmode = prop.getProperty(IdaasTestingConfig.CFG_HOSTMODE);
			if (hostmode == null || hostmode.equals("local")) {
				record.setUrl(parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",
						DEFAULT_URL).replaceAll("#", "|"));
			} else if (prop.getProperty(IdaasTestingConfig.CFG_HOST) != null) {
				record.setUrl(parts[4].replaceAll(
						"\\/v1\\/mgmt\\/idaas",
						String.format("https://%s:443/v1/mgmt/idaas",
								prop.getProperty(IdaasTestingConfig.CFG_HOST)))
						.replaceAll("#", "|"));
			} else {
				record.setUrl(parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",
						DEFAULT_URL).replaceAll("#", "|"));
			}

			// tenant
			if (prop.getProperty(IdaasTestingConfig.CFG_TENANT1) != null) {
				record.setHead(parts[7].replaceAll("\\[tenant\\]",
						prop.getProperty(IdaasTestingConfig.CFG_TENANT1)));
			} else {
				record.setHead(parts[7].replaceAll("\\[tenant\\]",
						DEFAULT_TENANT));
			}
		} catch (IOException e) {
			record.setStatus(TestingRecord.STATUS_PARSE_ERROR);
			e.printStackTrace();
		}

		record.setData(parts[9]);
		record.setStatus(TestingRecord.STATUS_PARSED);
	}

	// TODO: move to another class
	private HashMap<String, String> getHeader(String headerstr) {
		HashMap<String, String> headers = new HashMap<String, String>();
		String[] entries = trimFirstAndLast(headerstr, "{", "}").split(",");
		String prevKey = "notvalid";
		for (String entry : entries) {
			String[] parts = entry.split(":");
			String candidateKey = trimFirstAndLast(parts[0], "\"", "\"");
			// first part is a value for previous key
			if (parts.length == 1) {
				if (headers.containsKey(prevKey))
					headers.put(prevKey, headers.get(prevKey) + ","
							+ candidateKey);
				// first part is a real key
			} else {
				prevKey = candidateKey;
				String value = trimFirstAndLast(parts[1], "\"", "\"");
				headers.put(candidateKey, value);
			}
		}
		return headers;
	}

	// TODO: move to utility class
	private String trimFirstAndLast(String str, String lch, String rch) {
		if (str == null)
			return null;

		if (str.startsWith(lch))
			str = str.substring(1, str.length());
		if (str.endsWith(rch))
			return str.substring(0, str.length() - 1);

		return str;
	}
}
