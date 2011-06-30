package com.splunk.search;

import java.util.List;
import java.util.Map;

public class Results {

	private final String[] columns;
	private final List<Map<String, String[]>> rows;

	public Results(String[] columns, List<Map<String, String[]>> rows) {
		this.columns = columns;
		this.rows = rows;
	}

	public String[] getColumns() {
		return columns;
	}

	public List<Map<String, String[]>> getRows() {
		return rows;
	}

	public int rowCount() {
		return rows.size();
	}

}
