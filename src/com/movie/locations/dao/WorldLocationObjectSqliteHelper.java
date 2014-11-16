package com.movie.locations.dao;

import java.util.HashMap;
import java.util.Map;

import com.movie.locations.domain.WorldLocationObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorldLocationObjectSqliteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "worldlocationitems.db";
	public static final String TABLE_WORLD_LOCATION_OBJECT_ITEMS = "worldlocationitems";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SID = "_sid";
	public final static String COLUMN_DATETIME = "_datetime";
	public final static String COLUMN_QUESTION_TEXT = "_questiontext";
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
	

//	pc.writeString(position);
//	pc.writeString(createdAt);
//	pc.writeString(createdMeta);
//	pc.writeString(updatedAt);
//	pc.writeString(updatedMeta);
//	pc.writeString(meta);
//	pc.writeString(id);
//	pc.writeString(title);
//	pc.writeString(releaseYear);
//	pc.writeString(locations);
//	pc.writeString(funFacts);
//	pc.writeString(productionCompany);
//	pc.writeString(distributor);
//	pc.writeString(director);
//	pc.writeString(writer);
//	pc.writeString(actor1);
//	pc.writeString(actor2);
//	pc.writeString(actor3);
//	pc.writeString(latitude);
//	pc.writeString(longitude);
//	pc.writeString(staticMapImageUrl);
//	pc.writeString(questionId);
////	pc.writeString(filmTitle);
//	pc.writeString(dateTime);
//	pc.writeString(questionText);
//	pc.writeString(answer1);
//	pc.writeString(answer2);
//	pc.writeString(answer3);
//	pc.writeString(answer4);
//	pc.writeString(reaction1);
//	pc.writeString(reaction2);
//	pc.writeString(reaction3);
//	pc.writeString(reaction4);
//	pc.writeString(worldId);
//	pc.writeString(worldTitle);
//	pc.writeString(submittedAnswerIndex);
//	pc.writeValue(correctAnswerIndex); // not set on client	
////	pc.writeInt(correctAnswerIndex);
//	pc.writeString(answered);
//	pc.writeString(activeItem);
//	pc.writeString(activeItem1);
//	pc.writeString(activeItem2);
//	pc.writeString(activeItem3);
//	pc.writeString(activeItem4);
//	pc.writeString(level);

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_WORLD_LOCATION_OBJECT_ITEMS + "(" 
			+ COLUMN_ID + " text, " 
			+ COLUMN_SID + " text, "
			+ COLUMN_DATETIME + " text, "
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
			+ COLUMN_POSITION + " text, "
			+ COLUMN_CREATED_AT + " text, "
			+ COLUMN_CREATED_META + " text, "
			+ COLUMN_UPDATED_AT + " text, "
			+ COLUMN_UPDATED_META + " text, "
			+ COLUMN_META + " text, "
			+ COLUMN_TITLE + " text, "
			+ COLUMN_RELEASE_YEAR + " text, "
			+ COLUMN_LOCATIONS + " text, "
			+ COLUMN_FUN_FACTS + " text, "
			+ COLUMN_PRODUCTION_COMPANY + " text, "
			+ COLUMN_PRODUCTION_COMPANY + " text, "
			+ COLUMN_DIRECTOR + " text, "
			+ COLUMN_WRITER + " text, "
			+ COLUMN_ACTOR_1 + " text, "
			+ COLUMN_ACTOR_1 + " text, "
			+ COLUMN_ACTOR_3 + " text, "
			+ COLUMN_LATITUDE + " text, "
			+ COLUMN_LONGITUDE + " text, "
			+ COLUMN_STATIC_MAP_IMAGE_URL + " text);";

	public WorldLocationObjectSqliteHelper(Context context) {
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