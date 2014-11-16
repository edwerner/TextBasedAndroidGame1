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
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.ConclusionCard;
//import com.movie.locations.domain.FilmLocation.Builder;
import com.movie.locations.domain.FilmLocationDataObject;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.service.JsonService;
import com.movie.locations.util.CSVFile;

public class ConclusionCardService {
//	private ArrayList<QuizItem> quizList;
//	private Map<String, QuizItem> quizData;
//	private final String QUIZ_ITEM_URI = "http://movie-locations-app.appspot.com/questions";
	private JsonNode questionsJson;

	public ConclusionCardService() {
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

		String CARD_ID = null;
		String CARD_TITLE = null;
		String CARD_LEVEL = null;
		
		// compose world location objects
		List locationList = csvFile.read();
		ArrayList<ConclusionCard> cardArrayList = new ArrayList<ConclusionCard>();
		Iterator iter = locationList.iterator();
		while (iter.hasNext()) {
			
			Object[] result = (Object[]) iter.next();
			ConclusionCard card = new ConclusionCard();
			
			CARD_ID = removeDoubleQuotes(result[0].toString());
			card.setId(removeDoubleQuotes(CARD_ID));
			CARD_TITLE = removeDoubleQuotes(result[1].toString());
			card.setTitle(removeDoubleQuotes(CARD_TITLE));
			card.setCopy(removeDoubleQuotes(result[2].toString()));
			card.setImageUrl(removeDoubleQuotes(result[3].toString()));
			
			card.setLevel(removeDoubleQuotes(result[4].toString()));
			
			cardArrayList.add(card);
			System.out.println("OUTPUT CONCLUSION CARDS: " + card.getId());
		}
		
		ConclusionCardImpl datasource = new ConclusionCardImpl(context);
		datasource.delete();
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		ConclusionCard currentCard = datasource.selectRecordById(CARD_ID);
//		System.out.println("DELETED LOCATIONS BEFORE: " + locationList.size());
		
		if (currentCard != null) {
			datasource.deleteRecordsByLevel(CARD_TITLE);
		}
		
//		ConclusionCard afterCurrentTitleLocations = datasource.selectRecordById(CARD_TITLE);
//		System.out.println("DELETED LOCATIONS AFTER: " + afterCurrentTitleLocations.size());
		
		for (ConclusionCard card : cardArrayList) {
			datasource.createRecord(card);
			System.out.println("DATABASE BUILD OBJECTS CARDS: " + card.getTitle());
			System.out.println("DATABASE CARD BUILD OBJECTS ID: " + card.getId());
		}
		
		datasource.close();
	}
	public ArrayList<ConclusionCard> buildConclusionCardObject(JsonNode cardData, Context context) {

//		JsonNode locations = worldLocations.get("worldLocations");
		JsonNode cardItems = cardData.path("cards");
		System.out.println("WORLD LOCATIONS BUILD OBJECTS BAG ITEMS: " + cardData);
//		System.out.println(nameNode.getTextValue());
		
		// compose world location objects
		
		
		ArrayList<ConclusionCard> cardList = new ArrayList<ConclusionCard>();
		
		ConclusionCardImpl cardSource = new ConclusionCardImpl(context);

		// TODO: remove this - clear existing film location records
//		bagItemDatasource.delete();
		
		


		
		// set attributes
		
		for (JsonNode item : cardItems) {
			
			ConclusionCard card = new ConclusionCard();
			
			card.setId(removeDoubleQuotes(item.get("id").toString()));
			card.setTitle(removeDoubleQuotes(item.get("title").toString()));
			card.setCopy(removeDoubleQuotes(item.get("copyText").toString()));
			card.setLevel(removeDoubleQuotes(item.get("level").toString()));
			card.setImageUrl(removeDoubleQuotes(item.get("imageUrl").toString()));
			
//			generator.writeStringField("id", existingCardEntity.getProperty("id").toString());
//			generator.writeStringField("title", existingCardEntity.getProperty("title").toString());
//			generator.writeStringField("copyText", existingCardEntity.getProperty("copyText").toString());
//			generator.writeStringField("level", existingCardEntity.getProperty("level").toString());
//			generator.writeStringField("imageUrl", existingCardEntity.getProperty("imageUrl").toString());
//			bagItem.setItemId(removeDoubleQuotes(item.get("id").toString()));
//			bagItem.setBagGroupTitle(removeDoubleQuotes(item.get("group").toString()));
//			bagItem.setItemTitle(removeDoubleQuotes(item.get("title").toString()));
//			bagItem.setDescription(removeDoubleQuotes(item.get("description").toString()));
//			bagItem.setImageUrl(removeDoubleQuotes(item.get("imageUrl").toString()));
//			bagItem.setLevel(removeDoubleQuotes(item.get("level").toString()));
			
			cardList.add(card);
			
			// create database connection and store
			// location objects in sqlite database
			cardSource.open();
			cardSource.createRecord(card);
			ArrayList<ConclusionCard> cardDataRecordsList = cardSource.selectRecords();
			for (ConclusionCard localCard : cardDataRecordsList) {
				System.out.println("WORLD LOCATIONS CONCLUSION CARD OBJECT: " + localCard.getTitle());
			}
			cardSource.close();
		}
		return cardList;
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