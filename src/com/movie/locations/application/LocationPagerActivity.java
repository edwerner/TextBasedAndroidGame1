package com.movie.locations.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.movie.locations.R;
import com.movie.locations.adapter.LocationArrayAdapter;
import com.movie.locations.database.BagItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.receiver.DatabaseChangedReceiver;
import com.movie.locations.service.QuizItemService;
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
	private ViewPager mViewPager;
	private HashMap<String, ArrayList<FilmLocation>> filmMap;
	private User currentUser;
	private Context context;
	private ArrayList<String> worldTitles;
	private ArrayList<FilmLocation> locationList;
	private ArrayList<String> localWorldImageUrls;
	private FilmArrayList locationArrayList;
	private BagItemArrayList bagItemArrayList;
	private FilmLocation currentLocation;
	private ArrayList<QuizItem> quizItemList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_film_location_pager);
		
		if (savedInstanceState == null) {
			context = this;
			Bundle bundle = getIntent().getExtras();
			
			// query database for records
			BagItemImpl bagItemImpl = new BagItemImpl(this); // bagItemArrayList
			bagItemImpl.open();
			ArrayList<BagItem> bagItemList = bagItemImpl.selectRecords();
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
			
			int arraylength = worldTitles.size();
			ArrayList<FilmLocation> tempList = new ArrayList<FilmLocation>();
			String CURRENT_TITLE = "CURRENT_TITLE";
			
			for (int i = 0; i < arraylength; i++) {
				tempList.clear();
				CURRENT_TITLE = worldTitles.get(i);
				
				for (FilmLocation location : locationList) {
					tempList.add(location);
				}
				
				filmMap.put(CURRENT_TITLE, tempList);
			}
			
			currentUser = bundle.getParcelable("localUser");
			mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
			QuizItemService quizItemService = new QuizItemService(context);
			quizItemService.createQuizItemImpl();
			quizItemList = quizItemService.selectRecords();
		}
	}

	public DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			// update your list
			mSectionsPagerAdapter.notifyDataSetChanged();
			unregisterReceiver(mReceiver);
	   }
	};

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			LocationSectionFragment movieSectionFragment = new LocationSectionFragment();
			Bundle args = new Bundle();
			String ARG_SECTION_NUMBER = movieSectionFragment.getArgSectionNumber();
			args.putInt(ARG_SECTION_NUMBER, position);
			args.putStringArrayList("worldTitles", worldTitles);
			args.putStringArrayList("localWorldImageUrls", localWorldImageUrls);
			args.putParcelable("locationArrayList", locationArrayList);
			args.putParcelable("currentLocation", currentLocation);
			args.putParcelable("bagItemArrayList", bagItemArrayList);
			args.putParcelable("currentUser", currentUser);
			movieSectionFragment.setArguments(args);
			
			return movieSectionFragment;
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
	        int position = worldTitles.indexOf(getTitle());

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
	private class LocationSectionFragment extends Fragment implements
			OnPageChangeListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private final String ARG_SECTION_NUMBER = "section_number";
		private ArrayList<String> localWorldTitles;
		private String currentTitle;
		private LocationArrayAdapter locationAdapter;
		private ListView commentView;
		private FilmArrayList filmArrayList;
		private View rootView;

		public LocationSectionFragment() {
			super();
		}

		public String getArgSectionNumber() {
			return ARG_SECTION_NUMBER;
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
			ArrayList<QuizItem> finalQuizList = new ArrayList<QuizItem>();
			QuizItem firstLocation = quizItemList.get(0);
			currentTitle = firstLocation.getWorldTitle();
			
			for (QuizItem quizItem : quizItemList) {
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
			
			View rootView = inflater.inflate(R.layout.fragment_film_panel, container, false);
			setRootView(rootView);
			prepareArrayAdapterData(rootView);
			FilmArrayList localLocationArrayList = getArguments().getParcelable("locationArrayList");
			BagItemArrayList localBagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			User localCurrentUser = getArguments().getParcelable("currentUser");
			ArrayList<FilmLocation> finalList = new ArrayList<FilmLocation>();
			filmArrayList = getArguments().getParcelable("locationArrayList");
			ArrayList<FilmLocation> filmList = filmArrayList.getFilmList();
			localWorldTitles = getArguments().getStringArrayList("worldTitles");
			localWorldImageUrls = getArguments().getStringArrayList("localWorldImageUrls");
			currentTitle = localWorldTitles.get(getArguments().getInt(ARG_SECTION_NUMBER));
			
			for (FilmLocation loc : filmList) {
				if (loc.getTitle().equals(currentTitle)) {
					finalList.add(loc);
				}
			}
			
			Context context = getActivity().getApplicationContext();
			Intent intent = new Intent(context, LocationDetailActivity.class);
			FilmLocation localCurrentLocation = finalList.get(0);
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