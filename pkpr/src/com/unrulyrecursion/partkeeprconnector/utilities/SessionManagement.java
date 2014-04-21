package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
import com.unrulyrecursion.partkeeprconnector.PartKeeprConnectorApp;
import com.unrulyrecursion.partkeeprconnector.model.Part;
import com.unrulyrecursion.partkeeprconnector.model.PartCategory;
import com.unrulyrecursion.partkeeprconnector.model.StorageLocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class SessionManagement {

	public String base_url;
	public String session_id;
	private Map<String, String> status;
	private ProgressBar pb;
	private String loginUrlPart = "Auth/login";
	private String statusUrlPart = "System/getSystemStatus";
	private String pcUrlPart = "PartCategory/getAllCategories";
	private static String slUrlPart = "StorageLocation?_dc=1387642414789&page=1&start=0&limit=-1"; // TODO figure this out for real
	/*
	 * QueryAPI api;
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

	// The user permissions
	public static final String USER_PERM = "permissions";

	// Constructor
	public SessionManagement(Context context, String link) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();

		this.base_url = link;
		/*
		 * QueryAPI api = new QueryAPI(link);
		 */
	}

	public void setBaseUrl(String url) {
		this.base_url = url;
	}

	/**
	 * 
	 * @param username
	 * @param passHash
	 * @param tSessionId
	 */
	public Boolean login(String username, String passHash, String tSessionId) {
		// TODO might want to move this into asynctask since network
		String session = tSessionId;
		if (passHash != null && tSessionId == null) {
			Log.d("Session Management", "Building Login POST");
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(base_url + loginUrlPart);
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", passHash));

			Log.d("Session Management", "Adding namevalue pairs");
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpResponse response;
			JSONObject res = null;
			try {
				Log.d("Session Management", "Executing Login POST");
				response = httpClient.execute(httpPost);
				// TODO should handle typical server responses (404, etc)
				String resp =EntityUtils.toString(response.getEntity());
				Log.d("Session Management", "Server Response: "+resp);
				res = new JSONObject(resp);
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
				session = sess;
				return true;
			} else {
				Log.d("SessionMgt", "Login not successful");
				return false;
			}
		} else if (tSessionId != null) {
			Log.d("Session Management", "Going to attempt sessionId reuse - " + tSessionId);
			session = tSessionId;
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
	public String getName() {
		String name = pref.getString(KEY_NAME, null);
		return name;
	}

	// Retrieve the stored session Id
	public String getSessId() {
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

			/*
			 * TODO from SG Intent i = new Intent(_context,
			 * LoginFragment.class);
			 * 
			 * i.setAction("LOGIN"); // Closing all the Activities
			 * i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * 
			 * // Add new Flag to start new Activity
			 * i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * 
			 * // Staring Login Activity _context.startActivity(i);
			 */
		}

	}

	// clear username and sessID
	void clear() {
		editor.clear();
		editor.commit();
	}

	// Forcibly open the login page
	public void gotoLogin() {
		/*
		 * TODO from SG Log.d("intent data", "In gotologin"); // user is not
		 * logged in redirect him to Login Activity
		 * 
		 * Intent i = new Intent(_context, Login.class); i.setAction("LOGIN");
		 * // Closing all the Activities
		 * i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * 
		 * // Add new Flag to start new Activity
		 * i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * 
		 * // Staring Login Activity _context.startActivity(i);
		 */
	}

	/*
	 * Quick check for login *
	 */
	// Get Login State
	public boolean isLoggedIn() {
		if (getSessId() != null) {
			return true;
		}
		/*
		 * TODO from SG String result = api.getUsername(sessId);
		 * 
		 * if (result == null || result.isEmpty()) { return false; }
		 */

		return false;
	}
	
	public Map<String, String> getStatus() {
		// TODO REMOVE
		AsyncTask<String, Integer, JSONObject> othertask = new GetAllDataTask().execute(
			"POST", base_url, statusUrlPart, getSessId());

		AsyncTask<String, Integer, JSONObject> task = new GetStatusTask().execute(
				"POST", base_url, statusUrlPart, getSessId());
		try {
			task.get();
			Log.d("Session", "After task gotten: "+ task.getStatus());
//			while(task.getStatus() != AsyncTask.Status.FINISHED)
//			{}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}

	public ProgressBar getProgressBar() {
		return pb;
	}

	public void setProgressBar(ProgressBar pb) {
		this.pb = pb;
	}

	private class GetStatusTask extends GetRestTask {

		@Override
		protected JSONObject doInBackground(String... strings) {
			JSONObject response = super.doInBackground(strings);
			Log.d("GetStatusTask", "It worked!");
// TODO convert the rest to have work done in background thread with super call
			status = new HashMap<String, String>();
			Map<String, String> tmp = JsonParser.parseStatus(response);
			if (tmp != null) {
				status.putAll(tmp);
				Log.d("GetStatusTask", "Success");
			} else {
				Log.d("GetStatusTask", "Failure");
			}
			return response;
		}
	}
	
	// TODO Eventually put all urlparts into string resources, and allow editing (separate screen)
	private class GetAllDataTask extends GetRestTask {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pb.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... strings) {
			/*
			 * Get Status
			 */
			JSONObject statusRes = super.doInBackground("POST", base_url, statusUrlPart, getSessId());
			Log.d("GetStatusTask", "It worked!");
			status = new HashMap<String, String>();
			Map<String, String> tmp = JsonParser.parseStatus(statusRes);
			if (tmp != null) {
				status.putAll(tmp);
				Log.d("GetStatusTask", "Success");
			} else {
				Log.d("GetStatusTask", "Failure");
			}
			
			publishProgress(10);
			/*
			 * Get Part Categories
			 */

//			Log.d("Part Category LF", "Entering doInBackground");
			JSONObject pcRes = super.doInBackground("POST", base_url, pcUrlPart, getSessId());
			PartCategory pc = JsonParser.parsePartCategories(pcRes);
			// TODO commit to db
			publishProgress(35);
			
			/*
			 * Get Storage Locations
			 */

			JSONObject slRes = super.doInBackground("GET", base_url, slUrlPart, getSessId());
			ArrayList<StorageLocation> storageLocations = JsonParser.parseStorageLocationList(slRes);
			// TODO commit to db
			publishProgress(50);
			
			/*
			 * Get Parts
			 */
			 
			/* Part?_dc=1392816881375&category=10&page=1&start=0&limit=50&group=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%5D&sort=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22name%22%2C%22direction%22%3A%22ASC%22%7D%5D
			 * This is a webapp generated url with the following properties
			 * (the url is form encoded)
			 * Query Params: 
			 * _dc:1392816881375 //dc=don't cache, just a time in millis
			 * category:10 
			 * page:1 
			 * start:0 
			 * limit:50 
			 * group:[{"property":"categoryPath","direction":"ASC"}] 
			 * sort:[{"property":"categoryPath","direction":"ASC"},{"property":"name","direction":"ASC"}]
			 * 
			 * TODO figure out how this is generated and how I can handle generating it without the ugliness above
			 * remove categorypath from the sort..
			 * */
			
			ArrayList<Part> partsList = new ArrayList<Part>();
			 for (PartCategory partc : pc.flatten()){
				 String url = "Part?_dc=1392816881375&category="+partc.getId()+"&page=1&start=0&group=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%5D&sort=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22name%22%2C%22direction%22%3A%22ASC%22%7D%5D";
					
				 JSONObject pRes = super.doInBackground("GET", base_url, url, getSessId());
					partsList.addAll(JsonParser.parsePartsList(pRes));
			 }
			// TODO commit to db
			 publishProgress(100);
			
			return statusRes;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			pb.setProgress(values[0]);
			Log.d("Session Login", "Progress - " + values[0] + "%");
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			pb.setVisibility(View.GONE);
		}
		
	}

}
