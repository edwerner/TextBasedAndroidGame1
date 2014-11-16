package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsItemSqliteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "newsitems.db";
	public static final String TABLE_NEWS_ITEMS = "newsitems";
	public static final String COLUMN_ID = "_id";
	public final static String COLUMN_TITLE = "_title";
	public final static String COLUMN_TEXT = "_text";
	public final static String COLUMN_IMAGE_URL = "_imageurl";
	public final static String COLUMN_NEWS_TYPE = "_newstype";
	public final static String COLUMN_DATETIME = "_datetime";

	
	// News item updates should include
	//
	// 
	// - Display Name
	// - Avatar image
	// - Achievement title
	// - Date/time
	// - Movie title
	//
	// ------------------------------------------------------------------
	//
	// TODO:
	//
	// - Design avatar selection and menu update features
	// - Design User domain to store selected avatar

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NEWS_ITEMS + "(" 
			+ COLUMN_ID + " text, " 
			+ COLUMN_TITLE + " text, "
			+ COLUMN_TEXT + " text, "
			+ COLUMN_IMAGE_URL + " text, "
			+ COLUMN_NEWS_TYPE + " text, "
			+ COLUMN_DATETIME + " text);";

	public NewsItemSqliteHelper(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS newsitems");
		onCreate(database);
	}
}