package com.splunk.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;

import com.splunk.search.rest.Search;

public class Main {

	static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		File propsFile = new File(args[0]);
		String search = args[1];

		Properties props = new Properties();
		props.load(new FileInputStream(propsFile));

		URL server = new URL(props.getProperty("url"));
		String user = props.getProperty("user");
		String password = props.getProperty("password");

		Search srch = new Search(server);
		String authKey = srch.buildAuthKey(user, password);

		String jobId = srch.startSearch(search, authKey);

		Status status = srch.getStatus(jobId, authKey);
		while (status.isDone() == false) {
			logger.info("Waiting for jobId " + jobId + " to finish. "
					+ Math.round(status.doneProgress().floatValue() * 100)
					+ " percent complete. " + status.resultCount()
					+ " results retrieved so far.");
			Thread.sleep(2000);
			status = srch.getStatus(jobId, authKey);
		}

		System.out.println(srch.getStatus(jobId, authKey).resultCount()
				.toString()
				+ " events found.");

		int PAGE_SIZE = 100;
		int pageNumber = 0;
		Results res = srch.retrieveRows(search, authKey, PAGE_SIZE, pageNumber);

		String[] columns = res.getColumns();
		CSVWriter csv = new CSVWriter(new OutputStreamWriter(System.out));
		csv.writeNext(columns);

		while (res.rowCount() > 0) {
			for (Map<String, String[]> row : res.getRows()) {
				String[] rowArray = new String[columns.length];
				for (int i = 0; i < columns.length; i++) {
					rowArray[i] = org.apache.commons.lang.StringUtils.join(
							row.get(columns[i]), "|");
				}
				csv.writeNext(rowArray);
			}

			pageNumber++;
			res = srch.retrieveRows(search, authKey, PAGE_SIZE, pageNumber
					* PAGE_SIZE);
		}

	}
}
