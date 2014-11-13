package com.movie.locations.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Image;
import com.google.android.gms.plus.model.people.PersonBuffer;
//import com.googleandroid.maps.MapActivity;










import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.movie.locations.R;
import com.movie.locations.R.layout;
import com.movie.locations.R.menu;
import com.movie.locations.dao.AchievementImpl;
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.ConclusionCardImpl;
import com.movie.locations.dao.GameTitleImpl;
import com.movie.locations.dao.LocationCheckinImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.ClassLoaderHelper;
import com.movie.locations.domain.FastBlurBitmap;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.FilmLocationParcelableCollection;
import com.movie.locations.domain.MoviePostersHashMap;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.gcm.GcmIntentService;
import com.movie.locations.service.AchievementService;
import com.movie.locations.service.BagItemService;
import com.movie.locations.service.ConclusionCardService;
import com.movie.locations.service.FilmLocationService;
import com.movie.locations.service.GameTitleService;
import com.movie.locations.service.QuizItemService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener,
		ResultCallback<People.LoadPeopleResult>, View.OnClickListener {

	private static final String TAG = "android-plus-quickstart";

	private static final int STATE_DEFAULT = 0;
	private static final int STATE_SIGN_IN = 1;
	private static final int STATE_IN_PROGRESS = 2;

	private static final int RC_SIGN_IN = 0;

	private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

	private static final String SAVED_PROGRESS = "sign_in_progress";

	// GoogleApiClient wraps our service connection to Google Play services and
	// provides access to the users sign in state and Google's APIs.
	private GoogleApiClient mGoogleApiClient;

	// We use mSignInProgress to track whether user has clicked sign in.
	// mSignInProgress can be one of three values:
	//
	// STATE_DEFAULT: The default state of the application before the user
	// has clicked 'sign in', or after they have clicked
	// 'sign out'. In this state we will not attempt to
	// resolve sign in errors and so will display our
	// Activity in a signed out state.
	// STATE_SIGN_IN: This state indicates that the user has clicked 'sign
	// in', so resolve successive errors preventing sign in
	// until the user has successfully authorized an account
	// for our app.
	// STATE_IN_PROGRESS: This state indicates that we have started an intent to
	// resolve an error, and so we should not start further
	// intents until the current intent completes.
	private int mSignInProgress;

	// Used to store the PendingIntent most recently returned by Google Play
	// services until the user clicks 'sign in'.
	private PendingIntent mSignInIntent;

	// Used to store the error code most recently returned by Google Play
	// services
	// until the user clicks 'sign in'.
	private int mSignInError;

	// findViewById(R.id.sign_in_button).setOnClickListener(this);

	/*
	 * Track whether the sign-in button has been clicked so that we know to
	 * resolve all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;

	/*
	 * Store the connection result from onConnectionFailed callbacks so that we
	 * can resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;

	private SignInButton mSignInButton;
	private Button mSignOutButton;
	private Button mRevokeButton;
	private Button entryButton;
	private TextView mStatus;
	private ListView mCirclesListView;
	private ArrayAdapter<String> mCirclesAdapter;
	private ArrayList<String> mCirclesList;

	private TextView finalResult;
	private final String FILM_LOCATIONS_URI = "https://data.sfgov.org/api/views/yitu-d5am/rows.json";
	private int FILM_LOCATIONS_COUNT;
	private Context context;
	private ArrayList<FilmLocation> filmList;
	private ArrayList<QuizItem> quizList;
	private MovieLocationsImpl datasource;
	private LocationCheckinImpl checkinsource;
	private QuizItemImpl quizitemsource;
	private FilmLocationService service;
	private QuizItemService quizservice;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private BagItemImpl bagitemsource;
	private ConclusionCardImpl conclusioncardsource;
	private GameTitleImpl gametitlesource;
	private AchievementImpl achievementsource;

	private boolean mIntentInProgress;

	private Intent userProfileIntent;

	private User localUser;
	


	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	private String SENDER_ID = "543788746297";

	private TextView mDisplay;
	private GoogleCloudMessaging gcm;
	private AtomicInteger msgId = new AtomicInteger();

	private String regid;

	private UserImpl usersource;
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// savedInstanceState.setClassLoader(this.getClass().getClassLoader());

		// Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		// ClassLoaderHelper.setClassLoader(getClass().getClassLoader());

		// globalize context and pass reference to data service
		context = this;

		// google plus authentication here
		entryButton = (Button) findViewById(R.id.entry_button);
		mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		mSignOutButton = (Button) findViewById(R.id.sign_out_button);
		mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
		mStatus = (TextView) findViewById(R.id.sign_in_status);
		mCirclesListView = (ListView) findViewById(R.id.circles_list);

		mSignInButton.setOnClickListener(this);
		mSignOutButton.setOnClickListener(this);
		mRevokeButton.setOnClickListener(this);
		entryButton.setOnClickListener(this);

		mCirclesList = new ArrayList<String>();
		mCirclesAdapter = new ArrayAdapter<String>(this,
				R.layout.circle_member, mCirclesList);
		mCirclesListView.setAdapter(mCirclesAdapter);

		if (savedInstanceState != null) {
			mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS,
					STATE_DEFAULT);
		}

		mGoogleApiClient = buildGoogleApiClient();

		// to re-initialize database uncomment async task here
		usersource = new UserImpl(this);
		datasource = new MovieLocationsImpl(this);
		checkinsource = new LocationCheckinImpl(this);
		quizitemsource = new QuizItemImpl(this);
		bagitemsource = new BagItemImpl(this);
		conclusioncardsource = new ConclusionCardImpl(this);
		gametitlesource = new GameTitleImpl(this);
		achievementsource = new AchievementImpl(this);
		
//		if (datasource.selectRecords().isEmpty()) {
//			AsyncTaskRunner runner = new AsyncTaskRunner();
//			finalResult = (TextView) findViewById(R.id.textView1);
//			runner.execute(FILM_LOCATIONS_URI);
//		}

		// ImageView bgImage = (ImageView) findViewById(R.id.imageView1);
		// bgImage.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// Intent intent = new Intent(MainActivity.this,
		// FilmLocationPagerActivity.class);
		// startActivity(intent);
		// }
		// });

		// final String MOVIE_POSTER_URL =
		// "https://www.filmposters.com/images/posters/12819.jpg";
		// imageLoader.displayImage(MOVIE_POSTER_URL, bgImage);

		switch (mSignInProgress) {
		case STATE_DEFAULT:
			// not logged in
			entryButton.setEnabled(false);
			break;
		case STATE_SIGN_IN:
			// already logged in
			mSignInClicked = false;
			mSignInButton.setEnabled(false);
			entryButton.setEnabled(true);
			break;
		}
		


		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
				System.out.println("registerInBackground");
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
			System.out.println("No valid Google Play Services APK found.");
		}
		
		// configure universal image loader
		// and allow image caching
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.displayer(new FadeInBitmapDisplayer(500)).build();

		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();

		// initialize image loader
		ImageLoader.getInstance().init(config);
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
		// put connected clients into array on web server
		//
		// TODO: create rest api to send client id to server
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Check device for Play Services APK.
		checkPlayServices();
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;
					System.out.println("regid: " + regid);

					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
//					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
//				mDisplay.append(msg + "\n");
				System.out.println("************ Successfully connected. ************");
			}
		}.execute(null, null, null);
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
		// System.out.println("appVersion: " + appVersion);
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(this.getClass().getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private GoogleApiClient buildGoogleApiClient() {
		// When we build the GoogleApiClient we specify where connected and
		// connection failed callbacks should be returned, which Google APIs our
		// app uses and which OAuth 2.0 scopes our app requests.
		return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_PROGRESS, mSignInProgress);
	}

	@Override
	public void onClick(View v) {
		if (!mGoogleApiClient.isConnecting()) {
			// We only process button clicks when GoogleApiClient is not
			// transitioning
			// between connected and not connected.
			switch (v.getId()) {
			case R.id.sign_in_button:
				mStatus.setText(R.string.status_signing_in);
				if (!mGoogleApiClient.isConnecting()) {
					mSignInClicked = true;
					resolveSignInError();
				}
				break;
			case R.id.sign_out_button:
				// We clear the default account on sign out so that Google Play
				// services will not return an onConnected callback without user
				// interaction.
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
				mGoogleApiClient.connect();
				break;
			case R.id.revoke_access_button:
				// After we revoke permissions for the user with a
				// GoogleApiClient
				// instance, we must discard it and create a new one.
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				// Our sample has caches no user data from Google+, however we
				// would normally register a callback on
				// revokeAccessAndDisconnect
				// to delete user data so that we comply with Google developer
				// policies.
				Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
				mGoogleApiClient = buildGoogleApiClient();
				mGoogleApiClient.connect();
				break;
			case R.id.entry_button:
//				// configure universal image loader
//				// and allow image caching
//				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//						.cacheInMemory(true).cacheOnDisc(true)
//						.displayer(new FadeInBitmapDisplayer(500)).build();
//
//				// Create global configuration and initialize ImageLoader with this
//				// configuration
//				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//						getApplicationContext()).defaultDisplayImageOptions(
//						defaultOptions).build();
//
//				// initialize image loader
//				ImageLoader.getInstance().init(config);
				
				// TODO: refactor this
				//
				// set current user in service
				GcmIntentService.setCurrentUserId(localUser.getUserId());
				
				userProfileIntent = new Intent(MainActivity.this, NewsActivity.class);
				userProfileIntent.putExtra("localUser", localUser);
//
//				// start user profile intent
				startActivity(userProfileIntent);
				break;
			}
		}
	}

	/*
	 * onConnected is called when our Activity successfully connects to Google
	 * Play services. onConnected indicates that an account was selected on the
	 * device, that the selected account has granted any requested permissions
	 * to our app and that we were able to establish a service connection to
	 * Google Play services.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		// Reaching onConnected means we consider the user signed in.
		Log.i(TAG, "onConnected");

		// Update the user interface to reflect that the user is signed in.
		mSignInButton.setEnabled(false);
		mSignOutButton.setEnabled(true);
		mRevokeButton.setEnabled(true);
		entryButton.setEnabled(true);

		// Retrieve some profile information to personalize our app for the
		// user.
		Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		String verifiedEmailAddress = Plus.AccountApi
				.getAccountName(mGoogleApiClient);

		mStatus.setText(String.format(
				getResources().getString(R.string.signed_in_as),
				currentUser.getDisplayName()));

		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
				this);

		// Indicate that the sign in process is complete.
		mSignInProgress = STATE_DEFAULT;

		mSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		
		createDataServices();

		// // start new intent
		// Intent intent = new Intent(MainActivity.this,
		// FilmLocationPagerActivity.class);
		// startActivity(intent);

		
		
		// final String UNIQUE_USER_ID = currentUser.getDisplayName() + "::" +
		// formattedDate;

//		private String userId;
//		private String displayName;
//		private String emailAddress;
//		private String avatarImageUrl;
//		private String currentLevel;
//		private String currentPoints;
		
		// TODO: remove this - clear current records
//		usersource.delete();
		
		// open user database connection
		usersource.open();
		
//		String formattedDate = DateFormat.getDateTimeInstance().toString();
		
//		currentUser.getId();
		String userImageUrl = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=50";
		Image userImage = currentUser.getImage();
		
		// TODO: check for existing user and pull down all data
		//
		// also check against vip ids and update on server
		//
		// TODO: create settings activity and server entities to store settings
		User existingUser = null;
		
//		if (usersource.selectRecords() == null) {


		if (userImage.hasUrl()) {
			userImageUrl = userImage.getUrl();
		}
		
		existingUser = usersource.selectRecordById(currentUser.getId().toString());
		String currentUserSid = currentUser.getId();
		
		if (existingUser == null) {
			// create new user with attributes
			localUser = new User();
			localUser.setUserId(currentUserSid);
			
//			TODO: concat datetime on the front
//			 of this value using android api
			localUser.setUserSid(currentUserSid);
			
			localUser.setUserClientId(regid);
			localUser.setDisplayName(currentUser.getDisplayName());
			localUser.setEmailAddress(verifiedEmailAddress);
			localUser.setAvatarImageUrl(userImageUrl);
			localUser.setCurrentLevel("1");
			localUser.setCurrentPoints("0");
			
			// TODO: REMOVE THIS IF UNNEEDED
			final String POINTS_USER_ID = "POINTS_USER_ID_" + currentUserSid;
			localUser.setPointsUserId(POINTS_USER_ID);
			localUser.setPoints("0");
			localUser.setBonusPoints("0");
			localUser.setWorldCount("0");
			
			localUser.setEmailNotifications("true");
			localUser.setMobileNotifications("true");
			
			// persist new user
			usersource.createRecord(localUser);
		} else {

			// create new user with attributes
			localUser = new User();
			localUser.setUserId(currentUserSid);

//			TODO: concat datetime on the front
//			 of this value using android api
			localUser.setUserSid(currentUserSid);
			
			localUser.setUserClientId(regid);
			localUser.setDisplayName(currentUser.getDisplayName());
			localUser.setEmailAddress(verifiedEmailAddress);
			localUser.setAvatarImageUrl(userImageUrl);
			localUser.setCurrentLevel(existingUser.getCurrentLevel());
			localUser.setCurrentPoints(existingUser.getCurrentPoints());
			localUser.setPointsUserId(existingUser.getPointsUserId());
			localUser.setPoints(existingUser.getPoints());
			localUser.setBonusPoints(existingUser.getBonusPoints());
			localUser.setWorldCount(existingUser.getWorldCount());
			localUser.setEmailNotifications(existingUser.getEmailNotifications());
			localUser.setMobileNotifications(existingUser.getMobileNotifications());
			System.out.println("EXISTING USER LOGIN - CURRENT POINTS: " + existingUser.getCurrentPoints());
		}
//		}
		
		// TODO: check if user id/client id changed on the server
		
		ArrayList<User> userList = usersource.selectRecords();
		
		for (User user : userList) {
			System.out.println("JUST ADDED NEW USER - CLIENT SID: " + user.getUserSid());
			System.out.println("JUST ADDED NEW USER - CURRENT POINTS: " + user.getCurrentPoints());
		}
		
		// upload new user to server
		
		
		usersource.close();

		// upload new user to data store
		//
		// TODO: update existing user on server
//		NewUserTaskRunner runner = new NewUserTaskRunner();
//		runner.execute("http://movie-locations-app.appspot.com/secure/addUser");
		
//		System.out.println("UNIQUE_USER_ID: " + currentUser.getId());
//		System.out.println("USERNAME: " + localUser.getDisplayName());
//		System.out.println("EMAIL: " + localUser.getEmailAddress());

		// persist user domain to the server
		//
		// TODO: create async call here

		// checkinsource.open();
		// checkinsource.createRecord(checkinItem);
		// checkinsource.close();

		// TODO: write rest server call to serialize this data
		//
		//
		// i.e. server.addNewUser(localUser);
		// - rebuild user domain on server
		// - check if email address already exists
		// - serialize if not

		
//		System.out.println("AVATAR: " + localUser.getAvatarImageUrl());

		// userProfileIntent = new Intent(MainActivity.this,
		// UserDetailActivity.class);
		//
		// userProfileIntent.putExtra("localUser", localUser);
	}
	
//	private String createUserSid(String displayName) {
//		String tempSid = displayName.replaceAll(" ","");
//		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
//		String format = s.format(new Date());
//		final String finalSid = tempSid.concat("_").concat(format);
//		return finalSid;
//	}

	private void createDataServices() {
		if (datasource.selectRecords().isEmpty()) {
			FilmLocationService locationService = new FilmLocationService();
			InputStream locationStream = getResources().openRawResource(R.raw.locations_copy);
			locationService.createContentValues(locationStream, context);
		}
		if (quizitemsource.selectRecords().isEmpty()) {
			QuizItemService quizService = new QuizItemService();
			InputStream quizStream = getResources().openRawResource(R.raw.quiz_item_copy);
			quizService.createContentValues(quizStream, context);
		}
		if (bagitemsource.selectRecords().isEmpty()) {
			BagItemService bagService = new BagItemService();
			InputStream bagStream = getResources().openRawResource(R.raw.bag_item_copy);
			bagService.createContentValues(bagStream, context);
		}
		if (conclusioncardsource.selectRecords().isEmpty()) {
			ConclusionCardService cardService = new ConclusionCardService();
			InputStream cardStream = getResources().openRawResource(R.raw.conclusion_card_copy);
			cardService.createContentValues(cardStream, context);
		}
		if (gametitlesource.selectRecords().isEmpty()) {
			GameTitleService titleService = new GameTitleService();
			InputStream titleStream = getResources().openRawResource(R.raw.game_title_copy);
			titleService.createContentValues(titleStream, context);
		}
		if (achievementsource.selectRecords().isEmpty()) {
			AchievementService titleService = new AchievementService();
			InputStream achievementStream = getResources().openRawResource(R.raw.achievement_copy);
			titleService.createContentValues(achievementStream, context);
		}
	}

	/*
	 * onConnectionFailed is called when our Activity could not connect to
	 * Google Play services. onConnectionFailed indicates that the user needs to
	 * select an account, grant permissions or resolve an error in order to sign
	 * in.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			// Store the ConnectionResult so that we can use it later when the
			// user clicks
			// 'sign-in'.
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}
	}

	/*
	 * Starts an appropriate intent or dialog for user interaction to resolve
	 * the current error preventing the user from being signed in. This could be
	 * a dialog allowing the user to select an account, an activity allowing the
	 * user to consent to the permissions being requested by your app, a setting
	 * to enable device networking, etc.
	 */
	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, // your
																	// activity
						RC_SIGN_IN);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		switch (requestCode) {
		case RC_SIGN_IN:
			if (responseCode == RESULT_OK) {
				// If the error resolution was successful we should continue
				// processing errors.
				mSignInProgress = STATE_SIGN_IN;
			} else {
				// If the error resolution was not successful or the user
				// canceled,
				// we should stop processing errors.
				mSignInProgress = STATE_DEFAULT;
			}

			if (!mGoogleApiClient.isConnecting()) {
				// If Google Play services resolved the issue with a dialog then
				// onStart is not called so we need to re-attempt connection
				// here.
				mGoogleApiClient.connect();
			}
			break;
		}

		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onResult(LoadPeopleResult peopleData) {
		if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
			mCirclesList.clear();
			PersonBuffer personBuffer = peopleData.getPersonBuffer();
			try {
				int count = personBuffer.getCount();
				for (int i = 0; i < count; i++) {
					mCirclesList.add(personBuffer.get(i).getDisplayName());
				}
			} finally {
				personBuffer.close();
			}

			mCirclesAdapter.notifyDataSetChanged();
		} else {
			Log.e(TAG,
					"Error requesting visible circles: "
							+ peopleData.getStatus());
		}
	}

	private void onSignedOut() {
		// Update the UI to reflect that the user is signed out.
		mSignInButton.setEnabled(true);
		mSignOutButton.setEnabled(false);
		mRevokeButton.setEnabled(false);

		mStatus.setText(R.string.status_signed_out);

		mCirclesList.clear();
		mCirclesAdapter.notifyDataSetChanged();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// The connection to Google Play services was lost for some reason.
		// We call connect() to attempt to re-establish the connection or get a
		// ConnectionResult that we can attempt to resolve.
		mGoogleApiClient.connect();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PLAY_SERVICES_ERROR:
			if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
				return GooglePlayServicesUtil.getErrorDialog(mSignInError,
						this, RC_SIGN_IN,
						new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								Log.e(TAG,
										"Google Play services resolution cancelled");
								mSignInProgress = STATE_DEFAULT;
								mStatus.setText(R.string.status_signed_out);
							}
						});
			} else {
				return new AlertDialog.Builder(this)
						.setMessage(R.string.play_services_error)
						.setPositiveButton(R.string.close,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Log.e(TAG,
												"Google Play services error could not be "
														+ "resolved: "
														+ mSignInError);
										mSignInProgress = STATE_DEFAULT;
										mStatus.setText(R.string.status_signed_out);
									}
								}).create();
			}
		default:
			return super.onCreateDialog(id);
		}
	}

	public class NewUserTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
		private ProgressDialog dialog;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			String url = params[0];
			
			try {
				
//				String username = "tempUsername";
//				String password = "tempPassword";
				
//				// Set the Content-Type header
//				HttpHeaders requestHeaders = new HttpHeaders();
//				requestHeaders.setContentType(new MediaType("application",
//						"json"));
//				HttpEntity<QuizItem> requestEntity = new HttpEntity<QuizItem>(
//						quizItem, requestHeaders);
//				System.out.println("REST TEMPLATE PRE RESPONSE: "
//						+ quizItem.getSubmittedIndex());
//
//				// Create a new RestTemplate instance
//				RestTemplate restTemplate = new RestTemplate();
//
//				// Add the Jackson and String message converters
//				restTemplate.getMessageConverters().add(
//						new MappingJackson2HttpMessageConverter());
//				restTemplate.getMessageConverters().add(
//						new StringHttpMessageConverter());
				


				try {
					
					// Do your long operations here and return the result
//					String url = params[0];
					// resp = "async call in progress";

					// Set the Content-Type header
					HttpHeaders requestHeaders = new HttpHeaders();
					requestHeaders.setContentType(new MediaType("application", "json"));
					HttpEntity<User> requestEntity = new HttpEntity<User>(localUser, requestHeaders);
//					System.out.println("REST TEMPLATE PRE RESPONSE: " + quizItem.getSubmittedIndex());

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
					User response = responseEntity.getBody();

//					System.out.println("REST TEMPLATE POST RESPONSE: " + response.getAnswered());
					
					
					
					
					// check server for existing user, then update regId if that changed at all
					//
					// TODO: check if google plus id will change at all
					
					
//					// Set the username and password for creating a Basic Auth request
////					HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
//					HttpHeaders requestHeaders = new HttpHeaders();
//					requestHeaders.setContentType(new MediaType("application", "json"));
////					requestHeaders.setAuthorization(authHeader);
//					HttpEntity<User> requestEntity = new HttpEntity<User>(requestHeaders);
//
//					// Create a new RestTemplate instance
//					RestTemplate restTemplate = new RestTemplate();
//
//					// Add the String message converter
//					restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//					
//				    // Make the HTTP GET request to the Basic Auth protected URL
//				    ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
//					User response = responseEntity.getBody();
					System.out.println("NEW USER REGISTERED: " + response.getDisplayName());
					System.out.println("NEW USER REGISTERED WORLD COUNT: " + response.getWorldCount());
					System.out.println("NEW USER REGISTERED CURRENT POINTS: " + response.getCurrentPoints());
					System.out.println("NEW USER REGISTERED CURRENT LEVEL: " + response.getCurrentLevel());
					
					userProfileIntent = new Intent(MainActivity.this, NewsActivity.class);
					userProfileIntent.putExtra("localUser", response);

					// start user profile intent
					startActivity(userProfileIntent);
//					finish();
					
				} catch (HttpClientErrorException e) {
				    Log.e(TAG, e.getLocalizedMessage(), e);
				    // Handle 401 Unauthorized response
				}
				
				
				
				
				
				
//				FilmArrayList filmArrayList = new FilmArrayList();
//				filmArrayList.setFilmList(filmList);
//				
//				// Do your long operations here and return the result
//				String dataUrl = "http://movie-locations-app.appspot.com/submitLocObj";
//				
//				// resp = "async call in progress";
//
//				// Set the Content-Type header
//				HttpHeaders requestHeaders = new HttpHeaders();
//				requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity<FilmArrayList> requestEntity = new HttpEntity<FilmArrayList>(filmArrayList, requestHeaders);
////				System.out.println("REST TEMPLATE PRE RESPONSE: " + item.getTitle());
//
//				// Create a new RestTemplate instance
//				RestTemplate restTemplate = new RestTemplate();
//
//				// Add the Jackson and String message converters
//				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//
//				// Make the HTTP POST request, marshaling the request to
//				// JSON, and the response to a String
//				// ResponseEntity<String> responseEntity =
//				// restTemplate.exchange(url, HttpMethod.POST,
//				// requestEntity, String.class);
//				// String response = responseEntity.getBody();
//				
//				// String result = rest.postForObject(url, map, String.class);
////				ResponseEntity<String> responseEntity = restTemplate.exchange(dataUrl, HttpMethod.POST, requestEntity, String.class);
////				String response = restTemplate.postForObject(dataUrl, requestEntity, String.class);
//				
//				ResponseEntity<FilmArrayList> responseArrayList = restTemplate.exchange(dataUrl, HttpMethod.POST, requestEntity, FilmArrayList.class);
//				
////				@SuppressWarnings("unchecked")
////				ArrayList<FilmLocation> list = responseEntity.getBody();
//
//				System.out.println("REST TEMPLATE POST RESPONSE: " + responseArrayList);
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
			if (dialog != null) {
				dialog.dismiss();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog

			dialog = new ProgressDialog(context);
			dialog.setTitle("Updating...");
			dialog.setMessage("This app requires a Google Plus account and an internet connection to save progress.");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(String... text) {
			// finalResult.setText(text[0]);
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}

	private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
		private JsonNode json;
		private ProgressDialog dialog;
		private ArrayList<FilmLocation> locationList;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				// Do your long operations here and return the result
				String url = params[0];
				// resp = url;

//				service = new FilmLocationService();
//				filmList = service.getMovieData().buildLocationObjects()
//						.returnFilmList();
//				FILM_LOCATIONS_COUNT = filmList.size();
//
//				System.out.println("FILM_LOCATIONS_COUNT: "
//						+ FILM_LOCATIONS_COUNT);
//
//				resp = filmList.toString();
//
//				// clear existing film location records
//				datasource.delete();
//
//				
//				
//				
//				locationList = new ArrayList<FilmLocation>();
//				
//				
//				// persist to sqlite database
//				for (FilmLocation item : filmList) {
//					datasource.open();
//					datasource.createRecord(item);
//					datasource.close();
//
//					// send to callback
////					resp = response.getTitle();
//				}
//
//				// clear existing check-in records
//				checkinsource.delete();
//				
//				// clear all quiz question records
//				quizitemsource.delete();
//				
//				// TODO: get trivia questions from server
////				QuizItem quizItem = new QuizItem();
//				
//				quizservice = new QuizItemService();
//				quizList = quizservice.getQuizData().buildQuizObjects().returnQuizList();
//				
//				for (QuizItem item : quizList) {
//					quizitemsource.open();
//					quizitemsource.createRecord(item);
//					quizitemsource.close();
////					System.out.println("item get title: " + item.getFilmTitle());
//				}
//				
////				System.out.println("JSON: " + json);
//				
////				for (int i = 0; i < 10; i++) {
////					quizitemsource.open();
////					
////					if (i == 0) {
////						quizItem.setFilmTitle("180");
////						quizItem.setQuestionText("What year was 180 released?");
////						quizItem.setAnswer1("2009");
////						quizItem.setAnswer2("2010");
////						quizItem.setAnswer3("2011");
////						quizItem.setAnswer4("2012");
////						quizItem.setQuestionId("questionId_"+ i);
////						quizItem.setDateTime("dateTime");
////						quizItem.setCorrectAnswerIndex(0);
////						quizItem.setAnswered("false");
////					} else {
////						quizItem.setFilmTitle("Vertigo");
////						quizItem.setQuestionText("questionText");
////						quizItem.setAnswer1("answer1");
////						quizItem.setAnswer2("answer1");
////						quizItem.setAnswer3("answer1");
////						quizItem.setAnswer4("answer1");
////						quizItem.setDateTime("dateTime");
////						quizItem.setCorrectAnswerIndex(0);
////						quizItem.setAnswered("false");
////					}
////					
////					
////					
////					
////					
////					quizitemsource.createRecord(quizItem);
////					quizitemsource.close();
////				}
//
//				// TODO: write server-side rest api call
//				// to pull down check-in domain objects
//				//
//				// TODO: write server-side rest api call
//				// to add new user to database
//				//
//				// TODO: write server-side rest api call
//				// to sync new user check-in
//				//
//				// these server calls should be implemented
//				// in a way so that the user can multi-task
//				// when no internet is available and have
//				// the option to cancel if unresponsive
//				//
//				// the sync can be re-initiated when the user
//				// attempts to check-in somewhere else and
//				// saving progress inside a queue
//				//
//				// we're not persisting user objects locally
//				// because we're pulling down check-in objects
//				// which have a reference to the user's id
//				//
//				// TODO: see if an 'android back-online-listener'
//				// exists, and if so, re-factor this async call
//				// into a reusable class
//				// 
//				// we can create a configuration file for this
//				//
//				// if no internet connection, then display
//				// a button dialog telling user to try again
//				// or dismiss
//				
//				
//				
////				uploadToServer();
				
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
			
//			UploadFilmLocationsTaskRunner uploader = new UploadFilmLocationsTaskRunner();
//			uploader.execute("url");
			
			if (dialog != null) {
				dialog.dismiss();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog

			dialog = new ProgressDialog(context);
			dialog.setTitle("Initializing...");
			dialog.setMessage("Downloading over 800 film locations! Don't worry, this is a one-time thing.");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(String... text) {
			// finalResult.setText(text[0]);
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}
	
//	@SuppressWarnings("serial")
//	public class FilmArrayList implements Serializable {
//		private ArrayList<FilmLocation> filmList;
//		
//		public FilmArrayList() {
//			// empty constructor
//		}
//
//		public ArrayList<FilmLocation> getFilmList() {
//			return filmList;
//		}
//
//		public void setFilmList(ArrayList<FilmLocation> filmList) {
//			this.filmList = filmList;
//		}
//		
//	}
//
//	private class UploadFilmLocationsTaskRunner extends AsyncTask<String, String, String> {
//
//		private String resp;
//		private JsonNode json;
//		private ProgressDialog dialog;
//		private ArrayList<FilmLocation> locationList;
//
//		@Override
//		protected String doInBackground(String... params) {
//			publishProgress("Sleeping..."); // Calls onProgressUpdate()
//			try {
//				
//				FilmArrayList filmArrayList = new FilmArrayList();
//				filmArrayList.setFilmList(filmList);
//				
//				// Do your long operations here and return the result
//				String dataUrl = "http://movie-locations-app.appspot.com/submitLocObj";
//				
//				// resp = "async call in progress";
//
//				// Set the Content-Type header
//				HttpHeaders requestHeaders = new HttpHeaders();
//				requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity<FilmArrayList> requestEntity = new HttpEntity<FilmArrayList>(filmArrayList, requestHeaders);
////				System.out.println("REST TEMPLATE PRE RESPONSE: " + item.getTitle());
//
//				// Create a new RestTemplate instance
//				RestTemplate restTemplate = new RestTemplate();
//
//				// Add the Jackson and String message converters
//				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//
//				// Make the HTTP POST request, marshaling the request to
//				// JSON, and the response to a String
//				// ResponseEntity<String> responseEntity =
//				// restTemplate.exchange(url, HttpMethod.POST,
//				// requestEntity, String.class);
//				// String response = responseEntity.getBody();
//				
//				// String result = rest.postForObject(url, map, String.class);
////				ResponseEntity<String> responseEntity = restTemplate.exchange(dataUrl, HttpMethod.POST, requestEntity, String.class);
////				String response = restTemplate.postForObject(dataUrl, requestEntity, String.class);
//				
//				ResponseEntity<FilmArrayList> responseArrayList = restTemplate.exchange(dataUrl, HttpMethod.POST, requestEntity, FilmArrayList.class);
//				
////				@SuppressWarnings("unchecked")
////				ArrayList<FilmLocation> list = responseEntity.getBody();
//
//				System.out.println("REST TEMPLATE POST RESPONSE: " + responseArrayList);
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
//			if (dialog != null) {
//				dialog.dismiss();
//			}
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
//			dialog = new ProgressDialog(context);
//			dialog.setTitle("Initializing...");
//			dialog.setMessage("Downloading over 800 film locations! Don't worry, this is a one-time thing.");
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

}
