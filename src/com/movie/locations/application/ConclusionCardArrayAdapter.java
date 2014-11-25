package com.movie.locations.application;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.domain.ConclusionCard;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ConclusionCardArrayAdapter extends ArrayAdapter<ConclusionCard> {
	private final Context context;
	private final ArrayList<ConclusionCard> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private Intent intent;

	public ConclusionCardArrayAdapter(Context context, Intent intent,
			ArrayList<ConclusionCard> values) {
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

		View rowView = inflater.inflate(R.layout.conclusion_card_array_adapter,
				parent, false);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.conclusionCardImage1);
		TextView titleView = (TextView) rowView.findViewById(R.id.conclusionCardTitle1);
		TextView textCopyView = (TextView) rowView.findViewById(R.id.conclusionCardCopy1);
		TextView cardLevelView = (TextView) rowView.findViewById(R.id.conclusionCardLevel1);
		titleView.setText(values.get(position).getTitle());
		cardLevelView.setText(values.get(position).getLevel());
		textCopyView.setText(values.get(position).getCopy());
		final String BAG_ITEM_IMAGE_URL = values.get(position).getImageUrl();
		imageLoader.displayImage(BAG_ITEM_IMAGE_URL, imageView);

		return rowView;
	}
}

