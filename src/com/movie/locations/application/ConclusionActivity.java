package com.movie.locations.application;

import java.util.ArrayList;

import com.movie.locations.R;
import com.movie.locations.application.WorldLocationDetailActivity.FilmLocationFragment;
import com.movie.locations.dao.PointsItemImpl;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.User;
import com.movie.locations.util.StaticSortingUtilities;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ConclusionActivity extends ActionBarActivity {

	private Intent intent;
	public static String conclusionTitle;
	public static String conclusionCopy;
	public static String conclusionImageUrl;
	public static String pointsData;
	public static String bonusPointsData;
//	private Context context;
	private String currentUserId;
	private PointsItem pointsItem;
	String CURRENT_USER_ID;
	private String worldCount;
	private BagItemArrayList bagItemArrayList;
	private static Context context;
	private ConclusionCard conclusionCard;
	private UserImpl userSource;
	private String currentLevelString;
//	private static String emailNotifications;
//	private static String mobileNotifications;
//	private static User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conclusion);

//		if (savedInstanceState == null) {
			
//			context = this;
			// quizItem = new quizItem();
			intent = getIntent();

			// get intent string attributes
			Bundle extras = intent.getExtras();
			
//			emailNotifications = extras.getString("emailNotifications");
//			mobileNotifications = extras.getString("mobileNotifications");
			
//			System.out.println("EMAIL NOTIFICATIONS: " + emailNotifications);
//			System.out.println("MOBILE NOTIFICATIONS: " + mobileNotifications);
			
			conclusionTitle = extras.getString("conclusionTitle");
			conclusionCopy = extras.getString("conclusionCopy");
			conclusionImageUrl = extras.getString("conclusionImageUrl");
			
			bagItemArrayList = extras.getParcelable("bagItemArrayList");
//			conclusionImageUrl = extras.getString("conclusionImageUrl");
//			pointsData = extras.getString("pointsData");
//			bonusPointsData = extras.getString("bonusPointsData");
			currentUserId = extras.getString("currentUserId");
			pointsItem = extras.getParcelable("pointsItem");
//			System.out.println("POINTS ITEM PARCEL USER POINTS ID: " + pointsItem.getPointsUserId());
			System.out.println("BAG ITEM ARRAY LIST: " + bagItemArrayList);
			
			worldCount = extras.getString("worldCount");
			currentLevelString = extras.getString("currentLevel");
			conclusionCard = extras.getParcelable("conclusionCard");
			
			context = this;
			

			
//			Fragment fragment = new FilmLocationFragment();
//			
			Fragment conclusionFragment = new PlaceholderFragment(); 
			Bundle bundle = new Bundle();
			bundle.putParcelable("bagItemArrayList", bagItemArrayList);
			bundle.putParcelable("conclusionCard", conclusionCard);
			conclusionFragment.setArguments(bundle);
			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, conclusionFragment).commit();
			
			
			
			userSource = new UserImpl(this);
//			currentUser = userSource.selectRecordById(currentUserId);
//			System.out.println("CURRENT USER CONCLUSION ACTIVITY: " + currentUser.getUserId());
//			System.out.println("CURRENT USER CONCLUSION ACTIVITY CURRENT LEVEL: " + currentUser.getCurrentLevel());
//			System.out.println("CURRENT USER CONCLUSION ACTIVITY CURRENT POINTS: " + currentUser.getCurrentPoints());
			
//			if (pointsItem != null) {
//				CURRENT_USER_ID = pointsItem.getUserId();
//				updatePointsDatabase();
//			}
			
//			if (emailNotifications != null && mobileNotifications != null) {
//				updateUserNotificationsSettings();
//			}
//		}
	}
	
