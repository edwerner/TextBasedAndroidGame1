package com.movie.locations.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.CheckIn;
import com.movie.locations.domain.ClassLoaderHelper;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.NewsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.util.SessionIdentifierGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

public class NewsItemImpl {

	private NewsItemSqliteHelper dbHelper;

	private SQLiteDatabase database;

	// private String bagGroupTitle;
	// private String itemTitle;
	// private String description;
	// private String imageUrl;
	// private String level;
	// private static final String DATABASE_NAME = "newsitems.db";
	// public static final String TABLE_NEWS_ITEMS = "newsitems";

	public final static String TABLE_NAME = "newsitems"; // name of table
	public static final String COLUMN_ID = "_id";
	public final static String COLUMN_TITLE = "_title";
	public final static String COLUMN_TEXT = "_text";
	public final static String COLUMN_IMAGE_URL = "_imageurl";
	public final static String COLUMN_NEWS_TYPE = "_newstype";
	public final static String COLUMN_DATETIME = "_datetime";

	public static Map<String, NewsItem> BAG_ITEM_MAP = new HashMap<String, NewsItem>();
	private String[] allColumns = { COLUMN_ID, COLUMN_TITLE, COLUMN_TEXT,
			COLUMN_IMAGE_URL, COLUMN_NEWS_TYPE, COLUMN_DATETIME };

	/**
	 * 
	 * @param context
	 */
	public NewsItemImpl(Context context) {
		dbHelper = new NewsItemSqliteHelper(context);
		database = dbHelper.getWritableDatabase();
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
//		pc.writeString(id);
//		pc.writeString(title);
//		pc.writeString(text);
//		pc.writeString(imageUrl);
//		pc.writeString(newsType);
//		pc.writeString(dateTime);

		return newsItem;
	}

}