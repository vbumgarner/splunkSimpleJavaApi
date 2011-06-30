package com.splunk.search.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.splunk.search.Status;

public class StatusBuilder {

	static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	public static Status build(InputStream results) throws SAXException, IOException, ParserConfigurationException {		
		dbf.setNamespaceAware(true); // never forget this!
		Document d = dbf.newDocumentBuilder().parse(results);

		return new Status( d );
	}
}