//	private void updateUserNotificationsSettings() {
//
//		
//		userSource.open();
//		
//		// UPDATE USER PREFERENCES
//		final String FINAL_CURRENT_USER_ID_STRING = currentUserId;
//		
//		if (emailNotifications != null && mobileNotifications != null) {
//			userSource.updateUserNotificationPreferences(FINAL_CURRENT_USER_ID_STRING, emailNotifications, mobileNotifications);
//			System.out.println("UPDATED USER PREFERENCES DATABASE");
//		}
//		userSource.close();
//		
//	}

	public void updatePointsDatabase() {

		System.out.println("POINTS DATABASE");
		System.out.println("POINTS_ITEM CURRENT USER ID: " + CURRENT_USER_ID);
		
		System.out.println("POINTS ITEM PARCEL USER ID: " + CURRENT_USER_ID);
//			String currentBonusPoints = tempUser.getBonusPoints();
			String updatedPoints = null;
			String updatedBonusPoints = null;
			
			pointsData = pointsItem.getPoints();
//			final String finalPointsData = pointsData;
			bonusPointsData = pointsItem.getBonusPoints();
//			final String finalBonusPointsData = bonusPointsData;
			
			// THIS PART IS NOW STABLE

			if (pointsData != null) {
				System.out.println("POINTS DATABASE POINTS_DATA NOT NULL");
				// compose updated point sums
//				int pointsSum = Integer.parseInt(currentPoints) + Integer.parseInt(pointsData);
				int pointsSum = Integer.parseInt(pointsData);
				updatedPoints = Integer.toString(pointsSum);
//				pointsSource.updateRecordPointsValue(currentUserId, updatedPoints);
				System.out.println("POINTS DATABASE UPDATED POINTS: " + updatedPoints);
				if (bonusPointsData != null) {
					System.out.println("POINTS DATABASE BONUS_POINTS_DATA NOT NULL");
					// compose updated point sums
					System.out.println("NULL BROKEN VALUE CURRENT_POINTS: " + bonusPointsData);
					System.out.println("NULL BROKEN VALUE BONUS_POINTS_DATA: " + bonusPointsData);
					int bonusPointsSum = Integer.parseInt(bonusPointsData) + Integer.parseInt(bonusPointsData);

					System.out.println("NULL BROKEN VALUE BONUS_POINTS_SUM: " + bonusPointsSum);
					updatedBonusPoints = Integer.toString(bonusPointsSum);
//					pointsSource.updateRecordBonusPointsValue(currentUserId, updatedBonusPoints);
					System.out.println("POINTS DATABASE BONUS UPDATED BONUS POINTS: " + updatedBonusPoints);

				}
				
				int FINAL_BONUS_POINTS;
				
				if (updatedBonusPoints != null) {
					FINAL_BONUS_POINTS = Integer.parseInt(updatedPoints) + Integer.parseInt(updatedBonusPoints);
				} else {
					FINAL_BONUS_POINTS = Integer.parseInt(updatedPoints);
				}
				
				System.out.println("FINAL_BONUS_POINTS: " + updatedPoints);
				
//				userSource.open();

//				// SEE IF USER IS ELIGIBLE TO LEVEL UP
//				if (currentLevelCheck == currentLevel) {
//					// USER IS THE SAME LEVEL
//					
//					
//					
//				} else {
//					// UPDATE CURRENT USER LEVEL
//					final String CURRENT_USER_LEVEL = Integer.toString(currentLevelCheck);
//					userSource.updateCurrentUserLevel(CURRENT_USER_ID, CURRENT_USER_LEVEL);
//				}
				
				
				
				
				userSource.open();
				
				
				// CHECK CURRENT LEVEL
				
//				currentUser = userSource.selectRecordById(currentUserId);
//				int currentLevel = Integer.parseInt(currentUser.getCurrentLevel());
//				String CURRENT_LEVEL_STRING = Integer.toString(currentLevel);
//				int currentLevelCheck = StaticSortingUtilities.CHECK_LEVEL_RANGE(CURRENT_LEVEL_STRING, FINAL_BONUS_POINTS);
//				
//				final String CURRENT_USER_LEVEL = Integer.toString(currentLevelCheck);
//				userSource.updateCurrentUserLevel(currentUserId, CURRENT_USER_LEVEL);
				
				// update record with all current data
				userSource.updateRecordBonusPointsValue(currentUserId, updatedPoints, null);

//				User user = userSource.selectRecordById(currentUserId);
//				System.out.println("UPDATED CURRENT POINTS: " + user.getCurrentPoints());
				
				// UPDATE WORLD COUNT
				if (worldCount != null) {
					
					// CHECK FOR END OF NEW WORLDS
					// QUERY DATABASE FOR RECORDS WITH UPDATED WORLD COUNT
					
//					if () {
//						
//					}
					
					userSource.updateWorldCount(currentUserId, worldCount);
					System.out.println("UPDATED WORLD COUNT: " + worldCount);
				}
				
				if (currentLevelString != null) {
					userSource.updateCurrentUserLevel(currentUserId, currentLevelString);
//					userSource.updateWorldCount(currentUserId, worldCount);
					System.out.println("UPDATED CURRENT LEVEL STRING: " + currentLevelString);
				}
				
				
////				// UPDATE USER PREFERENCES
////				final String FINAL_CURRENT_USER_EMAIL_NOTIFICATIONS = emailNotifications;
////				final String FINAL_CURRENT_USER_MOBILE_NOTIFICATIONS = mobileNotifications;
//				
////				public void updateUserNotificationPreferences(String recordId, String updatedEmailNotifications, String updatedMobileNotifications) {
//				// UPDATE USER PREFERENCES
//				final String FINAL_CURRENT_USER_ID_STRING = currentUser.getUserId();
//				
//				if (emailNotifications != null && mobileNotifications != null) {
//					System.out.println("UPDATED USER PREFERENCES DATABASE");
//					userSource.updateUserNotificationPreferences(FINAL_CURRENT_USER_ID_STRING, emailNotifications, mobileNotifications);
//					
//				}
				
				
				
				
				User updatedUser = userSource.selectRecordById(currentUserId);
				
				System.out.println("CURRENT USER CONCLUSION ACTIVITY: " + updatedUser.getUserId());
				System.out.println("CURRENT USER CONCLUSION ACTIVITY CURRENT LEVEL: " + updatedUser.getCurrentLevel());
				System.out.println("CURRENT USER CONCLUSION ACTIVITY CURRENT POINTS: " + updatedUser.getCurrentPoints());
				System.out.println("CURRENT USER CONCLUSION ACTIVITY WORLD COUNT: " + updatedUser.getWorldCount());
				
				
				
				
//				pointsSource.delete();
				userSource.close();
			}
//		}
		
//		pointsSource.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conclusion, menu);
		return true;
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
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		private BagItemArrayList bagItemArrayList;
		private ConclusionCard conclusionCard;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_conclusion,
					container, false);
			TextView newBagItemTitleText = (TextView) rootView.findViewById(R.id.bagItemTitleHeadline1);
			TextView conclusionTitleText = (TextView) rootView.findViewById(R.id.conclusionTitleText);
			TextView conclusionCopyText = (TextView) rootView.findViewById(R.id.conclusionCopyText);
			TextView conclusionImageUrlText = (TextView) rootView.findViewById(R.id.conclusionImageUrlText);
			TextView pointsDataText = (TextView) rootView.findViewById(R.id.pointsDataText);
			TextView bonusPointsDataText = (TextView) rootView.findViewById(R.id.bonusPointsDataText);
			TextView conclusionCardTitleHeadlineText = (TextView) rootView.findViewById(R.id.conclusionCardTitleHeadline1);
			
			String CONCLUSION_TITLE_TEXT = conclusionTitle;
			String CONCLUSION_COPY_TEXT = conclusionCopy;
			
			
			bagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			conclusionCard = getArguments().getParcelable("conclusionCard");
			
			if (conclusionCard != null) {
//				conclusionCardTitleHeadline1

					conclusionCardTitleHeadlineText.setVisibility(TextView.VISIBLE);
					Intent intent = new Intent();
					
					ArrayList<ConclusionCard> cardList = new ArrayList<ConclusionCard>();
					cardList.add(conclusionCard);
					
					// SHOW NEW BAG ITEM(S)
					ConclusionCardArrayAdapter conclusionCardAdapter = new ConclusionCardArrayAdapter(context, intent, cardList);
					ListView cardListView = (ListView) rootView.findViewById(R.id.cardListView);
					cardListView.setVisibility(ListView.VISIBLE);
					cardListView.setAdapter(conclusionCardAdapter);
					
			}
			
			if (bagItemArrayList != null) {
				ArrayList<BagItem> bagList =  bagItemArrayList.getBagItemArrayList();
				
				if (bagList != null) {
					newBagItemTitleText.setVisibility(TextView.VISIBLE);
					Intent intent = new Intent();
					
					// SHOW NEW BAG ITEM(S)
					BagItemArrayAdapter bagItemAdapter = new BagItemArrayAdapter(context, intent, bagList);
					ListView bagListView = (ListView) rootView.findViewById(R.id.bagListView);
					bagListView.setVisibility(ListView.VISIBLE);
					bagListView.setAdapter(bagItemAdapter);
					
					int currentLevel = Integer.parseInt(bagList.get(0).getLevel());
					if (currentLevel < 2) {
						CONCLUSION_TITLE_TEXT = "Welcome to [app name]!";
						CONCLUSION_COPY_TEXT = "Here's some quest items to get you started. Refer to 'Help' section for details.";
					}
				}
				
			}
			
			conclusionTitleText.setText(CONCLUSION_TITLE_TEXT);
			conclusionCopyText.setText(CONCLUSION_COPY_TEXT);
			conclusionImageUrlText.setText(conclusionImageUrl);
			pointsDataText.setText(pointsData);
			bonusPointsDataText.setText(bonusPointsData);
			
			Button dismissConclusionButton = (Button) rootView.findViewById(R.id.dismissConclusionButton1);
			dismissConclusionButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					getActivity().finish();
				}
			});
			
			return rootView;
		}
	}

}

