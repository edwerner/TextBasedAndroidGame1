package com.movie.locations.adapter;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.domain.NavMenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NavMenuItemArrayAdapter extends ArrayAdapter<NavMenuItem> {
	private final Context context;
	private final ArrayList<NavMenuItem> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public String UNIQUE_MAP_IMAGE_URL = "";
	public final String DEFAULT_MAP_IMAGE_URL = "http://maps.googleapis.com/maps/api/staticmap?center=San+Francisco+California&zoom=14&size=200x200&sensor=true";
	public final String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
	public String CENTER = "";
	public String SETTINGS = "&zoom=16&size=200x200&sensor=true";
//	private Intent intent;

	public NavMenuItemArrayAdapter(Context context, Intent intent,
			ArrayList<NavMenuItem> values) {
		super(context, R.layout.bag_item_array_adapter, values);
		this.context = context;
//		this.intent = intent;
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

		ImageView imageView = (ImageView) rowView.findViewById(R.id.navMenuItemImage1);
		TextView bagItemTitle = (TextView) rowView.findViewById(R.id.navMenuItemTitle1);
//		TextView bagItemLevel = (TextView) rowView.findViewById(R.id.bagItemLevel1);
//		TextView bagItemDescription = (TextView) rowView.findViewById(R.id.bagItemDescription1);
		
//		TextView bagItemDescription = (TextView) rowView.findViewById(R.id.bagItemDescription1);
		
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
    	builder.setPositiveButton(CONFIRM_MESSAGE, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
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

		return rowView;
	}
}