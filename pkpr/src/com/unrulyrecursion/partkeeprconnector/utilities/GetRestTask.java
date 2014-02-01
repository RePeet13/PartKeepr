package com.unrulyrecursion.partkeeprconnector.utilities;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unrulyrecursion.partkeeprconnector.MainActivity;

import android.os.AsyncTask;
import android.util.Log;

public class GetRestTask extends AsyncTask<String, Integer, JSONObject> {

		private String[] urlParts;
		@Override
		protected JSONObject doInBackground(String... strings) {
			int count = strings.length;
			urlParts = strings;
			/*
			for (int i = 0; i < count; i++) {
				try {
					// TODO implement multiple requests if wanted
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			
			String url = MainActivity.session.base_url + urlParts[0];
			Log.d("JSON Task", "Building URL: " + url);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			String[] status = new String[5];
			String sid = MainActivity.session.getSessId();

			if (sid.compareToIgnoreCase("none")!=0) {
				request.setHeader("Cookie", "PHPSESSID=" + sid);
			}
			try {
				Log.d("JSON Task","Getting response from server");
				JSONObject result = new JSONObject(EntityUtils.toString(client.execute(request).getEntity()));

				Log.d("JSON Task","Checking server response");
//				JSONObject o = result.getJSONObject(0);
				status[0] = result.getString(JsonParser.TAG_STATUS);
				status[1] = result.getString(JsonParser.TAG_SUCCESS);
				status[2] = result.getString(JsonParser.TAG_TIMING);
				Log.d("JSON Task", "Seems ok, timing: " + status[2]);
				JSONObject response = result.optJSONObject(JsonParser.TAG_RESPONSE);
				
				if (status[0].compareToIgnoreCase("ok")==0) { // response is expected "ok"
					Log.d("JSON Task", "response status ok");
				}
				
				if (status[1].compareToIgnoreCase("true")==0) {
					Log.d("JSON Task", "response success true");
				}
				
				return response;
				
			} catch (JSONException e) {
				Log.w("JSON Task", "JSONException thrown");
				Log.w("JSON Task", e.getLocalizedMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.w("JSON Task", "IOException thrown");
				Log.w("JSON Task", e.getLocalizedMessage());
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// TODO Auto-generated method stub
//	        showDialog("Downloaded " + result + " bytes"); // Downloaded result results
		}
}
