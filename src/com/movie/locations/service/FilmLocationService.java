package com.movie.locations.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import android.content.Context;

import com.movie.locations.dao.LocationsImpl;
import com.movie.locations.domain.FilmLocationDataObject;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.util.CSVFile;

public class FilmLocationService {

	private final int SID = 0;
	private final int POSITION = 1;
	private final int CREATED_AT = 2;
	private final int CREATED_META = 3;
	private final int UPDATED_AT = 4;
	private final int UPDATED_META = 5;
	private final int META = 6;
	private final int ID = 7;
	private final int TITLE = 8;
	private final int RELEASE_YEAR = 9;
	private final int LOCATIONS = 10;
	private final int FUN_FACTS = 11;
	private final int PRODUCTION_COMPANY = 12;
	private final int DISTRIBUTOR = 13;
	private final int DIRECTOR = 14;
	private final int WRITER = 15;
	private final int ACTOR_1 = 16;
	private final int ACTOR_2 = 17;
	private final int ACTOR_3 = 18;
	private ArrayList<FilmLocation> filmList;
	private FilmLocationDataObject filmLocations;
	public JsonNode LOCATION_JSON;
	private Map<String, FilmLocation> filmData;
	
	public FilmLocationService() {
		// empty constructor
	}
	
	public JsonNode createJsonNode(String msg) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory(); 
		JsonParser parser = factory.createJsonParser(msg);
		JsonNode locationJson = mapper.readTree(parser);
		return locationJson;
	}

	public void createContentValues(InputStream stream, Context context) {
		
		CSVFile csvFile = new CSVFile(stream);

		String LOCATION_ID = null;
		String LOCATION_WORLD_TITLE = null;
		
		// compose world location objects
		List locationList = csvFile.read();
		ArrayList<FilmLocation> locationArrayList = new ArrayList<FilmLocation>();
		Iterator iter = locationList.iterator();
		while (iter.hasNext()) {
			Object[] result = (Object[]) iter.next();
			FilmLocation location = new FilmLocation();
			location.setSid(removeDoubleQuotes(result[0].toString()));
			location.setLevel(removeDoubleQuotes(result[1].toString()));
			location.setPosition(removeDoubleQuotes(result[2].toString()));
			location.setId(removeDoubleQuotes(result[3].toString()));
			LOCATION_WORLD_TITLE = removeDoubleQuotes(result[4].toString()); 
			location.setTitle(LOCATION_WORLD_TITLE);
			LOCATION_ID = removeDoubleQuotes(result[5].toString());
			location.setLocations(LOCATION_ID);
			location.setFunFacts(removeDoubleQuotes(result[6].toString()));
			location.setStaticMapImageUrl(removeDoubleQuotes(result[7].toString()));
			location.setFunFactsTitle(removeDoubleQuotes(result[8].toString()));
			location.setFunFactsImageUrl(removeDoubleQuotes(result[9].toString()));
			locationArrayList.add(location);
			System.out.println("OUTPUT LIST LOCATIONS: " + location.getId());
		}
		
		LocationsImpl datasource = new LocationsImpl(context); 
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		ArrayList<FilmLocation> currentTitleLocations = datasource.selectRecordsByTitle(LOCATION_WORLD_TITLE);
		System.out.println("DELETED LOCATIONS BEFORE: " + locationList.size());
		
		if (currentTitleLocations != null) {
			datasource.deleteRecordByTitle(LOCATION_WORLD_TITLE);
		}
		
		ArrayList<FilmLocation> afterCurrentTitleLocations = datasource.selectRecordsByTitle(LOCATION_WORLD_TITLE);
		System.out.println("DELETED LOCATIONS AFTER: " + afterCurrentTitleLocations.size());
		
		for (FilmLocation loc : locationArrayList) {
			datasource.createRecord(loc);
			System.out.println("DATABASE WORLD LOCATIONS BUILD OBJECTS LOCATIONS: " + loc.getLocations());
			System.out.println("DATABASE WORLD LOCATIONS BUILD OBJECTS ID: " + loc.getId());
		}
		
		datasource.close();
	}
	
	public void setLocationJson(JsonNode json) {
		LOCATION_JSON = json;
	}
	
	public JsonNode getLocationJson() {
		return LOCATION_JSON;
	}

