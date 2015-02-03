package com.movie.locations.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.application.ConclusionActivity;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.GameTitle;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GameTitleArrayAdapter extends ArrayAdapter<GameTitle> {
	private Context context;
	private ArrayList<GameTitle> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public GameTitleArrayAdapter(Context context, Intent intent,
			ArrayList<GameTitle> values) {
		super(context, R.layout.game_title_array_adapter, values);
		this.context = context;
		this.values = values;
	}

	public String removeParenthesis(String string) {
		String regex = string.replaceAll("\\(", " ");
		regex = regex.replaceAll("\\)", " ");
		return regex;
	}

	static class ViewHolder {
		ImageView itemImage;
		TextView itemTitle;
		TextView itemDesc;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final ViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.game_title_array_adapter, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.gameTitleImage);
			viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.gameTitleTitle);
			viewHolder.itemDesc = (TextView) convertView.findViewById(R.id.gameTitleDesc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final String tempPhase = values.get(position).getPhase();
		String gameTitleText = values.get(position).getTitle();
		System.out.println("gameTitleText: " + gameTitleText);
		final String missingWorldText = " (missing)";
		String BAG_ITEM_IMAGE_URL = "drawable://x_button";
		
		if (tempPhase.equals("MISSING") || tempPhase.equals("null")) {
			convertView.setAlpha((float) 0.5);
			gameTitleText += missingWorldText;
		} else {
			
			final String existingImageUrl = values.get(position).getImageUrl();
			
			if (existingImageUrl != null) {
				BAG_ITEM_IMAGE_URL = existingImageUrl;
			} 
			
			final Intent conclusionIntent = new Intent(context, ConclusionActivity.class);
			final ConclusionCard tempCard = new ConclusionCard();
			tempCard.setTitle(values.get(position).getTitle());
			tempCard.setLevel(values.get(position).getLevel());
			tempCard.setImageUrl(values.get(position).getId());
			tempCard.setCopy(values.get(position).getDescription());
			
			conclusionIntent.putExtra("conclusionCard", tempCard);
			
			if (!values.get(position).getId().equals(null)) {
				convertView.setOnClickListener(new View.OnClickListener() {
	    		    public void onClick(View v) {
	    		    	context.startActivity(conclusionIntent);
	    		    }
	    		});
			}
		}

		System.out.println("viewHolder: " + viewHolder.itemTitle);
		System.out.println("viewHolder gameTitleText: " + gameTitleText);
		viewHolder.itemTitle.setText(gameTitleText);
		viewHolder.itemDesc.setText(values.get(position).getDescription());	
		imageLoader.displayImage(BAG_ITEM_IMAGE_URL, viewHolder.itemImage);

		return convertView;
	}
}