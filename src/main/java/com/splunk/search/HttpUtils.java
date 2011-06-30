package com.splunk.search;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;



public class HttpUtils {
	private static SSLSocketFactory sslSocketFactory;

	/**
	 * Returns a SSL Factory instance that accepts all server certificates.
	 * 
	 * <pre>
	 * SSLSocket sock = (SSLSocket) getSocketFactory.createSocket(host, 443);
	 * </pre>
	 * 
	 * @return An SSL-specific socket factory.
	 **/
	public static final SSLSocketFactory getSocketFactory() {
		if (sslSocketFactory == null) {
			try {
				TrustManager[] tm = new TrustManager[] { new NaiveTrustManager() };
				SSLContext context = SSLContext.getInstance("SSL");
				context.init(new KeyManager[0], tm, new SecureRandom());

				sslSocketFactory = (SSLSocketFactory) context
						.getSocketFactory();

			} catch (KeyManagementException e) {
				System.out.println("No SSL algorithm support: "
						+ e.getMessage());
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				System.out
						.println("Exception when setting up the Naive key management.");
				e.printStackTrace();
			}
		}
		return sslSocketFactory;
	}
}
