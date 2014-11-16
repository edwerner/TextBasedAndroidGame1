package com.movie.locations.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.movie.locations.R;
import com.movie.locations.R.drawable;
import com.movie.locations.R.id;
import com.movie.locations.R.layout;
import com.movie.locations.R.menu;
import com.movie.locations.R.string;
import com.movie.locations.application.FilmLocationPagerActivity.MovieSectionFragment;
import com.movie.locations.application.FilmLocationPagerActivity.SectionsPagerAdapter;
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.ConclusionCardImpl;
import com.movie.locations.dao.GameTitleImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.NewsItemImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.Comment;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.FilmLocationCollection;
import com.movie.locations.domain.Friend;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.NavMenuItem;
import com.movie.locations.domain.NewsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.gcm.GcmIntentService;
import com.movie.locations.service.DatabaseChangedReceiver;
import com.movie.locations.service.FilmLocationService;
import com.movie.locations.util.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
//import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class NewsActivity extends ActionBarActivity {

	// navigation drawer
	private static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navArray;
	// get intent string attributes
	// Intent intent = getIntent();
	protected static ImageLoader imageLoader = ImageLoader.getInstance();

	private static User localUser;
	
	private static int pos;
	
	private Fragment userDetailFragment;
	private Fragment newsFragment;
	private FragmentTransaction ft;
	private static Intent intent;
	private static Context context;
//	private ConclusionCardImpl conclusionCardData;
	private User currentUser;
	private static ConclusionCardImpl conclusionCardImpl;
	private static MovieLocationsImpl datasource;
	private static QuizItemImpl quizitemsource;
	private static ArrayList<ConclusionCard> cardList;
	private static ConclusionCardArrayAdapter conclusionCardAdapter;
	private static BagItemImpl bagItemImpl;

	private static UserImpl userSource;
	

	private Button restoreLevelDataButton;
	private static ArrayList<BagItem> bagItemList;
	public static ArrayList<QuizItem> quizItemList;
	public static QuizItemImpl quizItemImpl;
	private static SwipeRefreshLayout swipeLayout;
	private static FilmArrayList locationArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		
		context = this;

//		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new NewsFragment()).commit();
			Bundle bundle = getIntent().getExtras();
			localUser = bundle.getParcelable("localUser");
			System.out.println("NEWS ACTIVITY LOCAL USER PARCEL CURRENT POINTS: " + localUser.getCurrentPoints());
			intent = getIntent();
//		}
		
		
		GcmIntentService.setCurrentUserId(localUser.getUserId());
		
		// setup navigation drawer
		navArray = new String[7];
		navArray[0] = "News";
		navArray[1] = "Profile";
//		navArray[2] = "Friends";
		navArray[2] = "Worlds";
//		navArray[3] = "Restore";
		navArray[3] = "Cards";
		navArray[4] = "Items";
		navArray[5] = "About";
		navArray[6] = "Settings";
//		navArray[6] = "Help";
		
		String[] navImageUrl = new String[navArray.length];
		final String userImageUrl = localUser.getAvatarImageUrl();
		navImageUrl[0] = "http://www.truevalhalla.com/blog/wp-content/uploads/2013/11/icon-news.png";
		navImageUrl[1] = userImageUrl;
		navImageUrl[2] = "http://icons.iconarchive.com/icons/fasticon/freestyle/128/tree-icon.png";
//		navImageUrl[3] = "http://www.softwarecrew.com/wp-content/uploads/2012/04/wise_data_recovery_icon1.png";
		navImageUrl[3] = "http://d1hwvnnkb0v1bo.cloudfront.net/content/art/app/icons/key_ring_reward_cards_icon.jpg";
		navImageUrl[4] = "https://d13yacurqjgara.cloudfront.net/users/114243/screenshots/1272737/screen_shot_2013-10-15_at_2.44.14_am_teaser.png";
		navImageUrl[5] = "http://png-1.findicons.com/files/icons/552/aqua_candy_revolution/128/get_info.png";
		navImageUrl[6] = "http://etc-mysitemyway.s3.amazonaws.com/icons/legacy-previews/icons/matte-blue-and-white-square-icons-business/116957-matte-blue-and-white-square-icon-business-gear11.png";
		

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		
		// CREATE ARRAY ADAPTER FOR MENU ITEMS
		// CREATE MENU ITEM DOMAIN OBJECT
		// ATTRIBUTES:
		// --------------------------------
		// TITLE
		// IMAGE URL
		// LINK-THROUGH FRAGMENT
		
//		NewsArrayAdapter adapter = new NewsArrayAdapter(getActivity(), intent, newsUpdateList);
		
		ArrayList<NavMenuItem> navList = new ArrayList<NavMenuItem>();
		
		for (int a = 0; a < navArray.length; a++) {
			NavMenuItem menuItem = new NavMenuItem();
			menuItem.setTitle(navArray[a]);
			menuItem.setImageUrl(navImageUrl[a]);
			navList.add(menuItem);
		}
		NavMenuItemArrayAdapter menuAdapter = new NavMenuItemArrayAdapter(this, intent, navList);
		
//		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navArray));
		mDrawerList.setAdapter(menuAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

//		if (savedInstanceState == null) {
//			selectItem(0);
//		}
		
		if (localUser != null) {
			if (localUser.getWorldCount().equals("1")) {
				// display help fragment
				selectItem(5);
//				System.out.println("CURRENT LEVEL: " + localUser.getCurrentLevel());
			}
		} else {
			selectItem(0);
		}
		
		
		
		userSource = new UserImpl(this);
//		User tempUser = userSource.selectRecordById(localUser.getUserId());
//		tempUser.setCurrentLevel(localUser.getCurrentLevel());
//		tempUser.setCurrentPoints(localUser.getCurrentPoints());
//		tempUser.setWorldCount(localUser.getWorldCount());
		final String FINAL_USER_ID = localUser.getUserId();
		final String FINAL_USER_CURRENT_LEVEL = localUser.getCurrentLevel();
		final String FINAL_USER_POINTS = localUser.getCurrentPoints();
		final String FINAL_USER_WORLD_COUNT = localUser.getWorldCount();
		userSource = new UserImpl(this);
//		localUser = userSource.selectRecordById(localUser.getUserId());
		
		userSource.updateWorldCount(FINAL_USER_ID, FINAL_USER_WORLD_COUNT);
		System.out.println("WORLD COUNT LOGGING: " + FINAL_USER_WORLD_COUNT);
//		System.out.println("WORLD COUNT LOGGING: " + FINAL_USER_WORLD_COUNT);
		userSource.updateRecordBonusPointsValue(FINAL_USER_ID, FINAL_USER_POINTS, "0");
		userSource.updateCurrentUserLevel(FINAL_USER_ID, FINAL_USER_CURRENT_LEVEL);
		
		datasource = new MovieLocationsImpl(this);
		conclusionCardImpl = new ConclusionCardImpl(this);
		cardList = conclusionCardImpl.selectRecords();
		quizitemsource = new QuizItemImpl(this);
		
		// DEFAULTS TO NEWS PAGE
		bagItemImpl = new BagItemImpl(this);
		// BAG ITEM NEWS ITEM
		bagItemList = bagItemImpl.selectRecords();
		System.out.println("BAG ITEM LIST SIZE: " + bagItemList.size());
		ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
		System.out.println("START NEWS ACTIVITY LOCATION LIST LENGTH: " + tempLocationList.size());
		FilmArrayList tempLocationArrayList = new FilmArrayList();
		tempLocationArrayList.setFilmList(tempLocationList);
		setUpdatedNewsData(tempLocationArrayList);
		
		// TODO: CREATE GAME TITLE DATABASE IMPLEMENTATION
		
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
//		filter.addCategory(Intent.CATEGORY_DEFAULT);
//		registerReceiver(mReceiver, filter);
	    
	}

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mReceiver);
    }
    
    
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
//        CheckBox mobileCheckbox = (CheckBox) R.id.emailNotifications1;
        

        
        
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxNotifications1:
                if (checked) {
                	System.out.println("Mobile notifications checked");
                	localUser.setMobileNotifications("true");
                } else {
                	localUser.setMobileNotifications("false");
                }
                break;
            case R.id.emailNotifications1:
            	if (checked) {
                	System.out.println("Email notifications checked");
                	localUser.setEmailNotifications("true");
                } else {
                	localUser.setEmailNotifications("false");
                }
                break;
            // TODO: Veggie sandwich
        }
    }
//	public DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
//		
//		@Override
//		public void onReceive(Context context, Intent intent) {
//	       // update your list
//		   
//		   
//		   Bundle extras = intent.getExtras();
//	//		newsIntent.putExtra("locationArrayList", localLocationArrayList);
//			locationArrayList = extras.getParcelable("locationArrayList");
//			System.out.println("DATABASE_CHANGED: " + locationArrayList);
//			ArrayList<FilmLocation> tempLocationList = locationArrayList.getFilmList();
//			
//			for (FilmLocation loc : tempLocationList) {
//				System.out.println("DATABASE_CHANGED: " + loc.getLocations());
//			}
//			
//			ArrayList<FilmLocation> tempLocationList2 = datasource.selectRecords();
//			for (FilmLocation loc2 : tempLocationList2) {
//				System.out.println("DATABASE_CHANGED AFTER: " + loc2.getLocations());
//			}
//			System.out.println("DATABASE CHANGED LIST LENGTH: " + tempLocationList2.size());
//			
//			
//			// UPDATE LOCAL COLLECTIONS WITH PAYLOAD DATA
//			
//			setUpdatedNewsData(locationArrayList);
//			
//			
//			
//			
//			
//			
//			
//		   System.out.println("UPDATED DATA FROM RECEIVER");
//		   
//	   }
//		
//	
//	};
	
	

	public static void setUpdatedNewsData(FilmArrayList locationArrayList) {
		NewsActivity.locationArrayList = locationArrayList;
	}
	
	public static FilmArrayList getUpdatedNewsData() {
		return locationArrayList;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news, menu);
		getMenuInflater().inflate(R.menu.user_detail, menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		// If the nav drawer is open, hide action items related to the content
		// view
		
		
		
		
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		



//    	String CONFIRM_MESSAGE = "Yes";
//    	String CANCEL_MESSAGE = "No";
//    	String DIALOG_TITLE = "Delete and restore current world - are you sure?";
//    	String DIALOG_MESSAGE = "You'll lose any existing progress.";
//		
//		
//		
//    	// CREATE CONFIRMATION DIALOG
//    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    	
//    	builder.setMessage(DIALOG_MESSAGE).setTitle(DIALOG_TITLE);
//    	
//    	// TODO: ITERATE AND DELETE CURRENT LEVEL DATA
//    	// - BAG ITEMS
//    	// - QUIZ ITEMS
//    	// - LOCATIONS
//    	
//    	
//    	
//
//    	
//    	
//    	
//		// Add the buttons
//    	builder.setPositiveButton(CONFIRM_MESSAGE, new DialogInterface.OnClickListener() {
//           public void onClick(DialogInterface dialog, int id) {
//               // User clicked OK button
//        	   System.out.println("RESTORED");
//        	   
//        	   // START ASYNC THREAD
//        	   RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
//        	   runner.execute("http://movie-locations-app.appspot.com/secure/restoreLevelData");
//           }
//       });
//    	builder.setNegativeButton(CANCEL_MESSAGE, new DialogInterface.OnClickListener() {
//           public void onClick(DialogInterface dialog, int id) {
//               // User cancelled the dialog
//        	   System.out.println("CANCELLED");
//           }
//       });
    	// Set other dialog properties
    	
//    	System.out.println("MENU TITLE 1: " + menu.getItem(1).getTitle());
//    	System.out.println("MENU TITLE 2: " + menu.getItem(2).getTitle());
//    	System.out.println("MENU TITLE 3: " + menu.getItem(3).getTitle());
    	
//    	restoreLevelDataButton = (Button) findViewById(R.id.restoreLevelData);
//    	
//    	if (menu.getItem(2).getTitle().equals("Worlds")) {
//    		restoreLevelDataButton.setVisibility(Button.VISIBLE);
//
//        	// Create the AlertDialog
//        	final AlertDialog dialog = builder.create();
//        	
//    		
//    		restoreLevelDataButton.setOnClickListener(new View.OnClickListener() {
//    		    public void onClick(View v) {
//    		    	dialog.show();
//    		    }
//    		});
//    	}
		return super.onPrepareOptionsMenu(menu);
	}
	

	public static class RestoreLevelDataTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
		private ProgressDialog dialog;
		private ArrayList<FilmLocation> filmLocationList;
		
		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				

	        	   
	        	   // CREATE REST TEMPLATE TO RETURN LEVEL JSON
						// TODO: create a function that only
						// places the restore button on the current
						// world available
						//
						// OR WE COULD LEAVE IT AS-IS SO THE USER
						// IS FREE TO REPLAY ANY LEVEL TO GAIN XP
						//
						// [THIS IS LEVEL GRINDING]
						// WE COULD ADD GRINDING ACHIEVEMENTS

						// Do your long operations here and return the result
