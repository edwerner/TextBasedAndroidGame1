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
import com.movie.locations.database.BagItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.utility.CSVFile;

public class BagItemService implements IService {

	private Context context;
	private BagItemImpl bagItemImpl;

	public BagItemService(Context context) {
		this.context = context;
	}
	
	public void createBagItemImpl() {
		bagItemImpl = new BagItemImpl(context);
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
		}
		
		bagItemImpl = new BagItemImpl(context);
			
		// create database connection and store
		// location objects in sqlite database
		bagItemImpl.open();
		
		BagItem currentTitleLocations = bagItemImpl.selectRecordById(BAG_ITEM_ID);
		
		if (currentTitleLocations != null) {
			bagItemImpl.deleteRecordByLevel(BAG_ITEM_LEVEL);
		}
		
		for (BagItem item : bagItemArrayList) {
			bagItemImpl.createRecord(item);
		}
		
		bagItemImpl.close();
	}

	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}

	public ArrayList<BagItem> selectRecords() {
		bagItemImpl.open();
		ArrayList<BagItem> bagItemArrayList = bagItemImpl.selectRecords();
		bagItemImpl.close();
		return bagItemArrayList;
	}
}