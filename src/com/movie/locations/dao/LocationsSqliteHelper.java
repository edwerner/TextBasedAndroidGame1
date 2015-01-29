package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationsSqliteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "locations.db";
	public static final String TABLE_LOCATIONS = "locations";
	private static final String COLUMN_ID = "_id";
	private final static String COLUMN_SID = "_sid";
	private static final String COLUMN_LEVEL = "_level";
	private final static String COLUMN_POSITION = "_position";
	private final static String COLUMN_TITLE = "_title";
	private final static String COLUMN_RELEASE_YEAR = "_releaseyear";
	private final static String COLUMN_LOCATIONS = "_locations";
	private final static String COLUMN_FUN_FACTS = "_funfacts";
	private final static String COLUMN_STATIC_MAP_IMAGE_URL = "_staticmapimageurl";
	private final static String COLUMN_FUN_FACTS_IMAGE_URL = "_funFactsImageUrl";
	private final static String COLUMN_FUN_FACTS_TITLE = "_funFactsTitle";
	private static final int DATABASE_VERSION = 5;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LOCATIONS + "(" + COLUMN_ID
			+ " text, " + COLUMN_SID
			+ " text, " + COLUMN_LEVEL
			+ " text, " + COLUMN_POSITION
			+ " text, " + COLUMN_TITLE
			+ " text, " + COLUMN_RELEASE_YEAR
			+ " text, " + COLUMN_LOCATIONS
			+ " text, " + COLUMN_FUN_FACTS
			+ " text, " + COLUMN_STATIC_MAP_IMAGE_URL
			+ " text, " + COLUMN_FUN_FACTS_IMAGE_URL
			+ " text, " + COLUMN_FUN_FACTS_TITLE
			+ " text);";

	public LocationsSqliteHelper(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS locations");
		onCreate(database);
	}
}