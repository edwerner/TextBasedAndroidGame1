package com.movie.locations.application;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.movie.locations.R;
import com.movie.locations.dao.BagItemImpl;
import com.movie.locations.dao.LocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.LocationMapParcel;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.receiver.DatabaseChangedReceiver;
import com.movie.locations.util.StaticSortingUtilities;
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

public class FilmLocationPagerActivity extends FragmentActivity {

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
//	private LocationsImpl locationsImpl;
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
		Bundle bundle = getIntent().getExtras();
		
		// query database for records
		BagItemImpl bagItemImpl = new BagItemImpl(this); // bagItemArrayList
		bagItemImpl.open();
		ArrayList<BagItem> bagItemList = bagItemImpl.selectRecords();
		bagItemImpl.close();
		
		// set parcelable array list
		bagItemArrayList = new BagItemArrayList(); 
		bagItemArrayList.setBagItemArrayList(bagItemList);
//		locationsImpl = new LocationsImpl(this);
		locationArrayList = bundle.getParcelable("locationArrayList");
		locationList = locationArrayList.getFilmList();
		// System.out.println("STARTED INTENT LIST LENGTH: " + locationList.size());
		filmMap = new LinkedHashMap<String, ArrayList<FilmLocation>>();
		worldTitles = new ArrayList<String>();
		localWorldImageUrls = new ArrayList<String>();
		
		// sort the list
//		Collections.sort(locationList, StaticSortingUtilities.LOCATIONS_ALPHABETICAL_ORDER);
		// System.out.println("LOCATION LIST FROM PARCEL: " + locationList.size());
		
		for (FilmLocation loc : locationList) {
			if (!worldTitles.contains(loc.getTitle())) {
				worldTitles.add(loc.getTitle());
				localWorldImageUrls.add(loc.getStaticMapImageUrl());
			}
		}
		
		int arraylength = worldTitles.size();
		// System.out.println("WORLD TITLES SIZE: " + arraylength);
		ArrayList<FilmLocation> tempList = new ArrayList<FilmLocation>();
		
		for (int a = 0; a < arraylength; a++) {
			// System.out.println("WORLD TITLES SIZE: " + worldTitles.get(a));
		}
		for (int i = 0; i < arraylength; i++) {
			tempList.clear();
//			// System.out.println("datasource: " + datasource);
			final String CURRENT_TITLE = worldTitles.get(i);
			// System.out.println("CURRENT_TITLE: " + CURRENT_TITLE);
			// System.out.println("SELECT RECORDS BY TITLE COUNT: " + locationList.size());
			
			for (FilmLocation location : locationList) {
				tempList.add(location);
			}
			
			filmMap.put(CURRENT_TITLE, tempList);
		}
		locationMap = new LocationMapParcel();
		locationMap.setLocationHashMap(filmMap);
		// System.out.println("FILM MAP LENGTH:" + filmMap.size());
		// System.out.println("LOCATION TITLE ARRAY LENGTH ****: " + arraylength);
		// System.out.println("FILM MAP SIZE ****: " + filmMap.size());
		currentUser = bundle.getParcelable("localUser");
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		quizItemImpl = new QuizItemImpl(context);
		quizItemImpl.open();
		newQuizList = quizItemImpl.selectRecords();
		quizItemImpl.close();
		
		// System.out.println("QUIZ LIST SIZE:" + newQuizList.size());
	}

//    @Override
//    protected void onPause() {
//        unregisterReceiver(mReceiver);
//        super.onPause();
//    }
	
    
	public DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			// update your list
			mSectionsPagerAdapter.notifyDataSetChanged();
		   // System.out.println("UPDATED DATA FROM RECEIVER");
		   unregisterReceiver(mReceiver);
	   }
	};
	
	@Override
	 public void onResume() {
		// System.out.println("******* RESUME PAGER ACTIVITY ********");
//		if (locationList != null) {
//			locationList = datasource.selectRecords();
//			locationArrayList.setFilmList(locationList);
//			Collections.sort(locationList, StaticSortingUtilities.LOCATIONS_ALPHABETICAL_ORDER);
//			// System.out.println("ON RESUME LOCATION LIST LENGTH: " + locationList.size());
//			for (FilmLocation loc : locationList) {
//				if (!worldTitles.contains(loc.getTitle())) {
//					worldTitles.add(loc.getTitle());
//					localWorldImageUrls.add(loc.getStaticMapImageUrl());
//				}
//			}
//			mSectionsPagerAdapter.notifyDataSetChanged();
//		}
		 super.onResume();
	 }

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

//		private int currentPositionIndex;
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
//			args.putParcelable("locationArrayList", locationArrayList);
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
			// System.out.println("WORLD COUNT: " + worldCount);
			return worldCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// System.out.println("WORLD TITLES SIZE: " + worldTitles.size());
			String localTitle = worldTitles.get(position);
			return localTitle;
		}
