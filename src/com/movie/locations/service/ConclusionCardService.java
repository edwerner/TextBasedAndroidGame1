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
import com.movie.locations.database.ConclusionCardImpl;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.utility.CSVFile;

public class ConclusionCardService implements IService {

	private Context context;
	private ConclusionCardImpl conclusionCardImpl;

	public ConclusionCardService(Context context) {
		this.context = context;
	}
	
	public void createConclusionCardImpl() {
		conclusionCardImpl = new ConclusionCardImpl(context);
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
		String CARD_ID = null;
		String CARD_TITLE = null;
		
		// compose world location objects
		List<?> locationList = csvFile.read();
		ArrayList<ConclusionCard> cardArrayList = new ArrayList<ConclusionCard>();
		Iterator<?> iter = locationList.iterator();
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
		}
		
		conclusionCardImpl = new ConclusionCardImpl(context);
			
		// create database connection and store
		// location objects in sqlite database
		conclusionCardImpl.open();
		ConclusionCard currentCard = conclusionCardImpl.selectRecordById(CARD_ID);
		
		if (currentCard != null) {
			conclusionCardImpl.deleteRecordByLevel(CARD_TITLE);
		}
		
		for (ConclusionCard card : cardArrayList) {
			conclusionCardImpl.createRecord(card);
		}
		
		conclusionCardImpl.close();
	}

	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}

	public ConclusionCard selectRecordById(String conclusionCardId) {
		conclusionCardImpl.open();
		ConclusionCard conclusionCard = conclusionCardImpl.selectRecordById(conclusionCardId);
		conclusionCardImpl.close();
		return conclusionCard;
	}
}