//						String url = params[0];
						// resp = "async call in progress";

						// Set the Content-Type header
						HttpHeaders requestHeaders = new HttpHeaders();
						requestHeaders.setContentType(new MediaType("application", "json"));
						
						
						// GET LAST LEVEL CURRENTLY AVAILABLE
//						ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
						
						// TODO: REFACTOR THIS VALUE TO RESTORE ALL LEVELS
//						int lastLocationIndex = datasource.selectRecords().size() - 1;
//						FilmLocation lastLocation = tempLocationList.get(lastLocationIndex);
//						String lastLocationTitle = lastLocation.getTitle();
						

						
						// SEND WORLD TITLE
						// WE WON'T NEED THIS IF WE'RE USING A
						// REST TEMPLATE
//						localUser.setUserSid(lastLocationTitle);
						HttpEntity<User> requestEntity = new HttpEntity<User>(localUser, requestHeaders);
//						System.out.println("REST TEMPLATE PRE RESPONSE: " + quizItem.getSubmittedIndex());

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

						
						// CREATE A FUNCTION ON THE SERVER TO
						// TAKE REQUEST BASED ON WORLD TITLE
						//
						// RETURN ALL GCM RESPONSE JSON TO CLIENT
						// AND REBUILD LEVELS
						String url = params[0];
						ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
						User response = responseEntity.getBody();
						resp = response.getUserSid();
//						if (response.getUserSid().equals("success")) {
//							// RESET DATABASE RECORDS
////							ArrayList<BagItem> bagList = bagItemImpl.selectRecords();
////							ArrayList<BagItem> bagList = bagItemImpl.selectRecordsByTitle(title);
////							
////							for (BagItem item : bagList) {
////								if (item.getLevel().equals(currentUser.getWorldCount())) {
////									
////								}
////							}
////							ArrayList<QuizItem> quizList = quizitemsource.selectRecords();
//							
////							ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
////							int lastLocationIndex = datasource.selectRecords().size();
////							FilmLocation lastLocation = tempLocationList.get(lastLocationIndex);
////							String lastLocationTitle = lastLocation.getTitle();
////							System.out.println("ASYNC DATASOURCE: " + datasource);
////							System.out.println("ASYNC TITLE: " + lastLocation.getTitle());
//							
////							datasource.deleteRecordsByTitle(lastLocationTitle);
////							quizitemsource.deleteRecordsByTitle(lastLocationTitle);
////							bagItemImpl.deleteRecordsByLevel(response.getCurrentLevel());
////							conclusionCardImpl.deleteRecordsByLevel(response.getCurrentLevel());
//							
////							locationQuizArrayAdapter.notifyDataSetChanged();
////							locationQuizArrayAdapter.setListItemTitles(listItemTitles);
////							locationQuizArrayAdapter.setListItemImageTiles(listItemImageTiles);
////							locationQuizArrayAdapter.notifyDataSetChanged();
//							
//							// REDRAW LIST ADAPTER
////							redrawListAdapter();
//							
//							
//							
//					
//							
////							for (FilmLocation item : locationList) {
////								
////							}
//							
////							bagItemImpl.delete();
////							quizitemsource.delete();
////							datasource.delete();
//							
//							String worldCount = localUser.getWorldCount();
////							String worldCount = null;
////							if (localUser.getWorldCount().equals("0")) {
////								worldCount = "1";
////							} else {
////								worldCount = localUser.getWorldCount();
////							}
//
//							// THESE DELETIONS NEED TO HAPPEN ELSEWHERE 
////							quizitemsource.deleteRecordsByLevel(worldCount);
////							datasource.deleteRecordsByLevel(worldCount);
////							bagItemImpl.deleteRecordsByLevel(worldCount);
////							conclusionCardImpl.deleteRecordsByLevel(worldCount);
//							
//							System.out.println("RESPONSE WORLD COUNT: " + response.getWorldCount());
////							System.out.println("RESPONSE RECORDS COUNT: " + lastLocationIndex);
//						}

//						System.out.println("REST TEMPLATE POST RESPONSE: " + response.getAnswered());
						
						
						
						
						// check server for existing user, then update regId if that changed at all
						//
						// TODO: check if google plus id will change at all
						
						
//						// Set the username and password for creating a Basic Auth request
////						HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
//						HttpHeaders requestHeaders = new HttpHeaders();
//						requestHeaders.setContentType(new MediaType("application", "json"));
////						requestHeaders.setAuthorization(authHeader);
//						HttpEntity<User> requestEntity = new HttpEntity<User>(requestHeaders);
	//
//						// Create a new RestTemplate instance
//						RestTemplate restTemplate = new RestTemplate();
	//
//						// Add the String message converter
//						restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//						
//					    // Make the HTTP GET request to the Basic Auth protected URL
//					    ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
//						User response = responseEntity.getBody();
						System.out.println("NEW USER REGISTERED: " + response.getDisplayName());
						
						
						
						
						
					

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
				
				quizItemList = quizItemImpl.selectRecordsByTitle(result);
				// RESET QUIZ ITEMS LOCALLY
				// WE NEED TO GET THE WORLD TITLE
				
				for (QuizItem localQuizItem : quizItemList) {
					// update database record
					quizItemImpl.updateRecordAnswered(localQuizItem.getQuestionId(), "false");
					quizItemImpl.updateRecordCorrectAnswerIndex(localQuizItem.getQuestionId(), "null");
				}
			}
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//			mDrawerLayout.closeDrawers();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		// return super.onOptionsItemSelected(item);

		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	@Override
	public void onResume() {
		System.out.println("******* NEWS RESUME ********");
		super.onResume();
		
//		System.out.println("CONCLUSION CARD EMPTY:" + conclusionCardAdapter.isEmpty());
		if (conclusionCardAdapter != null) {
			cardList = conclusionCardImpl.selectRecords();
			System.out.println("CONCLUSION CARD COUNT:" + cardList.size());
			conclusionCardAdapter.notifyDataSetChanged();	
		}
		
		if (localUser != null) {
			localUser = userSource.selectRecordById(localUser.getUserId());
		}
		
		
		quizItemList = quizItemImpl.selectRecords();
		cardList = conclusionCardImpl.selectRecords();

		// BAG ITEM NEWS ITEM
		bagItemList = bagItemImpl.selectRecords();
		
//		Intent intent = getIntent();
//		finish();
//		startActivity(intent);
	}
	 
	private void selectItem(int position) {

		// TODO: write references to activities

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(navArray[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
//		
//		userDetailFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_user_detail);
//		newsFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_news);
//		
        ft = getSupportFragmentManager().beginTransaction();
        
        
//     // setup navigation drawer
//     		navArray = new String[5];
//     		navArray[0] = "Updates";
//     		navArray[1] = "Profile";
////     		navArray[2] = "Friends";
//     		navArray[2] = "Levels";
//     		navArray[3] = "Cards";
//     		navArray[4] = "About";
////     		navArray[6] = "Help";
//        ft.replace(R.id.container, userDetailFragment);
//        ft.addToBackStack(F_SETTINGS);
//        ft.commit();
        
//    	navArray[0] = "Updates";
//		navArray[1] = "Profile";
////		navArray[2] = "Friends";
//		navArray[2] = "Worlds";
//		navArray[3] = "Recovery";
//		navArray[4] = "Cards";
//		navArray[5] = "About";
////		navArray[6] = "Help";
        
//		// setup navigation drawer
//		navArray = new String[6];
//		navArray[0] = "Updates";
//		navArray[1] = "Profile";
////		navArray[2] = "Friends";
//		navArray[2] = "Worlds";
////		navArray[3] = "Restore";
//		navArray[3] = "Cards";
//		navArray[4] = "Items";
//		navArray[5] = "About";
////		navArray[6] = "Help";
		switch (position) {
		case 0:
			ft.replace(R.id.content_frame, new NewsFragment());
//			ft.hide(userDetailFragment);
//			ft.show(newsFragment);
			break;
		case 1:
			ft.replace(R.id.content_frame, new UserDetailFragment());
//			ft.replace(R.id.container, userDetailFragment);
//			ft.hide(newsFragment);
//			ft.show(userDetailFragment);
			break;
		case 2:
			ft.replace(R.id.content_frame, new FilmLocationsFragment());
//			ft.replace(R.id.content_frame, new FriendsFragment());
//			ft.replace(R.id.container, new FilmLocationsFragment());
//			ft.replace(R.id.container, userDetailFragment);
			break;
		case 3:
			ft.replace(R.id.content_frame, new ConclusionCardFragment());
//			ft.replace(R.id.content_frame, new FilmLocationsFragment());
//			Intent pagerActivityIntent = new Intent(this, FilmLocationPagerActivity.class);
//			pagerActivityIntent.putExtra("localUser", localUser);
//			startActivity(pagerActivityIntent);
//			ft.replace(R.id.container, userDetailFragment);
			break;
		case 4:
			ft.replace(R.id.content_frame, new BagItemListFragment());
//			ft.replace(R.id.container, userDetailFragment);
			break;
		case 5:
			ft.replace(R.id.content_frame, new AboutFragment());
//			ft.replace(R.id.container, userDetailFragment);
//			ft.hide(newsFragment);
//			ft.show(userDetailFragment);
			break;
		case 6:
			ft.replace(R.id.content_frame, new SettingsFragment());
//			ft.replace(R.id.container, userDetailFragment);
//			ft.hide(newsFragment);
//			ft.show(userDetailFragment);
			break;
		}
		ft.commit();
		setPos(position);

		System.out.println("nav item " + position + " clicked");
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static int getPos() {
		return pos;
	}

	public void setPos(int position) {
		pos = position;
	}


	public static class BagItemListFragment extends Fragment {
		
		private static ArrayList<NewsItem> newsUpdateList;
//		private LinearLayout instructionsLayout;
		protected boolean refreshed = false;

		public BagItemListFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_bag_item_list, container, false);
			
//			conclusionCard = getArguments().getParcelable("conclusionCard");
			
//			if (conclusionCard != null) {
//				conclusionCardTitleHeadline1
			
//					conclusionCardTitleHeadlineText.setVisibility(TextView.VISIBLE);
//					Intent intent = new Intent();
					
			
			
			
//					ArrayList<ConclusionCard> cardList = new ArrayList<ConclusionCard>();
//					cardList.add(conclusionCard);
					
					// SHOW NEW BAG ITEM(S)
//					conclusionCardAdapter = new ConclusionCardArrayAdapter(context, intent, cardList);
//					ListView cardListView = (ListView) rootView.findViewById(R.id.conclusionCardListView1);
//					cardListView.setVisibility(ListView.VISIBLE);
//					cardListView.setAdapter(conclusionCardAdapter);
//					conclusionCardAdapter.notifyDataSetChanged();
					


					final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreBagItemDataListView1);
					
					GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
//					String BAG_ITEM_TITLE = "BAG_ITEM_TITLE";
//					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(BAG_ITEM_TITLE);
					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecords();
					
					for (GameTitle tempTitle : gameTitleList) {
						System.out.println("CARD DATABASE TITLE: " + tempTitle.getTitle());
					}
					

					System.out.println("REFRESHED DATA");
					
					// THESE SERVICE CALLS NEED TO HAPPEN AS A PARCEL
					// THAT UPDATES ON ACTIVITY RETURN
					//
					// BAG ITEM NEWS ITEM
//					BagItemImpl localBagItemImpl = new BagItemImpl(context);
//					ArrayList<BagItem> localBagItemList = localBagItemImpl.selectRecords();
					
					// FLAG ITEMS AS MISSING AND PASS THROUGH ADAPTER
					// UPDATE IMAGE URL FOR EXISTING AND DISABLE CLICK
					// HANDLER
//					ConclusionCardImpl cardImpl = new ConclusionCardImpl(context);
//					ArrayList<ConclusionCard> tempCardList = conclusionCardImpl.selectRecords();
					
					

					// sort the list
					Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
					Collections.sort(bagItemList, StaticSortingUtilities.BAG_ITEMS_ALPHABETICAL_ORDER);
					
					// ITERATE THROUGH NODES AND UPDATE NON-MISSING STATES
					System.out.println("GAME TITLE LENGTH: " + gameTitleList.size());
					System.out.println("BAG ITEM LENGTH: " + bagItemList.size());
					
					if (gameTitleList.size() > 0) {
						if (bagItemList.size() > 0) {
							for (int i = 0; i < bagItemList.size(); i++) {
								GameTitle tempTitle = gameTitleList.get(i);
								String currentTitleString = tempTitle.getTitle();
//								QuizItem tempQuizItem = quizItemImpl.selectRecordById(tempTitle.getId());
								BagItem existingBagItem = bagItemList.get(i);
								String currentBagItemTitle = existingBagItem.getItemTitle();
//								if (!currentTitleString.equals(currentBagItemTitle)) {
//									// CARD IS MISSING
//									tempTitle.setPhase("MISSING");
//									// UPDATE UI FROM ADAPTER
//								} else {
//									// SET THE IMAGE
									tempTitle.setPhase("EXISTS");
									String tempImageUrl = existingBagItem.getImageUrl();
									tempTitle.setImageUrl(tempImageUrl);
									tempTitle.setLevel(existingBagItem.getLevel());
//								}
								gameTitleList.set(i, tempTitle);
							}	
						}
					}
//					if (gameTitleList.size() > 0) {
////						if (localBagItemList.size() > 0) {
////							
////						}
//						for (int i = 0; i < bagItemList.size(); i++) {
//							GameTitle tempTitle = gameTitleList.get(i);
//							String currentTitleString = tempTitle.getTitle();
//							BagItem existingBagItem = bagItemList.get(i);
//							String currentCardTitle = existingBagItem.getItemTitle();
//							if (!currentTitleString.equals(currentCardTitle)) {
//								// CARD IS MISSING
//								tempTitle.setPhase("MISSING");
//								// UPDATE UI FROM ADAPTER
//							} else {
//								// SET THE IMAGE
//								tempTitle.setPhase("EXISTS");
//								String tempImageUrl = existingBagItem.getImageUrl();
//								tempTitle.setImageUrl(tempImageUrl);
//								tempTitle.setLevel(existingBagItem.getLevel());
//							}
//							gameTitleList.set(i, tempTitle);
//						}	
//					}
					
					final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
					
					if (gameTitleList.size() > 0) {
						restoreListView.setAdapter(levelRestoreListAdapter);
						levelRestoreListAdapter.notifyDataSetChanged();
					}
					
					
//					for (GameTitle tempTitle : levelRestoreListAdapter) {
//						
//					}
//					for (int i = 0; i < newQuizList.size(); i++) {
//						if (newQuizList.get(i).equals(
//								currentQuizItem.getQuestionId())) {
//							newQuizList.set(i, currentQuizItem);
//							// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(i));
//							// locationQuizArrayAdapter.insert(currentQuizItem,
//							// i);
//						}
//					}
//
//					// create switch to show modal if there's no more current
//					// levels
//					// and it's time to replay the current world
//					boolean worldComplete = false;
//					for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
//						QuizItem item = locationQuizArrayAdapter.getItem(i);
//						if (currentQuizItem.getWorldId().equals(
//								item.getWorldId())) {
//							locationQuizArrayAdapter.remove(item);
//							locationQuizArrayAdapter.insert(currentQuizItem, i);
//						}
//						if (locationQuizArrayAdapter.getItem(i).getAnswered()
//								.equals("true")) {
//							worldComplete = true;
//						} else {
//							worldComplete = false;
//							break;
//						}
//					}
					
					
					
					

					
					final WorldTitleAsyncTaskRunner runner = new WorldTitleAsyncTaskRunner();
					runner.setContext(context);
					runner.setLocalUser(localUser);
				 
				swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
			    swipeLayout.setOnRefreshListener(
					    new OnRefreshListener(){

							@Override
							public void onRefresh() {
								
								
								if (refreshed == false) {
									runner.execute("http://movie-locations-app.appspot.com/secure/list/BAG_ITEM_TITLE");
									refreshed = true;
								}
								
								
								
								new Handler().postDelayed(new Runnable() {
							        @Override 
							        public void run() {
							            swipeLayout.setRefreshing(false);
							        }
							    }, 1000);
							}});
			    swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
			            android.R.color.holo_blue_light,
			            android.R.color.holo_blue_bright,
			            android.R.color.holo_blue_light);
			    
//			}
			
			return rootView;
		}
	}
	public static class ConclusionCardFragment extends Fragment {
		
		private static ArrayList<NewsItem> newsUpdateList;
//		private LinearLayout instructionsLayout;
		protected boolean refreshed = false;

		public ConclusionCardFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_conclusion_card, container, false);
			
