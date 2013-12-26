package com.unrulyrecursion.partkeeprconnector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.utilities.JsonParser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

	private static String url = MainActivity.base_url+"Auth/login";
	
	private EditText mUser,mPass;
			
	// JSON Node names
	private static final String TAG_SESSION_ID = "sessionid";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_ADMIN = "admin";
	// could expand here to include preferences

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		
		// TODO Populate history list of servers (and usernames/pass)
		mUser = (EditText) rootView.findViewById(R.id.loginUser);
		mPass = (EditText) rootView.findViewById(R.id.loginPass);
		return rootView;
	}
	
	public void setUrl(String url) { // TODO call from url chooser
		MainActivity.setBaseUrl(url);
	}
	
	public void logIn() {
		JsonParser jParser = new JsonParser();
		
		// TODO error handle for failed logins here
		JSONObject response = jParser.getJSONFromUrl(url);
		
	}
	
	public void initiateLogin() {
		String user = mUser.getText().toString();
		String pass = mUser.getText().toString();
		String passHash="null";
		
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(pass.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        passHash = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (passHash.compareTo("null")!=0) {
			MainActivity.session.login(user,passHash);
			// TODO redirect to part categories fragment
		} else {
			// TODO start dialog about failed login here or so
		}
	}
}
