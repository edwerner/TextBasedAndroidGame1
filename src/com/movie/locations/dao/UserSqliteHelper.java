package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserSqliteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "users.db";
	private static final int DATABASE_VERSION = 1;
	private final String TABLE_USERS = "users";
	private final String COLUMN_ID = "_id";
	private final String COLUMN_USER_SID = "_usersid";
	private final String COLUMN_CLIENT_ID = "_clientid";
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
			+ COLUMN_CLIENT_ID + " text, " 
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

	public UserSqliteHelper(Context context) {
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
}