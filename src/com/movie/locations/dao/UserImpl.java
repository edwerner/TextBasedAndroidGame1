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
import com.movie.locations.domain.User;
import com.movie.locations.util.SessionIdentifierGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

public class UserImpl {

	private UserSqliteHelper dbHelper;

	private SQLiteDatabase database;

	public final static String TABLE_NAME = "users"; // name of table
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USER_SID = "_usersid";
	public static final String COLUMN_USER_CLIENT_ID = "_clientid";
	public final static String COLUMN_DISPLAY_NAME = "_displayname";
	public final static String COLUMN_EMAIL_ADDRESS = "_emailaddress";
	public final static String COLUMN_AVATAR_IMAGE_URL = "_avatarimageurl";
	public final static String COLUMN_CURRENT_LEVEL = "_currentlevel";
	public final static String COLUMN_ANSWER_CURRENT_POINTS = "_currentpoints";
	public static final String COLUMN_POINTS_USER_ID = "_pointsuserid";
	public static final String COLUMN_POINTS = "_points";
	public static final String COLUMN_BONUS_POINTS = "_bonuspoints";
	public static final String COLUMN_WORLD_COUNT = "_worldcount";
	public static final String COLUMN_EMAIL_NOTIFICATIONS = "_emailnotifications";
	public static final String COLUMN_MOBILE_NOTIFICATIONS = "_mobilenotifications";

	public static Map<String, QuizItem> QUIZ_ITEM_MAP = new HashMap<String, QuizItem>();
	private String[] allColumns = { COLUMN_ID, COLUMN_USER_SID,
			COLUMN_USER_CLIENT_ID, COLUMN_DISPLAY_NAME, COLUMN_EMAIL_ADDRESS,
			COLUMN_AVATAR_IMAGE_URL, COLUMN_CURRENT_LEVEL,
			COLUMN_ANSWER_CURRENT_POINTS, COLUMN_POINTS_USER_ID, COLUMN_POINTS,
			COLUMN_BONUS_POINTS, COLUMN_WORLD_COUNT,
			COLUMN_EMAIL_NOTIFICATIONS, COLUMN_MOBILE_NOTIFICATIONS };

	/**
	 * 
	 * @param context
	 */
	public UserImpl(Context context) {
		dbHelper = new UserSqliteHelper(context);
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

	public User createRecord(User user) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, user.getUserId());
		values.put(COLUMN_USER_SID, user.getUserSid());
		values.put(COLUMN_USER_CLIENT_ID, user.getUserClientId());
		values.put(COLUMN_DISPLAY_NAME, user.getDisplayName());
		values.put(COLUMN_EMAIL_ADDRESS, user.getEmailAddress());
		values.put(COLUMN_AVATAR_IMAGE_URL, user.getAvatarImageUrl());
		values.put(COLUMN_CURRENT_LEVEL, user.getCurrentLevel());
		values.put(COLUMN_ANSWER_CURRENT_POINTS, user.getCurrentPoints());
		values.put(COLUMN_POINTS_USER_ID, user.getPointsUserId());
		values.put(COLUMN_POINTS, user.getPoints());
		values.put(COLUMN_BONUS_POINTS, user.getPoints());
		values.put(COLUMN_WORLD_COUNT, user.getWorldCount());
		values.put(COLUMN_EMAIL_NOTIFICATIONS, user.getEmailNotifications());
		values.put(COLUMN_MOBILE_NOTIFICATIONS, user.getMobileNotifications());

		// values.put(MovieLocationsSqliteHelper.COLUMN_ID,
		// Integer.parseInt(id));

