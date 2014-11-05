package com.movie.locations.service;

import java.util.ArrayList;

import com.movie.locations.domain.FilmArrayList;
import com.movie.locations.domain.FilmLocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DatabaseChangedReceiver extends BroadcastReceiver {
    public static String ACTION_DATABASE_CHANGED = "com.movie.locations.service.DATABASE_CHANGED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println(ACTION_DATABASE_CHANGED);
		
		Bundle extras = intent.getExtras();
//		newsIntent.putExtra("locationArrayList", localLocationArrayList);
		FilmArrayList locationArrayList = extras.getParcelable("locationArrayList");
		System.out.println("DATABASE_CHANGED: " + locationArrayList);
		ArrayList<FilmLocation> tempLocationList = locationArrayList.getFilmList();
		
		for (FilmLocation loc : tempLocationList) {
			System.out.println("DATABASE_CHANGED: " + loc.getLocations());
		}
		
	}
}