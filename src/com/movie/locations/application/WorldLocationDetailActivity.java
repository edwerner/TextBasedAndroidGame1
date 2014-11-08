package com.movie.locations.application;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;






//import com.movie.locations.application.NewsActivity.RestoreLevelDataTaskRunner;
import com.movie.locations.application.QuizActivity;
import com.movie.locations.application.MainActivity.NewUserTaskRunner;
import com.movie.locations.R;
import com.movie.locations.R.id;
import com.movie.locations.R.layout;
import com.movie.locations.R.menu;
import com.movie.locations.R.string;
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.ConclusionCardImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.PointsItemImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.Comment;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.FilmLocationCollection;
import com.movie.locations.domain.FilmLocationParcelableCollection;
import com.movie.locations.domain.MoviePostersHashMap;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.QuizItemArrayList;
import com.movie.locations.domain.User;
import com.movie.locations.domain.WorldLocationArrayList;
import com.movie.locations.domain.WorldLocationObject;
import com.movie.locations.service.DatabaseChangedReceiver;
import com.movie.locations.service.QuizItemService;
import com.movie.locations.service.WorldLocationService;
import com.movie.locations.util.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorldLocationDetailActivity extends FragmentActivity implements
		ActionBar.TabListener {

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

	private static String sectionLabel = "sectionLabel";
	private static String title = "title";
	private static String releaseYear = "releaseYear";
	private static String location = "location";
	private static String funFacts = "funFacts";
	private static String productionCompany = "productionCompany";
	private static String distributor = "distributor";
	private static String director = "director";
	private static String actors = "director";
	private static String writer = "writer";
	private static String latitude = "latitude";
	private static String longitude = "longitude";
	private static String worldId = "worldId";

	private static MovieLocationsImpl datasource;

	private static QuizItem quizItem;
	// private ArrayList<FilmLocation> filmList;
	private static LinkedHashMap<String, ArrayList<FilmLocation>> filmMap;
	private static LinkedHashMap<String, WorldLocationObject> worldLocationMap;
	private static ArrayList<FilmLocation> filmArrayList;
	private static ArrayList<WorldLocationObject> worldLocationArrayList;

	private static User currentUser;

	private static Intent intent;
	private static Context context;

	private static final String UNIQUE_MAP_IMAGE_URL = null;
	// private MovieLocationsImpl datasource;
	// private ArrayList<FilmLocation> filmList;
	// private static LinkedHashMap<String, ArrayList<FilmLocation>> filmMap;

	// TODO: REFACTOR ARRAY LIST TO ACCEPT COMMENT DOMAIN OBJECTS
	private static ArrayList<Comment> commentList;

	// public static ArrayList<FilmLocation> locationList;
	public static ArrayList<QuizItem> quizList;
	public static QuizItem quizItemMatch;

	// public QuizItemImpl quizsource;

	private static BagItemArrayList bagItemArrayList;

	private static FilmArrayList locationArrayList;
	// public static QuizItem currentQuizItem;
	// public static WorldLocationArrayAdapter locationQuizItemAdapter;
	// public WorldLocationArrayList worldLocationList;
	public FilmLocation currentLocation;

	private static QuizItemImpl quizitemsource;

	private static ArrayList<QuizItem> newQuizList;

	private QuizItemArrayList localQuizItemArrayList;

	private UserImpl userSource;

	private Object localUser;

	private IntentFilter filter;

	private static PointsItemImpl pointsItemImpl;

	private static Dialog dialog;

	private static BagItemImpl bagItemImpl;

	private static ConclusionCardImpl conclusionCardImpl;

	private static LocationQuizArrayAdapter locationQuizArrayAdapter;

	// private static ArrayList<FilmLocation> locationList;

	private static ListView locationsList;
	
	private static QuizItemService quizItemService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_film_location_detail);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		context = this;

		intent = getIntent();

		// // FILM LOCATION STRING BUNDLE EXTRAS:
		// // ===================================
		// // section_label
		// // title
		// // releaseYear
		// // locations
		// // funFacts
		// // productionCompany
		// // distributor
		// // director
		// // actors
		// // latitude
		// // longitude

		// worldId = intent.getExtras().getString("worldId");
		// sectionLabel = intent.getExtras().getString("section_label");

		//

		// WorldLocationArrayList list =
		// bundle.getParcelable("worldLocationList");
		// System.out.println("WORLD LOCATION ARRAY BUNDLE: " + list);

		// releaseYear = intent.getExtras().getString("releaseYear");
		// location = intent.getExtras().getString("locations");
		// funFacts = intent.getExtras().getString("funFacts");
		// productionCompany =
		// intent.getExtras().getString("productionCompany");
		// distributor = intent.getExtras().getString("distributor");
		// director = intent.getExtras().getString("director");
		// actors = intent.getExtras().getString("actors");
		// writer = intent.getExtras().getString("writer");
		// latitude = intent.getExtras().getString("latitude");
		// longitude = intent.getExtras().getString("longitude");

		// System.out.println("WORLD ID: " + worldId);

		Bundle bundle = intent.getExtras();
		locationArrayList = bundle.getParcelable("locationArrayList");
		bagItemArrayList = bundle.getParcelable("bagItemArrayList");
		// System.out.println("LOCATION ARRAY LIST PARCELABLE: "
		// + locationArrayList);

		
		
		// TODO: REMOVE THIS PARCEL FROM CALLING CLASS