//			conclusionCard = getArguments().getParcelable("conclusionCard");
			
//			if (conclusionCard != null) {
//				conclusionCardTitleHeadline1
			
//					conclusionCardTitleHeadlineText.setVisibility(TextView.VISIBLE);
//					Intent intent = new Intent();
					
			
			
			
//					ArrayList<ConclusionCard> cardList = new ArrayList<ConclusionCard>();
//					cardList.add(conclusionCard);
					
					// SHOW NEW BAG ITEM(S)
//					conclusionCardAdapter = new ConclusionCardArrayAdapter(context, intent, cardList);
//					ListView cardListView = (ListView) rootView.findViewById(R.id.conclusionCardListView1);
//					cardListView.setVisibility(ListView.VISIBLE);
//					cardListView.setAdapter(conclusionCardAdapter);
//					conclusionCardAdapter.notifyDataSetChanged();
					


					final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreConclusionCardDataListView1);
					
					GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
					String CARD_TITLE = "CARD_TITLE";
					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(CARD_TITLE);
					
					for (GameTitle tempTitle : gameTitleList) {
						System.out.println("CARD DATABASE TITLE: " + tempTitle.getTitle());
					}
					

					System.out.println("REFRESHED DATA");
					
					
					
					// FLAG ITEMS AS MISSING AND PASS THROUGH ADAPTER
					// UPDATE IMAGE URL FOR EXISTING AND DISABLE CLICK
					// HANDLER
//					ConclusionCardImpl cardImpl = new ConclusionCardImpl(context);
//					ArrayList<ConclusionCard> tempCardList = conclusionCardImpl.selectRecords();
					
					

					// sort the list
					Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
					Collections.sort(cardList, StaticSortingUtilities.CARD_TITLES_ALPHABETICAL_ORDER);
					
					// ITERATE THROUGH NODES AND UPDATE NON-MISSING STATES
					
					// ADD CHECK FOR CURRENT LEVEL AGAINST GAME ITEM
					
					
					
					if (gameTitleList.size() > 0) {
						if (cardList.size() > 0) {
							for (int i = 0; i < cardList.size(); i++) {
								GameTitle tempTitle = gameTitleList.get(i);
								String currentTitleString = tempTitle.getTitle();
								QuizItem tempQuizItem = quizItemImpl.selectRecordById(tempTitle.getId());
								ConclusionCard existingCard = cardList.get(i);
								String currentCardTitle = existingCard.getTitle();
								if (!currentTitleString.equals(currentCardTitle)) {
									// CARD IS MISSING
									tempTitle.setPhase("MISSING");
									// UPDATE UI FROM ADAPTER
								} else if (tempQuizItem != null && tempQuizItem.getAnswered().equals("true")) {
									// SET THE IMAGE
									tempTitle.setPhase("EXISTS");
									String tempImageUrl = existingCard.getImageUrl();
									tempTitle.setImageUrl(tempImageUrl);
									tempTitle.setLevel(existingCard.getLevel());
								}
								gameTitleList.set(i, tempTitle);
							}	
						}
					}
					
					final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
					
					if (gameTitleList.size() > 0) {
						restoreListView.setAdapter(levelRestoreListAdapter);
						levelRestoreListAdapter.notifyDataSetChanged();
					}
					
					
//					for (GameTitle tempTitle : levelRestoreListAdapter) {
//						
//					}
//					for (int i = 0; i < newQuizList.size(); i++) {
//						if (newQuizList.get(i).equals(
//								currentQuizItem.getQuestionId())) {
//							newQuizList.set(i, currentQuizItem);
//							// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(i));
//							// locationQuizArrayAdapter.insert(currentQuizItem,
//							// i);
//						}
//					}
//
//					// create switch to show modal if there's no more current
//					// levels
//					// and it's time to replay the current world
//					boolean worldComplete = false;
//					for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
//						QuizItem item = locationQuizArrayAdapter.getItem(i);
//						if (currentQuizItem.getWorldId().equals(
//								item.getWorldId())) {
//							locationQuizArrayAdapter.remove(item);
//							locationQuizArrayAdapter.insert(currentQuizItem, i);
//						}
//						if (locationQuizArrayAdapter.getItem(i).getAnswered()
//								.equals("true")) {
//							worldComplete = true;
//						} else {
//							worldComplete = false;
//							break;
//						}
//					}
					
					
					
					

					
					final WorldTitleAsyncTaskRunner runner = new WorldTitleAsyncTaskRunner();
					runner.setContext(context);
					runner.setLocalUser(localUser);
				 
				swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
			    swipeLayout.setOnRefreshListener(
					    new OnRefreshListener(){

							@Override
							public void onRefresh() {
								
								cardList = conclusionCardImpl.selectRecords();
								System.out.println("CARD LIST LENGTH: " + cardList.size());
								System.out.println("GAME TITLE LIST LENGTH: " + gameTitleList.size());
								if (refreshed == false) {
//									runner.execute("http://movie-locations-app.appspot.com/secure/list/CARD_TITLE");
									
									refreshed = true;
								}
								
								
								
								new Handler().postDelayed(new Runnable() {
							        @Override 
							        public void run() {
							            swipeLayout.setRefreshing(false);
							        }
							    }, 1000);
							}});
			    swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
			            android.R.color.holo_blue_light,
			            android.R.color.holo_blue_bright,
			            android.R.color.holo_blue_light);
			    
//			}
			
			return rootView;
		}
	}
		
	// /**
	// * A placeholder fragment containing a simple view.
	// */
	public static class NewsFragment extends Fragment {
		
		private static ArrayList<NewsItem> newsUpdateList;
//		private LinearLayout instructionsLayout;
		private String QUIZ_ITEM_NEWS_ITEM_IMAGE_URL = "http://www.phonandroid.com/forum/download/file.php?avatar=218224_1342902645.jpg";

		public NewsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_news, container, false);
			


			final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreNewsItemDataListView1);
			
			GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
			String NEWS_ITEM_TITLE = "NEWS_ITEM_TITLE";
			final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(NEWS_ITEM_TITLE);
			
			for (GameTitle tempTitle : gameTitleList) {
				System.out.println("CARD DATABASE TITLE: " + tempTitle.getTitle());
			}
			

			System.out.println("REFRESHED DATA");
			
			// THESE SERVICE CALLS NEED TO HAPPEN AS A PARCEL
			// THAT UPDATES ON ACTIVITY RETURN
			//
			// BAG ITEM NEWS ITEM
//			BagItemImpl localBagItemImpl = new BagItemImpl(context);
//			ArrayList<BagItem> localBagItemList = localBagItemImpl.selectRecords();
			
			// FLAG ITEMS AS MISSING AND PASS THROUGH ADAPTER
			// UPDATE IMAGE URL FOR EXISTING AND DISABLE CLICK
			// HANDLER
			NewsItemImpl newsItemImpl = new NewsItemImpl(context);
			ArrayList<NewsItem> tempNewsItemList = newsItemImpl.selectRecords();
			
			

			// sort the list
			Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
			Collections.sort(tempNewsItemList, StaticSortingUtilities.NEWS_ITEM_TITLES_ALPHABETICAL_ORDER);
			
			// ITERATE THROUGH NODES AND UPDATE NON-MISSING STATES
			
			if (gameTitleList.size() > 0) {
//				if (localBagItemList.size() > 0) {
//					
//				}
				
				// ITERATE THROUGH COLLECTION AND GATHER
				// ALL TITLES AND HIDE EXISTING TITLE RESTORE
				for (int i = 0; i < tempNewsItemList.size(); i++) {
					GameTitle tempTitle = gameTitleList.get(i);
					String currentTitleString = tempTitle.getTitle();
					NewsItem existingBagItem = tempNewsItemList.get(i);
					String currentCardTitle = existingBagItem.getTitle();
					if (!currentTitleString.equals(currentCardTitle)) {
//						System.out.println("LOGGING CURRENT TITLE: " + currentTitleString + " " + i);
//						System.out.println("LOGGING NEWS ITEM TITLE: " + currentCardTitle + " " + i);
						
						System.out.println("LOGGING CURRENT TITLE TYPE: " + tempTitle.getType() + " " + i);
						System.out.println("LOGGING NEWS ITEM TITLE TYPE: " + existingBagItem.getNewsType() + " " + i);
						// CARD IS MISSING
						tempTitle.setPhase("MISSING");
						// UPDATE UI FROM ADAPTER
					} else {
						// SET THE IMAGE
						tempTitle.setPhase("EXISTS");
						String tempImageUrl = existingBagItem.getImageUrl();
						tempTitle.setImageUrl(tempImageUrl);
						tempTitle.setLevel("1");
					}
					gameTitleList.set(i, tempTitle);
				}	
			}
			
			final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
			
			if (gameTitleList.size() > 0) {
				restoreListView.setAdapter(levelRestoreListAdapter);
//				levelRestoreListAdapter.notifyDataSetChanged();
			}
			
			
