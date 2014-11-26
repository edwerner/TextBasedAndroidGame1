package com.movie.locations.domain;
import java.util.HashMap;
import java.util.Map;

public class StaticConstants {

	public static Map<String, String> staticConstantsMap;	
	public static enum FILM_ATTRIBUTES {
		SID,
		POSITION,
		CREATED_AT,
		CREATED_META,
		UPDATED_AT,
		UPDATED_META,
		META,
		ID,
		TITLE,
		RELEASE_YEAR,
		LOCATIONS,
		FUN_FACTS,
		PRODUCTION_COMPANY,
		DISTRIBUTOR,
		DIRECTOR,
		WRITER,
		ACTOR_1,
		ACTOR_2,
		ACTOR_3
	}
	
	public StaticConstants() {
		// empty constructor
	}
	
	public static void setStaticConstantsMap() {
		int index;
		staticConstantsMap = new HashMap<String, String>();
		for (FILM_ATTRIBUTES location : FILM_ATTRIBUTES.values()) {
			index = location.ordinal();
			staticConstantsMap.put(String.valueOf(index), location.toString());
		    index++;
		}
	}
	
	public static Map<String, String> getStaticConstantsMap() {
		return staticConstantsMap;
	}
}