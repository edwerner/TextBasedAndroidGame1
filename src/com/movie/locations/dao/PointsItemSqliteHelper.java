package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PointsItemSqliteHelper extends SQLiteOpenHelper {
	
	// THE XP STORAGE MECHANISM ON THE MOBILE CLIENT
	private static final String DATABASE_NAME = "pointsitems.db";
	public static final String TABLE_POINTS_ITEMS = "pointsitems";
	public static final String COLUMN_USER_ID = "_userid";
	public static final String COLUMN_POINTS_USER_ID = "_pointsuserid";
	public static final String COLUMN_POINTS = "_points";
	public static final String COLUMN_BONUS_POINTS = "_bonuspoints";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_POINTS_ITEMS + "(" 
			+ COLUMN_USER_ID + " text, "
			+ COLUMN_POINTS_USER_ID + " text, " 
			+ COLUMN_POINTS + " text, "
			+ COLUMN_BONUS_POINTS + " text);";

	public PointsItemSqliteHelper(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS pointsitems");
		onCreate(database);
	}
}
