package com.movie.locations.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;

import com.movie.locations.application.QuizActivity;
import com.movie.locations.AchievementActivity;
import com.movie.locations.R;
import com.movie.locations.dao.AchievementImpl;
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.ConclusionCardImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.PointsItemImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.Comment;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.QuizItemArrayList;
import com.movie.locations.domain.User;
import com.movie.locations.domain.WorldLocationObject;
import com.movie.locations.service.DatabaseChangedReceiver;
import com.movie.locations.service.QuizItemService;
import com.movie.locations.util.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorldLocationDetailActivity extends ActionBarActivity implements TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private static String title = "title";
	private static String location = "location";
	private static QuizItem quizItem;
	private User currentUser;
	private static Intent intent;
	private static Context context;
	private static final String UNIQUE_MAP_IMAGE_URL = null;
	public static ArrayList<QuizItem> quizList;
	public static QuizItem quizItemMatch;
	private static BagItemArrayList bagItemArrayList;
	private static FilmArrayList locationArrayList;
	public FilmLocation currentLocation;
	private static QuizItemImpl quizitemsource;
	private static ArrayList<QuizItem> newQuizList;
	private QuizItemArrayList localQuizItemArrayList;
	private static UserImpl userSource;
	private IntentFilter filter;
	private static PointsItemImpl pointsItemImpl;
	private static Dialog dialog;
	private static LocationQuizArrayAdapter locationQuizArrayAdapter;
	private static ListView locationsList;
	private static QuizItemService quizItemService;
	private AchievementImpl achievementImpl;
	private Achievement levelAchievement;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_film_location_detail);

		// Set up the action bar.
//		final ActionBar actionBar = getActionBar();
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getSupportActionBar().setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			getSupportActionBar().addTab(getSupportActionBar().newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		context = this;

		intent = getIntent();

		Bundle bundle = intent.getExtras();
		locationArrayList = bundle.getParcelable("locationArrayList");
		bagItemArrayList = bundle.getParcelable("bagItemArrayList");
		currentUser = bundle.getParcelable("localUser");
		
		// UPDATE USER SCORE/LEVEL
		userSource = new UserImpl(this);
		System.out.println("WORLD LOCATION DETAIL CURRENT USER TOTAL POINTS: " + currentUser.getCurrentPoints());
		currentLocation = bundle.getParcelable("currentLocation");
		title = currentLocation.getTitle();
		intent.putExtra("quizItemSid", currentUser.getUserSid());
//		bagItemImpl = new BagItemImpl(context);
//		datasource = new MovieLocationsImpl(context);
//		userImpl = new UserImpl(context);

		// initialize database connection
		quizitemsource = new QuizItemImpl(context);
		newQuizList = quizitemsource.selectRecords();
		System.out.println("QUIZ LIST SIZE: " + newQuizList.size());

		// newQuizList = quizitemsource.selectRecordsByWorldTitle(title);
		localQuizItemArrayList = new QuizItemArrayList();
		localQuizItemArrayList.setQuizList(newQuizList);
		intent.putExtra("localQuizItemArrayList", localQuizItemArrayList);

		// CONCLUSION CARD DATABASE IMPLEMENTATION
//		conclusionCardImpl = new ConclusionCardImpl(this);
		quizItemService = new QuizItemService();
		pointsItemImpl = new PointsItemImpl(context);
		achievementImpl = new AchievementImpl(context);
		

		String currentUserLevelString = currentUser.getCurrentLevel();
		int currentLevelInt = Integer.parseInt(currentUserLevelString);
		int nextLevel = currentLevelInt + 1;
		final String NEXT_ACHIEVEMENT_LEVEL = Integer.toString(nextLevel);
		levelAchievement = achievementImpl.selectRecordByLevel(NEXT_ACHIEVEMENT_LEVEL);
		System.out.println("LEVEL ACHIEVEMENT: " + levelAchievement.getLevel());
		System.out.println("LEVEL ACHIEVEMENT IMAGE URL: " + levelAchievement.getLevel());
		
		System.out.println("CURRENT USER MOBILE NOTIFICATIONS: " + currentUser.getMobileNotifications());

		// set world title
		setTitle(currentLocation.getTitle());
		System.out.println("UNIQUE_MAP_IMAGE_URL" + UNIQUE_MAP_IMAGE_URL);
		dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
	 	dialog.setContentView(R.layout.replay_level_overlay);
	 	filter = new IntentFilter();
		filter.addAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(mReceiver, filter);
	}


