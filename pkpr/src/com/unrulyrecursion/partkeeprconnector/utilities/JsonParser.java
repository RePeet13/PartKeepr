package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.PartCategory;

import android.util.Log;

public class JsonParser {
			
	// JSON Wrapper Node names
	// Response Wrapper
	public static final String TAG_STATUS = "status";
	public static final String TAG_SUCCESS = "success";
	public static final String TAG_RESPONSE = "response";
	public static final String TAG_TIMING = "timing";
	// Login
	public static final String TAG_SESSION_ID = "sessionid";
	// Part Categories
	private static final String TAG_PART_CATEGORY_ID = "id";
	private static final String TAG_PART_CATEGORY_NAME = "name";
	private static final String TAG_PART_CATEGORY_DESCRIPTION = "description";
	private static final String TAG_PART_CATEGORY_CHILDREN = "children";
	private static final String TAG_PART_CATEGORY_LEAF = "leaf";
	private static final String TAG_PART_CATEGORY_EXPANDED = "expanded"; // If should be expanded on open
	
	private static JSONArray tmp;
	
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

	public static JSONObject getJSONFromUrl(String url) {
		return getJSONFromUrl(url, "none");
	}
	public static JSONObject getJSONFromUrl(String url, String sessionId) {

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
	
	public static PartCategory parsePartCategories(JSONObject in) {
		Log.d("JSON Parser","Parsing Part Categories");
		PartCategory pc = new PartCategory();
		
		try {
			pc.setId(in.getInt(TAG_PART_CATEGORY_ID));
			pc.setName(in.getString(TAG_PART_CATEGORY_NAME));
			pc.setDescription(in.getString(TAG_PART_CATEGORY_DESCRIPTION));
			pc.setExpanded(in.getBoolean(TAG_PART_CATEGORY_EXPANDED));
			pc.setLeaf(in.getBoolean(TAG_PART_CATEGORY_LEAF));
		} catch (JSONException e) {
			return null;
		}
		
		if (pc.getLeaf()) {return pc;}
		tmp = null;
		
		try {
			tmp = in.getJSONArray(TAG_PART_CATEGORY_CHILDREN);
		} catch (JSONException e) {
			
		}
		
		JSONObject otmp;
		PartCategory kid;
		
		for (int i = 0; i < tmp.length(); i++) {
			try {
				kid = new PartCategory();
				otmp = tmp.getJSONObject(i);
				kid.setId(in.getInt(TAG_PART_CATEGORY_ID));
				kid.setName(in.getString(TAG_PART_CATEGORY_NAME));
				kid.setDescription(in.getString(TAG_PART_CATEGORY_DESCRIPTION));
				kid.setExpanded(in.getBoolean(TAG_PART_CATEGORY_EXPANDED));
				kid.setLeaf(in.getBoolean(TAG_PART_CATEGORY_LEAF));
				pc.addChild(kid);
			} catch (JSONException e) {
				// Need anything here?
			}
			
		}
		return pc;
	}
}
