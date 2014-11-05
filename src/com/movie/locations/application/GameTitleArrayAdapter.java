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
import com.movie.locations.domain.GameTitle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GameTitleArrayAdapter extends ArrayAdapter<GameTitle> {
	private final Context context;
	private final ArrayList<GameTitle> values;
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

	public GameTitleArrayAdapter(Context context, Intent intent,
			ArrayList<GameTitle> values) {
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

		View rowView = inflater.inflate(R.layout.game_title_array_adapter,
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
		
		

		ImageView imageView = (ImageView) rowView.findViewById(R.id.bagItemImage1);
		TextView bagItemTitle = (TextView) rowView.findViewById(R.id.bagItemTitle1);
		TextView bagItemLevel = (TextView) rowView.findViewById(R.id.bagItemLevel1);
		TextView bagItemDescription = (TextView) rowView.findViewById(R.id.bagItemDescription1);
		
//		TextView bagItemDescription = (TextView) rowView.findViewById(R.id.bagItemDescription1);
		
		
		String tempPhase = values.get(position).getPhase();
		final int bgMissingColor = R.color.grey_overlay;
		String gameTitleText = values.get(position).getTitle();
		String missingWorldText = " (missing)";
		String resetWorldText = " (reset)";
		String restoreWorldText = " (restore)";
    	String CONFIRM_MESSAGE = "Yes";
    	String CANCEL_MESSAGE = "No";
    	String DIALOG_TITLE = "Restore";
    	String DIALOG_MESSAGE = "Are you sure? You'll lose any existing progress.";
    	final String currentTitle = values.get(position).getTitle();
    	final String currentTitleType = values.get(position).getType();
		final String itemLevelText = " - Item level " + values.get(position).getLevel();
//		String BAG_ITEM_IMAGE_URL = "BAG_ITEM_IMAGE_URL";
		String BAG_ITEM_IMAGE_URL = "http://img2.wikia.nocookie.net/__cb20100902001548/frontierville/images/b/bb/Oak_Tree_Medium-icon.png";
		String RESTORE_LEVEL_DATA_URL = "RESTORE_LEVEL_DATA_URL";
		DIALOG_TITLE +=  " current world?";
		
		if (values.get(position).getType().equals("WORLD_TITLE")) {
			RESTORE_LEVEL_DATA_URL = "http://movie-locations-app.appspot.com/secure/restoreLevelData/" + currentTitle;
		} else if (values.get(position).getType().equals("CARD_TITLE") 
				|| values.get(position).getType().equals("BAG_ITEM_TITLE")
				|| values.get(position).getType().equals("NEWS_ITEM_TITLE")) {
//			/restoreGameItems/{type}/{title}
			RESTORE_LEVEL_DATA_URL = "http://movie-locations-app.appspot.com/secure/restoreGameItems/" + currentTitleType + "/" + currentTitle;
//			RESTORE_LEVEL_DATA_URL += currentTitle;
		}
//		RESTORE_LEVEL_DATA_URL += currentTitle;
		
		final String FINAL_RESTORE_LEVEL_DATA_URL = RESTORE_LEVEL_DATA_URL;
		
		if (tempPhase.equals("MISSING") || tempPhase.equals("null")) {
//			rowView.setBackgroundColor(bgMissingColor);
			rowView.setAlpha((float) 0.5);
			gameTitleText += missingWorldText;
			BAG_ITEM_IMAGE_URL = "https://cdn0.iconfinder.com/data/icons/vector-basic-tab-bar-icons/48/help-128.png";
//			RESTORE_LEVEL_DATA_URL = "http://movie-locations-app.appspot.com/secure/restoreLevelData/";
//			DIALOG_TITLE = "Restore";
			
			
			

			
	    	
	        
			
			
			
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
	        	   runner.execute(FINAL_RESTORE_LEVEL_DATA_URL);
	        	   
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
			
//	    	Button restoreLevelDataButton = (Button) rowView.findViewById(R.id.restoreLevelData);
	    	
//	    	if (values.get(position).getTitle().equals("Restore")) {
//	    		restoreLevelDataButton.setVisibility(Button.VISIBLE);
		    	
	        	// Create the AlertDialog
	        	final AlertDialog dialog = builder.create();
	        	
//	        	((Activity) context).registerForContextMenu(rowView);
	    		
	    		rowView.setOnClickListener(new View.OnClickListener() {
	    		    public void onClick(View v) {
	    		    	dialog.show();
	    		    }
	    		});
//	    	}
	    		
		} else if (tempPhase.equals("EXISTS") && !values.get(position).getType().equals("WORLD_TITLE")) {
			// ADD RESET BUTTON STATE
			// CLICK AND HOLD EXISTING TO RESTORE
			// TODO: CREATE CONTEXTUAL MENU FOR THIS
//			gameTitleText += resetWorldText;
			gameTitleText += itemLevelText;
//			BAG_ITEM_IMAGE_URL = "http://img2.wikia.nocookie.net/__cb20100902001548/frontierville/images/b/bb/Oak_Tree_Medium-icon.png";
			String existingImageUrl = values.get(position).getImageUrl();
			if (existingImageUrl != null) {
				BAG_ITEM_IMAGE_URL = existingImageUrl;
			}


			final Intent conclusionIntent = new Intent(context, ConclusionActivity.class);
			
			// get intent string attributes
			Bundle bundle = intent.getExtras();
//			System.out.println("BUNDLE INTENT: " + bundle);
			User localUser = bundle.getParcelable("localUser");
			
//			pc.writeString(userId);
//			pc.writeString(dateTime);
//			pc.writeString(filmId);
//			pc.writeString(comment);
//			pc.writeString(commentId);
			
//			private String id;
//			private String title;
//			private String copy;
//			private String imageUrl;
//			private String level;
			
//			private String id;
//			private String title;
//			private String type;
//			private String level;
//			private String phase;
//			private String imageUrl;
			
			conclusionIntent.putExtra("id", values.get(position).getId());
			conclusionIntent.putExtra("title", values.get(position).getTitle());
			conclusionIntent.putExtra("type", values.get(position).getType());
			conclusionIntent.putExtra("level", values.get(position).getLevel());
			conclusionIntent.putExtra("imageUrl", values.get(position).getImageUrl());
			
			conclusionIntent.putExtra("localUser", localUser);
			
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
			if (!values.get(position).getId().equals(null)) {
	    		rowView.setOnClickListener(new View.OnClickListener() {
	    		    public void onClick(View v) {
	    		    	context.startActivity(conclusionIntent);
	    		    }
	    		});
			}
			
			
			
//			RESTORE_LEVEL_DATA_URL = "http://movie-locations-app.appspot.com/secure/resetLevelData/";
//			DIALOG_TITLE = "Reset";
		} else if (tempPhase.equals("EXISTS") && values.get(position).getType().equals("WORLD_TITLE")) {
			// SET INTENT TO RESTORE CONTENT

			
			

			
	    	
	        
			
			
			
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
//	        	   RestoreLevelDataTaskRunner runner = new RestoreLevelDataTaskRunner();
//	        	   runner.execute(FINAL_RESTORE_LEVEL_DATA_URL);
	        	   
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
			
//	    	Button restoreLevelDataButton = (Button) rowView.findViewById(R.id.restoreLevelData);
	    	
//	    	if (values.get(position).getTitle().equals("Restore")) {
//	    		restoreLevelDataButton.setVisibility(Button.VISIBLE);
		    	
	        	// Create the AlertDialog
	        	final AlertDialog dialog = builder.create();
	        	
//	        	((Activity) context).registerForContextMenu(rowView);
	    		
	    		rowView.setOnClickListener(new View.OnClickListener() {
	    		    public void onClick(View v) {
	    		    	dialog.show();
	    		    }
	    		});
//	    	}
		}
		

		
		// bagItemImage1
		// bagItemLevel1
		// bagItemDescription1
		// bagItemImageUrl
		
		bagItemTitle.setText(gameTitleText);
//		bagItemLevel.setText(values.get(position).get);
//		bagItemDescription.setText(values.get(position).getDescription());
		
		// TODO: REPLACE THIS WITH A DRAWABLE RESOURCE
//		BAG_ITEM_IMAGE_URL = "https://cdn0.iconfinder.com/data/icons/vector-basic-tab-bar-icons/48/help-128.png";
		imageLoader.displayImage(BAG_ITEM_IMAGE_URL, imageView);
		

		

//		for (GameTitle title : gameTitleList) {
//			String tempTitle = title.getTitle();
//			tempTitle = tempTitle.replaceAll(" ", "");
//			FilmLocation tempLocation = datasource.selectRecordById(tempTitle);
//			if (tempLocation != null) {
//				// SET UI TO LOCATION
//			} else {
//				// SET UI TO MISSING LOCATION
//				worldStatus = "MISSING";
//				title.setPhase(worldStatus);
//				gameTitleList.set(counter, title);				
//			}
//			counter++;
//		}
		
		
		
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

