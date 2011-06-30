package com.splunk.search.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

public abstract class RestBase {
	private static Map<URL, HttpClient> clientMap;

	public static HttpClient getClient(URL server) {
		if (clientMap == null) {
			clientMap = new HashMap<URL, HttpClient>();
		}

		if (!clientMap.containsKey(server)) {
			HttpClient newClient = new DefaultHttpClient();
			clientMap.put(server, newClient);
		}

		return clientMap.get(server);
	}

	protected static InputStream doGet(URL server, String path,
			Map<String, String> headers) throws URISyntaxException, ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(buildURI(server, path));
		addHeaders(headers, httpget);

		HttpResponse response = execute(getClient(server), httpget);
		return getInputStream(response);
	}

	protected static InputStream doPost(URL server, String path,
			Map<String, String> headers, Map<String, String> postArgs)
			throws ClientProtocolException, IOException, URISyntaxException {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (String k : postArgs.keySet()) {
			formparams.add(new BasicNameValuePair(k, postArgs.get(k)));
		}

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				"UTF-8");
		HttpPost httppost = new HttpPost(buildURI(server, path));
		httppost.setEntity(entity);

		addHeaders(headers, httppost);

		HttpResponse response = execute(getClient(server), httppost);
		return getInputStream(response);
	}

	private static void addHeaders(Map<String, String> headers,
			HttpRequestBase method) {
		List<Header> headerList = new ArrayList<Header>();
		for (String k : headers.keySet()) {
			headerList.add(new BasicHeader(k, headers.get(k)));
		}
		method.setHeaders(headerList.toArray(new Header[headerList.size()]));
	}

	private static URI buildURI(URL server, String path)
			throws URISyntaxException {
		return new URI(server.toExternalForm() + path);
	}

	private static HttpResponse execute(HttpClient httpclient,
			HttpRequestBase method) throws ClientProtocolException, IOException {
		HttpResponse response = httpclient.execute(method);
		return response;
	}

	private static InputStream getInputStream(HttpResponse response)
			throws IllegalStateException, IOException {
		HttpEntity entity = response.getEntity();
		return entity.getContent();
	}

	protected Map<String, String> buildAuthHeaders(String authKey) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Splunk " + authKey);
		return headers;
	}
	
//	protected void foo() {
//		TrustManager easyTrustManager = new X509TrustManager() {
//
//		    @Override
//		    public void checkClientTrusted(
//		            X509Certificate[] chain,
//		            String authType) throws CertificateException {
//		        // Oh, I am easy!
//		    }
//
//		    @Override
//		    public void checkServerTrusted(
//		            X509Certificate[] chain,
//		            String authType) throws CertificateException {
//		        // Oh, I am easy!
//		    }
//
//		    @Override
//		    public X509Certificate[] getAcceptedIssuers() {
//		        return null;
//		    }
//		    
//		};
//
//		SSLContext sslcontext = SSLContext.getInstance("TLS");
//		sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);
//
//		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
//		SSLSocket socket = (SSLSocket) sf.createSocket();
//		socket.setEnabledCipherSuites(new String[] { "SSL_RSA_WITH_RC4_128_MD5" });
//
//		HttpParams params = new BasicHttpParams();
//		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000L);
//		InetSocketAddress address = new InetSocketAddress("locahost", 443);
//		sf.connectSocket(socket, address, null, params);
//
//	}

}