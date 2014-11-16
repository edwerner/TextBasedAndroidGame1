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

import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.ConclusionCardImpl;
import com.movie.locations.dao.GameTitleImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.ConclusionCard;
//import com.movie.locations.domain.FilmLocation.Builder;
import com.movie.locations.domain.FilmLocationDataObject;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.service.JsonService;
import com.movie.locations.util.CSVFile;

public class GameTitleService {
//	private ArrayList<QuizItem> quizList;
//	private Map<String, QuizItem> quizData;
//	private final String QUIZ_ITEM_URI = "http://movie-locations-app.appspot.com/questions";
	private JsonNode questionsJson;

	public GameTitleService() {
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

		String GAME_TITLE_ID = null;
		String GAME_TITLE = null;
//		String GAME_TITLE_LEVEL = null;
		
		// compose world location objects
		List locationList = csvFile.read();
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		Iterator iter = locationList.iterator();
		while (iter.hasNext()) {
			
//			private String id;
//			private String title;
//			private String type;
//			private String level;
//			private String phase;
//			private String imageUrl;
			
			Object[] result = (Object[]) iter.next();
			GameTitle title = new GameTitle();
			
			GAME_TITLE_ID = removeDoubleQuotes(result[0].toString());
			title.setId(removeDoubleQuotes(GAME_TITLE_ID));
			GAME_TITLE = removeDoubleQuotes(result[1].toString());
			title.setTitle(removeDoubleQuotes(GAME_TITLE));
			title.setType(removeDoubleQuotes(result[2].toString()));
			title.setLevel(removeDoubleQuotes(result[3].toString()));
			title.setPhase(removeDoubleQuotes(result[4].toString()));
			title.setImageUrl(removeDoubleQuotes(result[5].toString()));
			
			gameTitleList.add(title);
			System.out.println("OUTPUT CONCLUSION CARDS: " + title.getId());
		}
		
		GameTitleImpl datasource = new GameTitleImpl(context);
		datasource.delete();
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		GameTitle currentCard = datasource.selectRecordById(GAME_TITLE_ID);
//		System.out.println("DELETED LOCATIONS BEFORE: " + locationList.size());
		
		if (currentCard != null) {
			datasource.deleteRecordsByLevel(GAME_TITLE);
		}
		
//		ConclusionCard afterCurrentTitleLocations = datasource.selectRecordById(CARD_TITLE);
//		System.out.println("DELETED LOCATIONS AFTER: " + afterCurrentTitleLocations.size());
		
		for (GameTitle title : gameTitleList) {
			datasource.createRecord(title);
			System.out.println("DATABASE BUILD OBJECTS GAME TITLE: " + title.getTitle());
			System.out.println("DATABASE GAME TITLE BUILD OBJECTS ID: " + title.getId());
			System.out.println("DATABASE GAME TITLE BUILD OBJECTS TYPE: " + title.getType());
		}
		
		datasource.close();
	}
	public ArrayList<GameTitle> buildGameTitleObjects(JsonNode titleData, Context context) {

//		JsonNode locations = worldLocations.get("worldLocations");
//		JsonNode titleItems = titleData.path("gameTitleData");
		JsonNode titleType = titleData.path("type");
		JsonNode titleArray = titleData.path("gameTitleData");
		System.out.println("WORLD BUILD TITLE OBJECTS GAME TITLE DATA TYPE: " + titleType);
//		System.out.println(nameNode.getTextValue());
		
		// compose world location objects
		
		
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		
		GameTitleImpl gameTitleImpl = new GameTitleImpl(context);

		// TODO: remove this - clear existing film location records
//		bagItemDatasource.delete();
		
		
//		if (point.get("pointValue") != null) {
//			System.out.println("WORLD LOCATIONS BUILD OBJECTS POINTS ****: " + removeDoubleQuotes(point.get("pointValue").toString()));
//			pointsItem.setPoints(removeDoubleQuotes(point.get("pointValue").toString()));
//		}

		
		if (titleType != null) {
			
			// TODO: REMOVE THIS AFTER DEBUGGING
			gameTitleImpl.delete();
			
			
			String type = removeDoubleQuotes(titleType.toString());
			System.out.println("JSON NODE TITLE ITEM TYPE: " + type);
			if (titleArray != null) {

				for (JsonNode item : titleArray) {
					
//					System.out.println("JSON NODE TITLE ITEM TYPE: " + item);
					
					GameTitle title = new GameTitle();
					String localTitle = removeDoubleQuotes(item.toString());
//					
					final String localTitleId = type.concat("_").concat(localTitle);
					title.setId(localTitleId);
					title.setTitle(localTitle);
					title.setType(type);
					title.setLevel("null");
					title.setPhase("null");
					gameTitleList.add(title);
					
					// create database connection and store
					// location objects in sqlite database
					
					
					gameTitleImpl.open();
					ArrayList<GameTitle> gameTitleDatabaseList = gameTitleImpl.selectRecords();
					
					GameTitle duplicateTitle = gameTitleImpl.selectRecordById(localTitleId);
					if (duplicateTitle == null) {
						gameTitleImpl.createRecord(title);	
					}
					
					for (GameTitle localGameTitle : gameTitleDatabaseList) {
						System.out.println("GAME TITLE SERVICE DATABASE OBJECT: " + localGameTitle.getTitle());	
					}
					gameTitleImpl.close();
				}
			}
		}

		
		// set attributes
//		public static final String COLUMN_ID = "_id";
//		public static final String COLUMN_TITLE = "_title";
//		public static final String COLUMN_TYPE = "_type";
//		public static final String COLUMN_LEVEL = "_level";
		return gameTitleList;
	}

//	public BagItemService getQuizData() throws JsonProcessingException,
//			JSONException, IOException {
//		questionsJson = createJsonMapper(QUIZ_ITEM_URI);
//		// filmLocations = new FilmLocationDataObject(questionsJson);
//		 quizList = new ArrayList<QuizItem>();
//		// quizData = new HashMap<String, QuizItem>();
//		return this;
//	}

	public JsonNode createJsonMapper(String uri)
			throws JsonProcessingException, JSONException, IOException {
		final JsonService service = new JsonService();
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode json = mapper.readTree(service.getJson(uri).toString());
		return json;
	}

//	// builder creation pattern
//	public BagItemService buildQuizObjects() throws IOException, JSONException {
//
//		// convert json nodes to array
//		// int length = filmLocations.getMovies().size();
//		// JsonNode[] quizArray = new JsonNode[questionsJson.size()];
//
//		ObjectMapper mapper = new ObjectMapper();
//
//		Iterator<JsonNode> iterator = questionsJson.path("questions")
//				.getElements();
//
//		while (iterator.hasNext()) {
//			JsonNode node = iterator.next();
//			QuizItem quizItem = mapper.readValue(node, QuizItem.class);
////			System.out.println("service title: " + quizItem.getFilmTitle());
//			quizList.add(quizItem);
//		}
//
//		return this;
//	}

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

//	// private ArrayList<FilmLocation> filmList;
//	public ArrayList<QuizItem> returnQuizList() {
//		// ObjectMapper mapper = new ObjectMapper();
//		// JsonNode json = mapper.convertValue(filmData.get("location"),
//		// JsonNode.class);
//		// @SuppressWarnings("unchecked")
//		// Map<String,Object> jsonMap = mapper.readValue(json, Map.class);
//
//		// mapper.writeValue(json, filmList);
//
//		// System.out.println("FILM LIST: " + filmList);
//		return quizList;
//	}
}