package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieLocationsSqliteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "locations.db";
	public static final String TABLE_LOCATIONS = "locations";
	public static final String COLUMN_ID = "_id";
	public final static String COLUMN_SID = "_sid";
	public static final String COLUMN_LEVEL = "_level";
	public final static String COLUMN_POSITION = "_position";
	public final static String COLUMN_CREATED_AT = "_createdat";
	public final static String COLUMN_CREATED_META = "_createdmeta";
	public final static String COLUMN_UPDATED_AT = "_updateat";
	public final static String COLUMN_UPDATED_META = "_updatedmeta";
	public final static String COLUMN_META = "_meta";
	public final static String COLUMN_TITLE = "_title";
	public final static String COLUMN_RELEASE_YEAR = "_releaseyear";
	public final static String COLUMN_LOCATIONS = "_locations";
	public final static String COLUMN_FUN_FACTS = "_funfacts";
	public final static String COLUMN_PRODUCTION_COMPANY = "_productioncompany";
	public final static String COLUMN_DISTRIBUTOR = "_distributor";
	public final static String COLUMN_DIRECTOR = "_director";
	public final static String COLUMN_WRITER = "_writer";
	public final static String COLUMN_ACTOR_1 = "_actor1";
	public final static String COLUMN_ACTOR_2 = "_actor2";
	public final static String COLUMN_ACTOR_3 = "_actor3";
	public final static String COLUMN_LATITUDE = "_latitide";
	public final static String COLUMN_LONGITUDE = "_longitude";
	public final static String COLUMN_STATIC_MAP_IMAGE_URL = "_staticmapimageurl";

	private static final int DATABASE_VERSION = 5;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LOCATIONS + "(" + COLUMN_ID
			+ " text, " + COLUMN_SID
			+ " text, " + COLUMN_LEVEL
			+ " text, " + COLUMN_POSITION
			+ " text, " + COLUMN_CREATED_AT
			+ " text, " + COLUMN_CREATED_META
			+ " text, " + COLUMN_UPDATED_AT
			+ " text, " + COLUMN_UPDATED_META
			+ " text, " + COLUMN_META
			+ " text, " + COLUMN_TITLE
			+ " text, " + COLUMN_RELEASE_YEAR
			+ " text, " + COLUMN_LOCATIONS
			+ " text, " + COLUMN_FUN_FACTS
			+ " text, " + COLUMN_PRODUCTION_COMPANY
			+ " text, " + COLUMN_DISTRIBUTOR
			+ " text, " + COLUMN_DIRECTOR
			+ " text, " + COLUMN_WRITER
			+ " text, " + COLUMN_ACTOR_1
			+ " text, " + COLUMN_ACTOR_2
			+ " text, " + COLUMN_ACTOR_3
			+ " text, " + COLUMN_LATITUDE
			+ " text, " + COLUMN_LONGITUDE
			+ " text, " + COLUMN_STATIC_MAP_IMAGE_URL
			+ " text);";

	public MovieLocationsSqliteHelper(Context context) {
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

// package com.movie.locations.dao;
//
// import android.content.Context;
// import android.database.sqlite.SQLiteDatabase;
// import android.database.sqlite.SQLiteOpenHelper;
// import android.util.Log;
//
// public class MovieLocationsSqliteHelper extends SQLiteOpenHelper {
//
// public static final String TABLE_LOCATIONS = "locations";
// public static final String COLUMN_ID = "_id";
// public static final String COLUMN_LOCATION = "location";
//
// private static final String DATABASE_NAME = "locations.db";
// private static final int DATABASE_VERSION = 1;
//
// // Database creation sql statement
// private static final String DATABASE_CREATE = "create table "
// + TABLE_LOCATIONS + "(" + COLUMN_ID
// + " integer primary key autoincrement, " + COLUMN_LOCATION
// + " text not null);";
//
// public MovieLocationsSqliteHelper(Context context) {
// super(context, DATABASE_NAME, null, DATABASE_VERSION);
// }
//
// @Override
// public void onCreate(SQLiteDatabase database) {
// database.execSQL(DATABASE_CREATE);
// }
//
// @Override
// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Log.w(MovieLocationsSqliteHelper.class.getName(),
// "Upgrading database from version " + oldVersion + " to "
// + newVersion + ", which will destroy all old data");
// db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
// onCreate(db);
// }
//
// } 