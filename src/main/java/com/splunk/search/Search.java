package com.splunk.search;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.splunk.search.rest.Auth;
import com.splunk.search.rest.RestBase;
import com.splunk.search.rest.SearchRunner;

public class Search extends RestBase {
	static Logger logger = Logger.getLogger(RestBase.class);

	public static final String JOBS_PATH = "/services/search/jobs";

	private static final int DEFAULT_PAGE_SIZE = 500;

	private final URL server;
	private final SearchRunner runner;
	private final int pageSize;

	public Search(URL server) {
		this(server, DEFAULT_PAGE_SIZE);
	}

	public Search(URL server, int pageSize) {
		super();
		this.server = server;
		this.runner = new SearchRunner(server);
		this.pageSize = pageSize;
	}
	
	public static Results run(URL server, String user, String password, String search) throws IOException {
		Search s = new Search(server);
		AuthKey authKey = s.buildAuthKey(user, password);
		JobId jobId = s.startSearch(search, authKey);
		s.waitForJobCompletion(jobId, authKey);
		return s.retrieveAllRows(jobId, authKey);
	}

	public AuthKey buildAuthKey(String user, String password)
			throws IOException {
		return Auth.buildAuthKey(server, user, password);
	}

	public JobId startSearch(String search, AuthKey authKey) throws IOException {
		return runner.startSearch(search, authKey);
	}

	public void waitForJobCompletion(JobId jobId, AuthKey authKey) throws IOException {
		Status status = getStatus(jobId, authKey);
		while (status.isDone() == false) {
			logger.info("Waiting for jobId " + jobId + " to finish. "
					+ Math.round(status.getDoneProgress().floatValue() * 100)
					+ " percent complete. " + status.getResultCount()
					+ " results retrieved so far.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e);
			}
			status = getStatus(jobId, authKey);
		}
	}

	public Status getStatus(JobId jobId, AuthKey authKey) throws IOException {
		return runner.getStatus(jobId, authKey, true);
	}

	public Results retrieveRows(JobId jobId, AuthKey authKey, int count,
			int offset) throws IOException {
		return runner.retrieveRows(jobId, authKey, count, offset);
	}

	public Results retrievePage(JobId jobId, AuthKey authKey, int page)
			throws IOException {
		return runner.retrieveRows(jobId, authKey, pageSize, page * pageSize);
	}

	public Results retrieveAllRows(JobId jobId, AuthKey authKey) throws IOException {
		Results firstResults = retrieveRows(jobId, authKey, pageSize, 0);
		Status status = firstResults.getStatus();

		Iterator<Map<String, String[]>> myiter = new ResultsIteratorIterator(this, jobId,authKey,pageSize,firstResults);

		Results fullResults = new Results(firstResults.getColumns(),myiter,status);
		return fullResults;
	}
}
