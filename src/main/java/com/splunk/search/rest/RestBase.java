package com.splunk.search.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
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
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.splunk.search.AuthKey;

public abstract class RestBase {

	public static HttpClient getClient(URL server)
			throws KeyManagementException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException {
		HttpClient newClient = new DefaultHttpClient();

		SchemeSocketFactory socketFactory = new SSLSocketFactory(
				SelfSignedUtils.NAIVE_TRUST_STRATEGY,
				SelfSignedUtils.NAIVE_HOSTNAME_VERIFIER);
		Scheme sch = new Scheme("https", server.getPort(), socketFactory);
		newClient.getConnectionManager().getSchemeRegistry().register(sch);

		return newClient;
	}

	protected static InputStream doGet(URL server, String path,
			Map<String, String> headers) throws URISyntaxException,
			ClientProtocolException, IOException, KeyManagementException,
			UnrecoverableKeyException, NoSuchAlgorithmException,
			KeyStoreException {
		HttpGet httpget = new HttpGet(buildURI(server, path));
		addHeaders(headers, httpget);

		HttpResponse response = execute(getClient(server), httpget);
		if (response.getEntity().getContentLength() == 0) {
			return new ByteArrayInputStream("<empty/>".getBytes());
		}
		return getInputStream(response);
	}

	protected static InputStream doPost(URL server, String path,
			Map<String, String> headers, Map<String, String> postArgs)
			throws ClientProtocolException, IOException, URISyntaxException,
			KeyManagementException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException {

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

	protected Map<String, String> buildAuthHeaders(AuthKey authKey) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Splunk " + authKey);
		return headers;
	}
}