//			for (GameTitle tempTitle : levelRestoreListAdapter) {
//				
//			}
//			for (int i = 0; i < newQuizList.size(); i++) {
//				if (newQuizList.get(i).equals(
//						currentQuizItem.getQuestionId())) {
//					newQuizList.set(i, currentQuizItem);
//					// locationQuizArrayAdapter.remove(locationQuizArrayAdapter.getItem(i));
//					// locationQuizArrayAdapter.insert(currentQuizItem,
//					// i);
//				}
//			}
//
//			// create switch to show modal if there's no more current
//			// levels
//			// and it's time to replay the current world
//			boolean worldComplete = false;
//			for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
//				QuizItem item = locationQuizArrayAdapter.getItem(i);
//				if (currentQuizItem.getWorldId().equals(
//						item.getWorldId())) {
//					locationQuizArrayAdapter.remove(item);
//					locationQuizArrayAdapter.insert(currentQuizItem, i);
//				}
//				if (locationQuizArrayAdapter.getItem(i).getAnswered()
//						.equals("true")) {
//					worldComplete = true;
//				} else {
//					worldComplete = false;
//					break;
//				}
//			}
			
			
			
			

			
			final WorldTitleAsyncTaskRunner runner = new WorldTitleAsyncTaskRunner();
			runner.setContext(context);
			runner.setLocalUser(localUser);
		 
		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(
			    new OnRefreshListener(){

					@Override
					public void onRefresh() {
						
						
						boolean refreshed = false;
						if (refreshed == false) {
//							runner.execute("http://movie-locations-app.appspot.com/secure/list/NEWS_ITEM_TITLE");
							cardList = conclusionCardImpl.selectRecords();
							System.out.println("CARD LIST LENGTH: " + cardList.size());
							refreshed = true;
						}
						
						
						
						new Handler().postDelayed(new Runnable() {
					        @Override 
					        public void run() {
					            swipeLayout.setRefreshing(false);
					        }
					    }, 1000);
					}});
	    swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_blue_light,
	            android.R.color.holo_blue_bright,
	            android.R.color.holo_blue_light);
	    
	    
//	}
	    
			
//			instructionsLayout = (LinearLayout) rootView.findViewById(R.id.new_user_instructions);
//			
//			if (localUser.getCurrentLevel() == "0") {
//				// check for first level and give instructions
//				
//				Button instructionsButton = (Button) rootView.findViewById(R.id.dismiss_instructions);
//				
//				instructionsLayout.setVisibility(LinearLayout.VISIBLE);
//				
//				// bind check-in button click event
//				instructionsButton.setOnClickListener(new Button.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						instructionsLayout.setVisibility(LinearLayout.GONE);
//					}
//				});
//			}
			
			
			// TODO: CREATE NEWS GENERATOR CLASS THAT
			// QUERIES BY ARRAY LIST MAX VALUES FOR
			// EACH CATEGORY
			//
			// CATEGORIES:
			// ------------
			ListView commentView = (ListView) rootView.findViewById(R.id.commentView);
			
			
			newsUpdateList = new ArrayList<NewsItem>();
			
			NewsItemAdapter newsItemAdapter = new NewsItemAdapter();
			
			// SET COLLECTIONS
			// COMPOSE HERE AND REFACTOR LATER
			
//			// BAG ITEM NEWS ITEM
//			BagItemImpl localBagItemImpl = new BagItemImpl(context);
//			ArrayList<BagItem> localBagItemList = localBagItemImpl.selectRecords();
			BagItem latestBagItem = null;
			
			if (bagItemList.size() > 0) {
				int latestBagItemIndex = bagItemList.size() - 1;
				latestBagItem = bagItemList.get(latestBagItemIndex);
				newsItemAdapter.setLatestBagItem(latestBagItem);	
			}
			
			
			// CONCLUSION CARD NEWS ITEM
			
			ArrayList<ConclusionCard> localConclusionCardList = conclusionCardImpl.selectRecords();
			ConclusionCard latestConclusionCard = null;
			
			if (localConclusionCardList.size() > 0) {
				int latestConclusionCardIndex = localConclusionCardList.size() - 1;
				latestConclusionCard = localConclusionCardList.get(latestConclusionCardIndex);
				newsItemAdapter.setLatestConclusionCard(latestConclusionCard);	
			}
			
			// QUIZ ITEM NEWS ITEM
			quizItemImpl = new QuizItemImpl(context);
			quizItemList = quizItemImpl.selectRecords();
			int latestQuizItemIndex = quizItemList.size() - 1;
//			QuizItem latestQuizItem = quizItemList.get(latestQuizItemIndex);
//			newsItemAdapter.setLatestQuizItem(latestQuizItem);
			
			
			// CREATE NEWS ITEM ENTITIES FROM ADAPTER DATA
			//
			// BAG ITEM NEWS ITEM
			NewsItem bagItemNewsItem = new NewsItem();
			bagItemNewsItem.setNewsType("BagItem");
			
			if (latestBagItem != null) {
				String bagItemNewsItemTitle = latestBagItem.getItemTitle();
				String bagItemNewsItemText = latestBagItem.getDescription();
				String bagItemNewsItemImageUrl = latestBagItem.getImageUrl();
				bagItemNewsItem.setTitle(bagItemNewsItemTitle);
				bagItemNewsItem.setText(bagItemNewsItemText);
				bagItemNewsItem.setImageUrl(bagItemNewsItemImageUrl);	
			}
			
			
			// CONCLUSION CARD NEWS ITEM
			NewsItem conclusionCardNewsItem = new NewsItem();
			conclusionCardNewsItem.setNewsType("ConclusionCard");
			
			if (latestConclusionCard != null) {
				String conclusionCardNewsItemTitle = latestConclusionCard.getTitle();
				String conclusionCardNewsItemText = latestConclusionCard.getCopy();
				String conclusionCardImageUrl = latestConclusionCard.getImageUrl();
				conclusionCardNewsItem.setTitle(conclusionCardNewsItemTitle);
				conclusionCardNewsItem.setText(conclusionCardNewsItemText);
				conclusionCardNewsItem.setImageUrl(conclusionCardImageUrl);
			}
			
			
			// QUIZ ITEM NEWS ITEM
			NewsItem quizItemNewsItem = new NewsItem();
			quizItemNewsItem.setNewsType("QuizItem");
			// ITERATE THROUGH BAG ITEMS AND
			// GET THE CORRECT ANSWER INDEX OFF THE
			// LATEST CORRECTLY ANSWERED QUIZ ITEM
			
			ArrayList<QuizItem> correctlyAnsweredQuizItems = new ArrayList<QuizItem>(); 
			for (QuizItem item : quizItemList) {
				if (item.getAnswered().equals("true")) {
					correctlyAnsweredQuizItems.add(item);
				}
			}
			
//			QuizItem latestQuizItem = quizItemList.get(latestQuizItemIndex);
			
			
			int correctlyAnsweredQuizItemIndex = correctlyAnsweredQuizItems.size() - 1;
			
			QuizItem lastCorrectlyAnsweredQuizItem = null;
			
			String quizItemNewsItemTitle = null;
			NewsItem locationNewsItem = null;
			
			if (correctlyAnsweredQuizItems.size() > 0) {
				lastCorrectlyAnsweredQuizItem = correctlyAnsweredQuizItems.get(correctlyAnsweredQuizItemIndex);
				
				quizItemNewsItemTitle = lastCorrectlyAnsweredQuizItem.getQuestionText();
				
				
				int correctAnswerIndex = Integer.parseInt(lastCorrectlyAnsweredQuizItem.getCorrectAnswerIndex());
				String[] answers = new String[4];
				answers[0] = lastCorrectlyAnsweredQuizItem.getAnswer1();
				answers[1] = lastCorrectlyAnsweredQuizItem.getAnswer2();
				answers[2] = lastCorrectlyAnsweredQuizItem.getAnswer3();
				answers[3] = lastCorrectlyAnsweredQuizItem.getAnswer4();
				String finalCorrectAnswer = answers[correctAnswerIndex];
				String quizItemNewsItemText = finalCorrectAnswer;
				String latestActiveItemTitle = lastCorrectlyAnsweredQuizItem.getActiveItem();
				String quizItemNewsItemImageUrl = QUIZ_ITEM_NEWS_ITEM_IMAGE_URL;
				
				
				if (lastCorrectlyAnsweredQuizItem != null) {
					newsItemAdapter.setLatestQuizItem(lastCorrectlyAnsweredQuizItem);
					
					
					
					
					

					
					// LOCATION NEWS ITEM
					MovieLocationsImpl locationsImpl = new MovieLocationsImpl(context);
//					ArrayList<FilmLocation> locationList = locationsImpl.selectRecords();
//					int latestLocationIndex = locationList.size() - 1;
					String latestLocationId = lastCorrectlyAnsweredQuizItem.getWorldId();
					FilmLocation latestLocation = locationsImpl.selectRecordById(latestLocationId);
					
					if (latestLocation != null) {
						newsItemAdapter.setLatestLocation(latestLocation);	
					}
					
					
					// LOCATION NEWS ITEM
					locationNewsItem = new NewsItem();
					locationNewsItem.setNewsType("Location");
					
					if (latestLocation != null) {
						String locationNewsItemTitle = latestLocation.getTitle();
						String latestLocationText = latestLocation.getLocations();
						String latestLocationImageUrl = latestLocation.getStaticMapImageUrl();
						locationNewsItem.setTitle(locationNewsItemTitle);
						locationNewsItem.setText(latestLocationText);
						locationNewsItem.setImageUrl(latestLocationImageUrl);	
					}
					
					
					
					
					
					// ITERATE THROUGH BAG ITEMS AND GET URL
					// FOR THE LATEST QUIZ ITEM SOLVE
					for (BagItem item : bagItemList) {
						if (item.getItemTitle().equals(latestActiveItemTitle)) {
							quizItemNewsItemImageUrl = item.getImageUrl();
						}
					}
					
					if (quizItemNewsItem != null) {
						quizItemNewsItem.setTitle(quizItemNewsItemTitle);
						quizItemNewsItem.setText(quizItemNewsItemText);
						quizItemNewsItem.setImageUrl(quizItemNewsItemImageUrl);	
					}
				}
			}
			
			
			// ADD COMPOSED QUIZ ITEMS TO THE NEWS UPDATE LIST
			
			if (conclusionCardNewsItem != null) {
				newsUpdateList.add(conclusionCardNewsItem);	
			}
			
			if (bagItemNewsItem != null) {
				newsUpdateList.add(bagItemNewsItem);	
			}
			
			if (quizItemNewsItem != null) {
				newsUpdateList.add(quizItemNewsItem);	
			}
			
			if (locationNewsItem != null) {
				newsUpdateList.add(locationNewsItem);	
			}
			
			
			
			
//			String latestQuizItemText = latestQuizItem.
			
			
//			for (int i = 0; i < 10; i++) {
//				newsUpdateList.add(update);	
//			}
			
			NewsArrayAdapter adapter = new NewsArrayAdapter(getActivity(), intent, newsUpdateList);
			
			if (newsUpdateList.size() > 0) {
				commentView.setAdapter(adapter);
			} 
			

			
