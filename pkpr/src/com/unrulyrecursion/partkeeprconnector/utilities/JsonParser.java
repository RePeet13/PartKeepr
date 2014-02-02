package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.IOException;
import java.util.ArrayList;
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

import com.unrulyrecursion.partkeeprconnector.model.Part;
import com.unrulyrecursion.partkeeprconnector.model.PartCategory;

import android.util.Log;

public class JsonParser {
			
	// JSON Wrapper Node names
	// Response Wrapper
	public static final String TAG_STATUS = "status";
	public static final String TAG_SUCCESS = "success";
	public static final String TAG_RESPONSE = "response";
	public static final String TAG_TIMING = "timing";
	public static final String TAG_DATA = "data";
	public static final String TAG_TOTAL_COUNT = "totalCount";
	public static final String TAG_EXCEPTION = "exception";
	public static final String TAG_MESSAGE = "message";
	public static final String TAG_DETAIL = "detail";
	public static final String TAG_CODE = "code";
	// Login
	public static final String TAG_SESSION_ID = "sessionid";
	// Part Categories
	private static final String TAG_PC_ID = "id";
	private static final String TAG_PC_NAME = "name";
	private static final String TAG_PC_DESCRIPTION = "description";
	private static final String TAG_PC_CHILDREN = "children";
	private static final String TAG_PC_LEAF = "leaf";
	private static final String TAG_PC_EXPANDED = "expanded"; // If should be expanded on open
	// Parts
	private static final String TAG_P_NAME = "name";
	private static final String TAG_P_DESCRIPTION = "description";
	private static final String TAG_P_AVERAGE_PRICE = "averagePrice";
	private static final String TAG_P_STATUS = "status";
	private static final String TAG_P_NEEDS_REVIEW = "needsReview";
	private static final String TAG_P_CREATE_DATE = "createDate";
	private static final String TAG_P_ID = "id";
	private static final String TAG_P_STOCK_LEVEL = "stockLevel";
	private static final String TAG_P_MIN_STOCK_LEVEL = "minStockLevel";
	private static final String TAG_P_COMMENT = "comment";
	private static final String TAG_P_STORAGE_LOCATION_ID = "storageLocation_id";
	private static final String TAG_P_CATEGORY_PATH = "categoryPath";
	private static final String TAG_P_STORAGE_LOCATION_NAME = "storageLocationName";
	private static final String TAG_P_FOOTPRINT_ID = "footprint_id";
	private static final String TAG_P_FOOTPRINT_NAME = "footprintName";
	private static final String TAG_P_PART_CATEGORY = "category";
	private static final String TAG_P_PART_CATEGORY_NAME = "categoryName";
	private static final String TAG_P_PART_UNIT = "partUnit";
	private static final String TAG_P_PART_UNIT_NAME = "partUnitName";
	private static final String TAG_P_PART_UNIT_SHORT_NAME = "partUnitShortName";
	private static final String TAG_P_PART_UNIT_DEFAULT = "partUnitDefault";
	private static final String TAG_P_PART_CONDITION = "partCondition";
	private static final String TAG_P_ATTACHMENT_COUNT = "attachmentCount";
	private static final String TAG_P_ATTACHMENTS = "attachments";
	// Part Attachments
	private static final String TAG_PA = "something"; // TODO fill in
	
	private static JSONArray tmp;
	
	public JsonParser() {
	}
	