//		tempAdapterList
	    public int getItemPosition(Object item) {
	    	MovieSectionFragment fragment = (MovieSectionFragment) item;
	        String title = fragment.getFragmentTitle();
	        int position = worldTitles.indexOf(title);

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
		private LocationMapParcel localMapParcel;
		private String currentTitle;
		private FilmLocationArrayAdapter commentAdapter;
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
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
		
		// ON PAGE CHANGED UPDATE
		@Override
		public void onPageSelected(int position) {
			// System.out.println("PAGE INDEX CHANGED: " + position);
		}

//		@Override
//		  public void onRestoreInstanceState(Bundle savedInstanceState) {
//		    super.onRestoreInstanceState(savedInstanceState);
//		    // Restore UI state from the savedInstanceState.
//		    // This bundle has also been passed to onCreate.
//		    boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
//		    double myDouble = savedInstanceState.getDouble("myDouble");
//		    int myInt = savedInstanceState.getInt("MyInt");
//		    String myString = savedInstanceState.getString("MyString");
//		  }
		
		public void prepareArrayAdapterData(View rootView) {
			ArrayList<QuizItem> finalQuizList = new ArrayList<QuizItem>();
//			boolean answered = true;
//			QuizItem replayQuizItem = null;
			QuizItem firstLocation = newQuizList.get(0);
			currentTitle = firstLocation.getWorldTitle();
			
			for (QuizItem quizItem : newQuizList) {
				// System.out.println("LOCATION TITLE 1:" + currentTitle);
				// System.out.println("LOCATION TITLE 2:" + quizItem.getWorldTitle());
				// System.out.println("LOCAL CURRENT LOCATION CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
				if (quizItem.getWorldTitle().equals(currentTitle)) {
					finalQuizList.add(quizItem);
//					if (quizItem.getAnswered().equals("FALSE")) {
//						answered = false;
//					}
//					replayQuizItem = quizItem;
				}
			}
		}
		
		@Override
		public void onResume() {
			// System.out.println("Resume");
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
			View rootView = inflater.inflate(
					R.layout.fragment_film_panel, container, false);
			
//			ListView locationsList = (ListView) rootView
//					.findViewById(R.id.locationsView1);
			
			setRootView(rootView);
			prepareArrayAdapterData(rootView);
			
			// TO PASS THROUGH AS A PARCEL
			localMapParcel = getArguments().getParcelable("locationMap");
			FilmArrayList localLocationArrayList = getArguments().getParcelable("locationArrayList");
//			FilmLocation localCurrentLocation = getArguments().getParcelable("currentLocation");
			BagItemArrayList localBagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			User localCurrentUser = getArguments().getParcelable("currentUser");
			localWorldTitles = getArguments().getStringArrayList("worldTitles");
			localWorldImageUrls = getArguments().getStringArrayList("localWorldImageUrls");
			filmArrayList = getArguments().getParcelable("locationArrayList");
			ArrayList<FilmLocation> filmList = filmArrayList.getFilmList();
			currentTitle = localWorldTitles.get(getArguments().getInt(ARG_SECTION_NUMBER));
//			currentImageUrl = localWorldImageUrls.get(getArguments().getInt(ARG_SECTION_NUMBER));
			setFragmentTitle(currentTitle);
			// System.out.println("CURRENT FRAGMENT TITLE: " + currentTitle);
			// System.out.println("LOCAL MAP PARCEL: " + localMapParcel);
			//currentTitle
			ArrayList<FilmLocation> finalList = new ArrayList<FilmLocation>();
			for (FilmLocation loc : filmList) {
				if (loc.getTitle().equals(currentTitle)) {
					finalList.add(loc);
				}
			}
			
			final Context context = getActivity().getApplicationContext();
			final Intent intent = new Intent(context, WorldLocationDetailActivity.class);
			FilmLocation localCurrentLocation = finalList.get(0);
			// pass only current location
			intent.putExtra("locationArrayList", localLocationArrayList);
			intent.putExtra("currentLocation", localCurrentLocation);
			intent.putExtra("bagItemArrayList", localBagItemArrayList);
			intent.putExtra("localUser", localCurrentUser);
			// System.out.println("PAGER ACTIVITY CURRENT USER POINTS: " + localCurrentUser.getCurrentPoints());
			commentAdapter = new FilmLocationArrayAdapter(getActivity(), intent, finalList);
			commentView = (ListView) rootView.findViewById(R.id.listviewMapTiles);
			commentView.setAdapter(commentAdapter);
			commentAdapter.notifyDataSetChanged();
			mViewPager.setOnPageChangeListener(this);
			
			return rootView;
		}
	}
}
