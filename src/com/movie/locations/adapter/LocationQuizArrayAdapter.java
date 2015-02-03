package com.movie.locations.adapter;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.adapter.LocationArrayAdapter.ViewHolder;
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
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public String UNIQUE_MAP_IMAGE_URL = "";
	public final String DEFAULT_MAP_IMAGE_URL = "http://staticmap.openstreetmap.de/staticmap.php?center=48.854182,2.371105&zoom=13&size=500x500&maptype=mapnik";
	private String[] listItemTitles;
	private String[] listItemImageTiles;
	private ArrayList<BagItem> bagItemList;

	public LocationQuizArrayAdapter(Activity activity, Context context, Intent intent, ArrayList<QuizItem> values) {
		super(context, R.layout.film_list_array_adapter, values);
		this.context = context;
	}

	public String removeParenthesis(String string) {
		String regex = string.replaceAll("\\(", " ");
		regex = regex.replaceAll("\\)", " ");
		return regex;
	}

	static class ViewHolder {
		TextView worldText;
		ImageView worldImage;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final ViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.film_list_array_adapter, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.worldText = (TextView) convertView.findViewById(R.id.label);
			viewHolder.worldImage = (ImageView) convertView.findViewById(R.id.logo);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
//		// reveal completed indicator checkmark
//		if (values.get(position).getAnswered().equals("true")) {
//			ImageView checkmarkImageView = (ImageView) rowView.findViewById(R.id.green_checkmark_small);
//			checkmarkImageView.setVisibility(ImageView.VISIBLE);
//			final int HALFTONE = 50;
//			imageView.setImageAlpha(HALFTONE);
//		}
		
		final String[] titles = getListItemTitles();
		final String[] imageTiles = getListItemImageTiles();
		
		if (imageTiles[position] != null) {
			UNIQUE_MAP_IMAGE_URL = imageTiles[position];
		} else {
			UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
		}

		// ImageView mapThumb = (ImageView) rowView.findViewById(R.id.mapView1);
		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, viewHolder.worldImage);
		viewHolder.worldText.setText(titles[position]);

//		final int randomFilterColor = colors[getRandomColor()];
//		viewHolder.worldImage.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
		viewHolder.worldImage.setImageAlpha(50);

		return convertView;
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
