package com.movie.locations.application;

import java.io.IOException;
import java.io.InputStream;
import com.google.android.gms.plus.PlusShare;
import com.movie.locations.R;
import com.movie.locations.database.UserImpl;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ConclusionActivity extends ActionBarActivity {

	private String pointsData;
	private String bonusPointsData;
	private String currentUserId;
	private PointsItem pointsItem;
	private String worldCount;
	private BagItemArrayList bagItemArrayList;
	private ConclusionCard conclusionCard;
	private UserImpl userSource;
	private String currentLevelString;
	private String currentUserPoints;
	private String pointValue;
	private String updatedPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conclusion);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		bagItemArrayList = extras.getParcelable("bagItemArrayList");
		currentUserId = extras.getString("currentUserId");
		pointsItem = extras.getParcelable("pointsItem");
		pointValue = extras.getString("pointValue");
		System.out.println("BAG ITEM ARRAY LIST: " + bagItemArrayList);
		worldCount = extras.getString("worldCount");
		currentLevelString = extras.getString("currentLevel");
		conclusionCard = extras.getParcelable("conclusionCard");
		currentUserPoints = extras.getString("currentUserPoints");
		
		userSource = new UserImpl(this);

		Fragment conclusionFragment = new ConclusionFragment(); 
		Bundle bundle = new Bundle();
		bundle.putParcelable("bagItemArrayList", bagItemArrayList);
		bundle.putParcelable("conclusionCard", conclusionCard);
		bundle.putString("pointValue", pointValue);
		bundle.putString("updatedPoints", updatedPoints);
		bundle.putString("currentUserPoints", currentUserPoints);
//		bundle.putParcelable("pointsItem", pointsItem);
		conclusionFragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, conclusionFragment).commit();
	}
	

	public void updatePointsDatabase() {

		updatedPoints = null;
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

		protected ImageLoader imageLoader = ImageLoader.getInstance();
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_conclusion,
					container, false);
			TextView conclusionTitleText = (TextView) rootView.findViewById(R.id.conclusionTitleText);
			TextView conclusionCopyText = (TextView) rootView.findViewById(R.id.conclusionCopyText);
//			TextView conclusionImageUrlText = (TextView) rootView.findViewById(R.id.conclusionImageUrlText);
//			TextView conclusionCardTitleHeadlineText = (TextView) rootView.findViewById(R.id.conclusionCardTitleHeadline1);
			ImageView conclusionImage = (ImageView) rootView.findViewById(R.id.conclusionImage);
			final ConclusionCard conclusionCard = getArguments().getParcelable("conclusionCard");
			final String conclusionTitle = conclusionCard.getTitle();
			final String conclusionCopy = conclusionCard.getCopy();
			final String conclusionImageUrl = conclusionCard.getImageUrl();
			final String pointValue = getArguments().getString("pointValue");
			final Context context = getActivity().getApplicationContext();
			final String currentUserPoints = getArguments().getString("currentUserPoints");

			final String imageUrl = "assets://" + conclusionImageUrl + ".jpg";
			final String staticSiteUrl = " https://www.google.com/";
			System.out.println("CONCLUSION IMAGE URL: " + imageUrl);
			imageLoader.displayImage(imageUrl, conclusionImage);
			conclusionTitleText.setText(conclusionTitle);
			conclusionCopyText.setText(conclusionCopy);
			
			if (conclusionCard.getLevel() != null) {
				
				TextView pointsDataText = (TextView) rootView.findViewById(R.id.pointsDataText);
				TextView bonusPointsDataText = (TextView) rootView.findViewById(R.id.bonusPointsDataText);
				TextView pointsDataTitle = (TextView) rootView.findViewById(R.id.pointsDataTitle);
				TextView bonusPointsDataTextTitle = (TextView) rootView.findViewById(R.id.bonusPointsDataTextTitle);
				
				pointsDataText.setVisibility(TextView.VISIBLE);
				bonusPointsDataText.setVisibility(TextView.VISIBLE);
				pointsDataTitle.setVisibility(TextView.VISIBLE);
				bonusPointsDataTextTitle.setVisibility(TextView.VISIBLE);
				pointsDataText.setText(pointValue);
				bonusPointsDataText.setText(currentUserPoints);
				
			}
			
			Button dismissConclusionButton = (Button) rootView.findViewById(R.id.dismissConclusionButton1);
			dismissConclusionButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					getActivity().finish();
				}
			});

			Button shareButton = (Button) rootView.findViewById(R.id.share_button);
			InputStream imageStream = null;
			
			try {
				imageStream = getActivity().getAssets().open(conclusionImageUrl + ".jpg");
			} catch (IOException e) {
				e.printStackTrace();
			}
				
	    	Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
	    	System.out.println("Bitmap: " + bitmap);
	    	System.out.println("Content Resolver: " + getActivity().getContentResolver());
	    	final String postImage = Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
	    	System.out.println("Post Image: " + postImage);
			shareButton.setOnClickListener(new Button.OnClickListener() {
			    @Override
			    public void onClick(View v) {
					// Launch the Google+ share dialog with attribution to your app.
					  Intent shareIntent = new PlusShare.Builder(context)
					      .setType("text/plain")
					      .setText(conclusionTitle + " " + staticSiteUrl)
					      .setStream(Uri.parse(postImage))
					      .getIntent();
					  
					  startActivityForResult(shareIntent, 0);
			    }
			});
			return rootView;
		}
	}
}