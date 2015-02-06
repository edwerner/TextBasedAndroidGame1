package com.movie.locations.database;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.movie.locations.domain.PointsItem;

public class PointsItemImpl extends SQLiteOpenHelper implements IDatabase {
	
	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "pointsitems.db";
	private static final int DATABASE_VERSION = 1;
	private final String TABLE_NAME = "pointsitems"; // name of table
	private final String COLUMN_USER_ID = "_userid";
	private final String COLUMN_POINTS_USER_ID = "_pointsuserid";
	private final String COLUMN_POINTS = "_points";
	private final String COLUMN_BONUS_POINTS = "_bonuspoints";
	
	private final String[] allColumns = { COLUMN_USER_ID, COLUMN_POINTS_USER_ID,
			COLUMN_POINTS, COLUMN_BONUS_POINTS };

	// Database creation sql statement
	private final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_USER_ID + " text, "
			+ COLUMN_POINTS_USER_ID + " text, " 
			+ COLUMN_POINTS + " text, "
			+ COLUMN_BONUS_POINTS + " text);";

	/**
	 * 
	 * @param context
	 */
	public PointsItemImpl(Context context) {
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

	public PointsItem createRecord(PointsItem pointsItem) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_USER_ID, pointsItem.getUserId());
		values.put(COLUMN_POINTS_USER_ID, pointsItem.getPointsUserId());
		values.put(COLUMN_POINTS, pointsItem.getPoints());
		values.put(COLUMN_BONUS_POINTS, pointsItem.getPoints());

		long insertId = database.insert(TABLE_NAME, null, values);
		Cursor cursor = database.query(
				TABLE_NAME, allColumns,
				COLUMN_USER_ID + " = " + insertId, null, null, null, null);
		PointsItem pointsItemCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				pointsItemCursor = cursorToComment(cursor);
			}
			cursor.close();
		}
		return pointsItemCursor;
	}

	public PointsItem selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_USER_ID + "=?", recordIdArray, null, null, null, null);
		PointsItem pointsItem = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				pointsItem = cursorToComment(cursor);
			}
		}
		return pointsItem;
	}

	public void updateRecordPointsValue(String recordId, String pointsValue) {
		ContentValues pointsObject = new ContentValues();
		pointsObject.put(COLUMN_POINTS, pointsValue);
		database.update(TABLE_NAME, pointsObject, COLUMN_POINTS_USER_ID // should be COLUMN_USER_ID
				+ "=" + "'" + recordId + "'", null);
	}

	public void updateRecordBonusPointsValue(String recordId, String updatedPoints, String updatedBonusPoints) {
		ContentValues pointsObject = new ContentValues();
		
		if (updatedPoints != null) {
			pointsObject.put(COLUMN_POINTS, updatedPoints);	
		}
		
		if (updatedBonusPoints != null) {
			pointsObject.put(COLUMN_BONUS_POINTS, updatedBonusPoints);	
		}
		
		database.update(TABLE_NAME, pointsObject, COLUMN_USER_ID
				+ "=" + "'" + recordId + "'", null);
	}

	public ArrayList<PointsItem> selectRecords() {
		ArrayList<PointsItem> pointsItemList = new ArrayList<PointsItem>();
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns,
				null, null, null, null, null, null);

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				PointsItem pointsItem = cursorToComment(mCursor);
				pointsItemList.add(pointsItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return pointsItemList;
	}

	private PointsItem cursorToComment(Cursor cursor) {
		PointsItem pointsItem = new PointsItem();
		pointsItem.setUserId(cursor.getString(0));
		pointsItem.setPointsUserId(cursor.getString(1));
		pointsItem.setPoints(cursor.getString(2));
		pointsItem.setBonusPoints(cursor.getString(3));
		
		return pointsItem;
	}

	@Override
	public void deleteRecordById(String string) {
		
	}

	@Override
	public void deleteRecordByLevel(String level) {
		
	}

	@Override
	public void deleteRecordByTitle(String recordTitle) {
		
	}
}