package com.movie.locations.application;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.movie.locations.R;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.Comment;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.NewsItem;
import com.movie.locations.domain.QuizItem;
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

public class NewsArrayAdapter extends ArrayAdapter<NewsItem> {
	private final Context context;
	private final ArrayList<NewsItem> values;
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

	public NewsArrayAdapter(Context context, Intent intent,
			ArrayList<NewsItem> values) {
		super(context, R.layout.news_update_array_adapter, values);
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

		View rowView = inflater.inflate(R.layout.news_update_array_adapter,
				parent, false);

		final Intent commentIntent = new Intent(context, CommentDetailActivity.class);
		
		// get intent string attributes
		Bundle bundle = intent.getExtras();
//		System.out.println("BUNDLE INTENT: " + bundle);
		User localUser = bundle.getParcelable("localUser");
		
//		pc.writeString(userId);
//		pc.writeString(dateTime);
//		pc.writeString(filmId);
//		pc.writeString(comment);
//		pc.writeString(commentId);
		
		
		
		
//		String title;
//		String text;
//		String userId;
//		String achievementId;
//		String dateTime;
		
		commentIntent.putExtra("title", values.get(position).getTitle());
		commentIntent.putExtra("text", values.get(position).getText());
//		commentIntent.putExtra("userId", values.get(position).getUserId());
//		commentIntent.putExtra("dateTime", values.get(position).getDateTime());
//		commentIntent.putExtra("achievementId", values.get(position).getAchievementId());
		
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
//		if (!values.get(position).getText().equals(null)) {
			rowView.setOnClickListener(null);	
//		} 

//		TextView nameView = (TextView) rowView.findViewById(R.id.userDisplayName);
		TextView newsTitleText = (TextView) rowView.findViewById(R.id.newsUpdateTitleText1);
		TextView newsText = (TextView) rowView.findViewById(R.id.newsUpdateText1);
		TextView newsDescriptionText = (TextView) rowView.findViewById(R.id.newsDescriptionText1);
		
		String FINAL_NEWS_ITEM_IMAGE_URL = values.get(position).getImageUrl();
		
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

//		nameView.setText(values.get(position).getUserId());
//		newsTitleText.setText(values.get(position).getTitle());
		
		String FINAL_NEWS_TYPE_TITLE_TEXT = "Latest Game Item";
		String FINAL_NEWS_TEXT_PREFIX = "Game item description: ";
//		String FINAL_NEWS_TYPE_DESCRIPTION_TEXT = values.get(position).getText();
		String FINAL_NEWS_TEXT = "";
		
//		QuizItem
//		Location
//		ConclusionCard
//		BagItem
		
		if (values.get(position).getText() != null) {
			FINAL_NEWS_TEXT = values.get(position).getText();
			if (values.get(position).getNewsType().equals("QuizItem")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "Latest Quiz Item";
				FINAL_NEWS_TEXT_PREFIX = "Answer: ";
			} else if (values.get(position).getNewsType().equals("Location")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "Latest world location complete";
				FINAL_NEWS_TEXT_PREFIX = "";
			} else if (values.get(position).getNewsType().equals("BagItem")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "Latest bag item";
				FINAL_NEWS_TEXT_PREFIX = "Tagline: ";
			} else if (values.get(position).getNewsType().equals("ConclusionCard")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "You got a card!";
				FINAL_NEWS_TEXT_PREFIX = "Card Title: ";
			}
			String FINAL_NEWS_MESSAGE = FINAL_NEWS_TEXT_PREFIX + FINAL_NEWS_TEXT;
			newsText.setText(FINAL_NEWS_MESSAGE);
		} else {
			if (values.get(position).getNewsType().equals("QuizItem")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "No quiz items answered.";
				FINAL_NEWS_TEXT_PREFIX = "";
			} else if (values.get(position).getNewsType().equals("Location")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "No world locations completed.";
				FINAL_NEWS_TEXT_PREFIX = "";
			} else if (values.get(position).getNewsType().equals("BagItem")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "No new bag items.";
				FINAL_NEWS_TEXT_PREFIX = "";
			} else if (values.get(position).getNewsType().equals("ConclusionCard")) {
				FINAL_NEWS_TYPE_TITLE_TEXT = "No new cards.";
				FINAL_NEWS_TEXT_PREFIX = "";
			}
			FINAL_NEWS_ITEM_IMAGE_URL = "http://hakenberg.de/_images/icon.red.gif";
		}
		
		imageLoader.displayImage(FINAL_NEWS_ITEM_IMAGE_URL, imageView);

		newsTitleText.setText(FINAL_NEWS_TYPE_TITLE_TEXT);

		
		
//		newsDescriptionText.setText(FINAL_NEWS_TEXT);

		// Change icon based on name
//		String s = "NEW COMMENT ADDED: " + values.get(position).getComment();

//		System.out.println(s);

		// imageView.setImageResource(R.drawable.film_reel_sm);

		return rowView;
	}

}

