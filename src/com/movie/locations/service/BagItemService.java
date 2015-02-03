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
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.util.CSVFile;

public class BagItemService implements IService {

	public BagItemService() {
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
		String BAG_ITEM_ID = null;
		String BAG_ITEM_LEVEL = null;
		
		// compose world location objects
		List<?> locationList = csvFile.read();
		ArrayList<BagItem> bagItemArrayList = new ArrayList<BagItem>();
		Iterator<?> iter = locationList.iterator();
		while (iter.hasNext()) {
			Object[] result = (Object[]) iter.next();
			BagItem bagItem = new BagItem();
			
			bagItem.setBagGroupTitle(removeDoubleQuotes(result[1].toString()));
			BAG_ITEM_ID = result[0].toString();
			bagItem.setItemId(removeDoubleQuotes(BAG_ITEM_ID));
			bagItem.setItemTitle(removeDoubleQuotes(result[2].toString()));
			bagItem.setDescription(removeDoubleQuotes(result[3].toString()));
			bagItem.setImageUrl(removeDoubleQuotes(result[4].toString()));
			BAG_ITEM_LEVEL = removeDoubleQuotes(result[5].toString());
			bagItem.setLevel(BAG_ITEM_LEVEL);
			bagItemArrayList.add(bagItem);
			System.out.println("OUTPUT LIST: " + bagItem.getItemTitle());
		}
		
		BagItemImpl datasource = new BagItemImpl(context);
			
		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		BagItem currentTitleLocations = datasource.selectRecordById(BAG_ITEM_ID);
		System.out.println("DELETED LOCATIONS BEFORE: " + locationList.size());
		
		if (currentTitleLocations != null) {
			datasource.deleteRecordByLevel(BAG_ITEM_LEVEL);
		}
		
		for (BagItem item : bagItemArrayList) {
			datasource.createRecord(item);
			System.out.println("DATABASE BUILD BAG ITEM TITLE: " + item.getItemTitle());
			System.out.println("DATABASE BAG ITEM ID: " + item.getItemId());
		}
		
		datasource.close();
	}

	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}
}