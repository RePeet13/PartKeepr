package com.unrulyrecursion.partkeeprconnector.model;

import java.util.Date;

public class Server {
	public String url, user, sessionId, createDate, access, good;
	
	public Server() {
		
	}
	
	public Server(String url, String user, String sessionId, String access, String good) {
		Date d = new Date();
		this.createDate = d.toString();
		this.url = url;
		this.user = user;
		this.sessionId = sessionId;
		this.access = access;
		this.good = good;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

}