//		final User tempUser = bundle.getParcelable("localUser");
//		final String TEMP_USER_ID = tempUser.getUserId();

		
		currentUser = bundle.getParcelable("localUser");
		
		// UPDATE USER SCORE/LEVEL
		userSource = new UserImpl(this);
//		currentUser = userSource.selectRecordById(TEMP_USER_ID);
		System.out.println("WORLD LOCATION DETAIL CURRENT USER TOTAL POINTS: " + currentUser.getCurrentPoints());
		
		currentLocation = bundle.getParcelable("currentLocation");
		title = currentLocation.getTitle();

		intent.putExtra("quizItemSid", currentUser.getUserSid());

		// quizsource = new QuizItemImpl(context);

		// quizitemsource
		// bagItemImpl
		// datasource

		bagItemImpl = new BagItemImpl(context);
		datasource = new MovieLocationsImpl(context);

		// initialize database connection
		quizitemsource = new QuizItemImpl(context);
		newQuizList = quizitemsource.selectRecords();
		System.out.println("QUIZ LIST SIZE: " + newQuizList.size());

		// newQuizList = quizitemsource.selectRecordsByWorldTitle(title);
		localQuizItemArrayList = new QuizItemArrayList();
		localQuizItemArrayList.setQuizList(newQuizList);

		intent.putExtra("localQuizItemArrayList", localQuizItemArrayList);

		// CONCLUSION CARD DATABASE IMPLEMENTATION
		conclusionCardImpl = new ConclusionCardImpl(this);
		quizItemService = new QuizItemService();
		pointsItemImpl = new PointsItemImpl(context);
		
		// quizItem = bundle.getParcelable("quizItem");

		// worldLocationList = bundle.getParcelable("worldLocationList");
		// System.out.println("QUIZ ITEM: " + quizItem);

		// System.out.println("RELEASE_YEAR: " +
		// intent.getExtras().getString("releaseYear"));
		// System.out.println("FUN_FACTS: " +
		// intent.getExtras().getString("funFacts"));
		// System.out.println("PRODUCTION_COMPANY: " +
		// intent.getExtras().getString("productionCompany"));
		// System.out.println("DISTRIBUTOR: " +
		// intent.getExtras().getString("distributor"));
		// System.out.println("DIRECTOR: " +
		// intent.getExtras().getString("director"));
		// System.out.println("WRITER: " +
		// intent.getExtras().getString("writer"));
		// System.out.println("ACTORS: " +
		// intent.getExtras().getString("actors"));

		// set world title
		setTitle(currentLocation.getTitle());

		// filmMap = FilmLocationCollection.createFilmLocationMap(datasource
		// .selectRecords());

		// iterate through worldLocationMap
		// Iterator<Entry<String, WorldLocationObject>> questionIterator =
		// worldLocationMap.entrySet().iterator();

		// filmArrayList = new ArrayList<FilmLocation>();
		// worldLocationArrayList = new ArrayList<WorldLocationObject>();

		// LinkedHashMap<String, QuizItem> quizMap = new LinkedHashMap<String,
		// QuizItem>();

		// String worldId = "";

		// while (questionIterator.hasNext()) {
		// Entry<String, WorldLocationObject> questionParis =
		// questionIterator.next();
		//
		// System.out.println("questionParis: " + questionParis);
		//
		// WorldLocationObject obj = questionParis.getValue();
		// // worldLocationArrayList.add(obj);
		// }

		// System.out.println("quizMap: " + quizMap);
		// datasource = new MovieLocationsImpl(this);
		// filmMap = FilmLocationCollection.createFilmLocationMap(datasource
		// .selectRecords());
		//
		// filmArrayList = new ArrayList<FilmLocation>();

		System.out.println("UNIQUE_MAP_IMAGE_URL" + UNIQUE_MAP_IMAGE_URL);

		// Bundle filmMap;
		// ArrayList<String> filmList = new ArrayList<String>(filmMap.keySet());

		// System.out.println("filmMap.size()" + filmMap.size());

		// TODO: retrieve this parseable from the calling intent
		// quizItem = quizMap.get("180");

		// String answer1 = "answer 1";
		// String answer2 = "answer 2";
		// String answer3 = "answer 3";
		// String answer4 = "answer 4";

		// int correctAnswer = 0;

		// String questionText = "Which is the correct answer?";
		String locationText;

		if (location != null) {
			locationText = location.replaceAll(" ", "");
		} else {
			locationText = "placeholderId";
		}

		
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
				 System.out.println("ON RESUME CURRENT USER POINTS: " + currentUser.getCurrentPoints());
			 }
			 
			 
			 
//			boolean answered = true;
//			for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
//				QuizItem tempItem = locationQuizArrayAdapter.getItem(i);
//				
//				if (tempItem.getAnswered().equals("false")) {
//					answered = false;
//				}
////						quizCounter++;
//			}
//			
//			if (answered == true) {
//				initializeReplayWorld();
//			} 
		 }
		 // View rootView = getView();
		 // View rootView = inflater.inflate(R.layout.fragment_film_detail,
		 // container, false);
		
		 // TODO: get reference to activity view
		 // reloadArrayAdapterData(rootView);
		
