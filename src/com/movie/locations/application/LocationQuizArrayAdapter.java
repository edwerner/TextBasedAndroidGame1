package com.movie.locations.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.movie.locations.R;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.LocationObject;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.domain.WorldLocationObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LocationQuizArrayAdapter extends ArrayAdapter<QuizItem> {
	private final Context context;
	private final ArrayList<QuizItem> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private final String SEARCH_DELIMITER = "+San+Francisco+California";
	public String UNIQUE_MAP_IMAGE_URL = "";
	public final String DEFAULT_MAP_IMAGE_URL = "http://staticmap.openstreetmap.de/staticmap.php?center=48.854182,2.371105&zoom=13&size=500x500&maptype=mapnik";
//	public final String DEFAULT_MAP_IMAGE_URL = "http://maps.googleapis.com/maps/api/staticmap?center=San+Francisco+California&zoom=14&size=200x200&sensor=true";
	public final String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
	public String CENTER = "";
	public String SETTINGS = "&zoom=16&size=200x200&sensor=true";
	private String MOVIE_POSTER_URL = "";
	private User user;
	private Intent intent;
	private String[] listItemTitles;
	private String[] listItemImageTiles;
	private ArrayList<BagItem> bagItemList;
	private Activity activity;

	public LocationQuizArrayAdapter(Activity activity, Context context, Intent intent, ArrayList<QuizItem> values) {
		super(context, R.layout.film_list_array_adapter, values);
		this.context = context;
		this.intent = intent;
		this.values = values;
		this.activity = activity;
	}

	public String removeParenthesis(String string) {
		String regex = string.replaceAll("\\(", " ");
		regex = regex.replaceAll("\\)", " ");
		return regex;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.film_list_array_adapter,
				parent, false);
		
		
		// TODO: revert array adapter to locations domain
		
		
		
//		final Intent quizIntent = new Intent(context, QuizActivity.class);
//		quizIntent.putExtra("quizItemSid", currentUser.getUserSid());
//		quizIntent.putExtra("quizItem", currentQuizItem);
//		
//		
////		quizIntent.putExtra("bagItemArrayList", bagItemArrayList);
//		Bundle extras = intent.getExtras();
//		
//		String quizItemSid = extras.getString("quizItemSid");
//		BagItemArrayList bagItemArrayList = extras.getParcelable("bagItemArrayList");
////		BagItemArrayList bagItemArrayList = extras.getParcelable("bagItemArrayList");
//		
//		
//
//		final Intent quizIntent = new Intent(context, QuizActivity.class);
//		quizIntent.putExtra("quizItemSid", quizItemSid);
//		quizIntent.putExtra("bagItemArrayList", bagItemArrayList);
//		quizIntent.putExtra("quizItem", values.get(position));
		
		// get intent string attributes
//		Bundle bundle = intent.getExtras();
//		System.out.println("BUNDLE INTENT: " + bundle);
//		User localUser = bundle.getParcelable("localUser");
//		QuizItem quizItem = bundle.getParcelable("quizItem");
//		QuizItem quizItemMatch = quizsource.selectRecordById(values.get(position).getId());
				
//		Bundle quizBundle = quizIntent.getExtras();
		
//		// build new bundle with combined attributes
//		quizIntent.putExtra("position", values.get(position).getPosition());
		
//		QuizItem quizItem = values.get(position);
		
		
////		if (!values.get(position).getLatitude().equals("")) {
//			rowView.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
////					activity.startActivityForResult(quizIntent, 1);
//			        ((Activity) context).startActivityForResult(quizIntent, 1);
//				}
//			});	
//		}

		TextView textView = (TextView) rowView.findViewById(R.id.label);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		
		// reveal completed indicator checkmark
		System.out.println("VALUES LENGTH: " + values.size());
		if (values.get(position).getAnswered().equals("true")) {
			ImageView checkmarkImageView = (ImageView) rowView.findViewById(R.id.green_checkmark_small);
			checkmarkImageView.setVisibility(ImageView.VISIBLE);
			System.out.println("GREEN CHECKMARK");
			final int HALFTONE = 50;
			imageView.setImageAlpha(HALFTONE);
//			ImageView replayImageView = (ImageView) rowView.findViewById(R.id.replayImage_01);
//			replayImageView.setVisibility(ImageView.VISIBLE);
		}


//		private String[] listItemTitles;
//		private String[] listItemImageTiles;
		
		String[] titles = getListItemTitles();
		String[] imageTiles = getListItemImageTiles();
		System.out.println("IMAGE TILES LENGTH: " + imageTiles.length);
		if (imageTiles[position] != null) {
//			String formattedLocation = removeParenthesis((values.get(position)
//					.getLocations()));
		
		

//			//regex parenthesis
//			formattedLocation = formattedLocation.replaceAll(" ", "+");
//			formattedLocation += SEARCH_DELIMITER;
//			UNIQUE_MAP_IMAGE_URL = PREFIX + formattedLocation + SETTINGS;
//			
////			System.out.println("UNIQUE_MAP_IMAGE_URL: " + UNIQUE_MAP_IMAGE_URL);
//			
//			// Load image, decode it to Bitmap and display Bitmap in ImageView
//			// mapUnavailable.setVisibility(ImageView.GONE);
//			// defaultMapThumb.setVisibility(ImageView.GONE);
//			// moviePosterThumb.setVisibility(ImageView.GONE);
			
//			UNIQUE_MAP_IMAGE_URL = values.get(position).getStaticMapImageUrl();
			UNIQUE_MAP_IMAGE_URL = imageTiles[position];
			
//			imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
		} else {
			UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
//			// imageView.setVisibility(ImageView.GONE);
//
//			// defaultMapImage
//			// defaultMapThumb.setVisibility(ImageView.VISIBLE);
		}

		// ImageView mapThumb = (ImageView) rowView.findViewById(R.id.mapView1);
//		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);

		textView.setText(titles[position]);

//		// Change icon based on name
//		String s = values.get(position).getTitle();
//
//		System.out.println(s);

		// imageView.setImageResource(R.drawable.film_reel_sm);

		return rowView;
	}
	
    @Override
    public int getCount() {
        int size = getListItemTitles().length;
        return size;
    }

	public String[] getListItemTitles() {
		return listItemTitles;
	}

	public void setListItemTitles(String[] listItemTitles) {
		this.listItemTitles = listItemTitles;
	}

	public String[] getListItemImageTiles() {
		return listItemImageTiles;
	}

	public void setListItemImageTiles(String[] listItemImageTiles) {
		this.listItemImageTiles = listItemImageTiles;
	}

	public void setBagItemList(ArrayList<BagItem> bagItemList) {
		this.bagItemList = bagItemList;
		
	}
	
	public ArrayList<BagItem> getBagItemList() {
		return bagItemList;
	}

}
