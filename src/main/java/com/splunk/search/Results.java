package com.splunk.search;

import java.util.Iterator;
import java.util.Map;

public class Results {

	private final String[] columns;
	private final Iterator<Map<String, String[]>> rows;
	private final Status status;

	public Results(String[] columns, Iterator<Map<String, String[]>> rows, Status status) {
		this.columns = columns;
		this.rows = rows;
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public String[] getColumns() {
		return columns;
	}

	public Iterator<Map<String, String[]>> getRows() {
		return rows;
	}

}
