//package com.movie.locations.application;
//
//import java.io.IOException;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.TextView;
//
//public class GcmAsyncTaskRunner extends AsyncTask<String, Void, String> {
//
//	public static final String EXTRA_MESSAGE = "message";
//	public static final String PROPERTY_REG_ID = "registration_id";
////	private static final String PROPERTY_APP_VERSION = "appVersion";
////	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//
//	/**
//	 * Substitute you own sender ID here. This is the project number you got
//	 * from the API Console, as described in "Getting Started."
//	 */
//	private String SENDER_ID = "680842032717";
//
//	/**
//	 * Tag used on log messages.
//	 */
//	static final String TAG = "GCM_MESSAGE_TAG";
//
//	TextView mDisplay;
//	GoogleCloudMessaging gcm;
//	AtomicInteger msgId = new AtomicInteger();
//
//	String regid;
//
//	@Override
//	protected String doInBackground(String... urls) {
////		String response = "response message";
////		int URL_INDEX = 0;
//		final String MOVIE_LOCATIONS_MESSAGE = urls[0];
//		
//		// send message upstream
//		String msg = "";
//		try {
//			Bundle data = new Bundle();
//			data.putString("my_message", MOVIE_LOCATIONS_MESSAGE);
//			data.putString("my_action", "com.google.android.gcm.demo.app.ECHO_NOW");
//			String id = Integer.toString(msgId.incrementAndGet());
//			
//			System.out.println("DATA: " + data);
//			System.out.println("ID:" + id);
//			System.out.println("MSG_ID: " + msgId);
//			
//			gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
//			msg = "Sent message";
//		} catch (IOException ex) {
//			msg = "Error :" + ex.getMessage();
//		}
//		System.out.println(TAG + " STATUS: " + msg);
//		return msg;
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		System.out.println("SPRING REST CALL ON POST EXECUTE: " + result);
//	}
//}
