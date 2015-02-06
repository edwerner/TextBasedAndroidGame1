package com.movie.locations.database;

import android.database.sqlite.SQLiteDatabase;

interface IDatabase {
	public void onCreate(SQLiteDatabase database);
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion);
	public void delete();
	public void open();
	public void close();
	public void deleteRecordById(String string);
	public void deleteRecordByLevel(String level);
	public void deleteRecordByTitle(String recordTitle);
}