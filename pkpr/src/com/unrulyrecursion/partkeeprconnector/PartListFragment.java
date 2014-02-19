package com.unrulyrecursion.partkeeprconnector;

import java.util.ArrayList;
import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.model.Part;
import com.unrulyrecursion.partkeeprconnector.model.PartCategory;
import com.unrulyrecursion.partkeeprconnector.utilities.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PartListFragment extends ListFragment {

	// From fragments demo on Android Dev site
	boolean mDualPane;
	int mCurCheckPosition = 0;

	// url suffix
	// private static String urlPart = "getParts"; // TODO figure out real url
	private static String urlPart = "Part?_dc=1387642414788&page=1&start=0&limit=50&group=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%5D&sort=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22name%22%2C%22direction%22%3A%22ASC%22%7D%5D";

	private JSONObject parts = null;
	private ArrayList<Part> partsList;
	private ArrayList<String> partNames;
	private ArrayAdapter<String> adapter;
	private PartCategory pc;

	public static PartListFragment newInstance(PartCategory in) {
		Log.d("Part List Fragment", "Statically creating a part list fragment");
		PartListFragment out = new PartListFragment();
		
		Bundle args = new Bundle();
		args.putSerializable("pc", in);
		out.setArguments(args);
		
		return out;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		
		// Adapted from Fragments Demo on Android Dev site

		// Check to see if we have a frame in which to embed the details
		// fragment directly in the containing UI.
		View detailsFrame = getActivity().findViewById(R.id.partDetails);
		mDualPane = detailsFrame != null
				&& detailsFrame.getVisibility() == View.VISIBLE;

		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}

		if (mDualPane) {
			// In dual-pane mode, the list view highlights the selected item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			// Make sure our UI is in the correct state.
			showDetails(mCurCheckPosition);
		}
	}

	private String buildUrl() {
		// Limitation is that query is limited to 50 parts (but pagination is easy to figure out how to do)
		return "Part?_dc=1392816881375&category="+pc.getId()+"&page=1&start=0&limit=50&group=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%5D&sort=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22name%22%2C%22direction%22%3A%22ASC%22%7D%5D";
		
		/* Part?_dc=1392816881375&category=10&page=1&start=0&limit=50&group=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%5D&sort=%5B%7B%22property%22%3A%22categoryPath%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22name%22%2C%22direction%22%3A%22ASC%22%7D%5D
		 * This is a webapp generated url with the following properties
		 * (the url is form encoded)
		 * Query Params: 
		 * _dc:1392816881375 
		 * category:10 
		 * page:1 
		 * start:0 
		 * limit:50 
		 * group:[{"property":"categoryPath","direction":"ASC"}] 
		 * sort:[{"property":"categoryPath","direction":"ASC"},{"property":"name","direction":"ASC"}]
		 * 
		 * TODO figure out how this is generated and how I can handle generating it without the ugliness above
		 * */
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetails(position);
	}

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            PartDetailFragment details = (PartDetailFragment)
                    getFragmentManager().findFragmentById(R.id.partDetails);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = PartDetailFragment.newInstance(partsList.get(mCurCheckPosition));

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (index == 0) {
                    ft.replace(R.id.partDetails, details);
                }/* else { // Carried in from Fragment Demo, don't think I need it
                    ft.replace(R.id.a_item, details);
                } */
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
        	Bundle b = new Bundle();
        	b.putSerializable("part", partsList.get(index));
            Intent intent = new Intent();
            //intent.putExtra("index", index); // TODO not sure if I want or need this..
            intent.putExtras(b);
            intent.setClass(getActivity(), PartDetailActivity.class);
            
            startActivity(intent);
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		pc = (PartCategory) getArguments().getSerializable("pc");
		
		return inflater.inflate(R.layout.fragment_part_list, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (MainActivity.session.isLoggedIn()) {
			refreshList();
		}
	}

	public void refreshList() {
		Log.d("Part List", "Refreshing List");
		urlPart = buildUrl();
		AsyncTask<String, Integer, JSONObject> task = new partTask().execute(
				"GET", urlPart);
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

				adapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_1, partNames);
				setListAdapter(adapter);
			}
		}

	}
}
