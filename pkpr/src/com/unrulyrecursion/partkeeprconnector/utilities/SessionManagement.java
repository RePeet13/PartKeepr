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
		Log.d("Session Management","Entering Login");
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(MainActivity.base_url);
		httpPost.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", passHash));
		
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpResponse response;
		JSONArray res = null;
		try {
			response = httpClient.execute(httpPost);
			res = new JSONArray(EntityUtils.toString(response.getEntity()));
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
	
	private class getRestTask extends AsyncTask<String, Integer, JSONObject> {

		private String[] urlParts;
		@Override
		protected JSONObject doInBackground(String... strings) {
			int count = strings.length;
			urlParts = strings;
			/*
			for (int i = 0; i < count; i++) {
				try {
					// TODO implement multiple requests if wanted
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			
			Log.d("JSON Task", "Building URL");
			String url = base_url + urlParts[0];
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			String[] status = new String[5];

			if (session_id.compareToIgnoreCase("none")!=0) {
				request.setHeader("Cookie", "PHPSESSID=" + session_id);
			}
			try {
				Log.d("JSON Task","Getting response from server");
				JSONArray result = new JSONArray(EntityUtils.toString(client.execute(request).getEntity()));

				Log.d("JSON Task","Checking server response");
				JSONObject o = result.getJSONObject(0);
				status[0] = o.getString(JsonParser.TAG_STATUS);
				status[1] = o.getString(JsonParser.TAG_SUCCESS);
				status[2] = o.getString(JsonParser.TAG_TIMING);
				Log.d("JSON Task", "Seems ok, timing: " + status[2]);
				JSONObject response = o.optJSONObject(JsonParser.TAG_RESPONSE);
				
				if (status[0].compareToIgnoreCase("ok")==0) { // response is expected "ok"
					Log.d("JSON Task", "response status ok");
				}
				
				if (status[1].compareToIgnoreCase("true")==0) {
					Log.d("JSON Task", "response success true");
				}
				
				return response;
				
			} catch (JSONException e) {
				Log.w("JSON Task", "JSONException thrown");
				Log.w("JSON Task", e.getLocalizedMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.w("JSON Task", "IOException thrown");
				Log.w("JSON Task", e.getLocalizedMessage());
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// TODO Auto-generated method stub
//	        showDialog("Downloaded " + result + " bytes"); // Downloaded result results
		}
	}
}
