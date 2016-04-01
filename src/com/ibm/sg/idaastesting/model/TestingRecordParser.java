package com.ibm.sg.idaastesting.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestingRecordParser {

	private static String DEFAULT_URL = "http://localhost:9080/v1/mgmt/idaas";
	private static String DEFAULT_TENANT = "1.wga1.ibmcloudsecurity.com";
	private static final Set<String> ENDPOINTS = new HashSet<String>(
			Arrays.asList("sanctionedapps", "sanctionedappaces", "adminops",
					"connectortemplates", "attributesources", "aliasservice",
					"sslcertificates/signercert",
					"sslcertificates/personalcert", "adminops/apiprotection", "applications", "connectors"));

	public static boolean parseRecord(String str, TestingRecord record) {
		
		if (str.equals("") || str.startsWith("#"))
			return false;

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
			if (parts[4].indexOf("/v1/mgmt/idaas") != -1) {
				String hostmode = prop
						.getProperty(IdaasTestingConfig.CFG_HOSTMODE);
				if (hostmode == null || hostmode.equals("local")) {
					// local mode
					url = parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",
							DEFAULT_URL).replaceAll("#", "|");
				} else if (prop.getProperty(IdaasTestingConfig.CFG_HOST) != null) {
					// remote mode
					url = parts[4].replaceAll(
							"\\/v1\\/mgmt\\/idaas",
							String.format("https://%s:443/v1/mgmt/idaas", prop
									.getProperty(IdaasTestingConfig.CFG_HOST)))
							.replaceAll("#", "|");
				} else {
					// mode not specified
					url = parts[4].replaceAll("\\/v1\\/mgmt\\/idaas",
							DEFAULT_URL).replaceAll("#", "|");
				}
			} else {
				url = (String.format("https://%s:443" + parts[4], prop
						.getProperty(IdaasTestingConfig.CFG_MGA_HOST)))
				.replaceAll("#", "|");
			}

			if (prop.getProperty(IdaasTestingConfig.CFG_APPID) != null)
				url = url.replaceAll("\\[appid\\]",
						prop.getProperty(IdaasTestingConfig.CFG_APPID));
			record.setUrl(url);

			// end point
			for(String ep: ENDPOINTS) {
				if(parts[4].matches(".*" + ep + ".*")) {
					record.setEndpoint(ep);
					break;
				}
			}

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
		
		return true;
	}

	public static HashMap<String, String> getHeader(String headerstr) {
		HashMap<String, String> headers = new HashMap<String, String>();
		String[] entries = trimFirstAndLast(headerstr, "{", "}").split(",");
		String prevKey = "notvalid";
		for (String entry : entries) {
			String[] parts = entry.split(":");
			String candidateKey = trimFirstAndLast(parts[0].trim(), "\"", "\"");
			// first part is a value for previous key
			if (parts.length == 1) {
				if (headers.containsKey(prevKey))
					headers.put(prevKey, headers.get(prevKey) + ","
							+ candidateKey);
				// first part is a real key
			} else {
				prevKey = candidateKey;
				String value = trimFirstAndLast(parts[1].trim(), "\"", "\"");
				headers.put(candidateKey, value);
			}
		}
		return headers;
	}
	
	public static String trimFirstAndLast(String str, String lch, String rch) {
		if (str == null)
			return null;

		if (str.startsWith(lch))
			str = str.substring(1, str.length());
		if (str.endsWith(rch))
			return str.substring(0, str.length() - 1);

		return str;
	}

	public static boolean discoverRunningStatus(TestingRecord record) {
		boolean success = false;
		String msg = "";
		// status code
		if (record.getExpectedStatus().equals(record.getActualStatus())){
			success =true;
		} else {
			msg = "expect status code not match";
		}
		// message
		if (success) {
			String raw = trimFirstAndLast(record.getExpectedMsg(), "{", "}");
			try {
				Pattern r = Pattern.compile(raw);
				String actMsg = record.getActualMsg();
				if (actMsg == null)
					actMsg = "";
				Matcher m = r.matcher(actMsg);
				if (m.find()) {
					success = true;
				} else {
					success = false;
					msg = "expect msg not match";
				}
			} catch (Exception e) {
				record.setStatus(TestingRecord.STATUS_RUN_ERROR
						+ ": RE error, " + e.getMessage());
				return false;
			}
		}

		if(success)
			record.setStatus(TestingRecord.STATUS_RUN_SUCCESS);
		else 
			record.setStatus(TestingRecord.STATUS_RUN_FAILED + ":" + msg);
		return success;
	}
}
