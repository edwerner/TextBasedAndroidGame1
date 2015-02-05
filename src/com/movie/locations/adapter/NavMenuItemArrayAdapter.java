package com.movie.locations.adapter;

import java.util.ArrayList;
import android.content.Context;
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
	private String NAV_IMAGE_URL = "drawable://x_button";

	public NavMenuItemArrayAdapter(Context context, Intent intent,
			ArrayList<NavMenuItem> values) {
		super(context, R.layout.nav_menu_item_array_adapter, values);
		this.context = context;
		this.values = values;
	}

	static class ViewHolder {
		ImageView navItemImage;
		TextView navItemTitle;
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

		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.nav_menu_item_array_adapter, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.navItemImage = (ImageView) convertView.findViewById(R.id.navMenuItemImage1);
			viewHolder.navItemTitle = (TextView) convertView.findViewById(R.id.navMenuItemTitle1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.navItemTitle.setText(values.get(position).getTitle());
		
		if (values.get(position).getImageUrl() != null) {
			NAV_IMAGE_URL = values.get(position).getImageUrl();
		}
		
		imageLoader.displayImage(NAV_IMAGE_URL, viewHolder.navItemImage);

		return convertView;
	}
}