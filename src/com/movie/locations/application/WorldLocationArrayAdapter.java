package com.movie.locations.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.movie.locations.R;
import com.movie.locations.dao.QuizItemImpl;
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

public class WorldLocationArrayAdapter extends ArrayAdapter<WorldLocationObject> {
	private final Context context;
	private final ArrayList<WorldLocationObject> values;
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
	private QuizItemImpl quizitemsource;

	public WorldLocationArrayAdapter(Context context, Intent intent, ArrayList<WorldLocationObject> values) {
		super(context, R.layout.film_list_array_adapter, values);
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

		View rowView = inflater.inflate(R.layout.film_list_array_adapter,
				parent, false);

		final Intent quizIntent = new Intent(context, QuizActivity.class);
		
		// get intent string attributes
//		Bundle bundle = intent.getExtras();
//		System.out.println("BUNDLE INTENT: " + bundle);
//		User localUser = bundle.getParcelable("localUser");
//		QuizItem quizItem = bundle.getParcelable("quizItem");
		
		quizitemsource = new QuizItemImpl(context);
		
		QuizItem quizItemMatch = quizitemsource.selectRecordById(values.get(position).getId());
	
		quizIntent.putExtra("quizItem", quizItemMatch);
		
//		currentQuizItem = quizitemsource.selectRecordById(questionId);
		
//		Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
//		intent.putExtra("myKey",AnyValue);  
//		startActivity(intent);
				
//		Bundle quizBundle = quizIntent.getExtras();
		
//		// build new bundle with combined attributes
//		quizIntent.putExtra("position", values.get(position).getPosition());
//		quizIntent.putExtra("createdAt", values.get(position).getCreatedAt());
//		quizIntent.putExtra("createdMeta", values.get(position).getCreatedMeta());
//		quizIntent.putExtra("updatedAt", values.get(position).getUpdatedAt());
//		quizIntent.putExtra("updatedMeta", values.get(position).getUpdatedMeta());
//		quizIntent.putExtra("meta", values.get(position).getMeta());
//		quizIntent.putExtra("title", values.get(position).getTitle());
//		quizIntent.putExtra("releaseYear", values.get(position).getReleaseYear());
//		quizIntent.putExtra("funFacts", values.get(position).getFunFacts());
//		quizIntent.putExtra("productionCompany", values.get(position).getProductionCompany());
//		quizIntent.putExtra("distributor", values.get(position).getDistributor());
//		quizIntent.putExtra("director", values.get(position).getDirector());
//		quizIntent.putExtra("writer", values.get(position).getWriter());
//		quizIntent.putExtra("actor1", values.get(position).getActor1());
//		quizIntent.putExtra("actor2", values.get(position).getActor2());
//		quizIntent.putExtra("actor3", values.get(position).getActor3());
//		quizIntent.putExtra("geolocation", values.get(position).getGeolocation());
//		quizIntent.putExtra("locations", values.get(position).getLocations());
//		quizIntent.putExtra("latitude", values.get(position).getLatitude());
//		quizIntent.putExtra("longitude", values.get(position).getLongitude());
//		quizIntent.putExtra("sid", values.get(position).getPosition());
//		quizIntent.putExtra("id", values.get(position).getId());
//		quizIntent.putExtra("level", values.get(position).getLevel());
//		quizIntent.putExtra("staticMapImageUrl", values.get(position).getStaticMapImageUrl());
//		quizIntent.putExtra("questionId", values.get(position).getQuestionId());
//		quizIntent.putExtra("questionText", values.get(position).getQuestionText());
//		quizIntent.putExtra("dateTime", values.get(position).getDateTime());
//		quizIntent.putExtra("answer1", values.get(position).getAnswer1());
//		quizIntent.putExtra("answer2", values.get(position).getAnswer2());
//		quizIntent.putExtra("answer3", values.get(position).getAnswer3());
//		quizIntent.putExtra("answer4", values.get(position).getAnswer4());
//		quizIntent.putExtra("reaction1", values.get(position).getReaction1());
//		quizIntent.putExtra("reaction2", values.get(position).getReaction2());
//		quizIntent.putExtra("reaction3", values.get(position).getReaction3());
//		quizIntent.putExtra("reaction4", values.get(position).getReaction4());
//		quizIntent.putExtra("worldId", values.get(position).getWorldId());
//		quizIntent.putExtra("worldTitle", values.get(position).getWorldTitle());
//		quizIntent.putExtra("submittedAnswerIndex", values.get(position).getSubmittedAnswerIndex());
//		quizIntent.putExtra("answered", values.get(position).getAnswered());
//		quizIntent.putExtra("activeItem", values.get(position).getActiveItem());
//		quizIntent.putExtra("activeItem1", values.get(position).getActiveItem1());
//		quizIntent.putExtra("activeItem2", values.get(position).getActiveItem2());
//		quizIntent.putExtra("activeItem3", values.get(position).getActiveItem3());
//		quizIntent.putExtra("activeItem4", values.get(position).getActiveItem4());
////		bundle.putString("answered", values.get(position).getAnswered());
//		quizIntent.putExtra("correctAnswerIndex", values.get(position).getCorrectAnswerIndex());
		
//		QuizItemImpl quizsource;
//		
//		quizsource = new QuizItemImpl(context);
//		
//		ArrayList<QuizItem> quizList = quizsource.selectRecords();
//		
//		LinkedHashMap<String, QuizItem> quizMap = new LinkedHashMap<String, QuizItem>();
//		
//		quizMap = new LinkedHashMap<String, QuizItem>();
		
//		if (quizList != null) {
//			for (QuizItem item : quizList) {
//				quizMap.put(item.getWorldId(), item);
////				System.out.println("FILM ANSWER ONE (1) FROM DATABASE: " + item.getAnswer1());
////				System.out.println("FILM ANSWER ONE (2) FROM DATABASE: " + item.getAnswer2());
////				System.out.println("FILM ANSWER ONE (3) FROM DATABASE: " + item.getAnswer3());
////				System.out.println("FILM ANSWER ONE (4) FROM DATABASE: " + item.getAnswer4());
//				
//				System.out.println("QUIZ ITEM WORLD ID: " + item.getWorldId());
//				
////				  System.out.println("FILM TITLES FROM DATABASE: " + item.getFilmTitle());
////				System.out.println("FILM QUESTION ID FROM DATABASE: " + item.getQuestionId());
////				  System.out.println("FILM QUESTION TEXT FROM DATABASE: " + item.getQuestionText());
////				System.out.println("FILM ANSWERED FROM DATABASE: " + item.getAnswered());
//			}
//		}
		
		
//		QuizItem quizItemMatch = quizsource.selectRecordById(values.get(position).getId());
//		
//		if (quizItemMatch != null) {
//			System.out.println("FOUND QUIZ ITEM MATCH: " + quizItemMatch.getWorldId());
//			quizIntent.putExtra("quizItem", quizItemMatch);
////			intent.putExtra("quizItem", quizMap.get(values.get(position).getId()));
//		}
		
		
		// set quizitem attributes
//		QuizItem quizItem = new QuizItem();
//		quizItem.setQuestionId(cursor.getString(0));
////		quizItem.setFilmTitle(cursor.getString(1));
//		quizItem.setDateTime(cursor.getString(1));
//		quizItem.setQuestionText(cursor.getString(2));
//		quizItem.setAnswer1(cursor.getString(3));
//		quizItem.setAnswer2(cursor.getString(4));
//		quizItem.setAnswer3(cursor.getString(5));
//		quizItem.setAnswer4(cursor.getString(6));
//		quizItem.setReaction1(cursor.getString(7));
//		quizItem.setReaction2(cursor.getString(8));
//		quizItem.setReaction3(cursor.getString(9));
//		quizItem.setReaction4(cursor.getString(10));
//		quizItem.setWorldId(cursor.getString(11));
//		quizItem.setWorldTitle(cursor.getString(12));
//		quizItem.setAnswered(cursor.getString(13));
//		quizItem.setLevel(cursor.getString(14));
//		quizItem.setActiveItem(cursor.getString(15));
//		quizItem.setActiveItem1(cursor.getString(16));
//		quizItem.setActiveItem2(cursor.getString(17));
//		quizItem.setActiveItem3(cursor.getString(18));
//		quizItem.setActiveItem4(cursor.getString(19));
		
		// JUST USE THE PARCEL TO GET QUESTION DATA FROM BUNDLE
		
//		quizIntent.putExtra("questionId", values.get(position));
		
		
		
		
//		mapIntent.putExtra("title", values.get(position).getTitle());
//		mapIntent.putExtra("latitude", values.get(position).getLatitude());
//		mapIntent.putExtra("longitude", values.get(position).getLongitude());
//		mapIntent.putExtra("location", values.get(position).getLocations());
//		mapIntent.putExtra("funFacts", values.get(position).getFunFacts());
//		
//		mapIntent.putExtra("localUser", localUser);
		
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
		
		if (!values.get(position).getLatitude().equals("")) {
			rowView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					context.startActivity(quizIntent);
				}
			});	
		}

		TextView textView = (TextView) rowView.findViewById(R.id.label);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);

		if (values.get(position).getLocations() != null) {
			String formattedLocation = removeParenthesis((values.get(position)
					.getLocations()));

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
			
			UNIQUE_MAP_IMAGE_URL = values.get(position).getStaticMapImageUrl();
			
//			imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
		} else {
			UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
			// imageView.setVisibility(ImageView.GONE);

			// defaultMapImage
			// defaultMapThumb.setVisibility(ImageView.VISIBLE);
		}

		// ImageView mapThumb = (ImageView) rowView.findViewById(R.id.mapView1);
//		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);

		textView.setText(values.get(position).getLocations());

		// Change icon based on name
		String s = values.get(position).getTitle();

		System.out.println(s);

		// imageView.setImageResource(R.drawable.film_reel_sm);

		return rowView;
	}

}
