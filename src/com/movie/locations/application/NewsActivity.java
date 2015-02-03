package com.movie.locations.application;
import java.util.ArrayList;
import java.util.Collections;
import com.movie.locations.R;
import com.movie.locations.adapter.GameTitleArrayAdapter;
import com.movie.locations.adapter.NavMenuItemArrayAdapter;
import com.movie.locations.database.BagItemImpl;
import com.movie.locations.database.ConclusionCardImpl;
import com.movie.locations.database.GameTitleImpl;
import com.movie.locations.database.LocationsImpl;
import com.movie.locations.database.PointsItemImpl;
import com.movie.locations.database.QuizItemImpl;
import com.movie.locations.database.UserImpl;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.NavMenuItem;
import com.movie.locations.domain.NewsItem;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.utility.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
//import android.support.v4.widget.DrawerLayout;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsActivity extends ActionBarActivity {

	// navigation drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navArray;
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	private User localUser;
	private FragmentTransaction ft;
	private Intent intent;
	private Context context;
	private UserImpl userImpl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_news);
		context = this;

//		if (savedInstanceState == null) {
			intent = getIntent();
			Bundle bundle = intent.getExtras();
			localUser = bundle.getParcelable("localUser");
			// System.out.println("NEWS ACTIVITY LOCAL USER PARCEL CURRENT POINTS: " + localUser.getCurrentPoints());

			Fragment locationsFragment = new FilmLocationsFragment();
			Bundle locationsBundle = new Bundle();
			locationsBundle.putParcelable("localUser", localUser);
			locationsFragment.setArguments(locationsBundle);
//			ft.replace(R.id.content_frame, locationsFragment);
			
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, locationsFragment).commit();

			
			userImpl = new UserImpl(context);
//		}
		
		
//		GcmIntentService.setCurrentUserId(localUser.getUserId());
		
		// setup navigation drawer
		navArray = new String[5];
//		navArray[0] = "News";
		navArray[0] = "Worlds";
		navArray[1] = "Player";
//		navArray[2] = "Friends";
//		navArray[3] = "Restore";
//		navArray[3] = "Cards";
		navArray[2] = "Items";
		navArray[3] = "About";
		navArray[4] = "Settings";
//		navArray[6] = "Help";
		
		final String userImageUrl = localUser.getAvatarImageUrl();

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		
		// set up the drawer's list view with items and click listener
		ArrayList<NavMenuItem> navList = new ArrayList<NavMenuItem>();
		
		for (int a = 0; a < navArray.length; a++) {

			NavMenuItem menuItem = new NavMenuItem();
			String currentImageUrl = "drawable://";
			switch(a) {
			case 0:
				currentImageUrl += R.drawable.breadboard_menu;
				break;
			case 1:
				currentImageUrl = userImageUrl;
				break;
			case 2:
				currentImageUrl += R.drawable.backpack_blue;
				break;
			case 3:
				currentImageUrl += R.drawable.help;
				break;
			case 4:
				currentImageUrl += R.drawable.settings;
				break;
			}
			menuItem.setTitle(navArray[a]);
			menuItem.setImageUrl(currentImageUrl);
			navList.add(menuItem);
		}
		NavMenuItemArrayAdapter menuAdapter = new NavMenuItemArrayAdapter(this, intent, navList);
		
