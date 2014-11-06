package com.movie.locations.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

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

//import org.springframework.web.servlet.ModelAndView;

//import com.fasterxml.jackson.core.JsonFactory;
import org.codehaus.jackson.JsonFactory;

import android.content.Context;
import android.content.Intent;

import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
//import com.movie.locations.domain.FilmLocation.Builder;
import com.movie.locations.domain.FilmLocationDataObject;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.QuizItemArrayList;
import com.movie.locations.service.JsonService;
import com.movie.locations.util.CSVFile;

public class QuizItemService {
	private ArrayList<QuizItem> quizList;
	private Map<String, QuizItem> quizData;
	private final String QUIZ_ITEM_URI = "http://movie-locations-app.appspot.com/questions";
	private JsonNode questionsJson;

	public QuizItemService() {
		// empty constructor
	}
	
	public JsonNode createLocationJson(String msg) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory(); 
		JsonParser parser = factory.createJsonParser(msg);
		JsonNode locationJson = mapper.readTree(parser);
		return locationJson;
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
		String QUIZ_WORLD_ID = null;
		String ANSWERED_FALSE = "false";
		
		// compose world location objects
		List quizList = csvFile.read();
		ArrayList<QuizItem> quizArrayList = new ArrayList<QuizItem>();
		Iterator iter = quizList.iterator();
		while (iter.hasNext()) {
			
			Object[] result = (Object[]) iter.next();
			QuizItem quizItem = new QuizItem();
			
			quizItem.setQuestionId(removeDoubleQuotes(result[0].toString()));
			quizItem.setAnswerSubmitCount(removeDoubleQuotes(result[1].toString()));
			quizItem.setQuestionText(removeDoubleQuotes(result[2].toString()));
			quizItem.setAnswer1(removeDoubleQuotes(result[3].toString()));
			quizItem.setAnswer2(removeDoubleQuotes(result[4].toString()));
			quizItem.setAnswer3(removeDoubleQuotes(result[5].toString()));
			quizItem.setAnswer4(removeDoubleQuotes(result[6].toString()));
			quizItem.setReaction1(removeDoubleQuotes(result[7].toString()));
			quizItem.setReaction2(removeDoubleQuotes(result[8].toString()));
			quizItem.setReaction3(removeDoubleQuotes(result[9].toString()));
			quizItem.setReaction4(removeDoubleQuotes(result[10].toString()));
			QUIZ_WORLD_ID = removeDoubleQuotes(result[11].toString());
			quizItem.setWorldId(QUIZ_WORLD_ID);
			quizItem.setWorldTitle(removeDoubleQuotes(result[12].toString()));
			quizItem.setSubmittedAnswerIndex(removeDoubleQuotes(result[13].toString()));
			quizItem.setCorrectAnswerIndex(removeDoubleQuotes(result[14].toString()));
//			System.out.println("CORRECT ANSWER INDEX SERVICE: " + result[14].toString());
			quizItem.setAnswered(removeDoubleQuotes(result[15].toString()));
			quizItem.setActiveItem(removeDoubleQuotes(result[16].toString()));
			quizItem.setActiveItem1(removeDoubleQuotes(result[17].toString()));
			quizItem.setActiveItem2(removeDoubleQuotes(result[18].toString()));
			quizItem.setActiveItem3(removeDoubleQuotes(result[19].toString()));
			quizItem.setActiveItem4(removeDoubleQuotes(result[20].toString()));
			quizItem.setLevel(removeDoubleQuotes(result[21].toString()));
			
			quizArrayList.add(quizItem);
			System.out.println("OUTPUT LIST QUIZ ITEMS: " + quizItem.getWorldId());
		}
		
		QuizItemImpl datasource = new QuizItemImpl(context);
		datasource.delete();
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		ArrayList<QuizItem> currentTitleLocations = datasource.selectRecordsByTitle(QUIZ_WORLD_ID);
		System.out.println("DELETED QUIZ ITEMS BEFORE: " + quizList.size());
		
		if (currentTitleLocations != null) {
			datasource.deleteRecordsByTitle(QUIZ_WORLD_ID);
		}
		
