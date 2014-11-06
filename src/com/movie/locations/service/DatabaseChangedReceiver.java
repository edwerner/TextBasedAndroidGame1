package com.movie.locations.service;

import java.util.ArrayList;


//import com.movie.locations.domain.FilmArrayList;
//import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.QuizItemArrayList;

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
		
//		Bundle extras = intent.getExtras();
////		newsIntent.putExtra("locationArrayList", localLocationArrayList);
//		QuizItemArrayList quizArrayList = extras.getParcelable("quizArrayList");
//		System.out.println("DATABASE_CHANGED: " + quizArrayList);
//		ArrayList<QuizItem> quizItemArrayList = quizArrayList.getQuizList();
//		
//		for (QuizItem loc : quizItemArrayList) {
//			System.out.println("DATABASE_CHANGED: " + loc.getWorldTitle());
//		}
		
	}
}