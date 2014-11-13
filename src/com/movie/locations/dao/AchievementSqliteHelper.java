package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AchievementSqliteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "achievements.db";
	public final static String TABLE_ACHIEVEMENTS = "achievements"; // name of table
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_DESCRIPTION = "_description";
	public static final String COLUMN_USER_ID = "_userid";
	public static final String COLUMN_DATETIME = "_datetime";
	public static final String COLUMN_IMAGE_URL = "_imageurl";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_ACHIEVEMENTS + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_USER_ID + " text, "
			+ COLUMN_DATETIME + " text, "
			+ COLUMN_IMAGE_URL + " text);";

	public AchievementSqliteHelper(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS achievements");
		onCreate(database);
	}
}