package com.movie.locations.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.movie.locations.R;
import com.movie.locations.R.id;
import com.movie.locations.R.layout;
import com.movie.locations.R.menu;
import com.movie.locations.R.string;
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.CheckIn;
import com.movie.locations.domain.ClassLoaderHelper;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.FilmLocationCollection;
import com.movie.locations.domain.LocationMapParcel;
import com.movie.locations.domain.MoviePostersHashMap;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.domain.WorldLocationArrayList;
import com.movie.locations.domain.WorldLocationObject;
import com.movie.locations.service.DatabaseChangedReceiver;
import com.movie.locations.service.FilmLocationService;
import com.movie.locations.service.WorldLocationService;
import com.movie.locations.util.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FilmLocationPagerActivity extends FragmentActivity {

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
	private static ViewPager mViewPager;

	private MovieLocationsImpl datasource;
	// private ArrayList<FilmLocation> filmList;
	private HashMap<String, ArrayList<FilmLocation>> filmMap;
	private static LinkedHashMap<String, FilmLocation> titleMap;
	private static String title;
	// private static List<FilmLocation> listMatch;
	private static ArrayList<FilmLocation> filmContent;
//	private static List<FilmLocation> filmList;
	private static ArrayList<FilmLocation> content;
	private ListView filmListView;

	private static String section_label;

	private static String releaseYear;

	private static String locations;

	private static String funFacts;

	private static String productionCompany;

	private static String distributor;

	private static String director;

	private static String writer;

	private static String actors;

	private static String latitude;

	private static String longitude;
//	private static ArrayList<FilmLocation> locationList;
	private static int pagerPosition;
	private static Intent intent;
//	private static FilmLocation currentLocation;
	private static WorldLocationObject currentWorldLocation;
	private static String MOVIE_POSTER_URL;

	private static int currentPosition = 0;
	
	private static User currentUser;
	
	private static Context context;

	private QuizItemImpl quizsource;

	private User tempUser;

	private ArrayList<String> worldTitles;

	public static int initializedIndex = 0;

	public static ListView commentView;

	public static ListAdapter commentAdapter;

	public static ArrayList<FilmLocation> localFilmArrayList;

	public static ArrayList<FilmLocation> localAdapterFilmList;

//	private static ArrayList<BagItem> bagItemArrayList;
	

	private static ArrayList<FilmLocation> locationList;

	private static ArrayList<WorldLocationObject> worldLocationList;
	
	private static ArrayList<QuizItem> quizList;
	private static LinkedHashMap<String, QuizItem> quizMap;
//	private static LinkedHashMap<String, FilmLocation> locationMap;
	private LocationMapParcel locationMap;

	private ArrayList<String> localWorldImageUrls;
//	private static FilmArrayList filmArrayList; 

	
	private static WorldLocationArrayList list;
//	private static FilmLocation[] filmLocationArray;
	
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
//	private ArrayList<FilmLocation> filmList;
	
	private FilmArrayList locationArrayList;
	private static BagItemArrayList bagItemArrayList;
	
	private static FilmLocation currentLocation;
	
	private static UserImpl userImpl;

	private static QuizItemImpl quizitemsource;

	private static ArrayList<QuizItem> newQuizList;

	private static Dialog dialog;

//	private static String[] locationTitles;
//	private static int currentPositionIndex;
	
//	@Override
//	protected void onSaveInstanceState(Bundle bundle) {
//	  super.onSaveInstanceState(bundle);
//	  bundle.putParcelable("locationMap", locationMap);
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_film_location_pager);
		
		context = this;

		
		 
//		// TODO: check for fragment injection vulnerability
		Bundle bundle = getIntent().getExtras();
		
		// query database for records
		BagItemImpl bagitemsource = new BagItemImpl(this); // bagItemArrayList
		ArrayList<BagItem> bagItemList = bagitemsource.selectRecords();

		// set parcelable array list
		bagItemArrayList = new BagItemArrayList(); 
		bagItemArrayList.setBagItemArrayList(bagItemList);
		
		datasource = new MovieLocationsImpl(this);
		
		locationArrayList = bundle.getParcelable("locationArrayList");
//		locationArrayList.setFilmList(locationList);
		
//		locationList = datasource.selectRecords();
		locationList = locationArrayList.getFilmList();
		System.out.println("STARTED INTENT LIST LENGTH: " + locationList.size());
		
//		locationArrayList = new FilmArrayList();
//		locationArrayList.setFilmList(locationList);

		ArrayList<FilmLocation> titleList = new ArrayList<FilmLocation>();
		
		filmMap = new LinkedHashMap<String, ArrayList<FilmLocation>>();
		
		
//		refreshLocationTitles(locationArrayList.getFilmList());
		
//		String[] worldTitles = null;
		
		
		worldTitles = new ArrayList<String>();
		localWorldImageUrls = new ArrayList<String>();
//		int worldTitleCount = 0;
		


		// sort the list
//		Collections.sort(locationList, StaticSortingUtilities.LOCATIONS_ALPHABETICAL_ORDER);
		System.out.println("LOCATION LIST FROM PARCEL: " + locationList.size());
		
		for (FilmLocation loc : locationList) {
			if (!worldTitles.contains(loc.getTitle())) {
				worldTitles.add(loc.getTitle());
				localWorldImageUrls.add(loc.getStaticMapImageUrl());
			}
		}
		
		int arraylength = worldTitles.size();
		System.out.println("WORLD TITLES SIZE: " + arraylength);
		ArrayList<FilmLocation> tempList = new ArrayList<FilmLocation>();
		
		for (int a = 0; a < arraylength; a++) {
			System.out.println("WORLD TITLES SIZE: " + worldTitles.get(a));
		}
		for (int i = 0; i < arraylength; i++) {
			tempList.clear();
			System.out.println("datasource: " + datasource);
			final String CURRENT_TITLE = worldTitles.get(i);
			System.out.println("CURRENT_TITLE: " + CURRENT_TITLE);
//			ArrayList<FilmLocation> locationByTitle = datasource.selectRecordsByTitle(CURRENT_TITLE);
			
			System.out.println("SELECT RECORDS BY TITLE COUNT: " + locationList.size());
			
//			String currentTitle = "TITLE";
			
			for (FilmLocation location : locationList) {
//				currentTitle = location.getTitle();
				tempList.add(location);
			}
			
			filmMap.put(CURRENT_TITLE, tempList);
			

		
			
			
		}
		
		
		locationMap = new LocationMapParcel();
		locationMap.setLocationHashMap(filmMap);
		
		
		System.out.println("FILM MAP LENGTH:" + filmMap.size());