		ArrayList<QuizItem> afterCurrentTitleLocations = datasource.selectRecordsByTitle(QUIZ_WORLD_ID);
		System.out.println("DELETED LOCATIONS AFTER: " + afterCurrentTitleLocations.size());
		
		for (QuizItem temp : quizArrayList) {
			datasource.createRecord(temp);
			System.out.println("DATABASE QUIZ ITEMS BUILD OBJECTS LOCATIONS: " + temp.getWorldTitle());
			System.out.println("DATABASE QUIZ ITEMS BUILD OBJECTS ID: " + temp.getWorldId());
		}
		
		datasource.close();
		
		
		// SEND UPDATED COLLECTION TO VIEW
//		final FilmArrayList localLocationArrayList = new FilmArrayList();
//		localLocationArrayList.setFilmList(locationList);
		
		
		Intent newsIntent = new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
//		newsIntent.setAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
		QuizItemArrayList tempQuizItemArrayList = new QuizItemArrayList();
		tempQuizItemArrayList.setQuizList(quizArrayList);
		
		newsIntent.putExtra("quizArrayList", tempQuizItemArrayList);
		context.sendBroadcast(newsIntent);
		
		
		// SEND DATABASE CHANGED EVENT
//       	Intent broadcast = new Intent();
//       	broadcast.setAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
////        filter.addCategory(Intent.CATEGORY_DEFAULT);
//        sendBroadcast(broadcast);
//		
//		Intent intent = new Intent();
//        intent.setAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
////        intent.addFlags(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
//        context.sendBroadcast(intent);
	}
	
	public void resetAnsweredQuestion(String result, Context context) {
		QuizItemImpl datasource = new QuizItemImpl(context);
		datasource.updateRecordAnswered(result, "FALSE");
		Intent newsIntent = new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
		newsIntent.setAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
		ArrayList<QuizItem> currentTitleLocations = datasource.selectRecords();
		QuizItemArrayList tempQuizItemArrayList = new QuizItemArrayList();
		tempQuizItemArrayList.setQuizList(currentTitleLocations);
		
		newsIntent.putExtra("quizArrayList", tempQuizItemArrayList);
		context.sendBroadcast(newsIntent);
	}
	public ArrayList<QuizItem> buildWorldLocationQuizObject(JsonNode quizData, Context context) {

//		JsonNode locations = worldLocations.get("worldLocations");
		JsonNode quizItems = quizData.path("quizItems");
		System.out.println("WORLD LOCATIONS BUILD OBJECTS QUIZ ITEMS: " + quizData);
//		System.out.println(nameNode.getTextValue());
		
		// compose world location objects
		
		
		ArrayList<QuizItem> quizList = new ArrayList<QuizItem>();
		
		QuizItemImpl quizDatasource = new QuizItemImpl(context);

		// TODO: remove this - clear existing film location records
//		quizDatasource.delete();

		String LOCATION_WORLD_TITLE = null;
		

		
		

		
		// set attributes
		
		for (JsonNode item : quizItems) {
			
			QuizItem quizItem = new QuizItem();
			
			quizItem.setQuestionId(removeDoubleQuotes(item.get("questionId").toString()));
			quizItem.setQuestionText(removeDoubleQuotes(item.get("questionText").toString()));
			quizItem.setAnswer1(removeDoubleQuotes(item.get("answer1").toString()));
			quizItem.setAnswer2(removeDoubleQuotes(item.get("answer2").toString()));
			quizItem.setAnswer3(removeDoubleQuotes(item.get("answer3").toString()));
			quizItem.setAnswer4(removeDoubleQuotes(item.get("answer4").toString()));
			quizItem.setAnswered(removeDoubleQuotes(item.get("answered").toString()));
			quizItem.setReaction1(removeDoubleQuotes(item.get("reaction1").toString()));
			quizItem.setReaction2(removeDoubleQuotes(item.get("reaction2").toString()));
			quizItem.setReaction3(removeDoubleQuotes(item.get("reaction3").toString()));
			quizItem.setReaction4(removeDoubleQuotes(item.get("reaction4").toString()));
			quizItem.setWorldId(removeDoubleQuotes(item.get("worldId").toString()));
			LOCATION_WORLD_TITLE = removeDoubleQuotes(item.get("worldTitle").toString());
			quizItem.setWorldTitle(LOCATION_WORLD_TITLE);
			quizItem.setActiveItem(removeDoubleQuotes(item.get("activeItem").toString()));
			quizItem.setActiveItem1(removeDoubleQuotes(item.get("activeItem1").toString()));
			quizItem.setActiveItem2(removeDoubleQuotes(item.get("activeItem2").toString()));
			quizItem.setActiveItem3(removeDoubleQuotes(item.get("activeItem3").toString()));
			quizItem.setActiveItem4(removeDoubleQuotes(item.get("activeItem4").toString()));
			quizItem.setLevel(removeDoubleQuotes(item.get("level").toString()));
			if (!quizList.contains(quizItem)) {
				quizList.add(quizItem);
			}
			
		}
		

		
		// create database connection and store
		// location objects in sqlite database
		quizDatasource.open();
		
		ArrayList<QuizItem> currentQuizItems = quizDatasource.selectRecordsByTitle(LOCATION_WORLD_TITLE);
		System.out.println("DELETED QUIZ ITEM BEFORE: " + currentQuizItems.size());
		
		if (currentQuizItems != null) {
			quizDatasource.deleteRecordsByTitle(LOCATION_WORLD_TITLE);
			
			// UPDATE CURRENT ATTRIBUTES
//			datasource.
		}
		

		ArrayList<QuizItem> afterQuizItems = quizDatasource.selectRecordsByTitle(LOCATION_WORLD_TITLE);
		System.out.println("DELETED QUIZ ITEM AFTER: " + afterQuizItems.size());
		

		for (QuizItem item : quizList) {
//			FilmLocation existingLocation = datasource.selectRecordById(loc.getId());
//			if (existingLocation == null) {
				quizDatasource.createRecord(item);

				System.out.println("DATABASE QUIZ ITEM BUILD: " + item.getQuestionText());
//			}
			
//			if (existingLocation == null || existingLocation.getId() != loc.getId()) {
//				datasource.createRecord(loc);
//
//				System.out.println("DATABASE WORLD LOCATIONS BUILD OBJECTS LOCATIONS: " + loc.getLocations());
//				System.out.println("DATABASE WORLD LOCATIONS BUILD OBJECTS ID: " + loc.getId());
			}
		
//		quizDatasource.createRecord(quizItem);
//		ArrayList<QuizItem> quizDatasourceList = quizDatasource.selectRecords();
//		for (QuizItem quiz : quizDatasourceList) {
//			System.out.println("WORLD LOCATIONS ACTIVE OBJECT: " + quiz.getActiveItem());	
//		}
		quizDatasource.close();
		
		return quizList;
	}
	
