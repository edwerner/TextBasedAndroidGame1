package com.movie.locations.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonFactory;
import android.content.Context;
import com.movie.locations.database.AchievementImpl;
import com.movie.locations.domain.Achievement;
import com.movie.locations.utility.CSVFile;

public class AchievementService implements IService {

	private Context context;
	private AchievementImpl achievementImpl;

	public AchievementService(Context context) {
		this.context = context;
	}
	
	public void createAchievementImpl() {
		achievementImpl = new AchievementImpl(context);
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
	public void createContentValues(InputStream stream) {
		
		CSVFile csvFile = new CSVFile(stream);
		String ACHIEVEMENT_ID = null;
		String ACHIEVEMENT_LEVEL = null;
		
		// compose world location objects
		List<?> locationList = csvFile.read();
		ArrayList<Achievement> achievementArrayList = new ArrayList<Achievement>();
		Iterator<?> iter = locationList.iterator();
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
		}
		
		achievementImpl = new AchievementImpl(context);
			
		// create database connection and store
		// location objects in sqlite database
		achievementImpl.open();
		
		Achievement currentTitleLocations = achievementImpl.selectRecordByLevel(ACHIEVEMENT_LEVEL);
		
		if (currentTitleLocations != null) {
			achievementImpl.deleteRecordById(ACHIEVEMENT_ID);
		}
		
		for (Achievement item : achievementArrayList) {
			achievementImpl.createRecord(item);
		}
		
		achievementImpl.close();
	}
	
	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}

	public ArrayList<Achievement> selectRecordsByLevel(String currentUserLevel) {
		achievementImpl.open();
		ArrayList<Achievement> achievementArrayList = achievementImpl.selectRecordsByLevel(currentUserLevel);
		achievementImpl.close();
		return achievementArrayList;
	}

	public Achievement selectRecordByLevel(String nextLevel) {
		achievementImpl.open();
		Achievement nextLevelAchievement = achievementImpl.selectRecordByLevel(nextLevel);
		achievementImpl.close();
		return nextLevelAchievement;
	}
}