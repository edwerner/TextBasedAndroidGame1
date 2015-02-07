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
import com.movie.locations.database.GameTitleImpl;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.utility.CSVFile;

public class GameTitleService implements IService {

	private Context context;
	private GameTitleImpl gameTitleImpl;

	public GameTitleService(Context context) {
		this.context = context;
	}
	
	public void createGameTitleImpl() {
		this.gameTitleImpl = new GameTitleImpl(context);
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
		}
		
		gameTitleImpl = new GameTitleImpl(context);
			
		// create database connection and store
		// location objects in sqlite database
		gameTitleImpl.open();
		
		GameTitle currentCard = gameTitleImpl.selectRecordById(GAME_TITLE_ID);
		
		if (currentCard != null) {
			gameTitleImpl.deleteRecordByLevel(GAME_TITLE);
		}
		
		for (GameTitle title : gameTitleList) {
			gameTitleImpl.createRecord(title);
		}
		
		gameTitleImpl.close();
	}

	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}

	public ArrayList<GameTitle> selectRecordsByType(String gameTitleType) {
		gameTitleImpl.open();
		ArrayList<GameTitle> gameTitleArrayList = gameTitleImpl.selectRecordsByType(gameTitleType);
		gameTitleImpl.close();
		return gameTitleArrayList;
	}

	public ArrayList<GameTitle> selectRecordsByTypeAndLevel(String gameTitleType,
			String currentUserLevel) {
		gameTitleImpl.open();
		ArrayList<GameTitle> gameTitleArrayList = gameTitleImpl.selectRecordsByTypeAndLevel(gameTitleType, currentUserLevel);
		gameTitleImpl.close();
		return gameTitleArrayList;
	}
}