//	public ArrayList<QuizItem> buildQuizObject(JsonNode quizItems, Context context) {
//		
//		
////		JsonNode locations = worldLocations.get("worldLocations");
//		JsonNode quizItemJson = quizItems.path("quizItems");
//		System.out.println("WORLD LOCATIONS BUILD OBJECTS: " + quizItemJson);
////		System.out.println(nameNode.getTextValue());
//		
//		// compose world location objects
//		
//		
//		ArrayList<QuizItem> quizList = new ArrayList<QuizItem>();
//		
//		QuizItemImpl quizsource = new QuizItemImpl(context);
//
//		// clear existing film location records
//		quizsource.delete();
//		
//		
//
//
//		
//		// set attributes
//		
//		for (JsonNode quiz : quizItemJson) {
//			QuizItem quizItem = new QuizItem();
//			
////			generator.writeStringField("level", newLevel.getProperty("level").toString());
////			generator.writeStringField("locations", newLevel.getProperty("locations").toString());
////			generator.writeStringField("id", newLevel.getProperty("id").toString());
////			generator.writeStringField("position", newLevel.getProperty("position").toString());
////			generator.writeStringField("sid", newLevel.getProperty("sid").toString());
////			// generator.writeStringField("createdAt", newLevel.getProperty("createdAt").toString());
////			// generator.writeStringField("createdMeta", newLevel.getProperty("createdMeta").toString());
////			// generator.writeStringField("updatedAt", newLevel.getProperty("updatedAt").toString());
////			// generator.writeStringField("updatedMeta", newLevel.getProperty("updatedMeta").toString());
////			// generator.writeStringField("meta", newLevel.getProperty("meta").toString());
////			generator.writeStringField("title", newLevel.getProperty("title").toString());
////			// generator.writeStringField("releaseYear", newLevel.getProperty("releaseYear").toString());
////			// generator.writeStringField("funFacts", newLevel.getProperty("funFacts").toString());
////			// generator.writeStringField("productionCompany", newLevel.getProperty("productionCompany").toString());
////			// generator.writeStringField("distributor", newLevel.getProperty("distributor").toString());
////			// generator.writeStringField("director", newLevel.getProperty("director").toString());
////			// generator.writeStringField("writer", newLevel.getProperty("writer").toString());
////			// generator.writeStringField("actor1", newLevel.getProperty("actor1").toString());
////			// generator.writeStringField("actor2", newLevel.getProperty("actor2").toString());
////			// generator.writeStringField("actor3", newLevel.getProperty("actor3").toString());
////			generator.writeStringField("latitude", newLevel.getProperty("latitude").toString());
////			generator.writeStringField("longitude", newLevel.getProperty("longitude").toString());
////			generator.writeStringField("staticMapImageUrl", newLevel.getProperty("staticMapImageUrl").toString());
//			quizList.add(quizItem);
//			
//			// create database connection and store
//			// location objects in sqlite database
////			quizsource.open();
////			quizsource.createRecord(quizItem);
////			quizsource.close();
//		}
		