//		 if (locationQuizArrayAdapter != null) {
//			 // locationQuizArrayAdapter.notifyDataSetChanged();
//			 // redrawListAdapter();
//			 View view = getWindow().getDecorView().findViewById(R.layout.fragment_film_detail);
//			 loadArrayAdapterData(view);
//		 }
		
		 super.onResume();
	 }



	 private static void initializeReplayWorld(final QuizItem updatedQuizItem) {
			
	 	
//		quizitemsource = new QuizItemImpl(context);
//		newQuizList = quizitemsource.selectRecords();
		System.out.println("QUIZ LIST SIZE: " + newQuizList.size());
//		String currentQuizId = updatedQuizItem.getWorldId();
		// RESET QUIZ ITEM ANSWERED STATE
//		final QuizItem tempQuizItem = quizitemsource.selectRecordById(currentQuizId);
		
	
//		// newQuizList = quizitemsource.selectRecordsByWorldTitle(title);
//		localQuizItemArrayList = new QuizItemArrayList();
//		localQuizItemArrayList.setQuizList(newQuizList);
	
//		intent.putExtra("localQuizItemArrayList", localQuizItemArrayList);
		
		
			
	 	RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlayLayout);
	 	layout.setOnClickListener(new OnClickListener() {
		 	@Override
	 		public void onClick(View arg0) {
		 		System.out.println("CLICKED REPLAY MODAL: " + updatedQuizItem.getAnswered());
		 		if (updatedQuizItem.getAnswered().equals("true")) {
		 			System.out.println("RESETTING QUIZ ITEM: " + updatedQuizItem.getWorldId());
		 			System.out.println("CURRENT QUIZ ITEM ID: "+ updatedQuizItem.getQuestionId());
		 			final String updatedQuizItemId = updatedQuizItem.getQuestionId();
		 			
		 			RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
					runner.execute(updatedQuizItemId);
		 			
//		 			// RESET AND PERSIST QUIZ STATE
//		 			final String updatedQuizItemId = updatedQuizItem.getQuestionId();
//		 			quizitemsource.updateRecordAnswered(updatedQuizItemId, "FALSE");
		 			dialog.dismiss();
		 		}
//		 		final String RESTORE_LEVEL_DATA_URL = "http://movie-locations-app.appspot.com/secure/resetLevelData/";
//				final String FINAL_RESTORE_LEVEL_DATA_URL = RESTORE_LEVEL_DATA_URL + title;
//					RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
//					runner.execute(FINAL_RESTORE_LEVEL_DATA_URL);
//			 		dialog.dismiss();
	 			}
	 		});
	 		dialog.show();
	 	}

		public DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
			
			public void onReceive(Context context, Intent intent) {
				// update your list
			   
//				mSectionsPagerAdapter.notifyDataSetChanged();
				
				
				Bundle extras = intent.getExtras();
		//		newsIntent.putExtra("locationArrayList", localLocationArrayList);
				QuizItemArrayList quizArrayList = extras.getParcelable("quizArrayList");
				System.out.println("DATABASE_CHANGED: " + quizArrayList);
				newQuizList = quizArrayList.getQuizList();
				QuizItem firstQuizItem = newQuizList.get(0);
				
//				for (QuizItem tempQuizItem : newQuizList) {
//					if (getTitle().equals(tempQuizItem.getWorldTitle())) {
//						
//					}
//				}
				
				for (int i = 0; i < newQuizList.size(); i++) {
					QuizItem tempQuizItem = newQuizList.get(i);
					if (getTitle().equals(tempQuizItem.getWorldTitle())) {
						locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(0));
						locationQuizArrayAdapter.insert(tempQuizItem, 0);
						

						final String currentUserId = currentUser.getUserId();
						final PointsItem updatedUserDatabasePointsItem = pointsItemImpl.selectRecordById(currentUserId);
//						QuizItem tempQuizItem = quizitemsource.selectRecordById(result);
						

						// UPDATE USER POINTS
						String quizItemPointValue = tempQuizItem.getPointValue();
						int quizItemPointValueInt = Integer.parseInt(quizItemPointValue);
						
						if (updatedUserDatabasePointsItem != null) {
							String databasePoints = updatedUserDatabasePointsItem.getPoints();
							System.out.println("USER DATABASE POINTS: " + databasePoints);
							int databasePointsInt = Integer.parseInt(databasePoints);
							
							int updatedUserPointsInt = databasePointsInt - quizItemPointValueInt;
							String updatedUserPointsString = Integer.toString(updatedUserPointsInt);
							pointsItemImpl.updateRecordPointsValue(currentUserId, updatedUserPointsString);
							
							
							System.out.println("UPDATED USER DATABASE POINTS: " + updatedUserPointsString);
						}
						
					}
				}
				
				
				
//				for (QuizItem loc : newQuizList) {
//					System.out.println("DATABASE_CHANGED: " + loc.getWorldTitle());
////					String tempWorldTitle = loc.getWorldTitle();
////					if (tempWorldTitle.equals(getTitle())) {
////						initializeReplayWorld(loc);
////					}
//				}
				
