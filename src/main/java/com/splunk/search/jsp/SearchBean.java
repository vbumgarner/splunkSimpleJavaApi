package com.splunk.search.jsp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.splunk.search.Results;
import com.splunk.search.Search;

public class SearchBean {
	public String server;
	public String user;
	public String password;
	public String search;

	private Results results;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Results getResults() throws MalformedURLException, IOException {
		if (results == null) {
			results = Search.run(new URL(server), user, password, search);
		}
		return results;
	}
}
