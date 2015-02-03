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

public class GameTitleService implements IService {

	public GameTitleService() {
		// empty constructor
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
	public void createContentValues(InputStream stream, Context context) {
		
		CSVFile csvFile = new CSVFile(stream);
		String GAME_TITLE_ID = null;
		String GAME_TITLE = null;
		
		// compose world location objects
		List<?> locationList = csvFile.read();
		ArrayList<GameTitle> gameTitleList = new ArrayList<GameTitle>();
		Iterator<?> iter = locationList.iterator();
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

	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}
}