//		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navArray));
		mDrawerList.setAdapter(menuAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, 0
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

//		if (savedInstanceState == null) {
//			selectItem(0);
//		}
		
		if (localUser != null) {
			// System.out.println("WORLD COUNT NEWS ACTIVITY" + localUser.getWorldCount());
			// System.out.println("WORLD COUNT");
			if (localUser.getWorldCount().equals("1")) {
				// display help fragment
				selectItem(5);
//				// System.out.println("CURRENT LEVEL: " + localUser.getCurrentLevel());
			}
		} else {
			selectItem(0);
		}
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
        
        int id = view.getId();
		if (id == R.id.checkboxNotifications1) {
			if (checked) {
				// System.out.println("Mobile notifications checked");
				localUser.setMobileNotifications("true");
			} else {
				localUser.setMobileNotifications("false");
			}
		}
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
		return super.onPrepareOptionsMenu(menu);
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
		int itemId = item.getItemId();
		if (itemId == R.id.action_websearch) {
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
		} else {
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

//	@Override
//	public void onResume() {
//		// System.out.println("******* NEWS RESUME ********");
//		super.onResume();
//		
////		// System.out.println("CONCLUSION CARD EMPTY:" + conclusionCardAdapter.isEmpty());
//		if (conclusionCardAdapter != null) {
//			cardList = conclusionCardImpl.selectRecords();
//			// System.out.println("CONCLUSION CARD COUNT:" + cardList.size());
//			conclusionCardAdapter.notifyDataSetChanged();	
//		}
//		
//		if (localUser != null) {
//			localUser = userSource.selectRecordById(localUser.getUserId());
//		}
//		
////		quizItemList = quizItemImpl.selectRecords();
//		cardList = conclusionCardImpl.selectRecords();
//
//		// BAG ITEM NEWS ITEM
//		bagItemList = bagItemImpl.selectRecords();
//	}
	 
	private void selectItem(int position) {

		// TODO: write references to activities

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(navArray[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
        ft = getSupportFragmentManager().beginTransaction();
        
//		navArray[0] = "Worlds";
//		navArray[1] = "Profile";
//		navArray[2] = "Items";
//		navArray[3] = "About";
//		navArray[4] = "Settings";
		
		
		switch (position) {
		case 0:
			Fragment locationsFragment = new FilmLocationsFragment();
			Bundle locationsBundle = new Bundle();
			locationsBundle.putParcelable("localUser", localUser);
			locationsFragment.setArguments(locationsBundle);
			ft.replace(R.id.content_frame, locationsFragment);
			break;
		case 1:
			Fragment userFragment = new UserDetailFragment();
			Bundle userBundle = new Bundle();
			userBundle.putParcelable("localUser", localUser);
			userFragment.setArguments(userBundle);
			ft.replace(R.id.content_frame, userFragment);
			break;
		case 2:
//			ft.replace(R.id.content_frame, new NewsFragment());
			ft.replace(R.id.content_frame, new BagItemListFragment());
			break;
//		case 3:
//			ft.replace(R.id.content_frame, new ConclusionCardFragment());
//			break;
		case 3:
			ft.replace(R.id.content_frame, new AboutFragment());
			break;
		case 4:
			ft.replace(R.id.content_frame, new SettingsFragment());
			break;
		case 5:
			
			break;
		}
		ft.commit();

		// System.out.println("nav item " + position + " clicked");
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
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
	
	public class BagItemListFragment extends Fragment {
		
//		private LinearLayout instructionsLayout;
		protected boolean refreshed = false;
		private ArrayList<BagItem> bagItemList;
		private BagItemImpl bagItemImpl;

		public BagItemListFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_bag_item_list, container, false);


					final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreBagItemDataListView1);
					final Context context = getActivity().getApplicationContext();
					GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
//					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecords();

					String WORLD_TITLE = "BAG_ITEM_TITLE";
					
					gameTitleImpl.open();
					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(WORLD_TITLE);
					gameTitleImpl.close();
					
					int counter = 0;
					for (GameTitle tempTitle : gameTitleList) {
						counter++;
						// System.out.println("CARD DATABASE TITLE COUNTER: " + counter);
					}
					final Context bagContext = getActivity().getApplicationContext();
					
					bagItemImpl = new BagItemImpl(bagContext);
					bagItemImpl.open();
					bagItemList = bagItemImpl.selectRecords();
					bagItemImpl.close();
					
					// System.out.println("REFRESHED DATA");
					
					// sort the list
					Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
					Collections.sort(bagItemList, StaticSortingUtilities.BAG_ITEMS_ALPHABETICAL_ORDER);
					
					// ITERATE THROUGH NODES AND UPDATE NON-MISSING STATES
					// System.out.println("GAME TITLE LENGTH: " + gameTitleList.size());
					// System.out.println("BAG ITEM LENGTH: " + bagItemList.size());
					
//					if (gameTitleList.size() > 0) {
//						if (bagItemList.size() > 0) {
//							for (int i = 0; i < bagItemList.size(); i++) {
//								GameTitle tempTitle = gameTitleList.get(i);
//								BagItem existingBagItem = bagItemList.get(i);
//								tempTitle.setPhase("EXISTS");
//								String tempImageUrl = existingBagItem.getItemId();
//								// System.out.println("CURRENT BAG ITEM URL: " + tempImageUrl);
//								tempTitle.setImageUrl(tempImageUrl);
//								tempTitle.setLevel(existingBagItem.getLevel());
//								gameTitleList.set(i, tempTitle);
//							}	
//						}
//					}
					if (gameTitleList.size() > 0) {
						if (bagItemList.size() > 0) {
							for (int i = 0; i < bagItemList.size(); i++) {
								GameTitle tempTitle = gameTitleList.get(i);
								String currentTitleString = tempTitle.getTitle();
//								QuizItem tempQuizItem = quizItemImpl.selectRecordById(tempTitle.getId());
//								// System.out.println("TEMP QUIZ ITEM: " + tempQuizItem.getAnswered());
								// System.out.println("TEMP TITLE ID: " + tempTitle.getId());
								BagItem existingBagItem = bagItemList.get(i);
								String currentCardTitle = existingBagItem.getItemTitle();
								// System.out.println("CURRENT GAME TITLE TITLE: " + currentTitleString);
								// System.out.println("CURRENT CARD TITLE: " + currentCardTitle);
								if (currentTitleString.equals(currentCardTitle)) {
									System.out.println("BAG ITEM TITLE: " + currentCardTitle);
									// System.out.println("CARD EXISTS");
									tempTitle.setPhase("EXISTS");
									String tempImageUrl = existingBagItem.getImageUrl();
									tempTitle.setTitle(currentCardTitle);
									// System.out.println("BAG ITEM IMAGE URL: " + existingBagItem.getImageUrl());
									tempTitle.setImageUrl(tempImageUrl);
									tempTitle.setLevel(existingBagItem.getLevel());
									tempTitle.setDescription(existingBagItem.getDescription());
								} else {
									// System.out.println("CARD DOESN'T EXIST");
									tempTitle.setPhase("MISSING");
								}
								gameTitleList.set(i, tempTitle);
							}	
						}
					}
					final Intent intent = getActivity().getIntent();
					final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
					
					if (gameTitleList.size() > 0) {
						restoreListView.setAdapter(levelRestoreListAdapter);
						levelRestoreListAdapter.notifyDataSetChanged();
					}			
			return rootView;
		}
	}
	public class ConclusionCardFragment extends Fragment {
		
		protected boolean refreshed = false;
		private ArrayList<ConclusionCard> cardList;

		public ConclusionCardFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_conclusion_card, container, false);

					final Context cardContext = getActivity().getApplicationContext();
					
					ConclusionCardImpl conclusionCardImpl = new ConclusionCardImpl(cardContext);
					conclusionCardImpl.open();
					cardList = conclusionCardImpl.selectRecords();
					conclusionCardImpl.close();
					
					final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreConclusionCardDataListView1);
					final Context context = getActivity().getApplicationContext();
					GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
					String CARD_TITLE = "CARD_TITLE";
					
					gameTitleImpl.open();
					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(CARD_TITLE);
					gameTitleImpl.close();
					
					for (GameTitle tempTitle : gameTitleList) {
						// System.out.println("CARD DATABASE TITLE: " + tempTitle.getTitle());
					}
					// System.out.println("REFRESHED DATA");

					final Context quizContext = getActivity().getApplicationContext();
					QuizItemImpl quizItemImpl = new QuizItemImpl(quizContext);
					// sort the list
					Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
					Collections.sort(cardList, StaticSortingUtilities.CARD_TITLES_ALPHABETICAL_ORDER);
					

					// System.out.println("CARD TITLES LENGTH: " + gameTitleList.size());
					if (gameTitleList.size() > 0) {
						if (cardList.size() > 0) {
							for (int i = 0; i < cardList.size(); i++) {
								GameTitle tempTitle = gameTitleList.get(i);
								String currentTitleString = tempTitle.getTitle();
//								QuizItem tempQuizItem = quizItemImpl.selectRecordById(tempTitle.getId());
								// System.out.println("TEMP QUIZ ITEM: " + tempTitle.getId());
//								// System.out.println("TEMP QUIZ ITEM: " + tempQuizItem.getAnswered());
								// System.out.println("TEMP TITLE ID: " + tempTitle.getId());
								ConclusionCard existingCard = cardList.get(i);
								String currentCardTitle = existingCard.getTitle();
								// System.out.println("CURRENT GAME TITLE TITLE: " + currentTitleString);
								// System.out.println("CURRENT CARD TITLE: " + currentCardTitle);
								if (currentTitleString.equals(currentCardTitle)) {
									// SET THE IMAGE
									// System.out.println("CARD EXISTS");
									tempTitle.setPhase("EXISTS");
									String tempImageUrl = existingCard.getImageUrl();
									// System.out.println("TEMP IMAGE URL*: " + tempImageUrl);
									final String finalImageUrl = "assets://" + tempImageUrl + ".jpg";
									tempTitle.setImageUrl(finalImageUrl);
									tempTitle.setLevel(existingCard.getLevel());
									tempTitle.setType("conclusion");
								} else {
									// CARD IS MISSING
									tempTitle.setPhase("MISSING");
								}
								gameTitleList.set(i, tempTitle);
							}	
						}
					}
					final Intent intent = getActivity().getIntent();
					final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
					
					if (gameTitleList.size() > 0) {
						restoreListView.setAdapter(levelRestoreListAdapter);
						levelRestoreListAdapter.notifyDataSetChanged();
					}
			return rootView;
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class UserDetailFragment extends Fragment {
		
		public UserDetailFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_user_detail,
					container, false);

			ImageView userAvatar = (ImageView) rootView
					.findViewById(R.id.userAvatar1);

			final User localUser = getArguments().getParcelable("localUser");
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
			// System.out.println("localUser.getDisplayName(): " + localUser.getDisplayName());
			// System.out.println("userText: " + userText);
			userText.setText(localUser.getDisplayName());
			
//			currentLevelText.setText(localUser.getCurrentLevel());
			
			String currentPoints = localUser.getPoints();
			// System.out.println("localUser.getPoints: " + currentPoints);			
			final String FINAL_USER_ID = localUser.getUserId();
			final String FINAL_CURRENT_USER_LEVEL = localUser.getCurrentLevel();
			
			final Context pointsContext = getActivity().getApplicationContext();
			
			final PointsItemImpl pointsItemImpl = new PointsItemImpl(pointsContext);
			pointsItemImpl.open();
			final PointsItem FINAL_USER_POINTS_ITEM = pointsItemImpl.selectRecordById(FINAL_USER_ID);
			pointsItemImpl.close();
			
			final String FINAL_USER_POINTS;
			if (FINAL_USER_POINTS_ITEM != null) {
				FINAL_USER_POINTS = FINAL_USER_POINTS_ITEM.getPoints();
				// System.out.println("FINAL_USER_POINTS: " + FINAL_USER_POINTS);
				localUser.setCurrentPoints(FINAL_USER_POINTS);
			} else {
				FINAL_USER_POINTS = "0";
				localUser.setCurrentPoints(FINAL_USER_POINTS);
			}
			final int FINAL_USER_POINTS_INT = Integer.parseInt(FINAL_USER_POINTS);
			int currentLevel = StaticSortingUtilities.CHECK_LEVEL_RANGE(FINAL_CURRENT_USER_LEVEL, FINAL_USER_POINTS_INT);
			final String CURRENT_LEVEL_STRING = Integer.toString(currentLevel); 
			currentLevelText.setText(CURRENT_LEVEL_STRING);
			int[] levelRange = StaticSortingUtilities.getLevelRange();
			int nextLevelIndex = Integer.parseInt(localUser.getCurrentLevel()) + 1;
			final int finalLevelCap = levelRange[nextLevelIndex];
			String currentPointsString = FINAL_USER_POINTS + "/" + Integer.toString(finalLevelCap);
			pointsText.setText(currentPointsString);
			return rootView;
		}
	}
		

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class FilmLocationsFragment extends Fragment {

		protected boolean refreshed = false;

		public FilmLocationsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_film_locations,
					container, false);
			final User localUser = getArguments().getParcelable("localUser");
