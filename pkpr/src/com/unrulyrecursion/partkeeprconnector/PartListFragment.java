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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PartListFragment extends ListFragment {

	// url suffix
	//private static String urlPart = "getParts"; // TODO figure out real url
	private static String urlPart = "Part?_dc=1387642414788&page=1&start=0&limit=50&group=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%5D&sort=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22name%22%2C%22direction%22%3A%22ASC%22%7D%5D";
	
	// JSON Node names
	private static final String Tag_Part_Name = "PartName"; // TODO fill this in
	
	private JSONObject parts = null;
	private ArrayList<Part> partsList;
	private ArrayList<String> partNames;
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

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() { // Goto the details of the event

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*
				
				Log.d("Position of clicked item", Integer.toString(position));
				
				// TODO fix this
				Intent in = new Intent(getActivity().getApplicationContext(), DetailEvent.class);
				
				//pass the hashmap entry for an event to the detailevent class
				in.putExtra("hashmap", pc.getAllNames().get(position));
				Log.d("EVENT LIST PASSED: ", String.valueOf(names.get(position)));
	
				startActivity(in);
				
				*/
			}
		});
	}

	public void refreshList()  {
		Log.d("Part List","Refreshing List");
		AsyncTask<String, Integer, JSONObject> task = new partTask().execute("GET", urlPart);
	}
	
	private class partTask extends GetRestTask {

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			partsList = JsonParser.parsePartsList(result);
			if (partsList != null) {
				partNames = new ArrayList<String>();
				for (Part p : partsList) {
					partNames.add(p.toString());
				}
				
				adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, partNames);
				setListAdapter(adapter);
			}
		}
		
	}
}
