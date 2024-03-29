package com.movie.locations.database;

import java.util.ArrayList;
import com.movie.locations.domain.BagItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BagItemImpl extends SQLiteOpenHelper implements IDatabase {

	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "bagitems.db";
	private static final int DATABASE_VERSION = 1;
	private static String TABLE_NAME = "bagitems";
	private final String COLUMN_ID = "_id";
	private final String COLUMN_GROUP_TITLE = "_grouptitle";
	private final String COLUMN_TITLE = "_title";
	private final String COLUMN_DESCRIPTION = "_description";
	private final String COLUMN_IMAGE_URL = "_imageurl";
	private final String COLUMN_LEVEL = "_level";
	private final String[] allColumns = { COLUMN_ID, COLUMN_GROUP_TITLE,
			COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE_URL, COLUMN_LEVEL };

	// Database creation sql statement
	private final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_GROUP_TITLE + " text, " 
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_IMAGE_URL + " text, "
			+ COLUMN_LEVEL + " text);";

	/**
	 * 
	 * @param context
	 */
	public BagItemImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Method is called during an upgrade of the database,
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w("Database", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS bagitems");
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

	public BagItem createRecord(BagItem bagItem) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, bagItem.getItemId());
		values.put(COLUMN_GROUP_TITLE, bagItem.getBagGroupTitle());
		values.put(COLUMN_TITLE, bagItem.getItemTitle());
		values.put(COLUMN_DESCRIPTION, bagItem.getDescription());
		values.put(COLUMN_IMAGE_URL, bagItem.getImageUrl());
		values.put(COLUMN_LEVEL, bagItem.getLevel());

		long insertId = database.insert(TABLE_NAME, null, values);
		Cursor cursor = database.query(TABLE_NAME,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		BagItem bagItemCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				bagItemCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		return bagItemCursor;
	}
	
	@Override
	public void deleteRecordByLevel(String level) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_LEVEL + "=?", levelArray);
	}
	
	public BagItem selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		
		BagItem bagItem = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				bagItem = cursorToComment(cursor);
			}
		}
		return bagItem;
	}

	public ArrayList<BagItem> selectRecords() {
		ArrayList<BagItem> bagItemList = new ArrayList<BagItem>();
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				BagItem bagItem = cursorToComment(mCursor);
				bagItemList.add(bagItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return bagItemList;
	}

	private BagItem cursorToComment(Cursor cursor) {
		BagItem bagItem = new BagItem();
		bagItem.setBagGroupTitle(cursor.getString(0));
		bagItem.setItemId(cursor.getString(1));
		bagItem.setItemTitle(cursor.getString(2));
		bagItem.setDescription(cursor.getString(3));
		bagItem.setImageUrl(cursor.getString(4));
		bagItem.setLevel(cursor.getString(5));

		return bagItem;
	}

	@Override
	public void deleteRecordById(String string) {
		
	}

	@Override
	public void deleteRecordByTitle(String recordTitle) {
		
	}
}