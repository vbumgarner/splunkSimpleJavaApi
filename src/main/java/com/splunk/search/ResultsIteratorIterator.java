package com.splunk.search;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

class ResultsIteratorIterator implements Iterator<Map<String, String[]>> {

	//creates an iterator that can page through search results
	
	private final Search search;
	private int page = 0;
	private Iterator<Map<String, String[]>> currentIter;
	private boolean done = false;
	private AuthKey authKey;
	private JobId jobId;
	private int pageSize;

	public ResultsIteratorIterator(Search search, JobId jobId, AuthKey authKey,
			int pageSize, Results firstResults) {
		super();
		this.search = search;
		this.currentIter = firstResults.getRows();
		this.authKey = authKey;
		this.jobId = jobId;
		this.pageSize = pageSize;
	}

	@Override
	public void remove() {
		// ignore
	}

	@Override
	public Map<String, String[]> next() {
		if (hasNext()) {
			return currentIter.next();
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		if (done) {
			return false;
		}

		if (currentIter.hasNext()) {
			return true;
		} else {
			page++;
			try {
				currentIter = this.search.retrieveRows(jobId, authKey,
						pageSize, page * pageSize).getRows();
				if (!currentIter.hasNext()) {
					done = true;
				}
				return currentIter.hasNext();
			} catch (IOException e) {
				Search.logger.error(e);
				done = true;
				return false;
			}
		}
	}
}