//		savedInstanceState.putParcelable("locationMap", locationMap);
//		bundle.putString("title", title);
//		
		
		
		
//		
		
		
		
//		locationTitles = new String[arraylength];
		System.out.println("LOCATION TITLE ARRAY LENGTH ****: " + arraylength);
		
//		 // ITERATE THROUGH LOCATIONS AND STORE TITLES IN ARRAY
//		 int counter = 0;
////		 ArrayList<FilmLocation> localLocationList = new ArrayList<FilmLocation>();
//		 for (FilmLocation loc : locationList) {
//			 if (!Arrays.asList(locationTitles).contains(loc.getTitle())) {
//				 locationTitles[counter] = loc.getTitle();
//				 System.out.println("LOCATION TITLE ****: " + loc.getTitle());
//				 counter++;
//			 }
//		 }
		 
//		 filmMap = FilmLocationCollection.createFilmLocationMap(locationList);
		
//		selectRecordsByTitle
		 
		 
		 System.out.println("FILM MAP SIZE ****: " + filmMap.size());
//		 System.out.println("FILM MAP 1 ****: " + filmMap.get(locationTitles[1]));
		
		// TODO: REFACTOR THIS AND PASS ONLY USER ID
		// SO WE CAN UPDATE UI WHEN NEW LEVELS ARRIVE
		// BASED ON WORLD COMPLETE COUNT
		currentUser = bundle.getParcelable("localUser");
		userImpl = new UserImpl(this);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		quizitemsource = new QuizItemImpl(context);
		newQuizList = quizitemsource.selectRecords();
		
		System.out.println("QUIZ LIST SIZE:" + newQuizList.size());
		
		

        IntentFilter filter = new IntentFilter();
        filter.addAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mReceiver, filter);

		


//        Intent intent = new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
//        sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle results = getResultExtras(true);
//                System.out.println("********************* sendOrderedBroadcast **********************************************: " + results);
//            }
//        }, null, Activity.RESULT_OK, null, null);
        
        


//		initializeFirstMovie();
	}

	

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mReceiver);
    }
	
	public DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
		
		public void onReceive(Context context, Intent intent) {
	       // update your list
		   
		   
		   Bundle extras = intent.getExtras();
//			newsIntent.putExtra("locationArrayList", localLocationArrayList);
//			FilmArrayList locationArrayList = extras.getParcelable("locationArrayList");
			System.out.println("DATABASE_CHANGED: " + locationArrayList);
			ArrayList<FilmLocation> tempLocationList = locationArrayList.getFilmList();
			
//			for (FilmLocation loc : tempLocationList) {
//				System.out.println("DATABASE_CHANGED: " + loc.getLocations());
//			}
			
		   System.out.println("UPDATED DATA FROM RECEIVER");
		   
	   }

	};
	
//	 private String[] refreshLocationTitles(ArrayList<FilmLocation> filmList) {
//		
//		 int arraylength = locationArrayList.getFilmList().size();
//		 locationTitles = new String[arraylength];
//		 
//		 // CREATE LOCATION MAPPING
////		 private static LinkedHashMap<String, ArrayList<FilmLocation>> filmMap;
//		 
////		 locationTitles[0] = "NULL_TITLE";
//		 // ITERATE THROUGH LOCATIONS AND STORE TITLES IN ARRAY
//		 int counter = 0;
//		 ArrayList<FilmLocation> localLocationList = new ArrayList<FilmLocation>();
//		 for (FilmLocation loc : locationList) {
//			 if (!Arrays.asList(locationTitles).contains(loc.getTitle())) {
//				 locationTitles[counter] = loc.getTitle();
//				 System.out.println("LOCATION TITLE ****: " + loc.getTitle());
//				 counter++;
//			 }
//		 }
//		 
//		 // CREATE LOCATION MAPPING
//		 filmMap = FilmLocationCollection.createFilmLocationMap(locationList);
//		 
//		 return locationTitles;
//	}
	