//				for (int i = 0; i < newQuizList.size(); i++) {
////					if (newQuizList.get(i).equals(currentQuizItem.getQuestionId())) {
////						newQuizList.set(i, currentQuizItem);
//						 locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(i));
//						 locationQuizArrayAdapter.insert(firstQuizItem, i);
////					}
//				}
				
				locationQuizArrayAdapter.notifyDataSetChanged();
				// REDRAW VIEW WITH UPDATED COLLECTION
				
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
					
//					quizItemList = quizItemImpl.selectRecordsByWorldTitle(result);
//					final QuizItem tempQuizItem = quizitemsource.selectRecordById(result);
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
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

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
//		private UserImpl userImpl;
		private PointsItemImpl pointsItemImpl;

		public FilmLocationFragment() {
		}

		// @Override
		// public void onActivityResult(int requestCode, int resultCode, Intent
		// data) {
		// super.onActivityResult(requestCode, resultCode, data);
		// // switch(requestCode) {
		// // case (MY_CHILD_ACTIVITY) : {
		// if (resultCode == Activity.RESULT_OK) {
		// // TODO Extract the data returned from the child Activity.
		// System.out.println("RESULT_OK: " + resultCode);
		// }
		// // break;
		// // }
		// // }
		// }

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {

			System.out.println("******* RESULT ********");

			if (requestCode == 1) {

				if (resultCode == RESULT_OK) {

					// String result = data.getStringExtra("hello");
					currentQuizItem = data.getExtras()
							.getParcelable("quizItem");
//					System.out.println("QUIZ ITEM CORRECT ANSWER INDEX: " + currentQuizItem.getCorrectAnswerIndex());
					// update returned quizItem in database
					System.out.println("RESULT_OK: "
							+ currentQuizItem.getAnswered());
					
					// UPDATE USER POINTS
					String quizItemPointValue = currentQuizItem.getPointValue();
					int quizItemPointValueInt = Integer.parseInt(quizItemPointValue);
					
					System.out.println("POINT VALUE: " + quizItemPointValue);
//					String currentUserPoints = currentUser.getCurrentPoints();
//					String updatedUserPoints = currentUserPoints + quizItemPointValue;
//					currentUser.setCurrentPoints(updatedUserPoints);
//					userImpl.updateCurrentUserPoints(updatedUserPoints);
//					String currentUserDatabasePoints = userImpl.selectRecords();
					String currentUserId = currentUser.getUserId();
					

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
						
						
						System.out.println("UPDATED USER DATABASE POINTS: " + updatedUserPointsString);
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

					// // show modal if all levels have been completed
					// if (worldComplete == true) {
					// initializeReplayWorld();
					// }

					// if
					// (currentQuizItem.getWorldId().equals(item.getWorldId()))
					// {
					// locationQuizArrayAdapter.remove(item);
					// locationQuizArrayAdapter.insert(currentQuizItem,
					// position);
					// // quizIntent.putExtra("quizItem", currentQuizItem);
					// } else {
					// for (int i = 0; i < newQuizList.size(); i++) {
					// if
					// (newQuizList.get(i).equals(currentQuizItem.getQuestionId()))
					// {
					// // newQuizList.set(i, currentQuizItem);
					// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(i));
					// locationQuizArrayAdapter.insert(currentQuizItem, i);
					// }
					// }
					// // quizIntent.putExtra("quizItem", item);
					//
					// //
					// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(position));
					// // quizIntent.putExtra("quizItem", currentQuizItem);
					// }
					
					generateConclusionCard(quizItem);
					locationQuizArrayAdapter.notifyDataSetChanged();

				} else if (resultCode == RESULT_CANCELED) {

					// reset the current quiz item
					currentQuizItem = null;

					// Write your code if there's no result
					System.out.println("RESULT_CANCELED");
				}
			}

			// locationQuizArrayAdapter.notifyDataSetChanged();
//			generateConclusionCard(quizItem);
		}// onActivityResult

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
//					achievementIntent.putExtra("bagItemArrayList", bagItemArrayList);
			
			
//			JsonNode points = pointsJson.path("points");
//			String worldCount;
//			String currentLevel;
////					String notificationSettingsData = extras.getString("currentLevel");
//			
//			for (JsonNode point : points) {
//				if (point.get("worldCount") != null) {
////							System.out.println("WORLD LOCATIONS WORLD COUNT ****: " + removeDoubleQuotes(point.get("pointValue").toString()));
////							pointsItem.setPoints(removeDoubleQuotes(point.get("pointValue").toString()));
//					worldCount = removeDoubleQuotes(point.get("worldCount").toString());
//					achievementIntent.putExtra("worldCount", worldCount);
//					System.out.println("GCM INTENT SERVICE WORLD COUNT: " + worldCount);
//				}
//				if (point.get("currentLevel") != null) {
////							System.out.println("WORLD LOCATIONS WORLD COUNT ****: " + removeDoubleQuotes(point.get("pointValue").toString()));
////							pointsItem.setPoints(removeDoubleQuotes(point.get("pointValue").toString()));
//					currentLevel = removeDoubleQuotes(point.get("currentLevel").toString());
//					achievementIntent.putExtra("currentLevel", currentLevel);
//					System.out.println("GCM INTENT SERVICE CURRENT LEVEL: " + currentLevel);
//				}	
//			}
//					
			
