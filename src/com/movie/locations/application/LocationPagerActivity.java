package com.movie.locations.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.movie.locations.R;
import com.movie.locations.adapter.LocationArrayAdapter;
import com.movie.locations.database.BagItemImpl;
import com.movie.locations.database.QuizItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.LocationMapParcel;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.receiver.DatabaseChangedReceiver;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LocationPagerActivity extends FragmentActivity {

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
	private static ViewPager mViewPager;
	private HashMap<String, ArrayList<FilmLocation>> filmMap;
	private User currentUser;
	private Context context;
	private ArrayList<String> worldTitles;
	private ArrayList<FilmLocation> locationList;
	private LocationMapParcel locationMap;
	private ArrayList<String> localWorldImageUrls;
	private FilmArrayList locationArrayList;
	private BagItemArrayList bagItemArrayList;
	private FilmLocation currentLocation;
	private QuizItemImpl quizItemImpl;
	private ArrayList<QuizItem> newQuizList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_film_location_pager);		
		context = this;
		final Bundle bundle = getIntent().getExtras();
		
		// query database for records
		final BagItemImpl bagItemImpl = new BagItemImpl(this); // bagItemArrayList
		bagItemImpl.open();
		final ArrayList<BagItem> bagItemList = bagItemImpl.selectRecords();
		bagItemImpl.close();
		
		// set parcelable array list
		bagItemArrayList = new BagItemArrayList(); 
		bagItemArrayList.setBagItemArrayList(bagItemList);
		locationArrayList = bundle.getParcelable("locationArrayList");
		locationList = locationArrayList.getFilmList();
		filmMap = new LinkedHashMap<String, ArrayList<FilmLocation>>();
		worldTitles = new ArrayList<String>();
		localWorldImageUrls = new ArrayList<String>();
		
		for (FilmLocation loc : locationList) {
			if (!worldTitles.contains(loc.getTitle())) {
				worldTitles.add(loc.getTitle());
				localWorldImageUrls.add(loc.getStaticMapImageUrl());
			}
		}
		
		final int arraylength = worldTitles.size();
		final ArrayList<FilmLocation> tempList = new ArrayList<FilmLocation>();
		
		for (int i = 0; i < arraylength; i++) {
			tempList.clear();
			final String CURRENT_TITLE = worldTitles.get(i);
			
			for (FilmLocation location : locationList) {
				tempList.add(location);
			}	
			filmMap.put(CURRENT_TITLE, tempList);
		}
		
		locationMap = new LocationMapParcel();
		locationMap.setLocationHashMap(filmMap);
		currentUser = bundle.getParcelable("localUser");
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		quizItemImpl = new QuizItemImpl(context);
		quizItemImpl.open();
		newQuizList = quizItemImpl.selectRecords();
		quizItemImpl.close();
	}

	public DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			// update your list
			mSectionsPagerAdapter.notifyDataSetChanged();
		   unregisterReceiver(mReceiver);
	   }
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.director_sort, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		final ArrayList<String> titles = new ArrayList<String>();

		public SectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new MovieSectionFragment();
			Bundle args = new Bundle();
			args.putInt(MovieSectionFragment.ARG_SECTION_NUMBER, position);
			args.putStringArrayList("worldTitles", worldTitles);
			args.putStringArrayList("localWorldImageUrls", localWorldImageUrls);
			args.putParcelable("locationArrayList", locationArrayList);
			args.putParcelable("locationMap", locationMap);
			args.putParcelable("currentLocation", currentLocation);
			args.putParcelable("bagItemArrayList", bagItemArrayList);
			args.putParcelable("currentUser", currentUser);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show total pages.
			int worldCount = worldTitles.size();
			return worldCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String localTitle = worldTitles.get(position);
			return localTitle;
		}
		
	    public int getItemPosition(Object item) {
	    	final MovieSectionFragment fragment = (MovieSectionFragment) item;
	        final String title = fragment.getFragmentTitle();
	        final int position = worldTitles.indexOf(title);

	        if (position >= 0) {
	            return position;
	        } else {
	            return POSITION_NONE;
	        }
	    }
	}

	/**
	 * Movie fragment
	 * 
	 */
	public class MovieSectionFragment extends Fragment implements
			OnPageChangeListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		protected ImageLoader imageLoader = ImageLoader.getInstance();
		private ArrayList<String> localWorldTitles;
		private String currentTitle;
		private LocationArrayAdapter locationAdapter;
		private ListView commentView;
		private String fragmentTitle;
		private FilmArrayList filmArrayList;
		private View rootView;

		public MovieSectionFragment() {
			super();
		}

		public String getFragmentTitle() {
			return fragmentTitle;
		}
		
		public void setFragmentTitle(String fragmentTitle) {
			this.fragmentTitle = fragmentTitle;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
		
		// ON PAGE CHANGED UPDATE
		@Override
		public void onPageSelected(int position) {
		
		}
		
		public void prepareArrayAdapterData(View rootView) {
			final ArrayList<QuizItem> finalQuizList = new ArrayList<QuizItem>();
			final QuizItem firstLocation = newQuizList.get(0);
			currentTitle = firstLocation.getWorldTitle();
			
			for (QuizItem quizItem : newQuizList) {
				if (quizItem.getWorldTitle().equals(currentTitle)) {
					finalQuizList.add(quizItem);
				}
			}
		}
		
		@Override
		public void onResume() {
			prepareArrayAdapterData(getRootView());
			super.onResume();
		}
		
		public void setRootView(View rootView) {
			this.rootView = rootView;
		}
		
		public View getRootView() {
			return rootView;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(
					R.layout.fragment_film_panel, container, false);
			
			setRootView(rootView);
			prepareArrayAdapterData(rootView);
			final FilmArrayList localLocationArrayList = getArguments().getParcelable("locationArrayList");
			final BagItemArrayList localBagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			final User localCurrentUser = getArguments().getParcelable("currentUser");
			final ArrayList<FilmLocation> finalList = new ArrayList<FilmLocation>();
			filmArrayList = getArguments().getParcelable("locationArrayList");
			final ArrayList<FilmLocation> filmList = filmArrayList.getFilmList();
			localWorldTitles = getArguments().getStringArrayList("worldTitles");
			localWorldImageUrls = getArguments().getStringArrayList("localWorldImageUrls");
			currentTitle = localWorldTitles.get(getArguments().getInt(ARG_SECTION_NUMBER));
			setFragmentTitle(currentTitle);
			
			for (FilmLocation loc : filmList) {
				if (loc.getTitle().equals(currentTitle)) {
					finalList.add(loc);
				}
			}
			
			final Context context = getActivity().getApplicationContext();
			final Intent intent = new Intent(context, LocationDetailActivity.class);
			final FilmLocation localCurrentLocation = finalList.get(0);
			intent.putExtra("locationArrayList", localLocationArrayList);
			intent.putExtra("currentLocation", localCurrentLocation);
			intent.putExtra("bagItemArrayList", localBagItemArrayList);
			intent.putExtra("localUser", localCurrentUser);
			locationAdapter = new LocationArrayAdapter(getActivity(), intent, finalList);
			commentView = (ListView) rootView.findViewById(R.id.listviewMapTiles);
			commentView.setAdapter(locationAdapter);
			locationAdapter.notifyDataSetChanged();
			mViewPager.setOnPageChangeListener(this);
			
			return rootView;
		}
	}
}