//			UploadFilmLocationsTaskRunner runner = new UploadFilmLocationsTaskRunner();
//			runner.execute("http://movie-locations-app.appspot.com/submitLocObj");
			
			
			
			return rootView;
		}
		

		public class UploadFilmLocationsTaskRunner extends AsyncTask<String, String, String> {

			private String resp;
			private ProgressDialog dialog;
			private MovieLocationsImpl datasource;
			private ArrayList<FilmLocation> filmLocationList;
			
			@Override
			protected String doInBackground(String... params) {
				publishProgress("Sleeping..."); // Calls onProgressUpdate()
				try {
					
					String dataUrl = params[0];
					
					datasource = new MovieLocationsImpl(context);
					
//					service = new FilmLocationService();
//					filmList = service.getMovieData().buildLocationObjects().returnFilmList();
					
					filmLocationList = datasource.selectRecords();
							
//					for (FilmLocation item : filmLocationList) {
//						System.out.println("UPLOAD FILM LOCATIONS: " + item.getTitle());
//					}
					
					FilmArrayList filmArrayList = new FilmArrayList();
					filmArrayList.setFilmList(datasource.selectRecords());
					
//					FilmLocation firstLocation = filmLocationList.get(0);
					// Do your long operations here and return the result
					
					
				
					
					
					
					
					// Create and populate a simple object to be used in the request

					// Set the Content-Type header
					HttpHeaders requestHeaders = new HttpHeaders();
					requestHeaders.setContentType(new MediaType("application","json"));
					HttpEntity<FilmArrayList> requestEntity = new HttpEntity<FilmArrayList>(filmArrayList, requestHeaders);

					// Create a new RestTemplate instance
					RestTemplate restTemplate = new RestTemplate();

					// Add the Jackson and String message converters
					restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
					restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

					// Make the HTTP POST request, marshaling the request to JSON, and the response to a String
					ResponseEntity<FilmArrayList> responseEntity = restTemplate.exchange(dataUrl, HttpMethod.POST, requestEntity, FilmArrayList.class);
					FilmArrayList filmLocationResponse = responseEntity.getBody();
					ArrayList<FilmLocation> responseLocationList = filmLocationResponse.getFilmList();
					FilmLocation responseLocation = responseLocationList.get(0);
					
//					resp = filmLocationResponse.getTitle();
					
					System.out.println("REST TEMPLATE POST RESPONSE: " + responseLocation.getTitle());
					
					
					 
	//
//					// Set the Content-Type header
//					HttpHeaders requestHeaders = new HttpHeaders();
//					requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//					HttpEntity<FilmArrayList> requestEntity = new HttpEntity<FilmArrayList>(filmArrayList, requestHeaders);
////					System.out.println("REST TEMPLATE PRE RESPONSE: " + item.getTitle());
	//
//					// Create a new RestTemplate instance
//					RestTemplate restTemplate = new RestTemplate();
	//
//					// Add the Jackson and String message converters
//					restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//					restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

					// Make the HTTP POST request, marshaling the request to
					// JSON, and the response to a String
					// ResponseEntity<String> responseEntity =
					// restTemplate.exchange(url, HttpMethod.POST,
					// requestEntity, String.class);
					// String response = responseEntity.getBody();
					
					// String result = rest.postForObject(url, map, String.class);
//					ResponseEntity<String> responseEntity = restTemplate.exchange(dataUrl, HttpMethod.POST, requestEntity, String.class);
//					String response = restTemplate.postForObject(dataUrl, requestEntity, String.class);
					
//					postForObject(String url, Object request, Class<T> responseType, Object... uriVariables);
//					ResponseEntity<String> response = restTemplate.exchange(dataUrl, HttpMethod.POST, requestEntity, String.class);
//					String responseString = restTemplate.exchange(dataUrl, requestEntity, String.class, filmArrayList);
//					restTemplate.exchange(url, method, requestEntity, responseType)
//					String responseString = response.getBody();
					
//					@SuppressWarnings("unchecked")
//					ArrayList<FilmLocation> list = responseEntity.getBody();

//					System.out.println("REST TEMPLATE POST RESPONSE: " + responseString);
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
				dialog.setTitle("Initializing...");
				dialog.setMessage("Downloading level data.");
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
	
//	// user detail fragment
//
//
//	/**
//	 * A placeholder fragment containing a simple view.
//	 * check for first level and give instructions
//	 */
//	public class HelpFragment extends Fragment {
////		private LinearLayout instructionsLayout;
//		public HelpFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_help,
//					container, false);
//			
//			
//
////			instructionsLayout = (LinearLayout) rootView.findViewById(R.id.new_user_instructions);
//			
//			
//			
//			Button instructionsButton = (Button) rootView.findViewById(R.id.dismiss_instructions);
//			
//			// bind check-in button click event
//			instructionsButton.setOnClickListener(new Button.OnClickListener() {
//				@Override
//				public void onClick(View view) {
////					instructionsLayout.setVisibility(LinearLayout.GONE);
//					
//					// close activity
////					getActivity().finish();
//					
//					// change fragments
//					selectItem(0);
//				}
//			});
//				
//				
////			}
//
//			return rootView;
//		}
//	}
	
	// user detail fragment


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class UserDetailFragment extends Fragment {

		public UserDetailFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_user_detail,
					container, false);

			ImageView userAvatar = (ImageView) rootView
					.findViewById(R.id.userAvatar1);
			imageLoader.displayImage(localUser.getAvatarImageUrl(), userAvatar);

			// TextView userProfileTitleTag = (TextView)
			// findViewById(R.id.userProfileTitleTag1);
			// TextView userTextTag = (TextView)
			// findViewById(R.id.userFullNameTag1);
			TextView userText = (TextView) rootView
					.findViewById(R.id.userFullName1);

			// TextView achievementsTitleTag = (TextView)
			// findViewById(R.id.achievementsTitleTag1);
			// TextView achievementsTag = (TextView)
			// findViewById(R.id.achievementsTag1);
			TextView currentLevelText = (TextView) rootView.findViewById(R.id.levelText1);

			// TextView miscTitleTag = (TextView)
			// findViewById(R.id.miscTitleTag1);
			// TextView miscTag = (TextView) findViewById(R.id.miscTag1);
			TextView pointsText = (TextView) rootView.findViewById(R.id.pointsText1);

			// set text values
			System.out.println("localUser.getDisplayName(): " + localUser.getDisplayName());
			System.out.println("userText: " + userText);
			userText.setText(localUser.getDisplayName());
			
			currentLevelText.setText(localUser.getCurrentLevel());
			
			String currentPoints = localUser.getPoints();
			String currentBonusPoints = localUser.getBonusPoints();
			
			
			
			
			
			int[] levelRange = StaticSortingUtilities.getLevelRange();
			int nextLevelIndex = Integer.parseInt(localUser.getCurrentLevel()) + 1;
			final int finalLevelCap = levelRange[nextLevelIndex];
			
			int currentTotalPoints = Integer.parseInt(currentPoints) + Integer.parseInt(currentBonusPoints);
			
			String currentPointsString = Integer.toString(currentTotalPoints) + "/" + Integer.toString(finalLevelCap);
			pointsText.setText(currentPointsString);
			
//			localUser.get

//			Button getStartedButton = (Button) rootView
//					.findViewById(R.id.get_started_button);
//			
//			getStartedButton.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					// start new intent
//					Intent pagerActivityIntent = new Intent(getActivity(),
//							FilmLocationPagerActivity.class);
//					pagerActivityIntent.putExtra("localUser", localUser);
//					startActivity(pagerActivityIntent);
//				}
//			});

			return rootView;
		}
	}
	


	// /**
	// * A placeholder fragment containing a simple view.
	// */
	public static class FriendsFragment extends Fragment {

		private ArrayList<Friend> friendsList;

		public FriendsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
			ListView commentView = (ListView) rootView.findViewById(R.id.commentView);
			
			friendsList = new ArrayList<Friend>();
			Friend friend = new Friend();
			
//			update.setTitle("title");
//			update.setText("update text");
//			update.setUserId("user id");
//			update.setDateTime("datetime");
//			update.setTitle("achievement id");
			
			for (int i = 0; i < 10; i++) {
				friend.setDisplayName("friend " + i);
				friend.setUserId("friend_" + i);
				friend.setAvatarImage("avatar image url");
				friendsList.add(friend);	
			}
			
			FriendsArrayAdapter adapter = new FriendsArrayAdapter(
					getActivity(), intent, friendsList);
			
			if (friendsList.size() >= 0) {
				commentView.setAdapter(adapter);
			} 
			
			return rootView;
		}
	}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class FilmLocationsFragment extends Fragment {

		protected boolean refreshed = false;

		public FilmLocationsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_film_locations,
					container, false);
			
			// TODO: BUILD ARRAY ADAPTER TO PLACE WORLD
			// RESTORE BY TITLE BUTTONS

//			ImageView userAvatar = (ImageView) rootView
//					.findViewById(R.id.userAvatar1);
//			imageLoader.displayImage(localUser.getAvatarImageUrl(), userAvatar);
			
			TextView locationsTitle = (TextView) rootView.findViewById(R.id.locationsTag1);
//			TextView locationsText = (TextView) rootView.findViewById(R.id.locationsText);

			locationsTitle.setText("Film Locations");
//			locationsText.setText("Get started!");
			
			Button getStartedButton = (Button) rootView.findViewById(R.id.get_started_button);
			
			 
			
			// WE NEED TO USE A SERVICE OR LISTENER TO GET THIS DATA UPDATED
			//
			// IMPLEMENT LISTENER HERE AND GET PAYLOAD
			final MovieLocationsImpl datasource = new MovieLocationsImpl(context);
			final ArrayList<FilmLocation> defaultLocationList = datasource.selectRecords();
			System.out.println("DEFAULT LOCATION LIST :" +  defaultLocationList);
			final FilmArrayList defaultLocationArrayList = new FilmArrayList();
			defaultLocationArrayList.setFilmList(defaultLocationList);
//			
//			setUpdatedNewsData(defaultLocationArrayList);
//			System.out.println("LOCATION ARRAY LIST PARCELABLE: " + locationArrayList.getFilmList().get(0).getTitle());
			
			// TURN OFF BUTTON VISIBILITY
			if (defaultLocationList.size() == 0) {
				getStartedButton.setVisibility(Button.GONE);
				
				// LAUNCH DIALOG
				


				
		    	String CONFIRM_MESSAGE = "Okay";
		    	String DIALOG_TITLE = "Your level queue is empty.";
		    	String DIALOG_MESSAGE = "Pull down menu to refresh list. Confirm and restore world data.";
				
				
				
		    	// CREATE CONFIRMATION DIALOG
		    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    	
		    	builder.setMessage(DIALOG_MESSAGE).setTitle(DIALOG_TITLE);
		    	
		    	// TODO: ITERATE AND DELETE CURRENT LEVEL DATA
		    	// - BAG ITEMS
		    	// - QUIZ ITEMS
		    	// - LOCATIONS
		    	
		    	
		    	

		    	

		    	
		    	
		    	
				// Add the buttons
		    	builder.setPositiveButton(CONFIRM_MESSAGE, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
//		        	   getActivity().finish();
		        	   dialog.dismiss();
		           }
		       });
		    	
		    	final AlertDialog emptyLevelQueueDialog = builder.create();
		    	emptyLevelQueueDialog.show();
			} else {
				getStartedButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						// start new intent
						Intent pagerActivityIntent = new Intent(getActivity(), FilmLocationPagerActivity.class);
						
//						FilmArrayList finalList = getUpdatedNewsData();
						System.out.println("LIST LENGTH: " + defaultLocationArrayList.getFilmList().size());
						pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
						pagerActivityIntent.putExtra("localUser", localUser);
						System.out.println("LOCAL USER POINTS PARCEL: " + localUser.getCurrentPoints());
						startActivity(pagerActivityIntent);
//						System.out.println("clicked button");
					}
				});	
			}
			



			final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreLevelDataListView);
			
			GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
			String WORLD_TITLE = "WORLD_TITLE";
			final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(WORLD_TITLE);
			
			
			
//			String worldStatus = "EXISTS";
//			int counter = 0;
//			if (gameTitleList.size() > 0) {
//				for (GameTitle title : gameTitleList) {
//					String tempTitle = title.getTitle();
//					tempTitle = tempTitle.replaceAll(" ", "");
//					FilmLocation tempLocation = datasource.selectRecordById(tempTitle);
//					if (tempLocation == null) {
//						// SET UI TO MISSING LOCATION
//						worldStatus = "MISSING";
//						title.setPhase(worldStatus);
//						gameTitleList.set(counter, title);	
//					}
//					counter++;
//				}	
//			}
//			
			

			
			
			
//			final ListView restoreListView = (ListView) getView().findViewById(R.id.restoreLevelDataListView);
			
			System.out.println("REFRESHED DATA");
			final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
			if (gameTitleList.size() > 0) {
				restoreListView.setAdapter(levelRestoreListAdapter);
//				levelRestoreListAdapter.notifyDataSetChanged();
			}
			