//	public void reloadLevels() {
//		
//		filmMap.clear();
//		worldTitles.clear();
//		localWorldImageUrls.clear();
//		
//		for (FilmLocation loc : locationList) {
//			if (!worldTitles.contains(loc.getTitle())) {
//				worldTitles.add(loc.getTitle());
//				localWorldImageUrls.add(loc.getStaticMapImageUrl());
//			}
//		}
//		
//		int arraylength = worldTitles.size();
//		ArrayList<FilmLocation> tempList = new ArrayList<FilmLocation>();
//		
//		for (int i = 0; i < arraylength; i++) {
//			tempList.clear();
//			
//			ArrayList<FilmLocation> locationByTitle = datasource.selectRecordsByTitle(worldTitles.get(i));
//			
//			System.out.println("SELECT RECORDS BY TITLE COUNT: " + locationByTitle.size());
//			
//			String currentTitle = "TITLE";
//			
//			for (FilmLocation location : locationByTitle) {
//				currentTitle = location.getTitle();
//				tempList.add(location);
//			}
//			
//			filmMap.put(currentTitle, tempList);
//			
//
//		
//			
//			
//		}
//		
//		
////		locationMap = new LocationMapParcel();
//		locationMap.setLocationHashMap(filmMap);
//	}

	
	
	
	@Override
	 public void onResume() {
		 System.out.println("******* RESUME PAGER ACTIVITY ********");
		 // UPDATE AND REDRAW INTERFACE

//		if (tempUser != null) {
//			 currentUser = userImpl.selectRecordById(tempUser.getUserId());
//			 System.out.println("******* RESUME WORLD COUNT ********: " + currentUser.getWorldCount());
//		 }

//		Bundle extras = getIntent().getExtras();
//		
////		Bundle extras = intent.getExtras();
//		FilmArrayList localFilmArrayList = extras.getParcelable("locationArrayList");
//		System.out.println("Location Array List: " + localFilmArrayList);
//		locationList = localFilmArrayList.getFilmList();
		
//	       	Intent broadcast = new Intent();
//	       	broadcast.setAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
////	        filter.addCategory(Intent.CATEGORY_DEFAULT);
//	        sendBroadcast(broadcast);
		
//		IntentFilter filter = new IntentFilter();
//        filter.addAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
//        filter.addCategory(Intent.CATEGORY_DEFAULT);
//        registerReceiver(mReceiver, filter);

		
		if (locationList != null) {
			locationList = datasource.selectRecords();
			locationArrayList.setFilmList(locationList);
//			locationTitles = new String[locationArrayList.getFilmList().size()];
//			refreshLocationTitles(locationArrayList.getFilmList());

			Collections.sort(locationList, StaticSortingUtilities.LOCATIONS_ALPHABETICAL_ORDER);
			
			System.out.println("ON RESUME LOCATION LIST LENGTH: " + locationList.size());
			for (FilmLocation loc : locationList) {
				if (!worldTitles.contains(loc.getTitle())) {
					worldTitles.add(loc.getTitle());
					localWorldImageUrls.add(loc.getStaticMapImageUrl());
				}
			}

			mSectionsPagerAdapter.notifyDataSetChanged();
		}

//		refreshPagerCollections();

//		if (tempUser != null) {
//			 currentUser = userImpl.selectRecordById(tempUser.getUserId());
//			 System.out.println("******* RESUME WORLD COUNT ********: " + currentUser.getWorldCount()); 
//		 }

//		mReceiver.getResultExtras(true);
		
		 super.onResume();
	 }
	
	 private static void initializeReplayWorld(final QuizItem updatedQuizItem) {
			
		 
		 System.out.println("REPLAY DIALOG: " + dialog);
	 	
		 final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		 dialog.setContentView(R.layout.replay_level_overlay);
		 
//			quizitemsource = new QuizItemImpl(context);
//			newQuizList = quizitemsource.selectRecords();
		 System.out.println("QUIZ LIST SIZE: " + newQuizList.size());
//			String currentQuizId = updatedQuizItem.getWorldId();
		// RESET QUIZ ITEM ANSWERED STATE
//			final QuizItem tempQuizItem = quizitemsource.selectRecordById(currentQuizId);
		
	
//			// newQuizList = quizitemsource.selectRecordsByWorldTitle(title);
//			localQuizItemArrayList = new QuizItemArrayList();
//			localQuizItemArrayList.setQuizList(newQuizList);
	
//			intent.putExtra("localQuizItemArrayList", localQuizItemArrayList);
			
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlayLayout);
		layout.setOnClickListener(new OnClickListener() {
		 	@Override
			public void onClick(View arg0) {
		 		System.out.println("CLICKED REPLAY MODAL: " + updatedQuizItem.getAnswered());
//		 		if (updatedQuizItem.getAnswered().equals("true")) {
//		 			System.out.println("RESETTING QUIZ ITEM: " + updatedQuizItem.getWorldId());
//		 			System.out.println("CURRENT QUIZ ITEM ID: "+ updatedQuizItem.getQuestionId());
//		 			final String updatedQuizItemId = updatedQuizItem.getQuestionId();
		 			
//		 			RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
//					runner.execute(updatedQuizItemId);
		 			
		//			 			// RESET AND PERSIST QUIZ STATE
		//			 			final String updatedQuizItemId = updatedQuizItem.getQuestionId();
		//			 			quizitemsource.updateRecordAnswered(updatedQuizItemId, "FALSE");
//		 			dismissReplayDialog();
		 			dialog.dismiss();
		 		}
		//			 		final String RESTORE_LEVEL_DATA_URL = "http://movie-locations-app.appspot.com/secure/resetLevelData/";
		//					final String FINAL_RESTORE_LEVEL_DATA_URL = RESTORE_LEVEL_DATA_URL + title;
		//						RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
		//						runner.execute(FINAL_RESTORE_LEVEL_DATA_URL);
		//				 		dialog.dismiss();
//		 	}
		});
		dialog.show();
	 }

//		private static void dismissReplayDialog() {
////			if (dialog != null) {
//				dialog.dismiss();	
////			}
//			
//		}
	 
