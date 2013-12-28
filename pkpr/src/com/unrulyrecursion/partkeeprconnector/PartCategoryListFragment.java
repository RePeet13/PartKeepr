package com.unrulyrecursion.partkeeprconnector;

import java.util.ArrayList;

import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.PartCategory;
import com.unrulyrecursion.partkeeprconnector.utilities.JsonParser;

import android.content.Intent;
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
	private ArrayList<String> names;
	private String url = MainActivity.base_url + "PartCategory/getAllCategories";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(MainActivity.session.isLoggedIn()){
			JSONObject jobj = JsonParser.getJSONFromUrl(url,MainActivity.session.getSessId());
			pc = JsonParser.parsePartCategories(jobj);
		}
		super.onCreate(savedInstanceState);
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_part_category_list, container, false);
		if (MainActivity.session.isLoggedIn()){
			names = pc.getAllNames();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, (String[]) names.toArray());
			/* FROM SG
			ListAdapter adapter = new SimpleAdapter(getActivity(), eventList,
					R.layout.list_item,
					new String[] { TAG_TYPE, TAG_TITLE, TAG_DATE, TAG_SUMMARY }, new int[] {
							R.id.eventType, R.id.eventName, R.id.eventDate, R.id.eventDescrip
							});//Parsed JSON into an actual list
			*/
			setListAdapter(adapter);
		}
		return view;
	}

	@Override
	public void onResume() {
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {					//Goto the details of the event

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



	
}
