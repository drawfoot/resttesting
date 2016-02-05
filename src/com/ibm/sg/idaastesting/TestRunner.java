package com.ibm.sg.idaastesting;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.ibm.sg.idaastesting.model.TestingRecord;
import com.ibm.sg.idaastesting.model.TestingRecordList;
import com.ibm.sg.idaastesting.model.TestingRecordParser;
import com.ibm.sg.idaastesting.network.RestHttpClient;
import com.ibm.sg.idaastesting.network.RestRequest;
import com.ibm.sg.idaastesting.network.RestResponse;
import com.ibm.sg.idaastesting.resulttable.RTColumnInfo;
import com.ibm.sg.idaastesting.resulttable.RTTableViewer;

public class TestRunner {
	private AppWindow app;
	private RTTableViewer resultTableViewer;
	
	public TestRunner(AppWindow app) {
		this.app = app;
		
	}

	public void runSingleCheckedRecords() {
		final RestHttpClient httpclient = new RestHttpClient();
		resultTableViewer = app.getResultViewer();
		getRunTestingTask(app.getTestingRecordList().getModel(), 1, httpclient).start();
	}
	public void runMultipleRunner() {
		resultTableViewer = app.getResultViewer();
		for(TestingRecordList list: app.getTestingRecordLists()) {
			final RestHttpClient httpclient = new RestHttpClient();
			getRunTestingTask(list.getModel(), app.getMultipleRunRepeatTimes(), httpclient)
					.start();
		}
	}	

	private Thread getRunTestingTask(final List<TestingRecord> records,
			final int repeat, final RestHttpClient httpclient) {
		return new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < repeat; i++) {
					for (final TestingRecord data : records) {
						// set status to running
						data.setStatus(TestingRecord.STATUS_RUNNING);
						asyncUpdateResult(data);

						// ignore
						if (data.getNo() == null || !data.isChecked()) {
							data.setStatus(TestingRecord.STATUS_RUN_NOT_START);
							asyncUpdateResult(data);
							continue;
						}

						// TODO: extract to utility function
						RestRequest restRequest = new RestRequest();
						restRequest.setEndpointUrl(data.getUrl());
						restRequest.setUsername(data.getUser());
						restRequest.setPassword(data.getPass());
						restRequest.setBody(data.getData());
						restRequest.setMethod(data.getMethod().toLowerCase());
						HashMap<String, String> headers = TestingRecordParser
								.getHeader(data.getHead());
						restRequest.setHeaderMap(headers);
						RestResponse response;
						try {
							response = httpclient.doRequest(restRequest);
							data.setActualStatus(String.valueOf(response
									.getStatusCode()));
							data.setActualMsg(response.getBody());
							TestingRecordParser.discoverRunningStatus(data);
						} catch (Exception e) {
							String msg;
							if (e.getMessage() != null)
								msg = TestingRecord.STATUS_RUN_ERROR + ": "
										+ e.getMessage();
							else
								msg = TestingRecord.STATUS_RUN_ERROR;
							data.setStatus(msg);
						}

						// final update
						asyncUpdateResult(data);
					}
				}
			}// run
		};// thread
	}

	private void asyncUpdateResult(final TestingRecord record) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				resultTableViewer.getViewer().update(record,
						RTColumnInfo.COL_PROPS);
			} // run
		}); // Runnable
	}

}