//	 public static class RestoreLevelDataTaskRunner extends AsyncTask<String, String, String> {
//
//			private String resp;
//			private ProgressDialog dialog;
//			private ArrayList<FilmLocation> filmLocationList;
//			
//			@Override
//			protected String doInBackground(String... params) {
//				publishProgress("Sleeping..."); // Calls onProgressUpdate()
//				try {
//					resp = params[0];
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.out.println("ERROR STACK TRACE");
//					resp = e.getMessage();
//				}
//				return resp;
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//			 */
//			@Override
//			protected void onPostExecute(String result) {
//				// execution of result of Long time consuming operation
//				if (dialog != null) {
//					dialog.dismiss();
//				}
//				String message = "Success!";
//				if (result.equals("success")) {
//					message = "Level data restored.";
//				} else if (result.equals("error")) {
//					message = "Something went wrong.";
//				} else {
//					message = "Level data reset.";
//					
////					quizItemList = quizItemImpl.selectRecordsByWorldTitle(result);
////					final QuizItem tempQuizItem = quizitemsource.selectRecordById(result);
//					System.out.println("ANSWERED QUIZ ID: " + result);
//					// RESET QUIZ ITEMS LOCALLY
//					// WE NEED TO GET THE WORLD TITLE
//					
////					for (QuizItem localQuizItem : quizItemList) {
//						// update database record
//					quizitemsource.updateRecordAnswered(result, "FALSE");
////					System.out.pringln
////						quizItemImpl.updateRecordCorrectAnswerIndex(localQuizItem.getQuestionId(), "null");
////					}
//				}
//				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
////				mDrawerLayout.closeDrawers();
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onPreExecute()
//			 */
//			@Override
//			protected void onPreExecute() {
//				// Things to be done before execution of long running operation. For
//				// example showing ProgessDialog
//
//				dialog = new ProgressDialog(context);
//				dialog.setTitle("Restoring...");
//				dialog.setMessage("Replay available in a moment.");
//				dialog.setCancelable(false);
//				dialog.setIndeterminate(true);
//				dialog.show();
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
//			 */
//			@Override
//			protected void onProgressUpdate(String... text) {
//				// finalResult.setText(text[0]);
//				// Things to be done while execution of long running operation is in
//				// progress. For example updating ProgessDialog
//			}
//		}
//		}
//	@Override
//	 public void onResume() {
//		 System.out.println("******* RESUME PAGER ACTIVITY ********");
//		 // UPDATE AND REDRAW INTERFACE
//		 
//		if (tempUser != null) {
//			 currentUser = userImpl.selectRecordById(tempUser.getUserId());
//			 System.out.println("******* RESUME WORLD COUNT ********: " + currentUser.getWorldCount()); 
//		 }
//		
//		if (datasource != null) {
//			
//			locationList = datasource.selectRecords();
//			locationArrayList.setFilmList(locationList);
////			locationTitles = new String[locationArrayList.getFilmList().size()];
////			refreshLocationTitles(locationArrayList.getFilmList());
//			
//			
////			reloadLevels();
//			
////			for (FilmLocation loc : locationList) {
////				if (!worldTitles.contains(loc.getTitle())) {
////					worldTitles.add(loc.getTitle());
////					localWorldImageUrls.add(loc.getStaticMapImageUrl());
////				}
////			}
////			
//			mSectionsPagerAdapter.notifyDataSetChanged();
//		}
//			
////		refreshPagerCollections();
//			
//		if (tempUser != null) {
//			 currentUser = userImpl.selectRecordById(tempUser.getUserId());
//			 System.out.println("******* RESUME WORLD COUNT ********: " + currentUser.getWorldCount()); 
//		 }
//		
//		 super.onResume();
//	 }
	
//	// CALL THIS ON PAGE RETURN
//	private static void refreshPagerCollections() {
//		
//		int currentPagerIndex = mViewPager.getCurrentItem();
//		
//		localFilmArrayList = filmMap.get(locationTitles[currentPagerIndex]);
//		localAdapterFilmList = new ArrayList<FilmLocation>();
//		
//		
//		
//		for (int i = 0; i < localFilmArrayList.size(); i++) {
//			
////			if (localFilmArrayList.get(i).getTitle().equals(localFilmArrayList.get(currentPositionIndex).getTitle())) {
//				localAdapterFilmList.add(localFilmArrayList.get(i));
////			}
//			
//		}
//		
////		commentAdapter = new FilmLocationMapTileArrayAdapter(context, intent, localAdapterFilmList);
////
////		
////		
////			commentView.setAdapter(commentAdapter);
//		
////		mViewPager.notify();
//		
//	}
//	// initialize first world
//	private void initializeFirstMovie() {
//		currentLocation = locationArrayList.getFilmList().get(0);
//		
//		System.out.println("current world location" + currentLocation.getTitle());
//		
//		// pass through intent 
//
//		intent = new Intent(this, WorldLocationDetailActivity.class);
//		
//		// pass only current location
//		intent.putExtra("locationArrayList", locationArrayList);
//		intent.putExtra("currentLocation", currentLocation);
//		intent.putExtra("bagItemArrayList", bagItemArrayList);
//		intent.putExtra("localUser", currentUser);
//		
//	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.director_sort, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

//		private int currentPositionIndex;
		final ArrayList<String> titles = new ArrayList<String>();

		public SectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a MovieSectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			// System.out.println("INDEX: " + position);
			Fragment fragment = new MovieSectionFragment();
			Bundle args = new Bundle();
			args.putInt(MovieSectionFragment.ARG_SECTION_NUMBER, position);
			args.putStringArrayList("worldTitles", worldTitles);
			args.putStringArrayList("localWorldImageUrls", localWorldImageUrls);
			args.putParcelable("locationArrayList", locationArrayList);
			args.putParcelable("locationMap", locationMap);
//			args.putParcelable("locationArrayList", locationArrayList);
			args.putParcelable("currentLocation", currentLocation);
			args.putParcelable("bagItemArrayList", bagItemArrayList);
			args.putParcelable("currentUser", currentUser);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show total pages.
			int worldCount = worldTitles.size();
			
//			if (currentUser == null) {
//				currentUser = tempUser;
//			}
			
//			if (currentUser.getWorldCount().equals("0")) {
//				worldCount = 1;
//			} else {
//				worldCount = Integer.parseInt(currentUser.getWorldCount());
//			}
			
			// THIS WON'T WORK IF WE'RE RESTORING USER DATA
//			worldCount = Integer.parseInt(currentUser.getWorldCount());
			System.out.println("WORLD COUNT: " + worldCount);
			return worldCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
