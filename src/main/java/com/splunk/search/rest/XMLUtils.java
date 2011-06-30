package com.splunk.search.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtils {
	protected static XMLInputFactory xmlInputFactory = XMLInputFactory
			.newInstance();
	static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	private static XPathFactory xpathFactory = XPathFactory.newInstance();
	private static Map<String, XPathExpression> xpathCache = new HashMap<String, XPathExpression>();

	public static Document buildDocumet(InputStream results)
			throws SAXException, IOException, ParserConfigurationException {
//		dbf.setNamespaceAware(true); // hmm... Had trouble getting the Xpaths to work with this enabled.
		return dbf.newDocumentBuilder().parse(results);
	}

	protected static String retrieveSingleFieldValue(InputStream is,
			String fieldName) throws XMLStreamException {
		return retrieveSingleFieldValue(buildXmlReader(is), fieldName);
	}

	protected static String retrieveSingleFieldValue(XMLStreamReader r,
			String fieldName) throws XMLStreamException {
		while (r.hasNext()) {
			if (r.getEventType() == XMLStreamConstants.START_ELEMENT
					&& r.getName().equals(fieldName)) {
				StringBuffer sb = new StringBuffer();
				r.next();
				while(r.getEventType() != XMLStreamConstants.END_ELEMENT) {
					sb.append(r.getText());
					r.next();
				}
				return sb.toString();
			}
			r.next();
		}

		return null;
	}

	protected static XMLStreamReader buildXmlReader(InputStream is)
			throws XMLStreamException {
		return xmlInputFactory.createXMLStreamReader(is);
	}

	public static NodeList runXpath(Document doc, String path)
			throws XPathExpressionException {
		return (NodeList) getXpath(path).evaluate(doc, XPathConstants.NODESET);
	}

	public static String runXpathToString(Document doc, String path) throws DOMException,
			XPathExpressionException {
		NodeList nodes = runXpath(doc, path);
		if( nodes != null && nodes.getLength() > 0 ) {
			return nodes.item(0).getNodeValue();
		}
		return null;
	}

	public static XPathExpression getXpath(String path)
			throws XPathExpressionException {
		if (!xpathCache.containsKey(path)) {
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile(path);
			xpathCache.put(path, expr);
		}
		return xpathCache.get(path);
	}
	
	public static String getSingleValueOrMsg(InputStream is, String path) throws IOException, SAXException, ParserConfigurationException, DOMException, XPathExpressionException {
		Document resultsDoc = XMLUtils.buildDocumet(is);

		String val = XMLUtils.runXpathToString(resultsDoc,
				path);
		if (val == null) {
			String message = XMLUtils.runXpathToString(resultsDoc,
					"//msg/text()");
			if (message != null) {
				throw new IOException(message);
			}
			throw new IOException("Unknown error retrieving value");
		}
		
		return val;
	}

	public static boolean testNodeName(XMLStreamReader reader, String string) {
		if( reader.hasName() ) {
			return reader.getName().toString().equals(string);
		}
		return false;
	}

}
