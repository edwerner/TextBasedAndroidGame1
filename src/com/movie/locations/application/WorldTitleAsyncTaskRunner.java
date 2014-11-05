package com.movie.locations.application;

import org.codehaus.jackson.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;

import com.movie.locations.domain.User;



public class WorldTitleAsyncTaskRunner extends AsyncTask<String, String, String> {

	private String resp;
	private JsonNode json;
	private ProgressDialog dialog;
	private boolean initialized = false;
	private User localUser;
	private Context context;
	
	public void setLocalUser(User localUser) {
		this.localUser = localUser;
	}
	
//	public User getLocalUser() {
//		return this.localUser;
//	}

	public void setContext(Context context) {
		this.context = context;
	}
	
//	public Context getContext() {
//		return this.context;
//	}
	
	@Override
	protected String doInBackground(String... params) {
		publishProgress("Sleeping..."); // Calls onProgressUpdate()
		try {
			// Do your long operations here and return the result
			String url = params[0];
			// resp = "async call in progress";
			// Set the Content-Type header
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(new MediaType("application", "json"));
			HttpEntity<User> requestEntity = new HttpEntity<User>(localUser, requestHeaders);
//			System.out.println("REST TEMPLATE PRE RESPONSE: " + quizItem.getAnswered());

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();

			// Add the Jackson and String message converters
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

			// Make the HTTP POST request, marshaling the request to
			// JSON, and the response to a String
			// ResponseEntity<String> responseEntity =
			// restTemplate.exchange(url, HttpMethod.POST,
			// requestEntity, String.class);
			// String response = responseEntity.getBody();

			ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
			User localUser = responseEntity.getBody();
			
//			if (quizItem.getCorrectAnswerIndex() != null) {
//				// store correct answer index reference to update interface
//				currentAnswerIndex = quizItem.getCorrectAnswerIndex();
//			}

			// setCurrentQuestion(response);

			

			// send to callback
			resp = localUser.getUserClientId();
			System.out.println("REST TEMPLATE POST RESPONSE DISPLAY NAME FROM TITLE API: " + resp);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR STACK TRACE");
			resp = e.getMessage();
		}
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// execution of result of Long time consuming operation
		// finalResult.setText(result);
		// set json data here to serialize

//		updateUserInterface(result);

		if (dialog != null) {
			dialog.dismiss();
		}
		


//		final ListView restoreListView = (ListView) getView().findViewById(R.id.restoreLevelDataListView);
//		
//		GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
//		String WORLD_TITLE = "WORLD_TITLE";
//		final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(WORLD_TITLE);
//		
//		
//		
////		String worldStatus = "EXISTS";
////		int counter = 0;
////		if (gameTitleList.size() > 0) {
////			for (GameTitle title : gameTitleList) {
////				String tempTitle = title.getTitle();
////				tempTitle = tempTitle.replaceAll(" ", "");
////				FilmLocation tempLocation = datasource.selectRecordById(tempTitle);
////				if (tempLocation == null) {
////					// SET UI TO MISSING LOCATION
////					worldStatus = "MISSING";
////					title.setPhase(worldStatus);
////					gameTitleList.set(counter, title);	
////				}
////				counter++;
////			}	
////		}
//		
//		
//
//		
//		
//		
////		final ListView restoreListView = (ListView) getView().findViewById(R.id.restoreLevelDataListView);
//		
//		System.out.println("REFRESHED DATA");
//		final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
//		if (gameTitleList.size() > 0) {
//			restoreListView.setAdapter(levelRestoreListAdapter);
//			levelRestoreListAdapter.notifyDataSetChanged();
//		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		// Things to be done before execution of long running operation.
		// For
		// example showing ProgessDialog

//		int randomPhraseIndex = generateRandomNumber(0, 6);
		dialog = new ProgressDialog(context);
		dialog.setTitle("Retrieving...");
//		String message = "<i>" + randomPhrases[randomPhraseIndex] + "</i>";
		String message = "Building a list to restore levels and game items";
		dialog.setMessage(Html.fromHtml(message));
		dialog.setCancelable(false);
		dialog.setIndeterminate(true);
		dialog.show();
	}

	private int generateRandomNumber(int min, int max) {
		int randomNumber = min + (int)(Math.random() * ((max - min) + 1));
		return randomNumber;
	}
	
	// multi-lingual message prompts 
	public String[] randomPhrases = {
			"One moment please",
			"Un instant s'il vous plait",
			"Un momento por favor",
			"Einen Moment bitte",
			"Un momento per favore",
			"Ett ogonblick",
			"Een ogenblik geduld aub",
			"Odota hetki"
	};
	
	public String[] quotes = {
			"Ard-galen - 'The Green Region'. A grassy area.",
			"Cirion (Lord of Ships) - The twelfth Ruling Steward of Gondor."
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(String... text) {
		// finalResult.setText(text[0]);
		// Things to be done while execution of long running operation
		// is in
		// progress. For example updating ProgessDialog
	}
}