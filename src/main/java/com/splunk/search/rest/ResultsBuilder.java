package com.splunk.search.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.splunk.search.Results;

public class ResultsBuilder {
	

	public static Results build(InputStream results) throws XMLStreamException {
		XMLStreamReader reader = XMLUtils.buildXmlReader(results);
		String[] columns = buildColumns(reader);
		List<Map<String, String[]>> rows = buildRows(reader);
		
		return new Results(columns,rows);
	}

	private static String[] buildColumns(XMLStreamReader reader) throws XMLStreamException {
		List<String> columns = new ArrayList<String>();
		while( reader.hasNext()) {
			reader.next();
			String name = reader.getName().toString();
			
			if( reader.isStartElement() && name.equals("field") ) {
				columns.add(reader.getText());
			}
			if( reader.isEndElement() && name.equals("meta") ) {
				break;
			}
		}
		return columns.toArray(new String[columns.size()]);
	}

	private static List<Map<String, String[]>> buildRows(XMLStreamReader reader) throws XMLStreamException {
		List<Map<String, String[]>> rows = new ArrayList<Map<String,String[]>>();
		while( reader.hasNext()) {
			reader.next();
			String name = reader.getName().toString();
			
			String currentFieldname = "unknown";
			List<String> currentValues = new ArrayList<String>();
			Map<String,String[]> row = new HashMap<String, String[]>();

			if( reader.isStartElement() && name.equals("result") ) {
				row = new HashMap<String,String[]>();

			} else if( reader.isStartElement() && name.equals("field") ) {
				currentFieldname = reader.getAttributeValue(reader.getNamespaceURI(),"k");

			} else if( reader.isStartElement() && ( name.equals("text") || name.equals("v") ) ) {
				currentValues.add( reader.getText() );

			} else if( reader.isEndElement() && name.equals("field") ) {
				row.put(currentFieldname, currentValues.toArray(new String[currentValues.size()]));

			} else if( reader.isEndElement() && name.equals("result") ) {
				rows.add(row);

			}
		}
		
//	<result offset='0'>
//		<field k='_indextime'>
//			<value><text>1309404164</text></value>
//		</field>
//		<field k='_raw'><v xml:space='preserve' trunc='0'>1309404164.672      0 192.168.1.76 TCP_DENIED/403 3598 GET http://evsecure-crl.verisign.com/pca3-g5.crl - NONE/- text/html</v></field>

		
		return rows;
	}

}

/*
<?xml version='1.0' encoding='UTF-8'?>
<results preview='0'>
<meta>
<fieldOrder>
<field>_cd</field>
<field>_indextime</field>
<field>_raw</field>
<field>_serial</field>
<field>_si</field>
<field>_sourcetype</field>
<field>_subsecond</field>
<field>_time</field>
<field>host</field>
<field>index</field>
<field>linecount</field>
<field>source</field>
<field>sourcetype</field>
<field>splunk_server</field>
</fieldOrder>
</meta>
	<result offset='0'>
		<field k='_cd'>
			<value><text>11:233</text></value>
		</field>
		<field k='_indextime'>
			<value><text>1309404164</text></value>
		</field>
		<field k='_raw'><v xml:space='preserve' trunc='0'>1309404164.672      0 192.168.1.76 TCP_DENIED/403 3598 GET http://evsecure-crl.verisign.com/pca3-g5.crl - NONE/- text/html</v></field>
		<field k='_serial'>
			<value><text>0</text></value>
		</field>
		<field k='_si'>
			<value><text>minime</text></value>
			<value><text>main</text></value>
		</field>
		<field k='_sourcetype'>
			<value><text>squid</text></value>
		</field>
		<field k='_subsecond'>
			<value><text>.672</text></value>
		</field>
		<field k='_time'>
			<value><text>2011-06-29T22:22:44.672-05:00</text></value>
		</field>
		<field k='host'>
			<value><text>minime</text></value>
		</field>
		<field k='index'>
			<value><text>main</text></value>
		</field>
		<field k='linecount'>
			<value><text>1</text></value>
		</field>
		<field k='source'>
			<value><text>/Users/vbumgarn/Library/Logs/squid/squid-access.log</text></value>
		</field>
		<field k='sourcetype'>
			<value><text>squid</text></value>
		</field>
		<field k='splunk_server'>
			<value><text>minime</text></value>
		</field>
	</result>
*/
