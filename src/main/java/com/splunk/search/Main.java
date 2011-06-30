package com.splunk.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;

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

		Results res = Search.run(server, user, password, search);

		Integer resultCount = res.getStatus().resultCount();
		System.out.println(resultCount.toString() + " events found.");

		if (resultCount.intValue() == 0) {
			return;
		}

		String[] columns = res.getColumns();
		CSVWriter csv = new CSVWriter(new OutputStreamWriter(System.out));
		csv.writeNext(columns);
		csv.flush();

		for (Iterator<Map<String, String[]>> iter = res.getRows(); iter
				.hasNext();) {
			Map<String, String[]> row = iter.next();
			String[] rowArray = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				rowArray[i] = org.apache.commons.lang.StringUtils.join(
						row.get(columns[i]), "|");
			}
			csv.writeNext(rowArray);
			csv.flush();
		}

		csv.close();
	}
}
