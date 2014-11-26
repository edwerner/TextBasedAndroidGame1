package com.movie.locations.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable;

public class QuizItemMapParcel implements Parcelable {
	
	private HashMap<String, ArrayList<QuizItem>> quizItemHashMap;
	private ArrayList<QuizItem> quizItemList;
	
	public QuizItemMapParcel() {
		// empty constructor
	}

	public HashMap<String, ArrayList<QuizItem>> getQuizItemHashMap() {
		return quizItemHashMap;
	}

	public void setQuizItemHashMap(HashMap<String, ArrayList<QuizItem>> quizItemHashMap) {
		this.quizItemHashMap = quizItemHashMap;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		
     final int hashMapSize = quizItemHashMap.size();
        pc.writeInt(hashMapSize);
        if (hashMapSize > 0) {
            for (Map.Entry<String, ArrayList<QuizItem>> entry : quizItemHashMap.entrySet()) {
            	pc.writeString(entry.getKey());
            	pc.writeList(entry.getValue());
            }
        }
	}	
	
	public QuizItemMapParcel(Parcel pc) {
        final int readInt = pc.readInt();
        for (int i = 0; i < readInt; i++) {
            String key = pc.readString();
            pc.readList(quizItemList, QuizItem.class.getClassLoader());
            quizItemHashMap.put(key, quizItemList);
        }
	}

	public static final Parcelable.Creator<QuizItemMapParcel> CREATOR = new Parcelable.Creator<QuizItemMapParcel>() {
		public QuizItemMapParcel createFromParcel(Parcel in) {
			return new QuizItemMapParcel(in);
		}

		public QuizItemMapParcel[] newArray(int size) {
			return new QuizItemMapParcel[size];
		}
	};
}