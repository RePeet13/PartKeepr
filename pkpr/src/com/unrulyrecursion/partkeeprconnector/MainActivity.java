package com.unrulyrecursion.partkeeprconnector;

import java.util.Locale;
import com.unrulyrecursion.partkeeprconnector.utilities.*;
import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	public static SessionManagement session;
	public static String base_url = "http://sterlingthoughts.homelinux.com:7331/PartKeepr/frontend/rest.php/"; // TODO don't leave this url here

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			session = new SessionManagement(this, extras.getString("BASE_URL"));
			session.createLoginSession(extras.getString("USERNAME"), extras.getString("SESSION_ID"));
		}
		if (session == null) {
			session = new SessionManagement(this,"something");
		}
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Next two lines are required to successfully perform network
		// operations
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		// From Stack Overflow
		// src - http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title

		// Update the action bar title with the TypefaceSpan instance
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(((PartKeeprConnectorApp)getApplication()).getActionBarTitle());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static void setBaseUrl(String url) {
		MainActivity.base_url = url;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			Fragment fragment = null;
			Bundle args = new Bundle();
//			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			switch (position) {
			case 0:
				fragment = new PartCategoryListFragment();
				break;
			case 1:
				fragment = new StorageLocationFragment();
				break;
			}
			if (!session.isLoggedIn()) {
				return new NoServerFragment();
			}
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			Resources res = getResources();
			String[] pageTitles = res.getStringArray(R.array.pageTitles);
			switch (position) {
			case 0:
				return pageTitles[0].toUpperCase();
			case 1:
				return pageTitles[1].toUpperCase();
			case 2:
			}
			return null;
		}
	}
}
