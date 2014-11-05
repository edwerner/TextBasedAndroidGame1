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

public class GameTitleImpl {

	private GameTitleSqliteHelper dbHelper;

	private SQLiteDatabase database;

	// private String bagGroupTitle;
	// private String itemTitle;
	// private String description;
	// private String imageUrl;
	// private String level;

	public final static String TABLE_NAME = "gametitles"; // name of table
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_TYPE = "_type";
	public static final String COLUMN_LEVEL = "_level";
	public static final String COLUMN_PHASE = "_phase";

	public static Map<String, GameTitle> BAG_ITEM_MAP = new HashMap<String, GameTitle>();
	private String[] allColumns = { COLUMN_ID, COLUMN_TITLE, COLUMN_TYPE, COLUMN_LEVEL, COLUMN_PHASE };

	/**
	 * 
	 * @param context
	 */
	public GameTitleImpl(Context context) {
		dbHelper = new GameTitleSqliteHelper(context);
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

	public GameTitle createRecord(GameTitle gameTitle) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, gameTitle.getId());
		values.put(COLUMN_TITLE, gameTitle.getTitle());
		values.put(COLUMN_TYPE, gameTitle.getType());
		values.put(COLUMN_LEVEL, gameTitle.getLevel());
		values.put(COLUMN_PHASE, gameTitle.getPhase());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(GameTitleSqliteHelper.TABLE_GAME_TITLES,
				null, values);
		Cursor cursor = database.query(GameTitleSqliteHelper.TABLE_GAME_TITLES,
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

	public void deleteRecordsByLevel(String level) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
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
			// cursor.moveToFirst();
			// user = cursorToComment(cursor);
		}
		return title;
	}

	// public void updateRecord(String recordId, String answered)
	// throws SQLException {
	// ContentValues quizObject = new ContentValues();
	// quizObject.put(COLUMN_ANSWERED, answered);
	// database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	public ArrayList<GameTitle> selectRecords() {
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				GameTitle title = cursorToComment(mCursor);
				gameTitleList.add(title);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return gameTitleList; // iterate to get each value.
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
//		GameTitle title = null;


		// String num;
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
}
