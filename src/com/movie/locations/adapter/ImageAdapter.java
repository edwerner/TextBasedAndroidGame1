package com.movie.locations.adapter;

import com.movie.locations.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return thumbUrls.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

//		imageView.setImageResource(thumbUrls[position]);
		imageLoader.displayImage(thumbUrls[position], imageView);
		
		return imageView;
	}

	// references to our images
	private String[] thumbUrls = {
			"http://www.zeldadungeon.net/Zelda14/Items/Air-Potion-Icon.png",
			"https://cdn1.iconfinder.com/data/icons/all_google_icons_symbols_by_carlosjj-du/128/glasses.png",
			"http://us.cdn2.123rf.com/168nwm/thirteenfifty/thirteenfifty1201/thirteenfifty120100330/12093404-skeleton-key-silhouette.jpg",
			"http://www.zeldadungeon.net/Zelda14/Items/Heart-Potion-Icon.png",
			"http://a4.mzstatic.com/us/r30/Purple/e4/33/a9/mzl.isoelwer.175x175-75.png",
			"http://www.zeldadungeon.net/Zelda14/Items/Air-Potion-Icon.png",
			"https://cdn1.iconfinder.com/data/icons/all_google_icons_symbols_by_carlosjj-du/128/glasses.png",
			"http://us.cdn2.123rf.com/168nwm/thirteenfifty/thirteenfifty1201/thirteenfifty120100330/12093404-skeleton-key-silhouette.jpg",
			"http://www.zeldadungeon.net/Zelda14/Items/Heart-Potion-Icon.png"
	};
}