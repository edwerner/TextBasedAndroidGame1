package com.movie.locations.application;
import java.util.ArrayList;
import java.util.Collections;
import com.movie.locations.R;
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.ConclusionCardImpl;
import com.movie.locations.dao.GameTitleImpl;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.NewsItemImpl;
import com.movie.locations.dao.PointsItemImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.UserImpl;
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
import com.movie.locations.util.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
	private ConclusionCardImpl conclusionCardImpl;
	private MovieLocationsImpl datasource;
	private ArrayList<ConclusionCard> cardList;
	private ConclusionCardArrayAdapter conclusionCardAdapter;
	private BagItemImpl bagItemImpl;
	private UserImpl userSource;
	private PointsItemImpl pointsItemImpl;
	private ArrayList<BagItem> bagItemList;
	
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
		
		
//		GcmIntentService.setCurrentUserId(localUser.getUserId());
		
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
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open
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
			System.out.println("WORLD COUNT NEWS ACTIVITY" + localUser.getWorldCount());
			System.out.println("WORLD COUNT");
			if (localUser.getWorldCount().equals("1")) {
				// display help fragment
				selectItem(5);
//				System.out.println("CURRENT LEVEL: " + localUser.getCurrentLevel());
			}
		} else {
			selectItem(0);
		}
		userSource = new UserImpl(this);
		pointsItemImpl = new PointsItemImpl(this);
		final String FINAL_USER_ID = localUser.getUserId();
		final String FINAL_USER_CURRENT_LEVEL = localUser.getCurrentLevel();
//		final String FINAL_USER_POINTS = localUser.getCurrentPoints();
		final PointsItem FINAL_USER_POINTS_ITEM = pointsItemImpl.selectRecordById(FINAL_USER_ID);
		final String FINAL_USER_POINTS;
		if (FINAL_USER_POINTS_ITEM != null) {
			FINAL_USER_POINTS = FINAL_USER_POINTS_ITEM.getPoints();
			System.out.println("FINAL_USER_POINTS: " + FINAL_USER_POINTS);
			localUser.setCurrentPoints(FINAL_USER_POINTS);
		} else {
			FINAL_USER_POINTS = "0";
			localUser.setCurrentPoints(FINAL_USER_POINTS);
		}
		
		final String FINAL_USER_WORLD_COUNT = localUser.getWorldCount();
		userSource = new UserImpl(this);
		userSource.updateWorldCount(FINAL_USER_ID, FINAL_USER_WORLD_COUNT);
		System.out.println("WORLD COUNT LOGGING: " + FINAL_USER_WORLD_COUNT);
		userSource.updateCurrentUserLevel(FINAL_USER_ID, FINAL_USER_CURRENT_LEVEL);
		datasource = new MovieLocationsImpl(this);
		conclusionCardImpl = new ConclusionCardImpl(this);
		cardList = conclusionCardImpl.selectRecords();

		// DEFAULTS TO NEWS PAGE
		bagItemImpl = new BagItemImpl(this);

		// BAG ITEM NEWS ITEM
		bagItemList = bagItemImpl.selectRecords();
		System.out.println("BAG ITEM LIST SIZE: " + bagItemList.size());
		ArrayList<FilmLocation> tempLocationList = datasource.selectRecords();
		System.out.println("START NEWS ACTIVITY LOCATION LIST LENGTH: " + tempLocationList.size());
//		FilmArrayList tempLocationArrayList = new FilmArrayList();
//		tempLocationArrayList.setFilmList(tempLocationList);
//		setUpdatedNewsData(tempLocationArrayList);
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
				System.out.println("Mobile notifications checked");
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
		