//			@Override 
//			public void onRefresh() {
//			    new Handler().postDelayed(new Runnable() {
//			        @Override public void run() {
//			            swipeLayout.setRefreshing(false);
//			        }
//			    }, 5000);
//			}
			
			
			
			
			
			
			
			

			
			final WorldTitleAsyncTaskRunner runner = new WorldTitleAsyncTaskRunner();
			runner.setContext(context);
			runner.setLocalUser(localUser);
		 
		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(
			    new OnRefreshListener(){

					@Override
					public void onRefresh() {
						
						
						if (refreshed == false) {
							runner.execute("http://movie-locations-app.appspot.com/secure/list/WORLD_TITLE");
							refreshed = true;
						}
						
						
						
						new Handler().postDelayed(new Runnable() {
					        @Override 
					        public void run() {
					            swipeLayout.setRefreshing(false);
					        }
					    }, 1000);
					}});
	    swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_blue_light,
	            android.R.color.holo_blue_bright,
	            android.R.color.holo_blue_light);
	    
	    
	    
	    
	    
	    
			return rootView;
		}

//		public class WorldTitleAsyncTaskRunner extends AsyncTask<String, String, String> {
//
//			private String resp;
//			private JsonNode json;
//			private ProgressDialog dialog;
//			private boolean initialized = false;
//
//			@Override
//			protected String doInBackground(String... params) {
//				publishProgress("Sleeping..."); // Calls onProgressUpdate()
//				try {
//					// Do your long operations here and return the result
//					String url = params[0];
//					// resp = "async call in progress";
//					// Set the Content-Type header
//					HttpHeaders requestHeaders = new HttpHeaders();
//					requestHeaders.setContentType(new MediaType("application", "json"));
//					HttpEntity<User> requestEntity = new HttpEntity<User>(localUser, requestHeaders);
////					System.out.println("REST TEMPLATE PRE RESPONSE: " + quizItem.getAnswered());
//
//					// Create a new RestTemplate instance
//					RestTemplate restTemplate = new RestTemplate();
//
//					// Add the Jackson and String message converters
//					restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//					restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//
//					// Make the HTTP POST request, marshaling the request to
//					// JSON, and the response to a String
//					// ResponseEntity<String> responseEntity =
//					// restTemplate.exchange(url, HttpMethod.POST,
//					// requestEntity, String.class);
//					// String response = responseEntity.getBody();
//
//					ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
//					User localUser = responseEntity.getBody();
//					
////					if (quizItem.getCorrectAnswerIndex() != null) {
////						// store correct answer index reference to update interface
////						currentAnswerIndex = quizItem.getCorrectAnswerIndex();
////					}
//
//					// setCurrentQuestion(response);
//
//					
//
//					// send to callback
//					resp = localUser.getDisplayName();
//					System.out.println("REST TEMPLATE POST RESPONSE DISPLAY NAME FROM TITLE API: " + resp);
//
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
//				// finalResult.setText(result);
//				// set json data here to serialize
//
////				updateUserInterface(result);
//
//				if (dialog != null) {
//					dialog.dismiss();
//				}
//				
//
//
////				final ListView restoreListView = (ListView) getView().findViewById(R.id.restoreLevelDataListView);
////				
////				GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
////				String WORLD_TITLE = "WORLD_TITLE";
////				final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(WORLD_TITLE);
////				
////				
////				
//////				String worldStatus = "EXISTS";
//////				int counter = 0;
//////				if (gameTitleList.size() > 0) {
//////					for (GameTitle title : gameTitleList) {
//////						String tempTitle = title.getTitle();
//////						tempTitle = tempTitle.replaceAll(" ", "");
//////						FilmLocation tempLocation = datasource.selectRecordById(tempTitle);
//////						if (tempLocation == null) {
//////							// SET UI TO MISSING LOCATION
//////							worldStatus = "MISSING";
//////							title.setPhase(worldStatus);
//////							gameTitleList.set(counter, title);	
//////						}
//////						counter++;
//////					}	
//////				}
////				
////				
////
////				
////				
////				
//////				final ListView restoreListView = (ListView) getView().findViewById(R.id.restoreLevelDataListView);
////				
////				System.out.println("REFRESHED DATA");
////				final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
////				if (gameTitleList.size() > 0) {
////					restoreListView.setAdapter(levelRestoreListAdapter);
////					levelRestoreListAdapter.notifyDataSetChanged();
////				}
//
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onPreExecute()
//			 */
//			@Override
//			protected void onPreExecute() {
//				// Things to be done before execution of long running operation.
//				// For
//				// example showing ProgessDialog
//
//				int randomPhraseIndex = generateRandomNumber(0, 6);
//				dialog = new ProgressDialog(context);
//				dialog.setTitle("Updating...");
//				String message = "<i>" + randomPhrases[randomPhraseIndex] + "</i>";
//				dialog.setMessage(Html.fromHtml(message));
//				dialog.setCancelable(false);
//				dialog.setIndeterminate(true);
//				dialog.show();
//			}
//
//			private int generateRandomNumber(int min, int max) {
//				int randomNumber = min + (int)(Math.random() * ((max - min) + 1));
//				return randomNumber;
//			}
//			
//			// multi-lingual message prompts 
//			public String[] randomPhrases = {
//					"One moment please",
//					"Un instant s'il vous plait",
//					"Un momento por favor",
//					"Einen Moment bitte",
//					"Un momento per favore",
//					"Ett ogonblick",
//					"Een ogenblik geduld aub",
//					"Odota hetki"
//			};
//			
//			public String[] quotes = {
//					"Ard-galen - 'The Green Region'. A grassy area.",
//					"Cirion (Lord of Ships) - The twelfth Ruling Steward of Gondor."
//			};
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
//			 */
//			@Override
//			protected void onProgressUpdate(String... text) {
//				// finalResult.setText(text[0]);
//				// Things to be done while execution of long running operation
//				// is in
//				// progress. For example updating ProgessDialog
//			}
//		}
		 
	}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class AchievementsFragment extends Fragment {

		private static ArrayList<Achievement> achievementList;
		
		public AchievementsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_achievements,
					container, false);
			ListView commentView = (ListView) rootView.findViewById(R.id.commentView);
			
			achievementList = new ArrayList<Achievement>();
			Achievement comment = new Achievement();
//			String achievementId;
//			String title;
//			String catchPhrase;
//			String userId;
//			String dateTime;;
			
			for (int i = 0; i < 10; i++) {
				comment.setAchievementId("achievementId");
				comment.setTitle("title");
				comment.setCatchPhrase("catchPhrase");
				comment.setUserId("userId");
				comment.setDateTime("dateTime");
				achievementList.add(comment);	
			}
			
			AchievementArrayAdapter adapter = new AchievementArrayAdapter(
					getActivity(), intent, achievementList);
			
			if (achievementList.size() >= 0) {
				commentView.setAdapter(adapter);
			} 
			return rootView;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class AboutFragment extends Fragment {

		public AboutFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about,
					container, false);

			
			

//			instructionsLayout = (LinearLayout) rootView.findViewById(R.id.new_user_instructions);
			
			
			
			Button instructionsButton = (Button) rootView.findViewById(R.id.dismiss_instructions);
			
			// bind check-in button click event
			instructionsButton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View view) {
//					instructionsLayout.setVisibility(LinearLayout.GONE);
					
					// close activity
//					getActivity().finish();
					
					// change fragments
					selectItem(0);
				}
			});
				
				
//			}
			return rootView;
		}
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class SettingsFragment extends Fragment {

		public SettingsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);

			
			
			// CREATE ASYNC ACTIVITY TO UPLOAD AND SAVE 
			// USER PREFERENCES TO THE SERVER
//			UpdateUserPreferencesTaskRunner
			// SEND USER DOMAIN TO SERVER SIDE REST TEMPLATE
			
			final User tempUser = userSource.selectRecordById(localUser.getUserId());


	        String mobileNotifications = tempUser.getMobileNotifications();
	        String emailNotifications = tempUser.getEmailNotifications();
	        
	        String[] preferenceSettings = {
	        		mobileNotifications,
	        		emailNotifications
	        };
	        
	        int[] preferenceCheckboxes = { 
	        		R.id.checkboxNotifications1, 
	        		R.id.emailNotifications1
	        };
	        
//	        if (mobileNotifications.equals("true")) {
//	        	
//	        }
//	        if (emailNotifications.equals("true")) {
//	        	 
//	        }
//	        preferenceCheckboxes[0] =  mobile:
//	        	preferenceCheckboxes[1] =  R.id.checkboxNotifications1:
	  
	        for (int i = 0; i < preferenceCheckboxes.length; i++) {
	        	final int tempCheckBoxId = preferenceCheckboxes[i];
	        	final CheckBox tempCheckBox = (CheckBox) rootView.findViewById(tempCheckBoxId);
	        	final String tempPreferenceSetting = preferenceSettings[i];
	        	
	        	if (tempPreferenceSetting.equals("true")) {
	        		tempCheckBox.setChecked(true);
	        	} else {
	        		tempCheckBox.setChecked(false);
	        	}
	        	
//	            if (tempCheckBox.isChecked()) {
//	            	tempCheckBox.setChecked(false);
//	            }	
	        }
			
			Button updateUserPreferencesButton = (Button) rootView.findViewById(R.id.updateUserPreferencesButton1);
			
			// bind check-in button click event
			updateUserPreferencesButton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View view) {
					final UpdateUserPreferencesTaskRunner runner = new UpdateUserPreferencesTaskRunner();
					runner.execute("http://movie-locations-app.appspot.com/secure/updateUserPreferences");	
					

//					final String FINAL_CURRENT_USER_MOBILE_NOTIFICATIONS = currentUser.getMobileNotifications();
//					final String FINAL_CURRENT_USER_EMAIL_NOTIFICATIONS = currentUser.getEmailNotifications();
//					
////					public void updateUserNotificationPreferences(String recordId, String updatedEmailNotifications, String updatedMobileNotifications) {
//					// UPDATE USER PREFERENCES
//					final String FINAL_CURRENT_USER_ID_STRING = tempUser.getUserId();
//					userSource.updateUserNotificationPreferences(FINAL_CURRENT_USER_ID_STRING, FINAL_CURRENT_USER_EMAIL_NOTIFICATIONS, FINAL_CURRENT_USER_MOBILE_NOTIFICATIONS);
				}
			});
			
			
			
			

//			instructionsLayout = (LinearLayout) rootView.findViewById(R.id.new_user_instructions);
			
			
			Button deleteUserDataButton = (Button) rootView.findViewById(R.id.deleteAllUserData1);
			
			
			// bind check-in button click event
			deleteUserDataButton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View view) {
//					instructionsLayout.setVisibility(LinearLayout.GONE);
					
					// close activity
//					getActivity().finish();
					
					// change fragments
//					selectItem(0);
					
					
					// BUILD AND LAUNCH CONFIRMATION DIALOG
					
					

			        
					
					
					
			    	// CREATE CONFIRMATION DIALOG
			    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
			    	
			    	String DIALOG_MESSAGE = "Are you sure?";
			    	String DIALOG_TITLE = "Delete All User Data";
					builder.setMessage(DIALOG_MESSAGE).setTitle(DIALOG_TITLE);
			    	
			    	// TODO: ITERATE AND DELETE CURRENT LEVEL DATA
			    	// - BAG ITEMS
			    	// - QUIZ ITEMS
			    	// - LOCATIONS
			    	
			    	
			    	

			    	
			    	
			    	
					String CONFIRM_MESSAGE = "Yes";
					// Add the buttons
			    	builder.setPositiveButton(CONFIRM_MESSAGE, new DialogInterface.OnClickListener() {
			           

			    	
					
					public void onClick(DialogInterface dialog, int id) {
			        	   
//			        	   // START ASYNC THREAD
//			        	   RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
//			        	   runner.execute(FINAL_RESTORE_LEVEL_DATA_URL);
						
						
						// SEND USER DOMAIN TO SERVER SIDE REST TEMPLATE
						final DeleteUserTaskRunner runner = new DeleteUserTaskRunner();
						runner.execute("http://movie-locations-app.appspot.com/secure/cron/addDeleteUser");
			        	   
			               // User clicked OK button
			        	   System.out.println("RESTORED");
			        	   
//			        	   finish();
			           }
			       });
			    	String CANCEL_MESSAGE = "No";
					builder.setNegativeButton(CANCEL_MESSAGE, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
			               // User cancelled the dialog
			        	   System.out.println("CANCELLED");
			           }
			       });
					
