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

public class QuizItemArrayAdapter extends ArrayAdapter<FilmLocation> {
	private final Context context;
	private final ArrayList<FilmLocation> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public String UNIQUE_MAP_IMAGE_URL = "";
	public final String DEFAULT_MAP_IMAGE_URL = "http://staticmap.openstreetmap.de/staticmap.php?center=48.854182,2.371105&zoom=13&size=500x500&maptype=mapnik";
	public final String PREFIX = "http://maps.googleapis.com/maps/api/staticmap?center=";
	public String CENTER = "";
	public String SETTINGS = "&zoom=16&size=200x200&sensor=true";
	private Intent intent;

	public QuizItemArrayAdapter(Context context, Intent intent, ArrayList<FilmLocation> values) {
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

		// build new bundle with combined attributes
		quizIntent.putExtra("position", values.get(position).getPosition());
		quizIntent.putExtra("title", values.get(position).getTitle());
		quizIntent.putExtra("funFacts", values.get(position).getFunFacts());
		quizIntent.putExtra("locations", values.get(position).getLocations());
		quizIntent.putExtra("sid", values.get(position).getPosition());
		quizIntent.putExtra("id", values.get(position).getId());
		quizIntent.putExtra("level", values.get(position).getLevel());
		quizIntent.putExtra("staticMapImageUrl", values.get(position).getStaticMapImageUrl());
		
//		if (!values.get(position).getLatitude().equals("")) {
			rowView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
//                    notifyDataSetChanged();
					context.startActivity(quizIntent);
				}
			});	
//		}

		TextView textView = (TextView) rowView.findViewById(R.id.label);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);

		if (values.get(position).getLocations() != null) {
//			String formattedLocation = removeParenthesis((values.get(position).getLocations()));
			UNIQUE_MAP_IMAGE_URL = values.get(position).getStaticMapImageUrl();
		} else {
			UNIQUE_MAP_IMAGE_URL = DEFAULT_MAP_IMAGE_URL;
		}
		
		imageLoader.displayImage(UNIQUE_MAP_IMAGE_URL, imageView);

		textView.setText(values.get(position).getLocations());

		// Change icon based on name
		String s = values.get(position).getTitle();

		System.out.println(s);
		// imageView.setImageResource(R.drawable.film_reel_sm);

		return rowView;
	}
}
