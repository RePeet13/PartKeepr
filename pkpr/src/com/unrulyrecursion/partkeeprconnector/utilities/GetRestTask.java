package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.MainActivity;
import com.unrulyrecursion.partkeeprconnector.PartKeeprConnectorApp;

import android.os.AsyncTask;
import android.util.Log;

public class GetRestTask extends AsyncTask<String, Integer, JSONObject> {

		private String[] urlParts;

		@Override
		protected JSONObject doInBackground(String... strings) {
			/*
			 * Strings
			 * [0] Http method
			 * [1] base_url
			 * [2] url_part
			 * [3] sessionid
			 */
			
			// TODO should check whether this should be in retained fragment
			int count = strings.length;
			urlParts = strings;
			
			String url = urlParts[1] + urlParts[2];
			Log.d("JSON Task", "Building URL: " + url);
			
			HttpClient client = new DefaultHttpClient();
			HttpRequestBase request = null;
			if (urlParts[0].equalsIgnoreCase("POST")) {
				request = new HttpPost(url);
			} else if (urlParts[0].equalsIgnoreCase("GET")) {
				request = new HttpGet(url);
			}
			String[] status = new String[5];
//			String sid = MainActivity.session.getSessId();
			String sid = urlParts[3];

			if (sid.compareToIgnoreCase("none")!=0) {
//				request.setHeader("Cookie", "PHPSESSID=" + sid);
				request.setHeader("session", sid); // TODO could be more robust (null check)
			}
			try {
				Log.d("JSON Task","Getting response from server");
				JSONObject result = new JSONObject(EntityUtils.toString(client.execute(request).getEntity()));
				
				// TODO Add progress update stuff
				
				//Log.d("result", result.toString());
				Log.d("JSON Task","Checking server response");
				status[0] = result.getString(JsonParser.TAG_STATUS);
				status[1] = result.getString(JsonParser.TAG_SUCCESS);
				status[2] = result.getString(JsonParser.TAG_TIMING);
				
				Log.d("JSON Task", "timing: " + status[2]);
				
				if (status[0].compareToIgnoreCase("ok")==0) { // response is expected "ok"
					Log.d("JSON Task", "response status ok");
				} else {
					Log.d("JSON Task", "response not ok: " + status[0]);
				}
				
				if (status[1].compareToIgnoreCase("true") == 0) {
					Log.d("JSON Task", "response success true");
				} else {
					// TODO actually error somehow here (toast notification or so)
					// TODO should print exception out and stop response check
				}
				JSONObject response = result.getJSONObject(JsonParser.TAG_RESPONSE);
				//Log.d("Get REST Task", response.toString());

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

			
			// Log.d("Get Rest Task", "Didn't return the response");
			
			return null;
		}
}
