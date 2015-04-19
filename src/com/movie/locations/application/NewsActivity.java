package com.movie.locations.application;

import java.util.ArrayList;
import java.util.Collections;
import com.movie.locations.R;
import com.movie.locations.adapter.GameTitleArrayAdapter;
import com.movie.locations.adapter.NavMenuItemArrayAdapter;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.NavMenuItem;
import com.movie.locations.domain.User;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.service.AchievementService;
import com.movie.locations.service.BagItemService;
import com.movie.locations.service.GameTitleService;
import com.movie.locations.service.LocationService;
import com.movie.locations.service.UserService;
import com.movie.locations.utility.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
	private UserService userService;
	private GameTitleService gameTitleService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_news);

		if (savedInstanceState == null) {
			context = this;
			intent = getIntent();
			Bundle bundle = intent.getExtras();
			localUser = bundle.getParcelable("localUser");
			Fragment locationsFragment = new FilmLocationsFragment();
			Bundle locationsBundle = new Bundle();
			locationsBundle.putParcelable("localUser", localUser);
			locationsFragment.setArguments(locationsBundle);
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, locationsFragment).commit();

			userService = new UserService(context);
			userService.createUserImpl();
			
			gameTitleService = new GameTitleService(context);
			gameTitleService.createGameTitleImpl();
			
			navArray = new String[6];
			navArray[0] = "Player";
			navArray[1] = "Achievements";
			navArray[2] = "Inventory";
			navArray[3] = "Worlds";
			navArray[4] = "About";
			navArray[5] = "Settings";
			
			String userImageUrl = localUser.getAvatarImageUrl();
			mTitle = mDrawerTitle = getTitle();
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.left_drawer);
	
			// set a custom shadow that overlays the main content when the drawer opens
			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
			
			// set up the drawer's list view with items and click listener
			ArrayList<NavMenuItem> navList = new ArrayList<NavMenuItem>();
			
			for (int a = 0; a < navArray.length; a++) {
				NavMenuItem menuItem = new NavMenuItem();
				String currentImageUrl = "drawable://";
				
				switch(a) {
				case 0:
					currentImageUrl = userImageUrl;
					break;
				case 1:					
					currentImageUrl += R.drawable.green_checkmark_lg;
					break;
				case 2:
					currentImageUrl += R.drawable.backpack_blue;
					break;
				case 3:
					currentImageUrl += R.drawable.breadboard_menu;
					break;
				case 4:
					currentImageUrl += R.drawable.help;
					break;
				case 5:
					currentImageUrl += R.drawable.settings;
					break;
				}
				
				menuItem.setTitle(navArray[a]);
				menuItem.setImageUrl(currentImageUrl);
				navList.add(menuItem);
			}
			
			NavMenuItemArrayAdapter menuAdapter = new NavMenuItemArrayAdapter(this, intent, navList);
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
			
			if (localUser != null) {
				if (localUser.getCurrentLevel().equals("1")) {
					// display help fragment
					selectItem(4);
				}
			} else {
				selectItem(3);
			}
		}
	}

	 @Override
	 public void onResume() {
		 String userId = localUser.getUserId();
		 localUser = userService.selectRecordById(userId);
		 super.onResume();
	 }

    public void onCheckboxClicked(View view) {
        // Check for selected checkbox
        boolean checked = ((CheckBox) view).isChecked();
        
        int id = view.getId();
		if (id == R.id.checkboxNotifications1) {
			if (checked) {
				localUser.setMobileNotifications("true");
			} else {
				localUser.setMobileNotifications("false");
			}
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// If the nav drawer is open, hide action items related to the content
		// view
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}
	 
	private void selectItem(int position) {
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(navArray[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
        ft = getSupportFragmentManager().beginTransaction();

		switch (position) {
		case 0:
			Fragment userFragment = new UserDetailFragment();
			Bundle userBundle = new Bundle();
			userBundle.putParcelable("localUser", localUser);
			userFragment.setArguments(userBundle);
			ft.replace(R.id.content_frame, userFragment);
			break;
		case 1:
			Fragment achievementsFragment = new AchievementsFragment();
			Bundle achievementBundle = new Bundle();
			achievementBundle.putParcelable("localUser", localUser);
			achievementsFragment.setArguments(achievementBundle);
			ft.replace(R.id.content_frame, achievementsFragment);
			break;
		case 2:
			ft.replace(R.id.content_frame, new BagItemListFragment());
			break;
		case 3:
			Fragment locationsFragment = new FilmLocationsFragment();
			Bundle locationsBundle = new Bundle();
			locationsBundle.putParcelable("localUser", localUser);
			locationsFragment.setArguments(locationsBundle);
			ft.replace(R.id.content_frame, locationsFragment);
			break;
		case 4:
			ft.replace(R.id.content_frame, new AboutFragment());
			break;
		case 5:
			ft.replace(R.id.content_frame, new SettingsFragment());
			break;
		}
		ft.commit();
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
	
	private class BagItemListFragment extends Fragment {

		public BagItemListFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_bag_item_list, container, false);
			ListView restoreListView = (ListView) rootView.findViewById(R.id.gameTitleListView);

			Context bagContext = getActivity().getApplicationContext();
			
			ArrayList<GameTitle> gameTitleList = gameTitleService.selectRecordsByType("BAG_ITEM_TITLE");
			
			BagItemService bagItemService = new BagItemService(bagContext);
			bagItemService.createBagItemImpl();
			ArrayList<BagItem> bagItemList = bagItemService.selectRecords();
			
			// sort the list
			Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
			Collections.sort(bagItemList, StaticSortingUtilities.BAG_ITEMS_ALPHABETICAL_ORDER);
			
			// ITERATE THROUGH NODES AND UPDATE NON-MISSING STATES
			if (gameTitleList.size() > 0) {
				if (bagItemList.size() > 0) {
					for (int i = 0; i < bagItemList.size(); i++) {
						GameTitle tempTitle = gameTitleList.get(i);
						String currentTitleString = tempTitle.getTitle();
						BagItem existingBagItem = bagItemList.get(i);
						String currentCardTitle = existingBagItem.getItemTitle();
						if (currentTitleString.equals(currentCardTitle)) {
							tempTitle.setPhase("EXISTS");
							String tempImageUrl = existingBagItem.getImageUrl();
							tempTitle.setTitle(currentCardTitle);
							tempTitle.setImageUrl(tempImageUrl);
							tempTitle.setLevel(null);
							tempTitle.setDescription(existingBagItem.getDescription());
							tempTitle.setType("conclusion");
						} else {
							tempTitle.setPhase("MISSING");
						}
						gameTitleList.set(i, tempTitle);
					}	
				}
			}
			
			Intent intent = getActivity().getIntent();
			GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
			
			if (gameTitleList.size() > 0) {
				restoreListView.setAdapter(levelRestoreListAdapter);
				levelRestoreListAdapter.notifyDataSetChanged();
			}			
			return rootView;
		}
	}
	
	private class AchievementsFragment extends Fragment {

		public AchievementsFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_achievement_item_list, container, false);
			User localUser = getArguments().getParcelable("localUser");
			
			ImageView achievementImage = (ImageView) rootView.findViewById(R.id.achievementImage);
			achievementImage.setBackgroundResource(R.drawable.green_checkmark_lg);

			ListView restoreListView = (ListView) rootView.findViewById(R.id.gameTitleListView);
			Context achievementContext = getActivity().getApplicationContext();
			
			String currentUserLevel = localUser.getCurrentLevel();
			ArrayList<GameTitle> gameTitleList = gameTitleService.selectRecordsByTypeAndLevel("ACHIEVEMENT_TITLE", currentUserLevel);

			AchievementService achievementService = new AchievementService(achievementContext);
			achievementService.createAchievementImpl();
			ArrayList<Achievement> achievementList = achievementService.selectRecordsByLevel(currentUserLevel);

			// sort the list
			Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
			Collections.sort(achievementList, StaticSortingUtilities.ACHIEVEMENTS_ALPHABETICAL_ORDER);
			
			if (gameTitleList.size() > 0) {
				if (achievementList.size() > 0) {
					for (int i = 0; i < achievementList.size(); i++) {
						GameTitle tempTitle = gameTitleList.get(i);
						String currentTitleString = tempTitle.getTitle();
						Achievement existingAchievement = achievementList.get(i);
						String currentCardTitle = existingAchievement.getTitle();
						if (currentTitleString.equals(currentCardTitle)) {
							tempTitle.setPhase("EXISTS");
							String tempImageUrl = existingAchievement.getImageUrl();
							tempTitle.setTitle(currentCardTitle);
							tempTitle.setImageUrl(tempImageUrl);
							tempTitle.setLevel(existingAchievement.getLevel());
							tempTitle.setDescription(existingAchievement.getDescription());
							tempTitle.setType("achievement");
						} else {
							tempTitle.setPhase("MISSING");
						}
						gameTitleList.set(i, tempTitle);
					}	
				}
			}
			
			Intent intent = getActivity().getIntent();
			GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
			
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
	private class UserDetailFragment extends Fragment {
		
		public UserDetailFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_user_detail,
					container, false);

			ImageView userAvatar = (ImageView) rootView.findViewById(R.id.userAvatar1);
			User localUser = getArguments().getParcelable("localUser");
			imageLoader.displayImage(localUser.getAvatarImageUrl(), userAvatar);
			TextView userText = (TextView) rootView.findViewById(R.id.userFullName1);
			TextView currentLevelText = (TextView) rootView.findViewById(R.id.levelText1);
			TextView pointsText = (TextView) rootView.findViewById(R.id.pointsText1);
			userText.setText(localUser.getDisplayName());
			String FINAL_CURRENT_USER_LEVEL = localUser.getCurrentLevel();
			String USER_POINTS = localUser.getCurrentPoints();
			localUser.setCurrentPoints(USER_POINTS);
			int FINAL_USER_POINTS_INT = Integer.parseInt(USER_POINTS);
			int currentLevel = StaticSortingUtilities.CHECK_LEVEL_RANGE(FINAL_CURRENT_USER_LEVEL, FINAL_USER_POINTS_INT);
			String CURRENT_LEVEL_STRING = Integer.toString(currentLevel); 
			currentLevelText.setText(CURRENT_LEVEL_STRING);
			int[] levelRange = StaticSortingUtilities.getLevelRange();
			int nextLevelIndex = Integer.parseInt(localUser.getCurrentLevel()) + 1;
			int finalLevelCap = levelRange[nextLevelIndex];
			String currentPointsString = USER_POINTS + "/" + Integer.toString(finalLevelCap);
			pointsText.setText(currentPointsString);
			return rootView;
		}
	}
		

	/**
	 * A placeholder fragment containing a simple view.
	 */
	private class FilmLocationsFragment extends Fragment {

		public FilmLocationsFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_film_locations,
					container, false);
			
			final User localUser = getArguments().getParcelable("localUser");
			ImageView gameWorldsImage1 = (ImageView) rootView.findViewById(R.id.gameWorldsImage1);
			ImageView gameWorldsImage2 = (ImageView) rootView.findViewById(R.id.gameWorldsImage2);
			ImageView gameWorldsImage3 = (ImageView) rootView.findViewById(R.id.gameWorldsImage3);
			ImageView gameWorldsImage4 = (ImageView) rootView.findViewById(R.id.gameWorldsImage4);
			
			Context locationsContext = getActivity().getApplicationContext();
			
			LocationService locationService = new LocationService(locationsContext);
			locationService.createLocationsImpl();
			ArrayList<FilmLocation> locationsArrayList = locationService.selectRecords();
			
			final FilmArrayList defaultLocationArrayList = new FilmArrayList();
			final ArrayList<FilmLocation> worldLocationList1 = new ArrayList<FilmLocation>();
			final ArrayList<FilmLocation> worldLocationList2 = new ArrayList<FilmLocation>();
			final ArrayList<FilmLocation> worldLocationList3 = new ArrayList<FilmLocation>();
			final ArrayList<FilmLocation> worldLocationList4 = new ArrayList<FilmLocation>();
			
			for (int i = 0; i < locationsArrayList.size(); i++) {
				FilmLocation tempLocation = locationsArrayList.get(i);
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
			
			gameWorldsImage1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					defaultLocationArrayList.setFilmList(worldLocationList1);
					
					// start new intent
					Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
					pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
					pagerActivityIntent.putExtra("localUser", localUser);
					startActivity(pagerActivityIntent);
				}
			});
			
			gameWorldsImage2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					defaultLocationArrayList.setFilmList(worldLocationList2);
									
					// start new intent
					Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
					pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
					pagerActivityIntent.putExtra("localUser", localUser);
					startActivity(pagerActivityIntent);
				}
			});
			
			gameWorldsImage3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {		
					defaultLocationArrayList.setFilmList(worldLocationList3);
					
					// start new intent
					Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
					pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
					pagerActivityIntent.putExtra("localUser", localUser);
					startActivity(pagerActivityIntent);
				}
			});
			
			gameWorldsImage4.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					defaultLocationArrayList.setFilmList(worldLocationList4);
					
					// start new intent
					Intent pagerActivityIntent = new Intent(getActivity(), LocationPagerActivity.class);
					pagerActivityIntent.putExtra("locationArrayList", defaultLocationArrayList);
					pagerActivityIntent.putExtra("localUser", localUser);
					startActivity(pagerActivityIntent);
				}
			});

			return rootView;
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	private class AboutFragment extends Fragment {

		public AboutFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_about, container, false);
			Button instructionsButton = (Button) rootView.findViewById(R.id.dismiss_instructions);
			
			// bind check-in button click event
			instructionsButton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View view) {
					// change fragments
					selectItem(3);
					mDrawerLayout.openDrawer(mDrawerList);
				}
			});
			return rootView;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	private class SettingsFragment extends Fragment {

		public SettingsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
	        String mobileNotifications = localUser.getMobileNotifications();
	        String[] preferenceSettings = {
	        		mobileNotifications
	        };
	        int[] preferenceCheckboxes = { 
	        		R.id.checkboxNotifications1
	        };
	  
	        for (int i = 0; i < preferenceCheckboxes.length; i++) {
	        	int tempCheckBoxId = preferenceCheckboxes[i];
	        	CheckBox tempCheckBox = (CheckBox) rootView.findViewById(tempCheckBoxId);
	        	String tempPreferenceSetting = preferenceSettings[i];
	        	
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
					String FINAL_MOBILE_NOTIFICATIONS = localUser.getMobileNotifications();
					
					String FINAL_USER_ID = localUser.getUserId();
					String message = "Something went wrong";
					
					if (FINAL_MOBILE_NOTIFICATIONS.equals("true")) {
						message = "Notifications on";
					} else {
						message = "Notifications off";
					}

					userService.updateUserNotificationPreferences(FINAL_USER_ID, "false", FINAL_MOBILE_NOTIFICATIONS);
					
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}
			});
			
			return rootView;
		}
	}
}