//					String worldCount = points.get("worldCount").toString();
//					// TRAVERSE JSON NODE TO GET THIS VALUE
//					if (worldCount != null) {
//						achievementIntent.putExtra("worldCount", worldCount);
//						System.out.println("GCM INTENT SERVICE WORLD COUNT: " + worldCount);
//					}
			
			// TODO: GET THIS VALUE FROM SOMEWHERE
//					achievementIntent.putExtra("worldCount", "1");
//					pointsItem.setUserId(getCurrentUserId());
//			final String pointsUserId = "TEMP_POINTS_USER_ID_" + getCurrentUserId();
//			pointsItem.setUserId(getCurrentUserId());
//			pointsItem.setPointsUserId(pointsUserId);
//			achievementIntent.putExtra("currentUserId", getCurrentUserId());
//			achievementIntent.putExtra("pointsItem", pointsItem);
//					getApplication().startActivityForResult(achievementIntent, 1);
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

			
//			userImpl = new UserImpl(context);
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

			// quiz title and text
			// <TextView
			// android:id="@+id/quizTitle1"
			// android:layout_width="wrap_content"
			// android:layout_height="wrap_content"
			// android:textSize="36sp"
			// android:textStyle="bold" />
			//
			// <TextView
			// android:id="@+id/quizText1"
			// android:layout_width="wrap_content"
			// android:layout_height="wrap_content"
			// android:textSize="12sp" />

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

			// // movie poster click handler
			// filmList = new ArrayList<String>(filmMap.keySet());
			//
			// System.out.println("filmMap.size()" + filmMap.size());

			// filmArrayList = new ArrayList<FilmLocation>();

			// for (FilmLocation collection : filmMap.get(title)) {
			// // System.out.println("FILM MAP COLLECTION:" + collection);
			//
			// if (collection.getTitle().equals(title)) {
			// filmArrayList.add(collection);
			// }
			// }

			// @Override
			// public CharSequence getPageTitle(int position) {
			// Locale l = Locale.getDefault();
			//
			// switch (position) {
			// case 0:
			// return "movie";
			// case 1:
			// return "locations";
			// case 2:
			// return "quiz";
			// case 3:
			// return "comments";
			// }
			// return null;
			// }
			// }

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

			// if (!location.equals("null")) {
			// locationsList.setAdapter(locationQuizItemAdapter);
			// }

			// QuizItemImpl quizsource = new
			// QuizItemImpl(rootView.getContext());
			// MovieLocationsImpl datasource = new
			// MovieLocationsImpl(rootView.getContext());
			//
			// ArrayList<QuizItem> quizList = quizsource.selectRecords();
			//
			// quizMap = new LinkedHashMap<String, QuizItem>();
			//
			// if (quizList != null) {
			// for (QuizItem item : quizList) {
			// quizMap.put(item.getWorldId(), item);
			// // System.out.println("FILM ANSWER ONE (1) FROM DATABASE: " +
			// item.getAnswer1());
			// // System.out.println("FILM ANSWER ONE (2) FROM DATABASE: " +
			// item.getAnswer2());
			// // System.out.println("FILM ANSWER ONE (3) FROM DATABASE: " +
			// item.getAnswer3());
			// // System.out.println("FILM ANSWER ONE (4) FROM DATABASE: " +
			// item.getAnswer4());
			//
			// System.out.println("QUIZ ITEM WORLD ID: " + item.getWorldId());
			//
			// // System.out.println("FILM TITLES FROM DATABASE: " +
			// item.getFilmTitle());
			// // System.out.println("FILM QUESTION ID FROM DATABASE: " +
			// item.getQuestionId());
			// // System.out.println("FILM QUESTION TEXT FROM DATABASE: " +
			// item.getQuestionText());
			// // System.out.println("FILM ANSWERED FROM DATABASE: " +
			// item.getAnswered());
			// }
			// }

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

				// CREATE LEVEL RESTORE BUTTON
				// <Button
				// android:layout_width="wrap_content"
				// android:layout_height="wrap_content"
				// android:text="@string/button_text"
				// ... />

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

				//
				//
				// QuizItem quizItemMatch =
				// quizsource.selectRecordById(worldId);

				// ArrayList<QuizItem> quizList = quizsource.selectRecords();

				// System.out.println("LOCATION LIST COUNT: " +
				// locationList.size());
				// FilmLocation location = datasource.selectRecordById(worldId);

				// check for null location to set visibility
				// TextView locationsTagText = (TextView) rootView
				// .findViewById(R.id.locationsTag2);

				// TODO: move latitude/longitude to list adapter
				// System.out.println("LOCATION CHECK: " + location.length());

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
		
