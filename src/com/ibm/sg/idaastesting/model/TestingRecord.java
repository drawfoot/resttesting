package com.ibm.sg.idaastesting.model;

public class TestingRecord {
	private String no;
	private String method;
	private String expectedStatus;
	private String expectedMsg;
	private String actualStatus;
	private String actualMsg;
	private String url;
	private String head;	
	private String data;

	private String user;
	private String pass;
	private String status;
	private String severity;
	private boolean checked;
	private String endpoint;

	public static final String STATUS_PARSING = "parsing";
	public static final String STATUS_PARSED = "parsed";
	public static final String STATUS_PARSE_ERROR = "parse error";
	public static final String STATUS_RUNNING = "running";
	public static final String STATUS_RUN_NOT_START = "run not started";
	public static final String STATUS_RUN_ERROR = "run error";
	public static final String STATUS_RUN_SUCCESS = "run successful";
	public static final String STATUS_RUN_FAILED = "run failed";	

	public TestingRecord() {
		this.checked = true;
	}
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getExpectedStatus() {
		return expectedStatus;
	}
	public void setExpectedStatus(String expectedStatus) {
		this.expectedStatus = expectedStatus;
	}
	public String getExpectedMsg() {
		return expectedMsg;
	}
	public void setExpectedMsg(String expectedMsg) {
		this.expectedMsg = expectedMsg;
	}
	public String getActualStatus() {
		return actualStatus;
	}
	public void setActualStatus(String actualStatus) {
		this.actualStatus = actualStatus;
	}
	public String getActualMsg() {
		return actualMsg;
	}
	public void setActualMsg(String actualMsg) {
		this.actualMsg = actualMsg;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
}