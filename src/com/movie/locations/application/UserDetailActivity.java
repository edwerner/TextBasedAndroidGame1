package com.movie.locations.application;

import com.movie.locations.R;
import com.movie.locations.R.id;
import com.movie.locations.R.layout;
import com.movie.locations.R.menu;
import com.movie.locations.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class UserDetailActivity extends ActionBarActivity {

	protected static ImageLoader imageLoader = ImageLoader.getInstance();

	private static User localUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();

			// get intent string attributes
			// Intent intent = getIntent();
			Bundle bundle = getIntent().getExtras();
			localUser = bundle.getParcelable("localUser");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_detail, menu);
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

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_user_detail,
					container, false);

////			ImageView userAvatar = (ImageView) rootView
////					.findViewById(R.id.userAvatar1);
////			imageLoader.displayImage(localUser.getAvatarImageUrl(), userAvatar);
//
//			// TextView userProfileTitleTag = (TextView)
//			// findViewById(R.id.userProfileTitleTag1);
//			// TextView userTextTag = (TextView)
//			// findViewById(R.id.userFullNameTag1);
//			TextView userText = (TextView) rootView
//					.findViewById(R.id.userFullName1);
//
//			// TextView achievementsTitleTag = (TextView)
//			// findViewById(R.id.achievementsTitleTag1);
//			// TextView achievementsTag = (TextView)
//			// findViewById(R.id.achievementsTag1);
//			TextView achievementsText = (TextView) rootView
//					.findViewById(R.id.achievementsText1);
//
//			// TextView miscTitleTag = (TextView)
//			// findViewById(R.id.miscTitleTag1);
//			// TextView miscTag = (TextView) findViewById(R.id.miscTag1);
//			TextView miscText = (TextView) rootView.findViewById(R.id.miscText);
//
//			// set text values
//			System.out.println("localUser.getDisplayName(): "
//					+ localUser.getDisplayName());
//			System.out.println("userText: " + userText);
//			userText.setText(localUser.getDisplayName());
//			achievementsText.setText("First achievement +100 bonus points!");
//			miscText.setText("10 check-ins in one day - way to go!");

//			Button getStartedButton = (Button) rootView
//					.findViewById(R.id.get_started_button);
//			
//			getStartedButton.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					// start new intent
//					Intent pagerActivityIntent = new Intent(getActivity(),
//							FilmLocationPagerActivity.class);
//					pagerActivityIntent.putExtra("localUser", localUser);
//					startActivity(pagerActivityIntent);
//				}
//			});

			return rootView;
		}
	}

}
