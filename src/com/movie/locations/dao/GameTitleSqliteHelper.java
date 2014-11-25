package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GameTitleSqliteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "gametitles.db";
	public static final String TABLE_GAME_TITLES = "gametitles";	
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_TITLE = "_title";
	private static final String COLUMN_TYPE = "_type";
	private static final String COLUMN_LEVEL = "_level";
	private static final String COLUMN_PHASE = "_phase";	
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_GAME_TITLES + "(" 
			+ COLUMN_ID + " text, " 
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_TYPE + " text, "
			+ COLUMN_LEVEL + " text, "
			+ COLUMN_PHASE + " text);";
	
	public GameTitleSqliteHelper(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS gametitles");
		onCreate(database);
	}
}