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

		Integer resultCount = srch.getStatus(jobId, authKey).resultCount();
		System.out.println(resultCount.toString() + " events found.");
		
		if( resultCount.intValue() == 0 ) {
			return;
		}

		int PAGE_SIZE = 100;
		int pageNumber = 0;
		Results res = srch.retrieveRows(jobId, authKey, PAGE_SIZE, pageNumber);

		String[] columns = res.getColumns();
		CSVWriter csv = new CSVWriter(new OutputStreamWriter(System.out));
		csv.writeNext(columns);
		csv.flush();

		while (res.rowCount() > 0) {
			for (Map<String, String[]> row : res.getRows()) {
				String[] rowArray = new String[columns.length];
				for (int i = 0; i < columns.length; i++) {
					rowArray[i] = org.apache.commons.lang.StringUtils.join(
							row.get(columns[i]), "|");
				}
				csv.writeNext(rowArray);
				csv.flush();
			}

			pageNumber++;
			res = srch.retrieveRows(jobId, authKey, PAGE_SIZE, pageNumber
					* PAGE_SIZE);
		}
		csv.close();

	}
}
