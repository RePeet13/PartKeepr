package com.unrulyrecursion.partkeeprconnector;

import com.unrulyrecursion.partkeeprconnector.model.PartCategory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class PartListActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent i = this.getIntent();
	    Bundle b = i.getExtras();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PartListFragment list = new PartListFragment();
        
	    if (b != null) {
		    list = PartListFragment.newInstance((PartCategory)b.getSerializable("pc"));
	    }
	    
        ft.replace(R.id.partListFragment, list);
        Log.d("Part Detail Activity", "Adding Fragment");
        ft.commit();
	    
	    setContentView(R.layout.activity_part_list);
	    
	    // TODO pass information to the fragment
	}
}
