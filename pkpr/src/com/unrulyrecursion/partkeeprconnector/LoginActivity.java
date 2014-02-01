package com.unrulyrecursion.partkeeprconnector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.unrulyrecursion.partkeeprconnector.model.DBHelper;
import com.unrulyrecursion.partkeeprconnector.utilities.SessionManagement;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnItemSelectedListener {
	
	ArrayList<ArrayList<String>> spinnerStuff;

	private SessionManagement session;
	private static String url; // "Auth/login";
	private String spinSelect;
//	private DBHelper dbh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void onResume() {
		super.onResume();

//		dbh = new DBHelper(getBaseContext());
//		SQLiteDatabase db = dbh.getReadableDatabase(); // TODO might need to be in async task
//		spinnerStuff = dbh.getSavedServers(db);
		
		Resources res = getResources();
		String[] serverList = res.getStringArray(R.array.serverList);
		
		Spinner spinner = (Spinner) findViewById(R.id.serverSpinner);
		
		spinner.setOnItemSelectedListener(this);
//		String[] SArr = (String[])spinnerStuff.get(0).toArray();
//		if (spinnerStuff.get(0).size() == 0) {
//			SArr = new String[2];
//			SArr[0] = "No Servers Saved"; // TODO Probably should be a resource
//		}
		
		ArrayAdapter<CharSequence> aa = new ArrayAdapter<CharSequence>(getBaseContext(), android.R.layout.simple_spinner_item, serverList);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(aa);
		
//		db.close();
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
			url = spinSelect;
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
			Log.d("Initiating Login", "user: " + user);
			Log.d("Initiating Login", "pass: " + passHash);
			Log.d("Initiating Login", "url: " + url);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (passHash.compareTo("null") != 0) {
			Log.d("Initiating Login","Have pass hash, handing off to session");
			TextView tv = (TextView) findViewById(R.id.loginResponse);
			tv.setText(R.string.connecting);
			AsyncTask<String, Void, Boolean> login = new LoginTask().execute(user, passHash);
			Boolean response = false;
			try {
				response = login.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				tv.setText(R.string.failed);
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				tv.setText(R.string.failed);
				e.printStackTrace();
			}
			if (response) {
				tv.setText(R.string.success);
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra("USERNAME", user);
				i.putExtra("SESSION_ID", session.getSessId());
				i.putExtra("BASE_URL", session.base_url);
				startActivity(i);
			} else { // TODO failed login
				tv.setText(R.string.failed);
			}
		} else {
			// TODO failed hash
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		spinSelect = parent.getItemAtPosition(pos).toString();
		Log.d("Spinner Selected", "selected: " + spinSelect);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		spinSelect = null;		
		Log.d("Spinner Unselected", "selected: " + spinSelect);
	}
	
	private class LoginTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... inputs) {
			return session.login(inputs[0], inputs[1]);
		}
		
	}
}
