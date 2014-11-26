package com.movie.locations.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.codehaus.jackson.JsonFactory;
import android.content.Context;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.QuizItem;

public class PointsItemService {
	
	private ArrayList<QuizItem> quizList;
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

		JsonNode points = pointsData.path("points");

		// set attributes
		PointsItem pointsItem = new PointsItem();
		for (JsonNode point : points) {
			if (point.get("pointValue") != null) {
				System.out.println("WORLD LOCATIONS BUILD OBJECTS POINTS ****: " + removeDoubleQuotes(point.get("pointValue").toString()));
				pointsItem.setPoints(removeDoubleQuotes(point.get("pointValue").toString()));
			}
			if (point.get("bonusPointValue") != null) {
				System.out.println("WORLD LOCATIONS BUILD OBJECTS BONUS POINTS ****: " + removeDoubleQuotes(point.get("bonusPointValue").toString()));
				pointsItem.setBonusPoints(removeDoubleQuotes(point.get("bonusPointValue").toString()));
			}
		}
		return pointsItem;
	}

	public PointsItemService buildQuizObjects() throws IOException, JSONException {

		ObjectMapper mapper = new ObjectMapper();
		Iterator<JsonNode> iterator = questionsJson.path("questions")
				.getElements();

		while (iterator.hasNext()) {
			JsonNode node = iterator.next();
			QuizItem quizItem = mapper.readValue(node, QuizItem.class);
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
}