//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
    
	 @Override
	 public void onResume() {
		 System.out.println("******* RESUME ********");
		 registerReceiver(mReceiver, filter);
		 if (locationQuizArrayAdapter != null) {
			 QuizItem replayQuizItem = null;
			 boolean answered = true;
			 for (QuizItem item : newQuizList) {
				 if (item.getAnswered().equals("FALSE")) {
					 answered = false;
				 }
				 replayQuizItem = item;
			 }
			 if (answered == true) {
				 initializeReplayWorld(replayQuizItem);
			 }
			 if (currentUser != null) {
				 // RELOAD USER HERE TO UPDATE POINTS
				 final String CURRENT_USER_ID = currentUser.getUserId();
				 currentUser = userSource.selectRecordById(CURRENT_USER_ID);
				 System.out.println("ON RESUME USER MOBILE NOTIFICATIONS: " + currentUser.getMobileNotifications());
			 }
		 }
		 super.onResume();
	 }



	 private static void initializeReplayWorld(final QuizItem updatedQuizItem) {
		 
	 	RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlayLayout);
	 	layout.setOnClickListener(new OnClickListener() {
		 	@Override
	 		public void onClick(View arg0) {
		 		System.out.println("CLICKED REPLAY MODAL: " + updatedQuizItem.getAnswered());
		 		if (updatedQuizItem.getAnswered().equals("true")) {
		 			System.out.println("RESETTING QUIZ ITEM: " + updatedQuizItem.getWorldId());
		 			System.out.println("CURRENT QUIZ ITEM ID: "+ updatedQuizItem.getQuestionId());
		 			final String updatedQuizItemId = updatedQuizItem.getQuestionId();
		 			
		 			// RESET AND PERSIST QUIZ STATE
		 			RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
					runner.execute(updatedQuizItemId);
					
		 			dialog.dismiss();
		 		}
	 			}
	 		});
	 		dialog.show();
	 	}

		public DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
			
			public void onReceive(Context context, Intent intent) {

				// update your list
				Bundle extras = intent.getExtras();
				QuizItemArrayList quizArrayList = extras.getParcelable("quizArrayList");
				System.out.println("DATABASE_CHANGED: " + quizArrayList);
				newQuizList = quizArrayList.getQuizList();
				String currentUserId = currentUser.getUserId();
				String updatedUserPointsString = null;
				for (int i = 0; i < newQuizList.size(); i++) {
					QuizItem tempQuizItem = newQuizList.get(i);
					if (getTitle().equals(tempQuizItem.getWorldTitle())) {
						locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(0));
						locationQuizArrayAdapter.insert(tempQuizItem, 0);
						final PointsItem updatedUserDatabasePointsItem = pointsItemImpl.selectRecordById(currentUserId);
						
						// UPDATE USER POINTS
						String quizItemPointValue = tempQuizItem.getPointValue();
						int quizItemPointValueInt = Integer.parseInt(quizItemPointValue);
						
						if (updatedUserDatabasePointsItem != null) {
							String databasePoints = updatedUserDatabasePointsItem.getPoints();
							System.out.println("USER DATABASE POINTS: " + databasePoints);
							int databasePointsInt = Integer.parseInt(databasePoints);
							
							int updatedUserPointsInt = databasePointsInt - quizItemPointValueInt;
							updatedUserPointsString = Integer.toString(updatedUserPointsInt);
							pointsItemImpl.updateRecordPointsValue(currentUserId, updatedUserPointsString);
							
							System.out.println("UPDATED USER DATABASE POINTS: " + updatedUserPointsString);
						} else {
							updatedUserPointsString = currentUser.getPoints();
						}
					}
				}

				// REDRAW VIEW WITH UPDATED COLLECTION
				locationQuizArrayAdapter.notifyDataSetChanged();
				
			   System.out.println("UPDATED DATA FROM RECEIVER");
			   
			   unregisterReceiver(this);
		   }
		};



		
	 public static class RestoreLevelDataTaskRunner extends AsyncTask<String, String, String> {

			private String resp;
			private ProgressDialog dialog;
			private ArrayList<FilmLocation> filmLocationList;
			
			@Override
			protected String doInBackground(String... params) {
				publishProgress("Sleeping..."); // Calls onProgressUpdate()
				try {
					resp = params[0];
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
				String message = "Success!";
				if (result.equals("success")) {
					message = "Level data restored.";
				} else if (result.equals("error")) {
					message = "Something went wrong.";
				} else {
					message = "Level data reset.";
					
					System.out.println("ANSWERED QUIZ ID: " + result);
					// RESET QUIZ ITEMS LOCALLY
					// WE NEED TO GET THE WORLD TITLE
					
//					for (QuizItem localQuizItem : quizItemList) {
						// update database record
//					quizitemsource.updateRecordAnswered(result, "FALSE");
					quizItemService.resetAnsweredQuestion(result, context);
					
//					PointsItem updatedUserDatabasePointsItem = pointsItemImpl.selectRecordById(currentUserId);
//					System.out.pringln
//						quizItemImpl.updateRecordCorrectAnswerIndex(localQuizItem.getQuestionId(), "null");
//					}
				}
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				
				
//				mDrawerLayout.closeDrawers();
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
				dialog.setTitle("Restoring...");
				dialog.setMessage("Replay available in a moment.");
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.film_detail, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(arg0.getPosition());
		
	}


	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void onTabSelected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//		// When the given tab is selected, switch to the corresponding page in
//		// the ViewPager.
//		mViewPager.setCurrentItem(tab.getPosition());
//	}
//
//	@Override
//	public void onTabUnselected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//	}
//
//	@Override
//	public void onTabReselected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a MovieSectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new FilmLocationFragment();
			Bundle args = new Bundle();
			int fragmentIndex = position + 1;
			args.putInt(FilmLocationFragment.ARG_SECTION_NUMBER, fragmentIndex);
			args.putParcelable("localQuizItemArrayList", localQuizItemArrayList);
			args.putParcelable("localCurrentLocation", currentLocation);
			args.putParcelable("fragmentUser", currentUser);
			args.putParcelable("levelAchievement", levelAchievement);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();

			switch (position) {
			case 0:
				return "world";
			case 1:
				return "levels";
				// case 2:
				// return "quiz";
				// case 3:
				// return "comments";
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class FilmLocationFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		protected ImageLoader imageLoader = ImageLoader.getInstance();
		// public final String DEFAULT_MAP_IMAGE_URL =
		// "http://ojw.dev.openstreetmap.org/StaticMap/?lat=37.76663290389&lon=-122.44194030762&z=14&mode=Export&show=1";
		public final String DEFAULT_MAP_IMAGE_URL = "http://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=200x100&scale=2&sensor=true";
		public String UNIQUE_MAP_IMAGE_URL = "";
		// public final String PREFIX =
		// "http://ojw.dev.openstreetmap.org/StaticMap/?";
		public final String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
		public String CENTER = "";
		// public String SETTINGS = "&z=14&mode=Export&show=1";
		public String SETTINGS = "&zoom=13&size=200x100&scale=2&sensor=true";
		private String MOVIE_POSTER_URL = "";
		private final String SEARCH_DELIMITER = "+San+Francisco+California";
		// private static List<String> filmList;

		private ArrayList<FilmLocation> filmList;

		private QuizItem currentQuizItem;
		private QuizItemArrayList localQuizItemArrayList;
		private ArrayList<QuizItem> localQuizList;
		private FilmLocation localCurrentLocation;
		private UserImpl userImpl;
		private PointsItemImpl pointsItemImpl;
		private User fragmentUser;
//		private AchievementImpl achievementImpl;
		private Achievement levelAchievement;

		public FilmLocationFragment() {
		}
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {

			System.out.println("******* RESULT ********");

			if (requestCode == 1) {

				if (resultCode == RESULT_OK) {
					currentQuizItem = data.getExtras().getParcelable("quizItem");
					System.out.println("RESULT_OK: " + currentQuizItem.getAnswered());
					
					// UPDATE USER POINTS
					String quizItemPointValue = currentQuizItem.getPointValue();
					int quizItemPointValueInt = Integer.parseInt(quizItemPointValue);
					
					System.out.println("POINT VALUE: " + quizItemPointValue);
					String currentUserId = fragmentUser.getUserId();
					
					PointsItem newPointsItem = new PointsItem();
					newPointsItem.setUserId(currentUserId);
					newPointsItem.setPointsUserId(currentUserId);
					newPointsItem.setPoints(quizItemPointValue);
					
					PointsItem updatedUserDatabasePointsItem = pointsItemImpl.selectRecordById(currentUserId);
					
					if (updatedUserDatabasePointsItem != null) {
						String databasePoints = updatedUserDatabasePointsItem.getPoints();
						System.out.println("USER DATABASE POINTS: " + databasePoints);
						int databasePointsInt = Integer.parseInt(databasePoints);
						int updatedUserPointsInt = quizItemPointValueInt + databasePointsInt;
						String updatedUserPointsString = Integer.toString(updatedUserPointsInt);
						pointsItemImpl.updateRecordPointsValue(currentUserId, updatedUserPointsString);						
						
//						final User FINAL_CURRENT_USER = userImpl.selectRecordById(currentUserId);
//						final User FINAL_TEMP_USER = userImpl.selectRecordById(currentUserId);
//						final String FINAL_CURRENT_USER_LEVEL = FINAL_TEMP_USER.getCurrentLevel();
						final String FINAL_CURRENT_USER_LEVEL = fragmentUser.getCurrentLevel();
						final int FINAL_CURRENT_USER_LEVEL_INT = Integer.parseInt(FINAL_CURRENT_USER_LEVEL);
						final int FINAL_USER_POINTS_INT = Integer.parseInt(updatedUserPointsString);
						int currentLevel = StaticSortingUtilities.CHECK_LEVEL_RANGE(FINAL_CURRENT_USER_LEVEL, FINAL_USER_POINTS_INT);
						System.out.println("CURRENT LEVEL **: " + currentLevel);
						System.out.println("CURRENT LEVEL FINAL **: " + FINAL_CURRENT_USER_LEVEL_INT);
						System.out.println("RESULT MOBILE NOTIFICATIONS: " + fragmentUser.getMobileNotifications());
						
//						final String FINAL_MOBILE_NOTIFICATIONS = currentUser.getEmailNotifications();
////						FINAL_MOBILE_NOTIFICATIONS.equals("true")
//						System.out.println("MOBILE NOTIFICATIONS FINAL: " + FINAL_MOBILE_NOTIFICATIONS);
						if (currentLevel > FINAL_CURRENT_USER_LEVEL_INT && fragmentUser.getMobileNotifications().equals("true")) {
//							// SEND LEVEL UP NOTIFICATION
							
							if (levelAchievement != null) {
								sendLevelUpNotification();	
							}
							
						}
						
					} else {
						pointsItemImpl.createRecord(newPointsItem);
					} 
					
					
					// update database record
					quizitemsource.updateRecordAnswered(
							currentQuizItem.getQuestionId(),
							currentQuizItem.getAnswered());
					quizitemsource.updateRecordCorrectAnswerIndex(
							currentQuizItem.getQuestionId(),
							currentQuizItem.getCorrectAnswerIndex());
					// TODO: create another function to update correct answer
					// index
					// quizitemsource.updateRecord(currentQuizItem.getQuestionId(),
					// "true");

					for (int i = 0; i < newQuizList.size(); i++) {
						if (newQuizList.get(i).equals(
								currentQuizItem.getQuestionId())) {
							newQuizList.set(i, currentQuizItem);
							// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(i));
							// locationQuizArrayAdapter.insert(currentQuizItem,
							// i);
						}
					}

					// create switch to show modal if there's no more current
					// levels
					// and it's time to replay the current world
					boolean worldComplete = false;
					for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
						QuizItem item = locationQuizArrayAdapter.getItem(i);
						if (currentQuizItem.getWorldId().equals(
								item.getWorldId())) {
							locationQuizArrayAdapter.remove(item);
							locationQuizArrayAdapter.insert(currentQuizItem, i);
						}
						if (locationQuizArrayAdapter.getItem(i).getAnswered()
								.equals("true")) {
							worldComplete = true;
						} else {
							worldComplete = false;
							break;
						}
					}
					
					generateConclusionCard(quizItem);
					locationQuizArrayAdapter.notifyDataSetChanged();
				} else if (resultCode == RESULT_CANCELED) {

					// reset the current quiz item
					currentQuizItem = null;

					// Write your code if there's no result
					System.out.println("RESULT_CANCELED");
				}
			}
		}// onActivityResult


		private void sendLevelUpNotification() {
			int NOTIFICATION_ID = 1;
			NotificationManager mNotificationManager;
//			NotificationCompat.Builder builder;
			String copy = "Keep going!";
			String msg = "Welcome to level ";
			
			if (title != null) {

				String achievementId = levelAchievement.getAchievementId();
				String achievementTitle = levelAchievement.getTitle();
				String achievementCopy = levelAchievement.getDescription();
				String achievementLevel = levelAchievement.getLevel();
				String achievementImageUrl = levelAchievement.getImageUrl();
				
//				String FINAL_USER_LEVEL = fragmentUser.getCurrentLevel();
				msg += " " + achievementLevel + " !";
				
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
			            .setAutoCancel(true)
			            .setDefaults(Notification.DEFAULT_VIBRATE)
						.setContentTitle("Duchamp's Puzzle")
						.setContentText(msg)
						.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
//				System.out.println("TITLE: " + title);
//				System.out.println("COPY: " + copy);

				// create and start achievement activity
				mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				
				Intent achievementIntent = new Intent(context, AchievementActivity.class);
				AchievementActivity.setContext(context);
				
				
//				messageId = intent.getIntExtra("messageId", 1);
//				achievementTitle = intent.getStringExtra("achievementTitle");
//				achievementCopy = intent.getStringExtra("achievementCopy");
//				achievementImageUrl = intent.getStringExtra("achievementImageUrl");
//				levelUp = intent.getStringExtra("levelUp");
				
				
				
				achievementIntent.putExtra("messageId", achievementId);
				achievementIntent.putExtra("achievementTitle", achievementTitle);
				achievementIntent.putExtra("achievementCopy", achievementCopy);
				achievementIntent.putExtra("levelUp", achievementLevel);
				
//				String ACHIEVEMENT_IMAGE_URL = "http://mymoneybox.mfsa.com.mt/Files/Blue-SRT-4.png";
				achievementIntent.putExtra("achievementImageUrl", achievementImageUrl);
				System.out.println("achievementImageUrl: " + achievementImageUrl);

				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, achievementIntent, PendingIntent.FLAG_ONE_SHOT);

				mBuilder.setContentIntent(contentIntent);
				mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());	
			}
		}
		
		// @Override
		// public void onResume() {
		// System.out.println("******* RESUME ********");
		// // View rootView = getView();
		// // View rootView = inflater.inflate(R.layout.fragment_film_detail,
		// // container, false);
		//
		// // TODO: get reference to activity view
		// // reloadArrayAdapterData(rootView);
		//
		// if (locationQuizArrayAdapter != null) {
		// locationQuizArrayAdapter.notifyDataSetChanged();
		// }
		//
		// super.onResume();
		// }

		private void generateConclusionCard(QuizItem quizItem) {

			
			Intent achievementIntent = new Intent(context, ConclusionActivity.class);
			achievementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					Intent.FLAG_ACTIVITY_CLEAR_TOP | PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
//					ConclusionActivity.setContext(this.getBaseContext());
			
			// TODO: SET USER LEVEL
			// TODO: SET WORLD COUNT
			
			achievementIntent.putExtra("conclusionTitle", "Conclusion Title");
			achievementIntent.putExtra("conclusionCopy", "Conclusion copy");
			achievementIntent.putExtra("conclusionImageUrl", "conclusionImageUrl");
			
			// TODO: GET THIS CARD DATA FROM GCM MESSAGE
			ConclusionCard conclusionCard = new ConclusionCard();
			conclusionCard.setId("cardId");
			conclusionCard.setTitle("title");
			conclusionCard.setCopy("card copy");
			conclusionCard.setImageUrl("http://wow.zamimg.com/images/wow/icons/large/spell_holy_summonlightwell.jpg");
			conclusionCard.setLevel("level");
			
			achievementIntent.putExtra("conclusionCard", conclusionCard);
//			achievementIntent.putExtra("bagItemArrayList", bagItemArrayList);
			startActivity(achievementIntent);
				
			
		}

		public String removeParenthesis(String string) {
			String regex = string.replaceAll("\\(", "");
			regex = regex.replaceAll("\\)", "");
			return regex;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_film_detail,
					container, false);

			fragmentUser = getArguments().getParcelable("fragmentUser");
			levelAchievement = getArguments().getParcelable("levelAchievement");
			final String FINAL_USER_MOBILE_NOTIFICATIONS = fragmentUser.getMobileNotifications();
			System.out.println("FRAGMENT USER MOBILE NOTIFICATIONS: " + FINAL_USER_MOBILE_NOTIFICATIONS);
			pointsItemImpl = new PointsItemImpl(context);
			
			localQuizItemArrayList = getArguments().getParcelable(
					"localQuizItemArrayList");
			localQuizList = localQuizItemArrayList.getQuizList();

			localCurrentLocation = getArguments().getParcelable(
					"localCurrentLocation");
			// intent.putExtra("localQuizItemArrayList",
			// localQuizItemArrayList);

			// TextView dummyTextView = (TextView) rootView
			// .findViewById(R.id.section_label);
			// dummyTextView.setText(Integer.toString(getArguments().getInt(
			// ARG_SECTION_NUMBER)));

			// TextView titleTagText1 = (TextView)
			// rootView.findViewById(R.id.titleTag1);
			// TextView titleText1 = (TextView)
			// rootView.findViewById(R.id.title1);
			TextView locationsTagText1 = (TextView) rootView
					.findViewById(R.id.locationsTag1);
			TextView locationsText1 = (TextView) rootView
					.findViewById(R.id.locations1);
			TextView latitudeTagText1 = (TextView) rootView
					.findViewById(R.id.latitudeTag1);
			TextView latitudeText1 = (TextView) rootView
					.findViewById(R.id.latitude1);
			TextView longitudeTagText1 = (TextView) rootView
					.findViewById(R.id.longitudeTag1);
			TextView longitudeText1 = (TextView) rootView
					.findViewById(R.id.longitude1);

			TextView titleTagText2 = (TextView) rootView
					.findViewById(R.id.titleTag2);
			TextView titleText2 = (TextView) rootView.findViewById(R.id.title2);
			TextView releaseYearTagText2 = (TextView) rootView
					.findViewById(R.id.releaseYearTag2);
			TextView releaseYearText2 = (TextView) rootView
					.findViewById(R.id.releaseYear2);
			TextView locationsTagText2 = (TextView) rootView
					.findViewById(R.id.locationsTag2);
			TextView locationsText2 = (TextView) rootView
					.findViewById(R.id.locations2);
			TextView productionCompanyTagText2 = (TextView) rootView
					.findViewById(R.id.productionCompanyTag2);
			TextView productionCompanyText2 = (TextView) rootView
					.findViewById(R.id.productionCompany2);
			TextView distributorTagText2 = (TextView) rootView
					.findViewById(R.id.distributorTag2);
			TextView distributorText2 = (TextView) rootView
					.findViewById(R.id.distributor2);
			TextView directorTagText2 = (TextView) rootView
					.findViewById(R.id.directorTag2);
			TextView directorText2 = (TextView) rootView
					.findViewById(R.id.director2);
			TextView writerTagText2 = (TextView) rootView
					.findViewById(R.id.writerTag2);
			TextView writerText2 = (TextView) rootView
					.findViewById(R.id.writer2);
			TextView actorsTagText2 = (TextView) rootView
					.findViewById(R.id.actorsTag2);
			TextView actorsText2 = (TextView) rootView
					.findViewById(R.id.actors2);

			// TextView panelTitle = (TextView) rootView
			// .findViewById(R.id.panelTitle1);
			// TextView quizTitle = (TextView)
			// rootView.findViewById(R.id.quizTitle1);
			TextView quizText = (TextView) rootView
					.findViewById(R.id.quizText1);

			// LinearLayout commentListViewLayout = (LinearLayout)
			// rootView.findViewById(R.id.commentListLinearLayout);

			// TextView titleTagText3 = (TextView)
			// rootView.findViewById(R.id.titleTag3);
			// TextView titleText3 = (TextView)
			// rootView.findViewById(R.id.title3);
			// TextView locationsTagText3 = (TextView)
			// rootView.findViewById(R.id.locationsTag3);
			// TextView locationsText3 = (TextView)
			// rootView.findViewById(R.id.locations3);
			// TextView funFactsTagText3 = (TextView)
			// rootView.findViewById(R.id.funFactsTag3);
			// TextView funFactsText3 = (TextView)
			// rootView.findViewById(R.id.funFacts3);

			// ImageView filmImage = (ImageView) rootView
			// .findViewById(R.id.filmImage1);
			// Button bookmarkButton = (Button)
			// rootView.findViewById(R.id.bookmark_button);

			
			// TODO: CREATE WORLD MAP
			//
			// bookmarkButton.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			//
			// // print out film id
			// System.out.println("MOVIE title: " + title);
			// // Intent intent = new Intent(getActivity(),
			// // MoviePosterActivity.class);
			// // intent.putExtra("imageUrl", MOVIE_POSTER_URL);
			// // startActivity(intent);
			//
			// // TODO: write persistence for bookmarks
			// }
			// });

			// ImageView mapThumb = (ImageView) rootView
			// .findViewById(R.id.mapView1);
			// ImageView defaultMapThumb = (ImageView) rootView
			// .findViewById(R.id.defaultMapView1);

			// Create pojo that returns image url map

			// initializeMoviePostersHash();

			// MoviePostersHashMap map = new MoviePostersHashMap();
			// TODO: create support for empty urls
			// MOVIE_POSTER_URL =
			// MoviePostersHashMap.createHashMap().get(title);

			// moviePosterThumb.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// Intent intent = new Intent(getActivity(),
			// MoviePosterActivity.class);
			// intent.putExtra("imageUrl", MOVIE_POSTER_URL);
			// startActivity(intent);
			// }
			// });

			// mapThumb.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// Intent intent = new Intent(getActivity(),
			// MapDetailActivity.class);
			// intent.putExtra("title", title);
			// intent.putExtra("latitude", latitude);
			// intent.putExtra("longitude", longitude);
			// intent.putExtra("location", location);
			// intent.putExtra("funFacts", funFacts);
			// startActivity(intent);
			// }
			// });

			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			llp.setMargins(10, 0, 0, 0); // llp.setMargins(left, top, right,
											// bottom);
			// ListView locationsView = (ListView)
			// rootView.findViewById(R.id.locationsView1);
			ListView commentView = (ListView) rootView
					.findViewById(R.id.commentView);

			Button quizButton = (Button) rootView
					.findViewById(R.id.launch_quiz_button);
			//
			locationsList = (ListView) rootView
					.findViewById(R.id.locationsView1);

			prepareArrayAdapterData(rootView);
			
