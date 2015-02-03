package com.movie.locations.adapter;

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

public class LocationArrayAdapter extends ArrayAdapter<FilmLocation> {
	private Context context;
	private ArrayList<FilmLocation> values;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Intent intent;

	public LocationArrayAdapter(Context context, Intent intent,
			ArrayList<FilmLocation> values) {
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
		
		convertView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				context.startActivity(intent);
			}
		});	

		imageLoader.displayImage(values.get(position).getStaticMapImageUrl(), viewHolder.worldImage);
		viewHolder.worldText.setText(values.get(position).getLocations());
		
		return convertView;
	}
}
