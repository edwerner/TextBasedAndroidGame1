package com.movie.locations.dao;

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
import com.movie.locations.domain.ConclusionCard;
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

public class ConclusionCardImpl {

	private ConclusionCardSqliteHelper dbHelper;

	private SQLiteDatabase database;

//	private String id;
//	private String title;
//	private String copy;
//	private String imageUrl;
//	private String level;

	public final static String TABLE_NAME = "conclusioncards"; // name of table
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_COPY = "_copy";
	public static final String COLUMN_IMAGE_URL = "_imageurl";
	public static final String COLUMN_LEVEL = "_level";

	public static Map<String, ConclusionCard> BAG_ITEM_MAP = new HashMap<String, ConclusionCard>();
	private String[] allColumns = { COLUMN_ID,
			COLUMN_TITLE, COLUMN_COPY, COLUMN_IMAGE_URL, COLUMN_LEVEL };

	/**
	 * 
	 * @param context
	 */
	public ConclusionCardImpl(Context context) {
		dbHelper = new ConclusionCardSqliteHelper(context);
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

	public ConclusionCard createRecord(ConclusionCard card) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, card.getId());
		values.put(COLUMN_TITLE, card.getTitle());
		values.put(COLUMN_COPY, card.getCopy());
		values.put(COLUMN_IMAGE_URL, card.getImageUrl());
		values.put(COLUMN_LEVEL, card.getLevel());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(ConclusionCardSqliteHelper.TABLE_CONCLUSION_CARDS,
				null, values);
		Cursor cursor = database.query(ConclusionCardSqliteHelper.TABLE_CONCLUSION_CARDS,
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