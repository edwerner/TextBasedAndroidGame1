package com.movie.locations.application;

import java.io.IOException;
import java.io.InputStream;
import com.google.android.gms.plus.PlusShare;
import com.movie.locations.R;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.ConclusionCard;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConclusionActivity extends ActionBarActivity {

	private BagItemArrayList bagItemArrayList;
	private ConclusionCard conclusionCard;
	private String currentUserPoints;
	private String pointValue;
	private String updatedPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_conclusion);
		
		if (savedInstanceState == null) {
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			
			bagItemArrayList = extras.getParcelable("bagItemArrayList");
			pointValue = extras.getString("pointValue");
			conclusionCard = extras.getParcelable("conclusionCard");
			currentUserPoints = extras.getString("currentUserPoints");

			Fragment conclusionFragment = new ConclusionFragment(); 
			Bundle bundle = new Bundle();
			bundle.putParcelable("bagItemArrayList", bagItemArrayList);
			bundle.putParcelable("conclusionCard", conclusionCard);
			bundle.putString("pointValue", pointValue);
			bundle.putString("updatedPoints", updatedPoints);
			bundle.putString("currentUserPoints", currentUserPoints);
			conclusionFragment.setArguments(bundle);
			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, conclusionFragment).commit();	
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	private class ConclusionFragment extends Fragment {

		public ConclusionFragment() {
			// empty constructor
		}

		protected final ImageLoader imageLoader = ImageLoader.getInstance();
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_conclusion, container, false);
			TextView conclusionTitleText = (TextView) rootView.findViewById(R.id.conclusionTitleText);
			TextView conclusionCopyText = (TextView) rootView.findViewById(R.id.conclusionCopyText);
			ImageView conclusionImage = (ImageView) rootView.findViewById(R.id.conclusionImage);
			ConclusionCard conclusionCard = getArguments().getParcelable("conclusionCard");
			final String conclusionTitle = conclusionCard.getTitle();
			String conclusionCopy = conclusionCard.getCopy();
			String conclusionImageUrl = conclusionCard.getImageUrl();
			String pointValue = getArguments().getString("pointValue");
			final Context context = getActivity().getApplicationContext();
			String currentUserPoints = getArguments().getString("currentUserPoints");
			String imageUrl = "assets://" + conclusionImageUrl + ".jpg";
			final String staticSiteUrl = " http://make-quiz.appspot.com";
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

			LinearLayout shareButton = (LinearLayout) rootView.findViewById(R.id.share_button);
			InputStream imageStream = null;
			
			try {
				imageStream = getActivity().getAssets().open(conclusionImageUrl + ".jpg");
			} catch (IOException e) {
				e.printStackTrace();
			}
				
	    	Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
	    	final String postImage = Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
	    	final String gameItemShareMessage = conclusionTitle + " - Arduino Quiz Puzzle Game - " + staticSiteUrl;

			shareButton.setOnClickListener(new Button.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	// Launch the Google+ share dialog with attribution to your app.
			    	Intent shareIntent = new PlusShare.Builder(context)
			    	.setType("text/plain")
			    	.setText(gameItemShareMessage)
			    	.setStream(Uri.parse(postImage))
			    	.getIntent();
			    	
			    	startActivityForResult(shareIntent, 0);
			    }
			});
			return rootView;
		}
	}
}