//package com.movie.locations.application;
//
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TimeZone;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.codehaus.jackson.JsonNode;
//import org.jivesoftware.smack.XMPPException;
//import org.osmdroid.DefaultResourceProxyImpl;
//import org.osmdroid.ResourceProxy;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import com.movie.locations.R;
//import com.movie.locations.R.id;
//import com.movie.locations.R.layout;
//import com.movie.locations.dao.LocationCheckinImpl;
//import com.movie.locations.domain.CheckIn;
//import com.movie.locations.domain.ClassLoaderHelper;
//import com.movie.locations.domain.FilmLocation;
//import com.movie.locations.domain.User;
////import com.movie.locations.gcm.BroadcastCheckinActivity;
//import com.movie.locations.service.FilmLocationService;
//import com.movie.locations.util.SystemUiHider;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.movie.locations.gcm.SmackCcsClient;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.maps.MapActivity;
//
///**
// * An example full-screen activity that shows and hides the system UI (i.e.
// * status bar and navigation/system bar) with user interaction.
// * 
// * @see SystemUiHider
// */
//public class MapDetailActivity extends FragmentActivity {
//	/**
//	 * Whether or not the system UI should be auto-hidden after
//	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
//	 */
//	private static final boolean AUTO_HIDE = true;
//
//	/**
//	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
//	 * user interaction before hiding the system UI.
//	 */
//	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
//
//	/**
//	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
//	 * will show the system UI visibility upon interaction.
//	 */
//	private static final boolean TOGGLE_ON_CLICK = true;
//
//	/**
//	 * The flags to pass to {@link SystemUiHider#getInstance}.
//	 */
//	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
//
//	/**
//	 * The instance of the {@link SystemUiHider} for this activity.
//	 */
//	private SystemUiHider mSystemUiHider;
//
//	// private String title = "";
//	// private String latitude = "";
//	// private String longitude = "";
//	private String location = "";
//	private User currentUser;
//
//	private LocationCheckinImpl checkinsource;
//
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//
//	private Button checkinButton;
//
//	private String title;
//
//	private CheckIn checkin;
//
//	private Context context;
//	private Context mapDetailContext;
//
//	public static final String EXTRA_MESSAGE = "message";
//	public static final String PROPERTY_REG_ID = "registration_id";
//	private static final String PROPERTY_APP_VERSION = "appVersion";
//	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//
//	/**
//	 * Substitute you own sender ID here. This is the project number you got
//	 * from the API Console, as described in "Getting Started."
//	 */
//	String SENDER_ID = "680842032717";
//
//	/**
//	 * Tag used on log messages.
//	 */
//	static final String TAG = "GCM Demo";
//
//	TextView mDisplay;
//	GoogleCloudMessaging gcm;
//	AtomicInteger msgId = new AtomicInteger();
//
//	String regid;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.activity_map);
//
//		// Thread.currentThread().setContextClassLoader(
//		// 		getClass().getClassLoader());
//		// ClassLoaderHelper.setClassLoader(getClass().getClassLoader());
//
//		mDisplay = (TextView) findViewById(R.id.display);
//
//		// globalize context
//		mapDetailContext = this;
//		context = getApplicationContext();
//
//		// Check device for Play Services APK. If check succeeds, proceed with
//		// GCM registration.
//		if (checkPlayServices()) {
//			gcm = GoogleCloudMessaging.getInstance(this);
//			regid = getRegistrationId(context);
//
//			if (regid.isEmpty()) {
//				registerInBackground();
//				System.out.println("registerInBackground");
//			}
//		} else {
//			Log.i(TAG, "No valid Google Play Services APK found.");
//			System.out.println("No valid Google Play Services APK found.");
//		}
//
//		// set up database connection to check against
//		checkinsource = new LocationCheckinImpl(this);
//		// for (FilmLocation film : datasource.selectRecords()) {
//
//		// TODO: check for fragment injection vulnerability
//		Bundle bundle = getIntent().getExtras();
//
//		currentUser = bundle.getParcelable("localUser");
//		System.out.println("USER PARCEL EXTRA *****************************: "
//				+ currentUser.getDisplayName());
//
////		final View controlsView = findViewById(R.id.fullscreen_content_controls);
////		final View contentView = findViewById(R.id.fullscreen_content);
////
////		// Set up an instance of SystemUiHider to control the system UI for
////		// this activity.
////		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
////				HIDER_FLAGS);
////		mSystemUiHider.setup();
////		mSystemUiHider
////				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
////					// Cached values.
////					int mControlsHeight;
////					int mShortAnimTime;
////
////					@Override
////					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
////					public void onVisibilityChange(boolean visible) {
////						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
////							// If the ViewPropertyAnimator API is available
////							// (Honeycomb MR2 and later), use it to animate the
////							// in-layout UI controls at the bottom of the
////							// screen.
////							if (mControlsHeight == 0) {
////								mControlsHeight = controlsView.getHeight();
////							}
////							if (mShortAnimTime == 0) {
////								mShortAnimTime = getResources().getInteger(
////										android.R.integer.config_shortAnimTime);
////							}
////							controlsView
////									.animate()
////									.translationY(visible ? 0 : mControlsHeight)
////									.setDuration(mShortAnimTime);
////						} else {
////							// If the ViewPropertyAnimator APIs aren't
////							// available, simply show or hide the in-layout UI
////							// controls.
////							controlsView.setVisibility(visible ? View.VISIBLE
////									: View.GONE);
////						}
////
////						if (visible && AUTO_HIDE) {
////							// Schedule a hide().
////							delayedHide(AUTO_HIDE_DELAY_MILLIS);
////						}
////					}
////				});
//
////		// Set up the user interaction to manually show or hide the system UI.
////		contentView.setOnClickListener(new View.OnClickListener() {
////			@Override
////			public void onClick(View view) {
////				if (TOGGLE_ON_CLICK) {
////					mSystemUiHider.toggle();
////				} else {
////					mSystemUiHider.show();
////				}
////			}
////		});
//
//		// Upon interacting with UI controls, delay any scheduled hide()
//		// operations to prevent the jarring behavior of controls going away
//		// while interacting with the UI.
////		findViewById(R.id.dummy_button).setOnTouchListener(
////				mDelayHideTouchListener);
//
//		// Intent intent = getIntent();
//		// title = intent.getStringExtra("title");
//		// latitude = intent.getStringExtra("latitude");
//		// longitude = intent.getStringExtra("longitude");
//		// location = intent.getStringExtra("location");
//		//
//		//
//		// MyItemizedOverlay myItemizedOverlay = null;
//		// MapView mapView = (MapView) findViewById(R.id.mapview);
//		// mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
//		// mapView.setBuiltInZoomControls(true);
//		// mapView.setVisibility(1);
//		//
//		// // FragmentManager fm = getFragmentManager();
//		// // Fragment fragment =
//		// (fm.findFragmentById(R.id.streetmap_fragment));
//		// // android.support.v4.app.FragmentTransaction ft =
//		// fm.beginTransaction();
//		// // ft.remove(fragment);
//		// // ft.commit();
//		//
//		// Drawable marker =
//		// getResources().getDrawable(android.R.drawable.star_on);
//		// int markerWidth = marker.getIntrinsicWidth();
//		// int markerHeight = marker.getIntrinsicHeight();
//		// marker.setBounds(0, markerHeight, markerWidth, 0);
//		//
//		// ResourceProxy resourceProxy = new DefaultResourceProxyImpl(this);
//		//
//		// myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
//		// mapView.getOverlays().add(myItemizedOverlay);
//		//
//		// mapView.getController().setZoom(14);
//		//
//		// System.out.println("LATITUDE: " + latitude);
//		// System.out.println("TITLE: " + title);
//		//
//		// mapView.getController().setCenter(new
//		// GeoPoint(Double.parseDouble(latitude),
//		// Double.parseDouble(longitude)));
//		//
//		// GeoPoint myPoint1 = new GeoPoint(Double.parseDouble(latitude),
//		// Double.parseDouble(longitude));
//		// myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
//
//		// GeoPoint myPoint2 = new GeoPoint(37.6447, -122.2133);
//		// myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");
//
//		// GoogleMap mapview = (GoogleMap) findViewById(R.id.mapfragment);
//
//		// intent.putExtra("title", title);
//		// intent.putExtra("latitude", latitude);
//		// intent.putExtra("longitude", longitude);
//		// intent.putExtra("location", location);
//
//		Intent intent = getIntent();
//		title = intent.getExtras().getString("title");
//		String latitude = intent.getExtras().getString("latitude");
//		String longitude = intent.getExtras().getString("longitude");
//		location = intent.getExtras().getString("location");
//		System.out.println("LOCATION MAP CHECKIN: " + location);
//		// String funFacts = intent.getExtras().getString("funFacts");
//
//		setTitle(title);
//
//		// create new check-in domain object
//		checkin = new CheckIn();
//		// String todaysDate =
//		// DateFormat.format(DateFormat.getDateTimeInstance());
//
//		// Date date = new Date(location.getTime());
//		// DateFormat todaysDate = DateFormat.getDateTimeInstance();
//		// DateFormat formattedDate =
//		// android.text.format.DateFormat.getDateFormat(getApplicationContext());
//
//		// Date date = new Date(location.getTime());
//		// DateFormat dateFormat =
//		// android.text.format.DateFormat.getDateFormat(getApplicationContext());
//		// String dateString = formattedDate.format(todaysDate).toString();
//
//		// mTimeText.setText("Time: " + dateFormat.format(date));
//
//		// DateFormat formattedDate = DateFormat.getDateInstance();
//		// System.out.println("FORMATTED DATE: " + dateString);
//
////		DateFormat[] formats = new DateFormat[] { 
////				DateFormat.getDateInstance(),
////				DateFormat.getDateTimeInstance(), 
////				DateFormat.getTimeInstance() 
////		};
////		
////		for (DateFormat df : formats) {
////			System.out.println(df.format(new Date(0)));
////			df.setTimeZone(TimeZone.getTimeZone("UTC"));
////			System.out.println(df.format(new Date(0)));
////		}
//
////		DateFormat dateFormat = DateFormat.getDateInstance();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
//		Date currentTime = new Date();
//		String currentTimeString = dateFormat.format(currentTime);
//
//		System.out.println("DATE FORMATTED: " + currentTimeString);
//
//		// generating a device-only unique check-in
//		// id here this check-in will be sent to the
//		// server-side rest api and will be persisted there
//		final String UNIQUE_CHECKIN_ID = currentTimeString + title + location + currentUser.getUserId();
//
//		checkin.setCheckinId(UNIQUE_CHECKIN_ID);
//		checkin.setDatetime(currentTimeString);
//		checkin.setFilmTitle(title);
//		checkin.setFilmLocation(location);
//		checkin.setUser(currentUser);
//
//		// TODO: check for null values
//
//		// String formattedLocation = removeParenthesis((values.get(position)
//		// .getLocations()));
//		//
//		// //regex parenthesis
//		// formattedLocation = formattedLocation.replaceAll(" ", "+");
//
//		final LatLng LOCATION = new LatLng(Double.parseDouble(latitude),
//				Double.parseDouble(longitude));
//		// final LatLng KIEL = new LatLng(53.551, 9.993);
//
//		// GoogleMap mapview = ((MapFragment)
//		// getFragmentManager().findFragmentById(R.id.mapfragment))
//		// .getMap();
//		// Marker hamburg = mapview.addMarker(new
//		// MarkerOptions().position(HAMBURG)
//		// .title(title));
//
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
//				.findFragmentById(R.id.mapfragment);
//		GoogleMap mapview = mapFragment.getMap();
//
//		Marker marker = mapview.addMarker(new MarkerOptions()
//				.position(LOCATION));
//
//		if (!location.equals("")) {
//			marker.setTitle(location);
//		}
//
//		if (!title.equals("null")) {
//			marker.setSnippet("Film: " + title);
//		}
//
//		marker.showInfoWindow();
//		// .icon(BitmapDescriptorFactory
//		// .fromResource(R.drawable.ic_launcher)));
//
//		// Move the camera instantly to hamburg with a zoom of 15.
//		mapview.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION, 10));
//
//		// Zoom in, animating the camera.
//		mapview.animateCamera(CameraUpdateFactory.zoomTo(16), 500, null);
////		mapview.animateCamera(CameraUpdateFactory.zoomIn(), 1000, null);
//
////		// currentUser = bundle.getParcelable("localUser");
////		ImageView userAvatar = (ImageView) findViewById(R.id.userAvatarWidget);
////
////		// if (imageLoader != null) {
////		// imageLoader.displayImage(currentUser.getAvatarImageUrl(),
////		// userAvatar);
////		// }
////
////		// set current location on widget
////		TextView userFullNameWidget = (TextView) findViewById(R.id.userFullNameWidget);
////		userFullNameWidget.setText(location);
////
////		checkinButton = (Button) findViewById(R.id.check_in_button);
////
////		// bind check-in button click event
////		checkinButton.setOnClickListener(new View.OnClickListener() {
////			@Override
////			public void onClick(View view) {
////
////				// // checkin async task
////				CheckinAsyncTaskRunner runner = new CheckinAsyncTaskRunner();
////				runner.execute(location);
////
////				// // checkinButtonClickHandler();
////				//
////				// persist check-in domain
////				//
////				// moving this to async task
////				// --------------------------
////				//
////				// checkinsource.open();
////				// checkinsource.createRecord(checkin);
////				// checkinsource.close();
////
////				// set up async task here
////			}
////		});
//
//		// if (!checkinsource.selectRecords().isEmpty()) {
//		// ArrayList<CheckIn> checkinList = new
//		// ArrayList<CheckIn>(checkinsource.selectRecords());
//		// for (CheckIn checkin : checkinList) {
//		// System.out.println("NEW CHECKIN: " + checkin.getFilmLocation());
//		// }
//		// }
//	}
//
//	// private void checkinButtonClickHandler() {
//	// }
//
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//
//		// Trigger the initial hide() shortly after the activity has been
//		// created, to briefly hint to the user that UI controls
//		// are available.
////		delayedHide(5000);
//
//		// make server post to update database
//		//
//		// TODO: research background processes
//		//
//		// create async task for updating database
//		//
//		// this can have it's own ui drawn with a
//		// confirmation and dismiss button
//		//
//		// any tasks/threads left unresolved should
//		// be handled this way
//	}
//
////	/**
////	 * Touch listener to use for in-layout UI controls to delay hiding the
////	 * system UI. This is to prevent the jarring behavior of controls going away
////	 * while interacting with activity UI.
////	 */
////	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
////		@Override
////		public boolean onTouch(View view, MotionEvent motionEvent) {
////			if (AUTO_HIDE) {
////				delayedHide(AUTO_HIDE_DELAY_MILLIS);
////			}
////			return false;
////		}
////	};
////
////	Handler mHideHandler = new Handler();
////	Runnable mHideRunnable = new Runnable() {
////		@Override
////		public void run() {
////			mSystemUiHider.hide();
////		}
////	};
////
////	/**
////	 * Schedules a call to hide() in [delay] milliseconds, canceling any
////	 * previously scheduled calls.
////	 */
////	private void delayedHide(int delayMillis) {
////		mHideHandler.removeCallbacks(mHideRunnable);
////		mHideHandler.postDelayed(mHideRunnable, delayMillis);
////	}
////
////	// @Override
////	// protected boolean isRouteDisplayed() {
////	// // TODO Auto-generated method stub
////	// return false;
////	// }
//
//	private class CheckinAsyncTaskRunner extends
//			AsyncTask<String, String, String> {
//
//		private String resp;
//		private JsonNode json;
//		private ProgressDialog dialog;
//		private String location;
//		private final String MOVIE_LOCATIONS_MESSAGE = "MOVIE_LOCATIONS_MESSAGE";
//
//		@Override
//		protected String doInBackground(String... params) {
//			publishProgress("Sleeping..."); // Calls onProgressUpdate()
//			try {
//				// Do your long operations here and return the result
//				location = params[0];
//				// resp = url;
//
//				// clear existing check-in records
//				// checkinsource.delete();
//
//				System.out.println("CHECKIN: " + checkin);
//
//				// persist checkin data to sqlite database
//				checkinsource.open();
//				checkinsource.createRecord(checkin);
//				checkinsource.close();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("ERROR STACK TRACE");
//				resp = e.getMessage();
//			}
//			return resp;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//		 */
//		@Override
//		protected void onPostExecute(String result) {
//			// execution of result of Long time consuming operation
//			// finalResult.setText(result);
//			// set json data here to serialize
//
//			if (dialog != null) {
//				dialog.dismiss();
//			}
//
//			// for (CheckIn checkin : checkinsource.selectRecords()) {
//			// System.out.println("PERSISTING CHECKOUT: "
//			// + checkin.getFilmLocation());
//			// }
//
//			// make async call to server to persist check-in data
//			// GcmAsyncTaskRunner runner = new GcmAsyncTaskRunner();
//			// runner.execute(MOVIE_LOCATIONS_MESSAGE);
//
//			// final String CHECKIN_ID = "";
//
//			// TODO: refactor rest api to accept params
//			// @RequestMapping(value =
//			// "/checkin/{film}/{location}/{checkinId}/{userId}/{dateTime}",
//			// method = RequestMethod.GET)
//			WebServerAsyncTaskRunner webRunner = new WebServerAsyncTaskRunner();
//			webRunner.execute("http://movie-locations-app.appspot.com/checkin");
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see android.os.AsyncTask#onPreExecute()
//		 */
//		@Override
//		protected void onPreExecute() {
//			// Things to be done before execution of long running operation. For
//			// example showing ProgessDialog
//
//			dialog = new ProgressDialog(mapDetailContext);
//			dialog.setTitle(location);
//			dialog.setMessage("Checking in now!");
//			dialog.setCancelable(false);
//			dialog.setIndeterminate(true);
//			dialog.show();
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
//		 */
//		@Override
//		protected void onProgressUpdate(String... text) {
//			// finalResult.setText(text[0]);
//			// Things to be done while execution of long running operation is in
//			// progress. For example updating ProgessDialog
//		}
//	}
//
//	// send post request to the server with serialize, marshaled check-in object
//	public class WebServerAsyncTaskRunner extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... urls) {
//			String response = "response message";
//			String serverUrl = urls[0];
//			System.out.println("SERVER URL: " + serverUrl);
//			try {
//
//				// // Create and populate a simple object to be used in the
//				// request
//				// Message message = new Message();
//				// message.setId(555);
//				// message.setSubject("test subject");
//				// message.setText("test text");
//				//
//				// // Create a new RestTemplate instance
//				// RestTemplate restTemplate = new RestTemplate();
//				//
//				// // Add the Jackson and String message converters
//				// restTemplate.getMessageConverters().add(new
//				// MappingJackson2HttpMessageConverter());
//				// restTemplate.getMessageConverters().add(new
//				// StringHttpMessageConverter());
//				//
//				// // Make the HTTP POST request, marshaling the request to
//				// JSON, and the response to a String
//				// String response = restTemplate.postForObject(url, message,
//				// String.class);
//
//				// Set the Content-Type header
////				HttpHeaders requestHeaders = new HttpHeaders();
////				requestHeaders.setContentType(new MediaType("application", "json"));
//				
////				// convert object to json string
////				ObjectMapper mapper = new ObjectMapper();
////				String checkinJson = mapper.writeValueAsString(checkin);
//
////				HttpEntity<CheckIn> requestEntity = new HttpEntity<CheckIn>(checkin, requestHeaders);
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//
////				// Create a new RestTemplate instance
////				RestTemplate restTemplate = new RestTemplate();
////
////				// Add the Jackson and String message converters
////				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
////				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
////				
//////				headers.add("Accept", "application/json");
//////		        headers.add("Content-Type", "application/json");
////
////				// Make the HTTP POST request, marshaling the request to JSON,
////				// and the response to a String
//////				ResponseEntity<String> responseEntity = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, String.class);
//////				response = responseEntity.getBody();
////				restTemplate.postForObject(serverUrl, checkin, String.class);
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//
//				// Set the Content-Type header
//				HttpHeaders requestHeaders = new HttpHeaders();
//				requestHeaders.setContentType(new MediaType("application","json"));
//				HttpEntity<CheckIn> requestEntity = new HttpEntity<CheckIn>(checkin, requestHeaders);
//
//				// Create a new RestTemplate instance
//				RestTemplate restTemplate = new RestTemplate();
//
//				// Add the Jackson and String message converters
//				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//
//				// Make the HTTP POST request, marshaling the request to JSON, and the response to a String
//				ResponseEntity<String> responseEntity = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, String.class);
//				response = responseEntity.getBody();
//				
//				System.out.println("REST TEMPLATE POST RESPONSE: " + response);
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
//
//				// // Make the HTTP POST request, marshaling the request to
//				// JSON, and the response to a String
//				// response = restTemplate.postForObject(serverUrl, checkin,
//				// String.class);
//
//				// // Create a new RestTemplate instance
//				// RestTemplate restTemplate = new RestTemplate();
//				//
//				// // Add the String message converter
//				// restTemplate.getMessageConverters().add(
//				// new StringHttpMessageConverter());
//				//
//				//
//				// // Make the HTTP GET request, marshaling the response to a
//				// String
//				// // restTemplate.postForEntity(serverUrl, request,
//				// responseType, uriVariables);
//				// // response = restTemplate.getForObject(serverUrl,
//				// String.class,
//				// // "SpringSource");
//				//
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			System.out.println("SPRING REST CALL RESULT: " + response);
//			return response;
//		}
//
//		private String formatQueryString(String[] urls) {
//			String queryString = "";
//			for (int i = 0; i < urls.length; i++) {
//				queryString += urls[i];
//				queryString += "/";
//			}
//			return queryString;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			System.out.println("SPRING REST CALL ON POST EXECUTE: " + result);
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		// Check device for Play Services APK.
//		checkPlayServices();
//	}
//
//	/**
//	 * Check the device to make sure it has the Google Play Services APK. If it
//	 * doesn't, display a dialog that allows users to download the APK from the
//	 * Google Play Store or enable it in the device's system settings.
//	 */
//	private boolean checkPlayServices() {
//		int resultCode = GooglePlayServicesUtil
//				.isGooglePlayServicesAvailable(this);
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//						PLAY_SERVICES_RESOLUTION_REQUEST).show();
//			} else {
//				Log.i(TAG, "This device is not supported.");
//				finish();
//			}
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * Stores the registration ID and the app versionCode in the application's
//	 * {@code SharedPreferences}.
//	 * 
//	 * @param context
//	 *            application's context.
//	 * @param regId
//	 *            registration ID
//	 */
//	private void storeRegistrationId(Context context, String regId) {
//		final SharedPreferences prefs = getGcmPreferences(context);
//		int appVersion = getAppVersion(context);
//		Log.i(TAG, "Saving regId on app version " + appVersion);
//		SharedPreferences.Editor editor = prefs.edit();
//		editor.putString(PROPERTY_REG_ID, regId);
//		editor.putInt(PROPERTY_APP_VERSION, appVersion);
//		editor.commit();
//		// System.out.println("appVersion: " + appVersion);
//	}
//
//	/**
//	 * Gets the current registration ID for application on GCM service, if there
//	 * is one.
//	 * <p>
//	 * If result is empty, the app needs to register.
//	 * 
//	 * @return registration ID, or empty string if there is no existing
//	 *         registration ID.
//	 */
//	private String getRegistrationId(Context context) {
//		final SharedPreferences prefs = getGcmPreferences(context);
//		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
//		if (registrationId.isEmpty()) {
//			Log.i(TAG, "Registration not found.");
//			return "";
//		}
//		// Check if app was updated; if so, it must clear the registration ID
//		// since the existing regID is not guaranteed to work with the new
//		// app version.
//		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
//				Integer.MIN_VALUE);
//		int currentVersion = getAppVersion(context);
//		if (registeredVersion != currentVersion) {
//			Log.i(TAG, "App version changed.");
//			return "";
//		}
//		return registrationId;
//	}
//
//	/**
//	 * Registers the application with GCM servers asynchronously.
//	 * <p>
//	 * Stores the registration ID and the app versionCode in the application's
//	 * shared preferences.
//	 */
//	private void registerInBackground() {
//		new AsyncTask<Void, Void, String>() {
//			@Override
//			protected String doInBackground(Void... params) {
//				String msg = "";
//				try {
//					if (gcm == null) {
//						gcm = GoogleCloudMessaging.getInstance(context);
//					}
//					regid = gcm.register(SENDER_ID);
//					msg = "Device registered, registration ID=" + regid;
//
//					// You should send the registration ID to your server over
//					// HTTP, so it
//					// can use GCM/HTTP or CCS to send messages to your app.
//					sendRegistrationIdToBackend();
//
//					// For this demo: we don't need to send it because the
//					// device will send
//					// upstream messages to a server that echo back the message
//					// using the
//					// 'from' address in the message.
//
//					// Persist the regID - no need to register again.
//					storeRegistrationId(context, regid);
//				} catch (IOException ex) {
//					msg = "Error :" + ex.getMessage();
//					// If there is an error, don't just keep trying to register.
//					// Require the user to click a button again, or perform
//					// exponential back-off.
//				}
//				return msg;
//			}
//
//			@Override
//			protected void onPostExecute(String msg) {
//				mDisplay.append(msg + "\n");
//			}
//		}.execute(null, null, null);
//	}
//
//	// // Send an upstream message.
//	// public void onClick(final View view) {
//	//
//	// // check if intended element was clicked
//	// if (view == findViewById(R.id.send)) {
//	// new AsyncTask<Void, Void, String>() {
//	// @Override
//	// protected String doInBackground(Void... params) {
//	// String msg = "";
//	// try {
//	// Bundle data = new Bundle();
//	// data.putString("my_message", "Hello World");
//	// data.putString("my_action",
//	// "com.google.android.gcm.demo.app.ECHO_NOW");
//	// String id = Integer.toString(msgId.incrementAndGet());
//	// gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
//	// msg = "Sent message";
//	// } catch (IOException ex) {
//	// msg = "Error :" + ex.getMessage();
//	// }
//	// return msg;
//	// }
//	//
//	// @Override
//	// protected void onPostExecute(String msg) {
//	// mDisplay.append(msg + "\n");
//	// }
//	// }.execute(null, null, null);
//	// } else if (view == findViewById(R.id.clear)) {
//	// mDisplay.setText("");
//	// }
//	// }
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//
//	/**
//	 * @return Application's version code from the {@code PackageManager}.
//	 */
//	private static int getAppVersion(Context context) {
//		try {
//			PackageInfo packageInfo = context.getPackageManager()
//					.getPackageInfo(context.getPackageName(), 0);
//			return packageInfo.versionCode;
//		} catch (NameNotFoundException e) {
//			// should never happen
//			throw new RuntimeException("Could not get package name: " + e);
//		}
//	}
//
//	/**
//	 * @return Application's {@code SharedPreferences}.
//	 */
//	private SharedPreferences getGcmPreferences(Context context) {
//		// This sample app persists the registration ID in shared preferences,
//		// but
//		// how you store the regID in your app is up to you.
//		return getSharedPreferences(MapActivity.class.getSimpleName(),
//				Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * Sends the registration ID to your server over HTTP, so it can use
//	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
//	 * since the device sends upstream messages to a server that echoes back the
//	 * message using the 'from' address in the message.
//	 */
//	private void sendRegistrationIdToBackend() {
//		// Your implementation here.
//	}
//
//	public class GcmAsyncTaskRunner extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... urls) {
//			// String response = "response message";
//			// int URL_INDEX = 0;
//			final String MOVIE_LOCATIONS_MESSAGE = urls[0];
//
//			// send message upstream
//			String msg = "";
//			Bundle data = new Bundle();
//			data.putString("userId", "user_id");
//			data.putString("checkinMessage", MOVIE_LOCATIONS_MESSAGE);
//			data.putString("checkinId", "checkin_01");
//			String id = Integer.toString(msgId.incrementAndGet());
//
//			// System.out.println("DATA: " + data);
//			// System.out.println("ID:" + id);
//			// System.out.println("MSG_ID: " + msgId);
//
//			//
//			// WebServerAsyncTaskRunner webRunner = new
//			// WebServerAsyncTaskRunner();
//			// webRunner.execute("https://android.googleapis.com/gcm/send");
//
//			// gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
//			msg = "Sent message";
//			System.out.println(TAG + " STATUS: " + msg);
//			return msg;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			System.out.println("RESULT ON POST EXECUTE: " + result);
//		}
//	}
//
//}
