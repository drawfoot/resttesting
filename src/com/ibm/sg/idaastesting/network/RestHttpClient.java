package com.ibm.sg.idaastesting.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.BasicScheme;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;

import com.ibm.json.java.OrderedJSONObject;

public class RestHttpClient {
	private HttpClient client = getHttpClient();
	static {
		ProtocolSocketFactory protcolSocketFactory = new ClientProtocolSocketFactory();
		Protocol protocol = new Protocol("https", protcolSocketFactory, 443);
		Protocol.registerProtocol("https", protocol);
	}

	public RestResponse doRequest(RestRequest restRequest) 
	{
		HttpMethodBase httpMethod = null;
		String url;
		try {
			url = restRequest.getEndpointUrl();
			int index = url.indexOf("=");
			if(index!=-1) {
				String part1 = url.substring(0, index+1);
				String part2 = URLEncoder.encode(url.substring(index+1, url.length()), "UTF-8");
				url = part1 + part2;	
			}
			switch (restRequest.getMethod()) {
			case RestRequest.METHOD_GET:
				httpMethod = new GetMethod(url);
				break;
			case RestRequest.METHOD_POST:
				httpMethod = new PostMethod(url);
				if (restRequest.getBody() != null) {
					try {
						((PostMethod) httpMethod)
								.setRequestEntity(new StringRequestEntity(
										restRequest.getBody(),
										RestRequest.HEADER_VAL_APPLICATION_JSON,
										"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case RestRequest.METHOD_PUT:
				httpMethod = new PutMethod(url);
				if (restRequest.getBody() != null) {
					try {
						((PutMethod) httpMethod)
								.setRequestEntity(new StringRequestEntity(
										restRequest.getBody(),
										RestRequest.HEADER_VAL_APPLICATION_JSON,
										"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
				break;
			case RestRequest.METHOD_DELETE:
				httpMethod = new DeleteMethod(url);
				break;
			default:
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return doRequest(restRequest, httpMethod);
	}

	private RestResponse doRequest(RestRequest restRequest,
			HttpMethodBase httpMethod) {

		// add credentials
		String username = restRequest.getUsername();
		if (username != null) {
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
					username, restRequest.getPassword());
			Header header = new Header("Authorization",
					BasicScheme.authenticate(credentials, "UTF-8"));
			httpMethod.addRequestHeader(header);
		}
		
		// add headers
		Map<String, String> headerMap = restRequest.getHeaderMap();
		if (headerMap != null) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				httpMethod.addRequestHeader(new Header(entry.getKey(), entry
						.getValue()));
			}
		}
		
		// execute
		try {
			client.executeMethod(httpMethod);
			return getRestResponse(httpMethod);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			httpMethod.releaseConnection();
		}
	}	

	private HttpClient getHttpClient() {
		HttpConnectionManager connectionManager = getConnectionManager();
		HttpClient client = new HttpClient(connectionManager);
		HttpClientParams params = client.getParams();
		params.setAuthenticationPreemptive(true);
		params.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		return client;
	}

	private HttpConnectionManager getConnectionManager() {
		return new SimpleHttpConnectionManager();
	}

	private RestResponse getRestResponse(HttpMethodBase httpMethod)
			throws IOException {

		RestResponse restResponse = new RestResponse();

		Header[] responseHeaders = httpMethod.getResponseHeaders();
		LinkedHashMap<String, String> headerMap = new LinkedHashMap<String, String>();

		if (responseHeaders != null) {
			for (Header header : responseHeaders) {
				headerMap.put(header.getName(), header.getValue());
			}
		}

		restResponse.setStatusCode(httpMethod.getStatusCode());
		restResponse.setStatusText(httpMethod.getStatusText());
		restResponse.setHeaderMap(headerMap);
		if (httpMethod.getResponseBodyAsStream() != null) {
			String expmsg = IOUtils.toString(
					httpMethod.getResponseBodyAsStream(), "UTF-8");
			restResponse.setBody(expmsg);
		}
		return restResponse;
	}
	
	@SuppressWarnings("unused")
	public String getResponseId(RestResponse restResponse)
			throws IOException {
		String body = restResponse.getBody();
		OrderedJSONObject jsonObject = (OrderedJSONObject) OrderedJSONObject
				.parse(body);
		return (String) jsonObject.get("id");
	}	

	@SuppressWarnings("unused")
	private String getResponseMessage(RestResponse restResponse)
			throws IOException {
		String body = restResponse.getBody();
		OrderedJSONObject jsonObject = (OrderedJSONObject) OrderedJSONObject
				.parse(body);
		return (String) jsonObject.get("message");
	}
}
