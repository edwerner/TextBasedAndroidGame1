package com.movie.locations.application;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.domain.FilmLocation;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FilmLocationMapTileArrayAdapter extends ArrayAdapter<FilmLocation> {
	private Context context;
	private ArrayList<FilmLocation> values;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private String UNIQUE_MAP_IMAGE_URL = "";
	private String DEFAULT_MAP_IMAGE_URL = "http://staticmap.openstreetmap.de/staticmap.php?center=48.854182,2.371105&zoom=13&size=500x500&maptype=mapnik";
	private String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
	private String CENTER = "";
	private String SETTINGS = "&zoom=16&size=1200x1200&sensor=true";
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

		rowView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				context.startActivity(intent);
			}
		});	

		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);	
		UNIQUE_MAP_IMAGE_URL = values.get(position).getStaticMapImageUrl();
//		imageView.setImageAlpha(25);
		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);
		textView.setText(values.get(position).getLocations());
		return rowView;
	}
}
