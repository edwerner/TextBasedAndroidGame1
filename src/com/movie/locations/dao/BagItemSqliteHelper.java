package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BagItemSqliteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "bagitems.db";
	public static final String TABLE_BAG_ITEMS = "bagitems";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_GROUP_TITLE = "_grouptitle";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_DESCRIPTION = "_description";
	public static final String COLUMN_IMAGE_URL = "_imageurl";
	public static final String COLUMN_LEVEL = "_level";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_BAG_ITEMS + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_GROUP_TITLE + " text, " 
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_IMAGE_URL + " text, "
			+ COLUMN_LEVEL + " text);";

	public BagItemSqliteHelper(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS bagitems");
		onCreate(database);
	}
}