//			Locale l = Locale.getDefault();
//			title = filmArrayList.get(position).getTitle();
//			title = filmList.get(position).getTitle();
			
			// TODO: PUT ALL PAGE TITLES INTO AN ARRAY AND ITERATE
//			locationTitles = new String[locationArrayList.getFilmList().size()];

			System.out.println("WORLD TITLES SIZE: " + worldTitles.size());
			String localTitle = worldTitles.get(position);
//			currentPositionIndex = position;
			
//			if (title == null) {
//				title = locationTitles[0];
//			}
			
//			for (FilmLocation loc : locationArrayList.getFilmList()) {
//				System.out.println("PAGE TITLE: " + loc.getTitle());	
//			}
			
////			System.out.println("PAGE POSITION: " + position);
////			System.out.println("PAGE POSITION TITLE: " + title);
//			
////			int currentPagerIndex = mViewPager.getCurrentItem();
//			System.out.println("GET PAGE TITLE ADAPTER INDEX: " + position);
//			
////			refreshLocationTitles(locationArrayList.getFilmList());
//			
////			filmMap = FilmLocationCollection.createFilmLocationMap(locationList);
//			
//			localFilmArrayList = filmMap.get(title);
//			localAdapterFilmList = new ArrayList<FilmLocation>();
//
//			System.out.println("GET PAGE TITLE ADAPTER INDEX locationTitles[position]: " + title);
//			System.out.println("GET PAGE TITLE ADAPTER INDEX localFilmArrayList: " + localFilmArrayList);
//			
//			
//			for (int i = 0; i < localFilmArrayList.size(); i++) {
//				
////				if (localFilmArrayList.get(i).getTitle().equals(localFilmArrayList.get(currentPositionIndex).getTitle())) {
//					localAdapterFilmList.add(localFilmArrayList.get(i));
////				}
//				
//			}
			
			return localTitle;
		}
//		tempAdapterList
	    public int getItemPosition(Object item) {
	    	MovieSectionFragment fragment = (MovieSectionFragment) item;
	        String title = fragment.getFragmentTitle();
	        int position = worldTitles.indexOf(title);

	        if (position >= 0) {
	            return position;
	        } else {
	            return POSITION_NONE;
	        }
	    }
		
//		@Override
//		public int getItemPosition(Object object) {
//
//		    Fragment oFragment = (Fragment) object;
//		    oPooledFragments=new ArrayList<>(oFragmentManager.getFragments());
//		    if(oPooledFragments.contains(oFragment))
//		        return POSITION_NONE;
//		    else
//		        return POSITION_UNCHANGED;
//		    } 
//		}

//		private String formatFilmData(int position) {
//			filmList = new ArrayList<String>(filmMap.keySet());
//			String filmTitle = "Title Missing";
//			if (locationMap.get(position) != null) {
//				filmTitle = locationMap.get(position).getTitle();
//			}
//			return filmTitle;
//		}
	}

	/**
	 * Movie fragment
	 * 
	 */
	public static class MovieSectionFragment extends Fragment implements
			OnPageChangeListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
//		getArguments().getInt(ARG_SECTION_NUMBER))

		protected ImageLoader imageLoader = ImageLoader.getInstance();
		
		private String locationTitle;
		private ArrayList<String> localWorldTitles;

		private int currentPositionIndex;

		private LocationMapParcel localMapParcel;

		private String currentTitle;
		
		private ArrayList<FilmLocation> tempAdapterList;
		
		private FilmLocationMapTileArrayAdapter commentAdapter;
		
		private ListView commentView;
		
		private HashMap<String, ArrayList<FilmLocation>> localMap;

		private String fragmentTitle;

		private String currentImageUrl;

		private ArrayList<String> localWorldImageUrls;

		private FilmArrayList filmArrayList;

		private View rootView;

//		private int currentPositionIndex = 0;
		
