package com.unrulyrecursion.partkeeprconnector.model;

import android.provider.BaseColumns;

public final class DBSchema {
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
	public static final String SQL_ADD_DEMO = "INSERT OR REPLACE INTO " + 
			ServerCreds.TABLE_NAME + "(" + ServerCreds.COLUMN_NAME_BASE_URL + COMMA_SEP + 
			ServerCreds.COLUMN_NAME_USERNAME + COMMA_SEP + 
			ServerCreds.COLUMN_NAME_PASSWORD + COMMA_SEP + 
			ServerCreds.COLUMN_NAME_SESSION_ID + 
			") VALUES (\"http://demo.partkeepr.org/rest.php/\", \"admin\", " + 
			"\"21232f297a57a5a743894a0e4a801fc3\", \"1llje0297nfucah86f41me9ea2\")";
    
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DBSchema() {}

    
    /* Server Table */
    
    public static abstract class ServerCreds implements BaseColumns {
        public static final String TABLE_NAME = "server_creds";
        public static final String COLUMN_NAME_BASE_URL = "base_url";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_LAST_ACCESS = "last_access";
        public static final String COLUMN_NAME_SESSION_ID = "session_id";
    }
    
   static final String SQL_CREATE_SERVERS =
        "CREATE TABLE IF NOT EXISTS " + ServerCreds.TABLE_NAME + " (" +
	        ServerCreds._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
	        ServerCreds.COLUMN_NAME_BASE_URL + TEXT_TYPE + COMMA_SEP +
	        ServerCreds.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
	        ServerCreds.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
	        ServerCreds.COLUMN_NAME_LAST_ACCESS + TEXT_TYPE + COMMA_SEP +
	        ServerCreds.COLUMN_NAME_SESSION_ID + TEXT_TYPE + 
        " )";

    static final String SQL_DELETE_ALL_SERVERS =
        "DROP TABLE IF EXISTS " + ServerCreds.TABLE_NAME;
    
    
    /* Part Table */
    
    public static abstract class Parts implements BaseColumns {
        public static final String TABLE_NAME = "parts"; // TODO fix this set of columns
        public static final String COLUMN_NAME_BASE_URL = "base_url";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }
    
    static final String SQL_CREATE_PARTS =
		"CREATE TABLE IF NOT EXISTS " + Parts.TABLE_NAME + " (" +
				Parts._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
				Parts.COLUMN_NAME_BASE_URL + TEXT_TYPE + 
			" )";
    
    static final String SQL_DELETE_ALL_PARTS =
            "DROP TABLE IF EXISTS " + Parts.TABLE_NAME;
    
    
    /* Part Categories Table */
    
    public static abstract class PartCategories implements BaseColumns {
        public static final String TABLE_NAME = "part_categories"; // TODO fix this set of columns
        public static final String COLUMN_NAME_BASE_URL = "base_url";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }
    
    static final String SQL_CREATE_PART_CATEGORIES =
		"CREATE TABLE IF NOT EXISTS " + Parts.TABLE_NAME + " (" +
				PartCategories._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
				PartCategories.COLUMN_NAME_BASE_URL + TEXT_TYPE +
			" )";
    
    static final String SQL_DELETE_ALL_PART_CATEGORIES =
            "DROP TABLE IF EXISTS " + PartCategories.TABLE_NAME;
    
    
}