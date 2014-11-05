//package com.movie.locations.application;
//
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//import android.os.AsyncTask;
//
//public class WebServerAsyncTaskRunner extends AsyncTask<String, Void, String> {
//
//	@Override
//	protected String doInBackground(String... urls) {
//		String response = "response message";
//		int URL_INDEX = 0;
//		String serverUrl = formatQueryString(urls);
//		System.out.println("SERVER URL: " + serverUrl);
////		@RequestMapping(value = "/checkin/{film}/{location}/{checkinId}/{userId}/{dateTime}", method = RequestMethod.GET)
//		try {
//			
//			
//
//			
////			// Create and populate a simple object to be used in the request
////			Message message = new Message();
////			message.setId(555);
////			message.setSubject("test subject");
////			message.setText("test text");
////
////			// Create a new RestTemplate instance
////			RestTemplate restTemplate = new RestTemplate();
////
////			// Add the Jackson and String message converters
////			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
////			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
////
////			// Make the HTTP POST request, marshaling the request to JSON, and the response to a String
////			String response = restTemplate.postForObject(url, message, String.class);
//			
//			// Create a new RestTemplate instance
//			RestTemplate restTemplate = new RestTemplate();
//			
//			// Add the Jackson and String message converters
//			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//			
//			// Make the HTTP POST request, marshaling the request to JSON, and the response to a String
//			response = restTemplate.postForObject(serverUrl, checkin, String.class);
//			
//			
//			
//			
//			
//			
//			
////			// Create a new RestTemplate instance
////			RestTemplate restTemplate = new RestTemplate();
////
////			// Add the String message converter
////			restTemplate.getMessageConverters().add(
////					new StringHttpMessageConverter());
////			
////			
////			// Make the HTTP GET request, marshaling the response to a String
//////			restTemplate.postForEntity(serverUrl, request, responseType, uriVariables);
//////			response = restTemplate.getForObject(serverUrl, String.class,
//////					"SpringSource");
////
////			// System.out.println("SPRING REST CALL RESULT: " + response);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	private String formatQueryString(String[] urls) {
//		String queryString = "";
//		for (int i = 0; i < urls.length; i++) {
//			queryString += urls[i];
//			queryString += "/";
//		}
//		return queryString;
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		System.out.println("SPRING REST CALL ON POST EXECUTE: " + result);
//	}
//}
