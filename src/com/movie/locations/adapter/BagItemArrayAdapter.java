package com.movie.locations.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.domain.BagItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BagItemArrayAdapter extends ArrayAdapter<BagItem> {
	private final Context context;
	private ArrayList<BagItem> values;
	private final ImageLoader imageLoader = ImageLoader.getInstance();

	public BagItemArrayAdapter(Context context, Intent intent,
			ArrayList<BagItem> values) {
		super(context, R.layout.bag_item_array_adapter, values);
		this.context = context;
		this.values = values;
	}

	public String removeParenthesis(String string) {
		String regex = string.replaceAll("\\(", " ");
		regex = regex.replaceAll("\\)", " ");
		return regex;
	}
	
	static class ViewHolder {
		ImageView bagItemImage;
		TextView bagItemTitle;
		TextView bagItemLevel;
		TextView bagItemDescription;
	}		

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.bag_item_array_adapter, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.bagItemImage = (ImageView) convertView.findViewById(R.id.bagItemImage1);
			viewHolder.bagItemTitle = (TextView) convertView.findViewById(R.id.bagItemTitle1);
			viewHolder.bagItemLevel = (TextView) convertView.findViewById(R.id.bagItemLevel1);
			viewHolder.bagItemDescription = (TextView) convertView.findViewById(R.id.bagItemDescription1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.bagItemTitle.setText(values.get(position).getItemTitle());
		viewHolder.bagItemLevel.setText(values.get(position).getLevel());
		viewHolder.bagItemDescription.setText(values.get(position).getDescription());
		imageLoader.displayImage(values.get(position).getImageUrl(), viewHolder.bagItemImage);
		
		return convertView;
	}
}