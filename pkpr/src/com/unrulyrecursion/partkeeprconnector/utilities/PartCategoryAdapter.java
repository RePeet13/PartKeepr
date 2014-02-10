package com.unrulyrecursion.partkeeprconnector.utilities;

import java.util.ArrayList;
import java.util.List;

import com.unrulyrecursion.partkeeprconnector.model.PartCategory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PartCategoryAdapter extends ArrayAdapter<PartCategory> {
	
	private List<PartCategory> current;
	private PartCategory pc, up;
	private int topLevelPId;

	public PartCategoryAdapter(Context context, int resource,
			int textViewResourceId, List<PartCategory> objects) {
		super(context, resource, textViewResourceId, objects);
		up = new PartCategory();
		up.setName("Up a level");
		up.setDescription("...");
		
		current = new ArrayList<PartCategory>();
		current.add(up);
		current.addAll(objects);
		
		
		this.pc = objects.get(0);
		topLevelPId = pc.getId();
		
		if (current.size() > 2) {
			Log.d("PC Adapter", "Something's weird, there's more than one parent PC. Size: " + objects.size());
			for (PartCategory c : objects){
				Log.d("PC Adapter", c.toString());
			}
		}
	}
	
	public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
		Log.d("Part Category LF", "You clicked position " + position);
		for(int i = 0; i < current.size(); i++){
			Log.d("Part Category LF", current.get(i).toString() + " pos: " + i);
		}

		List<PartCategory> tmp = new ArrayList<PartCategory>();
		
		if (position == 0) {
			if (pc.getId() == current.get(position + 1).getId() || pc.getId() == current.get(position + 1).getParentId()) {
				tmp.add(pc);
			} else {
				tmp = pc.getChildrenOf(topLevelPId); // TODO could just have each child have a reference to parent...
			}
			if (tmp == null) {
				return;
			}
		} else if (!current.get(position).getLeaf()) {
			topLevelPId = current.get(position).getParentId();
			if (current.get(position).getId() == pc.getId()) {
				topLevelPId = pc.getId();
			}
			tmp.addAll(current.get(position).getChildren());
		} else {
			return;
			// TODO probably go to PartList of this category
		}
		current.clear();
		current.add(up);
		current.addAll(tmp);
				
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

	@Override
	public int getCount() {
		if (current != null) {
			return current.size(); 
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(android.R.layout.simple_list_item_2, null);
		}
		
		Log.d("PC Adapter", "Makin a view! pos: " + position + " of " + current.size()); // TODO delete
	    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
	    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
	    
	    text1.setText(current.get(position).getName());
	    text2.setText(current.get(position).getDescription());
		Log.d("PC Adapter", "View gotten, pos: " + position);
		    
		    /*
		    if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
		        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		        p.setMargins(5+20*flat.get(position).getDepth(), 0, 0, 0);
		        text1.requestLayout();
		    }
		    */
//		}
	    return view;
	}
}