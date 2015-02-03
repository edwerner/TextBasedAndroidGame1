package com.movie.locations.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.movie.locations.domain.ConclusionCard;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConclusionCardImpl extends SQLiteOpenHelper {

	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "conclusioncards.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "conclusioncards"; // name of table
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_TITLE = "_title";
	private static final String COLUMN_COPY = "_copy";
	private static final String COLUMN_IMAGE_URL = "_imageurl";
	private static final String COLUMN_LEVEL = "_level";
	private static Map<String, ConclusionCard> BAG_ITEM_MAP = new HashMap<String, ConclusionCard>();
	private String[] allColumns = { COLUMN_ID,
			COLUMN_TITLE, COLUMN_COPY, COLUMN_IMAGE_URL, COLUMN_LEVEL };

	/**
	 * 
	 * @param context
	 */
	public ConclusionCardImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		database = dbHelper.getWritableDatabase();
	}

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_TITLE + " text, " 
			+ COLUMN_COPY + " text, "
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
		database.execSQL("DROP TABLE IF EXISTS conclusioncards");
		onCreate(database);
	}

	public void delete() {
		database.delete(TABLE_NAME, null, null);
	}

	public void open() throws SQLException {
		database = this.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public ConclusionCard createRecord(ConclusionCard card) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, card.getId());
		values.put(COLUMN_TITLE, card.getTitle());
		values.put(COLUMN_COPY, card.getCopy());
		values.put(COLUMN_IMAGE_URL, card.getImageUrl());
		values.put(COLUMN_LEVEL, card.getLevel());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(TABLE_NAME,
				null, values);
		Cursor cursor = database.query(TABLE_NAME,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		ConclusionCard cardCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				cardCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		return cardCursor;
	}

	public void deleteRecordsByLevel(String level) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_LEVEL + "=?", levelArray);
	}
	public ConclusionCard selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		ConclusionCard card = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				card = cursorToComment(cursor);
			}
			// cursor.moveToFirst();
			// user = cursorToComment(cursor);
		}
		return card;
	}

	// public void updateRecord(String recordId, String answered)
	// throws SQLException {
	// ContentValues quizObject = new ContentValues();
	// quizObject.put(COLUMN_ANSWERED, answered);
	// database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	public ArrayList<ConclusionCard> selectRecords() {
		ArrayList<ConclusionCard> cardList = new ArrayList<ConclusionCard>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				ConclusionCard card = cursorToComment(mCursor);
				cardList.add(card);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return cardList; // iterate to get each value.
	}

	private ConclusionCard cursorToComment(Cursor cursor) {
		ConclusionCard card = new ConclusionCard();
		card.setId(cursor.getString(0));
		card.setTitle(cursor.getString(1));
		card.setCopy(cursor.getString(2));
		card.setImageUrl(cursor.getString(3));
		card.setLevel(cursor.getString(4));
		return card;
	}
}