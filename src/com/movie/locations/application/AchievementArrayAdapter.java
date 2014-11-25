package com.movie.locations.application;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.movie.locations.R;
import com.movie.locations.domain.Achievement;
import com.movie.locations.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementArrayAdapter extends ArrayAdapter<Achievement> {
	private final Context context;
	private final ArrayList<Achievement> values;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private Intent intent;

	public AchievementArrayAdapter(Context context, Intent intent,
			ArrayList<Achievement> values) {
		super(context, R.layout.comment_array_adapter, values);
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

		View rowView = inflater.inflate(R.layout.achievement_array_adapter,
				parent, false);

		final Intent commentIntent = new Intent(context, AchievementActivity.class);
		
		// get intent string attributes
		Bundle bundle = intent.getExtras();
		User localUser = bundle.getParcelable("localUser");
		
		commentIntent.putExtra("userId", values.get(position).getUserId());
		commentIntent.putExtra("dateTime", values.get(position).getDateTime());
		commentIntent.putExtra("title", values.get(position).getTitle());
//		commentIntent.putExtra("catchPhrase", values.get(position).getCatchPhrase());
		commentIntent.putExtra("localUser", localUser);
	
		if (!values.get(position).getTitle().equals(null)) {
			rowView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					context.startActivity(commentIntent);
				}
			});	
		}

		TextView nameView = (TextView) rowView.findViewById(R.id.userDisplayName);
		TextView commentView = (TextView) rowView.findViewById(R.id.userComment);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.userCommentAvatar);
		imageView.setImageResource(R.drawable.ic_launcher);
		nameView.setText("Achievement date");
		commentView.setText(values.get(position).getTitle());

		return rowView;
	}
}