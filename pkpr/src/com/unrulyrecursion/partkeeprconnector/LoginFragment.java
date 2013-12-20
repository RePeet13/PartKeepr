package com.unrulyrecursion.partkeeprconnector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		
		// TODO Populate history list of servers (and usernames/pass)
		
		return rootView;
	}
	
	public void setUrl(String url) { // TODO call from url chooser
		MainActivity.setBaseUrl(url);
	}
}