//		quizItemList = quizItemImpl.selectRecords();
		cardList = conclusionCardImpl.selectRecords();

		// BAG ITEM NEWS ITEM
		bagItemList = bagItemImpl.selectRecords();
	}
	 
	private void selectItem(int position) {

		// TODO: write references to activities

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(navArray[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
        ft = getSupportFragmentManager().beginTransaction();
        
		switch (position) {
		case 0:
			ft.replace(R.id.content_frame, new NewsFragment());
			break;
		case 1:
			Fragment userFragment = new UserDetailFragment();
			Bundle userBundle = new Bundle();
			userBundle.putParcelable("localUser", localUser);
			userFragment.setArguments(userBundle);
			ft.replace(R.id.content_frame, userFragment);
			break;
		case 2:
			Fragment locationsFragment = new FilmLocationsFragment();
			Bundle locationsBundle = new Bundle();
			locationsBundle.putParcelable("localUser", localUser);
			locationsFragment.setArguments(locationsBundle);
			ft.replace(R.id.content_frame, locationsFragment);
			break;
		case 3:
			ft.replace(R.id.content_frame, new ConclusionCardFragment());
			break;
		case 4:
			ft.replace(R.id.content_frame, new BagItemListFragment());
			break;
		case 5:
			ft.replace(R.id.content_frame, new AboutFragment());
			break;
		case 6:
			ft.replace(R.id.content_frame, new SettingsFragment());
			break;
		}
		ft.commit();

		System.out.println("nav item " + position + " clicked");
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
					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecords();
					
					for (GameTitle tempTitle : gameTitleList) {
						System.out.println("CARD DATABASE TITLE: " + tempTitle.getTitle());
					}
					final Context bagContext = getActivity().getApplicationContext();
					bagItemImpl = new BagItemImpl(bagContext);
					bagItemList = bagItemImpl.selectRecords();
					System.out.println("REFRESHED DATA");
					
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
								BagItem existingBagItem = bagItemList.get(i);
								tempTitle.setPhase("EXISTS");
								String tempImageUrl = existingBagItem.getImageUrl();
								tempTitle.setImageUrl(tempImageUrl);
								tempTitle.setLevel(existingBagItem.getLevel());
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
					cardList = conclusionCardImpl.selectRecords();
					final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreConclusionCardDataListView1);
					final Context context = getActivity().getApplicationContext();
					GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
					String CARD_TITLE = "CARD_TITLE";
					final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(CARD_TITLE);
					for (GameTitle tempTitle : gameTitleList) {
						System.out.println("CARD DATABASE TITLE: " + tempTitle.getTitle());
					}
					System.out.println("REFRESHED DATA");

					final Context quizContext = getActivity().getApplicationContext();
					QuizItemImpl quizItemImpl = new QuizItemImpl(quizContext);
					// sort the list
					Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
					Collections.sort(cardList, StaticSortingUtilities.CARD_TITLES_ALPHABETICAL_ORDER);
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
					final Intent intent = getActivity().getIntent();
					final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
					
					if (gameTitleList.size() > 0) {
						restoreListView.setAdapter(levelRestoreListAdapter);
						levelRestoreListAdapter.notifyDataSetChanged();
					}
			return rootView;
		}
	}
		
	// /**
	// * A placeholder fragment containing a simple view.
	// */
	public class NewsFragment extends Fragment {
		
		private ArrayList<NewsItem> newsUpdateList;
		private String QUIZ_ITEM_NEWS_ITEM_IMAGE_URL = "http://www.phonandroid.com/forum/download/file.php?avatar=218224_1342902645.jpg";
		
		public NewsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_news, container, false);
			final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreNewsItemDataListView1);
			final Context context = getActivity().getApplicationContext();
			GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
			String NEWS_ITEM_TITLE = "NEWS_ITEM_TITLE";
			final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(NEWS_ITEM_TITLE);
			for (GameTitle tempTitle : gameTitleList) {
				System.out.println("CARD DATABASE TITLE: " + tempTitle.getTitle());
			}
			System.out.println("REFRESHED DATA");
			NewsItemImpl newsItemImpl = new NewsItemImpl(context);
			ArrayList<NewsItem> tempNewsItemList = newsItemImpl.selectRecords();
			
			// sort the list
			Collections.sort(gameTitleList, StaticSortingUtilities.GAME_TITLES_ALPHABETICAL_ORDER);
			Collections.sort(tempNewsItemList, StaticSortingUtilities.NEWS_ITEM_TITLES_ALPHABETICAL_ORDER);
			
			if (gameTitleList.size() > 0) {				
				// ITERATE THROUGH COLLECTION AND GATHER
				// ALL TITLES AND HIDE EXISTING TITLE RESTORE
				for (int i = 0; i < tempNewsItemList.size(); i++) {
					GameTitle tempTitle = gameTitleList.get(i);
					String currentTitleString = tempTitle.getTitle();
					NewsItem existingBagItem = tempNewsItemList.get(i);
					String currentCardTitle = existingBagItem.getTitle();
					if (!currentTitleString.equals(currentCardTitle)) {
						System.out.println("LOGGING CURRENT TITLE TYPE: " + tempTitle.getType() + " " + i);
						System.out.println("LOGGING NEWS ITEM TITLE TYPE: " + existingBagItem.getNewsType() + " " + i);
						// CARD IS MISSING
						tempTitle.setPhase("MISSING");
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
			final Intent intent = getActivity().getIntent();
			final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
			if (gameTitleList.size() > 0) {
				restoreListView.setAdapter(levelRestoreListAdapter);
			}
			ListView commentView = (ListView) rootView.findViewById(R.id.commentView);
			newsUpdateList = new ArrayList<NewsItem>();
			NewsItemAdapter newsItemAdapter = new NewsItemAdapter();
			
			BagItem latestBagItem = null;
			final Context bagContext = getActivity().getApplicationContext();
			BagItemImpl bagItemImpl = new BagItemImpl(bagContext);
			ArrayList<BagItem> bagItemList = bagItemImpl.selectRecords();
			if (bagItemList.size() > 0) {
				int latestBagItemIndex = bagItemList.size() - 1;
				latestBagItem = bagItemList.get(latestBagItemIndex);
				newsItemAdapter.setLatestBagItem(latestBagItem);	
			}
			// CONCLUSION CARD NEWS ITEM
			final Context cardContext = getActivity().getApplicationContext();
			ConclusionCardImpl conclusionCardImpl = new ConclusionCardImpl(cardContext);
			ArrayList<ConclusionCard> localConclusionCardList = conclusionCardImpl.selectRecords();
			ConclusionCard latestConclusionCard = null;
			if (localConclusionCardList.size() > 0) {
				int latestConclusionCardIndex = localConclusionCardList.size() - 1;
				latestConclusionCard = localConclusionCardList.get(latestConclusionCardIndex);
				newsItemAdapter.setLatestConclusionCard(latestConclusionCard);	
			}
			// QUIZ ITEM NEWS ITEM
			QuizItemImpl quizItemImpl = new QuizItemImpl(context);
			ArrayList<QuizItem> quizItemList = quizItemImpl.selectRecords();
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
			NewsArrayAdapter adapter = new NewsArrayAdapter(getActivity(), intent, newsUpdateList);
			if (newsUpdateList.size() > 0) {
				commentView.setAdapter(adapter);
			}
			return rootView;
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class UserDetailFragment extends Fragment {

		private PointsItemImpl pointsItemImpl;
		
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
			System.out.println("localUser.getDisplayName(): " + localUser.getDisplayName());
			System.out.println("userText: " + userText);
			userText.setText(localUser.getDisplayName());
			
//			currentLevelText.setText(localUser.getCurrentLevel());
			
			String currentPoints = localUser.getPoints();
			System.out.println("localUser.getPoints: " + currentPoints);			
			final String FINAL_USER_ID = localUser.getUserId();
			final String FINAL_CURRENT_USER_LEVEL = localUser.getCurrentLevel();
			
			final Context pointsContext = getActivity().getApplicationContext();
			pointsItemImpl = new PointsItemImpl(pointsContext);

			final PointsItem FINAL_USER_POINTS_ITEM = pointsItemImpl.selectRecordById(FINAL_USER_ID);
			final String FINAL_USER_POINTS;
			if (FINAL_USER_POINTS_ITEM != null) {
				FINAL_USER_POINTS = FINAL_USER_POINTS_ITEM.getPoints();
				System.out.println("FINAL_USER_POINTS: " + FINAL_USER_POINTS);
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
			TextView locationsTitle = (TextView) rootView.findViewById(R.id.locationsTag1);
//			TextView locationsText = (TextView) rootView.findViewById(R.id.locationsText);

			locationsTitle.setText("Film Locations");
//			locationsText.setText("Get started!");
			
			Button getStartedButton = (Button) rootView.findViewById(R.id.get_started_button);
			final Context context = getActivity().getApplicationContext();
			final MovieLocationsImpl datasource = new MovieLocationsImpl(context);
			final ArrayList<FilmLocation> defaultLocationList = datasource.selectRecords();
			System.out.println("DEFAULT LOCATION LIST :" +  defaultLocationList);
			final FilmArrayList defaultLocationArrayList = new FilmArrayList();
			defaultLocationArrayList.setFilmList(defaultLocationList);
			
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
						System.out.println("LOCAL USER NOTIFICATIONS PARCEL: " + localUser.getMobileNotifications());
						startActivity(pagerActivityIntent);
//						System.out.println("clicked button");
					}
				});	
			}
			final ListView restoreListView = (ListView) rootView.findViewById(R.id.restoreLevelDataListView);
			GameTitleImpl gameTitleImpl = new GameTitleImpl(context); 
			String WORLD_TITLE = "WORLD_TITLE";
			final ArrayList<GameTitle> gameTitleList = gameTitleImpl.selectRecordsByType(WORLD_TITLE);
			System.out.println("REFRESHED DATA");
			final Intent intent = getActivity().getIntent();
			final GameTitleArrayAdapter levelRestoreListAdapter = new GameTitleArrayAdapter(getActivity(), intent, gameTitleList);
			if (gameTitleList.size() > 0) {
				restoreListView.setAdapter(levelRestoreListAdapter);
//				levelRestoreListAdapter.notifyDataSetChanged();
			}
			return rootView;
		}
	}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class AchievementsFragment extends Fragment {

		private ArrayList<Achievement> achievementList;
		
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
			for (int i = 0; i < 10; i++) {
				comment.setAchievementId("achievementId");
				comment.setTitle("title");
//				comment.setCatchPhrase("catchPhrase");
				comment.setUserId("userId");
				comment.setDateTime("dateTime");
				achievementList.add(comment);	
			}
			final Intent intent = getActivity().getIntent();
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
					System.out.println("MOBILE NOTIFICTIONS BEFORE: " + FINAL_MOBILE_NOTIFICATIONS);
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
						System.out.println("NEW USER REGISTERED: " + resp);
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
				final String FINAL_USER_ID = localUser.getUserId();
				String message = "Something went wrong";
				if (result.equals("true")) {
					userSource.updateUserNotificationPreferences(FINAL_USER_ID, "false", result);
					message = "Notifications on";
				} else {
					userSource.updateUserNotificationPreferences(FINAL_USER_ID, "false", "false");
					message = "Notifications off";
				}
//				settingsUser = userSource.selectRecordById(FINAL_USER_ID);
				System.out.println("MOBILE NOTIFICATIONS POST EXECUTE: " + localUser.getMobileNotifications());
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
