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
import org.json.JSONException;
import org.codehaus.jackson.JsonFactory;
import android.content.Context;
import android.content.Intent;

import com.movie.locations.database.QuizItemImpl;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.QuizItemArrayList;
import com.movie.locations.receiver.DatabaseChangedReceiver;
import com.movie.locations.utility.CSVFile;

public class QuizItemService implements IService {

	public QuizItemService() {
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
		String QUIZ_WORLD_ID = null;
		
		// compose world location objects
		List<?> quizList = csvFile.read();
		ArrayList<QuizItem> quizArrayList = new ArrayList<QuizItem>();
		Iterator<?> iter = quizList.iterator();
		while (iter.hasNext()) {
			Object[] result = (Object[]) iter.next();
			QuizItem quizItem = new QuizItem();
			quizItem.setQuestionId(removeDoubleQuotes(result[0].toString()));
			quizItem.setAnswerSubmitCount(removeDoubleQuotes(result[1].toString()));
			quizItem.setQuestionText(removeDoubleQuotes(result[2].toString()));
			quizItem.setAnswer1(removeDoubleQuotes(result[3].toString()));
			quizItem.setAnswer2(removeDoubleQuotes(result[4].toString()));
			quizItem.setAnswer3(removeDoubleQuotes(result[5].toString()));
			quizItem.setAnswer4(removeDoubleQuotes(result[6].toString()));
			quizItem.setReaction1(removeDoubleQuotes(result[7].toString()));
			quizItem.setReaction2(removeDoubleQuotes(result[8].toString()));
			quizItem.setReaction3(removeDoubleQuotes(result[9].toString()));
			quizItem.setReaction4(removeDoubleQuotes(result[10].toString()));
			QUIZ_WORLD_ID = removeDoubleQuotes(result[11].toString());
			quizItem.setWorldId(QUIZ_WORLD_ID);
			quizItem.setWorldTitle(removeDoubleQuotes(result[12].toString()));
			quizItem.setSubmittedAnswerIndex(removeDoubleQuotes(result[13].toString()));
			quizItem.setCorrectAnswerIndex(removeDoubleQuotes(result[14].toString()));
			quizItem.setAnswered(removeDoubleQuotes(result[15].toString()));
			quizItem.setActiveItem(removeDoubleQuotes(result[16].toString()));
			quizItem.setActiveItem1(removeDoubleQuotes(result[17].toString()));
			quizItem.setActiveItem2(removeDoubleQuotes(result[18].toString()));
			quizItem.setActiveItem3(removeDoubleQuotes(result[19].toString()));
			quizItem.setActiveItem4(removeDoubleQuotes(result[20].toString()));
			quizItem.setLevel(removeDoubleQuotes(result[21].toString()));
			quizItem.setPointValue(removeDoubleQuotes(result[22].toString()));
			
			quizArrayList.add(quizItem);
			System.out.println("OUTPUT LIST QUIZ ITEMS: " + quizItem.getWorldId());
		}
		
		QuizItemImpl datasource = new QuizItemImpl(context);

		// create database connection and store
		// location objects in sqlite database
		datasource.open();
		
		ArrayList<QuizItem> currentTitleLocations = datasource.selectRecordsByTitle(QUIZ_WORLD_ID);
		System.out.println("DELETED QUIZ ITEMS BEFORE: " + quizList.size());
		
		if (currentTitleLocations != null) {
			datasource.deleteRecordByTitle(QUIZ_WORLD_ID);
		}
		
		ArrayList<QuizItem> afterCurrentTitleLocations = datasource.selectRecordsByTitle(QUIZ_WORLD_ID);
		System.out.println("DELETED LOCATIONS AFTER: " + afterCurrentTitleLocations.size());
		
		for (QuizItem temp : quizArrayList) {
			datasource.createRecord(temp);
			System.out.println("DATABASE QUIZ ITEMS BUILD OBJECTS LOCATIONS: " + temp.getWorldTitle());
			System.out.println("DATABASE QUIZ ITEMS BUILD OBJECTS ID: " + temp.getWorldId());
		}
		
		datasource.close();
		
		Intent newsIntent = new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
		QuizItemArrayList tempQuizItemArrayList = new QuizItemArrayList();
		tempQuizItemArrayList.setQuizList(quizArrayList);
		newsIntent.putExtra("quizArrayList", tempQuizItemArrayList);
		context.sendBroadcast(newsIntent);
	}
	
	public void resetAnsweredQuestion(String result, Context context) {
		Intent newsIntent = new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
		newsIntent.setAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
		QuizItemImpl datasource = new QuizItemImpl(context);
		datasource.open();
		datasource.updateRecordAnswered(result, "FALSE");
		QuizItem tempQuizItem = datasource.selectRecordById(result);
		datasource.close();
		newsIntent.putExtra("updatedQuizItem", tempQuizItem);
		context.sendBroadcast(newsIntent);
	}
	
	@Override
	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}
}