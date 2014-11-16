package com.movie.locations.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movie.locations.domain.CheckIn;
import com.movie.locations.domain.ClassLoaderHelper;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.util.SessionIdentifierGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

public class QuizItemImpl {

	private QuizItemSqliteHelper dbHelper;

	private SQLiteDatabase database;

	public final static String TABLE_NAME = "quizitems"; // name of table

	// DATABASE COLUMN NAMES
	//
	// questionId
	// filmTitle
	// dateTime
	// questionText
	// answer1
	// answer2
	// answer3
	// answer4
	// reaction1
	// reaction2
	// reaction3
	// reaction4
	// worldId
	// worldTitle
	// submittedAnswerIndex // not persisting, just for http post
	// correctAnswerIndex // not needed on client
	// answered
	// level
	// activeItem
	// activeItem1
	// activeItem2
	// activeItem3
	// activeItem4

	public static final String COLUMN_ID = "_id";
	// public static final String COLUMN_SID = "_sid";
	// public final static String COLUMN_FILM_TITLE = "_filmtitle";
	public final static String COLUMN_ANSWER_SUBMIT_COUNT = "_datetime";
	public final static String COLUMN_QUESTION_TEXT = "_questiontext";
	public final static String COLUMN_ANSWER_01 = "_answer1";
	public final static String COLUMN_ANSWER_02 = "_answer2";
	public final static String COLUMN_ANSWER_03 = "_answer3";
	public final static String COLUMN_ANSWER_04 = "_answer4";
	public final static String COLUMN_REACTION_01 = "_reaction1";
	public final static String COLUMN_REACTION_02 = "_reaction2";
	public final static String COLUMN_REACTION_03 = "_reaction3";
	public final static String COLUMN_REACTION_04 = "_reaction4";
	public static final String COLUMN_WORLD_ID = "_worldid";
	public static final String COLUMN_WORLD_TITLE = "_worldtitle";
	// public static final String COLUMN_LOCATIONS = "_locations";
	// public static final String COLUMN_POSITION = "_position";
	public final static String COLUMN_ANSWERED = "_answered";
	public static final String COLUMN_LEVEL = "_level";
	public static final String COLUMN_ACTIVE_ITEM = "_activeitem";
	public static final String COLUMN_ACTIVE_ITEM_01 = "_activeitem1";
	public static final String COLUMN_ACTIVE_ITEM_02 = "_activeitem2";
	public static final String COLUMN_ACTIVE_ITEM_03 = "_activeitem3";
	public static final String COLUMN_ACTIVE_ITEM_04 = "_activeitem4";
	public static final String COLUMN_CORRECT_ANSWER_INDEX = "_correctanswerindex";
	// public final static String COLUMN_STATIC_MAP_IMAGE_URL =
	// "_staticmapimageurl";

	public static Map<String, QuizItem> QUIZ_ITEM_MAP = new HashMap<String, QuizItem>();
	private String[] allColumns = { COLUMN_ID, COLUMN_ANSWER_SUBMIT_COUNT,
			COLUMN_QUESTION_TEXT, COLUMN_ANSWER_01, COLUMN_ANSWER_02,
			COLUMN_ANSWER_03, COLUMN_ANSWER_04, COLUMN_REACTION_01,
			COLUMN_REACTION_02, COLUMN_REACTION_03, COLUMN_REACTION_04,
			COLUMN_WORLD_ID, COLUMN_WORLD_TITLE, COLUMN_ANSWERED, COLUMN_LEVEL,
			COLUMN_ACTIVE_ITEM, COLUMN_ACTIVE_ITEM_01, COLUMN_ACTIVE_ITEM_02,
			COLUMN_ACTIVE_ITEM_03, COLUMN_ACTIVE_ITEM_04,
			COLUMN_CORRECT_ANSWER_INDEX };

