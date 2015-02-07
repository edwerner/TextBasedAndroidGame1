package com.movie.locations.application;

import java.util.ArrayList;
import java.util.Collections;
import com.movie.locations.adapter.LocationQuizArrayAdapter;
import com.movie.locations.application.QuizActivity;
import com.movie.locations.R;
import com.movie.locations.database.AchievementImpl;
import com.movie.locations.database.ConclusionCardImpl;
import com.movie.locations.database.UserImpl;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.QuizItemArrayList;
import com.movie.locations.domain.User;
import com.movie.locations.receiver.DatabaseChangedReceiver;
import com.movie.locations.service.QuizItemService;
import com.movie.locations.service.UserService;
import com.movie.locations.utility.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LocationDetailActivity extends ActionBarActivity implements TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private String title;
	private QuizItem quizItem;
	private User currentUser;
	private BagItemArrayList bagItemArrayList;
	private FilmArrayList locationArrayList;
	private FilmLocation currentLocation;
	private UserImpl userImpl;
	private QuizItemArrayList localQuizItemArrayList;
	private IntentFilter filter;
//	private PointsItemImpl pointsItemImpl;
	private AchievementImpl achievementImpl;
	private Achievement levelAchievement;
	private Context context;
	private ArrayList<QuizItem> newQuizList;
	private Dialog dialog;
	private LocationQuizArrayAdapter locationQuizArrayAdapter;
	private ListView locationsList;
	private QuizItemService quizItemService;
	private UserService userService;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_film_location_detail);

		if (savedInstanceState == null) {
			// Set up the action bar.
//			final ActionBar actionBar = getActionBar();
			getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			// Create the adapter that will return a fragment for each of the three
			// primary sections of the app.
			mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			locationArrayList = bundle.getParcelable("locationArrayList");
			bagItemArrayList = bundle.getParcelable("bagItemArrayList");
			currentUser = bundle.getParcelable("localUser");
//			System.out.println("CURRENT USER LEVEL DETAIL ACTIVITY: " + currentUser.getCurrentLevel());
			currentLocation = bundle.getParcelable("currentLocation");
			title = currentLocation.getTitle();
			intent.putExtra("quizItemSid", currentUser.getUserSid());
			
			quizItemService = new QuizItemService(context);
			quizItemService.createQuizItemImpl();
			newQuizList = quizItemService.selectRecords();
			
			userService = new UserService(context);
			userService.createUserImpl();
			
			localQuizItemArrayList = new QuizItemArrayList();
			localQuizItemArrayList.setQuizList(newQuizList);
			intent.putExtra("localQuizItemArrayList", localQuizItemArrayList);
//			pointsItemImpl = new PointsItemImpl(context);
			achievementImpl = new AchievementImpl(context);
			String currentUserLevelString = currentUser.getCurrentLevel();
			int currentLevelInt = Integer.parseInt(currentUserLevelString);
			int nextLevelInt = currentLevelInt + 1;
			String nextLevel = Integer.toString(nextLevelInt);
			
			achievementImpl.open();
			levelAchievement = achievementImpl.selectRecordByLevel(nextLevel);
			achievementImpl.close();

			userImpl = new UserImpl(context);
			
			// set world title
			setTitle(currentLocation.getTitle());
			dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		 	dialog.setContentView(R.layout.replay_level_overlay);
		 	filter = new IntentFilter();
			filter.addAction(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
			filter.addCategory(Intent.CATEGORY_DEFAULT);
			registerReceiver(mReceiver, filter);	
		}
	}

	 @Override
	 public void onResume() {
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
		 }

