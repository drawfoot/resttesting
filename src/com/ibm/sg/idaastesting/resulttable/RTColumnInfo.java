package com.ibm.sg.idaastesting.resulttable;

public class RTColumnInfo {

	public static final String NUM = "no.";
	public static final String METHOD = "method";
	public static final String STATUS = "status";
	public static final String SEVERITY = "severity";	
	public static final String EXPECTSTATUS = "expectsc";
	public static final String EXPECTMSG = "expectmsg";
	public static final String ACTUALSTATUS = "actualsc";
	public static final String ACTUALMSG = "actualmsg";
	public static final String URL = "url";
	public static final String HEAD = "header";	
	public static final String DATA = "data";	
	public static final String[] COL_PROPS = { NUM, METHOD, STATUS, SEVERITY,
			EXPECTSTATUS, EXPECTMSG, ACTUALSTATUS, ACTUALMSG, URL, HEAD, DATA };
	public static final int COL_COUNT = 11;
	
	public static final int COL_IDX_NO = 0;
	public static final int COL_IDX_METHOD = 1;
	public static final int COL_IDX_STATUS = 2;
	public static final int COL_IDX_SEVERITY = 3;
	public static final int COL_IDX_EXPCODE = 4;
	public static final int COL_IDX_EXPMSG = 5;
	public static final int COL_IDX_ACTCODE = 6;
	public static final int COL_IDX_ACTMSG = 7;
	public static final int COL_IDX_URL = 8;
	public static final int COL_IDX_HEAD = 9;
	public static final int COL_IDX_DATA = 10;	
}
