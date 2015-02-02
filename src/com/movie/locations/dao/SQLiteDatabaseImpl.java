package com.movie.locations.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDatabaseImpl extends SQLiteOpenHelper {
	
	private String tableName = "undefined";
	private static String databaseName = "undefined";
	private static int databaseVersion = 1;
	private String[] databaseColumns = null;

	public void setDatabaseColumns(String[] allColumns) {
		this.databaseColumns = allColumns;
	}
	
	// Database creation sql statement
	private String createDatabase(String[] databaseColumns) {

		String tempTable = "create table " + getTableName() + "(";
		String postFix = "";
		
		for (int i = 0; i < databaseColumns.length; i++) {
			if (i == databaseColumns.length -1) {
				postFix = " text);";
			} else {
				postFix = " text, ";
			}
			String entry = databaseColumns[i] + postFix;
			tempTable += entry;	
		}
		return tempTable;
	}

	public SQLiteDatabaseImpl(Context context) {
		super(context, databaseName, null, databaseVersion);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(createDatabase(databaseColumns));
	}

	// Method is called during an upgrade of the database,
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w("Database", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + getTableName());
		onCreate(database);
	}

	public static void setDatabaseVersion(int databaseVersion) {
		SQLiteDatabaseImpl.databaseVersion = databaseVersion;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public static int getDatabaseVersion() {
		return databaseVersion;
	}
}
