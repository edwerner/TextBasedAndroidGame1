package com.movie.locations.service;
import java.io.IOException;
import java.io.InputStream;
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

public class PointsItemService implements IService {

	public PointsItemService() {
		// Empty constructor
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
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}


	@Override
	public void createContentValues(InputStream stream, Context context) {
		// TODO Auto-generated method stub
		
	}
}