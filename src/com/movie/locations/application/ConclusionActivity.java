package com.movie.locations.application;

import java.util.ArrayList;

import com.google.android.gms.plus.PlusShare;
import com.movie.locations.R;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.User;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ConclusionActivity extends ActionBarActivity {

	private Intent intent;
	private String conclusionTitle;
	private String conclusionCopy;
	private String conclusionImageUrl;
	private String pointsData;
	private String bonusPointsData;
	private String currentUserId;
	private PointsItem pointsItem;
	private String CURRENT_USER_ID;
	private String worldCount;
	private BagItemArrayList bagItemArrayList;
//	private Context context;
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
		
		intent = getIntent();

		// get intent string attributes
		Bundle extras = intent.getExtras();
		
//			emailNotifications = extras.getString("emailNotifications");
//			mobileNotifications = extras.getString("mobileNotifications");
		conclusionTitle = extras.getString("conclusionTitle");
		conclusionCopy = extras.getString("conclusionCopy");
		conclusionImageUrl = extras.getString("conclusionImageUrl");
		bagItemArrayList = extras.getParcelable("bagItemArrayList");
		currentUserId = extras.getString("currentUserId");
		pointsItem = extras.getParcelable("pointsItem");
		System.out.println("BAG ITEM ARRAY LIST: " + bagItemArrayList);
		worldCount = extras.getString("worldCount");
		currentLevelString = extras.getString("currentLevel");
		conclusionCard = extras.getParcelable("conclusionCard");
		Fragment conclusionFragment = new ConclusionFragment(); 
		Bundle bundle = new Bundle();
		bundle.putParcelable("bagItemArrayList", bagItemArrayList);
		bundle.putParcelable("conclusionCard", conclusionCard);
		bundle.putString("conclusionTitle", conclusionTitle);
		bundle.putString("conclusionCopy", conclusionCopy);
		bundle.putString("conclusionImageUrl", conclusionImageUrl);
		bundle.putString("pointsData", pointsData);
		conclusionFragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, conclusionFragment).commit();
		
		userSource = new UserImpl(this);
		
		if (pointsItem != null) {
			CURRENT_USER_ID = pointsItem.getUserId();
			updatePointsDatabase();
		}
	}
	

	public void updatePointsDatabase() {

		System.out.println("POINTS DATABASE");
		System.out.println("POINTS_ITEM CURRENT USER ID: " + CURRENT_USER_ID);
		System.out.println("POINTS ITEM PARCEL USER ID: " + CURRENT_USER_ID);
		String updatedPoints = null;
		String updatedBonusPoints = null;
		
		pointsData = pointsItem.getPoints();
		bonusPointsData = pointsItem.getBonusPoints();

		if (pointsData != null) {
			System.out.println("POINTS DATABASE POINTS_DATA NOT NULL");
			// compose updated point sums
			int pointsSum = Integer.parseInt(pointsData);
			updatedPoints = Integer.toString(pointsSum);
			System.out.println("POINTS DATABASE UPDATED POINTS: " + updatedPoints);
			if (bonusPointsData != null) {
				System.out.println("POINTS DATABASE BONUS_POINTS_DATA NOT NULL");
				// compose updated point sums
				System.out.println("NULL BROKEN VALUE CURRENT_POINTS: " + bonusPointsData);
				System.out.println("NULL BROKEN VALUE BONUS_POINTS_DATA: " + bonusPointsData);
				int bonusPointsSum = Integer.parseInt(bonusPointsData) + Integer.parseInt(bonusPointsData);
				System.out.println("NULL BROKEN VALUE BONUS_POINTS_SUM: " + bonusPointsSum);
				updatedBonusPoints = Integer.toString(bonusPointsSum);
				System.out.println("POINTS DATABASE BONUS UPDATED BONUS POINTS: " + updatedBonusPoints);
			}
			
//			int FINAL_BONUS_POINTS = 0;
//			
//			if (updatedBonusPoints != null) {
//				FINAL_BONUS_POINTS = Integer.parseInt(updatedPoints) + Integer.parseInt(updatedBonusPoints);
//			} else {
//				FINAL_BONUS_POINTS = Integer.parseInt(updatedPoints);
//			}
			
			System.out.println("FINAL_BONUS_POINTS: " + updatedPoints);
			userSource.open();
			
			// update record with all current data
			userSource.updateRecordBonusPointsValue(currentUserId, updatedPoints, null);
			
			// UPDATE WORLD COUNT
			if (worldCount != null) {
				userSource.updateWorldCount(currentUserId, worldCount);
				System.out.println("UPDATED WORLD COUNT: " + worldCount);
			}
			
			if (currentLevelString != null) {
				userSource.updateCurrentUserLevel(currentUserId, currentLevelString);
//					userSource.updateWorldCount(currentUserId, worldCount);
				System.out.println("UPDATED CURRENT LEVEL STRING: " + currentLevelString);
			}
			
			User updatedUser = userSource.selectRecordById(currentUserId);
			System.out.println("CURRENT USER CONCLUSION ACTIVITY: " + updatedUser.getUserId());
			System.out.println("CURRENT USER CONCLUSION ACTIVITY CURRENT LEVEL: " + updatedUser.getCurrentLevel());
			System.out.println("CURRENT USER CONCLUSION ACTIVITY CURRENT POINTS: " + updatedUser.getCurrentPoints());
			System.out.println("CURRENT USER CONCLUSION ACTIVITY WORLD COUNT: " + updatedUser.getWorldCount());
			userSource.close();
		}
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
	public static class ConclusionFragment extends Fragment {

		public ConclusionFragment() {
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
			
			final BagItemArrayList bagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			final ConclusionCard conclusionCard = getArguments().getParcelable("conclusionCard");
			final String conclusionTitle = getArguments().getParcelable("conclusionTitle");
			final String conclusionCopy = getArguments().getParcelable("conclusionCopy");
			final String conclusionImageUrl = getArguments().getParcelable("conclusionImageUrl");
			final String pointsData = getArguments().getParcelable("pointsData");
			final String bonusPointsData = getArguments().getParcelable("bonusPointsData");
			final Context context = getActivity().getApplicationContext();
			
			if (conclusionCard != null) {
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
					
//					int currentLevel = Integer.parseInt(bagList.get(0).getLevel());
//					if (currentLevel < 2) {
//						conclusionTitle = "Welcome to [app name]!";
//						conclusionCopy = "Here's some quest items to get you started. Refer to 'Help' section for details.";
//					}
				}
				
			}
			
			conclusionTitleText.setText(conclusionTitle);
			conclusionCopyText.setText(conclusionCopy);
			conclusionImageUrlText.setText(conclusionImageUrl);
			pointsDataText.setText(pointsData);
			bonusPointsDataText.setText(bonusPointsData);
			
			Button dismissConclusionButton = (Button) rootView.findViewById(R.id.dismissConclusionButton1);
			dismissConclusionButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					getActivity().finish();
				}
			});

			Button shareButton = (Button) rootView.findViewById(R.id.share_button);
			shareButton.setOnClickListener(new Button.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			      String image = "http://i.dailymail.co.uk/i/pix/2013/03/15/article-2293722-0294CDD8000004B0-59_306x455.jpg";
				// Launch the Google+ share dialog with attribution to your app.
			      Intent shareIntent = new PlusShare.Builder(context)
			          .setType("text/plain")
			          .setText("Test post")
			          .setContentUrl(Uri.parse(image))
//			          .setContentUrl(Uri.parse("https://developers.google.com/+/"))
			          .getIntent();

			      startActivityForResult(shareIntent, 0);
			    }
			});
			return rootView;
		}
	}
}

