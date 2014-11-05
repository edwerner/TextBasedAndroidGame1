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

import com.movie.locations.dao.NewsItemImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.domain.NewsItem;
//import com.movie.locations.domain.FilmLocation.Builder;
import com.movie.locations.domain.FilmLocationDataObject;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.service.JsonService;

public class NewsItemService {
//	private ArrayList<QuizItem> quizList;
//	private Map<String, QuizItem> quizData;
//	private final String QUIZ_ITEM_URI = "http://movie-locations-app.appspot.com/questions";
	private JsonNode questionsJson;

	public NewsItemService() {
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
	
	public ArrayList<NewsItem> buildNewsItemObject(JsonNode newsItemData, Context context) {

//		JsonNode locations = worldLocations.get("worldLocations");
		JsonNode newsItems = newsItemData.path("newsItems");
		System.out.println("WORLD LOCATIONS BUILD OBJECTS NEWS ITEMS: " + newsItemData);
//		System.out.println(nameNode.getTextValue());
		
		// compose world location objects
		
		
		ArrayList<NewsItem> newsItemList = new ArrayList<NewsItem>();
		
		NewsItemImpl newsItemDatasource = new NewsItemImpl(context);

		// TODO: remove this - clear existing film location records
//		newsItemDatasource.delete();
		
		


		
		// set attributes
		
		for (JsonNode item : newsItems) {
			
			NewsItem newsItem = new NewsItem();
			
//			pc.writeString(id);
//			pc.writeString(title);
//			pc.writeString(text);
//			pc.writeString(imageUrl);
//			pc.writeString(newsType);
//			pc.writeString(dateTime);
			
			newsItem.setId(removeDoubleQuotes(item.get("id").toString()));
			newsItem.setTitle(removeDoubleQuotes(item.get("title").toString()));
			newsItem.setText(removeDoubleQuotes(item.get("text").toString()));
			newsItem.setImageUrl(removeDoubleQuotes(item.get("imageUrl").toString()));
			newsItem.setNewsType(removeDoubleQuotes(item.get("newsType").toString()));
			newsItem.setDateTime(removeDoubleQuotes(item.get("dateTime").toString()));
			
			newsItemList.add(newsItem);
			
			// create database connection and store
			// location objects in sqlite database
			newsItemDatasource.open();
			newsItemDatasource.createRecord(newsItem);
			ArrayList<NewsItem> bagDatasourceList = newsItemDatasource.selectRecords();
			for (NewsItem localNewsItem : bagDatasourceList) {
				System.out.println("WORLD LOCATIONS NEWS ITEM OBJECT: " + localNewsItem.getText());	
			}
			newsItemDatasource.close();
		}
		return newsItemList;
	}

//	public NewsItemService getQuizData() throws JsonProcessingException,
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
//	public NewsItemService buildQuizObjects() throws IOException, JSONException {
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