//			TextView locationsTitle = (TextView) rootView.findViewById(R.id.locationsTag1);
//			TextView locationsText = (TextView) rootView.findViewById(R.id.locationsText);

//			locationsTitle.setText("Film Locations");
//			locationsText.setText("Get started!");
			
//			Button getStartedButton = (Button) rootView.findViewById(R.id.get_started_button);
			ImageView gameWorldsImage1 = (ImageView) rootView.findViewById(R.id.gameWorldsImage1);
			ImageView gameWorldsImage2 = (ImageView) rootView.findViewById(R.id.gameWorldsImage2);
			ImageView gameWorldsImage3 = (ImageView) rootView.findViewById(R.id.gameWorldsImage3);
			ImageView gameWorldsImage4 = (ImageView) rootView.findViewById(R.id.gameWorldsImage4);
			final Context context = getActivity().getApplicationContext();
			
			final LocationsImpl locationsImpl = new LocationsImpl(context);
			locationsImpl.open();
			final ArrayList<FilmLocation> defaultLocationList = locationsImpl.selectRecords();
			locationsImpl.close();
			
			// System.out.println("DEFAULT LOCATION LIST :" +  defaultLocationList);
			final FilmArrayList defaultLocationArrayList = new FilmArrayList();
//			defaultLocationArrayList.setFilmList(defaultLocationList);
			
			final ArrayList<FilmLocation> worldLocationList1 = new ArrayList<FilmLocation>();
			final ArrayList<FilmLocation> worldLocationList2 = new ArrayList<FilmLocation>();
			final ArrayList<FilmLocation> worldLocationList3 = new ArrayList<FilmLocation>();
			final ArrayList<FilmLocation> worldLocationList4 = new ArrayList<FilmLocation>();
			
			for (int i = 0; i < defaultLocationList.size(); i++) {
				FilmLocation tempLocation = defaultLocationList.get(i);
				if (i <= 24) {
					worldLocationList1.add(tempLocation);
				} else if (i > 24 && i <= 49) {
					worldLocationList2.add(tempLocation);
				} else if (i > 49 && i <= 74) {
					worldLocationList3.add(tempLocation);
				} else if (i > 74 && i <= 99) {
					worldLocationList4.add(tempLocation);
				}
			}
			
			ArrayList<ArrayList<FilmLocation>> worldLocationArrayList = new ArrayList<ArrayList<FilmLocation>>();		
			worldLocationArrayList.add(worldLocationList1);
			worldLocationArrayList.add(worldLocationList2);
			worldLocationArrayList.add(worldLocationList3);
			worldLocationArrayList.add(worldLocationList4);
			
