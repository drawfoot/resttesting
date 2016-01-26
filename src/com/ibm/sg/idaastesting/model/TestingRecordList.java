package com.ibm.sg.idaastesting.model;

import java.util.LinkedList;
import java.util.List;


public class TestingRecordList {
	private List<TestingRecord> models;
	public TestingRecordList() {
		models = new LinkedList<TestingRecord>();
		models.add(new TestingRecord("2.2.12.1.1", "DELETE"));
		models.add(new TestingRecord("2.2.12.1.2", "GET"));		
	}
	public List<TestingRecord> getModel() {
		return models;
	}
	
	public void addTestingRecord(TestingRecord record) {
		models.add(record);
	}
	
	public void addTestingRecord(List<TestingRecord> elements) {
		models.addAll(elements);
	}	
}

