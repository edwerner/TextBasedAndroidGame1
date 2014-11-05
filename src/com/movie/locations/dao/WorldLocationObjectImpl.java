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
import com.movie.locations.domain.WorldLocationObject;
import com.movie.locations.util.SessionIdentifierGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

public class WorldLocationObjectImpl {

	private WorldLocationObjectSqliteHelper dbHelper;

	private SQLiteDatabase database;

	public static final String TABLE_WORLD_LOCATION_OBJECT_ITEMS = "worldlocationitems";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SID = "_sid";
	public final static String COLUMN_DATETIME = "_datetime";
	public final static String COLUMN_QUESTION_TEXT = "_questiontext";
	public final static String COLUMN_QUESTION_ID = "_questionid";
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
	public final static String COLUMN_ANSWERED = "_answered";
	public static final String COLUMN_LEVEL = "_level";
	public static final String COLUMN_ACTIVE_ITEM = "_activeitem";
	public static final String COLUMN_ACTIVE_ITEM_01 = "_activeitem1";
	public static final String COLUMN_ACTIVE_ITEM_02 = "_activeitem2";
	public static final String COLUMN_ACTIVE_ITEM_03 = "_activeitem3";
	public static final String COLUMN_ACTIVE_ITEM_04 = "_activeitem4";
	public static final String COLUMN_CORRECT_ANSWER_INDEX = "_correctanswerindex";
	public final static String COLUMN_POSITION = "_position";
	public final static String COLUMN_CREATED_AT = "_createdat";
	public final static String COLUMN_CREATED_META = "_createdmeta";
	public final static String COLUMN_UPDATED_AT = "_updateat";
	public final static String COLUMN_UPDATED_META = "_updatedmeta";
	public final static String COLUMN_META = "_meta";
	public final static String COLUMN_TITLE = "_title";
	public final static String COLUMN_RELEASE_YEAR = "_releaseyear";
	public final static String COLUMN_LOCATIONS = "_locations";
	public final static String COLUMN_FUN_FACTS = "_funfacts";
	public final static String COLUMN_PRODUCTION_COMPANY = "_productioncompany";
	public final static String COLUMN_DISTRIBUTOR = "_distributor";
	public final static String COLUMN_DIRECTOR = "_director";
	public final static String COLUMN_WRITER = "_writer";
	public final static String COLUMN_ACTOR_1 = "_actor1";
	public final static String COLUMN_ACTOR_2 = "_actor2";
	public final static String COLUMN_ACTOR_3 = "_actor3";
	public final static String COLUMN_LATITUDE = "_latitide";
	public final static String COLUMN_LONGITUDE = "_longitude";
	public final static String COLUMN_STATIC_MAP_IMAGE_URL = "_staticmapimageurl";

	public static Map<String, WorldLocationObject> WORLD_LOCATION_OBJECT_ITEM_MAP = new HashMap<String, WorldLocationObject>();
	
	private String[] allColumns = { COLUMN_ID, COLUMN_SID, COLUMN_DATETIME,
			COLUMN_QUESTION_TEXT, COLUMN_QUESTION_ID, COLUMN_ANSWER_01, COLUMN_ANSWER_02,
			COLUMN_ANSWER_03, COLUMN_ANSWER_04, COLUMN_REACTION_01,
			COLUMN_REACTION_02, COLUMN_REACTION_03, COLUMN_REACTION_04,
			COLUMN_WORLD_ID, COLUMN_WORLD_TITLE, COLUMN_ANSWERED, COLUMN_LEVEL,
			COLUMN_ACTIVE_ITEM, COLUMN_ACTIVE_ITEM_01, COLUMN_ACTIVE_ITEM_02,
			COLUMN_ACTIVE_ITEM_03, COLUMN_ACTIVE_ITEM_04,
			COLUMN_CORRECT_ANSWER_INDEX, COLUMN_POSITION, COLUMN_CREATED_AT,
			COLUMN_CREATED_META, COLUMN_UPDATED_AT, COLUMN_UPDATED_META,
			COLUMN_META, COLUMN_TITLE, COLUMN_RELEASE_YEAR, COLUMN_LOCATIONS,
			COLUMN_FUN_FACTS, COLUMN_PRODUCTION_COMPANY, COLUMN_DISTRIBUTOR,
			COLUMN_DIRECTOR, COLUMN_WRITER, COLUMN_ACTOR_1, COLUMN_ACTOR_2,
			COLUMN_ACTOR_3, COLUMN_LATITUDE, COLUMN_LONGITUDE,
			COLUMN_STATIC_MAP_IMAGE_URL };

