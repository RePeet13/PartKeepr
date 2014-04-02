package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.text.format.DateFormat;

public class SavedServers implements Serializable {
	
	private ArrayList<HashMap<String,String>> saved;
	// TODO define date format to use as standard and put up here
	
	// TODO in login activity have in shared prefs a "hasSavedServers" to check, then get this (or has pref(name)?
	public SavedServers () {saved = new ArrayList<HashMap<String, String>>(); }
	
	public void addServer(String url, String user, String sessionId) {
		
		// TODO input checks
		
		HashMap<String, String> tmp = new HashMap<String,String>();
		Date d = new Date();
		
		tmp.put("url", url);
		tmp.put("user", user);
		tmp.put("sessionId", sessionId);
		tmp.put("createDate", d.toString());
		tmp.put("access", d.toString());
		tmp.put("good", "true");
		
		saved.add(tmp);
	}
	// TODO decide how much of url to keep in this
	// TODO sanitize url inputs so they are the same
	
	public void removeServer(int pos) {
		// TODO sanity checks
		saved.remove(pos);
	}
	
	public void accessServer(int pos) {
		Date d = new Date();
		saved.get(pos).put("access", d.toString());
		saved.get(pos).put("good", "true");
	}
	
	public void accessServer(String url, String user) {
		for (HashMap<String, String> hm : saved) {
			if (hm.get("url").compareToIgnoreCase(url) == 0 && hm.get("user").compareToIgnoreCase(user) == 0) {
				Date d = new Date();
				hm.put("access", d.toString());
				hm.put("good", "true");
				return;
			}
		}
	}
	
	
	public ArrayList<HashMap<String, String>> getSaved() {
		return saved;
	}
	
}
