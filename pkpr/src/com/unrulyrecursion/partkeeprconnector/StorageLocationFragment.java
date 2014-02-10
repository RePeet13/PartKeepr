package com.unrulyrecursion.partkeeprconnector;

import java.util.ArrayList;

import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.StorageLocation;
import com.unrulyrecursion.partkeeprconnector.utilities.GetRestTask;
import com.unrulyrecursion.partkeeprconnector.utilities.JsonParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StorageLocationFragment extends ListFragment {
	
	private static String urlPart = "StorageLocation?_dc=1387642414789&page=1&start=0&limit=-1"; // TODO figure this out for real
	private ArrayList<StorageLocation> storageLocations;
	private ArrayList<String> SLNames;
	private ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(MainActivity.session.isLoggedIn()){
			refreshList();
		}
	}

	public void refreshList()  {
		Log.d("Storage Location List","Refreshing List");
		AsyncTask<String, Integer, JSONObject> task = new slTask().execute("GET", urlPart);
	}
	
	private class slTask extends GetRestTask {

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			storageLocations = JsonParser.parseStorageLocationList(result);
			if (storageLocations != null) {
				SLNames = new ArrayList<String>();
				for (StorageLocation sl : storageLocations) {
					SLNames.add(sl.toString());
				}
				
				adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, SLNames);
				setListAdapter(adapter);
			}
		}
		
	}
}