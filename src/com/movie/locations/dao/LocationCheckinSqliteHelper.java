package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationCheckinSqliteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "checkins.db";
	public static final String TABLE_CHECKINS = "checkins";
	public static final String COLUMN_ID = "_id";
	public final static String COLUMN_DATETIME = "_datetime";
	public final static String COLUMN_LOCATION = "_location";
	public final static String COLUMN_USER_ID = "_userid";
	public final static String COLUMN_FILM_TITLE = "_fimtitle";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CHECKINS + "(" 
			+ COLUMN_ID + " text, " 
			+ COLUMN_DATETIME + " text, " 
			+ COLUMN_LOCATION + " text, "
			+ COLUMN_USER_ID + " text, "
			+ COLUMN_FILM_TITLE + " text);";

	public LocationCheckinSqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Method is called during an upgrade of the database,
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w("Database", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS checkins");
		onCreate(database);
	}
}