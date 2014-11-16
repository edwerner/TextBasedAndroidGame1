package com.movie.locations.application;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.FilmLocationContent;
import com.movie.locations.service.FilmLocationService;
import com.movie.locations.adapter.ArrayAdapterItem;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

	private ArrayList<FilmLocation> filmList;
	private MovieLocationsImpl datasource;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		filmList = new ArrayList<FilmLocation>();
		datasource = new MovieLocationsImpl(getActivity());
//		filmList = datasource.selectRecords();

//

//	    // Get the message from the intent
//	    Intent intent = getIntent();
////	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//		Bundle extras = intent.getExtras();
//		extras.getParcelable("filmList");
//		
////		System.out.println("*********************** EXTRAS FILM LIST PARCELABLE *********************** : " + extras.getParcelableArrayList("filmList"));
//		
//		List<FilmLocation> filmList = extras.getParcelableArrayList("filmList");
//		
//		for (FilmLocation location : filmList) {
//			System.out.println("*********************** FILM TITLE *********************** " + location);
//		}
		

//		Parcel parcel = unmarshall(filmList);
//		MyClass myclass = new MyClass(parcel);
		
//		MovieLocationsImpl datasource;
//		FilmLocationService service;
//		ItemListActivity activity = null;
//
//		datasource = new MovieLocationsImpl(activity.getBaseContext());

//		Intent intent = new Intent(getActivity(), MainActivity.class);
//		
////		Intent intent = getActivity().getIntent();
////
//		Bundle extras = intent.getExtras();
//		
//		System.out.println("*********************** EXTRAS *********************** : " + extras);
////		
////		
//		@SuppressWarnings("unchecked")
//		ArrayList<FilmLocation> filmList = extras.getParcelableArrayList("filmList");
		
//		for (FilmLocation data : filmList) {
////			addItem(new BagItem(data.getSid(), data.getTitle()));
//			System.out.println("FILM TITLE: " + data.getTitle());
//		}
//		
		
//		filmList = extras.getParcelableArrayList("filmList");
				
//		for (FilmLocation data : datasource.selectRecords()) {
//			addItem(new BagItem(data.getSid(), data.getTitle()));
//		}
		// // TODO: replace with a real list adapter.
//		setListAdapter(new ArrayAdapter<FilmLocation>(
//				getActivity(), android.R.layout.simple_list_item_activated_1,
//				android.R.id.text1, filmList));
//		setListAdapter(new ArrayAdapter<FilmLocation>(
//				getActivity(), android.R.layout.simple_list_item_activated_1,
//				android.R.id.text1, datasource.selectRecords()));
		setListAdapter(new ArrayAdapter<FilmLocation>(
				getActivity(), android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, datasource.selectRecords()));

		// our adapter instance

		// // call json service here

		//
		// setListAdapter(new
		// ArrayAdapter<FilmLocationContent.Item>(getActivity(),
		// android.R.layout.simple_list_item_activated_1,
		// android.R.id.text1, FilmLocationContent.ITEMS));
		//
		// ArrayAdapterItem adapter = new ArrayAdapterItem(this,
		// R.layout.simple_list_item_activated_1, ObjectItemData);
		//
		// // create a new ListView, set the adapter and item click listener
		// ListView listViewItems = new ListView(this);
		// listViewItems.setAdapter(adapter);
		// listViewItems.setOnItemClickListener(new
		// OnItemClickListenerListViewItem());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
//		mCallbacks.onItemSelected(FilmLocationContent.ITEMS.get(position).id);
		mCallbacks.onItemSelected(datasource.selectRecords().get(position).getSid());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
