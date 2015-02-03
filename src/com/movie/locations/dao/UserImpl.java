package com.movie.locations.dao;
import java.util.ArrayList;
import com.movie.locations.domain.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserImpl extends SQLiteOpenHelper implements DatabaseImpl {

	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "users.db";
	private static final int DATABASE_VERSION = 1;
	private final String TABLE_USERS = "users";
	private final String COLUMN_ID = "_id";
	private final String COLUMN_USER_SID = "_usersid";
	private final String COLUMN_USER_CLIENT_ID = "_clientid";
	private final String COLUMN_DISPLAY_NAME = "_displayname";
	private final String COLUMN_EMAIL_ADDRESS = "_emailaddress";
	private final String COLUMN_AVATAR_IMAGE_URL = "_avatarimageurl";
	private final String COLUMN_CURRENT_LEVEL = "_currentlevel";
	private final String COLUMN_ANSWER_CURRENT_POINTS = "_currentpoints";
	private final String COLUMN_POINTS_USER_ID = "_pointsuserid";
	private final String COLUMN_POINTS = "_points";
	private final String COLUMN_BONUS_POINTS = "_bonuspoints";
	private final String COLUMN_WORLD_COUNT = "_worldcount";
	private final String COLUMN_EMAIL_NOTIFICATIONS = "_emailnotifications";
	private final String COLUMN_MOBILE_NOTIFICATIONS = "_mobilenotifications";
	
	// Database creation sql statement
	private final String DATABASE_CREATE = "create table "
			+ TABLE_USERS + "(" 
			+ COLUMN_ID + " text, "
			+ COLUMN_USER_SID + " text, " 
			+ COLUMN_USER_CLIENT_ID + " text, " 
			+ COLUMN_DISPLAY_NAME + " text, "
			+ COLUMN_EMAIL_ADDRESS + " text, "
			+ COLUMN_AVATAR_IMAGE_URL + " text, "
			+ COLUMN_CURRENT_LEVEL + " text, "
			+ COLUMN_ANSWER_CURRENT_POINTS + " text, "
			+ COLUMN_POINTS_USER_ID + " text, "
			+ COLUMN_POINTS + " text, "
			+ COLUMN_BONUS_POINTS + " text, "
			+ COLUMN_WORLD_COUNT + " text, "
			+ COLUMN_EMAIL_NOTIFICATIONS + " text, "
			+ COLUMN_MOBILE_NOTIFICATIONS + " text);";

	public UserImpl(Context context) {
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
		database.execSQL("DROP TABLE IF EXISTS users");
		onCreate(database);
	}
	
	private String[] allColumns = { COLUMN_ID, COLUMN_USER_SID,
			COLUMN_USER_CLIENT_ID, COLUMN_DISPLAY_NAME, COLUMN_EMAIL_ADDRESS,
			COLUMN_AVATAR_IMAGE_URL, COLUMN_CURRENT_LEVEL,
			COLUMN_ANSWER_CURRENT_POINTS, COLUMN_POINTS_USER_ID, COLUMN_POINTS,
			COLUMN_BONUS_POINTS, COLUMN_WORLD_COUNT,
			COLUMN_EMAIL_NOTIFICATIONS, COLUMN_MOBILE_NOTIFICATIONS };

	@Override
	public void delete() {
		database.delete(TABLE_USERS, null, null);
	}

	@Override
	public void open() throws SQLException {
		database = this.getWritableDatabase();
	}

	@Override
	public void close() {
		database.close();
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

		long insertId = database.insert(TABLE_USERS, null,
				values);
		Cursor cursor = database.query(TABLE_USERS,
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

	public void updateRecordBonusPointsValue(String recordId,
			String updatedPoints, String updatedBonusPoints) {
		ContentValues pointsObject = new ContentValues();

		if (updatedPoints != null) {
			pointsObject.put(COLUMN_ANSWER_CURRENT_POINTS, updatedPoints);
		}

		if (updatedBonusPoints != null) {
			pointsObject.put(COLUMN_BONUS_POINTS, updatedBonusPoints);
		}

		database.update(TABLE_USERS, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}
	
	public void updateUserNotificationPreferences(String recordId, 
			String updatedEmailNotifications, 
			String updatedMobileNotifications) {
		ContentValues pointsObject = new ContentValues();

		if (updatedEmailNotifications != null) {
			pointsObject.put(COLUMN_EMAIL_NOTIFICATIONS, updatedEmailNotifications);
		}
		
		if (updatedMobileNotifications != null) {
			pointsObject.put(COLUMN_MOBILE_NOTIFICATIONS, updatedMobileNotifications);
		}

		database.update(TABLE_USERS, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}

	public void updateWorldCount(String recordId, String updatedWorldCount) {
		ContentValues pointsObject = new ContentValues();

		if (updatedWorldCount != null) {
			pointsObject.put(COLUMN_WORLD_COUNT, updatedWorldCount);
		}

		database.update(TABLE_USERS, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}

	public User selectRecordById(String string) throws SQLException {
		String[] recordIdArray = { string };
		Cursor cursor = database.query(TABLE_USERS, allColumns,
				COLUMN_ID + "=?", recordIdArray, null, null, null, null);
		User user = null;
		if (cursor != null) {
			// lazy evaluation
			if (cursor.moveToFirst()) {
				user = cursorToComment(cursor);
			}
		}
		return user;
	}

	public ArrayList<User> selectRecords() {
		ArrayList<User> userList = new ArrayList<User>();
		Cursor mCursor = database.query(true, TABLE_USERS, allColumns, null,
				null, null, null, null, null);

		if (mCursor != null && mCursor.moveToFirst()) {
			while (!mCursor.isAfterLast()) {
				User user = cursorToComment(mCursor);
				userList.add(user);
				mCursor.moveToNext();
			}
			mCursor.close();
		}
		return userList;
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

		return user;
	}

	public void updateCurrentUserLevel(String recordId, String currentLevelCheck) {

		ContentValues pointsObject = new ContentValues();

		if (currentLevelCheck != null) {
			pointsObject.put(COLUMN_CURRENT_LEVEL, currentLevelCheck);
		}

		database.update(TABLE_USERS, pointsObject, COLUMN_ID + "=" + "'"
				+ recordId + "'", null);
	}

	@Override
	public void deleteRecordById(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRecordByLevel(String level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRecordByTitle(String recordTitle) {
		// TODO Auto-generated method stub
		
	}
}