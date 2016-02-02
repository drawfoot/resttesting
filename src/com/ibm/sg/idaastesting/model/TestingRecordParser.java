package com.ibm.sg.idaastesting.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;


public class TestingRecordParser {

	private String DEFAULT_URL = "http://localhost:9080/com.ibm.security.access.idaas.rest.services";
	private String DEFAULT_TENANT = "1.wga1.ibmcloudsecurity.com";
	
	public void parseRecord(String str, TestingRecord record) {
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

			// url
			String url;
			String hostmode = prop.getProperty(IdaasTestingConfig.CFG_HOSTMODE);
			if (hostmode == null || hostmode.equals("local")) {
				url = parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",
						DEFAULT_URL).replaceAll("#", "|");
			} else if (prop.getProperty(IdaasTestingConfig.CFG_HOST) != null) {
				url = parts[4].replaceAll(
						"\\/v1\\/mgmt\\/idaas",
						String.format("https://%s:443/v1/mgmt/idaas",
								prop.getProperty(IdaasTestingConfig.CFG_HOST)))
						.replaceAll("#", "|");
			} else {
				url = parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",
						DEFAULT_URL).replaceAll("#", "|");
			}
			if (prop.getProperty(IdaasTestingConfig.CFG_APPID) != null)
				url = url.replaceAll("\\[appid\\]",
						prop.getProperty(IdaasTestingConfig.CFG_APPID));
			record.setUrl(url);

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

	public HashMap<String, String> getHeader(String headerstr) {
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
	
	public String trimFirstAndLast(String str, String lch, String rch) {
		if (str == null)
			return null;

		if (str.startsWith(lch))
			str = str.substring(1, str.length());
		if (str.endsWith(rch))
			return str.substring(0, str.length() - 1);

		return str;
	}
	
	public void discoverRunningStatus(TestingRecord record) {
		boolean success = false;
		// status code
		if (record.getExpectedStatus().equals(record.getActualStatus())){
			success =true;
		}
		// message
		if (success) {
			String raw = trimFirstAndLast(record.getExpectedMsg(), "{", "}").replace("\\", "\\\\");
			Pattern r = Pattern.compile(raw);
			Matcher m = r.matcher(record.getActualMsg());
			if (m.find()) {
				success = true;
			}
		}
		
		if(success) 
			record.setStatus(TestingRecord.STATUS_RUN_SUCCESS);
		else 
			record.setStatus(TestingRecord.STATUS_RUN_FAILED);
	}
}