//			    	Button restoreLevelDataButton = (Button) rowView.findViewById(R.id.restoreLevelData);
			    	
//			    	if (values.get(position).getTitle().equals("Restore")) {
//			    		restoreLevelDataButton.setVisibility(Button.VISIBLE);
				    	
			        	// Create the AlertDialog
			        	final AlertDialog dialog = builder.create();
			        	
//			        	((Activity) context).registerForContextMenu(rowView);
			    		
//			    		rowView.setOnClickListener(new View.OnClickListener() {
//			    		    public void onClick(View v) {
			    		    	dialog.show();
//			    		    }
//			    		});
				}
			});
				
				
//			}
			return rootView;
		}
	}


	public class DeleteUserTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
		private ProgressDialog dialog;
		private ArrayList<FilmLocation> filmLocationList;
		
		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				

	        	   
	        	   // CREATE REST TEMPLATE TO RETURN LEVEL JSON
						// TODO: create a function that only
						// places the restore button on the current
						// world available
						//
						// OR WE COULD LEAVE IT AS-IS SO THE USER
						// IS FREE TO REPLAY ANY LEVEL TO GAIN XP
						//
						// [THIS IS LEVEL GRINDING]
						// WE COULD ADD GRINDING ACHIEVEMENTS

						// Do your long operations here and return the result
//						String url = params[0];
						// resp = "async call in progress";

						// Set the Content-Type header
						HttpHeaders requestHeaders = new HttpHeaders();
						requestHeaders.setContentType(new MediaType("application", "json"));
						
						
						// GET LAST LEVEL CURRENTLY AVAILABLE
//						ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
						
						// TODO: REFACTOR THIS VALUE TO RESTORE ALL LEVELS
//						int lastLocationIndex = datasource.selectRecords().size() - 1;
//						FilmLocation lastLocation = tempLocationList.get(lastLocationIndex);
//						String lastLocationTitle = lastLocation.getTitle();
						

						
						// SEND WORLD TITLE
						// WE WON'T NEED THIS IF WE'RE USING A
						// REST TEMPLATE
//						localUser.setUserSid(lastLocationTitle);
						HttpEntity<User> requestEntity = new HttpEntity<User>(localUser, requestHeaders);
//						System.out.println("REST TEMPLATE PRE RESPONSE: " + quizItem.getSubmittedIndex());

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

						
						// CREATE A FUNCTION ON THE SERVER TO
						// TAKE REQUEST BASED ON WORLD TITLE
						//
						// RETURN ALL GCM RESPONSE JSON TO CLIENT
						// AND REBUILD LEVELS
						String url = params[0];
						ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
						User response = responseEntity.getBody();
						resp = response.getUserSid();
//						if (response.getUserSid().equals("success")) {
//							// RESET DATABASE RECORDS
////							ArrayList<BagItem> bagList = bagItemImpl.selectRecords();
////							ArrayList<BagItem> bagList = bagItemImpl.selectRecordsByTitle(title);
////							
////							for (BagItem item : bagList) {
////								if (item.getLevel().equals(currentUser.getWorldCount())) {
////									
////								}
////							}
////							ArrayList<QuizItem> quizList = quizitemsource.selectRecords();
//							
////							ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
////							int lastLocationIndex = datasource.selectRecords().size();
////							FilmLocation lastLocation = tempLocationList.get(lastLocationIndex);
////							String lastLocationTitle = lastLocation.getTitle();
////							System.out.println("ASYNC DATASOURCE: " + datasource);
////							System.out.println("ASYNC TITLE: " + lastLocation.getTitle());
//							
////							datasource.deleteRecordsByTitle(lastLocationTitle);
////							quizitemsource.deleteRecordsByTitle(lastLocationTitle);
////							bagItemImpl.deleteRecordsByLevel(response.getCurrentLevel());
////							conclusionCardImpl.deleteRecordsByLevel(response.getCurrentLevel());
//							
////							locationQuizArrayAdapter.notifyDataSetChanged();
////							locationQuizArrayAdapter.setListItemTitles(listItemTitles);
////							locationQuizArrayAdapter.setListItemImageTiles(listItemImageTiles);
////							locationQuizArrayAdapter.notifyDataSetChanged();
//							
//							// REDRAW LIST ADAPTER
////							redrawListAdapter();
//							
//							
//							
//					
//							
////							for (FilmLocation item : locationList) {
////								
////							}
//							
////							bagItemImpl.delete();
////							quizitemsource.delete();
////							datasource.delete();
//							
//							String worldCount = localUser.getWorldCount();
////							String worldCount = null;
////							if (localUser.getWorldCount().equals("0")) {
////								worldCount = "1";
////							} else {
////								worldCount = localUser.getWorldCount();
////							}
//
//							// THESE DELETIONS NEED TO HAPPEN ELSEWHERE 
////							quizitemsource.deleteRecordsByLevel(worldCount);
////							datasource.deleteRecordsByLevel(worldCount);
////							bagItemImpl.deleteRecordsByLevel(worldCount);
////							conclusionCardImpl.deleteRecordsByLevel(worldCount);
//							
//							System.out.println("RESPONSE WORLD COUNT: " + response.getWorldCount());
////							System.out.println("RESPONSE RECORDS COUNT: " + lastLocationIndex);
//						}

//						System.out.println("REST TEMPLATE POST RESPONSE: " + response.getAnswered());
						
						
						
						
						// check server for existing user, then update regId if that changed at all
						//
						// TODO: check if google plus id will change at all
						
						
//						// Set the username and password for creating a Basic Auth request
////						HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
//						HttpHeaders requestHeaders = new HttpHeaders();
//						requestHeaders.setContentType(new MediaType("application", "json"));
////						requestHeaders.setAuthorization(authHeader);
//						HttpEntity<User> requestEntity = new HttpEntity<User>(requestHeaders);
	//
//						// Create a new RestTemplate instance
//						RestTemplate restTemplate = new RestTemplate();
	//
//						// Add the String message converter
//						restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//						
//					    // Make the HTTP GET request to the Basic Auth protected URL
//					    ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
//						User response = responseEntity.getBody();
						System.out.println("NEW USER REGISTERED: " + response.getDisplayName());
						
						
						
						
						
					

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
//			String message = "Success!";
//			if (result.equals("success")) {
//				message = "Level data restored.";
//			} else if (result.equals("error")) {
//				message = "Something went wrong.";
//			} else {
//				message = "Level data reset.";
//				
//				quizItemList = quizItemImpl.selectRecordsByTitle(result);
//				// RESET QUIZ ITEMS LOCALLY
//				// WE NEED TO GET THE WORLD TITLE
//				
//				for (QuizItem localQuizItem : quizItemList) {
//					// update database record
//					quizItemImpl.updateRecordAnswered(localQuizItem.getQuestionId(), "false");
//					quizItemImpl.updateRecordCorrectAnswerIndex(localQuizItem.getQuestionId(), "null");
//				}
//			}
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//			mDrawerLayout.closeDrawers();
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
	
	public class UpdateUserPreferencesTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
		private ProgressDialog dialog;
		private ArrayList<FilmLocation> filmLocationList;
		
		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				

	        	   
	        	   // CREATE REST TEMPLATE TO RETURN LEVEL JSON
						// TODO: create a function that only
						// places the restore button on the current
						// world available
						//
						// OR WE COULD LEAVE IT AS-IS SO THE USER
						// IS FREE TO REPLAY ANY LEVEL TO GAIN XP
						//
						// [THIS IS LEVEL GRINDING]
						// WE COULD ADD GRINDING ACHIEVEMENTS

						// Do your long operations here and return the result
//						String url = params[0];
						// resp = "async call in progress";

						// Set the Content-Type header
						HttpHeaders requestHeaders = new HttpHeaders();
						requestHeaders.setContentType(new MediaType("application", "json"));
						
						
						// GET LAST LEVEL CURRENTLY AVAILABLE
//						ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
						
						// TODO: REFACTOR THIS VALUE TO RESTORE ALL LEVELS
//						int lastLocationIndex = datasource.selectRecords().size() - 1;
//						FilmLocation lastLocation = tempLocationList.get(lastLocationIndex);
//						String lastLocationTitle = lastLocation.getTitle();
						

						
						// SEND WORLD TITLE
						// WE WON'T NEED THIS IF WE'RE USING A
						// REST TEMPLATE
//						localUser.setUserSid(lastLocationTitle);
						HttpEntity<User> requestEntity = new HttpEntity<User>(localUser, requestHeaders);
//						System.out.println("REST TEMPLATE PRE RESPONSE: " + quizItem.getSubmittedIndex());

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

						
						// CREATE A FUNCTION ON THE SERVER TO
						// TAKE REQUEST BASED ON WORLD TITLE
						//
						// RETURN ALL GCM RESPONSE JSON TO CLIENT
						// AND REBUILD LEVELS
						String url = params[0];
						ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
						User response = responseEntity.getBody();
						resp = response.getUserSid();
//						if (response.getUserSid().equals("success")) {
//							// RESET DATABASE RECORDS
////							ArrayList<BagItem> bagList = bagItemImpl.selectRecords();
////							ArrayList<BagItem> bagList = bagItemImpl.selectRecordsByTitle(title);
////							
////							for (BagItem item : bagList) {
////								if (item.getLevel().equals(currentUser.getWorldCount())) {
////									
////								}
////							}
////							ArrayList<QuizItem> quizList = quizitemsource.selectRecords();
//							
////							ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
////							int lastLocationIndex = datasource.selectRecords().size();
////							FilmLocation lastLocation = tempLocationList.get(lastLocationIndex);
////							String lastLocationTitle = lastLocation.getTitle();
////							System.out.println("ASYNC DATASOURCE: " + datasource);
////							System.out.println("ASYNC TITLE: " + lastLocation.getTitle());
//							
////							datasource.deleteRecordsByTitle(lastLocationTitle);
////							quizitemsource.deleteRecordsByTitle(lastLocationTitle);
////							bagItemImpl.deleteRecordsByLevel(response.getCurrentLevel());
////							conclusionCardImpl.deleteRecordsByLevel(response.getCurrentLevel());
//							
////							locationQuizArrayAdapter.notifyDataSetChanged();
////							locationQuizArrayAdapter.setListItemTitles(listItemTitles);
////							locationQuizArrayAdapter.setListItemImageTiles(listItemImageTiles);
////							locationQuizArrayAdapter.notifyDataSetChanged();
//							
//							// REDRAW LIST ADAPTER
////							redrawListAdapter();
//							
//							
//							
//					
//							
////							for (FilmLocation item : locationList) {
////								
////							}
//							
////							bagItemImpl.delete();
////							quizitemsource.delete();
////							datasource.delete();
//							
//							String worldCount = localUser.getWorldCount();
////							String worldCount = null;
////							if (localUser.getWorldCount().equals("0")) {
////								worldCount = "1";
////							} else {
////								worldCount = localUser.getWorldCount();
////							}
//
//							// THESE DELETIONS NEED TO HAPPEN ELSEWHERE 
////							quizitemsource.deleteRecordsByLevel(worldCount);
////							datasource.deleteRecordsByLevel(worldCount);
////							bagItemImpl.deleteRecordsByLevel(worldCount);
////							conclusionCardImpl.deleteRecordsByLevel(worldCount);
//							
//							System.out.println("RESPONSE WORLD COUNT: " + response.getWorldCount());
////							System.out.println("RESPONSE RECORDS COUNT: " + lastLocationIndex);
//						}

//						System.out.println("REST TEMPLATE POST RESPONSE: " + response.getAnswered());
						
						
						
						
						// check server for existing user, then update regId if that changed at all
						//
						// TODO: check if google plus id will change at all
						
						
//						// Set the username and password for creating a Basic Auth request
////						HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
//						HttpHeaders requestHeaders = new HttpHeaders();
//						requestHeaders.setContentType(new MediaType("application", "json"));
////						requestHeaders.setAuthorization(authHeader);
//						HttpEntity<User> requestEntity = new HttpEntity<User>(requestHeaders);
	//
//						// Create a new RestTemplate instance
//						RestTemplate restTemplate = new RestTemplate();
	//
