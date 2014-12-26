package com.movie.locations.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.movie.locations.domain.FilmLocation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MovieLocationsImpl {

	private MovieLocationsSqliteHelper dbHelper;
	private SQLiteDatabase database;
	private final static String COLUMN_TABLE = "locations"; // name of table
	private final static String COLUMN_SID = "_sid";
	private static final String COLUMN_ID = "_id";
	public static final String COLUMN_LEVEL = "_level";
	private final static String COLUMN_POSITION = "_position";
	private final static String COLUMN_TITLE = "_title";
	private final static String COLUMN_LOCATIONS = "_locations";
	private final static String COLUMN_FUN_FACTS = "_funfacts";
	private final static String COLUMN_STATIC_MAP_IMAGE_URL = "_staticmapimageurl";
	private final static String COLUMN_FUN_FACTS_IMAGE_URL = "_funFactsImageUrl";
	private final static String COLUMN_FUN_FACTS_TITLE = "_funFactsTitle";

	// public static final String COLUMN_LOCATION = "location";
	private String[] allColumns = { COLUMN_ID, COLUMN_SID, COLUMN_LEVEL,
			COLUMN_POSITION, COLUMN_TITLE, COLUMN_LOCATIONS, COLUMN_FUN_FACTS, COLUMN_STATIC_MAP_IMAGE_URL, COLUMN_FUN_FACTS_IMAGE_URL, COLUMN_FUN_FACTS_TITLE };

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
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, location.getId());
		values.put(COLUMN_SID, location.getSid());
		values.put(COLUMN_LEVEL, location.getLevel());
		values.put(COLUMN_POSITION, location.getPosition());
		values.put(COLUMN_TITLE, location.getTitle());
		values.put(COLUMN_LOCATIONS, location.getLocations());
		values.put(COLUMN_FUN_FACTS, location.getFunFacts());
		values.put(COLUMN_STATIC_MAP_IMAGE_URL, location.getStaticMapImageUrl());
		values.put(COLUMN_FUN_FACTS_IMAGE_URL, location.getFunFactsImageUrl());
		values.put(COLUMN_FUN_FACTS_TITLE, location.getFunFactsTitle());

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
		location.setSid(cursor.getString(0));
		location.setLevel(cursor.getString(1));
		location.setPosition(cursor.getString(2));
		location.setId(cursor.getString(3));
		location.setTitle(cursor.getString(4));
//		System.out.println("SET TITLE: " + cursor.getString(4));
		location.setLocations(cursor.getString(5));
		location.setFunFacts(cursor.getString(6));
		location.setStaticMapImageUrl(cursor.getString(7));
		location.setFunFactsImageUrl(cursor.getString(8));
		location.setFunFactsTitle(cursor.getString(9));
		return location;
	}
}