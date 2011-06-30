package com.splunk.search.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.splunk.search.AuthKey;
import com.splunk.search.JobId;
import com.splunk.search.Results;
import com.splunk.search.Status;

public class SearchRunner extends RestBase {
	private static Logger logger = Logger.getLogger(SearchRunner.class);

	public static final String JOBS_PATH = "/services/search/jobs";

	private URL server;

	private Status status = null;

	public SearchRunner(URL server) {
		this.server = server;
	}

	public JobId startSearch(String search, AuthKey authKey) throws IOException {
		if (!search.startsWith("search")) {
			search = "search " + search;
		}

		// curl -u admin:changeme -k https://localhost:8089/services/search/jobs
		// -d"search=search *"
		Map<String, String> postArgs = new HashMap<String, String>();
		postArgs.put("search", search);

		try {
			InputStream results = doPost(server, JOBS_PATH,
					buildAuthHeaders(authKey), postArgs);
			return new JobId( XMLUtils.getSingleValueOrMsg(results, "//sid/text()") );
		} catch (Exception e) {
			logger.error(e);
			throw new IOException(e);
		}
	}

	public Status getStatus(JobId jobId, AuthKey authKey, boolean needFresh) throws IOException {
		if( status != null && !needFresh ) {
			return status;
		}

		try {
			InputStream results = doGet(server, JOBS_PATH + "/" + jobId,
					buildAuthHeaders(authKey));
			status = new Status(XMLUtils.buildDocumet(results));
			return status;
		} catch (Exception e) {
			logger.error(e);
			throw new IOException(e);
		}
		// /services/search/jobs/1258421375.19
	}

	public Results retrieveRows(JobId jobId, AuthKey authKey, int count,
			int offset) throws IOException {
		try {
			InputStream results = doGet(server, JOBS_PATH + "/" + jobId
					+ "/results?count=" + Integer.toString(count) + "&offset="
					+ Integer.toString(offset), buildAuthHeaders(authKey));
			return ResultsBuilder.build(results,getStatus(jobId,authKey,false));
		} catch (Exception e) {
			logger.error(e);
			throw new IOException(e);
		}
	}

}
