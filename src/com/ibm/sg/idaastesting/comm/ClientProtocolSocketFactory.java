package com.ibm.sg.idaastesting.comm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * 
 */
public class ClientProtocolSocketFactory implements ProtocolSocketFactory {

	private SSLContext _sslcontext;

	private TrustManager[] _trustManager = new TrustManager[] { new X509TrustManager() {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	} };

	public ClientProtocolSocketFactory() {
		super();
	}

	private SSLContext getSSLContext() {
		SSLContext result = null;
		if (this._sslcontext == null) {
			this._sslcontext = createSSLContext();
		}
		result = _sslcontext;
		return result;
	}

	private SSLContext createSSLContext() {
		SSLContext context;
		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, _trustManager, null);
		} catch (KeyManagementException e) {
			throw new HttpClientError(e.toString());

		} catch (NoSuchAlgorithmException e) {
			throw new HttpClientError(e.toString());
		}
		SSLContext result = context;

		return result;
	}

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException,
			UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
				arg2, arg3);
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2,
			int arg3, HttpConnectionParams arg4) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
				arg2, arg3);
	}
}
