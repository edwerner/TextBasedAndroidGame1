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
import com.movie.locations.dao.GameTitleImpl;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.util.CSVFile;

public class GameTitleService {

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
		
		// compose world location objects
		List locationList = csvFile.read();
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		Iterator iter = locationList.iterator();
		while (iter.hasNext()) {
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
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		GameTitle currentCard = datasource.selectRecordById(GAME_TITLE_ID);
		
		if (currentCard != null) {
			datasource.deleteRecordByLevel(GAME_TITLE);
		}
		
		for (GameTitle title : gameTitleList) {
			datasource.createRecord(title);
			System.out.println("DATABASE BUILD OBJECTS GAME TITLE: " + title.getTitle());
			System.out.println("DATABASE GAME TITLE BUILD OBJECTS ID: " + title.getId());
			System.out.println("DATABASE GAME TITLE BUILD OBJECTS TYPE: " + title.getType());
		}
		
		datasource.close();
	}
	
	public ArrayList<GameTitle> buildGameTitleObjects(JsonNode titleData, Context context) {

		JsonNode titleType = titleData.path("type");
		JsonNode titleArray = titleData.path("gameTitleData");
		System.out.println("WORLD BUILD TITLE OBJECTS GAME TITLE DATA TYPE: " + titleType);		
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		GameTitleImpl gameTitleImpl = new GameTitleImpl(context);

		if (titleType != null) {
			
			// TODO: REMOVE THIS AFTER DEBUGGING
//			gameTitleImpl.delete();
			
			String type = removeDoubleQuotes(titleType.toString());
			System.out.println("JSON NODE TITLE ITEM TYPE: " + type);
			if (titleArray != null) {

				for (JsonNode item : titleArray) {		
					GameTitle title = new GameTitle();
					String localTitle = removeDoubleQuotes(item.toString());
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
		return gameTitleList;
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