		long insertId = database.insert(UserSqliteHelper.TABLE_USERS, null,
				values);
		Cursor cursor = database.query(UserSqliteHelper.TABLE_USERS,
				allColumns, COLUMN_ID + " = " + insertId, null, null, null,
				null);
		User userCursor = null;

		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				userCursor = cursorToComment(cursor);
			}
			cursor.close();
		}

		return userCursor;
	}

	// // updatedPoints, updatedBonusPoints
	// public void updateRecordBonusPointsValue(String recordId, String
	// updatedPoints, String updatedBonusPoints) {
	// ContentValues pointsObject = new ContentValues();
	//
	// if (updatedPoints != null) {
	// pointsObject.put(COLUMN_POINTS, updatedPoints);
	// }
	//
	// if (updatedBonusPoints != null) {
	// pointsObject.put(COLUMN_BONUS_POINTS, updatedBonusPoints);
	// }
	//
	// database.update(TABLE_NAME, pointsObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	// updatedPoints, updatedBonusPoints
	public void updateRecordBonusPointsValue(String recordId,
			String updatedPoints, String updatedBonusPoints) {
		ContentValues pointsObject = new ContentValues();

		if (updatedPoints != null) {
			// pointsObject.put(COLUMN_POINTS, updatedPoints);
			pointsObject.put(COLUMN_ANSWER_CURRENT_POINTS, updatedPoints);
		}

		if (updatedBonusPoints != null) {
			pointsObject.put(COLUMN_BONUS_POINTS, updatedBonusPoints);
		}

		database.update(TABLE_NAME, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}
	
	public void updateUserNotificationPreferences(String recordId, String updatedEmailNotifications, String updatedMobileNotifications) {
		ContentValues pointsObject = new ContentValues();

		if (updatedEmailNotifications != null) {
			pointsObject.put(COLUMN_EMAIL_NOTIFICATIONS, updatedEmailNotifications);
		}
		
		if (updatedMobileNotifications != null) {
			pointsObject.put(COLUMN_MOBILE_NOTIFICATIONS, updatedMobileNotifications);
		}

		database.update(TABLE_NAME, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}

	// updatedPoints, updatedBonusPoints
	public void updateWorldCount(String recordId, String updatedWorldCount) {
		ContentValues pointsObject = new ContentValues();

		if (updatedWorldCount != null) {
			pointsObject.put(COLUMN_WORLD_COUNT, updatedWorldCount);
		}

		database.update(TABLE_NAME, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}

	public User selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_NAME, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		User user = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				user = cursorToComment(cursor);
			}
			// cursor.moveToFirst();
			// user = cursorToComment(cursor);
		}
		return user;
	}

	// public void updateRecord(String recordId, String answered)
	// throws SQLException {
	// ContentValues quizObject = new ContentValues();
	// quizObject.put(COLUMN_ANSWERED, answered);
	// database.update(TABLE_NAME, quizObject, COLUMN_ID + "=" + "'"
	// + recordId + "'", null);
	// }

	public ArrayList<User> selectRecords() {
		ArrayList<User> userList = new ArrayList<User>();
		// String[] cols = new String[] { COLUMN_ID };
		Cursor mCursor = database.query(true, TABLE_NAME, allColumns, null,
				null, null, null, null, null);

		// String num;
		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				User user = cursorToComment(mCursor);
				userList.add(user);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return userList; // iterate to get each value.
	}

	private User cursorToComment(Cursor cursor) {

		User user = new User();
		user.setUserId(cursor.getString(0));
		user.setUserSid(cursor.getString(1));
		user.setUserClientId(cursor.getString(2));
		user.setDisplayName(cursor.getString(3));
		user.setEmailAddress(cursor.getString(4));
		user.setAvatarImageUrl(cursor.getString(5));
		user.setCurrentLevel(cursor.getString(6));
		user.setCurrentPoints(cursor.getString(7));
		user.setPointsUserId(cursor.getString(8));
		user.setPoints(cursor.getString(9));
		user.setBonusPoints(cursor.getString(10));
		user.setWorldCount(cursor.getString(11));
		user.setEmailNotifications(cursor.getString(12));
		user.setMobileNotifications(cursor.getString(13));

		// values.put(COLUMN_ID, user.getUserId());
		// values.put(COLUMN_USER_SID, user.getUserSid());
		// values.put(COLUMN_USER_CLIENT_ID, user.getUserClientId());
		// values.put(COLUMN_DISPLAY_NAME, user.getDisplayName());
		// values.put(COLUMN_EMAIL_ADDRESS, user.getEmailAddress());
		// values.put(COLUMN_AVATAR_IMAGE_URL, user.getAvatarImageUrl());
		// values.put(COLUMN_CURRENT_LEVEL, user.getCurrentLevel());
		// values.put(COLUMN_ANSWER_CURRENT_POINTS, user.getCurrentPoints());
		// values.put(COLUMN_POINTS_USER_ID, user.getPointsUserId());
		// values.put(COLUMN_POINTS, user.getPoints());
		// values.put(COLUMN_BONUS_POINTS, user.getPoints());
		// values.put(COLUMN_WORLD_COUNT, user.getWorldCount());

		return user;
	}

	public void updateCurrentUserLevel(String recordId, String currentLevelCheck) {

		ContentValues pointsObject = new ContentValues();

		if (currentLevelCheck != null) {
			pointsObject.put(COLUMN_CURRENT_LEVEL, currentLevelCheck);
		}

		database.update(TABLE_NAME, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);

	}
}