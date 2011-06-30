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
	private static Logger logger = Logger.getLogger(RestBase.class);

	public static final String JOBS_PATH = "/services/search/jobs";

	private static final int DEFAULT_PAGE_SIZE = 100;

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

	public AuthKey buildAuthKey(String user, String password)
			throws IOException {
		return Auth.buildAuthKey(server, user, password);
	}

	public JobId startSearch(String search, AuthKey authKey) throws IOException {
		return runner.startSearch(search, authKey);
	}

	public Status getStatus(JobId jobId, AuthKey authKey) throws IOException {
		return runner.getStatus(jobId, authKey);
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

		Iterator<Map<String, String[]>> myiter = new ResultsIteratorIterator(jobId,authKey,pageSize,firstResults);
		Results fullResults = new Results(firstResults.getColumns(),myiter);
		return fullResults;
	}

	private class ResultsIteratorIterator implements Iterator<Map<String, String[]>> {
			private int page = 0;
			private Iterator<Map<String, String[]>> currentIter;
			private boolean done = false;
			private AuthKey authKey;
			private JobId jobId;
			private int pageSize;

			public ResultsIteratorIterator(JobId jobId, AuthKey authKey, int pageSize,
					Results firstResults) {
				super();
				this.currentIter = firstResults.getRows();
				this.authKey = authKey;
				this.jobId = jobId;
				this.pageSize = pageSize;
			}

			@Override
			public void remove() {
				//ignore
			}
			
			@Override
			public Map<String, String[]> next() {
				if( hasNext() ) {
					return currentIter.next();
				}
				return null;
			}
			
			@Override
			public boolean hasNext() {
				if( done ) {
					return false;
				}

				if( currentIter.hasNext() ) {
					return true;
				} else {
					page++;
					try {
						currentIter = retrieveRows(jobId,authKey,pageSize,page*pageSize).getRows();
						if( !currentIter.hasNext() ) {
							done = true;
						}
						return currentIter.hasNext();
					} catch (IOException e) {
						logger.error(e);
						done = true;
						return false;
					}
				}
			}
	}
}
