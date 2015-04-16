package com.movie.locations.application;

import java.io.IOException;
import java.io.InputStream;
import com.google.android.gms.plus.PlusShare;
import com.movie.locations.R;
import com.movie.locations.domain.Achievement;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.NotificationManager;
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
import android.widget.TextView;

public class AchievementActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_achievement);

		if (savedInstanceState == null) {
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			Achievement achievement = extras.getParcelable("achievement");
			Fragment achievementFragment = new AchievementFragment();
			Bundle userBundle = new Bundle();
			userBundle.putParcelable("achievement", achievement);
			achievementFragment.setArguments(userBundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, achievementFragment).commit();
		}
	}

	// kill notification thread
	public void cancelNotification(int notifyId) {
		String service = Context.NOTIFICATION_SERVICE;
		NotificationManager manager = (NotificationManager) getBaseContext()
				.getSystemService(service);
		manager.cancelAll();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	private class AchievementFragment extends Fragment {

		public AchievementFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_achievement, container, false);
			Achievement achievement = getArguments().getParcelable("achievement");
			String achievementId = achievement.getAchievementId();
			String achievementTitle = achievement.getTitle();
			final String achievementCopy = achievement.getDescription();
			String achievementImageUrl = achievement.getImageUrl();
			ImageLoader imageLoader = ImageLoader.getInstance();
			TextView achievementTitleText = (TextView) rootView.findViewById(R.id.achievementTitleText1);
			TextView achievementCopyText = (TextView) rootView.findViewById(R.id.achievementCopyText1);
			ImageView achievementPoster = (ImageView) rootView.findViewById(R.id.achievementPoster1);
			imageLoader.displayImage(achievementImageUrl, achievementPoster);
			achievementTitleText.setText(achievementTitle);
			achievementCopyText.setText(achievementCopy);
			Button dismissAchievementButton = (Button) rootView.findViewById(R.id.dismissAchievement1);
			
			dismissAchievementButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					getActivity().finish();
				}
			});

			Button shareButton = (Button) rootView.findViewById(R.id.share_button);
			InputStream imageStream = null;
			
			try {
				imageStream = getActivity().getAssets().open(achievementId + ".jpg");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			final Context context = getActivity().getApplicationContext();
			final String staticSiteUrl = " http://low-tech-ridge.appspot.com";
	    	Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
	    	final String postImage = Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
	    	
			shareButton.setOnClickListener(new Button.OnClickListener() {
			    @Override
			    public void onClick(View v) {
					// Launch the Google+ share dialog with attribution to your app.
					  Intent shareIntent = new PlusShare.Builder(context)
					      .setType("text/plain")
					      .setText(achievementCopy + " " + staticSiteUrl) // APPEND STATIC HTML LINK HERE
					      .setStream(Uri.parse(postImage))
					      .getIntent();
					  
					  startActivityForResult(shareIntent, 0); 
			    }
			});

			return rootView;
		}
	}
}