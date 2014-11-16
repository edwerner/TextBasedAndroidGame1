package com.movie.locations.application;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.movie.locations.R;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.User;
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

public class FilmLocationMapTileArrayAdapter extends ArrayAdapter<FilmLocation> {
	private final Context context;
	private final ArrayList<FilmLocation> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private final String SEARCH_DELIMITER = "+San+Francisco+California";
	public String UNIQUE_MAP_IMAGE_URL = "";
//	public final String DEFAULT_MAP_IMAGE_URL = "http://maps.googleapis.com/maps/api/staticmap?center=San+Francisco+California&zoom=16&size=1200x1200&sensor=true";
	public final String DEFAULT_MAP_IMAGE_URL = "http://staticmap.openstreetmap.de/staticmap.php?center=48.854182,2.371105&zoom=13&size=500x500&maptype=mapnik";
	public final String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
	public String CENTER = "";
	public String SETTINGS = "&zoom=16&size=1200x1200&sensor=true";
	private String MOVIE_POSTER_URL = "";
	private User user;
	private Intent intent;

	public FilmLocationMapTileArrayAdapter(Context context, Intent intent,
			ArrayList<FilmLocation> values) {
		super(context, R.layout.film_list_map_tile_array_adapter, values);
		this.context = context;
		this.intent = intent;
		this.values = values;
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

		View rowView = inflater.inflate(R.layout.film_list_map_tile_array_adapter,
				parent, false);

//		final Intent mapIntent = new Intent(context, MapDetailActivity.class);
		
		// get intent string attributes
//		Bundle bundle = intent.getExtras();
//		System.out.println("BUNDLE INTENT: " + bundle);
//		User localUser = bundle.getParcelable("localUser");
		
//		if (!values.get(position).getLatitude().equals("")) {
			rowView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					context.startActivity(intent);
				}
			});	
//		}

		TextView textView = (TextView) rowView.findViewById(R.id.label);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);

//		if (values.get(position).getStaticMapImageUrl() != null) {
//			String formattedLocation = removeParenthesis((values.get(position)
//					.getLocations()));

//			//regex parenthesis
//			formattedLocation = formattedLocation.replaceAll(" ", "+");
//			formattedLocation += SEARCH_DELIMITER;
//			UNIQUE_MAP_IMAGE_URL = PREFIX + formattedLocation + SETTINGS;
			
//			System.out.println("UNIQUE_MAP_IMAGE_URL: " + UNIQUE_MAP_IMAGE_URL);
			
			// Load image, decode it to Bitmap and display Bitmap in ImageView
			// mapUnavailable.setVisibility(ImageView.GONE);
			// defaultMapThumb.setVisibility(ImageView.GONE);
			// moviePosterThumb.setVisibility(ImageView.GONE);
			
			UNIQUE_MAP_IMAGE_URL = values.get(position).getStaticMapImageUrl();
			
//			imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
//		} else {
//			UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
//			// imageView.setVisibility(ImageView.GONE);
//
//			// defaultMapImage
//			// defaultMapThumb.setVisibility(ImageView.VISIBLE);
//		}

		// ImageView mapThumb = (ImageView) rowView.findViewById(R.id.mapView1);
//		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
		
		imageView.setImageAlpha(25);
		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);

		textView.setText(values.get(position).getLocations());

		// Change icon based on name
//		String s = values.get(position).getTitle();
//
//		System.out.println(s);

		// imageView.setImageResource(R.drawable.film_reel_sm);
		

		return rowView;
	}

}
