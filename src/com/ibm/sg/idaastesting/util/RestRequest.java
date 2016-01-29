package com.ibm.sg.idaastesting.util;

import java.util.Map;

public class RestRequest {
	public static final String HEADER_KEY_CONTENT_TYPE = "Content-type";
	public static final String HEADER_KEY_ACCEPT = "Accept";
	public static final String HEADER_KEY_ACCEPT_LANGUAGE = "Accept-Language";
	public static final String HEADER_VAL_APPLICATION_JSON = "application/json";
	public static final String HEADER_VAL_APPLICATION_XML = "application/xml";
	public static final String HEADER_VAL_LANGUAGE_EN = "en";
	public static final String METHOD_GET = "get";
	public static final String METHOD_POST = "post";
	public static final String METHOD_PUT = "put";
	public static final String METHOD_DELETE = "delete";	

	private String endpointUrl;
	private String username;
	private String password;
	private Map<String, String> headerMap;
	private String body;
	private String method;

	public RestRequest() {
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	
}
