package com.movie.locations.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticConstants {
//	public final int SID = 0;
//	public final int POSITION = 1;
//	public final int CREATED_AT = 2;
//	public final int CREATED_META = 3;
//	public final int UPDATED_AT = 4;
//	public final int UPDATED_META = 5;
//	public final int META = 6;
//	public final int ID = 7;
//	public final int TITLE = 8;
//	public final int RELEASE_YEAR = 9;
//	public final int LOCATIONS = 10;
//	public final int FUN_FACTS = 11;
//	public final int PRODUCTION_COMPANY = 12;
//	public final int DISTRIBUTOR = 13;
//	public final int DIRECTOR = 14;
//	public final int WRITER = 15;
//	public final int ACTOR_1 = 16;
//	public final int ACTOR_2 = 17;
//	public final int ACTOR_3 = 18;
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