//			ImageView[] worldImageArray = {
//					gameWorldsImage1,
//					gameWorldsImage2,
//					gameWorldsImage3,
//					gameWorldsImage4
//			};
			
//			for (int i = 0; i < worldImageArray.length; i++) {
//				final ArrayList<FilmLocation> currentArrayList = worldLocationArrayList.get(i);
				
//				// System.out.println("CURRENT ARRAY LIST: " + currentArrayList.size());
				gameWorldsImage1.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						defaultLocationArrayList.setFilmList(worldLocationList1);
						// System.out.println("WORLD LOCATION LIST LENGTH: " + worldLocationList1.size());
						
						// start new intent
						Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
//							FilmArrayList finalList = getUpdatedNewsData();
						// System.out.println("LIST LENGTH: " + defaultLocationArrayList.getFilmList().size());
						pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
						pagerActivityIntent.putExtra("localUser", localUser);
						// System.out.println("LOCAL USER POINTS PARCEL: " + localUser.getCurrentPoints());
						// System.out.println("LOCAL USER NOTIFICATIONS PARCEL: " + localUser.getMobileNotifications());
						startActivity(pagerActivityIntent);
//							// System.out.println("clicked button");
					}
				});
//			}
			
				
				
//				// System.out.println("CURRENT ARRAY LIST: " + currentArrayList.size());
				gameWorldsImage2.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						defaultLocationArrayList.setFilmList(worldLocationList2);
						
						
						// start new intent
						Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
