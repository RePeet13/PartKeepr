package com.unrulyrecursion.partkeeprconnector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.unrulyrecursion.partkeeprconnector.model.DBHelper;
import com.unrulyrecursion.partkeeprconnector.utilities.SessionManagement;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Login extends Activity {
	
	ArrayList<ArrayList<String>> spinnerStuff;

	private SessionManagement session;
	private static String url; // "Auth/login";
	private DBHelper dbh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void onResume() {
		super.onResume();

		dbh = new DBHelper(getBaseContext());
		SQLiteDatabase db = dbh.getReadableDatabase(); // TODO might need to be in async task
		spinnerStuff = dbh.getSavedServers(db);
		
		Spinner spinner = (Spinner) findViewById(R.id.serverSpinner);
		String[] SArr = (String[])spinnerStuff.get(0).toArray();
		if (spinnerStuff.get(0).size() == 0) {
			SArr = new String[2];
			SArr[0] = "No Servers Saved"; // TODO Probably should be a resource
		}
		ArrayAdapter<CharSequence> aa = new ArrayAdapter<CharSequence>(getBaseContext(), android.R.layout.simple_spinner_item, SArr);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(aa);
		
		db.close();
	}

	public void setUrl(String url) { // TODO call from url chooser
		session.setBaseUrl(url);
	}

	public void initiateLogin(View view) {
		Log.d("Login", "Initiating Login");
		
		String user = ((EditText) findViewById(R.id.loginUser)).getText().toString();
		String pass = ((EditText) findViewById(R.id.loginPass)).getText().toString();
		String passHash = "null";
		
		String url = ((EditText) findViewById(R.id.serverText)).getText().toString();
		if (url==null || url.compareToIgnoreCase("")==0){
			// TODO set url to spinner value
		}
		session = new SessionManagement(this, url);

		try {
			Log.d("Initiating Login","Trying Pass Hash");
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
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
		if (passHash.compareTo("null") != 0) {
			Log.d("Initiating Login","Have pass hash, handing off to session");
			if (session.login(user, passHash)) {
				
			} else { // TODO failed login
				
			}
		} else {
			// TODO failed hash
		}
	}
}
