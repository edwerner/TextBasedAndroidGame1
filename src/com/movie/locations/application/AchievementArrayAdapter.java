package com.movie.locations.application;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.movie.locations.R;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.Comment;
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

public class AchievementArrayAdapter extends ArrayAdapter<Achievement> {
	private final Context context;
	private final ArrayList<Achievement> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private final String SEARCH_DELIMITER = "+San+Francisco+California";
	public String UNIQUE_MAP_IMAGE_URL = "";
	public final String DEFAULT_MAP_IMAGE_URL = "http://maps.googleapis.com/maps/api/staticmap?center=San+Francisco+California&zoom=14&size=200x200&sensor=true";
	public final String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
	public String CENTER = "";
	public String SETTINGS = "&zoom=16&size=200x200&sensor=true";
	private String MOVIE_POSTER_URL = "";
	private User user;
	private Intent intent;

	public AchievementArrayAdapter(Context context, Intent intent,
			ArrayList<Achievement> values) {
		super(context, R.layout.comment_array_adapter, values);
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

		View rowView = inflater.inflate(R.layout.achievement_array_adapter,
				parent, false);

		final Intent commentIntent = new Intent(context, CommentDetailActivity.class);
		
		// get intent string attributes
		Bundle bundle = intent.getExtras();
//		System.out.println("BUNDLE INTENT: " + bundle);
		User localUser = bundle.getParcelable("localUser");
		
//		String title;
//		String catchPhrase;
//		String userId;
//		String dateTime;
		
		commentIntent.putExtra("userId", values.get(position).getUserId());
		commentIntent.putExtra("dateTime", values.get(position).getDateTime());
		commentIntent.putExtra("title", values.get(position).getTitle());
//		commentIntent.putExtra("catchPhrase", values.get(position).getCatchPhrase());
		
		commentIntent.putExtra("localUser", localUser);
		
		// leave listener attached leaving
		// any null location map view hidden
		//
		// this way we can let the user add
		// a location to reveal the map view
		// 
		// this means we're only setting the
		// adapter if location is not null
		
		// if the map quota goes over, we need to prevent
		// the user from passing an empty value to the map
		// detail activity by blocking click events
		if (!values.get(position).getTitle().equals(null)) {
			rowView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					context.startActivity(commentIntent);
				}
			});	
		}

		TextView nameView = (TextView) rowView.findViewById(R.id.userDisplayName);
		TextView commentView = (TextView) rowView.findViewById(R.id.userComment);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.userCommentAvatar);

//		if (values.get(position).getLocations() != null) {
//			String formattedLocation = removeParenthesis((values.get(position)
//					.getLocations()));
//
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
//			imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
//		} else {
//			UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
//			// imageView.setVisibility(ImageView.GONE);
//
//			// defaultMapImage
//			// defaultMapThumb.setVisibility(ImageView.VISIBLE);
//		}

		
		
		// ImageView mapThumb = (ImageView) rowView.findViewById(R.id.mapView1);
//		imageLoader.displayImage(DEFAULT_MAP_IMAGE_URL, imageView);
		imageView.setImageResource(R.drawable.ic_launcher);

		nameView.setText("Achievement date");
		commentView.setText(values.get(position).getTitle());

		// Change icon based on name
//		String s = "NEW COMMENT ADDED: " + values.get(position).getTitle();

//		System.out.println(s);

		// imageView.setImageResource(R.drawable.film_reel_sm);

		return rowView;
	}

}

