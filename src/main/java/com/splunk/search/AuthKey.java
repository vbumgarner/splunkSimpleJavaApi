package com.splunk.search;

public class AuthKey {
	private final String authKey;

	public AuthKey(String authKey) {
		super();
		this.authKey = authKey;
	}
	
	public String getAuthKey() {
		return authKey;
	}

	@Override
	public String toString() {
		return authKey;
	}
	
	@Override
	public boolean equals(Object obj) {
		return authKey.equals(obj.toString());
	}
	
	@Override
	public int hashCode() {
		return authKey.hashCode();
	}
}
