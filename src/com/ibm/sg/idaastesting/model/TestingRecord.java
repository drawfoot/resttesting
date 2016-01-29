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
	private String tenant;

	public TestingRecord(String no, String method) {
		this.no = no;
		this.method = method;
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

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

}