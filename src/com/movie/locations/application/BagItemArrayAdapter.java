package com.movie.locations.application;
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
	private Context context;
	private ArrayList<BagItem> values;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Intent intent;

	public BagItemArrayAdapter(Context context, Intent intent,
			ArrayList<BagItem> values) {
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

		View rowView = inflater.inflate(R.layout.bag_item_array_adapter,
				parent, false);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.bagItemImage1);
		TextView bagItemTitle = (TextView) rowView.findViewById(R.id.bagItemTitle1);
		TextView bagItemLevel = (TextView) rowView.findViewById(R.id.bagItemLevel1);
		TextView bagItemDescription = (TextView) rowView.findViewById(R.id.bagItemDescription1);
		bagItemTitle.setText(values.get(position).getItemTitle());
		bagItemLevel.setText(values.get(position).getLevel());
		bagItemDescription.setText(values.get(position).getDescription());
		final String BAG_ITEM_IMAGE_URL = values.get(position).getImageUrl();
		imageLoader.displayImage(BAG_ITEM_IMAGE_URL, imageView);
		
		return rowView;
	}
}

