package com.movie.locations.domain;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class CheckInOverlay {
	
	public CheckInOverlay() {
		// empty constructor
	}
	
	public static LinearLayout createOverlay(Context context) {

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		TextView titleView = new TextView(context);
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titleView.setLayoutParams(lparams);
		titleView.setTextAppearance(context, android.R.attr.textAppearanceLarge);
		titleView.setText("Overlay working!");
		layout.addView(titleView);

		return layout;
	}
	
}
