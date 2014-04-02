package com.unrulyrecursion.partkeeprconnector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.unrulyrecursion.partkeeprconnector.model.DBHelper;
import com.unrulyrecursion.partkeeprconnector.model.DBSchema;
import com.unrulyrecursion.partkeeprconnector.utilities.SavedServers;
import com.unrulyrecursion.partkeeprconnector.utilities.SessionManagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnItemSelectedListener {
	
	ArrayList<ArrayList<String>> spinnerStuff;

	private SessionManagement session;
	private static String url; // "Auth/login";
	private String spinSelect;
	public static final String FILE_NAME = "servers";
	private SharedPreferences sp;
	private ServerCursorAdapter sAdapter;
	private SavedServers ss;
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
		Cursor c = dbh.getSavedServers();
//		String[] from = new String[] {DBSchema.ServerCreds.COLUMN_NAME_BASE_URL, DBSchema.ServerCreds.COLUMN_NAME_USERNAME};
//		int[] to = new int[] {R.id.server1, R.id.server2};
//		
//		sAdapter = new SimpleCursorAdapter(getBaseContext(), R.id.serverList, c, from, to, 0);
		sAdapter = new ServerCursorAdapter(getBaseContext(), R.layout.entry_server_session, c, 0);
		ListView lv = (ListView) findViewById(R.id.serverList);
		lv.setAdapter(sAdapter);
//		ImageView iv = (ImageView) parent.findViewById(R.id.serverFresh);
//		iv.setImageDrawable(drawable)

		Resources res = getResources();
		
//		dbh = new DBHelper(getBaseContext());
//		SQLiteDatabase db = dbh.getReadableDatabase(); // TODO might need to be in async task
//		spinnerStuff = dbh.getSavedServers(db);
		
//		ListView lv = (ListView) findViewById(R.id.serverList);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// TODO initiate login to server
				// will need to handle non-successful (re-enter password, auto fill user)
			}
		
		});
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
		
		// TODO check for connectivity and break/notify if there isnt any
		
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
			AsyncTask<String, Void, Boolean> login = new LoginTask().execute(user, passHash, null);
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
				// TODO Save details for list // TODO delete saved servers in favor of db
				/*
				if (ss != null) {
					ss.accessServer(url, user);
				} else { // likely is not a shared pref file yet, make one
					ss = new SavedServers();
					ss.addServer(url, user, session.getSessId());
				}
				*/
				
				Log.d("Login Activity", "Calling DBHelper for successful login");
				dbh.accessedServer(getBaseContext(), url, user, session.getSessId());
				
				/* TODO Remove this (using db now
				ObjectOutputStream os;
				try {
					os = new ObjectOutputStream(getBaseContext().openFileOutput(FILE_NAME, 0));
					os.writeObject(ss);
					os.close();
					Log.d("Login Activity", "Wrote out saved servers file");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
				// Open new Activity
				tv.setText(R.string.success);
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra("USERNAME", user);
				i.putExtra("SESSION_ID", session.getSessId());
				i.putExtra("BASE_URL", session.base_url);
				startActivity(i);
			} else {
				// TODO failed login
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
	
	public void SavedServerClick(View v) {
		TextView tUrl = (TextView) v.findViewById(R.id.server1);
		TextView tName = (TextView) v.findViewById(R.id.server2);
		TextView tSid = (TextView) v.findViewById(R.id.serverSID);
		String url = tUrl.getText().toString();
		String name = tName.getText().toString();
		String sid = tSid.getText().toString();

		Log.d("Login Activity", "Saved server selected: " + name + " - " + sid + " - " + url);
		
		session = new SessionManagement(this, url);
		
		session.createLoginSession(name, sid);
		Map<String, String> m = session.getStatus();
		Log.d("Login Activity", "Session status returned");
		
		if (m.isEmpty()) {
			// TODO Toast a failure and don't go anywhere or login
		} else {
			Log.d("Login Activity", "Calling DBHelper for successful login");
			dbh.accessedServer(getBaseContext(), url, name, session.getSessId());

			// Inform Singleton
			PartKeeprConnectorApp pk = (PartKeeprConnectorApp) getApplication();
			pk.setmSession(session);
			pk.setBase_url(url);
			
			// Open new Activity
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.putExtra("USERNAME", name);
			i.putExtra("SESSION_ID", session.getSessId());
			i.putExtra("BASE_URL", session.base_url);
			startActivity(i);
		}
	}
	
	private class LoginTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... inputs) {
			return session.login(inputs[0], inputs[1], inputs[2]);
		}	
	}
	
	private class ServerCursorAdapter extends ResourceCursorAdapter {

		public ServerCursorAdapter(Context context, int layout, Cursor c,
				int flags) {
			super(context, layout, c, flags);
		}

		@Override
		public void bindView(View v, Context context, Cursor cursor) { // TODO log these out, add button listeners
			Log.d("ServerCursorAdapter", "Building new server entry");
			TextView url = (TextView) v.findViewById(R.id.server1);
	        url.setText(cursor.getString(cursor.getColumnIndex(DBSchema.ServerCreds.COLUMN_NAME_BASE_URL)));
	        Log.d("ServerCursorAdapter", "Setting url to: " + url.getText());
			TextView name = (TextView) v.findViewById(R.id.server2);
	        name.setText(cursor.getString(cursor.getColumnIndex(DBSchema.ServerCreds.COLUMN_NAME_USERNAME)));
	        Log.d("ServerCursorAdapter", "Setting name to: " + name.getText());
			TextView sid = (TextView) v.findViewById(R.id.serverSID);
	        sid.setText(cursor.getString(cursor.getColumnIndex(DBSchema.ServerCreds.COLUMN_NAME_SESSION_ID)));
	        Log.d("ServerCursorAdapter", "Setting sid to: " + sid.getText());
	        ImageView iv = (ImageView) v.findViewById(R.id.serverFresh);
	        // TODO add freshness check here 
	        iv.setImageResource(R.drawable.ball_green);
		}

	}
}
