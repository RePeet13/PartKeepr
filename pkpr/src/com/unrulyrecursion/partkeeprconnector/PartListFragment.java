package com.unrulyrecursion.partkeeprconnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.Part;
import com.unrulyrecursion.partkeeprconnector.utilities.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

public class PartListFragment extends ListFragment {

	// url suffix
	//private static String urlPart = "getParts"; // TODO figure out real url
	private static String urlPart = "Part?_dc=1387642414788&page=1&start=0&limit=50&group=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%5D&sort=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22name%22%2C%22direction%22%3A%22ASC%22%7D%5D";
	
	// JSON Node names
	private static final String Tag_Part_Name = "PartName"; // TODO fill this in
	
	private JSONObject parts = null;
	private ArrayList<Part> partsList;
	private ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(MainActivity.session.isLoggedIn()){
			refreshList();
		}
	}

	public void refreshList()  {
		Log.d("Part List","Refreshing List");
		AsyncTask<String, Integer, JSONObject> task = new partTask().execute(urlPart);
	}
	
	private class partTask extends GetRestTask {

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			partsList = JsonParser.parsePartsList(result);
			if (partsList != null) {
				String[] tmp = null;
				tmp = new String[partsList.size() + 20];
				int i=0;
				for(Part p : partsList) {
					tmp[i] = p.toString();
					i++;
				}
				
				if(tmp[0] != null) {
					adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tmp);
					setListAdapter(adapter);
				}
			}
		}
		
	}
}
