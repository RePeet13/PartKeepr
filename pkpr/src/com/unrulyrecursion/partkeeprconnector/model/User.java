package com.unrulyrecursion.partkeeprconnector.model;

import java.util.HashMap;

public class User {

	private String username, passHash;
	private int id;
	private HashMap<String, String> prefs;
	
	public User (String username, String passHash, int id) {
		this.setUsername(username);
		this.setPassHash(passHash);
		this.setId(id);
		prefs = new HashMap<String, String>();
	}
	
	public User () { prefs = new HashMap<String, String>(); }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap<String, String> getPrefs() {
		return prefs;
	}

	public void setPrefs(HashMap<String, String> prefs) {
		this.prefs = prefs;
	}
	
	public void addPref(String name, String value) {
		prefs.put(name, value);
	}

	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
}
