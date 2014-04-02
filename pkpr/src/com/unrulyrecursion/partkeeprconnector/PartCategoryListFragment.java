package com.unrulyrecursion.partkeeprconnector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.PartCategory;
import com.unrulyrecursion.partkeeprconnector.utilities.GetRestTask;
import com.unrulyrecursion.partkeeprconnector.utilities.JsonParser;
import com.unrulyrecursion.partkeeprconnector.utilities.PartCategoryAdapter;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PartCategoryListFragment extends ListFragment {

	private String urlPart = "PartCategory/getAllCategories";
	private PartCategoryAdapter adapter;
	private List<String> crumbs;
	private View mView;
	private PartCategory pc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_part_category_list, container, false);
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(MainActivity.session.isLoggedIn()){
//			JSONObject jobj = JsonParser.getJSONFromUrl(url,MainActivity.session.getSessId());
//			pc = JsonParser.parsePartCategories(jobj);

			crumbs = new ArrayList<String>();
			refreshList();
		}
		ListView lv = getListView();
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				if (pos > 0) { // TODO figure out how to exclude "up a levels"
					Log.d("PC List Fragment", "Item " + pos + " has been long clicked");
					PartCategory pc = adapter.getSelected(pos);
		            Intent intent = new Intent();
		            Bundle b = new Bundle();
		            
		            b.putSerializable("pc", pc);
		            intent.putExtras(b);
		            intent.setClass(getActivity(), PartListActivity.class);
		            Log.d("Part Category Fragment", "Sending intent to PartListActivity");
		            startActivity(intent);
			        
		            return true;
				}
				return false;
			}
			
			
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() { // Goto the details of the event

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			PartCategory par = adapter.onItemClick(parent, view, position, id);

			crumbs.clear();
			if (par != null) {
				crumbs.addAll(par.getCrumbs());
			} else {
				crumbs.add("");
			}
			setCrumbs();
			
			adapter.notifyDataSetChanged();
			
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
		PartKeeprConnectorApp pa = (PartKeeprConnectorApp) (getActivity().getApplication());
		Log.d("PC RefreshList", pa.getBase_url());
		AsyncTask<String, Integer, JSONObject> task = new pcRESTtask().execute("POST", pa.getBase_url(), urlPart, pa.getmSession().getSessId());
		// TODO implement timeouts to allow a 'refresh manually' to happen
	}
	
	private void setCrumbs() {
		if (crumbs == null || mView == null) {return;}
		StringBuilder s = new StringBuilder();
		for (String st : crumbs) {
			s.append(" > ");
			s.append(st);
		}
		TextView tv = (TextView) mView.findViewById(R.id.crumbs);
		tv.setText(s.toString());
	}

	private class pcRESTtask extends GetRestTask {

		@Override
		protected JSONObject doInBackground(String... strings) {
//			Log.d("Part Category LF", "Entering doInBackground");
			JSONObject result = super.doInBackground(strings);
			crumbs = new ArrayList<String>();
			// TODO implement levels and being at the right one
			pc = JsonParser.parsePartCategories(result);
			
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			if (pc != null) {
				ArrayList<PartCategory> top = new ArrayList<PartCategory>();
				top.add(pc);
//				for (PartCategory p : current){
//					Log.d("List Adapter Input", p.getId() + " " + p.getName());
//				}
				crumbs.clear();
				crumbs.add("");
				setCrumbs();
				adapter = new PartCategoryAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, top);
				setListAdapter(adapter);
			}
		}
		
	}
}
