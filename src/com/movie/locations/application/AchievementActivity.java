package com.movie.locations.application;
import com.movie.locations.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementActivity extends ActionBarActivity {

	private static Context achievementContext;
	private int messageId;
	private static String message;
	private static String levelUp;
	private static String achievementTitle;
	private static String achievementCopy;
	private static String achievementImageUrl;
	protected static ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievement);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new AchievementFragment()).commit();
			Intent intent = getIntent();
			messageId = intent.getIntExtra("messageId", 1);
			message = intent.getStringExtra("message");
			achievementTitle = intent.getStringExtra("achievementTitle");
			achievementCopy = intent.getStringExtra("achievementCopy");
			achievementImageUrl = intent.getStringExtra("achievementImageUrl");
			levelUp = intent.getStringExtra("levelUp");
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

	public static void setContext(Context context) {
		achievementContext = context;
	}

	public Context getContext() {
		return achievementContext;
	}

	// kill notification thread
	public void cancelNotification(int notifyId) {
		String service = Context.NOTIFICATION_SERVICE;
		NotificationManager manager = (NotificationManager) getContext()
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

	public String getAchievementTitle() {
		return achievementTitle;
	}

	public static void setAchievementTitle(String title) {
		achievementTitle = title;
	}

	public String getAchievementCopy() {
		return achievementCopy;
	}

	public static void setAchievementCopy(String copy) {
		achievementCopy = copy;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class AchievementFragment extends Fragment {

		public AchievementFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_achievement,
					container, false);
			TextView achievementTitleText = (TextView) rootView.findViewById(R.id.achievementTitleText1);
			achievementTitleText.setText(achievementTitle);
			TextView achievementCopyText = (TextView) rootView.findViewById(R.id.achievementCopyText1);
			achievementCopyText.setText(achievementCopy);
			ImageView achievementPoster = (ImageView) rootView.findViewById(R.id.achievementPoster1);
			imageLoader.displayImage(achievementImageUrl, achievementPoster);
			
			TextView currentLevelText = (TextView) rootView.findViewById(R.id.currentLevelText1);
			currentLevelText.setText("Welcome to level " + levelUp + " !");
			
			return rootView;
		}
	}

}
