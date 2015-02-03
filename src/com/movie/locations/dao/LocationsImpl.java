package com.movie.locations.dao;
import java.util.ArrayList;
import com.movie.locations.domain.FilmLocation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationsImpl extends SQLiteOpenHelper implements DatabaseImpl {

	private SQLiteDatabase database;
	private final static String DATABASE_NAME = "locations.db";
	private static final int DATABASE_VERSION = 5;
	private final static String TABLE_NAME = "locations"; // name of table
	private final static String COLUMN_SID = "_sid";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_LEVEL = "_level";
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
	public LocationsImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" + COLUMN_ID
			+ " text, " + COLUMN_SID
			+ " text, " + COLUMN_LEVEL
			+ " text, " + COLUMN_POSITION
			+ " text, " + COLUMN_TITLE
			+ " text, " + COLUMN_LOCATIONS
			+ " text, " + COLUMN_FUN_FACTS
			+ " text, " + COLUMN_STATIC_MAP_IMAGE_URL
			+ " text, " + COLUMN_FUN_FACTS_IMAGE_URL
			+ " text, " + COLUMN_FUN_FACTS_TITLE
			+ " text);";

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
		database.execSQL("DROP TABLE IF EXISTS locations");
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
				TABLE_NAME, null, values);
		Cursor cursor = database.query(
				TABLE_NAME, allColumns,
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
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				FilmLocation location = cursorToComment(mCursor);
				locations.add(location);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return locations;
	}

	public ArrayList<FilmLocation> selectRecordsByTitle(String title) {
		ArrayList<FilmLocation> locations = new ArrayList<FilmLocation>();
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

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
		return locations;
	}
	
	public void deleteRecordByTitle(String recordTitle) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] recordTitleArray = { recordTitle };
		database.delete(TABLE_NAME, COLUMN_TITLE + "=?", recordTitleArray);
	}
	
	@Override
	public void deleteRecordByLevel(String level) {
		
		// DELETE ALL DATABASE RECORDS WITH MATCHING LOCATION TITLE
		String[] levelArray = { level };
		database.delete(TABLE_NAME, COLUMN_LEVEL + "=?", levelArray);
	}
	
	public FilmLocation selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
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
		location.setLocations(cursor.getString(5));
		location.setFunFacts(cursor.getString(6));
		location.setStaticMapImageUrl(cursor.getString(7));
		location.setFunFactsImageUrl(cursor.getString(8));
		location.setFunFactsTitle(cursor.getString(9));
		return location;
	}

	@Override
	public void deleteRecordById(String string) {
		// TODO Auto-generated method stub
		
	}
}