//	// builder creation pattern
//	public FilmLocationService buildLocationObjects() throws IOException,
//			JSONException {
//
//		// convert json nodes to array
//		int length = filmLocations.getMovies().size();
//		JsonNode[] movieArray = new JsonNode[length];
//
//		// iterate through movies and map to array
//		for (int movie = 0; movie < length; movie++) {
//			movieArray[movie] = filmLocations.getMovies().get(movie);
////			System.out.println("MOVIE ARRAY[i]: " + movieArray[movie]);
//		}
//		
//		for (int i = 0; i < 20; i++) {
//			String latitude = "latitude";
//			String longitude = "longitude";
//			
////			// TODO: refactor this into a separate function
////			if (movieArray[i].get(LOCATIONS) != null) {
////				String SEARCH_QUERY = removeDoubleQuotesAndParenthesis(movieArray[i].get(LOCATIONS).toString());
////				SEARCH_QUERY = SEARCH_QUERY.replaceAll(" ", "+");
////				System.out.println("SEARCH_QUERY: " + SEARCH_QUERY);
////				JsonNode locations = getGelocation(GEOLOCATION_URI_PREFIX + SEARCH_QUERY + SEARCH_DELIMITER + LIMIT  + SENSORS);
////				System.out.println("LOCATIONS_MAP_QUERY: " + locations);
////				latitude = locations.get("results").findPath("geometry").findPath("location").findPath("lat").toString();
////				longitude = locations.get("results").findPath("geometry").findPath("location").findPath("lng").toString();
////			} else {
////				latitude = null;
////				longitude = null;	
////			}
//			
//			FilmLocation location = new FilmLocation();
//			location.setLocations(removeDoubleQuotes(movieArray[i].get(LOCATIONS).toString()));
//			location.setSid(removeDoubleQuotes(movieArray[i].get(SID).toString()));
//			location.setPosition(removeDoubleQuotes(movieArray[i].get(POSITION).toString()));
//			location.setCreatedAt(removeDoubleQuotes(movieArray[i].get(CREATED_AT).toString()));
//			location.setCreatedMeta(removeDoubleQuotes(movieArray[i].get(CREATED_META).toString()));
//			location.setUpdatedAt(removeDoubleQuotes(movieArray[i].get(UPDATED_AT).toString()));
//			location.setUpdatedMeta(removeDoubleQuotes(movieArray[i].get(UPDATED_META).toString()));
//			location.setMeta(removeDoubleQuotes(movieArray[i].get(META).toString()));
//			location.setId(removeDoubleQuotes(movieArray[i].get(ID).toString()));
//			
//			// remove trailing white spaces
//			// to avoid duplicate titles
//			location.setTitle(removeDoubleQuotes(movieArray[i].get(TITLE).toString()).replaceFirst("\\s+$", ""));
//			location.setReleaseYear(removeDoubleQuotes(movieArray[i].get(RELEASE_YEAR).toString()));
//			location.setFunFacts(removeDoubleQuotes(movieArray[i].get(FUN_FACTS).toString()));
//			location.setProductionCompany(removeDoubleQuotes(movieArray[i].get(PRODUCTION_COMPANY).toString()));
//			location.setDistributor(removeDoubleQuotes(movieArray[i].get(DISTRIBUTOR).toString()));
//			location.setDirector(removeDoubleQuotes(movieArray[i].get(DIRECTOR).toString()));
//			location.setWriter(removeDoubleQuotes(movieArray[i].get(WRITER).toString()));
//			location.setActor1(removeDoubleQuotes(movieArray[i].get(ACTOR_1).toString()));
//			location.setActor2(removeDoubleQuotes(movieArray[i].get(ACTOR_2).toString()));
//			location.setActor3(removeDoubleQuotes(movieArray[i].get(ACTOR_3).toString()));
//			location.setLatitude(latitude);
//			location.setLongitude(longitude);
//			filmList.add(location);
//		}
//		return this;
//	}
	
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)","");
	}
	
	public String removeDoubleQuotesAndParenthesis(String string) {
		String regex = string.replaceAll("(^\")|(\"$)","");
		regex = regex.replaceAll("\\("," ");
		regex = regex.replaceAll("\\)"," ");
		regex = regex.replaceFirst("\\s+$", "");
		return regex;
	}

	// private ArrayList<FilmLocation> filmList;
	public ArrayList<FilmLocation> returnFilmList() {
		return filmList;
	}
}