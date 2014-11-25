package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuizItemSqliteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "quizitems.db";
	public static final String TABLE_QUIZ_ITEMS = "quizitems";
	private static final String COLUMN_ID = "_id";
	private final static String COLUMN_FILM_TITLE = "_filmtitle";
	private final static String COLUMN_ANSWER_SUBMIT_COUNT = "_datetime";
	private final static String COLUMN_QUESTION_TEXT = "_questiontext";
	private final static String COLUMN_ANSWER_01 = "_answer1";
	private final static String COLUMN_ANSWER_02 = "_answer2";
	private final static String COLUMN_ANSWER_03 = "_answer3";
	private final static String COLUMN_ANSWER_04 = "_answer4";
	private final static String COLUMN_REACTION_01 = "_reaction1";
	private final static String COLUMN_REACTION_02 = "_reaction2";
	private final static String COLUMN_REACTION_03 = "_reaction3";
	private final static String COLUMN_REACTION_04 = "_reaction4";
	private static final String COLUMN_WORLD_ID = "_worldid";
	private static final String COLUMN_WORLD_TITLE = "_worldtitle";
	private final static String COLUMN_ANSWERED = "_answered";
	private static final String COLUMN_LEVEL = "_level";
	private static final String COLUMN_ACTIVE_ITEM = "_activeitem";
	private static final String COLUMN_ACTIVE_ITEM_01 = "_activeitem1";
	private static final String COLUMN_ACTIVE_ITEM_02 = "_activeitem2";
	private static final String COLUMN_ACTIVE_ITEM_03 = "_activeitem3";
	private static final String COLUMN_ACTIVE_ITEM_04 = "_activeitem4";
	private static final String COLUMN_CORRECT_ANSWER_INDEX = "_correctanswerindex";
	private static final String COLUMN_POINT_VALUE = "_pointvalue";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_QUIZ_ITEMS + "(" 
			+ COLUMN_ID + " text, " 
			+ COLUMN_FILM_TITLE + " text, "
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

	public QuizItemSqliteHelper(Context context) {
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
}