package com.splunk.search.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Auth extends RestBase {
	public static final String LOGIN_PATH = "/services/auth/login";

	public static String buildAuthKey(URL server, String user, String password) throws IOException {
		Map<String,String> postArgs = new HashMap<String,String>();
		postArgs.put("username",user);
		postArgs.put("password",password);

		try {
			InputStream is = doPost( server , LOGIN_PATH , new HashMap<String,String>() , postArgs );
			XMLStreamReader r = XMLUtils.buildXmlReader(is);
			return XMLUtils.retrieveSingleFieldValue(r,"sessionKey");
		} catch (URISyntaxException e) {
			IOException ioe = new IOException(e);
			throw ioe;
		} catch (XMLStreamException e) {
			IOException ioe = new IOException(e);
			throw ioe;
		}

		//		serverContent = httplib2.Http().request(baseurl + '/services/auth/login',
//			    'POST', headers={}, body=urllib.urlencode({'username':userName, 'password':password}))[1]
//
//			sessionKey = minidom.parseString(serverContent).getElementsByTagName('sessionKey')[0].childNodes[0].nodeValue
		
//		<response>
//		<sessionKey>30774f9d502004b5c655c08b5362bdca</sessionKey>

	}

}
