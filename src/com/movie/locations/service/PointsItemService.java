package com.movie.locations.service;

import java.io.BufferedReader;
import java.io.IOException;
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

import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.PointsItemImpl;
import com.movie.locations.dao.QuizItemImpl;
//import com.movie.locations.domain.FilmLocation.Builder;
import com.movie.locations.domain.FilmLocationDataObject;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.service.JsonService;

public class PointsItemService {
	private ArrayList<QuizItem> quizList;
	private Map<String, QuizItem> quizData;
	private final String QUIZ_ITEM_URI = "http://movie-locations-app.appspot.com/questions";
	private JsonNode questionsJson;

	public PointsItemService() {
		
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
	
	public PointsItem buildPointsItemObject(JsonNode pointsData, Context context) {

//		quizItem.setLevel(removeDoubleQuotes(item.get("level").toString()));
//		JsonNode locations = worldLocations.get("worldLocations");
		JsonNode points = pointsData.path("points");
		
//		item.get("questionId").toString()
		
//		JsonNode pointItem = points.get("pointValue");
		

		// set attributes
		PointsItem pointsItem = new PointsItem();
		
//		String pointsUserId = "TEMP_WORLD_ID_" + currentUserId;
		
		for (JsonNode point : points) {
			
//			quizItem.setQuestionId(removeDoubleQuotes(point.get("pointValue").toString()));
			

//			values.put(COLUMN_USER_ID, pointsItem.getUserId());
//			values.put(COLUMN_POINTS_USER_ID, pointsItem.getPointsUserId());
//			values.put(COLUMN_POINTS, pointsItem.getPoints());
//			values.put(COLUMN_BONUS_POINTS, pointsItem.getPoints());
//			pointsSource.delete();
			
			// SETTING THESE INSIDE ACTIVITY NOW
			// THIS SERVICE IS ONLY INTENDED FOR
			// DESERIALIZING AND FORMATTING JSON
			// RESPONSE
			
			// TODO: MOVE OTHER DATABASE TRANSACTIONS
			// TO CONSLUSION INTENT
			
//			pointsItem.setPointsUserId(pointsUserId);
//			pointsItem.setUserId(currentUserId);
			
			// TODO: set world attributes and copy text on the server
			// and include in message json
			if (point.get("pointValue") != null) {
				System.out.println("WORLD LOCATIONS BUILD OBJECTS POINTS ****: " + removeDoubleQuotes(point.get("pointValue").toString()));
				pointsItem.setPoints(removeDoubleQuotes(point.get("pointValue").toString()));
			}
			if (point.get("bonusPointValue") != null) {
				System.out.println("WORLD LOCATIONS BUILD OBJECTS BONUS POINTS ****: " + removeDoubleQuotes(point.get("bonusPointValue").toString()));
				pointsItem.setBonusPoints(removeDoubleQuotes(point.get("bonusPointValue").toString()));
			}
			
			// MOVING DATABASE OPERATIONS TO CONCLUSION ACTIVITY
			
//			PointsItemImpl pointsSource = new PointsItemImpl(context); 
//			PointsItem tempPointsItem = pointsSource.selectRecordById(currentUserId);
//			pointsSource.open();
////			pointsSource.delete();
//			if (tempPointsItem == null) {
//
//				// create database connection and store
//				// location objects in sqlite database
//				
////				pointsSource.delete();
//				pointsSource.createRecord(pointsItem);
////				pointsSource.close();
//			} else {
//				String currentPoints = tempPointsItem.getPoints();
//				String currentBonusPoints = tempPointsItem.getBonusPoints();
//				
//				if (point.get("pointValue") != null) {
//					// compose updated point sums
//					int pointsSum = Integer.parseInt(currentPoints) + Integer.parseInt(pointsItem.getPoints());
//					String updatedPoints = Integer.toString(pointsSum);
////					pointsSource.updateRecordPointsValue(currentUserId, updatedPoints);
//					System.out.println("CURRENT POINTS: " + updatedPoints);
//					String updatedBonusPoints = null;
//					
//					if (point.get("bonusPointValue") != null) {
//						// compose updated point sums
//						int bonusPointsSum = Integer.parseInt(currentBonusPoints) + Integer.parseInt(pointsItem.getBonusPoints());
//						updatedBonusPoints = Integer.toString(bonusPointsSum);
////						pointsSource.updateRecordBonusPointsValue(currentUserId, updatedBonusPoints);
//						System.out.println("CURRENT BONUS POINTS: " + updatedBonusPoints);
//					}
//					
//					// update record with all current data
//					pointsSource.updateRecordBonusPointsValue(currentUserId, updatedPoints, updatedBonusPoints);
//				}
//				pointsSource.close();
//				ArrayList<PointsItem> quizDatasourceList = pointsSource.selectRecords();
//				
//				if (quizDatasourceList != null) {
//					for (PointsItem quiz : quizDatasourceList) {
//						System.out.println("POINTS SERVICE DATABASE SAVE POINTS: " + quiz.getPoints());
//						System.out.println("POINTS SERVICE DATABASE SAVE BONUS POINTS: " + quiz.getBonusPoints());
//					}	
//				} else {
//					System.out.println("POINTS SERVICE DATABASE EMPTY");
//				}
//			}
		}
		

		
//		ArrayList<PointsItem> quizDatasourceList = pointsSource.selectRecords();
//		
//		if (quizDatasourceList != null) {
//			for (PointsItem quiz : quizDatasourceList) {
//				System.out.println("POINTS SERVICE DATABASE SAVE: " + quiz.getPointsUserId());	
//			}	
//		} else {
//			System.out.println("POINTS SERVICE DATABASE EMPTY");
//		}
		
		
		// save points to database
		
		
		
		
		
//		String pointValue = points.get("pointValue").toString();
//		System.out.println("WORLD LOCATIONS BUILD OBJECTS POINTS: " + pointsData);
//		System.out.println("WORLD LOCATIONS BUILD OBJECTS POINTS JSON NODE TO STRING: " + pointItem.toString());
//		System.out.println("WORLD LOCATIONS BUILD OBJECTS BONUS POINTS VALUE: " + bonusPointsData);
//		System.out.println(nameNode.getTextValue());
		
		// compose world location objects
		
		
//		ArrayList<QuizItem> quizList = new ArrayList<QuizItem>();
		
//		QuizItemImpl quizDatasource = new QuizItemImpl(context);
//
//		// TODO: remove this - clear existing film location records
//		quizDatasource.delete();
//		
		

		// TODO: build user database query to update score attribute

		
		// set attributes
		
//		for (JsonNode item : pointValue) {
			
//			PointsItem pointsItem = new PointsItem();
//			pointsItem.setPoints(pointValue.getTextValue());
//			if (bonusPointsData != null) {
//				pointsItem.setBonusPoints(bonusPointsData);
//			}
			
//			QuizItem quizItem = new QuizItem();
//			
//			quizItem.setQuestionId(removeDoubleQuotes(item.get("questionId").toString()));
//			quizItem.setQuestionText(removeDoubleQuotes(item.get("questionText").toString()));
//			quizItem.setAnswer1(removeDoubleQuotes(item.get("answer1").toString()));
//			quizItem.setAnswer2(removeDoubleQuotes(item.get("answer2").toString()));
//			quizItem.setAnswer3(removeDoubleQuotes(item.get("answer3").toString()));
//			quizItem.setAnswer4(removeDoubleQuotes(item.get("answer4").toString()));
//			quizItem.setAnswered(removeDoubleQuotes(item.get("answered").toString()));
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
		return pointsItem;
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

	public PointsItemService getQuizData() throws JsonProcessingException,
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
	public PointsItemService buildQuizObjects() throws IOException, JSONException {

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