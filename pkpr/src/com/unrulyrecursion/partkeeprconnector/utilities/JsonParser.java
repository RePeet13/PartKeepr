package com.unrulyrecursion.partkeeprconnector.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.Part;
import com.unrulyrecursion.partkeeprconnector.model.PartCategory;
import com.unrulyrecursion.partkeeprconnector.model.StorageLocation;
import com.unrulyrecursion.partkeeprconnector.model.User;

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
	public static final String TAG_ID = "id";
	// Login
	public static final String TAG_SESSION_ID = "sessionid";
	// Part Categories
	// public static final String TAG_PC_ID = "id";
	public static final String TAG_PC_NAME = "name";
	public static final String TAG_PC_DESCRIPTION = "description";
	public static final String TAG_PC_CHILDREN = "children";
	public static final String TAG_PC_LEAF = "leaf";
	public static final String TAG_PC_EXPANDED = "expanded"; // If should be
																// expanded on
																// open
	// Parts
	public static final String TAG_P_NAME = "name";
	public static final String TAG_P_DESCRIPTION = "description";
	public static final String TAG_P_AVERAGE_PRICE = "averagePrice";
	public static final String TAG_P_STATUS = "status";
	public static final String TAG_P_NEEDS_REVIEW = "needsReview";
	public static final String TAG_P_CREATE_DATE = "createDate";
	// public static final String TAG_P_ID = "id";
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
	// public static final String TAG_SL_ID = "id";
	public static final String TAG_SL_NAME = "name";
	public static final String TAG_SL_START = "start";
	// Users
	// public static final String TAG_U_ID = "id";
	public static final String TAG_U_NAME = "username";
	// Status
	public static final String TAG_S_INACTIVE_CRON_COUNT = "inactiveCronjobCount";
	public static final String TAG_S_INACTIVE_CRON_JOBS = "inactiveCronjobs";
	public static final String TAG_S_SCHEMA = "schemaStatus";

	public static int depth = 1; // used to set depth of children

	private static Boolean log = false;

	public JsonParser() {
	}

	public static String parseLoginResponse(JSONObject res) {

		try { // grab the JSON data from the URL and return the entities of it.

			if (log)
				Log.d("JSON L Parser", "Checking server response");
			JSONObject o = res;
			Double timing = o.getDouble(TAG_TIMING);
			JSONObject response = o.optJSONObject(TAG_RESPONSE);
			String out = null;

			/*
			 * if (status.compareToIgnoreCase("ok")==0) { // response is
			 * expected "ok" Log.d("JSON Parser", "response status ok"); } if
			 * (success.compareToIgnoreCase("true")==0) { Log.d("JSON Parser",
			 * "response success true"); }
			 */

			if (log)
				Log.d("JSON L Parser", "Timing:" + timing);

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

	public static PartCategory parsePartCategories(JSONObject in) {
		if (log)
			Log.d("JSON PC Parser", "Parsing Part Categories");
		PartCategory pc = new PartCategory();

		try {
			if (log)
				Log.d("JSON PC Parser", "JSON: " + in.toString());
			if (in.has(TAG_ID)) {
				pc.setId(in.getInt(TAG_ID));
				Log.d("JSON PC Parser", "id: " + in.getInt(TAG_ID));
			}
			if (in.has(TAG_PC_NAME)) {
				pc.setName(in.getString(TAG_PC_NAME));
			}
			if (in.has(TAG_PC_DESCRIPTION)) {
				pc.setDescription(in.getString(TAG_PC_DESCRIPTION));
			}
			if (in.has(TAG_PC_EXPANDED)) {
				pc.setExpanded(in.getBoolean(TAG_PC_EXPANDED));
			}
			if (in.has(TAG_PC_LEAF)) {
				pc.setLeaf(in.getBoolean(TAG_PC_LEAF));
			}

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
				PartCategory tp;
				for (int i = 0; i < tmp.length(); i++) {
					if (log)
						Log.d("JSON PC Parser", "Parsing child " + i + " of "
								+ tmp.length());
					depth++;
					if (log)
						Log.d("JSON PC Parser",
								"Setting bearing to 156 and depth to " + depth
										+ ", dive! dive!");
					tp = parsePartCategories(tmp.getJSONObject(i));
					tp.setParent(pc);
					pc.addChild(tp);
					depth--;
				}
				if (log)
					Log.d("JSON PC Parser", "Surfacing one level " + depth);
			} catch (JSONException e) {
				Log.w("JSON PC Parser", "Problem adding child");
				// Need anything here?
			}
		}
		return pc;
	}

	public static ArrayList<Part> parsePartsList(JSONObject in) {
		if (log)
			Log.d("JSON P Parser", "Parsing Part");
		ArrayList<Part> parts = new ArrayList<Part>();
		try {
			// Log.d("JSON P Parser", "Part JSON: " + in.toString()); // TODO
			// comment out

			JSONArray tmp = in.getJSONArray(TAG_DATA);
			if (log)
				Log.d("JSON P Parser", "Data: " + tmp.toString());
			int total = 0;

			if (in.has(TAG_TOTAL_COUNT)) {
				total = in.getInt(TAG_TOTAL_COUNT);
				if (total > in.length()) {
					total = in.length();
				}
			}
			

			if (log)
				Log.d("JSON P Parser", "Total Parts: " + total);
			JSONObject otmp;
			Part p;
			for (int i = 0; i < total; i++) {
				if (log)
					Log.d("JSON P Parser", "Getting part " + i);
				otmp = tmp.getJSONObject(i);
				p = new Part();

				if (otmp.has(TAG_P_NAME)) {
					p.setName(otmp.getString(TAG_P_NAME));
				}
				if (otmp.has(TAG_P_DESCRIPTION)) {
					p.setDescription(otmp.getString(TAG_P_DESCRIPTION));
				}
				if (otmp.has(TAG_P_STATUS)) {
					p.setStatus(otmp.getString(TAG_P_STATUS));
				}
				if (otmp.has(TAG_P_CREATE_DATE)) {
					p.setCreateDate(otmp.getString(TAG_P_CREATE_DATE));
				}
				if (otmp.has(TAG_P_CREATE_DATE)) {
					p.setPcName(otmp.getString(TAG_PC_NAME));
				}
				if (otmp.has(TAG_ID)) {
					p.setId(otmp.getInt(TAG_ID));
				}
				if (otmp.has(TAG_P_PART_CATEGORY)) {
					p.setCategoryId(otmp.getInt(TAG_P_PART_CATEGORY));
				}
				if (otmp.has(TAG_P_STORAGE_LOCATION_ID)) {
					p.setStorageLocId(otmp.getInt(TAG_P_STORAGE_LOCATION_ID));
				}
				if (otmp.has(TAG_P_STORAGE_LOCATION_NAME)) {
					p.setStorageLocName(otmp
							.getString(TAG_P_STORAGE_LOCATION_NAME));
				}
				if (otmp.has(TAG_P_ATTACHMENT_COUNT)) {
					p.setAttachmentCount(otmp.getInt(TAG_P_ATTACHMENT_COUNT));
				}
				if (otmp.has(TAG_P_STOCK_LEVEL)) {
					p.setStockLevel(otmp.getInt(TAG_P_STOCK_LEVEL));
				}
				if (otmp.has(TAG_P_MIN_STOCK_LEVEL)) {
					p.setMinStockLevel(otmp.getInt(TAG_P_MIN_STOCK_LEVEL));
				}
				if (otmp.has(TAG_P_CATEGORY_PATH)) {
					p.setCategoryPath(otmp.getString(TAG_P_CATEGORY_PATH));
				}
				if (otmp.has(TAG_P_COMMENT)) {
					p.setComment(otmp.getString(TAG_P_COMMENT));
				}
				if (otmp.has(TAG_P_ATTACHMENTS)) {
					p.setAttachIds(parsePartAttachments(
							otmp.getJSONObject(TAG_P_ATTACHMENTS),
							p.getAttachmentCount()));
				}

				if (log)
					Log.d("JSON P Parser", "Adding part to array");
				parts.add(p);
			}
			return parts;

			/*
			 * p.setNeedsReview(in.getBoolean(TAG_P_NEEDS_REVIEW)); private
			 * static final String TAG_P_AVERAGE_PRICE = "averagePrice"; private
			 * static final String TAG_P_NEEDS_REVIEW = "needsReview"; private
			 * static final String TAG_P_ID = "id"; private static final String
			 * TAG_P_STOCK_LEVEL = "stockLevel"; private static final String
			 * TAG_P_MIN_STOCK_LEVEL = "minStockLevel"; private static final
			 * String TAG_P_COMMENT = "comment"; private static final String
			 * TAG_P_STORAGE_LOCATION_ID = "storageLocation_id"; private static
			 * final String TAG_P_CATEGORY_PATH = "categoryPath"; private static
			 * final String TAG_P_STORAGE_LOCATION_NAME = "storageLocationName";
			 * private static final String TAG_P_FOOTPRINT_ID = "footprint_id";
			 * private static final String TAG_P_FOOTPRINT_NAME =
			 * "footprintName"; private static final String TAG_P_PART_CATEGORY
			 * = "category"; private static final String
			 * TAG_P_PART_CATEGORY_NAME = "categoryName"; private static final
			 * String TAG_P_PART_UNIT = "partUnit"; private static final String
			 * TAG_P_PART_UNIT_NAME = "partUnitName"; private static final
			 * String TAG_P_PART_UNIT_SHORT_NAME = "partUnitShortName"; private
			 * static final String TAG_P_PART_UNIT_DEFAULT = "partUnitDefault";
			 * private static final String TAG_P_PART_CONDITION =
			 * "partCondition"; private static final String
			 * TAG_P_ATTACHMENT_COUNT = "attachmentCount"; private static final
			 * String TAG_P_ATTACHMENTS = "attachments";
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

	private static Set<Integer> parsePartAttachments(JSONObject jo, int count) {
		Set<Integer> attachIds = new TreeSet<Integer>();
		/*
		 * "response": { "totalCount": 1, "data": [ { "id": 1, "part_id": 2,
		 * "originalFilename": "IMG_20130315_142307.jpg", "mimetype":
		 * "image/jpeg", "extension": "jpeg", "size": 1870569, "description":
		 * "", "image": true } ] }
		 */
		try {
			if (jo.has(TAG_TOTAL_COUNT)) {
				if (jo.getInt(TAG_TOTAL_COUNT) != count) {
					Log.d("JSON Part Attach Parse", "Count != expected count");
					return null;
				}
				JSONArray ja = jo.getJSONArray(TAG_DATA);
				for (int i = 0; i < count; i++) {
					JSONObject o = ja.getJSONObject(i);
					// TODO this is where the PartAttachment would be built
					attachIds.add(o.getInt(TAG_ID));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return attachIds;
	}

	public static ArrayList<StorageLocation> parseStorageLocationList(
			JSONObject in) {
		if (log)
			Log.d("JSON SL Parser", "Parsing Storage Location");
		ArrayList<StorageLocation> sl = new ArrayList<StorageLocation>();
		try {
			if (log)
				Log.d("JSON SL Parser", "SL JSON: " + in.toString()); // TODO
																		// comment
																		// out

			JSONArray tmp = in.getJSONArray(TAG_DATA);
			if (log)
				Log.d("JSON SL Parser", "Data: " + tmp.toString());
			int total = 0;
			if (in.has(TAG_TOTAL_COUNT)) {
				total = in.getInt(TAG_TOTAL_COUNT);
			}

			if (log)
				Log.d("JSON SL Parser", "Total Storage Locations: " + total);
			JSONObject otmp;
			StorageLocation sloc;
			for (int i = 0; i < total; i++) {
				if (log)
					Log.d("JSON SL Parser", "Getting StorageLocation " + i);
				otmp = tmp.getJSONObject(i);
				sloc = new StorageLocation();

				if (otmp.has(TAG_P_NAME)) {
					sloc.setName(otmp.getString(TAG_P_NAME));
				}
				if (otmp.has(TAG_ID)) {
					sloc.setId(otmp.getInt(TAG_ID));
				}

				if (log)
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

	public static ArrayList<User> parseUsers(JSONObject in) {
		if (log)
			Log.d("JSON User Parser", "Parsing Users");
		ArrayList<User> u = new ArrayList<User>();
		try {
			// Log.d("JSON User Parser", "User JSON: " + in.toString()); // TODO
			// comment out

			JSONArray tmp = in.getJSONArray(TAG_DATA);

			if (log)
				Log.d("JSON User Parser", "Data: " + tmp.toString());
			int total = 0;
			if (in.has(TAG_TOTAL_COUNT)) {
				total = in.getInt(TAG_TOTAL_COUNT);
			}

			if (log)
				Log.d("JSON User Parser", "Total Users: " + total);
			JSONObject otmp;
			User utmp;
			for (int i = 0; i < total; i++) {
				if (log)
					Log.d("JSON User Parser", "Getting User " + i);
				otmp = tmp.getJSONObject(i);
				utmp = new User();

				if (otmp.has(TAG_U_NAME)) {
					utmp.setUsername(otmp.getString(TAG_U_NAME));
				}
				if (otmp.has(TAG_ID)) {
					utmp.setId(otmp.getInt(TAG_ID));
				}

				if (log)
					Log.d("JSON User Parser", "Adding user to array");
				u.add(utmp);
			}
			return u;

		} catch (JSONException e) {
			Log.d("JSON SL Parser", "There was a naughty Json Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.d("JSON SL Parser", "There was a naughty Null Exception");
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> parseStatus(JSONObject in) {
		if (log)
			Log.d("JSON Status Parser", "Parsing Status");
		Map<String, String> status = new HashMap<String, String>();

		try {
			if (log)
				Log.d("JSON Status Parser", "Status JSON: " + in.toString());

			JSONObject tmp = in.getJSONObject(TAG_DATA);

			if (log)
				Log.d("JSON Status Parser", "Data: " + tmp.toString());
			int total = 0;

			if (in.has(TAG_S_INACTIVE_CRON_COUNT)) {
				total = in.getInt(TAG_S_INACTIVE_CRON_COUNT);
				status.put(TAG_S_INACTIVE_CRON_COUNT, total + "");
			}

			if (log)
				Log.d("JSON Status Parser", "Total Inactive Cron Jobs: "
						+ total);
			JSONArray otmp = tmp.getJSONArray(TAG_S_INACTIVE_CRON_JOBS);

			for (int i = 0; i < total; i++) {
				if (log)
					Log.d("JSON Status Parser", "Getting Cron Job " + i);
				status.put(i + "", otmp.getString(i));
			}

			if (tmp.has(TAG_S_SCHEMA)) {
				status.put(TAG_S_SCHEMA, tmp.getString(TAG_S_SCHEMA));
			}
			if (tmp.has(TAG_TIMING)) {
				status.put(TAG_TIMING, tmp.getString(TAG_TIMING));
			}

			return status;

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
