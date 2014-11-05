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
import com.movie.locations.util.SessionIdentifierGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

public class LocationCheckinImpl {

	private LocationCheckinSqliteHelper dbHelper;

	private SQLiteDatabase database;

	public final static String TABLE_NAME = "checkins"; // name of table
	public static final String COLUMN_ID = "_id";
	public final static String COLUMN_DATETIME = "_datetime";
	public final static String COLUMN_LOCATION = "_location";
	public final static String COLUMN_USER_ID = "_userid";
	public final static String COLUMN_FILM_TITLE = "_fimtitle";
	public static Map<String, CheckIn> CHECKIN_MAP = new HashMap<String, CheckIn>();

	// public static final String COLUMN_ID = "_id";
	// public final static String COLUMN_DATETIME = "_datetime";
	// public final static String COLUMN_LOCATION = "_location";
	// public final static String COLUMN_USER_ID = "_userid";
	// public final static String COLUMN_FILM_TITLE = "_fimtitle";

	// public static final String COLUMN_LOCATION = "location";
	private String[] allColumns = { COLUMN_ID, COLUMN_DATETIME,
			COLUMN_LOCATION, COLUMN_USER_ID, COLUMN_FILM_TITLE };

	/**
	 * 
	 * @param context
	 */
	public LocationCheckinImpl(Context context) {
		dbHelper = new LocationCheckinSqliteHelper(context);
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

	public CheckIn createRecord(CheckIn checkin) {
		// ContentValues values = new ContentValues();
		// values.put(COLUMN_ID, id);
		// values.put(COLUMN_LOCATION, name);
		// return database.insert(COLUMN_TABLE, null, values);

		// THIS SHOULD BE HANDLED DURING PERSISTENCE
		// SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
		// String format = s.format(new Date());
		// checkin.setDatetime(datetime);

		// SimpleDateFormat checkinDate = new
		// SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		String formattedDate = DateFormat.getDateTimeInstance().toString();
		ContentValues values = new ContentValues();

		// generating a device-only unique check-in
		// id here this check-in will be sent to the
		// server-side rest api and will be persisted there
//		final String UNIQUE_CHECKIN_ID = formattedDate + "::"
//				+ checkin.getFilmTitle() + "::" + checkin.getFilmLocation()
//				+ "::" + checkin.getUser().getUserId();

		values.put(COLUMN_ID, checkin.getCheckinId());
		values.put(COLUMN_DATETIME, checkin.getDatetime());
		values.put(COLUMN_LOCATION, checkin.getFilmLocation());
		values.put(COLUMN_USER_ID, checkin.getUser().getUserId());
		values.put(COLUMN_FILM_TITLE, checkin.getFilmTitle());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(
				LocationCheckinSqliteHelper.TABLE_CHECKINS, null, values);
		Cursor cursor = database.query(
				LocationCheckinSqliteHelper.TABLE_CHECKINS, allColumns,
				COLUMN_ID + " = " + insertId, null, null, null, null);
		CheckIn checkinCursor = null;
		
		if (cursor != null) {
			// lazy evaluation
		    if (cursor.moveToFirst()) {
		    	checkinCursor = cursorToComment(cursor);
		    }
		    cursor.close();
		}
				
		
//		cursor.moveToFirst();
		// Comment newComment = cursorToComment(cursor);
//		CheckIn checkinCursor = cursorToComment(cursor);
//		cursor.close();

		// create secure random instance here
		//
		// SessionIdentifierGenerator
		// SessionIdentifierGenerator sessionIdGenerator = new
		// SessionIdentifierGenerator();
//		ClassLoaderHelper.LOCATION_CHECKIN_MAP.put(UNIQUE_CHECKIN_ID, checkin);

		return checkinCursor;

		// final class SessionIdentifierGenerator {
		// private SecureRandom random = new SecureRandom();
		//
		// public String nextSessionId() {
		// return new BigInteger(130, random).toString(32);
		// }
		// }
	}
	
//	// just one
//	Cursor cursor = db.query(...);
//	if (cursor != null) {
//	    if (cursor.moveToFirst()) {
//	        value = cursor.getSomething();
//	    }
//	    cursor.close();
//	}
//
//	// multiple columns
//	Cursor cursor = db.query(...);
//	if (cursor != null) {
//	    while (cursor.moveToNext()) {
//	        values.add(cursor.getSomething());
//	    }
//	    cursor.close();
//	}

	public ArrayList<CheckIn> selectRecords() {
		ArrayList<CheckIn> checkins = new ArrayList<CheckIn>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);
		
		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				CheckIn checkin = cursorToComment(mCursor);
				checkins.add(checkin);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return checkins; // iterate to get each value.
	}

	private CheckIn cursorToComment(Cursor cursor) {
		CheckIn checkin = new CheckIn();

		// TODO: create check-in domain object

		checkin.setCheckinId(cursor.getString(0));
		checkin.setDatetime(cursor.getString(1));
		checkin.setFilmLocation(cursor.getString(2));
		checkin.setUserId(cursor.getString(3));
		checkin.setFilmTitle(cursor.getString(4));
		
		return checkin;
	}
}