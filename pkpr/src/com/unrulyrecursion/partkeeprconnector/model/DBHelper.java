package com.unrulyrecursion.partkeeprconnector.model;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "PartKeeprConnector.db";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* Getting Data Out */

	public ArrayList<ArrayList<String>> getSavedServers(SQLiteDatabase db) {
		String query = "SELECT * FROM " + DBSchema.ServerCreds.TABLE_NAME;
		Cursor c = db.rawQuery(query, new String[] {});
		ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
		ArrayList<String> url = new ArrayList<String>(); // urls
		ArrayList<String> u = new ArrayList<String>(); // usernames
		ArrayList<String> p = new ArrayList<String>(); // passwords
		try {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				url.add(c.getString(c
						.getColumnIndex(DBSchema.ServerCreds.COLUMN_NAME_BASE_URL)));
				u.add(c.getString(c
						.getColumnIndex(DBSchema.ServerCreds.COLUMN_NAME_USERNAME)));
				p.add(c.getString(c
						.getColumnIndex(DBSchema.ServerCreds.COLUMN_NAME_PASSWORD)));
			}
			arr.add(url);
			arr.add(u);
			arr.add(p);
		} finally {
			c.close();
		}

		return arr;
	}

	/* Database basic methods */

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBSchema.SQL_CREATE_SERVERS);
		db.execSQL(DBSchema.SQL_CREATE_PARTS);
		db.execSQL(DBSchema.SQL_CREATE_PART_CATEGORIES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
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
}
