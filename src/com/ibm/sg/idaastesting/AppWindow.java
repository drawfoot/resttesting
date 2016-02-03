package com.ibm.sg.idaastesting;

import java.util.HashMap;
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
import com.ibm.sg.idaastesting.model.TestingRecordParser;
import com.ibm.sg.idaastesting.network.RestHttpClient;
import com.ibm.sg.idaastesting.network.RestRequest;
import com.ibm.sg.idaastesting.network.RestResponse;
import com.ibm.sg.idaastesting.resulttable.RTColumnInfo;
import com.ibm.sg.idaastesting.resulttable.RTTableViewer;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.RowLayout;

public class AppWindow extends ApplicationWindow {
	private TextViewer textViewer;
	private RTTableViewer resultTableViewer;
	private TestingRecordList recordList = new TestingRecordList();
	private TestingRecordParser recordParser = new TestingRecordParser();
	private RestHttpClient httpclient = new RestHttpClient();
	private Text txtSearchText;
	private Text text;

	// TODO: performance and user experience GUI no responding if the network
	// hang when running test,
	// TODO: filtering and ids config and default config
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
		sashForm.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_BACKGROUND));

		// upper
		SashForm sashForm_upper = new SashForm(sashForm, SWT.NONE);

		Group grpTestingScripts = new Group(sashForm_upper, SWT.NONE);
		grpTestingScripts.setText("Testing Scripts");
		grpTestingScripts.setLayout(new FillLayout(SWT.HORIZONTAL));

		// Script Viewer
		textViewer = new TextViewer(grpTestingScripts, SWT.BORDER | SWT.MULTI
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

		Button btnParseTestingScripts = new Button(sashForm_upper, SWT.NONE);
		btnParseTestingScripts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getParseScriptsTask().start();
			}
		});
		btnParseTestingScripts.setText("Parse");
		sashForm_upper.setWeights(new int[] { 10, 1 });

		SashForm sashForm_middle = new SashForm(sashForm, SWT.VERTICAL);

		// Middle
		Group grpParsedTestCases = new Group(sashForm_middle, SWT.NONE);
		grpParsedTestCases.setText("Parsed Scripts and Run Results");
		grpParsedTestCases.setLayout(new FillLayout(SWT.VERTICAL));
		// grpParsedTestCases.setLayout(new TableColumnLayout());
		resultTableViewer = new RTTableViewer(grpParsedTestCases, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI, recordList);

		Composite composite = new Composite(sashForm_middle, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));

		Label lblNewLabel = new Label(composite, SWT.HORIZONTAL | SWT.CENTER);
		lblNewLabel.setText("Seach By (No./Status/URL/Head)");

		txtSearchText = new Text(composite, SWT.BORDER);
		txtSearchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				resultTableViewer.getFilter().setSearchText(
						txtSearchText.getText());
				resultTableViewer.getViewer().refresh();
			}
		});
		txtSearchText.selectAll();

		Label lblNewLabel_1 = new Label(composite, SWT.HORIZONTAL | SWT.CENTER);
		lblNewLabel_1.setText("[ID] Placeholder Replacement");

		Combo combo = new Combo(composite, SWT.READ_ONLY);

		text = new Text(composite, SWT.BORDER);
		sashForm_middle.setWeights(new int[] {10, 1});

		// Bottom
		SashForm sashForm_lower = new SashForm(sashForm, SWT.VERTICAL);
		SashForm sashFormBottomL = new SashForm(sashForm_lower, SWT.HORIZONTAL);
		final Button btnCheckAll = new Button(sashFormBottomL, SWT.BORDER
				| SWT.FLAT | SWT.CHECK | SWT.CENTER);
		btnCheckAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckAll.getSelection() == true)
					resultTableViewer.checkAll(true);
				else
					resultTableViewer.checkAll(false);
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
						.getViewer().getSelection();
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
						.getViewer().getSelection();
				for (Object element : selection.toArray()) {
					resultTableViewer.setChecked(element, false);
				}
			}
		});
		btnUncheckSelected.setText("Uncheck Selected");

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
		
		Button btnMultipleRunSetup = new Button(sashFormBottomL, SWT.NONE);
		btnMultipleRunSetup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MultiRunDialog dialog = new MultiRunDialog(getShell(), recordList);
				dialog.create();
				dialog.open();
			}
		});
		btnMultipleRunSetup.setText("Multiple Run Setup");
		Button btnRunTestCases = new Button(sashFormBottomL, SWT.NONE);
		btnRunTestCases.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getRunTestingTask().start();
			}
		});
		btnRunTestCases.setText("Run");
		sashFormBottomL.setWeights(new int[] {1, 1, 1, 1, 1});
		sashForm.setWeights(new int[] { 1, 4, 1 });

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
						resultTableViewer.getViewer().refresh();

						StyledText recordsText = textViewer.getTextWidget();
						int totallines = recordsText.getLineCount();
						int index = 0;
						String line;
						while (index < totallines) {
							line = recordsText.getLine(index).trim();
							index++;
							if (line.equals(""))
								continue;
							TestingRecord record = new TestingRecord();
							recordParser.parseRecord(line, record);
							recordList.addTestingRecord(record);
							resultTableViewer.getViewer().add(record);
						}// while
						resultTableViewer.adjustColumnWidth();
						// TODO
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
						Object[] items = resultTableViewer.getViewer()
								.getCheckedElements();
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
							HashMap<String, String> headers = recordParser
									.getHeader(data.getHead());
							restRequest.setHeaderMap(headers);
							RestResponse response;
							try {
								response = httpclient.doRequest(restRequest);
								data.setActualStatus(String.valueOf(response
										.getStatusCode()));
								data.setActualMsg(response.getBody());
								recordParser.discoverRunningStatus(data);
							} catch (Exception e) {
								String msg;
								if (e.getMessage() != null)
									msg = TestingRecord.STATUS_RUN_ERROR + ": "
											+ e.getMessage();
								else
									msg = TestingRecord.STATUS_RUN_ERROR;
								data.setStatus(msg);
							}

							resultTableViewer.getViewer().update(data,
									RTColumnInfo.COL_PROPS);
						} // for
					} // run
				}); // Runnable
			}// run
		};// thread
	}// getRunTestingTask
}
