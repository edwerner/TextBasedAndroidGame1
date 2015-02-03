package com.movie.locations.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.GameTitle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GameTitleImpl extends SQLiteOpenHelper implements IDatabase {

	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "gametitles.db";	
	private static final int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "gametitles"; // name of table
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_TITLE = "_title";
	private static final String COLUMN_TYPE = "_type";
	private static final String COLUMN_LEVEL = "_level";
	private static final String COLUMN_PHASE = "_phase";
	private String[] allColumns = { COLUMN_ID, COLUMN_TITLE, COLUMN_TYPE, COLUMN_LEVEL, COLUMN_PHASE };

	/**
	 * 
	 * @param context
	 */
	public GameTitleImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " text, " 
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_TYPE + " text, "
			+ COLUMN_LEVEL + " text, "
			+ COLUMN_PHASE + " text);";

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
	
	@Override
	public void delete() {
		database.delete(TABLE_NAME, null, null);
	}

	@Override
	public void open() throws SQLException {
		database = this.getWritableDatabase();
	}

	@Override
	public void close() {
		database.close();
	}

	public GameTitle createRecord(GameTitle gameTitle) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, gameTitle.getId());
		values.put(COLUMN_TITLE, gameTitle.getTitle());
		values.put(COLUMN_TYPE, gameTitle.getType());
		values.put(COLUMN_LEVEL, gameTitle.getLevel());
		values.put(COLUMN_PHASE, gameTitle.getPhase());

		long insertId = database.insert(TABLE_NAME,
				null, values);
		Cursor cursor = database.query(TABLE_NAME,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		GameTitle gameTitleCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				gameTitleCursor = cursorToComment(cursor);
			}
			cursor.close();
		}
		return gameTitleCursor;
	}

	@Override
	public void deleteRecordByLevel(String level) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LEVEL
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_LEVEL + "=?", levelArray);
	}
	
	public GameTitle selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		GameTitle title = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				title = cursorToComment(cursor);
			}
		}
		return title;
	}

	public ArrayList<GameTitle> selectRecords() {
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				GameTitle title = cursorToComment(mCursor);
				gameTitleList.add(title);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return gameTitleList;
	}

	private GameTitle cursorToComment(Cursor cursor) {

		GameTitle gameTitle = new GameTitle();
		gameTitle.setId(cursor.getString(0));
		gameTitle.setTitle(cursor.getString(1));
		gameTitle.setType(cursor.getString(2));
		gameTitle.setLevel(cursor.getString(3));
		gameTitle.setPhase(cursor.getString(4));	

		return gameTitle;
	}


	public ArrayList<GameTitle> selectRecordsByType(String type) throws SQLException {
		String[] recordTypeArray = { type };
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_TYPE + "=?", recordTypeArray, null, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				GameTitle title = cursorToComment(cursor);
				gameTitleList.add(title);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return gameTitleList;
	}

	@Override
	public void deleteRecordById(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRecordByTitle(String recordTitle) {
		// TODO Auto-generated method stub
		
	}
}