//		 userImpl.open();
//		 String userId = currentUser.getUserId();
//		 currentUser = userImpl.selectRecordById(userId);
//		 userImpl.close();
		
		 registerReceiver(mReceiver, filter);
		 super.onResume();
	 }

	 private void initializeReplayWorld(final QuizItem updatedQuizItem) {
		 
	 	RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlayLayout);
	 	layout.setOnClickListener(new OnClickListener() {
		 	@Override
	 		public void onClick(View arg0) {
			 		if (updatedQuizItem.getAnswered().equals("true")) {
			 			String updatedQuizItemId = updatedQuizItem.getQuestionId();
			 			
			 			// RESET AND PERSIST QUIZ STATE
			 			RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
						runner.execute(updatedQuizItemId);
						
			 			dialog.dismiss();
			 		}
	 			}
	 		});
	 		dialog.show();
	 }

	 private DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
			
		public void onReceive(Context context, Intent intent) {

			String userId = currentUser.getUserId();
			
			Bundle extras = intent.getExtras();
			QuizItem updatedQuizItem = extras.getParcelable("updatedQuizItem");
			
			for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
				final QuizItem item = locationQuizArrayAdapter.getItem(i);
				if (updatedQuizItem.getWorldId().equals(item.getWorldId())) {
					locationQuizArrayAdapter.remove(item);
					locationQuizArrayAdapter.insert(updatedQuizItem, i);
				}
			}
			
			// UPDATE USER POINTS
			String quizItemPointValue = updatedQuizItem.getPointValue();
			int quizItemPointValueInt = Integer.parseInt(quizItemPointValue);
			
			User tempUser = userService.selectRecordById(userId);
			String databasePoints = tempUser.getCurrentPoints();
			
			int databasePointsInt = Integer.parseInt(databasePoints);
			int FINAL_USER_POINTS_INT = databasePointsInt - quizItemPointValueInt;
			String updatedUserPointsString = Integer.toString(FINAL_USER_POINTS_INT);					
			String CURRENT_USER_LEVEL = currentUser.getCurrentLevel();
			int FINAL_CURRENT_USER_LEVEL_INT = Integer.parseInt(CURRENT_USER_LEVEL);
			int currentLevel = StaticSortingUtilities.CHECK_LEVEL_RANGE(CURRENT_USER_LEVEL, FINAL_USER_POINTS_INT);
			if (currentLevel < FINAL_CURRENT_USER_LEVEL_INT) {
				int previousLevel = FINAL_CURRENT_USER_LEVEL_INT - 1;
				String currentLevelString = Integer.toString(previousLevel);
				userService.setCurrentLevel(userId, currentLevelString);
				currentUser.setCurrentLevel(currentLevelString);
			}
			
			userService.setCurrentPoints(userId, updatedUserPointsString);

			// RE-DRAW VIEW WITH UPDATED COLLECTION
			locationQuizArrayAdapter.notifyDataSetChanged();

			unregisterReceiver(this);
	   }
	};

	private class RestoreLevelDataTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
		private ProgressDialog dialog;
		
		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping...");
			try {
				resp = params[0];
			} catch (Exception e) {
				e.printStackTrace();
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
			if (result != null) {
				message = "Level data reset.";
				quizItemService.resetAnsweredQuestion(result);
			} else {
				message = "Something went wrong!";
			}
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
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		mViewPager.setCurrentItem(arg0.getPosition());	
	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		private int pagesLength = 2;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			LocationFragment locationFragment = new LocationFragment();
			Bundle args = new Bundle();
			int fragmentIndex = position + 1;
			String ARG_SECTION_NUMBER = locationFragment.getArgSectionNumber();
			args.putInt(ARG_SECTION_NUMBER, fragmentIndex);
			args.putParcelable("localQuizItemArrayList", localQuizItemArrayList);
			args.putParcelable("localCurrentLocation", currentLocation);
			args.putParcelable("fragmentUser", currentUser);
			args.putParcelable("levelAchievement", levelAchievement);
			args.putString("title", title);
			args.putParcelable("quizItem", quizItem);
			args.putParcelable("bagItemArrayList", bagItemArrayList);
			args.putParcelable("locationArrayList", locationArrayList);
			locationFragment.setArguments(args);
			
			return locationFragment;
		}

		@Override
		public int getCount() {
			// Show total pages.
			return pagesLength;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "fun fact";
			case 1:
				return "quiz";
			}
			return null;
		}
	}

	/**
	 * A Location fragment containing a simple view.
	 */
	private class LocationFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private final String ARG_SECTION_NUMBER = "section_number";
		protected ImageLoader imageLoader = ImageLoader.getInstance();
		private QuizItem currentQuizItem;
		private FilmLocation localCurrentLocation;
		private BagItemArrayList bagItemArrayList;
		private FilmArrayList locationArrayList;
		private ConclusionCardImpl conclusionCardImpl;
		
		public LocationFragment() {
			// Empty constructor
		}
		
		public String getArgSectionNumber() {
			return ARG_SECTION_NUMBER;
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {

			if (requestCode == 1) {

				String updatedUserPointsString = null;
				
				if (resultCode == RESULT_OK) {
					currentQuizItem = data.getExtras().getParcelable("quizItem");
					
					// UPDATE USER POINTS
					String quizItemPointValue = currentQuizItem.getPointValue();
					int quizItemPointValueInt = Integer.parseInt(quizItemPointValue);
					String userId = currentUser.getUserId();

					User tempUser = userService.selectRecordById(userId);
					String databasePoints = tempUser.getCurrentPoints();
					
					int databasePointsInt = Integer.parseInt(databasePoints);
					final int FINAL_USER_POINTS_INT = quizItemPointValueInt + databasePointsInt;
					updatedUserPointsString = Integer.toString(FINAL_USER_POINTS_INT);
					userService.setCurrentPoints(userId, updatedUserPointsString);						
					String FINAL_CURRENT_USER_LEVEL = currentUser.getCurrentLevel();
					int FINAL_CURRENT_USER_LEVEL_INT = Integer.parseInt(FINAL_CURRENT_USER_LEVEL);
					int currentLevel = StaticSortingUtilities.CHECK_LEVEL_RANGE(FINAL_CURRENT_USER_LEVEL, FINAL_USER_POINTS_INT);
					if (currentLevel > FINAL_CURRENT_USER_LEVEL_INT) {
						String currentLevelString = Integer.toString(currentLevel);
						levelUpCurrentUser(userId, currentLevelString);
					}

					for (int i = 0; i < newQuizList.size(); i++) {
						if (newQuizList.get(i).equals(currentQuizItem.getQuestionId())) {
							newQuizList.set(i, currentQuizItem);
						}
					}

					for (int i = 0; i < locationQuizArrayAdapter.getCount(); i++) {
						final QuizItem item = locationQuizArrayAdapter.getItem(i);
						if (currentQuizItem.getWorldId().equals(item.getWorldId())) {
							locationQuizArrayAdapter.remove(item);
							locationQuizArrayAdapter.insert(currentQuizItem, i);
						}
					}
	
					quizItemService.updateRecordAnswered(currentQuizItem.getQuestionId(),
							currentQuizItem.getAnswered());
					quizItemService.updateRecordCorrectAnswerIndex(currentQuizItem.getQuestionId(), 
							currentQuizItem.getCorrectAnswerIndex());
					
					generateConclusionCard(currentQuizItem, updatedUserPointsString);
					locationQuizArrayAdapter.notifyDataSetChanged();
				} else if (resultCode == RESULT_CANCELED) {
					// reset the current quiz item
					currentQuizItem = null;
				}
			}
			getActivity().finish();
		}
		
		private void generateConclusionCard(QuizItem quizItem, String currentUserPoints) {
			Intent achievementIntent = new Intent(context, ConclusionActivity.class);
			achievementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			String questionId = quizItem.getQuestionId();
			
			conclusionCardImpl.open();
			ConclusionCard conclusionCard = conclusionCardImpl.selectRecordById(questionId);
			conclusionCardImpl.close();
			
			conclusionCard.setId(questionId);
			conclusionCard.setTitle(conclusionCard.getTitle());
			conclusionCard.setCopy(conclusionCard.getCopy());
			conclusionCard.setImageUrl(conclusionCard.getImageUrl());
			conclusionCard.setLevel(conclusionCard.getLevel());
			achievementIntent.putExtra("conclusionCard", conclusionCard);
			achievementIntent.putExtra("currentUserPoints", currentUserPoints);
			achievementIntent.putExtra("pointValue", quizItem.getPointValue());
			startActivity(achievementIntent);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_film_detail, container, false);

			conclusionCardImpl = new ConclusionCardImpl(context);
			levelAchievement = getArguments().getParcelable("levelAchievement");
			quizItem = getArguments().getParcelable("quizItem");
			bagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			locationArrayList = getArguments().getParcelable("locationArrayList");
//			pointsItemImpl = new PointsItemImpl(context);
			userImpl = new UserImpl(context);
			localQuizItemArrayList = getArguments().getParcelable("localQuizItemArrayList");
			localCurrentLocation = getArguments().getParcelable("localCurrentLocation");
			TextView titleTagText2 = (TextView) rootView.findViewById(R.id.titleTag2);
			TextView titleText2 = (TextView) rootView.findViewById(R.id.title2);
			TextView locationsTagText2 = (TextView) rootView.findViewById(R.id.locationsTag2);
			TextView locationsText2 = (TextView) rootView.findViewById(R.id.locations2);
			ImageView locationImage = (ImageView) rootView.findViewById(R.id.locationImage1);
			imageLoader.displayImage(localCurrentLocation.getFunFactsImageUrl(), locationImage);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			llp.setMargins(10, 0, 0, 0); // left, top, right, bottom;
			locationsList = (ListView) rootView.findViewById(R.id.locationsView1);
			prepareArrayAdapterData(rootView);
			
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
				case 1:
					locationsList.setVisibility(ListView.GONE);
					titleTagText2.setText("Game Item");
					titleText2.setText(localCurrentLocation.getFunFactsTitle());
					locationsTagText2.setText("Random Fun Fact");
					locationsText2.setText(localCurrentLocation.getFunFacts());
					break;
	
				case 2:
					locationImage.setVisibility(ImageView.GONE);
					locationsList.setVisibility(ListView.VISIBLE);
					break;
			}
			return rootView;
		}
		
		private void prepareArrayAdapterData(View rootView) {

			try {
				ArrayList<FilmLocation> locationList = locationArrayList.getFilmList();
				ArrayList<FilmLocation> finalLocationList = new ArrayList<FilmLocation>();

				for (FilmLocation loc : locationList) {
					if (loc.getTitle().equals(localCurrentLocation.getTitle())) {
						finalLocationList.add(loc);
					}
				}

				// sort the list
				Collections.sort(finalLocationList, StaticSortingUtilities.LOCATIONS_ALPHABETICAL_ORDER);

				String[] listItemTitles = new String[finalLocationList.size()];
				String[] listItemImageTiles = new String[finalLocationList.size()];

				// populate arrays
				int counter = 0;
				for (FilmLocation location : finalLocationList) {
					listItemTitles[counter] = location.getLocations();
					listItemImageTiles[counter] = location.getStaticMapImageUrl();
					counter++;
				}

				ArrayList<QuizItem> finalQuizList = new ArrayList<QuizItem>();
				Collections.sort(newQuizList, StaticSortingUtilities.QUIZ_ITEMS_ALPHABETICAL_ORDER);
				boolean answered = true;
				QuizItem replayQuizItem = null;
				for (QuizItem quizItem : newQuizList) {
					if (quizItem.getWorldTitle().equals(localCurrentLocation.getTitle())) {
						finalQuizList.add(quizItem);
						if (quizItem.getAnswered().equals("FALSE")) {
							answered = false;
						}
						replayQuizItem = quizItem;
					}
				}
				
				if (answered == true) {
					initializeReplayWorld(replayQuizItem);
				}

				Intent intent = getActivity().getIntent();
				
				// create new location quiz array adapter
				locationQuizArrayAdapter = new LocationQuizArrayAdapter(
						getActivity(), context, intent, finalQuizList);

				// set list item titles
				locationQuizArrayAdapter.setListItemTitles(listItemTitles);

				// set list item image tiles
				locationQuizArrayAdapter.setListItemImageTiles(listItemImageTiles);
				locationsList.setAdapter(locationQuizArrayAdapter);
				locationsList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
						@Override
						public void onItemClick(AdapterView<?> parent,
								final View view, int position, long id) {
							
							QuizItem item = (QuizItem) parent.getItemAtPosition(position);
							Intent quizIntent = new Intent(context, QuizActivity.class);
							String quizItemSid = currentUser.getUserSid();
							quizIntent.putExtra("currentUser", currentUser);
							quizIntent.putExtra("quizItemSid", quizItemSid);
							quizIntent.putExtra("bagItemArrayList", bagItemArrayList);
							quizIntent.putExtra("quizItem", item);
							startActivityForResult(quizIntent, 1);
						}
					});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void levelUpCurrentUser(String currentUserId, String currentLevelString) {
		if (userImpl != null) {
			userImpl.open();
			userImpl.updateCurrentUserLevel(currentUserId, currentLevelString);
			userImpl.close();
//			currentUser.setCurrentLevel(currentLevelString);
//			currentUser.setCurrentLevel(currentLevelString);
		}
		
		// SEND LEVEL UP NOTIFICATION
		if (levelAchievement != null && currentUser.getMobileNotifications().equals("true")) {
			sendLevelUpNotification();
		}
	}

	private void sendLevelUpNotification() {
		int NOTIFICATION_ID = 1;
		NotificationManager mNotificationManager;
		String msg = "Welcome to level ";
		
		if (title != null) {
			msg += " " + levelAchievement.getLevel() + " !";
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
	            .setAutoCancel(true)
	            .setDefaults(Notification.DEFAULT_VIBRATE)
				.setContentTitle("CircuitQuest")
				.setContentText(msg)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

			// create and start achievement activity
			mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			Intent achievementIntent = new Intent(context, AchievementActivity.class);
			achievementIntent.putExtra("achievement", levelAchievement);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, achievementIntent, PendingIntent.FLAG_ONE_SHOT);
			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());	
		}
	}
}