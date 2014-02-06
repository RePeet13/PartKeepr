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
import com.unrulyrecursion.partkeeprconnector.model.StorageLocation;

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
	public static final String TAG_PC_ID = "id";
	public static final String TAG_PC_NAME = "name";
	public static final String TAG_PC_DESCRIPTION = "description";
	public static final String TAG_PC_CHILDREN = "children";
	public static final String TAG_PC_LEAF = "leaf";
	public static final String TAG_PC_EXPANDED = "expanded"; // If should be expanded on open
	// Parts
	public static final String TAG_P_NAME = "name";
	public static final String TAG_P_DESCRIPTION = "description";
	public static final String TAG_P_AVERAGE_PRICE = "averagePrice";
	public static final String TAG_P_STATUS = "status";
	public static final String TAG_P_NEEDS_REVIEW = "needsReview";
	public static final String TAG_P_CREATE_DATE = "createDate";
	public static final String TAG_P_ID = "id";
	public static final String TAG_P_STOCK_LEVEL = "stockLevel";
	public static final String TAG_P_MIN_STOCK_LEVEL = "minStockLevel";
	public static final String TAG_P_COMMENT = "comment";
	public static final String TAG_P_STORAGE_LOCATION_ID = "storageLocation_id";
	public static final String TAG_P_CATEGORY_PATH = "categoryPath";
	public static final String TAG_P_STORAGE_LOCATION_NAME = "storageLocationName";
	public static final String TAG_P_FOOTPRINT_ID = "footprint_id";
	public static final String TAG_P_FOOTPRINT_NAME = "footprintName";
	public static final String TAG_P_PART_CATEGORY = "category";
	public static final String TAG_P_PART_CATEGORY_NAME = "categoryName";
	public static final String TAG_P_PART_UNIT = "partUnit";
	public static final String TAG_P_PART_UNIT_NAME = "partUnitName";
	public static final String TAG_P_PART_UNIT_SHORT_NAME = "partUnitShortName";
	public static final String TAG_P_PART_UNIT_DEFAULT = "partUnitDefault";
	public static final String TAG_P_PART_CONDITION = "partCondition";
	public static final String TAG_P_ATTACHMENT_COUNT = "attachmentCount";
	public static final String TAG_P_ATTACHMENTS = "attachments";
	// Part Attachments
	public static final String TAG_PA = "something"; // TODO fill in
	// Storage Locations
	public static final String TAG_SL_ID = "id";
	public static final String TAG_SL_NAME = "name";
	public static final String TAG_SL_START = "start"; //might need to go up with data and total count
	
	public static int depth = 1; // used to set depth of children
	
	public JsonParser() { }
	
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
			//Log.d("JSON PC Parser", "JSON: " + in.toString());
			if (in.has(TAG_PC_ID)) {pc.setId(in.getInt(TAG_PC_ID)); Log.d("JSON PC Parser", "id: " + in.getInt(TAG_PC_ID));}
			if (in.has(TAG_PC_NAME)) {pc.setName(in.getString(TAG_PC_NAME));}
			if (in.has(TAG_PC_DESCRIPTION)) {pc.setDescription(in.getString(TAG_PC_DESCRIPTION));}
			if (in.has(TAG_PC_EXPANDED)) {pc.setExpanded(in.getBoolean(TAG_PC_EXPANDED));}
			if (in.has(TAG_PC_LEAF)) {pc.setLeaf(in.getBoolean(TAG_PC_LEAF));}
			
			pc.setDepth(depth);
		} catch (JSONException e) {
			Log.w("JSON PC Parser", pc.getId() + " failed something");
			return null;
		} catch (NullPointerException e) {
			return null;
		}
		
		if (pc.getLeaf()) {
			return pc;
		} else {
			try {
				JSONArray tmp = in.getJSONArray(TAG_PC_CHILDREN);
				for (int i = 0; i < tmp.length(); i++) {
					Log.d("JSON PC Parser", "Parsing child " + i + " of " + tmp.length());
					depth++;
					Log.d("JSON PC Parser", "Setting bearing to 156 and depth to " + depth + ", dive! dive!");
					pc.addChild(parsePartCategories(tmp.getJSONObject(i)));
					depth--;
				}
				Log.d("JSON PC Parser", "Surfacing one level " + depth);
			} catch (JSONException e) {
				Log.w("JSON PC Parser", "Problem adding child");
				// Need anything here?
			}
		}
		
		return pc;
	}
	
	public static ArrayList<Part> parsePartsList(JSONObject in) {
		Log.d("JSON P Parser","Parsing Part");
		ArrayList<Part> parts = new ArrayList<Part>();
		try {
			//Log.d("JSON P Parser", "Part JSON: " + in.toString()); // TODO comment out

			JSONArray tmp = in.getJSONArray(TAG_DATA);
			Log.d("JSON P Parser", "Data: " + tmp.toString());
			int total = 0;
			if (in.has(TAG_TOTAL_COUNT)) {total = in.getInt(TAG_TOTAL_COUNT);}
			Log.d("JSON P Parser","Total Parts: " + total);
			JSONObject otmp;
			Part p;
			for (int i=0; i<total; i++) {
				Log.d("JSON P Parser" , "Getting part " + i);
				otmp = tmp.getJSONObject(i);
				p = new Part();
				
				if (otmp.has(TAG_P_NAME)) {p.setName(otmp.getString(TAG_P_NAME));}
				if (otmp.has(TAG_P_DESCRIPTION)) {p.setDescription(otmp.getString(TAG_P_DESCRIPTION));}
				if (otmp.has(TAG_P_STATUS)) {p.setStatus(otmp.getString(TAG_P_STATUS));}
				if (otmp.has(TAG_P_CREATE_DATE)) {p.setCreateDate(otmp.getString(TAG_P_CREATE_DATE));}
				if (otmp.has(TAG_P_CREATE_DATE)) {p.setPcName(otmp.getString(TAG_PC_NAME));}
				if (otmp.has(TAG_P_ID)) {p.setId(otmp.getInt(TAG_P_ID));}
				if (otmp.has(TAG_P_PART_CATEGORY)) {p.setCategoryId(otmp.getInt(TAG_P_PART_CATEGORY));}
				if (otmp.has(TAG_P_STORAGE_LOCATION_ID)) {p.setStorageLocId(otmp.getInt(TAG_P_STORAGE_LOCATION_ID));}
				if (otmp.has(TAG_P_STORAGE_LOCATION_NAME)) {p.setStorageLocName(otmp.getString(TAG_P_STORAGE_LOCATION_NAME));}
				if (otmp.has(TAG_P_ATTACHMENT_COUNT)) {p.setAttachmentCount(otmp.getInt(TAG_P_ATTACHMENT_COUNT));}
				Log.d("JSON P Parser", "Adding part to array");
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
			Log.d("JSON P Parser", "There was a naughty Json Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.d("JSON P Parser", "There was a naughty Null Exception");
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<StorageLocation> parseStorageLocationList(JSONObject in) {
		Log.d("JSON SL Parser","Parsing Storage Location");
		ArrayList<StorageLocation> sl = new ArrayList<StorageLocation>();
		try {
			//Log.d("JSON SL Parser", "SL JSON: " + in.toString()); // TODO comment out

			JSONArray tmp = in.getJSONArray(TAG_DATA);
			Log.d("JSON SL Parser", "Data: " + tmp.toString());
			int total = 0;
			if (in.has(TAG_TOTAL_COUNT)) {total = in.getInt(TAG_TOTAL_COUNT);}
			Log.d("JSON SL Parser","Total Storage Locations: " + total);
			JSONObject otmp;
			StorageLocation sloc;
			for (int i=0; i<total; i++) {
				Log.d("JSON SL Parser" , "Getting StorageLocation " + i);
				otmp = tmp.getJSONObject(i);
				sloc = new StorageLocation();
				
				if (otmp.has(TAG_P_NAME)) {sloc.setName(otmp.getString(TAG_P_NAME));}
				if (otmp.has(TAG_P_ID)) {sloc.setId(otmp.getInt(TAG_P_ID));}
				Log.d("JSON SL Parser", "Adding location to array");
				sl.add(sloc);
			}
			return sl;

			
		} catch (JSONException e) {
			Log.d("JSON SL Parser", "There was a naughty Json Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.d("JSON SL Parser", "There was a naughty Null Exception");
			e.printStackTrace();
		}
		return null;
	}
}
