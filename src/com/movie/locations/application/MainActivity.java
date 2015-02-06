package com.movie.locations.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;
import com.movie.locations.R;
import com.movie.locations.database.UserImpl;
import com.movie.locations.domain.User;
import com.movie.locations.service.AchievementService;
import com.movie.locations.service.BagItemService;
import com.movie.locations.service.ConclusionCardService;
import com.movie.locations.service.FilmLocationService;
import com.movie.locations.service.GameTitleService;
import com.movie.locations.service.QuizItemService;
import android.os.AsyncTask;
import android.content.Context;
import android.view.Menu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener,
		ResultCallback<People.LoadPeopleResult>, View.OnClickListener {

	private static final String TAG = "android-plus-quickstart";
	private static final int STATE_DEFAULT = 0;
	private static final int STATE_SIGN_IN = 1;
//	private static final int STATE_IN_PROGRESS = 2;
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
//	private PendingIntent mSignInIntent;

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
//	private Button mRevokeButton;
	private Button entryButton;
	private TextView mStatus;
//	private ListView mCirclesListView;
	private ArrayAdapter<String> mCirclesAdapter;
	private ArrayList<String> mCirclesList;
	private Context context;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private boolean mIntentInProgress;
	private Intent userProfileIntent;
	private User localUser;
	
	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	private String SENDER_ID = "543788746297";

	private GoogleCloudMessaging gcm;
	private String regid;
	private UserImpl usersource;
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		// globalize context and pass reference to data service
		context = this;

		// google plus authentication here
		entryButton = (Button) findViewById(R.id.entry_button);
		mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		mSignOutButton = (Button) findViewById(R.id.sign_out_button);
//		mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
		mStatus = (TextView) findViewById(R.id.sign_in_status);
//		mCirclesListView = (ListView) findViewById(R.id.circles_list);

		mSignInButton.setOnClickListener(this);
		mSignOutButton.setOnClickListener(this);
//		mRevokeButton.setOnClickListener(this);
		entryButton.setOnClickListener(this);

		mCirclesList = new ArrayList<String>();
		mCirclesAdapter = new ArrayAdapter<String>(this,
				R.layout.circle_member, mCirclesList);
//		mCirclesListView.setAdapter(mCirclesAdapter);

		if (savedInstanceState != null) {
			mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS,
					STATE_DEFAULT);
		}

		mGoogleApiClient = buildGoogleApiClient();

		// to re-initialize database uncomment async task here
		usersource = new UserImpl(this);
		
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
			int id = v.getId();
			if (id == R.id.sign_in_button) {
				mStatus.setText(R.string.status_signing_in);
				if (!mGoogleApiClient.isConnecting()) {
					mSignInClicked = true;
					resolveSignInError();
				}
			} else if (id == R.id.sign_out_button) {
				// We clear the default account on sign out so that Google Play
				// services will not return an onConnected callback without user
				// interaction.
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
				mGoogleApiClient.connect();
				onSignedOut();
			} 
			
//			else if (id == R.id.revoke_access_button) {
//				// After we revoke permissions for the user with a
//				// GoogleApiClient
//				// instance, we must discard it and create a new one.
//				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//				// Our sample has caches no user data from Google+, however we
//				// would normally register a callback on
//				// revokeAccessAndDisconnect
//				// to delete user data so that we comply with Google developer
//				// policies.
//				Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
//				mGoogleApiClient = buildGoogleApiClient();
//				mGoogleApiClient.connect();
//			} 
			
			else if (id == R.id.entry_button) {
				userProfileIntent = new Intent(MainActivity.this, NewsActivity.class);
				userProfileIntent.putExtra("localUser", localUser);
				
				// start user profile intent
				startActivity(userProfileIntent);
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
//		mRevokeButton.setEnabled(true);
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
		
		// open user database connection
		usersource.open();
		
		String userImageUrl = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=50";
		Image userImage = currentUser.getImage();
		User existingUser = null;

		if (userImage.hasUrl()) {
			userImageUrl = userImage.getUrl();
		}
		
		existingUser = usersource.selectRecordById(currentUser.getId().toString());
		String currentUserSid = currentUser.getId();
		
		if (existingUser == null) {
			createDataServices();
			// create new user with attributes
			localUser = new User();
			localUser.setUserId(currentUserSid);
			localUser.setUserSid(currentUserSid);
			localUser.setUserClientId(regid);
			localUser.setDisplayName(currentUser.getDisplayName());
			localUser.setEmailAddress(verifiedEmailAddress);
			localUser.setAvatarImageUrl(userImageUrl);
			localUser.setCurrentLevel("1");
			localUser.setCurrentPoints("0");
			String POINTS_USER_ID = "POINTS_USER_ID_" + currentUserSid;
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
		
		ArrayList<User> userList = usersource.selectRecords();
		
		for (User user : userList) {
			System.out.println("JUST ADDED NEW USER - CLIENT SID: " + user.getUserSid());
			System.out.println("JUST ADDED NEW USER - CURRENT POINTS: " + user.getCurrentPoints());
		}
		
		usersource.close();
	}

	private void createDataServices() {
		FilmLocationService locationService = new FilmLocationService(context);
		InputStream locationStream = getResources().openRawResource(R.raw.locations_copy);
		locationService.createContentValues(locationStream);
		locationService.createLocationsImpl();
		
		QuizItemService quizService = new QuizItemService(context);
		quizService.createQuizItemImpl();
		InputStream quizStream = getResources().openRawResource(R.raw.quiz_item_copy);
		quizService.createContentValues(quizStream);
		
		BagItemService bagService = new BagItemService(context);
		InputStream bagStream = getResources().openRawResource(R.raw.bag_item_copy);
		bagService.createContentValues(bagStream);
		bagService.createBagItemImpl();
		
		ConclusionCardService cardService = new ConclusionCardService(context);
		InputStream cardStream = getResources().openRawResource(R.raw.conclusion_card_copy);
		cardService.createContentValues(cardStream);
		cardService.createConclusionCardImpl();
		
		GameTitleService titleService = new GameTitleService(context);
		InputStream titleStream = getResources().openRawResource(R.raw.game_title_copy);
		titleService.createContentValues(titleStream);
		titleService.createGameTitleImpl();
		
		AchievementService achievementService = new AchievementService(context);
		InputStream achievementStream = getResources().openRawResource(R.raw.achievement_copy);
		achievementService.createContentValues(achievementStream);
		achievementService.createAchievementImpl();
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
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
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
		entryButton.setEnabled(false);
//		mRevokeButton.setEnabled(false);
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

	@SuppressWarnings("deprecation")
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
}