//							FilmArrayList finalList = getUpdatedNewsData();
//						// System.out.println("LIST LENGTH: " + defaultLocationArrayList.getFilmList().size());
						pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
						pagerActivityIntent.putExtra("localUser", localUser);
						// System.out.println("LOCAL USER POINTS PARCEL: " + localUser.getCurrentPoints());
						// System.out.println("LOCAL USER NOTIFICATIONS PARCEL: " + localUser.getMobileNotifications());
						startActivity(pagerActivityIntent);
//							// System.out.println("clicked button");
					}
				});
				
				
				
//				// System.out.println("CURRENT ARRAY LIST: " + currentArrayList.size());
				gameWorldsImage3.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						
						defaultLocationArrayList.setFilmList(worldLocationList3);
						
						
						// start new intent
						Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
//							FilmArrayList finalList = getUpdatedNewsData();
//						// System.out.println("LIST LENGTH: " + defaultLocationArrayList.getFilmList().size());
						pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
						pagerActivityIntent.putExtra("localUser", localUser);
						// System.out.println("LOCAL USER POINTS PARCEL: " + localUser.getCurrentPoints());
						// System.out.println("LOCAL USER NOTIFICATIONS PARCEL: " + localUser.getMobileNotifications());
						startActivity(pagerActivityIntent);
