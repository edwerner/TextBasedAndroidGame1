package com.movie.locations.database;
import java.util.ArrayList;
import com.movie.locations.domain.QuizItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuizItemImpl extends SQLiteOpenHelper implements IDatabase {

	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "quizitems.db";
	private static final int DATABASE_VERSION = 1;
	private final String TABLE_NAME = "quizitems"; // name of table
	private final String COLUMN_ID = "_id";
	private final String COLUMN_ANSWER_SUBMIT_COUNT = "_datetime";
	private final String COLUMN_QUESTION_TEXT = "_questiontext";
	private final String COLUMN_ANSWER_01 = "_answer1";
	private final String COLUMN_ANSWER_02 = "_answer2";
	private final String COLUMN_ANSWER_03 = "_answer3";
	private final String COLUMN_ANSWER_04 = "_answer4";
	private final String COLUMN_REACTION_01 = "_reaction1";
	private final String COLUMN_REACTION_02 = "_reaction2";
	private final String COLUMN_REACTION_03 = "_reaction3";
	private final String COLUMN_REACTION_04 = "_reaction4";
	private final String COLUMN_WORLD_ID = "_worldid";
	private final String COLUMN_WORLD_TITLE = "_worldtitle";
	private final String COLUMN_ANSWERED = "_answered";
	private final String COLUMN_LEVEL = "_level";
	private final String COLUMN_ACTIVE_ITEM = "_activeitem";
	private final String COLUMN_ACTIVE_ITEM_01 = "_activeitem1";
	private final String COLUMN_ACTIVE_ITEM_02 = "_activeitem2";
	private final String COLUMN_ACTIVE_ITEM_03 = "_activeitem3";
	private final String COLUMN_ACTIVE_ITEM_04 = "_activeitem4";
	private final String COLUMN_CORRECT_ANSWER_INDEX = "_correctanswerindex";
	private final String COLUMN_POINT_VALUE = "_pointvalue";
	private final String[] allColumns = { COLUMN_ID, COLUMN_ANSWER_SUBMIT_COUNT,
			COLUMN_QUESTION_TEXT, COLUMN_ANSWER_01, COLUMN_ANSWER_02,
			COLUMN_ANSWER_03, COLUMN_ANSWER_04, COLUMN_REACTION_01,
			COLUMN_REACTION_02, COLUMN_REACTION_03, COLUMN_REACTION_04,
			COLUMN_WORLD_ID, COLUMN_WORLD_TITLE, COLUMN_ANSWERED, COLUMN_LEVEL,
			COLUMN_ACTIVE_ITEM, COLUMN_ACTIVE_ITEM_01, COLUMN_ACTIVE_ITEM_02,
			COLUMN_ACTIVE_ITEM_03, COLUMN_ACTIVE_ITEM_04,
			COLUMN_CORRECT_ANSWER_INDEX, COLUMN_POINT_VALUE };

	// Database creation sql statement
	private final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_ANSWER_SUBMIT_COUNT + " text, "
			+ COLUMN_QUESTION_TEXT + " text, "
			+ COLUMN_ANSWER_01 + " text, "
			+ COLUMN_ANSWER_02 + " text, "
			+ COLUMN_ANSWER_03 + " text, "
			+ COLUMN_ANSWER_04 + " text, "
			+ COLUMN_REACTION_01 + " text, "
			+ COLUMN_REACTION_02 + " text, "
			+ COLUMN_REACTION_03 + " text, "
			+ COLUMN_REACTION_04 + " text, "
			+ COLUMN_WORLD_ID + " text, "
			+ COLUMN_WORLD_TITLE + " text, "
			+ COLUMN_ANSWERED + " text, "
			+ COLUMN_LEVEL + " text, "
			+ COLUMN_ACTIVE_ITEM + " text, "
			+ COLUMN_ACTIVE_ITEM_01 + " text, "
			+ COLUMN_ACTIVE_ITEM_02 + " text, "
			+ COLUMN_ACTIVE_ITEM_03 + " text, "
			+ COLUMN_ACTIVE_ITEM_04 + " text, "
			+ COLUMN_CORRECT_ANSWER_INDEX + " text, "
			+ COLUMN_POINT_VALUE + " text);";
	
	/**
	 * 
	 * @param context
	 */
	public QuizItemImpl(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS quizitems");
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

	public QuizItem createRecord(QuizItem quizItem) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, quizItem.getQuestionId());
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
		values.put(COLUMN_POINT_VALUE, quizItem.getPointValue());

		long insertId = database.insert(TABLE_NAME,
				null, values);
		Cursor cursor = database.query(TABLE_NAME,
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

	@Override
	public void deleteRecordByTitle(String recordTitle) {
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] recordTitleArray = { recordTitle };
		database.delete(TABLE_NAME, COLUMN_WORLD_TITLE + "=?", recordTitleArray);
	}
	
	@Override
	public void deleteRecordByLevel(String level) {
		// DELETE ALL DATABASE RECORDS WITH MATCHING LEVEL
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_LEVEL + "=?", levelArray);
	}
	
	public QuizItem selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		
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
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, COLUMN_WORLD_TITLE + "=" + "'"
				+ worldTitle + "'", null,
				null, null, null, null, null);

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				QuizItem quizItem = cursorToComment(mCursor);
				quizItems.add(quizItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return quizItems;
	}

	public ArrayList<QuizItem> selectRecordsByTitle(String title) {
		ArrayList<QuizItem> quizItemsArrayList = new ArrayList<QuizItem>();
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

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
		return quizItemsArrayList;
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
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				QuizItem quizItem = cursorToComment(mCursor);
				quizItems.add(quizItem);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return quizItems;
	}

	private QuizItem cursorToComment(Cursor cursor) {
		QuizItem quizItem = new QuizItem();
		quizItem.setQuestionId(cursor.getString(0));
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
		quizItem.setPointValue(cursor.getString(21));
		return quizItem;
	}

	@Override
	public void deleteRecordById(String string) {
		
	}
}