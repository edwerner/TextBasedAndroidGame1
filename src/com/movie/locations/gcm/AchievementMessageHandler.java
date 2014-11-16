//package com.movie.locations.gcm;
//
//import android.content.Context;
//import android.content.Intent;
//
//import com.movie.locations.AchievementActivity;
//
//public class AchievementMessageHandler {
//	
//	public String message;
//	public AchievementMessageHandler() {
//		// empty constructor
//	}
//	
//	public void launchAchievementActivity(Context context) {
//		Context localContext = context.getApplicationContext();
//		Intent achievementIntent = new Intent(localContext, AchievementActivity.class);
//		// starting an activity from service
//		achievementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		localContext.startActivity(achievementIntent);
//	}
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
//}
