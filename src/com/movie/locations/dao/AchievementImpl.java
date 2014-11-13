package com.movie.locations.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.CheckIn;
import com.movie.locations.domain.ClassLoaderHelper;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.util.SessionIdentifierGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

public class AchievementImpl {

	private AchievementSqliteHelper dbHelper;

	private SQLiteDatabase database;

	// String achievementId;
	// String title;
	// String description;
	// String userId;
	// String dateTime;
	// String imageUrl;

	public final static String TABLE_NAME = "achievements"; // name of table
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_DESCRIPTION = "_description";
	public static final String COLUMN_USER_ID = "_userid";
	public static final String COLUMN_DATETIME = "_datetime";
	public static final String COLUMN_IMAGE_URL = "_imageurl";

	public static Map<String, BagItem> BAG_ITEM_MAP = new HashMap<String, BagItem>();
	private String[] allColumns = { COLUMN_ID, COLUMN_TITLE,
			COLUMN_DESCRIPTION, COLUMN_USER_ID, COLUMN_DATETIME,
			COLUMN_IMAGE_URL };

	/**
	 * 
	 * @param context
	 */
	public AchievementImpl(Context context) {
		dbHelper = new AchievementSqliteHelper(context);
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

	public Achievement createRecord(Achievement achievement) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, achievement.getAchievementId());
		values.put(COLUMN_TITLE, achievement.getTitle());
		values.put(COLUMN_DESCRIPTION, achievement.getDescription());
		values.put(COLUMN_USER_ID, achievement.getUserId());
		values.put(COLUMN_DATETIME, achievement.getImageUrl());
		values.put(COLUMN_IMAGE_URL, achievement.getImageUrl());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(AchievementSqliteHelper.TABLE_ACHIEVEMENTS,
				null, values);
		Cursor cursor = database.query(AchievementSqliteHelper.TABLE_ACHIEVEMENTS,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		Achievement achievementCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				achievementCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		return achievementCursor;
	}

	public void deleteRecordById(String level) {

		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_ID + "=?", levelArray);
	}

	public Achievement selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		Achievement achievement = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				achievement = cursorToComment(cursor);
			}
			// cursor.moveToFirst();
			// user = cursorToComment(cursor);
		}
		return achievement;
	}

	// public void updateRecord(String recordId, String answered)
	// throws SQLException {
	// ContentValues quizObject = new ContentValues();
	// quizObject.put(COLUMN_ANSWERED, answered);
	// database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	public ArrayList<Achievement> selectRecords() {
		ArrayList<Achievement> achievementList = new ArrayList<Achievement>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				Achievement bagItem = cursorToComment(mCursor);
				achievementList.add(bagItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return achievementList; // iterate to get each value.
	}

	private Achievement cursorToComment(Cursor cursor) {
		Achievement achievement = new Achievement();
		achievement.setAchievementId(cursor.getString(0));
		achievement.setTitle(cursor.getString(1));
		achievement.setDescription(cursor.getString(2));
		achievement.setUserId(cursor.getString(3));
		achievement.setDateTime(cursor.getString(4));
		achievement.setImageUrl(cursor.getString(5));

		return achievement;
	}
}