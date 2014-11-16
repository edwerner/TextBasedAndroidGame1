package com.movie.locations.domain;

import java.util.HashMap;
import java.util.Map;

public class ClassLoaderHelper {

	private static ClassLoader classLoader;

	public static Map<String, FilmLocation> LOCATION_MAP = new HashMap<String, FilmLocation>();
	public static Map<String, CheckIn> LOCATION_CHECKIN_MAP = new HashMap<String, CheckIn>();
	
	public static void setClassLoader(ClassLoader loader) {
		classLoader = loader;
	}

	public static ClassLoader getClassLoader() {
		return classLoader;
	}
}
