package com.unrulyrecursion.partkeeprconnector.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PartKeeprConnector.db";
    
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.SQL_CREATE_SERVERS);
        db.execSQL(DBSchema.SQL_CREATE_PARTS);
        db.execSQL(DBSchema.SQL_CREATE_PART_CATEGORIES);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DBSchema.SQL_DELETE_ALL_SERVERS);
        db.execSQL(DBSchema.SQL_DELETE_ALL_PARTS);
        db.execSQL(DBSchema.SQL_DELETE_ALL_PART_CATEGORIES);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
