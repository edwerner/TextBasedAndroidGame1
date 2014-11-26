package com.movie.locations.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.codehaus.jackson.JsonFactory;
import android.content.Context;
import com.movie.locations.dao.AchievementImpl;
import com.movie.locations.domain.Achievement;
import com.movie.locations.util.CSVFile;

public class AchievementService {
	private JsonNode questionsJson;

	public AchievementService() {
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

		String ACHIEVEMENT_ID = null;
		String ACHIEVEMENT_LEVEL = null;
		
		// compose world location objects
		List locationList = csvFile.read();
		ArrayList<Achievement> achievementArrayList = new ArrayList<Achievement>();
		Iterator iter = locationList.iterator();
		while (iter.hasNext()) {
			Object[] result = (Object[]) iter.next();
			Achievement achievement = new Achievement();
			ACHIEVEMENT_ID = result[0].toString();
			achievement.setAchievementId(removeDoubleQuotes(ACHIEVEMENT_ID));
			achievement.setTitle(removeDoubleQuotes(removeDoubleQuotes(result[1].toString())));
			achievement.setDescription(removeDoubleQuotes(result[2].toString()));
			achievement.setUserId(removeDoubleQuotes(result[3].toString()));
			achievement.setDateTime(removeDoubleQuotes(result[4].toString()));
			achievement.setImageUrl(removeDoubleQuotes(result[5].toString()));
			ACHIEVEMENT_LEVEL = result[6].toString();
			achievement.setLevel(removeDoubleQuotes(ACHIEVEMENT_LEVEL));
			achievementArrayList.add(achievement);
			System.out.println("OUTPUT LIST: " + achievement.getTitle());
		}
		
		AchievementImpl datasource = new AchievementImpl(context);
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		Achievement currentTitleLocations = datasource.selectRecordByLevel(ACHIEVEMENT_LEVEL);
		System.out.println("DELETED LOCATIONS BEFORE: " + locationList.size());
		
		if (currentTitleLocations != null) {
			datasource.deleteRecordById(ACHIEVEMENT_ID);
		}
		
		for (Achievement item : achievementArrayList) {
			datasource.createRecord(item);
			System.out.println("DATABASE ACHIEVEMENT TITLE: " + item.getTitle());
			System.out.println("DATABASE ACHIEVEMENT ID: " + item.getAchievementId());
		}
		
		datasource.close();
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