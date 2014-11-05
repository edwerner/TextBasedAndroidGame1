package com.movie.locations.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonService {
	
	private final static String DEFAULT_GEOCODE_URL = "http://nominatim.openstreetmap.org/search.php?q=San+Francisco&format=json";

	public JsonService() {
		// empty constructor
	}
	
	public JSONObject getJson(String url) 
			throws JSONException, IOException {
		JSONObject json = readJsonFromUrl(url);
		return json;
	}
	
	public JSONArray getJsonArray(String url) 
			throws JSONException, IOException {
		JSONArray jsonArray = readJsonArrayFromUrl(url);
		return jsonArray;
	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static JSONArray readJsonArrayFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = null;
		JSONArray jsonArray;
		try {
			is = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			jsonArray = new JSONArray(jsonText);
		} catch (Exception e) {
			is = new URL(DEFAULT_GEOCODE_URL).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			jsonArray = new JSONArray(jsonText);
		} finally {
			is.close();
		}
		return jsonArray;
		
//		InputStream is = new URL(url).openStream();
//		try {
//			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
//					Charset.forName("UTF-8")));
//			String jsonText = readAll(rd);
//			JSONArray jsonArray = new JSONArray(jsonText);
//			return jsonArray;
//		} finally {
//			is.close();
//		}
	}
}