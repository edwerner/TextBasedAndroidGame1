package com.movie.locations.application;
import java.io.IOException;
import java.io.InputStream;

import com.google.android.gms.plus.PlusShare;
import com.movie.locations.R;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementActivity extends ActionBarActivity {

	private int messageId;
	private String levelUp;
	private String achievementTitle;
	private String achievementCopy;
	private String achievementImageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievement);

		if (savedInstanceState == null) {

			Intent intent = getIntent();
			messageId = intent.getIntExtra("messageId", 1);
			achievementTitle = intent.getStringExtra("achievementTitle");
			achievementCopy = intent.getStringExtra("achievementCopy");
			achievementImageUrl = intent.getStringExtra("achievementImageUrl");
			levelUp = intent.getStringExtra("levelUp");
			
			Fragment achievementFragment = new AchievementFragment();
			Bundle userBundle = new Bundle();
			userBundle.putInt("messageId", messageId);
			userBundle.putString("achievementTitle", achievementTitle);
			userBundle.putString("achievementCopy", achievementCopy);
			userBundle.putString("achievementImageUrl", achievementImageUrl);
			userBundle.putString("levelUp", levelUp);
			achievementFragment.setArguments(userBundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, achievementFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.achievement, menu);
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

	// kill notification thread
	public void cancelNotification(int notifyId) {
		String service = Context.NOTIFICATION_SERVICE;
		NotificationManager manager = (NotificationManager) getBaseContext()
				.getSystemService(service);
//		manager.cancel(notifyId);
		manager.cancelAll();
		System.out.println("CANCELED NOTIFICATION");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		cancelNotification(messageId);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class AchievementFragment extends Fragment {

		public AchievementFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_achievement,
					container, false);

			final String achievementTitle = getArguments().getString("achievementTitle");
			final String achievementCopy = getArguments().getString("achievementCopy");
			final String achievementImageUrl = getArguments().getString("achievementImageUrl");
			final String levelUp = getArguments().getString("levelUp");
			final ImageLoader imageLoader = ImageLoader.getInstance();
			
			TextView achievementTitleText = (TextView) rootView.findViewById(R.id.achievementTitleText1);
			achievementTitleText.setText(achievementTitle);
			TextView achievementCopyText = (TextView) rootView.findViewById(R.id.achievementCopyText1);
			achievementCopyText.setText(achievementCopy);
			ImageView achievementPoster = (ImageView) rootView.findViewById(R.id.achievementPoster1);
			final String imageUrl = "assets://" + achievementImageUrl + ".jpg";
			imageLoader.displayImage(imageUrl, achievementPoster);
			
//			TextView currentLevelText = (TextView) rootView.findViewById(R.id.currentLevelText1);
//			currentLevelText.setText("Welcome to level" + levelUp + "!");
			
			Button dismissConclusionButton = (Button) rootView.findViewById(R.id.dismissConclusionButton1);
			dismissConclusionButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					getActivity().finish();
				}
			});

			Button shareButton = (Button) rootView.findViewById(R.id.share_button);
			InputStream imageStream = null;
			
			try {
				imageStream = getActivity().getAssets().open(achievementImageUrl + ".jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			InputStream inputStream = am.open(file:///android_asset/myfoldername/myfilename);
				

			final Context context = getActivity().getApplicationContext();
			final String staticSiteUrl = " https://www.google.com/";
	    	Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
	    	System.out.println("Bitmap: " + bitmap);
	    	System.out.println("Content Resolver: " + getActivity().getContentResolver());
	    	final String postImage = Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
	    	System.out.println("Post Image: " + postImage);
			shareButton.setOnClickListener(new Button.OnClickListener() {
			    @Override
			    public void onClick(View v) {
//			    	Uri screenshotUri = Uri.parse(path);
//			    	String image = "http://i.dailymail.co.uk/i/pix/2013/03/15/article-2293722-0294CDD8000004B0-59_306x455.jpg";
					// Launch the Google+ share dialog with attribution to your app.
					  Intent shareIntent = new PlusShare.Builder(context)
					      .setType("text/plain")
					      .setText(achievementCopy + " " + staticSiteUrl) // APPEND STATIC HTML LINK HERE
					      .setStream(Uri.parse(postImage))
//					          .setContentUrl(Uri.parse("https://developers.google.com/+/"))
					      .getIntent();
					  
					  startActivityForResult(shareIntent, 0);

			      
			    }
			});

			return rootView;
		}
	}
}
