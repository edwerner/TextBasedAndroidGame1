package com.movie.locations.application;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.QuizItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationQuizArrayAdapter extends ArrayAdapter<QuizItem> {
	private final Context context;
	private final ArrayList<QuizItem> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public String UNIQUE_MAP_IMAGE_URL = "";
	public final String DEFAULT_MAP_IMAGE_URL = "http://staticmap.openstreetmap.de/staticmap.php?center=48.854182,2.371105&zoom=13&size=500x500&maptype=mapnik";
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
		
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		
		// reveal completed indicator checkmark
		// System.out.println("VALUES LENGTH: " + values.size());
		if (values.get(position).getAnswered().equals("true")) {
			ImageView checkmarkImageView = (ImageView) rowView.findViewById(R.id.green_checkmark_small);
			checkmarkImageView.setVisibility(ImageView.VISIBLE);
			// System.out.println("GREEN CHECKMARK");
			final int HALFTONE = 50;
			imageView.setImageAlpha(HALFTONE);
		}
		
		String[] titles = getListItemTitles();
		String[] imageTiles = getListItemImageTiles();
		// System.out.println("IMAGE TILES LENGTH: " + imageTiles.length);
		if (imageTiles[position] != null) {
			UNIQUE_MAP_IMAGE_URL = imageTiles[position];
		} else {
			UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
		}

		// ImageView mapThumb = (ImageView) rowView.findViewById(R.id.mapView1);
		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
		textView.setText(titles[position]);

//		final int randomFilterColor = colors[getRandomColor()];
		imageView.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
		imageView.setImageAlpha(50);
		
		// imageView.setImageResource(R.drawable.film_reel_sm);

		return rowView;
	}
	

	public int[] colors = {
			Color.BLUE,
			Color.CYAN,
			Color.GRAY,
			Color.MAGENTA,
			Color.RED,
			Color.YELLOW
	};
	public int getRandomColor() {
	    Random rand = new Random();
	    int randomNum = rand.nextInt(colors.length);
	    return randomNum;
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
