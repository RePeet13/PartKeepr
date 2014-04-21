package com.unrulyrecursion.partkeeprconnector;

import com.unrulyrecursion.partkeeprconnector.model.Part;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PartDetailActivity extends FragmentActivity {

	Part in;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part_detail);
		// From Fragments demo on Android Dev Site
		/* 
		if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }
		*/
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
        	Intent i = getIntent();
        	Bundle b = i.getExtras();
        	
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PartDetailFragment details = new PartDetailFragment();
            
        	if (b != null) {
	            details = PartDetailFragment.newInstance((Part)i.getExtras().getSerializable("part"));
        	}
        	
            ft.replace(R.id.partDetailFragment, details);
            Log.d("Part Detail Activity", "Adding Fragment");
            ft.commit();
        }
	}
	
}