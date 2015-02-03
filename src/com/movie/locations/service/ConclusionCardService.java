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
import com.movie.locations.dao.ConclusionCardImpl;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.util.CSVFile;

public class ConclusionCardService {

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
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		ConclusionCard currentCard = datasource.selectRecordById(CARD_ID);
		
		if (currentCard != null) {
			datasource.deleteRecordByLevel(CARD_TITLE);
		}
		
		for (ConclusionCard card : cardArrayList) {
			datasource.createRecord(card);
			System.out.println("DATABASE BUILD OBJECTS CARDS: " + card.getTitle());
			System.out.println("DATABASE CARD BUILD OBJECTS ID: " + card.getId());
		}
		datasource.close();
	}
	
	public ArrayList<ConclusionCard> buildConclusionCardObject(JsonNode cardData, Context context) {

		JsonNode cardItems = cardData.path("cards");
		System.out.println("WORLD LOCATIONS BUILD OBJECTS BAG ITEMS: " + cardData);		
		ArrayList<ConclusionCard> cardList = new ArrayList<ConclusionCard>();
		
		ConclusionCardImpl cardSource = new ConclusionCardImpl(context);

		// set attributes		
		for (JsonNode item : cardItems) {
			ConclusionCard card = new ConclusionCard();
			card.setId(removeDoubleQuotes(item.get("id").toString()));
			card.setTitle(removeDoubleQuotes(item.get("title").toString()));
			card.setCopy(removeDoubleQuotes(item.get("copyText").toString()));
			card.setLevel(removeDoubleQuotes(item.get("level").toString()));
			card.setImageUrl(removeDoubleQuotes(item.get("imageUrl").toString()));
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