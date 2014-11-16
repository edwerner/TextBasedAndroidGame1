package com.movie.locations.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;

import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.WorldLocationObjectImpl;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.WorldLocationObject;
import com.movie.locations.gcm.GcmIntentService;

public class WorldLocationService {
	
	private String locationJson;
	private String quizJson;
	
	// empty constructor
	public WorldLocationService() {
		
	}
	
	public void createWorldLocationObjects() {
		
		// iterate through collections and build
		// world location domain objects to pass
		// through intent
		
		
		
	}
	
	public JsonNode createJsonNode(String msg) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory(); 
		JsonParser parser = factory.createJsonParser(msg);
		JsonNode locationJson = mapper.readTree(parser);
		return locationJson;
	}
	
	public ArrayList<WorldLocationObject> buildWorldLocationObjects(Context context, ArrayList<QuizItem> quizList, ArrayList<FilmLocation> locationList) throws JsonParseException, IOException {
		
//		JsonNode locations = locationData.get("worldLocations");
//		JsonNode quizItems = quizData.path("quizItems");

//		JsonNode quizJson = createJsonNode(getQuizJson());
//		JsonNode locationJson = createJsonNode(getLocationJson());
		
//		TODO: create mapping between location and quiz items
		
//		LinkedHashMap<String, WorldLocationObject[]> worldMap = new LinkedHashMap<String, WorldLocationObject[]>();
//		LinkedHashMap<String, WorldLocationObject[]> worldMap = new LinkedHashMap<String, WorldLocationObject[]>();
		
		// 1 - create arrays for quiz and location items
		// 2 - create array mapping of quiz and location items
		// 3 - create world location mapping querying new mapping (from step 2)
		//
		// match question id and world id for mappings
		
		HashMap<String, FilmLocation> locationMap = new HashMap<String, FilmLocation>();
		HashMap<String, QuizItem> quizMap = new HashMap<String, QuizItem>();
		
//		FilmLocation[] locationArray = new FilmLocation[locationJson.size()];
//		QuizItem[] quizArray = new QuizItem[quizJson.size()];
		
		ArrayList<WorldLocationObject> worldLocationList = new ArrayList<WorldLocationObject>();
		
//		// create and initialize quiz array counter
//		int quizCounter = 0;
//		
		// iterate through quiz json and create quiz object array
		for (QuizItem quizItem : quizList) {
			
			// check for duplicates then create quiz item mapping
			if (!quizMap.containsKey(quizItem.getQuestionId())) {
				
				// map location item by question id string
				quizMap.put(quizItem.getQuestionId(), quizItem);
			}
		}
		
		// iterate through quiz json and create quiz object array
		for (FilmLocation location : locationList) {
			
			// check for duplicates then create quiz item mapping
			if (!locationMap.containsKey(location.getId())) {
				
				// map location item by question id string
				locationMap.put(location.getId(), location);
			}
		}
//		
//		// create and initialize location array counter
//		int locationCounter = 0;
//		
//		// iterate through location json and create location object array
//		for (FilmLocation locItem : locationList) {
//			
//			// instantiate new location
//			FilmLocation currentLocation = new FilmLocation();
//			currentLocation.setPosition(locItem.getPosition());
////			location.setCreatedMeta(loc.getCreatedMeta());
//			currentLocation.setSid(locItem.getLocations());
////			location.setProductionCompany(loc.getProducionCompany());
////			location.setActor3(loc.getActor1());
////			location.setActor2(loc.getActor2());
////			location.setActor1(loc.getActor3());
////			location.setDirector(loc.getDirector());
////			location.setMeta(loc.getMeta());
////			location.setReleaseYear(loc.getReleaseYear());
//			String worldId = locItem.getId();
//			currentLocation.setId(worldId);
////			location.setUpdatedAt(loc.getLocation());
//			currentLocation.setTitle(locItem.getTitle());
//			currentLocation.setLevel(locItem.getLevel());
////			location.setFunFacts(loc.getFunFacts());
//			currentLocation.setLocations(locItem.getLocations());
////			location.setCreatedAt(loc.getCreatedAt());
////			location.setUpdatedMeta(loc.getUpdatedAt());
//			currentLocation.setLongitude(locItem.getLongitude());
////			location.setDistributor(loc.getDistributor());
//			currentLocation.setLatitude(locItem.getLatitude());
////			location.setWriter(loc.getWriter());
//			currentLocation.setStaticMapImageUrl(locItem.getStaticMapImageUrl());
//	
//			// check for duplicates then create location mapping
//			if (!locationMap.containsKey(worldId)) {
//				
//				// map location item by world id string
//				locationMap.put(worldId, currentLocation);
//			}
//			
//			// populate location item array
//			locationArray[locationCounter] = currentLocation;
//			
//			// increment location array counter
//			locationCounter++;
//		}
		
		
		
		// iterate through locations and query quiz mapping 
		// as you create new world location objects
		for (FilmLocation locItem : locationList) {
			
			// find match
			QuizItem localQuizItem = quizMap.get(locItem.getId());
			FilmLocation location = locationMap.get(locItem.getId());
			WorldLocationObject localWorldLocationObject = new WorldLocationObject();
//			WorldLocationObject[] localWorldLocationObjectArray;

			localWorldLocationObject.setCreatedAt(location.getCreatedAt());
			localWorldLocationObject.setCreatedMeta(location.getCreatedMeta());
			localWorldLocationObject.setUpdatedAt(location.getUpdatedAt());
			localWorldLocationObject.setUpdatedMeta(location.getUpdatedMeta());
			localWorldLocationObject.setMeta(location.getMeta());
			localWorldLocationObject.setTitle(location.getTitle());
			localWorldLocationObject.setReleaseYear(location.getReleaseYear());
			localWorldLocationObject.setFunFacts(location.getFunFacts());
			localWorldLocationObject.setProductionCompany(location.getProductionCompany());
			localWorldLocationObject.setDistributor(location.getDistributor());
			localWorldLocationObject.setDirector(location.getDirector());
			localWorldLocationObject.setWriter(location.getWriter());
//			localWorldLocationObject.setGeolocation(location.getGeolocation());
			localWorldLocationObject.setLocations(location.getLocations());
			localWorldLocationObject.setLatitude(location.getLatitude());
			localWorldLocationObject.setLongitude(location.getLongitude());
			localWorldLocationObject.setSid(location.getSid());
			localWorldLocationObject.setId(location.getId());
			localWorldLocationObject.setLevel(location.getLevel());
			localWorldLocationObject.setStaticMapImageUrl(location.getStaticMapImageUrl());
			localWorldLocationObject.setQuestionId(location.getId());
			
			
			localWorldLocationObject.setPosition(location.getPosition());
			localWorldLocationObject.setActor1(location.getActor1());
			localWorldLocationObject.setActor2(location.getActor2());
			localWorldLocationObject.setActor3(location.getActor3());
			
			
			
			
			localWorldLocationObject.setAnswer1(localQuizItem.getAnswer1());
			localWorldLocationObject.setAnswer2(localQuizItem.getAnswer2());
			localWorldLocationObject.setAnswer3(localQuizItem.getAnswer3());
			localWorldLocationObject.setAnswer4(localQuizItem.getAnswer4());
			localWorldLocationObject.setAnswered(localQuizItem.getAnswered());
			localWorldLocationObject.setDateTime(localQuizItem.getAnswerSubmitCount());
			localWorldLocationObject.setQuestionText(localQuizItem.getQuestionText());
			
			localWorldLocationObject.setReaction1(localQuizItem.getReaction1());
			localWorldLocationObject.setReaction2(localQuizItem.getReaction2());
			localWorldLocationObject.setReaction3(localQuizItem.getReaction3());
			localWorldLocationObject.setReaction4(localQuizItem.getReaction4());
			
			localWorldLocationObject.setWorldId(localQuizItem.getWorldId());
			localWorldLocationObject.setWorldTitle(localQuizItem.getWorldTitle());
			localWorldLocationObject.setSubmittedAnswerIndex(localQuizItem.getSubmittedAnswerIndex());
			localWorldLocationObject.setCorrectAnswerIndex(localQuizItem.getCorrectAnswerIndex());
			
			localWorldLocationObject.setActiveItem(localQuizItem.getActiveItem());
			localWorldLocationObject.setActiveItem1(localQuizItem.getActiveItem1());
			localWorldLocationObject.setActiveItem2(localQuizItem.getActiveItem2());
			localWorldLocationObject.setActiveItem3(localQuizItem.getActiveItem3());
			localWorldLocationObject.setActiveItem4(localQuizItem.getActiveItem4());
			
			worldLocationList.add(localWorldLocationObject);
		}
		
//		for (int a = 0; a < locationArray.length; a++) {
//			QuizItem localQuizItem = quizMap.get(locationArray[a]);
//			WorldLocationObject localWorldLocationObject = new WorldLocationObject();
////			WorldLocationObject[] localWorldLocationObjectArray;
//
//			localWorldLocationObject.setCreatedAt(locationArray[a].getCreatedAt());
//			localWorldLocationObject.setCreatedMeta(locationArray[a].getCreatedMeta());
//			localWorldLocationObject.setUpdatedAt(locationArray[a].getUpdatedAt());
//			localWorldLocationObject.setUpdatedMeta(locationArray[a].getUpdatedMeta());
//			localWorldLocationObject.setMeta(locationArray[a].getMeta());
//			localWorldLocationObject.setTitle(locationArray[a].getTitle());
//			localWorldLocationObject.setReleaseYear(locationArray[a].getReleaseYear());
//			localWorldLocationObject.setFunFacts(locationArray[a].getFunFacts());
//			localWorldLocationObject.setProductionCompany(locationArray[a].getProductionCompany());
//			localWorldLocationObject.setDistributor(locationArray[a].getDistributor());
//			localWorldLocationObject.setDirector(locationArray[a].getDirector());
//			localWorldLocationObject.setWriter(locationArray[a].getWriter());
//			localWorldLocationObject.setGeolocation(locationArray[a].getGeolocation());
//			localWorldLocationObject.setLocations(locationArray[a].getLocations());
//			localWorldLocationObject.setLatitude(locationArray[a].getLatitude());
//			localWorldLocationObject.setLongitude(locationArray[a].getLongitude());
//			localWorldLocationObject.setSid(locationArray[a].getSid());
//			localWorldLocationObject.setId(locationArray[a].getId());
//			localWorldLocationObject.setLevel(locationArray[a].getLevel());
//			localWorldLocationObject.setStaticMapImageUrl(locationArray[a].getStaticMapImageUrl());
//			localWorldLocationObject.setQuestionId(locationArray[a].getId());
//			
//			
//			localWorldLocationObject.setPosition(locationArray[a].getPosition());
//			localWorldLocationObject.setActor1(locationArray[a].getActor1());
//			localWorldLocationObject.setActor2(locationArray[a].getActor2());
//			localWorldLocationObject.setActor3(locationArray[a].getActor3());
//			
//			
//			
//			
//			localWorldLocationObject.setAnswer1(localQuizItem.getAnswer1());
//			localWorldLocationObject.setAnswer2(localQuizItem.getAnswer2());
//			localWorldLocationObject.setAnswer3(localQuizItem.getAnswer3());
//			localWorldLocationObject.setAnswer4(localQuizItem.getAnswer4());
//			localWorldLocationObject.setAnswered(localQuizItem.getAnswered());
//			localWorldLocationObject.setDateTime(localQuizItem.getDateTime());
//			localWorldLocationObject.setQuestionText(localQuizItem.getQuestionText());
//			
//			localWorldLocationObject.setReaction1(localQuizItem.getReaction1());
//			localWorldLocationObject.setReaction2(localQuizItem.getReaction2());
//			localWorldLocationObject.setReaction3(localQuizItem.getReaction3());
//			localWorldLocationObject.setReaction4(localQuizItem.getReaction4());
//			
//			localWorldLocationObject.setWorldId(localQuizItem.getWorldId());
//			localWorldLocationObject.setWorldTitle(localQuizItem.getWorldTitle());
//			localWorldLocationObject.setSubmittedAnswerIndex(localQuizItem.getSubmittedAnswerIndex());
//			localWorldLocationObject.setCorrectAnswerIndex(localQuizItem.getCorrectAnswerIndex());
//			
//			localWorldLocationObject.setActiveItem(localQuizItem.getActiveItem());
//			localWorldLocationObject.setActiveItem1(localQuizItem.getActiveItem1());
//			localWorldLocationObject.setActiveItem2(localQuizItem.getActiveItem2());
//			localWorldLocationObject.setActiveItem3(localQuizItem.getActiveItem3());
//			localWorldLocationObject.setActiveItem4(localQuizItem.getActiveItem4());
//			
//			worldLocationList.add(localWorldLocationObject);
//			
//			
////			// persist new world location object
////			
////
//////			TODO: move this mapping over to pager activity
//////			LinkedHashMap<String, WorldLocationObject> worldMap = new LinkedHashMap<String, WorldLocationObject>();
//////			worldMap.put(locationArray[a].getLocations(), localWorldLocationObject);
////			
////			// persist world location object to mysqlite database
////			WorldLocationObjectImpl worldlocationdata = new WorldLocationObjectImpl(context);
////			
////			// open database connection
////			worldlocationdata.open();
////			
////			// create new record
////			worldlocationdata.createRecord(localWorldLocationObject);
////			
////			// close database connection
////			worldlocationdata.close();
//		}
		
		return worldLocationList;
		
		
		
		
		
		
//		// worldMap.put(key, value);
//		
//		// iterate through collections and create array mapping
//		for (JsonNode locItem : locationJson) {
//			if (!worldMap.containsKey(removeDoubleQuotes(locItem.get("worldTitle").toString()))) {
//				
//				// create another quiz mapping to get value here
//				worldMap.put(removeDoubleQuotes(locItem.get("worldTitle").toString()), value);
//				if () {
//					
//				}
//			}
//		
//				
//	
////			for () {
////				
////			}
//		}
		
		
		
		
		
		
//		ArrayList<QuizItem> quizList = quizService.buildWorldLocationQuizObject(quizJson, this);
		
		
		
//		JsonNode locations = getLocationJson().get("worldLocations");
//		JsonNode quizItems = quizData.path("quizItems");
		
		

		
		// set attributes
		
//		for (JsonNode item : quizItems) {
//			
//			QuizItem quizItem = new QuizItem();
//			
//			quizItem.setQuestionId(removeDoubleQuotes(item.get("questionId").toString()));
//			quizItem.setQuestionText(removeDoubleQuotes(item.get("questionText").toString()));
//			quizItem.setAnswer1(removeDoubleQuotes(item.get("answer1").toString()));
//			quizItem.setAnswer2(removeDoubleQuotes(item.get("answer2").toString()));
//			quizItem.setAnswer3(removeDoubleQuotes(item.get("answer3").toString()));
//			quizItem.setAnswer4(removeDoubleQuotes(item.get("answer4").toString()));
//			quizItem.setReaction1(removeDoubleQuotes(item.get("reaction1").toString()));
//			quizItem.setReaction2(removeDoubleQuotes(item.get("reaction2").toString()));
//			quizItem.setReaction3(removeDoubleQuotes(item.get("reaction3").toString()));
//			quizItem.setReaction4(removeDoubleQuotes(item.get("reaction4").toString()));
//			quizItem.setWorldId(removeDoubleQuotes(item.get("worldId").toString()));
//			quizItem.setWorldTitle(removeDoubleQuotes(item.get("worldTitle").toString()));
//			quizItem.setActiveItem(removeDoubleQuotes(item.get("activeItem").toString()));
//			quizItem.setActiveItem1(removeDoubleQuotes(item.get("activeItem1").toString()));
//			quizItem.setActiveItem2(removeDoubleQuotes(item.get("activeItem2").toString()));
//			quizItem.setActiveItem3(removeDoubleQuotes(item.get("activeItem3").toString()));
//			quizItem.setActiveItem4(removeDoubleQuotes(item.get("activeItem4").toString()));
//			quizItem.setLevel(removeDoubleQuotes(item.get("level").toString()));
//			quizList.add(quizItem);
//			
//			// create database connection and store
//			// location objects in sqlite database
//			quizDatasource.open();
//			quizDatasource.createRecord(quizItem);
//			ArrayList<QuizItem> quizDatasourceList = quizDatasource.selectRecords();
//			for (QuizItem quiz : quizDatasourceList) {
//				System.out.println("WORLD LOCATIONS ACTIVE OBJECT: " + quiz.getActiveItem());	
//			}
//			quizDatasource.close();
//		}
//		return null;
	}

	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}

	public void setLocationJson(String locationJson) {
		this.locationJson = locationJson;
		
	}

	public String getLocationJson() {
		return locationJson;
		
	}

	public void setQuizJson(String quizJson) {
		this.quizJson = quizJson;
		
	}

	public String getQuizJson() {
		return quizJson;
		
	}
	
//	public void initializeWorldLocationJson(String msg) {
//		try {
//			createWorldLocationJson(msg);
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	private JsonNode createWorldLocationJson(String msg) throws JsonParseException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		JsonFactory factory = mapper.getJsonFactory(); 
//		JsonParser parser = factory.createJsonParser(msg);
//		JsonNode locationJson = mapper.readTree(parser);
//		return locationJson;
//	}
}
