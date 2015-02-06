package com.movie.locations.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DatabaseChangedReceiver extends BroadcastReceiver {
    public static final String ACTION_DATABASE_CHANGED = "com.movie.locations.service.DATABASE_CHANGED";

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println(ACTION_DATABASE_CHANGED);
	}
}