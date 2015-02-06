package com.movie.locations.service;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;

interface IService {
	public JsonNode createJsonNode(String msg) throws JsonParseException, IOException;
	public void createContentValues(InputStream stream);
	public String removeDoubleQuotes(String string);
	
}
