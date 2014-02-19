package com.unrulyrecursion.partkeeprconnector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PartListActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent i = this.getIntent();
	    int pcIndex;
	    if (i != null && i.hasExtra("PartCategoryId")) {
	    	pcIndex = i.getIntExtra("PartCategoryId", -1);
	    	i.hasExtra("PartCategoryId");
	    }
	    setContentView(R.layout.activity_part_list);
	    
	    // TODO pass information to the fragment
	}
}
