package com.splunk.search.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import com.splunk.search.Results;
import com.splunk.search.Status;


public class Search extends RestBase {
	public static final String JOBS_PATH = "/services/search/jobs";

	private final URL server;

	public Search(URL server) {
		super();
		this.server = server;
	}

	public String buildAuthKey(String user, String password) throws IOException {
		return Auth.buildAuthKey(server,user,password);
	}

	public String startSearch(String search, String authKey) throws ClientProtocolException, IOException, URISyntaxException, XMLStreamException {
		if ( ! search.startsWith("search") ) {
		    search = "search " + search;
		}

		//curl -u admin:changeme -k https://localhost:8089/services/search/jobs -d"search=search *"
		Map<String,String> postArgs = new HashMap<String,String>();
		postArgs.put("search",search);

		InputStream results = doPost(server, JOBS_PATH, buildAuthHeaders(authKey), postArgs);
		return XMLUtils.retrieveSingleFieldValue(results,"sid");
	}

	public Status getStatus(String jobId, String authKey) throws ClientProtocolException, URISyntaxException, IOException, SAXException, ParserConfigurationException {
		InputStream results = doGet(server,JOBS_PATH + "/" + jobId, buildAuthHeaders(authKey));
		return StatusBuilder.build(results);
		// /services/search/jobs/1258421375.19
	}

	public Results retrieveRows(String jobId, String authKey, int count, int offset) throws ClientProtocolException, IOException, URISyntaxException, XMLStreamException {
		Map<String,String> postArgs = new HashMap<String,String>();
//		postArgs.put("output_mode","csv");
//		curl -u admin:changeme -k https://localhost:8089/services/search/jobs/1309405142.30/results?count=100\&offset=100
		InputStream results = doPost(server,JOBS_PATH + "/" + jobId + "/results?count=" + Integer.toString( count ) + "&offset=" + Integer.toString( offset ), buildAuthHeaders(authKey), postArgs);
		return ResultsBuilder.build(results);
		//You can get your search results in JSON, CSV or XML by setting output_mode=csv. The simplest method is to retrieve searches in CSV format, like this:
		//curl -u admin:changeme -k https://localhost:8089/services/search/jobs/1258421375.19/results/ -d"output_mode=csv"
	}
	
}
