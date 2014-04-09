package com.unrulyrecursion.partkeeprconnector;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.unrulyrecursion.partkeeprconnector.utilities.SessionManagement;
import com.unrulyrecursion.partkeeprconnector.utilities.TypefaceSpan;

import android.app.Application;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;

public class PartKeeprConnectorApp extends Application {
	
	private SessionManagement mSession;
	private String base_url;
	private Typeface tf;
	private SpannableString s;

	@Override
	public void onCreate() {
		super.onCreate();
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
		/*
		// src - http://www.androidhive.info/2012/02/android-using-external-fonts/
        // Load Font Face
		String fontPath = "fonts/Ubuntu/Ubuntu_BoldItalic.ttf";
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        */
		
		// From Stack Overflow
		// src - http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
		s = new SpannableString("PartKeepr");
		s.setSpan(new TypefaceSpan(getBaseContext(), "Ubuntu/Ubuntu_BoldItalic.ttf"), 0, s.length(),
		        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	public String getBase_url() {
		return base_url;
	}

	public void setBase_url(String base_url) {
		this.base_url = base_url;
	}

	public SessionManagement getmSession() {
		return mSession;
	}

	public void setmSession(SessionManagement mSession) {
		this.mSession = mSession;
	}
	
	public SpannableString getActionBarTitle() {
		return s;
	}
}