	public static String parseLoginResponse(JSONObject res) {

		try { //grab the JSON data from the URL and return the entities of it.
			
			Log.d("JSON L Parser","Checking server response");
			JSONObject o = res;
			Double timing = o.getDouble(TAG_TIMING);
			JSONObject response = o.optJSONObject(TAG_RESPONSE);
			String out = null;
			
			/*
			if (status.compareToIgnoreCase("ok")==0) { // response is expected "ok"
				Log.d("JSON Parser", "response status ok");
			}
			if (success.compareToIgnoreCase("true")==0) {
				Log.d("JSON Parser", "response success true");
			}
			*/
			Log.d("JSON L Parser", "Timing:"+timing);
			
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

	/*
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
	*/
	
	public static PartCategory parsePartCategories(JSONObject in) {
		Log.d("JSON PC Parser","Parsing Part Categories");
		PartCategory pc = new PartCategory();
		try {
			Log.d("JSON PC Parser", "JSON: " + in.toString()); // TODO comment out
			Log.d("JSON PC Parser", "id: ");// + in.getInt(TAG_PC_ID) + " name: " + in.getString(TAG_PC_NAME) + " leaf: " + in.getBoolean(TAG_PC_LEAF));
			if (in.has(TAG_PC_ID)) {pc.setId(in.getInt(TAG_PC_ID));}
			if (in.has(TAG_PC_NAME)) {pc.setName(in.getString(TAG_PC_NAME));}
			if (in.has(TAG_PC_DESCRIPTION)) {pc.setDescription(in.getString(TAG_PC_DESCRIPTION));}
			if (in.has(TAG_PC_EXPANDED)) {pc.setExpanded(in.getBoolean(TAG_PC_EXPANDED));}
			if (in.has(TAG_PC_LEAF)) {pc.setLeaf(in.getBoolean(TAG_PC_LEAF));}
		} catch (JSONException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
		
		if (pc.getLeaf()) {return pc;}
		tmp = null;
		
		try {
			tmp = in.getJSONArray(TAG_PC_CHILDREN);
		} catch (JSONException e) {
			
		}
		
		if (tmp != null) {
			JSONObject otmp;
			PartCategory kid;
			
			for (int i = 0; i < tmp.length(); i++) {
				Log.d("JSON PC Parser", "Parsing child " + i);
				try {
					kid = new PartCategory();
					otmp = tmp.getJSONObject(i);
					kid.setId(in.getInt(TAG_PC_ID));
					kid.setName(in.getString(TAG_PC_NAME));
					kid.setDescription(in.getString(TAG_PC_DESCRIPTION));
					kid.setExpanded(in.getBoolean(TAG_PC_EXPANDED));
					kid.setLeaf(in.getBoolean(TAG_PC_LEAF));
					pc.addChild(kid);
				} catch (JSONException e) {
					// Need anything here?
				}
			}
		}
		return pc;
	}
	
	public static ArrayList<Part> parsePartsList(JSONObject in) {
		Log.d("JSON P Parser","Parsing Part");
		ArrayList<Part> parts = new ArrayList<Part>();
		try {
			Log.d("JSON P Parser", "Part JSON: " + in.toString()); // TODO comment out

			JSONArray tmp = in.getJSONArray(TAG_DATA);
			int total = in.getInt(TAG_TOTAL_COUNT);
			JSONObject otmp;
			Part p;
			for (int i=0; i<total; i++) {
				otmp = tmp.getJSONObject(i);
				p = new Part();
				
				p.setName(in.getString(TAG_P_NAME));
				p.setDescription(in.getString(TAG_P_DESCRIPTION));
				p.setStatus(in.getString(TAG_P_STATUS));
				p.setCreateDate(in.getString(TAG_P_CREATE_DATE));
				p.setPcName(in.getString(TAG_PC_NAME));
				p.setId(in.getInt(TAG_P_ID));
				p.setCategoryId(in.getInt(TAG_P_PART_CATEGORY));
				p.setStorageLocId(in.getInt(TAG_P_STORAGE_LOCATION_ID));
				p.setStorageLocName(in.getString(TAG_P_STORAGE_LOCATION_NAME));
				p.setAttachmentCount(in.getInt(TAG_P_ATTACHMENT_COUNT));
				
				parts.add(p);
			}
			return parts;

			/*
			p.setNeedsReview(in.getBoolean(TAG_P_NEEDS_REVIEW));
			private static final String TAG_P_AVERAGE_PRICE = "averagePrice";
			private static final String TAG_P_NEEDS_REVIEW = "needsReview";
			private static final String TAG_P_ID = "id";
			private static final String TAG_P_STOCK_LEVEL = "stockLevel";
			private static final String TAG_P_MIN_STOCK_LEVEL = "minStockLevel";
			private static final String TAG_P_COMMENT = "comment";
			private static final String TAG_P_STORAGE_LOCATION_ID = "storageLocation_id";
			private static final String TAG_P_CATEGORY_PATH = "categoryPath";
			private static final String TAG_P_STORAGE_LOCATION_NAME = "storageLocationName";
			private static final String TAG_P_FOOTPRINT_ID = "footprint_id";
			private static final String TAG_P_FOOTPRINT_NAME = "footprintName";
			private static final String TAG_P_PART_CATEGORY = "category";
			private static final String TAG_P_PART_CATEGORY_NAME = "categoryName";
			private static final String TAG_P_PART_UNIT = "partUnit";
			private static final String TAG_P_PART_UNIT_NAME = "partUnitName";
			private static final String TAG_P_PART_UNIT_SHORT_NAME = "partUnitShortName";
			private static final String TAG_P_PART_UNIT_DEFAULT = "partUnitDefault";
			private static final String TAG_P_PART_CONDITION = "partCondition";
			private static final String TAG_P_ATTACHMENT_COUNT = "attachmentCount";
			private static final String TAG_P_ATTACHMENTS = "attachments";
			*/
			
		} catch (JSONException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}
}
