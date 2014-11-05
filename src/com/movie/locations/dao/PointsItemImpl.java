package com.movie.locations.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.PointsItem;

public class PointsItemImpl {
	private PointsItemSqliteHelper dbHelper;

	private SQLiteDatabase database;

	// THE XP STORAGE MECHANISM ON THE MOBILE CLIENT
	public final static String POINTS_ITEM_TABLE = "pointsitems"; // name of
																	// table
	public static final String COLUMN_USER_ID = "_userid";
	public static final String COLUMN_POINTS_USER_ID = "_pointsuserid";
	public static final String COLUMN_POINTS = "_points";
	public static final String COLUMN_BONUS_POINTS = "_bonuspoints";

	public static Map<String, PointsItem> POINTS_ITEM_MAP = new HashMap<String, PointsItem>();
	private String[] allColumns = { COLUMN_USER_ID, COLUMN_POINTS_USER_ID,
			COLUMN_POINTS, COLUMN_BONUS_POINTS };

	/**
	 * 
	 * @param context
	 */
	public PointsItemImpl(Context context) {
		dbHelper = new PointsItemSqliteHelper(context);
		database = dbHelper.getWritableDatabase();
	}

	public void delete() {
		database.delete(POINTS_ITEM_TABLE, null, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public PointsItem createRecord(PointsItem pointsItem) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_USER_ID, pointsItem.getUserId());
		values.put(COLUMN_POINTS_USER_ID, pointsItem.getPointsUserId());
		values.put(COLUMN_POINTS, pointsItem.getPoints());
		values.put(COLUMN_BONUS_POINTS, pointsItem.getPoints());

		// pc.writeString(userId);
		// pc.writeString(pointsUserId);
		// pc.writeString(points);
		// pc.writeString(bonusPoints);
		// pointsItem.setUserId(cursor.getString(0));
		// pointsItem.setPointsUserId(cursor.getString(1));
		// pointsItem.setPoints(cursor.getString(2));
		// pointsItem.setBonusPoints(cursor.getString(3));

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(
				PointsItemSqliteHelper.TABLE_POINTS_ITEMS, null, values);
		Cursor cursor = database.query(
				PointsItemSqliteHelper.TABLE_POINTS_ITEMS, allColumns,
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
		Cursor cursor = database.query(POINTS_ITEM_TABLE, allColumns,
				COLUMN_USER_ID + "=?", recordIdArray, null, null, null, null);
		PointsItem pointsItem = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				pointsItem = cursorToComment(cursor);
			}
			// cursor.moveToFirst();
			// user = cursorToComment(cursor);
		}
		return pointsItem;
	}

	public void updateRecordPointsValue(String recordId, String pointsValue) {
		ContentValues pointsObject = new ContentValues();
		pointsObject.put(COLUMN_POINTS, pointsValue);
		database.update(POINTS_ITEM_TABLE, pointsObject, COLUMN_POINTS_USER_ID // should be COLUMN_USER_ID
				+ "=" + "'" + recordId + "'", null);
	}

	// updatedPoints, updatedBonusPoints
	public void updateRecordBonusPointsValue(String recordId, String updatedPoints, String updatedBonusPoints) {
		ContentValues pointsObject = new ContentValues();
		
		if (updatedPoints != null) {
			pointsObject.put(COLUMN_POINTS, updatedPoints);	
		}
		
		if (updatedBonusPoints != null) {
			pointsObject.put(COLUMN_BONUS_POINTS, updatedBonusPoints);	
		}
		
		database.update(POINTS_ITEM_TABLE, pointsObject, COLUMN_USER_ID
				+ "=" + "'" + recordId + "'", null);
	}

	// public void updateRecord(String recordId, String answered)
	// throws SQLException {
	// ContentValues quizObject = new ContentValues();
	// quizObject.put(COLUMN_ANSWERED, answered);
	// database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	public ArrayList<PointsItem> selectRecords() {
		ArrayList<PointsItem> pointsItemList = new ArrayList<PointsItem>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, POINTS_ITEM_TABLE, allColumns,
				null, null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				PointsItem pointsItem = cursorToComment(mCursor);
				pointsItemList.add(pointsItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return pointsItemList; // iterate to get each value.
	}

	private PointsItem cursorToComment(Cursor cursor) {

		PointsItem pointsItem = new PointsItem();
		pointsItem.setUserId(cursor.getString(0));
		pointsItem.setPointsUserId(cursor.getString(1));
		pointsItem.setPoints(cursor.getString(2));
		pointsItem.setBonusPoints(cursor.getString(3));
		// pc.writeString(userId);
		// pc.writeString(pointsUserId);
		// pc.writeString(points);
		// pc.writeString(bonusPoints);

		return pointsItem;
	}
}