//		 private void initializeReplayWorld() {
//		
//		 final Dialog dialog = new Dialog(context,
//		 android.R.style.Theme_Translucent_NoTitleBar);
//		 dialog.setContentView(R.layout.replay_level_overlay);
//		 RelativeLayout layout = (RelativeLayout)
//		 dialog.findViewById(R.id.overlayLayout);
//		 layout.setOnClickListener(new OnClickListener() {
//		
//		 @Override
//		 public void onClick(View arg0) {
//		 dialog.dismiss();
//		
////		 final String RESET_QUIZ_ITEMS_ANSWERED_FALSE = "false";
////		 final String RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX = "null";
////		
////		 int counter = 0;
////		
////		 // TODO: SET UP DATABASE CALL TO RESET
////		 // LEVEL DATA AND ENABLE REPLAY VALUE
////		
////		
////		 for (int i = 0 ; i < locationQuizArrayAdapter.getCount(); i++){
////		 QuizItem item = locationQuizArrayAdapter.getItem(i);
////		 QuizItem updatedQuizItem = item;
////		 updatedQuizItem.setAnswered(RESET_QUIZ_ITEMS_ANSWERED_FALSE);
////		 updatedQuizItem.setCorrectAnswerIndex(RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX);
////		
////		 locationQuizArrayAdapter.remove(item);
////		
////		 locationQuizArrayAdapter.insert(updatedQuizItem, i);
////		
////		
////		
////		 // update database record
////		 quizitemsource.updateRecordAnswered(updatedQuizItem.getQuestionId(),
////		 RESET_QUIZ_ITEMS_ANSWERED_FALSE);
////		 quizitemsource.updateRecordCorrectAnswerIndex(updatedQuizItem.getQuestionId(),
////		 RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX);
//		
//		 }
//		
//		
//		 // Iterator<QuizItem> iterator = newQuizList.iterator();
//		 //
//		 // while (iterator.hasNext()) {
//		 // QuizItem updatedQuizItem = iterator.next();
//		 //
//		 // // update database record
//		 //
////		 quizitemsource.updateRecordAnswered(updatedQuizItem.getQuestionId(),
////		 RESET_QUIZ_ITEMS_ANSWERED_FALSE);
////		 //
////		 quizitemsource.updateRecordCorrectAnswerIndex(updatedQuizItem.getQuestionId(),
////		 RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX);
//		 //
//		 //
//		 // // ITERATE THROUGH ARRAYADAPTER AND RESET ITEMS
//		 // QuizItem oldQuizItem = locationQuizArrayAdapter.getItem(counter);
//		 //
//		 // // REMOVE OLD QUIZITEM
//		 // locationQuizArrayAdapter.remove(oldQuizItem);
//		 //
//		 // // REPLACE WITH NEW QUIZITEM
//		 // locationQuizArrayAdapter.insert(updatedQuizItem, counter);
//		 // counter++;
//		 // }
//		 // locationQuizArrayAdapter.notifyDataSetChanged();
//		 // iterator.remove();
////		 }
//		 });
//		 dialog.show();
//		 }
		
		// private void initializeReplayWorld() {
		//
		// final Dialog dialog = new Dialog(context,
		// android.R.style.Theme_Translucent_NoTitleBar);
		// dialog.setContentView(R.layout.replay_level_overlay);
		// RelativeLayout layout = (RelativeLayout)
		// dialog.findViewById(R.id.overlayLayout);
		// layout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// dialog.dismiss();
		//
		// final String RESET_QUIZ_ITEMS_ANSWERED_FALSE = "false";
		// final String RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX = "null";
		//
		// int counter = 0;
		//
		// // TODO: SET UP DATABASE CALL TO RESET
		// // LEVEL DATA AND ENABLE REPLAY VALUE
		//
		//
		// for (int i = 0 ; i < locationQuizArrayAdapter.getCount(); i++){
		// QuizItem item = locationQuizArrayAdapter.getItem(i);
		// QuizItem updatedQuizItem = item;
		// updatedQuizItem.setAnswered(RESET_QUIZ_ITEMS_ANSWERED_FALSE);
		// updatedQuizItem.setCorrectAnswerIndex(RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX);
		//
		// locationQuizArrayAdapter.remove(item);
		//
		// locationQuizArrayAdapter.insert(updatedQuizItem, i);
		//
		//
		//
		// // update database record
		// quizitemsource.updateRecordAnswered(updatedQuizItem.getQuestionId(),
		// RESET_QUIZ_ITEMS_ANSWERED_FALSE);
		// quizitemsource.updateRecordCorrectAnswerIndex(updatedQuizItem.getQuestionId(),
		// RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX);
		//
		// }
		//
		//
		// // Iterator<QuizItem> iterator = newQuizList.iterator();
		// //
		// // while (iterator.hasNext()) {
		// // QuizItem updatedQuizItem = iterator.next();
		// //
		// // // update database record
		// //
		// quizitemsource.updateRecordAnswered(updatedQuizItem.getQuestionId(),
		// RESET_QUIZ_ITEMS_ANSWERED_FALSE);
		// //
		// quizitemsource.updateRecordCorrectAnswerIndex(updatedQuizItem.getQuestionId(),
		// RESET_QUIZ_ITEMS_CORRECT_ANSWER_INDEX);
		// //
		// //
		// // // ITERATE THROUGH ARRAYADAPTER AND RESET ITEMS
		// // QuizItem oldQuizItem = locationQuizArrayAdapter.getItem(counter);
		// //
		// // // REMOVE OLD QUIZITEM
		// // locationQuizArrayAdapter.remove(oldQuizItem);
		// //
		// // // REPLACE WITH NEW QUIZITEM
		// // locationQuizArrayAdapter.insert(updatedQuizItem, counter);
		// // counter++;
		// // }
		// // locationQuizArrayAdapter.notifyDataSetChanged();
		// // iterator.remove();
		// }
		// });
		// dialog.show();
		// }

		private void prepareArrayAdapterData(View rootView) {

			try {

				// ArrayList<FilmLocation> locationList;
				// MovieLocationsImpl datasource = new
				// MovieLocationsImpl(context);
				// ArrayList<FilmLocation> newLocationList =
				// datasource.selectRecords();

				// QuizItemImpl quizsource = new QuizItemImpl(context);
				// newQuizList = quizsource
				// .selectRecords();

				// WorldLocationService worldLocationService = new
				// WorldLocationService();
				//
				// // ArrayList<WorldLocationObject> reloadWorldLocationList;
				//
				//
				//
				// ArrayList<WorldLocationObject> localWorldLocationList;
				// // WorldLocationArrayList tempWorldLocationArrayList = null;
				// // WorldLocationArrayAdapter locationQuizItemAdapter;
				// // localWorldLocationList =
				// worldLocationService.buildWorldLocationObjects(context,
				// newQuizList, newLocationList);
				//
				// WorldLocationArrayList tempWorldLocationArrayList = new
				// WorldLocationArrayList();
				// tempWorldLocationArrayList.setWorldLocationList(localWorldLocationList);

				// ArrayList<WorldLocationArrayList>
				// worldLocationObjectArrayList = new
				// ArrayList<WorldLocationArrayList>();
				// public WorldLocationArrayList worldLocationList;

				// ArrayList<WorldLocationArrayList> values;
				// WorldLocationArrayList worldLocationList;

				// ArrayList<WorldLocationArrayList>
				// worldLocationObjectArrayList = new
				// ArrayList<WorldLocationArrayList>();
				// // public WorldLocationArrayList worldLocationList;
				// // worldLocationObjectArrayList.add(worldLocationList);
				//
				//
				// worldLocationObjectArrayList.add(tempWorldLocationArrayList);
				//
				// public static WorldLocationArrayAdapter
				// locationQuizItemAdapter;
				// locationQuizItemAdapter = new
				// WorldLocationArrayAdapter(context, intent,
				// localWorldLocationList);

				// pass listItemTitles titles array to adapter
				//

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

				// ArrayList<String> locationTitleList = new
				// ArrayList<String>();
				// locationTitleList.add(object);

				// Collections.sort(locationTitleList,
				// TITLE_STRINGS_ALPHABETICAL_ORDER);

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

				// Fragment worldLocationFragment = (Fragment)
				// getActivity().getSupportFragmentManager().findFragmentById(R.layout.fragment_film_detail);

				// Fragment worldLocationFragment = (Fragment)
				// getActivity().getSupportFragmentManager().findFragmentById(R.layout.fragment_film_detail);

				//
				// ListView locationsList = (ListView)
				// rootView.findViewById(R.id.locationsView1);

				// System.out.println("LOGGING worldLocationFragment: " +
				// worldLocationFragment);
				//
				// ListView locationsList = (ListView)
				// worldLocationFragment.getView().findViewById(R.id.locationsView1);
				// locationQuizItemAdapter.clear();

				System.out.println("LOGGING locationQuizArrayAdapter: "
						+ locationQuizArrayAdapter);
				System.out.println("LOGGING locationsList: " + locationsList);

				// locationQuizItemAdapter.clear();
				// locationQuizItemAdapter.remove(object);

				// locationsList.setAdapter(null);

				// final ListView listView = (ListView)
				// rootView.findViewById(R.id.locationsView1);

//				initializeReplayWorld();
//				
//				for (Film) {
//					
//				}
//				int quizCounter = 0;
				
				locationsList.setAdapter(locationQuizArrayAdapter);
				
//				ArrayList<QuizItem> localQuizList = locationQuizArrayAdapter
				

//				boolean answered = true;
//				for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
//					QuizItem tempItem = locationQuizArrayAdapter.getItem(i);
//					
//					if (tempItem.getAnswered().equals("false")) {
//						answered = false;
//					}
////								quizCounter++;
//				}
//				
//				if (answered == true) {
//					initializeReplayWorld();
//				}
				 
				// QuizItem quizItem = values.get(position);

				// QuizItem quizItem = new QuizItem();
				// quizIntent.putExtra("quizItem", quizItem);

//				boolean answered = true;
//				for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
//					QuizItem tempItem = locationQuizArrayAdapter.getItem(i);
//					
//					if (tempItem.getAnswered().equals("false")) {
//						answered = false;
//					}
////							quizCounter++;
//				}
//				
//				if (answered == true) {
//					initializeReplayWorld();
//				}
				
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
								// view.animate().setDuration(2000).alpha(0).withEndAction(new
								// Runnable() {
								// view.animate().setDuration(2000).alpha(0).withEndAction(new
								// Runnable() {
								// @Override
								// public void run() {
								// newQuizList.remove(item);

								// newQuizList.set(index, object);

								// view.setAlpha(1);
								// System.out.println("CLICKED LIST VIEW");

								// open database session
								// quizitemsource.open();

								// TODO: set this value outside of this function
								//
								// create local quiz item records by id
								// final QuizItem currentQuizItem =
								// quizitemsource.selectRecordById(item.getQuestionId());

								// newQuizList.set(position, currentQuizItem);

								// System.out.println("LOCAL QUIZ ITEM BEFORE: "
								// + localQuizItem.getAnswered());
								//
								// // update quiz item
								// quizItem.setAnswered("true");

								// update database record
								// quizitemsource.updateRecord(quizItem.getQuestionId(),
								// "true");

								// System.out.println("LOCAL QUIZ ITEM AFTER: "
								// + quizItem.getAnswered());

								// close database session
								// quizitemsource.close();

								// TODO: implement fade out/in animation
								// update the item
								// newQuizList.set(position, item);

								// if (currentQuizItem == null) {
								// currentQuizItem = item;
								// }

								// if (currentQuizItem != null) {
								//
								// } else {
								// currentQuizItem = item;
								// System.out.println("CURRENT_QUIZ_ITEM_NULL: "
								// + currentQuizItem.getAnswered());
								// }

								final Intent quizIntent = new Intent(context, QuizActivity.class);
								final String CURRENT_USER_ID = currentUser.getUserId();
								final String QUIZ_ITEM_SID = currentUser.getUserSid();
								
								System.out.println("QUIZ ITEM PARCEL CURRENT POINTS: " + currentUser.getCurrentPoints());
								quizIntent.putExtra("currentUser", currentUser);
								quizIntent.putExtra("quizItemSid", QUIZ_ITEM_SID);
								quizIntent.putExtra("bagItemArrayList", bagItemArrayList);
								quizIntent.putExtra("quizItem", item);
								startActivityForResult(quizIntent, 1);

								// ********************************************************//
								// TODO: create conclusion messaging on question
								// complete
								// and launch new activity with title, copy and
								// image url
								// ********************************************************//

								//
								// if (currentQuizItem != null) {
								//
								// if
								// (currentQuizItem.getWorldId().equals(item.getWorldId()))
								// {
								// locationQuizArrayAdapter.remove(item);
								// locationQuizArrayAdapter.insert(currentQuizItem,
								// position);
								// quizIntent.putExtra("quizItem",
								// currentQuizItem);
								// } else {
								// for (int i = 0; i < newQuizList.size(); i++)
								// {
								// if
								// (newQuizList.get(i).equals(currentQuizItem.getQuestionId()))
								// {
								// // newQuizList.set(i, currentQuizItem);
								// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(i));
								// locationQuizArrayAdapter.insert(currentQuizItem,
								// i);
								// }
								// }
								// quizIntent.putExtra("quizItem", item);
								//
								// //
								// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(position));
								// // quizIntent.putExtra("quizItem",
								// currentQuizItem);
								// }
								//
								// } else {
								// quizIntent.putExtra("quizItem", item);
								// }

								// else if () {
								//
								// }
								// else {
								// quizIntent.putExtra("quizItem", item);
								// }
								// if (currentQuizItem != null) {
								// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(position));
								// locationQuizArrayAdapter.insert(currentQuizItem,
								// position);
								// currentQuizItem = null;
								// locationQuizArrayAdapter.notifyDataSetChanged();
								// }

								// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(position));
								// locationQuizArrayAdapter.insert(currentQuizItem,
								// position);

								// System.out.println("CURRENT_QUIZ_ITEM_NOT_NULL: "
								// + currentQuizItem);

								// System.out.println("PRE INTENT QUIZ ACTIVE ITEM 1: "
								// + currentQuizItem.getActiveItem1());
								// System.out.println("PRE INTENT QUIZ ACTIVE ITEM 2: "
								// + currentQuizItem.getActiveItem2());
								// System.out.println("PRE INTENT QUIZ ACTIVE ITEM 3: "
								// + currentQuizItem.getActiveItem3());
								// System.out.println("PRE INTENT QUIZ ACTIVE ITEM 4: "
								// + currentQuizItem.getActiveItem4());
								//
								// System.out.println("PRE INTENT QUIZ ANSWER 1: "
								// + currentQuizItem.getAnswer1());
								// System.out.println("PRE INTENT QUIZ ANSWER 2: "
								// + currentQuizItem.getAnswer2());
								// System.out.println("PRE INTENT QUIZ ANSWER 3: "
								// + currentQuizItem.getAnswer3());
								// System.out.println("PRE INTENT QUIZ ANSWER 4: "
								// + currentQuizItem.getAnswer4());
								// context.startActivity(quizIntent);

								// locationQuizArrayAdapter.notifyDataSetChanged();

								// startActivityForResult(quizIntent, 1);

								// setCurrentQuizItem(quizItem);

								// TODO: cut persistence from quiz activity and
								// only
								// access the database from the main calling
								// thread
								// within **this** activity
								// }
								// });
							}

						});

				// TODO: design a game where all game data exists in bundles
				// without persistence

				// localWorldLocationList =
				// worldLocationService.buildWorldLocationObjects(context,
				// newQuizList, newLocationList);
				// tempWorldLocationArrayList.setWorldLocationList(localWorldLocationList);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

		private void setCurrentQuizItem(QuizItem quizItem) {
			// TODO Auto-generated method stub

		}
	}

}
