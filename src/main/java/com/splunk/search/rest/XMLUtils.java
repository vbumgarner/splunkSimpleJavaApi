package com.splunk.search.rest;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLUtils {
	protected static XMLInputFactory xmlInputFactory = XMLInputFactory
	.newInstance();

	protected static String retrieveSingleFieldValue(InputStream is,
			String fieldName) throws XMLStreamException {
		return retrieveSingleFieldValue(buildXmlReader(is), fieldName);
	}

	protected static String retrieveSingleFieldValue(XMLStreamReader r,
			String fieldName) throws XMLStreamException {
		while (r.hasNext()) {
			if (r.getName().equals(fieldName)) {
				return r.getText();
			}
			r.next();
		}

		return null;
	}

	protected static XMLStreamReader buildXmlReader(InputStream is)
			throws XMLStreamException {
		return xmlInputFactory.createXMLStreamReader(is);
	}

}
