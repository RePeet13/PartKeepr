package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
			
	// JSON Wrapper Node names
	private static final String TAG_STATUS = "status";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_RESPONSE = "response";
	private static final String TAG_TIMING = "timing";
	private static final String TAG_SESSION_ID = "sessionid";
	
	public JsonParser() {
	}
	
	public static String parseLoginResponse(JSONArray in) {

		try { //grab the JSON data from the URL and return the entities of it.
			
			Log.d("JSON Parser","Checking server response");
			JSONObject o = in.getJSONObject(0);
			String status = o.getString(TAG_STATUS);
			String success = o.getString(TAG_SUCCESS);
			Double timing = o.getDouble(TAG_TIMING);
			JSONObject response = o.optJSONObject(TAG_RESPONSE);
			String out = null;
			
			if (status.compareToIgnoreCase("ok")==0) { // response is expected "ok"
				Log.d("JSON Parser", "response status ok");
			}
			if (success.compareToIgnoreCase("true")==0) {
				Log.d("JSON Parser", "response success true");
			}
			Log.d("JSON Parser", "Timing:"+timing);
			
			out = response.getString(TAG_SESSION_ID);
			
			return out;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getJSONFromUrl(String url) {
		return getJSONFromUrl(url, "none");
	}
	public JSONObject getJSONFromUrl(String url, String sessionId) {

		try { //grab the JSON data from the URL and return the entities of it.
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			if (sessionId.compareToIgnoreCase("none")!=0) {
				request.setHeader("Cookie", "PHPSESSID=" + sessionId);
			}
			JSONArray res = new JSONArray(EntityUtils.toString(client.execute(request).getEntity()));
			
			Log.d("JSON Parser","Checking server response");
			JSONObject o = res.getJSONObject(0);
			String status = o.getString(TAG_STATUS);
			String success = o.getString(TAG_SUCCESS);
			Double timing = o.getDouble(TAG_TIMING);
			JSONObject response = o.optJSONObject(TAG_RESPONSE);
			
			if (status.compareToIgnoreCase("ok")==0) { // response is expected "ok"
				Log.d("JSON Parser", "response status ok");
			}
			
			if (success.compareToIgnoreCase("true")==0) {
				Log.d("JSON Parser", "response success true");
			}
			
			Log.d("JSON Parser", "Timing:"+timing);
			
			return response;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
