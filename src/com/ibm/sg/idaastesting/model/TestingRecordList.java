package com.ibm.sg.idaastesting.model;

import java.util.LinkedList;
import java.util.List;


public class TestingRecordList {
	private List<TestingRecord> models;
	public TestingRecordList() {
		models = new LinkedList<TestingRecord>();		
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
	
	public void reset() {
		models.clear();
	}
}

