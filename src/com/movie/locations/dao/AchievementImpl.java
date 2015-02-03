package com.movie.locations.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.BagItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AchievementImpl extends SQLiteOpenHelper implements DatabaseImpl {

	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "achievements.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "achievements"; // name of table
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_TITLE = "_title";
	private static final String COLUMN_DESCRIPTION = "_description";
	private static final String COLUMN_USER_ID = "_userid";
	private static final String COLUMN_DATETIME = "_datetime";
	private static final String COLUMN_IMAGE_URL = "_imageurl";
	private static final String COLUMN_LEVEL = "_level";
	
	private String[] allColumns = { COLUMN_ID, COLUMN_TITLE,
			COLUMN_DESCRIPTION, COLUMN_USER_ID, COLUMN_DATETIME,
			COLUMN_IMAGE_URL, COLUMN_LEVEL };

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_USER_ID + " text, "
			+ COLUMN_DATETIME + " text, "
			+ COLUMN_IMAGE_URL + " text, "
			+ COLUMN_LEVEL + " text);";
	
	/**
	 * 
	 * @param context
	 */
	public AchievementImpl(Context context) {
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

	public Achievement createRecord(Achievement achievement) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, achievement.getAchievementId());
		values.put(COLUMN_TITLE, achievement.getTitle());
		values.put(COLUMN_DESCRIPTION, achievement.getDescription());
		values.put(COLUMN_USER_ID, achievement.getUserId());
		values.put(COLUMN_DATETIME, achievement.getDateTime());
		values.put(COLUMN_IMAGE_URL, achievement.getImageUrl());
		values.put(COLUMN_LEVEL, achievement.getLevel());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(TABLE_NAME,
				null, values);
		Cursor cursor = database.query(TABLE_NAME,
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

	@Override
	public void deleteRecordById(String level) {

		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_ID + "=?", levelArray);
	}

	public Achievement selectRecordByLevel(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_LEVEL + "=?", recordIdArray, null, null, null, null);
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

	public Achievement cursorToComment(Cursor cursor) {
		Achievement achievement = new Achievement();
		achievement.setAchievementId(cursor.getString(0));
		achievement.setTitle(cursor.getString(1));
		achievement.setDescription(cursor.getString(2));
		achievement.setUserId(cursor.getString(3));
		achievement.setDateTime(cursor.getString(4));
		achievement.setImageUrl(cursor.getString(5));
		achievement.setLevel(cursor.getString(6));

		return achievement;
	}

	@Override
	public void deleteRecordByLevel(String level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRecordByTitle(String recordTitle) {
		// TODO Auto-generated method stub
		
	}
}