//							// System.out.println("clicked button");
					}
				});
				
				
				
//				// System.out.println("CURRENT ARRAY LIST: " + currentArrayList.size());
				gameWorldsImage4.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						defaultLocationArrayList.setFilmList(worldLocationList4);
						
						// start new intent
						Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
//							FilmArrayList finalList = getUpdatedNewsData();
//						// System.out.println("LIST LENGTH: " + defaultLocationArrayList.getFilmList().size());
						pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
						pagerActivityIntent.putExtra("localUser", localUser);
						// System.out.println("LOCAL USER POINTS PARCEL: " + localUser.getCurrentPoints());
						// System.out.println("LOCAL USER NOTIFICATIONS PARCEL: " + localUser.getMobileNotifications());
						startActivity(pagerActivityIntent);
//							// System.out.println("clicked button");
					}
				});

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

	        String mobileNotifications = localUser.getMobileNotifications();
//	        String emailNotifications = localUser.getEmailNotifications();
	        
	        String[] preferenceSettings = {
	        		mobileNotifications
//	        		, emailNotifications
	        };
	        
	        int[] preferenceCheckboxes = { 
	        		R.id.checkboxNotifications1
//	        		, R.id.emailNotifications1
	        };
	        
//	        if (mobileNotifications.equals("true")) {
//	        	
//	        }
//	        if (emailNotifications.equals("true")) {
//	        	 
//	        }
//	        preferenceCheckboxes[0] =  mobile:
//	        preferenceCheckboxes[1] =  R.id.checkboxNotifications1:
	  
	        for (int i = 0; i < preferenceCheckboxes.length; i++) {
	        	final int tempCheckBoxId = preferenceCheckboxes[i];
	        	final CheckBox tempCheckBox = (CheckBox) rootView.findViewById(tempCheckBoxId);
	        	final String tempPreferenceSetting = preferenceSettings[i];
	        	
	        	if (tempPreferenceSetting.equals("true")) {
	        		tempCheckBox.setChecked(true);
	        	} else {
	        		tempCheckBox.setChecked(false);
	        	}
	        }
			
			Button updateUserPreferencesButton = (Button) rootView.findViewById(R.id.updateUserPreferencesButton1);
			
			// bind check-in button click event
			updateUserPreferencesButton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View view) {
					final String FINAL_MOBILE_NOTIFICATIONS = localUser.getMobileNotifications();
//					localUser.setEmailNotifications(FINAL_MOBILE_NOTIFICATIONS);
					final UpdateUserPreferencesTaskRunner runner = new UpdateUserPreferencesTaskRunner();
					runner.execute(FINAL_MOBILE_NOTIFICATIONS);
					// System.out.println("MOBILE NOTIFICTIONS BEFORE: " + FINAL_MOBILE_NOTIFICATIONS);
				}
			});
			return rootView;
		}
		
		class UpdateUserPreferencesTaskRunner extends AsyncTask<String, String, String> {

			private String resp;
			private ProgressDialog dialog;
			private ArrayList<FilmLocation> filmLocationList;
			
			@Override
			protected String doInBackground(String... params) {
				publishProgress("Sleeping..."); // Calls onProgressUpdate()
				try {
						String url = params[0];
						resp = url;
						// System.out.println("NEW USER REGISTERED: " + resp);
				} catch (Exception e) {
					e.printStackTrace();
					// System.out.println("ERROR STACK TRACE");
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
				
				userImpl.open();
				final String FINAL_USER_ID = localUser.getUserId();
				String message = "Something went wrong";
				if (result.equals("true")) {
					userImpl.updateUserNotificationPreferences(FINAL_USER_ID, "false", result);
					message = "Notifications on";
				} else {
					userImpl.updateUserNotificationPreferences(FINAL_USER_ID, "false", "false");
					message = "Notifications off";
				}
				userImpl.close();
				
//				settingsUser = userSource.selectRecordById(FINAL_USER_ID);
				// System.out.println("MOBILE NOTIFICATIONS POST EXECUTE: " + localUser.getMobileNotifications());
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
	}
}
