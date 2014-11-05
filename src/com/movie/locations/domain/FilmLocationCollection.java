package com.movie.locations.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//import com.movie.locations.dao.MovieLocationsImpl;

public class FilmLocationCollection {

	private static ArrayList<FilmLocation> filmCollection1;
	private static ArrayList<FilmLocation> filmCollection2;
	private static LinkedHashMap<String, ArrayList<FilmLocation>> filmMap;
	private static LinkedHashMap<String, FilmLocation> titleMap;
	private static ArrayList<FilmLocation> tempList;
	public static List<String> filmList;

	// private MovieLocationsImpl datasource;

	// public FilmLocationCollection() {
	//
	// }
	
//	public static List<String> createFilmList(String title) {
//		filmList = new ArrayList<String>(filmMap.keySet());
////		filmList = new ArrayList<String>(filmMap.keySet());
//		filmList.add(title);
//		
//		return filmList;
//	}
	
	public static LinkedHashMap<String, FilmLocation> createFilmTitleMap(LinkedHashMap<String, ArrayList<FilmLocation>> filmLocationMap) {
		titleMap = new LinkedHashMap<String, FilmLocation>();
		
		Iterator<Entry<String, ArrayList<FilmLocation>>> iterator = filmLocationMap.entrySet().iterator();
		   
	    while (iterator.hasNext()) {
	        Map.Entry pairs = iterator.next();
//	        System.out.println("KEY: " + pairs.getKey() + " = " + pairs.getValue());
	        
	        @SuppressWarnings("unchecked")
			ArrayList<FilmLocation> locationObject = (ArrayList<FilmLocation>) pairs.getValue();
//	        locationObject = locationObject.get(0);
	        
//	        boolean match = false;
	        
	        for (FilmLocation location : locationObject) {
	        	
	        	
	        	if (!titleMap.containsKey(location.getTitle())) {
	        		titleMap.put(location.getTitle(), location);
//	        		System.out.println("LOCATION TITLE LOCATION MAPPED: " + location.getTitle());
	        	}
	        }
	        
	        
//	        iterator.remove(); // avoids a ConcurrentModificationException
	    }
	    return titleMap;
	}
	public static LinkedHashMap<String, ArrayList<FilmLocation>> createFilmLocationMap(ArrayList<FilmLocation> list) {
		// String collection = "collection";
		filmCollection1 = new ArrayList<FilmLocation>();
		// filmCollection2 = new ArrayList<FilmLocation>();

		filmMap = new LinkedHashMap<String, ArrayList<FilmLocation>>();
		tempList = new ArrayList<FilmLocation>();
//		ArrayList<FilmLocation> tempList = new ArrayList<FilmLocation>();
		String title = "title";

//		for (FilmLocation location : list) {
//			// System.out.println("********** FILM TITLE FROM COLLECTION ************ "
//			// + location.getTitle());
//			filmCollection1.add(location);
//		}
		
//		int counter = 0;
//		int prev = counter - 1;

		String currentLocation = null;
		ArrayList<FilmLocation> filmList = new ArrayList<FilmLocation>();
		
		for (FilmLocation location : list) {
			if (currentLocation != null) {
				if (location.getTitle().equals(currentLocation)) {
					filmList.add(location);
				} else {
					filmMap.put(currentLocation, filmList);
					currentLocation = null;
					filmList.clear();
				}
			} else {
				currentLocation = location.getTitle();
				filmList.add(location);
			}
//			tempList.add(location);
//			title = location.getTitle();
//			filmMap.put(title, tempList);
//			tempList.clear();
//			counter++;
		}
		
//		formatStuff(filmMap);
		return filmMap;
	}

//	public static void checkForTitleDuplicates() {
//		ArrayList<FilmLocation> list;
//
//		for (FilmLocation location : filmCollection) {
//			if (location.getTitle().equals(title)) {
//				// System.out.println("FOUND A TITLE DUPLICATE!" + title);
//				list = new ArrayList<FilmLocation>();
//				list.add(location);
//				tempList = list;
//			}
//			filmMap.put(title, tempList);
//		}
//		formatStuff(filmMap);
//	}

	public static void formatStuff(Map<String, ArrayList<FilmLocation>> filmMap) {
		@SuppressWarnings("rawtypes")
		Iterator iterator = filmMap.entrySet().iterator();
		while (iterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) iterator.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			iterator.remove(); // avoids a ConcurrentModificationException
		}
	}
}
