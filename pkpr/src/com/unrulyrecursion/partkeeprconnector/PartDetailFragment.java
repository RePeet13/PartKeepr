package com.unrulyrecursion.partkeeprconnector;

import com.unrulyrecursion.partkeeprconnector.model.Part;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PartDetailFragment extends Fragment {
	
	private FragmentActivity mActivity;
	private Part cur;
	
	public static PartDetailFragment newInstance(Part in) {
		Log.d("Part Detail Fragment", "Statically creating fragment");
		PartDetailFragment p = new PartDetailFragment();
		
		Bundle args = new Bundle();
		args.putSerializable("part", in);
		p.setArguments(args);
		
		return p;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Part Detail Fragment", "Creating View");
		if (container == null) {
			return null;
		}
		
		View v = inflater.inflate(R.layout.fragment_part_detail_new, container, false);
		
		cur = (Part) getArguments().getSerializable("part");
		
		if (cur != null) {
			// Populate Attributes of Part
			TextView title = (TextView) v.findViewById(R.id.detailTitle);
			title.setText(cur.getName());
			Log.d("Part Detail Fragment", "Creating view for " + cur.getName());
			
			TextView desc = (TextView) v.findViewById(R.id.detailDescription);
			desc.setText(cur.getDescription());
			
			TextView category = (TextView) v.findViewById(R.id.detailCategory);
			category.setText(cur.getPcName());
			
			TextView storLoc = (TextView) v.findViewById(R.id.detailStorageLocation);
			storLoc.setText(cur.getStorageLocName());

			TextView comment = (TextView) v.findViewById(R.id.detailComment);
			comment.setText(cur.getComment());

			TextView createDate = (TextView) v.findViewById(R.id.detailCreateDate);
			createDate.setText(cur.getCreateDate());
			
			/* TODO implement if needed/wanted
			TextView stock = (TextView) v.findViewById(R.id.detailStockLevel);
			category.setText(cur.getStock() + "");
 			*/
			
			
			/*

			TextView category = (TextView) v.findViewById(R.id.detailCategory);
			category.setText(cur.getPcName());
			
			 */
		} else { Log.d("Part Detail Fragment", "No part passed in"); }
		
		return v;
	}

	public int getShownIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

}