//						// Add the String message converter
//						restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//						
//					    // Make the HTTP GET request to the Basic Auth protected URL
//					    ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
//						User response = responseEntity.getBody();
						System.out.println("NEW USER REGISTERED: " + response.getDisplayName());
						
						
						
						
						
					

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
//			String message = "Success!";
//			if (result.equals("success")) {
//				message = "Level data restored.";
//			} else if (result.equals("error")) {
//				message = "Something went wrong.";
//			} else {
//				message = "Level data reset.";
//				
//				quizItemList = quizItemImpl.selectRecordsByTitle(result);
//				// RESET QUIZ ITEMS LOCALLY
//				// WE NEED TO GET THE WORLD TITLE
//				
//				for (QuizItem localQuizItem : quizItemList) {
//					// update database record
//					quizItemImpl.updateRecordAnswered(localQuizItem.getQuestionId(), "false");
//					quizItemImpl.updateRecordCorrectAnswerIndex(localQuizItem.getQuestionId(), "null");
//				}
//			}
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//			mDrawerLayout.closeDrawers();
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
			dialog.setMessage("Saving your preferences on the server");
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
	
	
//	// user detail fragment
//
//
//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class FilmLocationsFragment extends Fragment {
//
//
//		/**
//		 * The {@link android.support.v4.view.PagerAdapter} that will provide
//		 * fragments for each of the sections. We use a
//		 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
//		 * will keep every loaded fragment in memory. If this becomes too memory
//		 * intensive, it may be best to switch to a
//		 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
//		 */
//		private static SectionsPagerAdapter mSectionsPagerAdapter;
//
//		/**
//		 * The {@link ViewPager} that will host the section contents.
//		 */
//		private static ViewPager mViewPager;
//
//		private MovieLocationsImpl datasource;
//		// private ArrayList<FilmLocation> filmList;
//		private static LinkedHashMap<String, ArrayList<FilmLocation>> filmMap;
//		private static LinkedHashMap<String, FilmLocation> titleMap;
//		private static CharSequence title;
//		// private static List<FilmLocation> listMatch;
//		private static ArrayList<FilmLocation> filmContent;
//		private static List<String> filmList;
//		private static ArrayList<FilmLocation> content;
//		private ListView filmListView;
//
//		private static String section_label;
//
//		private static String releaseYear;
//
//		private static String locations;
//
//		private static String funFacts;
//
//		private static String productionCompany;
//
//		private static String distributor;
//
//		private static String director;
//
//		private static String writer;
//
//		private static String actors;
//
//		private static String latitude;
//
//		private static String longitude;
//		private static ArrayList<FilmLocation> filmArrayList;
//		private static int pagerPosition;
//		private static Intent intent;
//		private static FilmLocation currentLocation;
//		private static String MOVIE_POSTER_URL;
//
//		private static int currentPosition = 0;
//		
////		private static User currentUser;
//
//		public FilmLocationsFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_user_detail,
//					container, false);
//			
//			// Create the adapter that will return a fragment for each of the three
//			// primary sections of the app.
//			mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
//			
////			protected ImageLoader imageLoader = ImageLoader.getInstance();
//
////				setContentView(R.layout.activity_film_location_pager);
//				// ClassLoaderHelper.setClassLoader(getClass().getClassLoader());
//
//
////				// TODO: check for fragment injection vulnerability
////				Bundle bundle = getIntent().getExtras();
//////				
////				currentUser = bundle.getParcelable("localUser");
//
//
//				System.out.println("LOCAL USER DETAIL ACTIVITY WENT THROUGH: " +  localUser.getDisplayName());
//				
//				datasource = new MovieLocationsImpl(getActivity());
//				filmMap = FilmLocationCollection.createFilmLocationMap(datasource
//						.selectRecords());
//
//				if (filmMap != null) {
//					titleMap = FilmLocationCollection.createFilmTitleMap(filmMap);
//				}
//
//				filmArrayList = new ArrayList<FilmLocation>();
//
//				// Set up the ViewPager with the sections adapter.
//				mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
//				mViewPager.setAdapter(mSectionsPagerAdapter);
//
//				initializeFirstMovie();
//				
//				
//				
//
//				
//				
////				System.out.println("USER PARCEL EXTRA *****************************: " + currentUser.getDisplayName());
////				
////				
////				
////				
//////				currentUser = bundle.getParcelable("localUser");
////				ImageView userAvatar = (ImageView) findViewById(R.id.userAvatarWidget);
////				imageLoader.displayImage(currentUser.getAvatarImageUrl(), userAvatar);
////				
////				TextView userFullNameWidget = (TextView) findViewById(R.id.userFullNameWidget);
////				userFullNameWidget.setText(currentUser.getDisplayName());
//				
//				
//				
//				
//				
//				
//				
//				
//
////				bundle.putString("displayName", user.getDisplayName());
////				bundle.putString("userAvatar", user.getAvatarImageUrl());
//
////				Fragment fragment = Fragment.instantiate(this, UserWidgetFragment.class.getName(), bundle);
////				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
////				transaction.replace(R.id.user_widget_fragment, fragment);
////				transaction.commitAllowingStateLoss();
//				
////				Bundle fragmentBundle = new Bundle();
////				fragmentBundle.putString("displayName", user.getDisplayName());
////				fragmentBundle.putString("userAvatar", user.getAvatarImageUrl());
////				UserWidgetFragment userWidgetFragment = new UserWidgetFragment();
////				userWidgetFragment.setArguments(fragmentBundle);
//				
//				return rootView;
//			}
//
//			private void initializeFirstMovie() {
//				filmList = new ArrayList<String>(filmMap.keySet());
//				currentLocation = titleMap.get(filmList.get(0));
//
//				intent = new Intent(getActivity(), WorldLocationDetailActivity.class);
//				intent.putExtra("section_label", currentLocation.getTitle());
//				intent.putExtra("title", currentLocation.getTitle());
//				intent.putExtra("releaseYear", currentLocation.getReleaseYear());
//				intent.putExtra("locations", currentLocation.getLocations());
//				intent.putExtra("funFacts", currentLocation.getFunFacts());
//				intent.putExtra("productionCompany", currentLocation.getProductionCompany());
//				intent.putExtra("distributor", currentLocation.getDistributor());
//				intent.putExtra("director", currentLocation.getDirector());
//				intent.putExtra("writer", currentLocation.getWriter());
//				intent.putExtra("actors", currentLocation.getActor1());
//				intent.putExtra("latitude", currentLocation.getLatitude());
//				intent.putExtra("longitude", currentLocation.getLongitude());
//				
//				intent.putExtra("localUser", localUser);
////				intent.putExtra("userAvatarUrl", currentUser.getAvatarImageUrl());
//			}
//
//			protected static void setCurrentPosition(int position) {
//				currentPosition = position;
//				System.out.println("CURRENT POSITION SET: " + position);
//			}
//
//			protected static int getCurrentPosition() {
//				return currentPosition;
//			}
//
////			@Override
////			public boolean onCreateOptionsMenu(Menu menu) {
////				// Inflate the menu; this adds items to the action bar if it is present.
////				getMenuInflater().inflate(R.menu.director_sort, menu);
////				return true;
////			}
//
//			/**
//			 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//			 * one of the sections/tabs/pages.
//			 */
//			public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//				public SectionsPagerAdapter(FragmentManager fm) {
//					super(fm);
//				}
//
//				@Override
//				public Fragment getItem(int position) {
//					// getItem is called to instantiate the fragment for the given page.
//					// Return a MovieSectionFragment (defined as a static inner class
//					// below) with the page number as its lone argument.
//					// System.out.println("INDEX: " + position);
//					Fragment fragment = new MovieSectionFragment();
//					Bundle args = new Bundle();
//					args.putInt(MovieSectionFragment.ARG_SECTION_NUMBER, position);
//					fragment.setArguments(args);
//					return fragment;
//				}
//
//				@Override
//				public int getCount() {
//					// Show total pages.
//					return filmMap.keySet().size();
//				}
//
//				@Override
//				public CharSequence getPageTitle(int position) {
//					Locale l = Locale.getDefault();
//					title = formatFilmData(position);
//					return title;
//				}
//
//				private String formatFilmData(int position) {
//					filmList = new ArrayList<String>(filmMap.keySet());
//					String filmTitle = "Title Missing";
//					if (filmList.get(position) != null) {
//						filmTitle = filmList.get(position);
//					}
//					return filmTitle;
//				}
//			}
//
//			/**
//			 * Movie fragment
//			 * 
//			 */
//			public static class MovieSectionFragment extends Fragment implements
//					OnPageChangeListener {
//				/**
//				 * The fragment argument representing the section number for this
//				 * fragment.
//				 */
//				public static final String ARG_SECTION_NUMBER = "section_number";
//
//				protected ImageLoader imageLoader = ImageLoader.getInstance();
//
//				public MovieSectionFragment() {
//					super();
//				}
//
//				@Override
//				public void onPageScrollStateChanged(int arg0) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onPageScrolled(int arg0, float arg1, int arg2) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onPageSelected(int position) {
//					setCurrentPosition(position);
//					System.out.println("PAGE INDEX CHANGED: " + position);
//
//					String currentLoc = filmList.get(getCurrentPosition());
//					currentLocation = titleMap.get(currentLoc);
//					// intent = new Intent(getActivity(),
//					// WorldLocationDetailActivity.class);
//					intent.replaceExtras(intent);
//					intent.putExtra("section_label", currentLocation.getTitle());
//					intent.putExtra("title", currentLocation.getTitle());
//					intent.putExtra("releaseYear", currentLocation.getReleaseYear());
//					intent.putExtra("locations", currentLocation.getLocations());
//					intent.putExtra("funFacts", currentLocation.getFunFacts());
//					intent.putExtra("productionCompany",
//							currentLocation.getProductionCompany());
//					intent.putExtra("distributor", currentLocation.getDistributor());
//					intent.putExtra("director", currentLocation.getDirector());
//					intent.putExtra("writer", currentLocation.getWriter());
//					intent.putExtra("actors", currentLocation.getActor1());
//					intent.putExtra("latitude", currentLocation.getLatitude());
//					intent.putExtra("longitude", currentLocation.getLongitude());
//				}
//
//				@Override
//				public View onCreateView(LayoutInflater inflater, ViewGroup container,
//						Bundle savedInstanceState) {
//					View rootView = inflater.inflate(
//							R.layout.fragment_film_panel, container, false);
//					// INFO:
//					//
//					// all of these values need to be separate
//					// from the intent data values because the
//					// pager indexing can only accurately be
//					// captured after the first swipe
//					String currentLoc = filmList.get(getArguments().getInt(
//							ARG_SECTION_NUMBER));
////					System.out.println("current loc: " + currentLoc);
////					ImageView moviePosterThumb = (ImageView) rootView
////							.findViewById(R.id.imageView1);
////					MOVIE_POSTER_URL = MoviePostersHashMap.createHashMap().get(
////							currentLoc);
////					moviePosterThumb.setAlpha(0.1f);
////					imageLoader.displayImage(MOVIE_POSTER_URL, moviePosterThumb);
//		//
//					// movie poster click handler
//					filmList = new ArrayList<String>(filmMap.keySet());
//
//					System.out.println("filmMap.size()" + filmMap.size());
//
//					filmArrayList = new ArrayList<FilmLocation>();
//
//					for (FilmLocation collection : filmMap.get(title)) {
//						// System.out.println("FILM MAP COLLECTION:" + collection);
//
//						if (collection.getTitle().equals(title)) {
//							filmArrayList.add(collection);
//						}
//					}
//					
//					WorldLocationArrayAdapter commentAdapter = new WorldLocationArrayAdapter(
//							getActivity(), intent, filmArrayList);
//					ListView commentView = (ListView) rootView
//							.findViewById(R.id.listview);
//					
//					if (!currentLocation.getLocations().equals("null")) {
//						commentView.setAdapter(commentAdapter);
//					} 
//					
//					rootView.setOnClickListener(new View.OnClickListener() {
//						public void onClick(View v) {
//							startActivity(intent);
//						}
//					});
//
//					TextView filmTitle = (TextView) rootView
//							.findViewById(R.id.film_title);
//					filmTitle.setText(currentLoc);
//					mViewPager.setOnPageChangeListener(this);
//
//					return rootView;
//				}
////			}
//			
//			
//			
//			
//			
//			
//			
//
////			Button getStartedButton = (Button) rootView
////					.findViewById(R.id.get_started_button);
////			
////			getStartedButton.setOnClickListener(new View.OnClickListener() {
////				public void onClick(View v) {
////					// start new intent
////					Intent pagerActivityIntent = new Intent(getActivity(),
////							FilmLocationPagerActivity.class);
////					pagerActivityIntent.putExtra("localUser", localUser);
////					startActivity(pagerActivityIntent);
////				}
////			});
//
////			return rootView;
//		}
//	}

}