//		public int currentPositionIndex = 1;

		public MovieSectionFragment() {
			super();
		}

		public String getFragmentTitle() {
			return fragmentTitle;
		}
		
		public void setFragmentTitle(String fragmentTitle) {
			this.fragmentTitle = fragmentTitle;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		
		// ON PAGE CHANGED UPDATE
		@Override
		public void onPageSelected(int position) {
//			setCurrentPosition(position);
			System.out.println("PAGE INDEX CHANGED: " + position);
			
			

//			String currentLoc = filmList.get(getCurrentPosition());
			
			// TODO: refactor all unnecessary linkedhashmaps into arrays
//			currentLocation = filmList.get(position);
			
//			currentPositionIndex  = position;
			
//			HashMap<String, ArrayList<FilmLocation>> localMap = localMapParcel.getLocationHashMap();
//			String localCurrentTitle = localWorldTitles.get(position);
//			
//			tempAdapterList = localMap.get(localCurrentTitle); // USE NON-STATIC MEMBER HERE
//			FilmLocationMapTileArrayAdapter commentAdapter = new FilmLocationMapTileArrayAdapter(getActivity(), intent, tempAdapterList);
//			
//			ListView commentView = (ListView) getView().findViewById(R.id.listviewMapTiles);
//			commentView.setAdapter(commentAdapter);
			
			
			// THIS NEEDS TO GO
			// THIS ONLY SETS THE FIRST
//			currentLocation = locationArrayList.getFilmList().get(position);
//			currentPositionIndex = position;
			
//			for (int i = 0; i < localFilmArrayList.size(); i++) {
//				
////				if (localFilmArrayList.get(i).getTitle().equals(localFilmArrayList.get(currentPositionIndex).getTitle())) {
//					localAdapterFilmList.add(localFilmArrayList.get(i));
////				}
//				
//			}
			
//			ArrayList<FilmLocation> localFilmArrayList = locationArrayList.getFilmList();
//			ArrayList<FilmLocation> localAdapterFilmList = new ArrayList<FilmLocation>(); 
//			
//			for (int i = 0; i < localFilmArrayList.size(); i++) {
//				localAdapterFilmList.add(localFilmArrayList.get(i));
//			}
			
////			filmMap
//			ArrayList<FilmLocation> localFilmList = filmMap.get(locationTitles[position]); 
//			
////			ArrayList<FilmLocation> localFilmList = new ArrayList<FilmLocation>(); 
//			for (FilmLocation loc : locationArrayList.getFilmList()) {
////				if (loc.getTitle().equals(locationTitles[position])) {
//					localFilmList.add(loc);
////					System.out.println("PAGE TITLE EQUALS LOCATION TITLE: " + title);
////				}
//			}
//			currentWorldLocation = worldLocationList.get(position);
			
//			intent = new Intent(this, WorldLocationDetailActivity.class);
			
			// pass only current location
//			intent.putExtra("locationArrayList", locationArrayList);
			
		
			
			
			
			
			
			
			
			
			
			
			// THIS NEEDS TO BE REFACTORED
//			if (currentLocation != null) {
////				Intent updatedIntent = new Intent(getActivity(), WorldLocationDetailActivity.class);
////				updatedIntent.putExtra("worldLocationList", list);
//				intent.putExtra("currentLocation", currentLocation);
//				intent.putExtra("locationArrayList", locationArrayList);
//				intent.putExtra("bagItemArrayList", bagItemArrayList);
//			}

//			commentView = (ListView) getView().findViewById(R.id.listviewMapTiles);
			
//			setFragmentTitle(localWorldTitles.get(position));
//			System.out.println("GET FRAGMENT TITLE: " + getFragmentTitle());
//			tempAdapterList = localMap.get(getFragmentTitle());
//			commentAdapter.notifyDataSetChanged();
		}

//		@Override
//		  public void onRestoreInstanceState(Bundle savedInstanceState) {
//		    super.onRestoreInstanceState(savedInstanceState);
//		    // Restore UI state from the savedInstanceState.
//		    // This bundle has also been passed to onCreate.
//		    boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
//		    double myDouble = savedInstanceState.getDouble("myDouble");
//		    int myInt = savedInstanceState.getInt("MyInt");
//		    String myString = savedInstanceState.getString("MyString");
//		  }
		

		public void prepareArrayAdapterData(View rootView) {
			ArrayList<QuizItem> finalQuizList = new ArrayList<QuizItem>();
			boolean answered = true;
			QuizItem replayQuizItem = null;
			QuizItem firstLocation = newQuizList.get(0);
			currentTitle = firstLocation.getWorldTitle();
			
			for (QuizItem quizItem : newQuizList) {
				System.out.println("LOCATION TITLE 1:" + currentTitle);
				System.out.println("LOCATION TITLE 2:" + quizItem.getWorldTitle());
				System.out.println("LOCAL CURRENT LOCATION CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
				if (quizItem.getWorldTitle().equals(currentTitle)) {
					finalQuizList.add(quizItem);
//					System.out.println("QUIZ ITEM GET ANSWERED:" + quizItem.getAnswered());
					if (quizItem.getAnswered().equals("FALSE")) {
						answered = false;
					}
					replayQuizItem = quizItem;
				}
			}
			if (answered == true) {
//				System.out.println("QUIZ ITEM ANSWERED: " + replayQuizItem.getAnswered());
				System.out.println("QUIZ ITEM ANSWERED TRUE");
				System.out.println("REPLAY QUIZ ITEM: " + replayQuizItem);
				if (replayQuizItem != null) {
					initializeReplayWorld(replayQuizItem);
				}
				
			}
			
		}
		
		@Override
		public void onResume() {
			System.out.println("Resume");
			prepareArrayAdapterData(getRootView());
			super.onResume();
		}
		
		public void setRootView(View rootView) {
			this.rootView = rootView;
		}
		
		public View getRootView() {
			return rootView;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_film_panel, container, false);
			
//			ListView locationsList = (ListView) rootView
//					.findViewById(R.id.locationsView1);
			
			setRootView(rootView);
			prepareArrayAdapterData(rootView);
			
			
			
//			Bundle bundle = savedInstanceState.getBundle("locationMap");
//			LocationMapParcel locationMap = (LocationMapParcel) savedInstanceState.getParcelable("locationMap");
//			locationMap.setWorldLocationList(filmMap);
			
//			bundle.putParcelable("locationMap", locationMap);
			
			
			// WE NEED AN ARRAY LIST OF ARRAY LISTS
			// TO PASS THROUGH AS A PARCEL
			localMapParcel = getArguments().getParcelable("locationMap");
			
//			args.putParcelable("locationArrayList", locationArrayList);
//			args.putParcelable("currentLocation", currentLocation);
//			args.putParcelable("bagItemArrayList", bagItemArrayList);
//			args.putParcelable("currentUser", currentUser);

//			ArrayList<ArrayList<FilmLocation>> localLocationsList = new ArrayList<ArrayList<FilmLocation>>();
			localMap = localMapParcel.getLocationHashMap();
			

//		    Iterator<Entry<String, ArrayList<FilmLocation>>> iterator = localMap.entrySet().iterator();
		    
//		    while (iterator.hasNext()) {
//		        Map.Entry<String, ArrayList<FilmLocation>> pairs = (Map.Entry<String, ArrayList<FilmLocation>>) iterator.next();
//		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
//		        localLocationsList.add(pairs.getValue());
//		        iterator.remove(); // avoids a ConcurrentModificationException
//		    }
			
//			FilmLocation loc = localMap.get(currentTitle).get(0);
			
//			args.putParcelable("locationArrayList", locationArrayList);
//			args.putParcelable("currentLocation", currentLocation);
//			args.putParcelable("bagItemArrayList", bagItemArrayList);
//			args.putParcelable("currentUser", currentUser);
			
			FilmArrayList localLocationArrayList = getArguments().getParcelable("locationArrayList");
//			FilmLocation localCurrentLocation = getArguments().getParcelable("currentLocation");
			BagItemArrayList localBagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			User localCurrentUser = getArguments().getParcelable("currentUser");
			
			localWorldTitles = getArguments().getStringArrayList("worldTitles");
			localWorldImageUrls = getArguments().getStringArrayList("localWorldImageUrls");
			filmArrayList = getArguments().getParcelable("locationArrayList");
			ArrayList<FilmLocation> filmList = filmArrayList.getFilmList();
			
//			System.out.println("LOCAL WORLD TITLES 0: " + localWorldTitles.get(0));
//			System.out.println("LOCAL WORLD TITLES 1: " + localWorldTitles.get(1));
			
			
			currentTitle = localWorldTitles.get(getArguments().getInt(ARG_SECTION_NUMBER));
			currentImageUrl = localWorldImageUrls.get(getArguments().getInt(ARG_SECTION_NUMBER));
			
			setFragmentTitle(currentTitle);
			
			System.out.println("CURRENT FRAGMENT TITLE: " + currentTitle);
//			String localTitle = getArguments().getString("locationTitle");
			
			System.out.println("LOCAL MAP PARCEL: " + localMapParcel);
			
			
			
//			tempAdapterList = localMap.get(currentTitle); // USE NON-STATIC MEMBER HERE
//			
//			setTempAdapterList(tempAdapterList);
			//currentTitle
			ArrayList<FilmLocation> finalList = new ArrayList<FilmLocation>();
			
			for (FilmLocation loc : filmList) {
				if (loc.getTitle().equals(currentTitle)) {
					finalList.add(loc);
				}
			}
			
			

			
			intent = new Intent(context, WorldLocationDetailActivity.class);
			
			FilmLocation localCurrentLocation = finalList.get(0);
					
			// pass only current location
			intent.putExtra("locationArrayList", localLocationArrayList);
			intent.putExtra("currentLocation", localCurrentLocation);
			intent.putExtra("bagItemArrayList", localBagItemArrayList);
			intent.putExtra("localUser", localCurrentUser);
			System.out.println("PAGER ACTIVITY CURRENT USER POINTS: " + localCurrentUser.getCurrentPoints());
//			
			commentAdapter = new FilmLocationMapTileArrayAdapter(getActivity(), intent, finalList);
//			
			commentView = (ListView) rootView.findViewById(R.id.listviewMapTiles);
			
//			commentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//					startActivity(intent);
//				}
//			});
			commentView.setAdapter(commentAdapter);
			
			commentAdapter.notifyDataSetChanged();
			
//			FilmArrayList localLocationArrayList = getArguments().getParcelable("locationArrayList");
//			FilmLocation localCurrentLocation = getArguments().getParcelable("currentLocation");
//			BagItemArrayList bagItemArrayList = getArguments().getParcelable("bagItemArrayList");
//			User currentUser = getArguments().getParcelable("currentUser");
			
//			// default background click area
//			rootView.setOnClickListener(new LinearLayout.OnClickListener() {
//				public void onClick(View v) {
//					startActivity(intent);
//				}
//			});
			
//			ArrayList<FilmLocation> imageTileArrayList = new ArrayList<FilmLocation>();
//			
//			for (FilmLocation collection : locationList) {
//				if (collection.getTitle().equals(title)) {
//					imageTileArrayList.add(collection);
//				}
//			}
			
//			ArrayList<FilmLocation> imageTileArrayList = filmMap.get(title);
//			
//			
//			for (FilmLocation loc : imageTileArrayList) {
//				System.out.println("FILM MAP LOCATION: " + loc.getTitle());
//			}
			
			// LOCATION LIST SORT BY WORLD COUNT
			
			
//			locationArrayList.getFilmList()
//			ArrayList<FilmLocation> localFilmArrayList = locationArrayList.getFilmList();
			
			// START ON FIRST ARRAY INDEX
			// TRY LEVEL MATCH WITH LANDING PAGE AT INDEX ZERO (0)
			
//			ListView
//			refreshPagerCollections();
			
			
//			
//			int currentPagerIndex = mViewPager.getCurrentItem();
//			
//			localFilmArrayList = filmMap.get(locationTitles[currentPagerIndex]);
//			localAdapterFilmList = new ArrayList<FilmLocation>();
//			
//			
//			
//			for (int i = 0; i < localFilmArrayList.size(); i++) {
//				
////				if (localFilmArrayList.get(i).getTitle().equals(localFilmArrayList.get(currentPositionIndex).getTitle())) {
//					localAdapterFilmList.add(localFilmArrayList.get(i));
////				}
//				
//			}
			
////			ArrayList<FilmLocation> localFilmList = new ArrayList<FilmLocation>(); 
//			for (FilmLocation loc : locationArrayList.getFilmList()) {
////				if (loc.getTitle().equals(locationTitles[position])) {
//					localFilmList.add(loc);
////					System.out.println("PAGE TITLE EQUALS LOCATION TITLE: " + title);
////				}
//			}
			
			
//			ArrayList<FilmLocation> tempLocationList = new ArrayList<FilmLocation>();
////			currentPositionIndex
////			String currentLevel = Integer.toString(currentPositionIndex);
//			
////			FilmLocation locationIndex = locationList.get(currentPositionIndex);
//			
////			currentPositionIndex
////			System.out.println("CURRENT POSITION INDEX: " + currentPositionIndex);
//			for (FilmLocation loc : locationList) {
////				if (loc.getTitle().equals(title)) {
//					System.out.println("GET LEVEL: " + loc.getLevel());
////					System.out.println("GET CURRENT LEVEL: " + currentLevel);
//					tempLocationList.add(loc);
////				}
//			}
			
//			for (int i = 0; i < localFilmArrayList.size(); i++) {
//				
////				if (localFilmArrayList.get(i).getTitle().equals(localFilmArrayList.get(currentPositionIndex).getTitle())) {
//					localAdapterFilmList.add(localFilmArrayList.get(i));
////				}
//				
//			}
			
			
//			int currentPagerIndex = mViewPager.getCurrentItem();
//			System.out.println("CREATED A VIEW: " + currentPagerIndex);
//			
//			localFilmArrayList = filmMap.get(locationTitles[currentPagerIndex]);
//			localAdapterFilmList = new ArrayList<FilmLocation>();
//			
//			
//			
//			for (int i = 0; i < localFilmArrayList.size(); i++) {
//				
////				if (localFilmArrayList.get(i).getTitle().equals(localFilmArrayList.get(currentPositionIndex).getTitle())) {
//					localAdapterFilmList.add(localFilmArrayList.get(i));
////				}
//				
//			}
			
//			// GET FILM MAP FROM BUNDLE
//			final Set<Entry<String, ArrayList<FilmLocation>>> mapValues = filmMap.entrySet();
//			final int maplength = mapValues.size();
//			final Entry<String,ArrayList<FilmLocation>>[] mapArray = new Entry[maplength];
//			mapValues.toArray(mapArray);
//			
//			ArrayList<FilmLocation> tempAdapterList;
			
//			if (initializedIndex > 1) {
//				tempAdapterList = filmMap.get(title);
//				System.out.println("TEMP INITIALIZED TRUE: " + initializedIndex);
//			} else {
//				tempAdapterList = mapArray[0].getValue();
//				System.out.println("TEMP INITIALIZED FALSE: " + initializedIndex);
//			}
//			tempAdapterList = mapArray[0].getValue();
////			ArrayList<FilmLocation> tempAdapterList = filmMap.get(title);
//			
//			
//			System.out.println("TEMP POSITION INDEX: " + currentPositionIndex);
//			System.out.println("TEMP ADAPTER 0: " + filmMap);
//			System.out.println("TEMP ADAPTER 1: " + title);
//			System.out.println("TEMP ADAPTER TITLE: " + title);
//			System.out.println("TEMP ADAPTER LENGTH: " + tempAdapterList.size());
//			System.out.println("TEMP FILM MAP LENGTH: " + filmMap.size());
//			
			
			
			
//			System.out.println("GET PAGE TITLE ADAPTER INDEX locationTitles[position]: " + title);
//			System.out.println("GET PAGE TITLE ADAPTER INDEX localFilmArrayList: " + localFilmArrayList);
			
			
//			for (int i = 0; i < tempAdapterList.size(); i++) {
//				
////				if (localFilmArrayList.get(i).getTitle().equals(localFilmArrayList.get(currentPositionIndex).getTitle())) {
//				tempAdapterList.add(localFilmArrayList.get(i));
////				}
//				
//			}
			
			
			
//		   Iterator it = localMap.entrySet().iterator();
//		    while (it.hasNext()) {
//		        Map.Entry pairs = (Map.Entry)it.next();
//		        System.out.println("MAP ENTRY: " + pairs.getKey() + " = " + pairs.getValue());
////		        it.remove(); // avoids a ConcurrentModificationException
//		    }
			    
			
			
//			initializedIndex++;
			
//			if (!currentLocation.getLocations().equals("null")) {
//				commentView.setAdapter(commentAdapter);
//			} else {
			
//				ImageView defaultMapTile = (ImageView) rootView.findViewById(R.id.defaultMapTile1);
//				defaultMapTile.setVisibility(ImageView.VISIBLE);
//				defaultMapTile.setOnClickListener(new LinearLayout.OnClickListener() {
//					public void onClick(View v) {
//						startActivity(intent);
//					}
//				});
//			}
//			
//			
//			intent = new Intent(context, WorldLocationDetailActivity.class);
//			
//			// pass only current location
//			intent.putExtra("locationArrayList", locationArrayList);
//			intent.putExtra("currentLocation", currentLocation);
//			intent.putExtra("bagItemArrayList", bagItemArrayList);
//			intent.putExtra("localUser", currentUser);
//			
//			// default background click area
//			rootView.setOnClickListener(new LinearLayout.OnClickListener() {
//				public void onClick(View v) {
//					startActivity(intent);
//				}
//			});
			
			
//			ImageView defaultMapTile = (ImageView) rootView.findViewById(R.id.defaultMapTile1);
//			defaultMapTile.setVisibility(ImageView.VISIBLE);
//			defaultMapTile.setBackgroundResource(firstItem);
//			imageLoader.displayImage(currentImageUrl, defaultMapTile);
			
			
//			defaultMapTile.setOnClickListener(new LinearLayout.OnClickListener() {
//				public void onClick(View v) {
//					startActivity(intent);
//				}
//			});

//			TextView filmTitle = (TextView) rootView.findViewById(R.id.film_title);
//			filmTitle.setText(currentTitle);
			
			mViewPager.setOnPageChangeListener(this);

			
			return rootView;
		}

		private void setTempAdapterList(ArrayList<FilmLocation> adapterList) {
			tempAdapterList = adapterList;
		}
		
		private ArrayList<FilmLocation> getTempAdapterList() {
			return tempAdapterList;
		}
	}

	public class QuizItemTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
		private JsonNode json;
		private ProgressDialog dialog;
		

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
//				// Do your long operations here and return the result
////				String url = params[0];
//				String response = "response message";
//				String serverUrl = params[0];
//
				
				
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

			if (dialog != null) {
				dialog.setTitle("Messaging complete.");
				dialog.setMessage("Time for more trivia!");
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
			dialog.setTitle("Sending...");
			
			// TODO: create random text generator
			// for messages so that people with 
			// slower connections are kept occupied
			dialog.setMessage("Should only take a sec.");
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


}