	/**
	 * 
	 * @param context
	 */
	public QuizItemImpl(Context context) {
		dbHelper = new QuizItemSqliteHelper(context);
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

	public QuizItem createRecord(QuizItem quizItem) {
		ContentValues values = new ContentValues();

		// COLUMN_ID, COLUMN_FILM_TITLE,
		// COLUMN_DATETIME, COLUMN_QUESTION_TEXT, COLUMN_ANSWER_01,
		// COLUMN_ANSWER_02, COLUMN_ANSWER_03, COLUMN_ANSWER_04,
		// COLUMN_REACTION_01, COLUMN_REACTION_02, COLUMN_REACTION_03,
		// COLUMN_REACTION_04, COLUMN_WORLD_ID, COLUMN_WORLD_TITLE,
		// COLUMN_ANSWERED, COLUMN_LEVEL,
		// COLUMN_ACTIVE_ITEM, COLUMN_ACTIVE_ITEM_01, COLUMN_ACTIVE_ITEM_02,
		// COLUMN_ACTIVE_ITEM_03, COLUMN_ACTIVE_ITEM_04

		values.put(COLUMN_ID, quizItem.getQuestionId());
		// values.put(COLUMN_SID, quizItem.get());
		// values.put(COLUMN_FILM_TITLE, quizItem.getFilmTitle());
		values.put(COLUMN_ANSWER_SUBMIT_COUNT, quizItem.getAnswerSubmitCount());
		values.put(COLUMN_QUESTION_TEXT, quizItem.getQuestionText());
		values.put(COLUMN_ANSWER_01, quizItem.getAnswer1());
		values.put(COLUMN_ANSWER_02, quizItem.getAnswer2());
		values.put(COLUMN_ANSWER_03, quizItem.getAnswer3());
		values.put(COLUMN_ANSWER_04, quizItem.getAnswer4());
		values.put(COLUMN_REACTION_01, quizItem.getReaction1());
		values.put(COLUMN_REACTION_02, quizItem.getReaction2());
		values.put(COLUMN_REACTION_03, quizItem.getReaction3());
		values.put(COLUMN_REACTION_04, quizItem.getReaction4());
		values.put(COLUMN_WORLD_ID, quizItem.getWorldId());
		values.put(COLUMN_WORLD_TITLE, quizItem.getWorldTitle());
		values.put(COLUMN_ANSWERED, quizItem.getAnswered());
		values.put(COLUMN_LEVEL, quizItem.getLevel());
		values.put(COLUMN_ACTIVE_ITEM, quizItem.getActiveItem());
		values.put(COLUMN_ACTIVE_ITEM_01, quizItem.getActiveItem1());
		values.put(COLUMN_ACTIVE_ITEM_02, quizItem.getActiveItem2());
		values.put(COLUMN_ACTIVE_ITEM_03, quizItem.getActiveItem3());
		values.put(COLUMN_ACTIVE_ITEM_04, quizItem.getActiveItem4());
		values.put(COLUMN_CORRECT_ANSWER_INDEX, quizItem.getCorrectAnswerIndex()); // not set on client

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(QuizItemSqliteHelper.TABLE_QUIZ_ITEMS,
				null, values);
		Cursor cursor = database.query(QuizItemSqliteHelper.TABLE_QUIZ_ITEMS,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		QuizItem quizItemCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				quizItemCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		return quizItemCursor;
	}

	public void deleteRecordsByTitle(String recordTitle) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] recordTitleArray = { recordTitle };
		database.delete(TABLE_NAME, COLUMN_WORLD_TITLE + "=?", recordTitleArray);
	}
	
	public void deleteRecordsByLevel(String level) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_LEVEL + "=?", levelArray);
	}
	
	public QuizItem selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_WORLD_ID + "=?", recordIdArray, null, null, null, null);
		QuizItem quizItem = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				quizItem = cursorToComment(cursor);
			}
		}
		return quizItem;
	}


	public ArrayList<QuizItem> selectRecordsByWorldTitle(String worldTitle) {
		ArrayList<QuizItem> quizItems = new ArrayList<QuizItem>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, COLUMN_WORLD_TITLE + "=" + "'"
				+ worldTitle + "'", null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				QuizItem quizItem = cursorToComment(mCursor);
				quizItems.add(quizItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return quizItems; // iterate to get each value.
	}

	public ArrayList<QuizItem> selectRecordsByTitle(String title) {
		ArrayList<QuizItem> quizItemsArrayList = new ArrayList<QuizItem>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);
		// String num;

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				QuizItem quizItem = cursorToComment(mCursor);
				
				if (quizItem.getWorldTitle().equals(title)) {
					quizItemsArrayList.add(quizItem);
				}
				
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return quizItemsArrayList; // iterate to get each value.
	}	

	public void updateRecordCorrectAnswerIndex(String recordId, String correctAnswerIndex) {
		ContentValues quizObject = new ContentValues();
		quizObject.put(COLUMN_CORRECT_ANSWER_INDEX, correctAnswerIndex);
		database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}

	public void updateRecordAnswered(String recordId, String answered)
			throws SQLException {
		ContentValues quizObject = new ContentValues();
		quizObject.put(COLUMN_ANSWERED, answered);
		database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}

	public ArrayList<QuizItem> selectRecords() {
		ArrayList<QuizItem> quizItems = new ArrayList<QuizItem>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				QuizItem quizItem = cursorToComment(mCursor);
				quizItems.add(quizItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return quizItems; // iterate to get each value.
	}

	private QuizItem cursorToComment(Cursor cursor) {

		QuizItem quizItem = new QuizItem();
		quizItem.setQuestionId(cursor.getString(0));
		// quizItem.setFilmTitle(cursor.getString(1));
		quizItem.setAnswerSubmitCount(cursor.getString(1));
		quizItem.setQuestionText(cursor.getString(2));
		quizItem.setAnswer1(cursor.getString(3));
		quizItem.setAnswer2(cursor.getString(4));
		quizItem.setAnswer3(cursor.getString(5));
		quizItem.setAnswer4(cursor.getString(6));
		quizItem.setReaction1(cursor.getString(7));
		quizItem.setReaction2(cursor.getString(8));
		quizItem.setReaction3(cursor.getString(9));
		quizItem.setReaction4(cursor.getString(10));
		quizItem.setWorldId(cursor.getString(11));
		quizItem.setWorldTitle(cursor.getString(12));
		quizItem.setAnswered(cursor.getString(13));
		quizItem.setLevel(cursor.getString(14));
		quizItem.setActiveItem(cursor.getString(15));
		quizItem.setActiveItem1(cursor.getString(16));
		quizItem.setActiveItem2(cursor.getString(17));
		quizItem.setActiveItem3(cursor.getString(18));
		quizItem.setActiveItem4(cursor.getString(19));
		quizItem.setCorrectAnswerIndex(cursor.getString(20));
		// quizItem.setCorrectAnswerIndex(cursor.getInt(6)); // not needed on
		// client

		return quizItem;
	}
}