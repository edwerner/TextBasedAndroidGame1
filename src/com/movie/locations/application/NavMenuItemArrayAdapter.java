package com.movie.locations.application;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.movie.locations.R;
import com.movie.locations.application.NewsActivity.RestoreLevelDataTaskRunner;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.Comment;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.NavMenuItem;
import com.movie.locations.domain.NewsItem;
import com.movie.locations.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NavMenuItemArrayAdapter extends ArrayAdapter<NavMenuItem> {
	private final Context context;
	private final ArrayList<NavMenuItem> values;
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

	public NavMenuItemArrayAdapter(Context context, Intent intent,
			ArrayList<NavMenuItem> values) {
		super(context, R.layout.bag_item_array_adapter, values);
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

		final View rowView = inflater.inflate(R.layout.nav_menu_item_array_adapter,
				parent, false);

//		final Intent commentIntent = new Intent(context, CommentDetailActivity.class);
		
		// get intent string attributes
//		Bundle bundle = intent.getExtras();
//		System.out.println("BUNDLE INTENT: " + bundle);
//		User localUser = bundle.getParcelable("localUser");
		
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
		
//		commentIntent.putExtra("title", values.get(position).getItemTitle());
//		commentIntent.putExtra("level", values.get(position).getLevel());
//		commentIntent.putExtra("description", values.get(position).getDescription());
//		commentIntent.putExtra("imageUrl", values.get(position).getImageUrl());
//		
//		
//		commentIntent.putExtra("localUser", localUser);
		
//		navMenuItemImage1
//		navMenuItemTitle1
		

		ImageView imageView = (ImageView) rowView.findViewById(R.id.navMenuItemImage1);
		TextView bagItemTitle = (TextView) rowView.findViewById(R.id.navMenuItemTitle1);
//		TextView bagItemLevel = (TextView) rowView.findViewById(R.id.bagItemLevel1);
//		TextView bagItemDescription = (TextView) rowView.findViewById(R.id.bagItemDescription1);
		
//		TextView bagItemDescription = (TextView) rowView.findViewById(R.id.bagItemDescription1);
		
		
		// bagItemImage1
		// bagItemLevel1
		// bagItemDescription1
		// bagItemImageUrl
		
		bagItemTitle.setText(values.get(position).getTitle());
//		bagItemLevel.setText(values.get(position).getLevel());
//		bagItemDescription.setText(values.get(position).getDescription());
		
		final String BAG_ITEM_IMAGE_URL = values.get(position).getImageUrl();
		imageLoader.displayImage(BAG_ITEM_IMAGE_URL, imageView);
		
    	String CONFIRM_MESSAGE = "Yes";
    	String CANCEL_MESSAGE = "No";
    	String DIALOG_TITLE = "Restore current world";
    	String DIALOG_MESSAGE = "Are you sure? You'll lose any existing progress.";
		
		
		
    	// CREATE CONFIRMATION DIALOG
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	
    	builder.setMessage(DIALOG_MESSAGE).setTitle(DIALOG_TITLE);
    	
    	// TODO: ITERATE AND DELETE CURRENT LEVEL DATA
    	// - BAG ITEMS
    	// - QUIZ ITEMS
    	// - LOCATIONS
    	
    	
    	

    	
    	
    	
		// Add the buttons
    	builder.setPositiveButton(CONFIRM_MESSAGE, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   
        	   // START ASYNC THREAD
        	   RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
        	   runner.execute("http://movie-locations-app.appspot.com/secure/restoreLevelData");
        	   
               // User clicked OK button
        	   System.out.println("RESTORED");
           }
       });
    	builder.setNegativeButton(CANCEL_MESSAGE, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   
               // User cancelled the dialog
        	   System.out.println("CANCELLED");
           }
       });
		
//    	Button restoreLevelDataButton = (Button) rowView.findViewById(R.id.restoreLevelData);
    	
//    	if (values.get(position).getTitle().equals("Restore")) {
////    		restoreLevelDataButton.setVisibility(Button.VISIBLE);
//	    	
//        	// Create the AlertDialog
//        	final AlertDialog dialog = builder.create();
//        	
//    		
//    		rowView.setOnClickListener(new View.OnClickListener() {
//    		    public void onClick(View v) {
//    		    	dialog.show();
//    		    }
//    		});
//    	}
		
		
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
//			rowView.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					context.startActivity(commentIntent);
//				}
//			});	
//		}

//		TextView nameView = (TextView) rowView.findViewById(R.id.userDisplayName);
//		TextView newsView = (TextView) rowView.findViewById(R.id.newsUpdateText1);
//
//		ImageView imageView = (ImageView) rowView.findViewById(R.id.userCommentAvatar);

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
//		newsView.setText(values.get(position).getText());

		// Change icon based on name
//		String s = "NEW COMMENT ADDED: " + values.get(position).getComment();

//		System.out.println(s);

		// imageView.setImageResource(R.drawable.film_reel_sm);

		return rowView;
	}

}

