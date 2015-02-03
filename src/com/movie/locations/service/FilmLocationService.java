package com.movie.locations.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import android.content.Context;
import com.movie.locations.dao.LocationsImpl;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.util.CSVFile;

public class FilmLocationService implements IService {
	
	public FilmLocationService() {
		// empty constructor
	}
	
	@Override
	public JsonNode createJsonNode(String msg) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory(); 
		JsonParser parser = factory.createJsonParser(msg);
		JsonNode locationJson = mapper.readTree(parser);
		return locationJson;
	}

	@Override
	public void createContentValues(InputStream stream, Context context) {
		
		CSVFile csvFile = new CSVFile(stream);

		String LOCATION_ID = null;
		String LOCATION_WORLD_TITLE = null;
		
		// compose world location objects
		List<?> locationList = csvFile.read();
		ArrayList<FilmLocation> locationArrayList = new ArrayList<FilmLocation>();
		Iterator<?> iter = locationList.iterator();
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
	
	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)","");
	}
}