	/**
	 * 
	 * @param context
	 */
	public WorldLocationObjectImpl(Context context) {
		dbHelper = new WorldLocationObjectSqliteHelper(context);
		database = dbHelper.getWritableDatabase();
	}

	public void delete() {
		database.delete(TABLE_WORLD_LOCATION_OBJECT_ITEMS, null, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public WorldLocationObject createRecord(WorldLocationObject location) {
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_SID, location.getSid());
		values.put(COLUMN_LEVEL, location.getLevel());
		values.put(COLUMN_POSITION, location.getPosition());
		values.put(COLUMN_CREATED_AT, location.getCreatedAt());
		values.put(COLUMN_CREATED_META, location.getCreatedMeta());
		values.put(COLUMN_UPDATED_AT, location.getUpdatedAt());
		values.put(COLUMN_UPDATED_META, location.getUpdatedMeta());
		values.put(COLUMN_META, location.getMeta());
		values.put(COLUMN_ID, location.getId());
		values.put(COLUMN_TITLE, location.getTitle());
		values.put(COLUMN_RELEASE_YEAR, location.getReleaseYear());
		values.put(COLUMN_LOCATIONS, location.getLocations());
		values.put(COLUMN_FUN_FACTS, location.getFunFacts());
		values.put(COLUMN_PRODUCTION_COMPANY, location.getProductionCompany());
		values.put(COLUMN_DISTRIBUTOR, location.getDistributor());
		values.put(COLUMN_DIRECTOR, location.getDirector());
		values.put(COLUMN_WRITER, location.getWriter());
		values.put(COLUMN_ACTOR_1, location.getActor1());
		values.put(COLUMN_ACTOR_2, location.getActor2());
		values.put(COLUMN_ACTOR_3, location.getActor3());
		values.put(COLUMN_LATITUDE, location.getLatitude());
		values.put(COLUMN_LONGITUDE, location.getLongitude());
		values.put(COLUMN_STATIC_MAP_IMAGE_URL, location.getStaticMapImageUrl());
		values.put(COLUMN_QUESTION_ID, location.getQuestionId());
		values.put(COLUMN_DATETIME, location.getDateTime());
		values.put(COLUMN_QUESTION_TEXT, location.getQuestionText());
		values.put(COLUMN_ANSWER_01, location.getAnswer1());
		values.put(COLUMN_ANSWER_02, location.getAnswer2());
		values.put(COLUMN_ANSWER_03, location.getAnswer3());
		values.put(COLUMN_ANSWER_04, location.getAnswer4());
		values.put(COLUMN_QUESTION_ID, location.getQuestionId());
		values.put(COLUMN_REACTION_01, location.getReaction1());
		values.put(COLUMN_REACTION_02, location.getReaction2());
		values.put(COLUMN_REACTION_03, location.getReaction3());
		values.put(COLUMN_REACTION_04, location.getReaction4());
		values.put(COLUMN_WORLD_ID, location.getWorldId());
		values.put(COLUMN_WORLD_TITLE, location.getWorldTitle());
//		values.put(COLUMN_SUBMITTED_ANSWER_INDEX, location.getSubmittedAnswerIndex());
		values.put(COLUMN_CORRECT_ANSWER_INDEX, location.getCorrectAnswerIndex());
		values.put(COLUMN_ANSWERED, location.getAnswered());
		values.put(COLUMN_ACTIVE_ITEM, location.getActiveItem());
		values.put(COLUMN_ACTIVE_ITEM_01, location.getActiveItem1());
		values.put(COLUMN_ACTIVE_ITEM_02, location.getActiveItem2());
		values.put(COLUMN_ACTIVE_ITEM_03, location.getActiveItem3());
		values.put(COLUMN_ACTIVE_ITEM_04, location.getActiveItem4());
		

		long insertId = database.insert(WorldLocationObjectSqliteHelper.TABLE_WORLD_LOCATION_OBJECT_ITEMS,
				null, values);
		Cursor cursor = database.query(WorldLocationObjectSqliteHelper.TABLE_WORLD_LOCATION_OBJECT_ITEMS,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		WorldLocationObject worldLocationCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				worldLocationCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		return worldLocationCursor;
	}

	public WorldLocationObject selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_WORLD_LOCATION_OBJECT_ITEMS,
				allColumns, COLUMN_ID + "=?", recordIdArray, null, null, null,
				null);
		WorldLocationObject location = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				location = cursorToComment(cursor);
			}
		}
		return location;
	}