//		datasource.open();
//		ArrayList<FilmLocation> databaseLocations = datasource.selectRecords();
//		
//		for (FilmLocation locList : databaseLocations) {
//			System.out.println("DATABASE LOCATIONS BUILD OBJECTS: " + locList.getLocations());
//		}
//		datasource.close();
		
//		JsonNode position = locations.get("positions");
//		System.out.println("WORLD LOCATIONS BUILD OBJECTS: " + locations);
//		
//		// return world location object
//		return quizList;
//	}

	public QuizItemService getQuizData() throws JsonProcessingException,
			JSONException, IOException {
		questionsJson = createJsonMapper(QUIZ_ITEM_URI);
		// filmLocations = new FilmLocationDataObject(questionsJson);
		 quizList = new ArrayList<QuizItem>();
		// quizData = new HashMap<String, QuizItem>();
		return this;
	}

	public JsonNode createJsonMapper(String uri)
			throws JsonProcessingException, JSONException, IOException {
		final JsonService service = new JsonService();
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode json = mapper.readTree(service.getJson(uri).toString());
		return json;
	}

	// builder creation pattern
	public QuizItemService buildQuizObjects() throws IOException, JSONException {

		// convert json nodes to array
		// int length = filmLocations.getMovies().size();
		// JsonNode[] quizArray = new JsonNode[questionsJson.size()];

		ObjectMapper mapper = new ObjectMapper();

		Iterator<JsonNode> iterator = questionsJson.path("questions")
				.getElements();

		while (iterator.hasNext()) {
			JsonNode node = iterator.next();
			QuizItem quizItem = mapper.readValue(node, QuizItem.class);
//			System.out.println("service title: " + quizItem.getFilmTitle());
			quizList.add(quizItem);
		}

		return this;
	}

	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}

	public String removeDoubleQuotesAndParenthesis(String string) {
		String regex = string.replaceAll("(^\")|(\"$)", "");
		regex = regex.replaceAll("\\(", " ");
		regex = regex.replaceAll("\\)", " ");
		regex = regex.replaceFirst("\\s+$", "");
		return regex;
	}

	// private ArrayList<FilmLocation> filmList;
	public ArrayList<QuizItem> returnQuizList() {
		// ObjectMapper mapper = new ObjectMapper();
		// JsonNode json = mapper.convertValue(filmData.get("location"),
		// JsonNode.class);
		// @SuppressWarnings("unchecked")
		// Map<String,Object> jsonMap = mapper.readValue(json, Map.class);

		// mapper.writeValue(json, filmList);

		// System.out.println("FILM LIST: " + filmList);
		return quizList;
	}
}