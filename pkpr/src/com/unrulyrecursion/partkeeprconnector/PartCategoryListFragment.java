package com.unrulyrecursion.partkeeprconnector;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.PartCategory;
import com.unrulyrecursion.partkeeprconnector.utilities.GetRestTask;
import com.unrulyrecursion.partkeeprconnector.utilities.JsonParser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class PartCategoryListFragment extends ListFragment {

	private PartCategory pc;
	private String urlPart = "PartCategory/getAllCategories";
	private ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_part_category_list, container, false);
		return view;
	}

	@Override
	public void onResume() {
		if(MainActivity.session.isLoggedIn()){
//			JSONObject jobj = JsonParser.getJSONFromUrl(url,MainActivity.session.getSessId());
//			pc = JsonParser.parsePartCategories(jobj);
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
		super.onResume();
}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void refreshList() {
		Log.d("Part Category Fragment", "Refreshing List");
		AsyncTask<String, Integer, JSONObject> task = new pcRESTtask().execute(urlPart);

	}

	private class pcRESTtask extends GetRestTask {

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
			pc = JsonParser.parsePartCategories(result);
			if (pc != null) {
				adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, (String[]) pc.getAllNames().toArray());
				setListAdapter(adapter);
			}
		}
	}
}
