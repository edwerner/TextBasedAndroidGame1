package com.movie.locations.application;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GameTitleArrayAdapter extends ArrayAdapter<GameTitle> {
	private final Context context;
	private final ArrayList<GameTitle> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public String UNIQUE_MAP_IMAGE_URL = "";
	public final String DEFAULT_MAP_IMAGE_URL = "http://maps.googleapis.com/maps/api/staticmap?center=San+Francisco+California&zoom=14&size=200x200&sensor=true";
	public final String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
	public String CENTER = "";
	public String SETTINGS = "&zoom=16&size=200x200&sensor=true";
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
		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.bagItemImage1);
		TextView bagItemTitle = (TextView) rowView.findViewById(R.id.bagItemTitle1);
		String tempPhase = values.get(position).getPhase();
		String gameTitleText = values.get(position).getTitle();
		String missingWorldText = " (missing)";
    	String CONFIRM_MESSAGE = "Yes";
    	String CANCEL_MESSAGE = "No";
    	String DIALOG_TITLE = "Restore";
    	String DIALOG_MESSAGE = "Are you sure? You'll lose any existing progress.";
		final String itemLevelText = " - Item level " + values.get(position).getLevel();
		String BAG_ITEM_IMAGE_URL = "http://img2.wikia.nocookie.net/__cb20100902001548/frontierville/images/b/bb/Oak_Tree_Medium-icon.png";
		DIALOG_TITLE +=  " current world?";
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
			
        	// Create the AlertDialog
        	final AlertDialog dialog = builder.create();
    		rowView.setOnClickListener(new View.OnClickListener() {
    		    public void onClick(View v) {
    		    	dialog.show();
    		    }
    		});
	    		
		} else if (tempPhase.equals("EXISTS") && !values.get(position).getType().equals("WORLD_TITLE")) {
			gameTitleText += itemLevelText;
			String existingImageUrl = values.get(position).getImageUrl();
			if (existingImageUrl != null) {
				BAG_ITEM_IMAGE_URL = existingImageUrl;
			}
			final Intent conclusionIntent = new Intent(context, ConclusionActivity.class);

			// get intent string attributes
			Bundle bundle = intent.getExtras();
			User localUser = bundle.getParcelable("localUser");
			conclusionIntent.putExtra("id", values.get(position).getId());
			conclusionIntent.putExtra("title", values.get(position).getTitle());
			conclusionIntent.putExtra("type", values.get(position).getType());
			conclusionIntent.putExtra("level", values.get(position).getLevel());
			conclusionIntent.putExtra("imageUrl", values.get(position).getImageUrl());
			conclusionIntent.putExtra("localUser", localUser);
			if (!values.get(position).getId().equals(null)) {
	    		rowView.setOnClickListener(new View.OnClickListener() {
	    		    public void onClick(View v) {
	    		    	context.startActivity(conclusionIntent);
	    		    }
	    		});
			}
		} else if (tempPhase.equals("EXISTS") && values.get(position).getType().equals("WORLD_TITLE")) {
			// SET INTENT TO RESTORE CONTENT
	    	// CREATE CONFIRMATION DIALOG
	    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    	builder.setMessage(DIALOG_MESSAGE).setTitle(DIALOG_TITLE);
	    	
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
        	// Create the AlertDialog
        	final AlertDialog dialog = builder.create();
    		rowView.setOnClickListener(new View.OnClickListener() {
    		    public void onClick(View v) {
    		    	dialog.show();
    		    }
    		});
		}
		bagItemTitle.setText(gameTitleText);
		imageLoader.displayImage(BAG_ITEM_IMAGE_URL, imageView);

		return rowView;
	}
}