//	public void updateRecord(String recordId, String answered)
//			throws SQLException {
//		ContentValues locationObject = new ContentValues();
//		locationObject.put(COLUMN_ANSWERED, answered);
//		database.update(TABLE_WORLD_LOCATION_OBJECT_ITEMS, locationObject,
//				COLUMN_ID + "=" + "'" + recordId + "'", null);
//	}

	public ArrayList<WorldLocationObject> selectRecords() {
		ArrayList<WorldLocationObject> worldLocationList = new ArrayList<WorldLocationObject>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true,
				TABLE_WORLD_LOCATION_OBJECT_ITEMS, allColumns, null, null,
				null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				WorldLocationObject location = cursorToComment(mCursor);
				worldLocationList.add(location);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return worldLocationList; // iterate to get each value.
	}

	private WorldLocationObject cursorToComment(Cursor cursor) {
		
		WorldLocationObject location = new WorldLocationObject();
		location.setSid(cursor.getString(0));
		location.setLevel(cursor.getString(1));
		location.setPosition(cursor.getString(2));
		location.setCreatedAt(cursor.getString(3));
		location.setCreatedMeta(cursor.getString(4));
		location.setUpdatedAt(cursor.getString(5));
		location.setUpdatedMeta(cursor.getString(6));
		location.setMeta(cursor.getString(7));
		location.setId(cursor.getString(8));
		location.setTitle(cursor.getString(9));
		location.setReleaseYear(cursor.getString(10));
		location.setLocations(cursor.getString(11));
		location.setFunFacts(cursor.getString(12));
		location.setProductionCompany(cursor.getString(13));
		location.setDistributor(cursor.getString(14));
		location.setDirector(cursor.getString(15));
		location.setWriter(cursor.getString(16));
		location.setActor1(cursor.getString(17));
		location.setActor2(cursor.getString(18));
		location.setActor3(cursor.getString(19));
		location.setLatitude(cursor.getString(20));
		location.setLongitude(cursor.getString(21));
		location.setStaticMapImageUrl(cursor.getString(22));
		location.setQuestionId(cursor.getString(23));
		location.setDateTime(cursor.getString(24));
		location.setQuestionText(cursor.getString(25));
		location.setAnswer1(cursor.getString(26));
		location.setAnswer2(cursor.getString(27));
		location.setAnswer3(cursor.getString(28));
		location.setAnswer4(cursor.getString(29));
		location.setReaction1(cursor.getString(30));
		location.setReaction2(cursor.getString(31));
		location.setReaction3(cursor.getString(32));
		location.setReaction4(cursor.getString(33));
		location.setWorldId(cursor.getString(34));
		location.setWorldTitle(cursor.getString(35));
		location.setSubmittedAnswerIndex(cursor.getString(36));
		location.setCorrectAnswerIndex(cursor.getString(37));
		location.setAnswered(cursor.getString(38));
		location.setActiveItem(cursor.getString(39));
		location.setActiveItem1(cursor.getString(40));
		location.setActiveItem2(cursor.getString(41));
		location.setActiveItem3(cursor.getString(42));
		location.setActiveItem4(cursor.getString(43));

		return location;
	}
}