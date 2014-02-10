package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class SessionManagement {
	
	public String base_url;
	public String session_id;
	private String loginUrlPart = "Auth/login";
	
	/*
	QueryAPI api;
	 */
	
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "PartKeeprPref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";

	// Email address (make variable public to access from outside)
	public static final String SESS_ID = "sessionId";
	
	//The user permissions
	public static final String USER_PERM = "permissions";

	// Constructor
	public SessionManagement(Context context, String link) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		
		this.base_url=link;
		/*
		QueryAPI api = new QueryAPI(link);
		*/
	}
	
	public void setBaseUrl(String url) {
		this.base_url = url;
	}
	
	
	/**
	 * 
	 * @param username
	 * @param passHash
	 * @return String Sessionid
	 */
	public Boolean login(String username, String passHash) {
		Log.d("Session Management","Building Login POST");
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(base_url+loginUrlPart);
		httpPost.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", passHash));
		
		Log.d("Session Management", "Adding namevalue pairs)");
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpResponse response;
		JSONObject res = null;
		try {
			Log.d("Session Management", "Executing Login POST");
			response = httpClient.execute(httpPost);
			res = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (res == null) {
			return false; // TODO
		}
		String sess = JsonParser.parseLoginResponse(res);
		if (sess != null) {
			// Successful Login
			Log.d("SessionMgt", "Successful Login");
			createLoginSession(username, sess);
			return true;
		}
		
		
		return false;
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String sessionId) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing sessionId in pref
		editor.putString(SESS_ID, sessionId);

		// commit changes
		editor.commit();
	}

	// Retrieve the stored username
	public String getName(){
		String name = pref.getString(KEY_NAME, null);
		return name;
	}
	
	// Retrieve the stored session Id
	public String getSessId(){
		String sessId = pref.getString(SESS_ID, null);
		return sessId;
	}
	
	/**
	 * Check login method will check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin(String sessId) {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			
			/* TODO from SG
			Intent i = new Intent(_context, LoginFragment.class);
			
			i.setAction("LOGIN");
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
			*/
		}

	}

	//clear username and sessID
	void clear(){
		editor.clear();
		editor.commit();
	}
	
	// Forcibly open the login page
	public void gotoLogin() {
		/* TODO from SG
		Log.d("intent data", "In gotologin");
		// user is not logged in redirect him to Login Activity

		Intent i = new Intent(_context, Login.class);
		i.setAction("LOGIN");
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
		 */
	}

	/*
	 * Quick check for login *
	 */
	// Get Login State
	public boolean isLoggedIn() {
		if (getSessId()!=null) {
			return true;
		}
		/* TODO from SG
		String result = api.getUsername(sessId);

		if (result == null || result.isEmpty()) {
			return false;
		}
		*/
		
		return false;
	}
}