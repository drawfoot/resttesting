package com.ibm.sg.idaastesting;

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
import com.ibm.sg.idaastesting.resulttable.RTColumnInfo;
import com.ibm.sg.idaastesting.resulttable.RTTableViewer;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Label;

public class AppWindow extends ApplicationWindow {
	private TestingRecordList recordList;
	private TestRunner testRunner;
	private TextViewer scriptTextViewer;
	private RTTableViewer resultTableViewer;
	private Text txtSearchText;
	private Button btnCheckMultiRunner;
	private boolean running = false;

	// TODO: id place holder
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
		recordList = new TestingRecordList();
		testRunner = new TestRunner(this);
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
		scriptTextViewer = new TextViewer(grpTestingScripts, SWT.BORDER
				| SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		StyledText styledText = scriptTextViewer.getTextWidget();
		styledText.setBlockSelection(true);
		scriptTextViewer
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
				| SWT.FULL_SELECTION | SWT.MULTI, this);
		sashForm_middle.setWeights(new int[] { 16 });

		// Bottom
		SashForm sashForm_lower = new SashForm(sashForm, SWT.VERTICAL);

		Composite composite = new Composite(sashForm_lower, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Label lblNewLabel = new Label(composite, SWT.CENTER);
		lblNewLabel.setText("Seach By (No./Status/URL/Head)");

		txtSearchText = new Text(composite, SWT.BORDER | SWT.SEARCH);
		txtSearchText.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		txtSearchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				resultTableViewer.getFilter().setSearchText(
						txtSearchText.getText());
				resultTableViewer.getViewer().refresh();
			}
		});
		txtSearchText.selectAll();
		SashForm sashFormBottomL = new SashForm(sashForm_lower, SWT.HORIZONTAL);

		SashForm sashForm_1 = new SashForm(sashFormBottomL, SWT.VERTICAL);
		final Button btnCheckAll = new Button(sashForm_1, SWT.BORDER | SWT.FLAT
				| SWT.CHECK | SWT.CENTER);
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
		sashForm_1.setWeights(new int[] { 1, 1, 1 });

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
				MultiRunDialog dialog = new MultiRunDialog(getShell(),
						AppWindow.this);
				dialog.create();
				dialog.open();
			}
		});
		btnMultipleRunSetup.setText("Multiple Run Setup");

		Composite composite_1 = new Composite(sashFormBottomL, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.VERTICAL));
		final Button btnRunTestCases = new Button(composite_1, SWT.NONE);
		btnRunTestCases.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				class Monitor extends Thread {
					private boolean monitoring = true;

					@Override
					public void run() {
						while (monitoring) {
							if (!testRunner.isAlive()) {
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										btnRunTestCases.setText("Run");
									}
								});
								running = false;
								monitoring = false;
							}
						}
					}
					
					public void kill() {
						monitoring = false;
					}
				};
				Monitor monitor = new Monitor();
				if (!running) {
					if (btnCheckMultiRunner.getSelection())
						testRunner.run(MultiRunDialog.getRunnerInfo(),
								MultiRunDialog.getRepeatTimes());
					else
						testRunner.run();
					monitor.start();
					running = true;
					btnRunTestCases.setText("Stop");
				} else {
					monitor.kill();
					testRunner.kill();
					running = false;
					btnRunTestCases.setText("Run");
				}
			}
		});
		btnRunTestCases.setText("Run");

		btnCheckMultiRunner = new Button(composite_1, SWT.CHECK | SWT.CENTER);
		btnCheckMultiRunner.setText("Run Multiple Runner");
		sashFormBottomL.setWeights(new int[] { 1, 1, 1, 1 });
		sashForm_lower.setWeights(new int[] { 1, 4 });
		sashForm.setWeights(new int[] { 2, 8, 3 });

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

	public TestingRecordList getTestingRecordList() {
		return recordList;
	}

	public int getMultipleRunRepeatTimes() {
		return MultiRunDialog.getRepeatTimes();
	}

	public RTTableViewer getResultViewer() {
		return resultTableViewer;
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
						MultiRunDialog.reset();
						resultTableViewer.getViewer().refresh();

						StyledText recordsText = scriptTextViewer
								.getTextWidget();
						int totallines = recordsText.getLineCount();
						int index = 0;
						String line;
						while (index < totallines) {
							line = recordsText.getLine(index).trim();
							index++;
							TestingRecord record = new TestingRecord();
							if(TestingRecordParser.parseRecord(line, record)) {
								recordList.addTestingRecord(record);
								resultTableViewer.getViewer().add(record);
							}
						}// while
						resultTableViewer.adjustColumnWidth();
						// TODO
					} // run
				}); // Runnable
			}// run
		};// thread
	}// getParseScriptsTask

}
