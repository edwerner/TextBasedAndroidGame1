//package com.movie.locations.adapter;
//
//import android.R;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
//import com.google.android.gms.maps.model.Marker;
//
//public class PopupAdapter implements InfoWindowAdapter {
//	LayoutInflater inflater = null;
//	Context context;
//
//	PopupAdapter(LayoutInflater inflater, Context context) {
//		this.inflater = inflater;
//		this.context = context;
//	}
//
//	@Override
//	public View getInfoWindow(Marker marker) {
//		return (null);
//	}
//
//	@Override
//	public View getInfoContents(Marker marker) {
//
////		MyModel mapItem = (MyModel) MainActivity.markers.get(marker.getId());
//		View popup = inflater.inflate(R.layout.popup, null);
//
////		TextView tv = (TextView) popup.findViewById(R.id.title);
////		ImageView im = (ImageView) popup.findViewById(R.id.icon);
////		tv.setText(marker.getTitle());
////		tv = (TextView) popup.findViewById(R.id.snippet);
////		tv.setText(marker.getSnippet());
//
//		im.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				System.out.println("map popup clicked!");
//			}
//		});
//
//		tv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context, "Message", Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		return (popup);
//	}
//
//}