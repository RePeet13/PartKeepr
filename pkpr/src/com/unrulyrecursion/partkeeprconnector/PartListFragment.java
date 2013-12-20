package com.unrulyrecursion.partkeeprconnector;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;

import com.unrulyrecursion.partkeeprconnector.utilities.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class PartListFragment extends Fragment {

	// url to hit
	private static String url = MainActivity.base_url + "getParts"; // TODO figure out real url
	
	// JSON Node names
	private static final String Tag_Part_Name = "PartName"; // TODO fill this in
	
	JSONArray parts = null;
	ArrayList<HashMap<String, String>> partsList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		partsList = new ArrayList<HashMap<String,String>>();
		
		JsonParser jParser = new JsonParser();
		
		parts = jParser.getJSONFromUrl(url);
		
		super.onCreate(savedInstanceState);
	}
}
