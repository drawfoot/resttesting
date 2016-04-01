package com.ibm.sg.idaastesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private static final Map<String, String> epIds = new HashMap<String,String>();
	private List<TestingRunnable> runnables = new ArrayList<TestingRunnable>();

	public TestRunner(AppWindow app) {
		this.app = app;
	}
	
	public void createUpdateEpId(String ep, String id) {
		//synchronized (epIds) {
			epIds.put(ep, id);
		//}
	}
	
	public String getEpId(String ep) {
		//synchronized (epIds) {
			return epIds.get(ep);
		//}
	}	

	public void run() {
		runnables.clear();
		RestHttpClient httpClient = new RestHttpClient();
		resultTableViewer = app.getResultViewer();
		TestingRunnable runnable = new TestingRunnable("default", app.getTestingRecordList().getModel(),
				1, httpClient);
		runnable.start();
		runnables.add(runnable);
	}

	public void run(Map<String, TestingRecordList> infoMap, int repeat) {
		resultTableViewer = app.getResultViewer();
		runnables.clear();
		for(Map.Entry<String, TestingRecordList> entry: infoMap.entrySet()) {
			RestHttpClient httpClient = new RestHttpClient();
			TestingRunnable runnable = new TestingRunnable(entry.getKey(), entry.getValue().getModel(),
					repeat, httpClient);
			runnable.start();
			runnables.add(runnable);
		}
	}
	
	public void kill() {
		for(TestingRunnable runnable: runnables) {
			runnable.stop();
			runnable.join();
		}
	}

	public boolean isAlive() {
		for(TestingRunnable runnable: runnables) {
			if(runnable.isAlive())
				return true;
		}
		return false;
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

	class TestingRunnable implements Runnable {
		public Thread t;
		private String threadName;
		private List<TestingRecord> records;
		private RestHttpClient httpClient;
		private int repeat;
		boolean suspended = false;
		boolean stopped = false;		

		TestingRunnable(String threadName, List<TestingRecord> records,
				int repeat, RestHttpClient httpClient) {
			this.threadName = threadName;
			this.records = records;
			this.repeat = repeat;
			this.httpClient = httpClient;
		}

		public void run() {
			System.out.println("Running " + threadName);
			try {
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
						String epId = getEpId(data.getEndpoint());
						if (epId != null)
							restRequest.setEndpointUrl(data.getUrl().replace(
									"[id]", epId));
						else
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
							response = httpClient.doRequest(restRequest);
							data.setActualStatus(String.valueOf(response
									.getStatusCode()));
							data.setActualMsg(response.getBody());
							// update result
							TestingRecordParser.discoverRunningStatus(data);
							if (data.getActualStatus().equals("201")
									&& restRequest.getMethod().equals(
											RestRequest.METHOD_POST)) {
								createUpdateEpId(data.getEndpoint(),
										httpClient.getResponseId(response));
							}
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

						// suspend
						synchronized (this) {
							while (suspended) {
								wait();
							}
						}

						// stop
						synchronized (this) {
							if (stopped) {
								return;
							}
						}
						
					} // go through records
				} // repeat
			} catch (InterruptedException e) {
				System.out.println("Thread " + threadName + " interrupted.");
			}
			System.out.println("Thread " + threadName + " exiting.");
		}

		public void start() {
			System.out.println("Starting " + threadName);
			if (t == null) {
				t = new Thread(this, threadName);
				t.start();
			}
		}

		void suspend() {
			suspended = true;
		}
			
		synchronized void resume() {
			suspended = false;
			notify();
		}
		
		public void stop() {
			stopped = true;
		}
		
		public boolean isAlive() {
			return t.isAlive();
		}
		
		public void join() {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Thread " + threadName + " interrupted.");
				e.printStackTrace();
			}
		}
	} // TestingRunnable
}
