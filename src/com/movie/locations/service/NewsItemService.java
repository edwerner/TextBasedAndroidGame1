package com.movie.locations.service;
import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonFactory;
import android.content.Context;
import com.movie.locations.dao.NewsItemImpl;
import com.movie.locations.domain.NewsItem;

public class NewsItemService {

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

		JsonNode newsItems = newsItemData.path("newsItems");
		System.out.println("WORLD LOCATIONS BUILD OBJECTS NEWS ITEMS: " + newsItemData);
		
		ArrayList<NewsItem> newsItemList = new ArrayList<NewsItem>();
		NewsItemImpl newsItemDatasource = new NewsItemImpl(context);

		// set attributes
		for (JsonNode item : newsItems) {
			NewsItem newsItem = new NewsItem();
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
}