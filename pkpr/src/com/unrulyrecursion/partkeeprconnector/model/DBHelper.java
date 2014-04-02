package com.unrulyrecursion.partkeeprconnector.model;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

	private Context mContext;

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "PartKeeprConnector.db";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* Getting Data Out */

	public Cursor getSavedServers() {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM " + DBSchema.ServerCreds.TABLE_NAME;
		Cursor c = db.rawQuery(query, new String[] {}); // empty string [] for
														// selection arguments
		return c;
	}

	/* Getting Data In */

	public void accessedServer(Context c, String url, String username,
			String sessionId) {
		mContext = c;
		AsyncTask<String, Integer, String> task = new AddServerTask().execute(
				url, username, sessionId);
	}

	/* Database basic methods */

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBSchema.SQL_CREATE_SERVERS);
		db.execSQL(DBSchema.SQL_CREATE_PARTS);
		db.execSQL(DBSchema.SQL_CREATE_PART_CATEGORIES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is to simply to discard the data and start over
		db.execSQL(DBSchema.SQL_DELETE_ALL_SERVERS);
		db.execSQL(DBSchema.SQL_DELETE_ALL_PARTS);
		db.execSQL(DBSchema.SQL_DELETE_ALL_PART_CATEGORIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	private class AddServerTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			if (mContext != null) {
				Toast toast = Toast.makeText(mContext, "Server Session Saved",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (arg0[0] != null && arg0.length != 0) {
				Log.d("AddServerTask", "Doing in background, saving - "
						+ arg0[0] + " arg count " + arg0.length);
				SQLiteDatabase db = getWritableDatabase();
				String[] ret = { DBSchema.ServerCreds._ID };
				String[] where = { arg0[0] }; // Search for server with url

				Cursor c = db.query(DBSchema.ServerCreds.TABLE_NAME, // The table to query
						ret, // The columns to return
						DBSchema.ServerCreds.COLUMN_NAME_BASE_URL + " LIKE ?", // The columns for the WHERE clause
						where, // The values for the WHERE clause
						null, // don't group the rows
						null, // don't filter by row groups
						null //DBSchema.ServerCreds._ID + " ASC" // The sort order
				);

				Date d = new Date();
				ContentValues cv = new ContentValues();
				cv.put(DBSchema.ServerCreds.COLUMN_NAME_LAST_ACCESS,
						d.toString());

				if (c != null && c.getCount() == 0) {
					Log.d("AddServerTask",
							"Cursor non null, but empty, inserting data");
					// TODO Insert
					cv.put(DBSchema.ServerCreds.COLUMN_NAME_BASE_URL, arg0[0]);
					cv.put(DBSchema.ServerCreds.COLUMN_NAME_USERNAME, arg0[1]);
					cv.put(DBSchema.ServerCreds.COLUMN_NAME_SESSION_ID, arg0[2]);

					Long id = db.insert(DBSchema.ServerCreds.TABLE_NAME, null,
							cv);
					if (id != null) {
						Log.d("AddServerTask", "Successfully added, id " + id);
					}
				} else {
					Log.d("AddServerTask",
							"Cursor non null, and not empty, updating data");
					// TODO Update
					if (c != null) {
						c.moveToFirst();
						String[] whereArgs = { c.getString(c
								.getColumnIndex(DBSchema.ServerCreds._ID)) };
						int count = db.update(DBSchema.ServerCreds.TABLE_NAME,
								cv, DBSchema.ServerCreds._ID + " LIKE ? ",
								whereArgs);
						Log.d("AddServerTask", "Updated " + count
								+ " rows. If not one, something's wrong..");
					}
				}
				return "true";
			}
			return "false";
		}
	}
}
