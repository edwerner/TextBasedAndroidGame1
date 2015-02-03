package com.movie.locations.dao;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.movie.locations.domain.NewsItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class NewsItemImpl extends SQLiteOpenHelper {

	private NewsItemSqliteHelper dbHelper;
	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "newsitems.db";
	private static final int DATABASE_VERSION = 1; 
	final static String TABLE_NAME = "newsitems"; // name of table
	private static final String COLUMN_ID = "_id";
	private final static String COLUMN_TITLE = "_title";
	private final static String COLUMN_TEXT = "_text";
	private final static String COLUMN_IMAGE_URL = "_imageurl";
	private final static String COLUMN_NEWS_TYPE = "_newstype";
	private final static String COLUMN_DATETIME = "_datetime";
	private String[] allColumns = { COLUMN_ID, COLUMN_TITLE, COLUMN_TEXT,
			COLUMN_IMAGE_URL, COLUMN_NEWS_TYPE, COLUMN_DATETIME };

	/**
	 * 
	 * @param context
	 */
	public NewsItemImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		dbHelper = new NewsItemSqliteHelper(context);
	}

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " text, " 
			+ COLUMN_TITLE + " text, "
			+ COLUMN_TEXT + " text, "
			+ COLUMN_IMAGE_URL + " text, "
			+ COLUMN_NEWS_TYPE + " text, "
			+ COLUMN_DATETIME + " text);";

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
	
	public void delete() {
		database.delete(TABLE_NAME, null, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public NewsItem createRecord(NewsItem newsItem) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, newsItem.getId());
		values.put(COLUMN_TITLE, newsItem.getTitle());
		values.put(COLUMN_TEXT, newsItem.getText());
		values.put(COLUMN_IMAGE_URL, newsItem.getImageUrl());
		values.put(COLUMN_NEWS_TYPE, newsItem.getNewsType());
		values.put(COLUMN_DATETIME, newsItem.getDateTime());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(NewsItemSqliteHelper.TABLE_NEWS_ITEMS,
				null, values);
		Cursor cursor = database.query(NewsItemSqliteHelper.TABLE_NEWS_ITEMS,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		NewsItem newsItemCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				newsItemCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		return newsItem;
	}

//	public void deleteRecordsByLevel(String level) {
//
//		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
//		String[] levelArray = { level };
//		database.delete(TABLE_NAME, COLUMN_LEVEL + "=?", levelArray);
//	}

	public NewsItem selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		NewsItem newsItem = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				newsItem = cursorToComment(cursor);
			}
			// cursor.moveToFirst();
			// user = cursorToComment(cursor);
		}
		return newsItem;
	}

	// public void updateRecord(String recordId, String answered)
	// throws SQLException {
	// ContentValues quizObject = new ContentValues();
	// quizObject.put(COLUMN_ANSWERED, answered);
	// database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	public ArrayList<NewsItem> selectRecords() {
		ArrayList<NewsItem> newsItemList = new ArrayList<NewsItem>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				NewsItem newsItem = cursorToComment(mCursor);
				newsItemList.add(newsItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return newsItemList; // iterate to get each value.
	}

	private NewsItem cursorToComment(Cursor cursor) {

		NewsItem newsItem = new NewsItem();
		newsItem.setId(cursor.getString(0));
		newsItem.setTitle(cursor.getString(1));
		newsItem.setText(cursor.getString(2));
		newsItem.setImageUrl(cursor.getString(3));
		newsItem.setNewsType(cursor.getString(4));
		newsItem.setDateTime(cursor.getString(5));
		return newsItem;
	}

}