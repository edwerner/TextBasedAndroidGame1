package com.movie.locations.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.MappingIterator;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

//import org.springframework.web.servlet.ModelAndView;






import android.content.Context;
import android.os.Bundle;

import com.movie.locations.application.FilmLocationPagerActivity;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.domain.FilmArrayList;
//import com.movie.locations.domain.FilmLocation.Builder;
import com.movie.locations.domain.FilmLocationDataObject;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.service.JsonService;
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
	private JsonNode filmJsonNode;
	private Map<String, FilmLocation> filmData;
	private StringBuffer geolocationDataList;
	private FilmLocationDataObject filmLocations;
	private JsonNode geolocationData;
	private final String FILM_LOCATIONS_URI = "https://data.sfgov.org/api/views/yitu-d5am/rows.json";
	private final String GEOLOCATION_URI_PREFIX = "http://maps.googleapis.com/maps/api/geocode/json?address=";
//	private final String GEOLOCATION_URI_PREFIX = "http://nominatim.openstreetmap.org/search.php?q=";
	private final String SENSORS = "&sensor=true";
	private final String FORMAT = "&format=json";
	private final String LIMIT = "&limit=1";
//	private final String SEARCH_DELIMITER = "%20San%20Francisco%20California";
	private final String SEARCH_DELIMITER = "+San+Francisco+California";
	public JsonNode LOCATION_JSON;
	
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
			location.setCreatedAt(removeDoubleQuotes(result[3].toString()));
			location.setCreatedMeta(removeDoubleQuotes(result[4].toString()));
			location.setUpdatedAt(removeDoubleQuotes(result[5].toString()));
			location.setUpdatedMeta(removeDoubleQuotes(result[6].toString()));
			location.setMeta(removeDoubleQuotes(result[7].toString()));
			location.setId(removeDoubleQuotes(result[8].toString()));
			LOCATION_WORLD_TITLE = removeDoubleQuotes(result[9].toString()); 
			location.setTitle(LOCATION_WORLD_TITLE);
			location.setReleaseYear(removeDoubleQuotes(result[10].toString()));
			LOCATION_ID = removeDoubleQuotes(result[11].toString());
			location.setLocations(LOCATION_ID);
			location.setFunFacts(removeDoubleQuotes(result[12].toString()));
			location.setProductionCompany(removeDoubleQuotes(result[13].toString()));
			location.setDistributor(removeDoubleQuotes(result[14].toString()));
			location.setDirector(removeDoubleQuotes(result[15].toString()));
			location.setWriter(removeDoubleQuotes(result[16].toString()));
			location.setActor1(removeDoubleQuotes(result[17].toString()));
			location.setActor2(removeDoubleQuotes(result[18].toString()));
			location.setActor3(removeDoubleQuotes(result[19].toString()));
			location.setLatitude(removeDoubleQuotes(result[20].toString()));
			location.setLongitude(removeDoubleQuotes(result[21].toString()));
			location.setStaticMapImageUrl(removeDoubleQuotes(result[22].toString()));
			
			locationArrayList.add(location);
			System.out.println("OUTPUT LIST LOCATIONS: " + location.getId());
		}
		
		MovieLocationsImpl datasource = new MovieLocationsImpl(context);
		datasource.delete();
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		ArrayList<FilmLocation> currentTitleLocations = datasource.selectRecordsByTitle(LOCATION_WORLD_TITLE);
		System.out.println("DELETED LOCATIONS BEFORE: " + locationList.size());
		
		if (currentTitleLocations != null) {
			datasource.deleteRecordsByTitle(LOCATION_WORLD_TITLE);
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
	
	public ArrayList<FilmLocation> buildWorldLocationObject(JsonNode worldLocations, Context context) {
	
		JsonNode locations = worldLocations.path("worldLocations");
		
		// compose world location objects
		ArrayList<FilmLocation> locationList = new ArrayList<FilmLocation>();
		
		MovieLocationsImpl datasource = new MovieLocationsImpl(context);

		// clear existing film location records
		// TODO: REMOVE THIS AFTER DEBUGGING
		// TODO: REFACTOR THIS DATABASE CALL
//		datasource.delete();
		
		String LOCATION_ID = null;
		String LOCATION_WORLD_TITLE = null;

		// set attributes
		
		for (JsonNode loc : locations) {
			FilmLocation location = new FilmLocation();
			
			location.setPosition(removeDoubleQuotes(loc.get("position").toString()));
			location.setCreatedMeta(removeDoubleQuotes(loc.get("createdMeta").toString()));
			location.setSid(removeDoubleQuotes(loc.get("sid").toString()));
			location.setProductionCompany(removeDoubleQuotes(loc.get("productionCompany").toString()));
			location.setActor3(removeDoubleQuotes(loc.get("actor3").toString()));
			location.setActor2(removeDoubleQuotes(loc.get("actor2").toString()));
			location.setActor1(removeDoubleQuotes(loc.get("actor1").toString()));
			location.setDirector(removeDoubleQuotes(loc.get("director").toString()));
			location.setMeta(removeDoubleQuotes(loc.get("meta").toString()));
			location.setReleaseYear(removeDoubleQuotes(loc.get("releaseYear").toString()));
			LOCATION_ID = removeDoubleQuotes(loc.get("id").toString());
			location.setId(LOCATION_ID);
			location.setUpdatedAt(removeDoubleQuotes(loc.get("updatedAt").toString()));
			LOCATION_WORLD_TITLE = removeDoubleQuotes(loc.get("title").toString());
			location.setTitle(LOCATION_WORLD_TITLE);
			location.setLevel(removeDoubleQuotes(loc.get("level").toString()));
			location.setFunFacts(removeDoubleQuotes(loc.get("funFacts").toString()));
			location.setLocations(removeDoubleQuotes(loc.get("locations").toString()));
			location.setCreatedAt(removeDoubleQuotes(loc.get("createdAt").toString()));
			location.setUpdatedMeta(removeDoubleQuotes(loc.get("updatedMeta").toString()));
			location.setLongitude(removeDoubleQuotes(loc.get("longitude").toString()));
			location.setDistributor(removeDoubleQuotes(loc.get("distributor").toString()));
			location.setLatitude(removeDoubleQuotes(loc.get("latitude").toString()));
			location.setWriter(removeDoubleQuotes(loc.get("writer").toString()));
			location.setStaticMapImageUrl(removeDoubleQuotes(loc.get("staticMapImageUrl").toString()));
			
			if (!locationList.contains(location)) {
				locationList.add(location);	
			}
		}
		
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		ArrayList<FilmLocation> currentTitleLocations = datasource.selectRecordsByTitle(LOCATION_WORLD_TITLE);
		System.out.println("DELETED LOCATIONS BEFORE: " + locationList.size());
		
		if (currentTitleLocations != null) {
			datasource.deleteRecordsByTitle(LOCATION_WORLD_TITLE);
		}
		
		ArrayList<FilmLocation> afterCurrentTitleLocations = datasource.selectRecordsByTitle(LOCATION_WORLD_TITLE);
		System.out.println("DELETED LOCATIONS AFTER: " + afterCurrentTitleLocations.size());
		
		for (FilmLocation loc : locationList) {
			datasource.createRecord(loc);
			System.out.println("DATABASE WORLD LOCATIONS BUILD OBJECTS LOCATIONS: " + loc.getLocations());
			System.out.println("DATABASE WORLD LOCATIONS BUILD OBJECTS ID: " + loc.getId());
		}
		
		datasource.close();

		return locationList;
	}

	
	public void setLocationJson(JsonNode json) {
		LOCATION_JSON = json;
	}
	
	public JsonNode getLocationJson() {
		return LOCATION_JSON;
	}
	
	public FilmLocationService getMovieData() throws JsonProcessingException,
			JSONException, IOException {
		JsonNode filmJson = createJsonMapper(FILM_LOCATIONS_URI);
		filmLocations = new FilmLocationDataObject(filmJson);
		filmList = new ArrayList<FilmLocation>();
		filmData = new HashMap<String, FilmLocation>();
		return this;
	}

	public JsonNode createJsonMapper(String uri)
			throws JsonProcessingException, JSONException, IOException {
		final JsonService service = new JsonService();
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode json = mapper.readTree(service.getJson(uri).toString());
		return json;
	}

	public JSONArray getGeolocationData(String uri)
			throws JsonProcessingException, JSONException, IOException {
//		System.out.println("JSON: " + uri);
		final JsonService service = new JsonService();
		final ObjectMapper mapper = new ObjectMapper();
//		final JSONArray json = mapper.valueToTree(service.getJson(uri).toString());
		final JSONArray json = service.getJsonArray(uri);
//		JSONArray jsonArray = json.toString();
//		@SuppressWarnings("unchecked")
//		final JSONObject jsonObj = new JSONObject((Map<JSONObject, JsonNode>) json);
		
		return json;
	}

	// builder creation pattern
	public FilmLocationService buildLocationObjects() throws IOException,
			JSONException {

		// convert json nodes to array
		int length = filmLocations.getMovies().size();
		JsonNode[] movieArray = new JsonNode[length];

		// iterate through movies and map to array
		for (int movie = 0; movie < length; movie++) {
			movieArray[movie] = filmLocations.getMovies().get(movie);
//			System.out.println("MOVIE ARRAY[i]: " + movieArray[movie]);
		}
		
		for (int i = 0; i < 20; i++) {
			String latitude;
			String longitude;
			
			// TODO: refactor this into a separate function
			if (movieArray[i].get(LOCATIONS) != null) {
				String SEARCH_QUERY = removeDoubleQuotesAndParenthesis(movieArray[i].get(LOCATIONS).toString());
				SEARCH_QUERY = SEARCH_QUERY.replaceAll(" ", "+");
				System.out.println("SEARCH_QUERY: " + SEARCH_QUERY);
				JsonNode locations = getGelocation(GEOLOCATION_URI_PREFIX + SEARCH_QUERY + SEARCH_DELIMITER + LIMIT  + SENSORS);
				System.out.println("LOCATIONS_MAP_QUERY: " + locations);
				latitude = locations.get("results").findPath("geometry").findPath("location").findPath("lat").toString();
				longitude = locations.get("results").findPath("geometry").findPath("location").findPath("lng").toString();
			} else {
				latitude = null;
				longitude = null;	
			}
			
			FilmLocation location = new FilmLocation();
			
			location.setLocations(removeDoubleQuotes(movieArray[i].get(LOCATIONS).toString()));
			
			location.setSid(removeDoubleQuotes(movieArray[i].get(SID).toString()));
			location.setPosition(removeDoubleQuotes(movieArray[i].get(POSITION).toString()));
			location.setCreatedAt(removeDoubleQuotes(movieArray[i].get(CREATED_AT).toString()));
			location.setCreatedMeta(removeDoubleQuotes(movieArray[i].get(CREATED_META).toString()));
			location.setUpdatedAt(removeDoubleQuotes(movieArray[i].get(UPDATED_AT).toString()));
			location.setUpdatedMeta(removeDoubleQuotes(movieArray[i].get(UPDATED_META).toString()));
			location.setMeta(removeDoubleQuotes(movieArray[i].get(META).toString()));
			location.setId(removeDoubleQuotes(movieArray[i].get(ID).toString()));
			
			// remove trailing white spaces
			// to avoid duplicate titles
			location.setTitle(removeDoubleQuotes(movieArray[i].get(TITLE).toString()).replaceFirst("\\s+$", ""));
			
			location.setReleaseYear(removeDoubleQuotes(movieArray[i].get(RELEASE_YEAR).toString()));
			
			location.setFunFacts(removeDoubleQuotes(movieArray[i].get(FUN_FACTS).toString()));
			location.setProductionCompany(removeDoubleQuotes(movieArray[i].get(PRODUCTION_COMPANY).toString()));
			location.setDistributor(removeDoubleQuotes(movieArray[i].get(DISTRIBUTOR).toString()));
			location.setDirector(removeDoubleQuotes(movieArray[i].get(DIRECTOR).toString()));
			location.setWriter(removeDoubleQuotes(movieArray[i].get(WRITER).toString()));
			location.setActor1(removeDoubleQuotes(movieArray[i].get(ACTOR_1).toString()));
			location.setActor2(removeDoubleQuotes(movieArray[i].get(ACTOR_2).toString()));
			location.setActor3(removeDoubleQuotes(movieArray[i].get(ACTOR_3).toString()));
			
			location.setLatitude(latitude);
			location.setLongitude(longitude);

			filmList.add(location);
		}
		return this;
	}
	
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
	
	public JsonNode getGelocation(String location) throws IOException,
	JSONException {
			geolocationData = createJsonMapper(location);
		return geolocationData;
	}
}