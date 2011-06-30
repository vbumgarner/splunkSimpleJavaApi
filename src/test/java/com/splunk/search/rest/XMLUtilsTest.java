package com.splunk.search.rest;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLUtilsTest {


	@Test
	public void testRunXpath() throws Exception {
		Document d = XMLUtils.buildDocumet( XMLUtilsTest.class.getResourceAsStream("status.xml") );
		NodeList response = XMLUtils.runXpath(d, "/entry/link");
		Assert.assertEquals( 8, response.getLength() );
		Assert.assertEquals( 44, XMLUtils.runXpath(d, "/entry/content/dict/key").getLength() );
	}

	
}
