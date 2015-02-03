package com.movie.locations.service;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;

import android.content.Context;

interface IService {
	public JsonNode createJsonNode(String msg) throws JsonParseException, IOException;
	public void createContentValues(InputStream stream, Context context);
	public String removeDoubleQuotes(String string);
	
}
