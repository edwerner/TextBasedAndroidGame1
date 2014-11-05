//package com.movie.locations.application;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//
//import org.codehaus.jackson.JsonParseException;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.movie.locations.R;
//import com.movie.locations.dao.MovieLocationsImpl;
//import com.movie.locations.dao.QuizItemImpl;
//import com.movie.locations.domain.FilmLocation;
//import com.movie.locations.domain.QuizItem;
//import com.movie.locations.domain.WorldLocationArrayList;
//import com.movie.locations.domain.WorldLocationObject;
//import com.movie.locations.service.WorldLocationService;
//
//public class ArrayAdapterBuilder extends ArrayAdapter<WorldLocationObject> {
//
//	private MovieLocationsImpl datasource;
//	private LinkedHashMap<String, QuizItem> quizMap;
//	private LinkedHashMap<String, FilmLocation> locationMap;
//	private ArrayList<WorldLocationObject> worldLocationObjectList;
//	private ArrayList<FilmLocation> locationList;
//	private ArrayList<QuizItem> quizList;
//	private WorldLocationService worldLocationService = new WorldLocationService();
//	private QuizItemImpl quizItemImpl;
//	private MovieLocationsImpl locationImpl;
//	private WorldLocationArrayList list;
//	private Context context;
//	private Intent intent;
//	private ArrayList<WorldLocationObject> values;
//
////	public ArrayAdapterBuilder<WorldLocationObject>() {
////		
////	}
//	
//	public ArrayAdapterBuilder(Context context, int resource, Intent intent, ArrayList<WorldLocationObject> values) {
//		super(context, resource);
//		// TODO Auto-generated constructor stub
//		this.context = context;
//		this.intent = intent;
//		this.values = values;
//	}
//	
//	public void rebuildArrayAdapter(Context context, Intent intent) {
//		
//		quizItemImpl = new QuizItemImpl(context);
//		locationImpl = new MovieLocationsImpl(context);
//		
//		quizList = quizItemImpl.selectRecords();
//		
//		
//		try {
//			worldLocationObjectList = worldLocationService.buildWorldLocationObjects(context, quizList, locationList);
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		worldLocationList.
//		
//
////		
////		list = new WorldLocationArrayList();
//////		list.setClassLoder(classLoader);
////		list.setWorldLocationList(worldLocationList);
////		
//		Bundle extras = intent.getExtras();
////		worldLocationList = extras.getParcelable("worldLocationList");
//		WorldLocationArrayAdapter locationQuizItemAdapter = new WorldLocationArrayAdapter(context, intent, worldLocationObjectList);
//		ListView locationsList = (ListView) findViewById(R.id.locationsView1);
//		locationsList.setAdapter(locationQuizItemAdapter);
//		
////		locationQuizItemAdapter = new WorldLocationArrayAdapter(this, intent, worldLocationList.getWorldLocationList()); 
//	}
//
//}
