package com.movie.locations.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movie.locations.domain.CheckIn;
import com.movie.locations.domain.ClassLoaderHelper;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

public class MovieLocationsImpl {

	private MovieLocationsSqliteHelper dbHelper;

	private SQLiteDatabase database;

	public final static String COLUMN_TABLE = "locations"; // name of table
	public final static String COLUMN_SID = "_sid";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LEVEL = "_level";
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
	public static Map<String, FilmLocation> LOCATION_MAP = new HashMap<String, FilmLocation>();

	// public static final String COLUMN_LOCATION = "location";
	private String[] allColumns = { COLUMN_ID, COLUMN_SID, COLUMN_LEVEL,
			COLUMN_POSITION, COLUMN_CREATED_AT, COLUMN_CREATED_META,
			COLUMN_UPDATED_AT, COLUMN_UPDATED_META, COLUMN_META, COLUMN_TITLE,
			COLUMN_RELEASE_YEAR, COLUMN_LOCATIONS, COLUMN_FUN_FACTS,
			COLUMN_PRODUCTION_COMPANY, COLUMN_DISTRIBUTOR, COLUMN_DIRECTOR,
			COLUMN_WRITER, COLUMN_ACTOR_1, COLUMN_ACTOR_2, COLUMN_ACTOR_3,
			COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_STATIC_MAP_IMAGE_URL };

	/**
	 * 
	 * @param context
	 */
	public MovieLocationsImpl(Context context) {
		dbHelper = new MovieLocationsSqliteHelper(context);
		database = dbHelper.getWritableDatabase();
	}

	public void delete() {
		database.delete(COLUMN_TABLE, null, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public FilmLocation createRecord(FilmLocation location) {
		// ContentValues values = new ContentValues();
		// values.put(COLUMN_ID, id);
		// values.put(COLUMN_LOCATION, name);
		// return database.insert(COLUMN_TABLE, null, values);

		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, location.getId());
		values.put(COLUMN_SID, location.getSid());
		values.put(COLUMN_LEVEL, location.getLevel());
		values.put(COLUMN_POSITION, location.getPosition());
		values.put(COLUMN_CREATED_AT, location.getCreatedAt());
		values.put(COLUMN_CREATED_META, location.getCreatedMeta());
		values.put(COLUMN_UPDATED_AT, location.getUpdatedAt());
		values.put(COLUMN_UPDATED_META, location.getUpdatedMeta());
		values.put(COLUMN_META, location.getMeta());
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

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(
				MovieLocationsSqliteHelper.TABLE_LOCATIONS, null, values);
		Cursor cursor = database.query(
				MovieLocationsSqliteHelper.TABLE_LOCATIONS, allColumns,
				COLUMN_ID + " = " + insertId, null, null, null, null);

		FilmLocation locationCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				locationCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		// ClassLoaderHelper.LOCATION_MAP.put(location.getSid(), location);

		return locationCursor;

	}

	public ArrayList<FilmLocation> selectRecords() {
		ArrayList<FilmLocation> locations = new ArrayList<FilmLocation>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, COLUMN_TABLE, allColumns, null,
				null, null, null, null, null);
		// String num;

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				FilmLocation location = cursorToComment(mCursor);
				locations.add(location);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return locations; // iterate to get each value.
	}


	public ArrayList<FilmLocation> selectRecordsByTitle(String title) {
		ArrayList<FilmLocation> locations = new ArrayList<FilmLocation>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, COLUMN_TABLE, allColumns, null,
				null, null, null, null, null);
		// String num;

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				FilmLocation location = cursorToComment(mCursor);
				
				if (location.getTitle().equals(title)) {
					locations.add(location);
				}
				
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return locations; // iterate to get each value.
	}
	
	public void deleteRecordsByTitle(String recordTitle) {
//
////		ContentValues quizObject = new ContentValues();
//		
//		ArrayList<FilmLocation> locations = new ArrayList<FilmLocation>();
//		
//		// String[] cols = new String[] { COLUMN_ID };
//		Cursor mCursor = database.query(true, COLUMN_TABLE, allColumns, null,
//				null, null, null, null, null);
//		// String num;
//
//		if (mCursor != null && mCursor.moveToFirst()) {
//			while (!mCursor.isAfterLast()) {
//				FilmLocation location = cursorToComment(mCursor);
//				
//				if (location.getTitle().equals(recordTitle)) {
//					locations.add(location);
//				}
//				
//				mCursor.moveToNext();
//			}
//			mCursor.close();
//		}
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] recordTitleArray = { recordTitle };
		database.delete(COLUMN_TABLE, COLUMN_TITLE + "=?", recordTitleArray);
	}
	
	public void deleteRecordsByLevel(String level) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] levelArray = { level };
		database.delete(COLUMN_TABLE, COLUMN_LEVEL + "=?", levelArray);
	}
	
	
	public FilmLocation selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(COLUMN_TABLE, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		FilmLocation location = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				location = cursorToComment(cursor);
			}
		}
		return location;
	}

	private FilmLocation cursorToComment(Cursor cursor) {
		FilmLocation location = new FilmLocation();

		// private String[] allColumns = { COLUMN_ID, COLUMN_SID,
		// COLUMN_POSITION,
		// COLUMN_CREATED_AT, COLUMN_CREATED_META, COLUMN_UPDATED_AT,
		// COLUMN_UPDATED_META, COLUMN_META, COLUMN_TITLE,
		// COLUMN_RELEASE_YEAR, COLUMN_LOCATIONS, COLUMN_FUN_FACTS,
		// COLUMN_PRODUCTION_COMPANY, COLUMN_PRODUCTION_COMPANY,
		// COLUMN_DISTRIBUTOR, COLUMN_DIRECTOR, COLUMN_WRITER, COLUMN_ACTOR_1,
		// COLUMN_ACTOR_2, COLUMN_ACTOR_3, COLUMN_LATITUDE, COLUMN_LONGITUDE };

		location.setId(cursor.getString(0));
		location.setSid(cursor.getString(1));
		location.setLevel(cursor.getString(2));
		location.setPosition(cursor.getString(3));
		location.setCreatedAt(cursor.getString(4));
		location.setCreatedMeta(cursor.getString(5));
		location.setUpdatedAt(cursor.getString(6));
		location.setUpdatedMeta(cursor.getString(7));
		location.setMeta(cursor.getString(8));
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
		return location;
	}
}