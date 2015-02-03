package com.movie.locations.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.movie.locations.domain.BagItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BagItemImpl extends SQLiteOpenHelper {

	private BagItemSqliteHelper dbHelper;
	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "bagitems.db";
	private static final int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "bagitems"; // name of table
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_GROUP_TITLE = "_grouptitle";
	private static final String COLUMN_TITLE = "_title";
	private static final String COLUMN_DESCRIPTION = "_description";
	private static final String COLUMN_IMAGE_URL = "_imageurl";
	private static final String COLUMN_LEVEL = "_level";
	private static Map<String, BagItem> BAG_ITEM_MAP = new HashMap<String, BagItem>();
	private String[] allColumns = { COLUMN_ID, COLUMN_GROUP_TITLE,
			COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE_URL, COLUMN_LEVEL };

	/**
	 * 
	 * @param context
	 */
	public BagItemImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		dbHelper = new BagItemSqliteHelper(context);
//		database = dbHelper.getWritableDatabase();
	}


	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_GROUP_TITLE + " text, " 
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_IMAGE_URL + " text, "
			+ COLUMN_LEVEL + " text);";

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
		database.execSQL("DROP TABLE IF EXISTS bagitems");
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

	public BagItem createRecord(BagItem bagItem) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, bagItem.getItemId());
		values.put(COLUMN_GROUP_TITLE, bagItem.getBagGroupTitle());
		values.put(COLUMN_TITLE, bagItem.getItemTitle());
		values.put(COLUMN_DESCRIPTION, bagItem.getDescription());
		values.put(COLUMN_IMAGE_URL, bagItem.getImageUrl());
		values.put(COLUMN_LEVEL, bagItem.getLevel());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(BagItemSqliteHelper.TABLE_BAG_ITEMS,
				null, values);
		Cursor cursor = database.query(BagItemSqliteHelper.TABLE_BAG_ITEMS,
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

	public void deleteRecordsByLevel(String level) {
		
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
			// cursor.moveToFirst();
			// user = cursorToComment(cursor);
		}
		return bagItem;
	}

	// public void updateRecord(String recordId, String answered)
	// throws SQLException {
	// ContentValues quizObject = new ContentValues();
	// quizObject.put(COLUMN_ANSWERED, answered);
	// database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	public ArrayList<BagItem> selectRecords() {
		ArrayList<BagItem> bagItemList = new ArrayList<BagItem>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				BagItem bagItem = cursorToComment(mCursor);
				bagItemList.add(bagItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return bagItemList; // iterate to get each value.
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
}