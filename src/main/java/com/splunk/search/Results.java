package com.splunk.search;

import java.util.Iterator;
import java.util.Map;

public class Results {

	private final String[] columns;
	private final Iterator<Map<String, String[]>> rows;

	public Results(String[] columns, Iterator<Map<String, String[]>> rows) {
		this.columns = columns;
		this.rows = rows;
	}

	public String[] getColumns() {
		return columns;
	}

	public Iterator<Map<String, String[]>> getRows() {
		return rows;
	}

}
