package com.splunk.search.rest;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class SelfSignedUtils {

	public static TrustStrategy NAIVE_TRUST_STRATEGY = new TrustStrategy() {

		@Override
		public boolean isTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// be really easy...
			return true;
		}
	};

	public static X509HostnameVerifier NAIVE_HOSTNAME_VERIFIER = new X509HostnameVerifier() {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			// be really easy...
			return true;
		}

		@Override
		public void verify(String arg0, String[] arg1, String[] arg2)
				throws SSLException {
		}

		@Override
		public void verify(String arg0, X509Certificate arg1)
				throws SSLException {
		}

		@Override
		public void verify(String arg0, SSLSocket arg1) throws IOException {
		}
	};

}