//			achievementImpl = new AchievementImpl(context);
			final String FINAL_USER_LEVEL = fragmentUser.getCurrentLevel();
//			levelAchievement = achievementImpl.selectRecordById(FINAL_USER_LEVEL);

			// intent.putExtra("createdAt",
			// currentWorldLocation.getCreatedAt());
			// intent.putExtra("createdMeta",
			// currentWorldLocation.getCreatedMeta());
			// intent.putExtra("updatedAt",
			// currentWorldLocation.getUpdatedAt());
			// intent.putExtra("updatedMeta",
			// currentWorldLocation.getUpdatedMeta());
			// intent.putExtra("meta", currentWorldLocation.getMeta());
			// intent.putExtra("title", currentWorldLocation.getTitle());
			// intent.putExtra("releaseYear",
			// currentWorldLocation.getReleaseYear());
			// intent.putExtra("funFacts", currentWorldLocation.getFunFacts());
			// intent.putExtra("productionCompany",
			// currentWorldLocation.getProductionCompany());
			// intent.putExtra("distributor",
			// currentWorldLocation.getDistributor());
			// intent.putExtra("director", currentWorldLocation.getDirector());
			// intent.putExtra("writer", currentWorldLocation.getWriter());
			// // intent.putExtra("worldId",
			// currentWorldLocation.getGeolocation());
			// intent.putExtra("locations",
			// currentWorldLocation.getLocations());
			// intent.putExtra("latitude", currentWorldLocation.getLatitude());
			// intent.putExtra("longitude",
			// currentWorldLocation.getLongitude());
			//
			//
			// intent.putExtra("sid", currentWorldLocation.getSid());
			// intent.putExtra("id", currentWorldLocation.getId());
			// intent.putExtra("level", currentWorldLocation.getLevel());
			// intent.putExtra("staticMapImageUrl",
			// currentWorldLocation.getStaticMapImageUrl());
			// intent.putExtra("questionId",
			// currentWorldLocation.getQuestionId());
			//
			//
			// intent.putExtra("position", currentWorldLocation.getPosition());
			// intent.putExtra("actor1", currentWorldLocation.getActor1());
			// intent.putExtra("actor2", currentWorldLocation.getActor2());
			// intent.putExtra("actor3", currentWorldLocation.getActor3());
			//
			//
			//
			//
			// intent.putExtra("answer1", currentWorldLocation.getAnswer1());
			// intent.putExtra("answer2", currentWorldLocation.getAnswer2());
			// intent.putExtra("answer3", currentWorldLocation.getAnswer3());
			// intent.putExtra("answer4", currentWorldLocation.getAnswer4());
			// intent.putExtra("answered", currentWorldLocation.getAnswered());
			// intent.putExtra("dateTime", currentWorldLocation.getDateTime());
			// intent.putExtra("questionText",
			// currentWorldLocation.getQuestionText());
			//
			// intent.putExtra("reaction1",
			// currentWorldLocation.getReaction1());
			// intent.putExtra("reaction2",
			// currentWorldLocation.getReaction2());
			// intent.putExtra("reaction3",
			// currentWorldLocation.getReaction3());
			// intent.putExtra("reaction4",
			// currentWorldLocation.getReaction4());
			//
			// intent.putExtra("worldId", currentWorldLocation.getWorldId());
			// intent.putExtra("worldTitle",
			// currentWorldLocation.getWorldTitle());
			// intent.putExtra("submittedAnswerIndex",
			// currentWorldLocation.getSubmittedAnswerIndex());
			// intent.putExtra("correctAnswerIndex",
			// currentWorldLocation.getCorrectAnswerIndex());
			//
			// intent.putExtra("activeItem",
			// currentWorldLocation.getActiveItem());
			// intent.putExtra("activeItem1",
			// currentWorldLocation.getActiveItem1());
			// intent.putExtra("activeItem2",
			// currentWorldLocation.getActiveItem2());
			// intent.putExtra("activeItem3",
			// currentWorldLocation.getActiveItem3());
			// intent.putExtra("activeItem4",
			// currentWorldLocation.getActiveItem4());

			// filmImage.setVisibility(ImageView.VISIBLE);
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {

			case 1: // MOVIE TAB
				// restoreLevelDataButton.setVisibility(Button.VISIBLE);

				// commentListViewLayout.setVisibility(LinearLayout.GONE);

				// defaultMapThumb.setVisibility(ImageView.GONE);
				// mapThumb.setVisibility(ImageView.GONE);
				// moviePosterThumb.setVisibility(ImageView.GONE);
				quizButton.setVisibility(Button.GONE);
				locationsList.setVisibility(ListView.GONE);

				// final String movieTitleText = "Movie";
				// panelTitle.setText(movieTitleText);

				titleTagText2.setText("Title");
				titleText2.setText(localCurrentLocation.getTitle());
				System.out.println("Title: " + localCurrentLocation.getTitle());

				locationsTagText2.setText("Location");
				locationsText2.setText(localCurrentLocation.getLocations());
				System.out.println("Locations: "
						+ localCurrentLocation.getLocations());

				releaseYearTagText2.setText("Release Year");
				releaseYearText2.setText(localCurrentLocation.getReleaseYear());
				System.out.println("Release Year: "
						+ localCurrentLocation.getReleaseYear());

				productionCompanyTagText2.setText("Production Company");
				productionCompanyText2.setText(localCurrentLocation
						.getProductionCompany());
				System.out.println("ProductionCompany: "
						+ localCurrentLocation.getProductionCompany());

				distributorTagText2.setText("Distributor");
				distributorText2.setText(localCurrentLocation.getDistributor());
				System.out.println("Distributor: "
						+ localCurrentLocation.getDistributor());

				directorTagText2.setText("Director");
				directorText2.setText(localCurrentLocation.getDirector());
				System.out.println("Director: "
						+ localCurrentLocation.getDirector());

				actorsTagText2.setText("Actor 1");
				actorsText2.setText(localCurrentLocation.getActor1());
				System.out.println("Actor 1: "
						+ localCurrentLocation.getActor1());

				actorsTagText2.setText("Actor 2");
				actorsText2.setText(localCurrentLocation.getActor2());
				System.out.println("Actor 2: "
						+ localCurrentLocation.getActor2());

				actorsTagText2.setText("Actor 3");
				actorsText2.setText(localCurrentLocation.getActor3());
				System.out.println("Actor 3: "
						+ localCurrentLocation.getActor3());

				writerTagText2.setText("Writer");
				writerText2.setText(localCurrentLocation.getWriter());
				System.out.println("Writer: "
						+ localCurrentLocation.getWriter());

				break;

			case 2: // LOCATIONS TAB

				// restoreLevelDataButton.setVisibility(Button.GONE);
				// reloadArrayAdapterData(rootView);

				// locationQuizItemAdapter = new WorldLocationArrayAdapter(this,
				// intent, worldLocationList.getWorldLocationList());
				// locationsList.setAdapter(locationQuizItemAdapter);

				// commentListViewLayout.setVisibility(LinearLayout.GONE);
				quizButton.setVisibility(Button.GONE);
				locationsList.setVisibility(ListView.VISIBLE);

				final String filmLocationsText = localCurrentLocation
						.getTitle();

				// panelTitle.setText(filmLocationsText);

				System.out.println("Fun facts: "
						+ localCurrentLocation.getFunFacts());

				if (localCurrentLocation.getLocations() != null) {
					String formattedLocation = removeParenthesis(location);
					formattedLocation = formattedLocation.replaceAll(" ", "+");
					formattedLocation += SEARCH_DELIMITER;
					UNIQUE_MAP_IMAGE_URL = PREFIX + formattedLocation
							+ SETTINGS;

					// Load image, decode it to Bitmap and display Bitmap in
					// ImageView
					// mapUnavailable.setVisibility(ImageView.GONE);
					// defaultMapThumb.setVisibility(ImageView.GONE);
					// moviePosterThumb.setVisibility(ImageView.GONE);
					// imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, mapThumb);
				} else {
					UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
					// mapThumb.setVisibility(ImageView.GONE);
					// moviePosterThumb.setVisibility(ImageView.GONE);

					// defaultMapImage
					// defaultMapThumb.setVisibility(ImageView.VISIBLE);
				}

				// check for null location to set visibility
				if (localCurrentLocation.getLocations().equals("null")) {
					locationsTagText1.setText("No locations available.");
				}

				// // check for null location to set visibility
				// if (!location.equals("null")) {
				// locationsTagText1.setText("Location");
				// locationsText1.setText(location);
				// latitudeTagText1.setLayoutParams(llp);
				// latitudeTagText1.setText("Latitude");
				// latitudeText1.setText(latitude);
				// longitudeTagText1.setText("Longitude");
				// longitudeText1.setText(longitude);
				// } else {
				// locationsTagText1.setText("No locations available.");
				// locationsTagText1.setVisibility(0);
				// locationsText1.setVisibility(0);
				// latitudeTagText1.setVisibility(0);
				// latitudeTagText1.setVisibility(0);
				// latitudeText1.setVisibility(0);
				// longitudeTagText1.setVisibility(0);
				// longitudeText1.setVisibility(0);
				// }

				// bookmarkButton.setVisibility(Button.GONE);

				break;

			case 3: // QUIZ TAB
				// locationsList.setVisibility(ListView.GONE);
				// quizButton.setVisibility(Button.VISIBLE);
				// // TODO: create quiz page
				// final String quizTitleText = title + " Movie Quiz";
				// // quizTitle.setText(quizTitleText);
				// panelTitle.setText(quizTitleText);
				// final String quizQuestionText = title + " Movie Question";
				// quizText.setText(quizQuestionText);
				//
				// quizButton.setOnClickListener(new View.OnClickListener() {
				// public void onClick(View v) {
				//
				// Intent quizIntent = new Intent(getActivity(),
				// QuizActivity.class);
				// quizIntent.putExtra("quizItem", quizItem);
				// quizIntent.putExtra("title", title);
				// startActivity(quizIntent);
				//
				// // TODO: write persistence for quizzes
				// }
				// });

				// bookmarkButton.setVisibility(Button.GONE);
				break;

			case 4: // COMMENTS TAB

				// // moviePosterThumb.setVisibility(ImageView.VISIBLE);
				// final String commentsTitle = title + " Movie Comments";
				// panelTitle.setText(commentsTitle);
				// // posterThumb.setAlpha(0.3f);
				//
				// // mapUnavailable.setVisibility(ImageView.GONE);
				// // defaultMapThumb.setVisibility(ImageView.GONE);
				// // mapThumb.setVisibility(ImageView.GONE);
				//
				// // Load image, decode it to Bitmap and display Bitmap in
				// // ImageView
				//
				// // final String MOVIE_POSTER_URL =
				// // "https://www.filmposters.com/images/posters/12819.jpg";
				// // imageLoader.displayImage(MOVIE_POSTER_URL,
				// moviePosterThumb);
				//
				// titleTagText2.setLayoutParams(llp);
				//
				// titleTagText2.setText("Title");
				// titleText2.setText(title);
				// System.out.println("Title: " + title);
				//
				//
				// // <!-- Put listview here -->
				// //
				// // <ListView
				// // android:id="@+id/listview"
				// // android:layout_width="wrap_content"
				// // android:layout_height="wrap_content" />
				//
				// // TODO: show/hide commentListLinearLayout
				// // commentListViewLayout.setVisibility(LinearLayout.VISIBLE);
				//
				//
				//
				// commentList = new ArrayList<Comment>();
				// Comment comment = new Comment();
				//
				// for (int i = 0; i < 10; i++) {
				// comment.setComment("static comment");
				// comment.setCommentId("commentId");
				// comment.setDateTime("dateTime");
				// comment.setFilmId("filmId");
				// comment.setUserId("userId");
				// commentList.add(comment);
				// }
				//
				// CommentArrayAdapter adapter = new CommentArrayAdapter(
				// getActivity(), intent, commentList);
				//
				// if (commentList.size() >= 0) {
				// commentView.setAdapter(adapter);
				// }
				// // bookmarkButton.setVisibility(Button.GONE);
				break;
			}
			return rootView;
		}
		
		private void prepareArrayAdapterData(View rootView) {

			try {

				// get location list from parcel
				ArrayList<FilmLocation> locationList = locationArrayList
						.getFilmList();

				ArrayList<FilmLocation> finalLocationList = new ArrayList<FilmLocation>();

				// locationList = datasource.selectRecords();

				for (FilmLocation loc : locationList) {
					if (loc.getTitle().equals(localCurrentLocation.getTitle())) {
						finalLocationList.add(loc);
					}
				}

				// sort the list
				Collections.sort(finalLocationList,
						StaticSortingUtilities.LOCATIONS_ALPHABETICAL_ORDER);

				String[] listItemTitles = new String[finalLocationList.size()];
				String[] listItemImageTiles = new String[finalLocationList
						.size()];

				// populate list view item data arrays
				int counter = 0;
				for (FilmLocation location : finalLocationList) {
					listItemTitles[counter] = location.getLocations();
					listItemImageTiles[counter] = location
							.getStaticMapImageUrl();

					System.out.println("FINAL LOCATION LIST COUNT: " + counter);
					counter++;
				}

				ArrayList<QuizItem> finalQuizList = new ArrayList<QuizItem>();

//				Collections.sort(finalQuizList,
//						StaticSortingUtilities.QUIZ_ITEMS_ALPHABETICAL_ORDER);
				
				boolean answered = true;
				QuizItem replayQuizItem = null;
				for (QuizItem quizItem : newQuizList) {
					System.out.println("LOCATION TITLE 1:" + localCurrentLocation.getTitle());
					System.out.println("LOCATION TITLE 2:" + quizItem.getWorldTitle());
					System.out.println("LOCAL CURRENT LOCATION CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
					if (quizItem.getWorldTitle().equals(localCurrentLocation.getTitle())) {
						finalQuizList.add(quizItem);
//						System.out.println("QUIZ ITEM GET ANSWERED:" + quizItem.getAnswered());
						if (quizItem.getAnswered().equals("FALSE")) {
							answered = false;
						}
						replayQuizItem = quizItem;
					}
				}
				if (answered == true) {
					System.out.println("QUIZ ITEM ANSWERED: " + replayQuizItem.getAnswered());
					initializeReplayWorld(replayQuizItem);
				}

				System.out.println("FINAL QUIZ LIST SIZE: " + finalQuizList.size());
				// create new location quiz array adapter
				locationQuizArrayAdapter = new LocationQuizArrayAdapter(
						getActivity(), context, intent, finalQuizList);

				// set list item titles
				locationQuizArrayAdapter.setListItemTitles(listItemTitles);

				// set list item image tiles
				locationQuizArrayAdapter
						.setListItemImageTiles(listItemImageTiles);

				System.out.println("LOGGING locationQuizArrayAdapter: "
						+ locationQuizArrayAdapter);
				System.out.println("LOGGING locationsList: " + locationsList);
				
				locationsList.setAdapter(locationQuizArrayAdapter);
				
				locationsList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									final View view, int position, long id) {
								final QuizItem item = (QuizItem) parent
										.getItemAtPosition(position);
								System.out
										.println("QUIZ ITEM FINAL QUIZ ITEM: "
												+ item.getAnswered());

								final Intent quizIntent = new Intent(context, QuizActivity.class);
								final String CURRENT_USER_ID = fragmentUser.getUserId();
								final String QUIZ_ITEM_SID = fragmentUser.getUserSid();
								
								System.out.println("QUIZ ITEM PARCEL CURRENT POINTS: " + fragmentUser.getCurrentPoints());
								quizIntent.putExtra("currentUser", fragmentUser);
								quizIntent.putExtra("quizItemSid", QUIZ_ITEM_SID);
								quizIntent.putExtra("bagItemArrayList", bagItemArrayList);
								quizIntent.putExtra("quizItem", item);
								startActivityForResult(quizIntent, 1);

								// ********************************************************//
								// TODO: REFACTOR conclusion messaging on question
								// complete
								// and launch new activity with title, copy and
								// image url
								// ********************************************************//

								// TODO: cut persistence from quiz activity and
								// only
								// access the database from the main calling
								// thread
								// within **this** activity
								// }
								// });
							}

						});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
