package com.movie.locations.domain;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class QuizItemArrayList implements Parcelable {
	
	private ArrayList<QuizItem> quizList = new ArrayList<QuizItem>();
	
	public QuizItemArrayList() {
		// empty constructor
	}

	public ArrayList<QuizItem> getQuizList() {
		return quizList;
	}

	public void setQuizList(ArrayList<QuizItem> quizList) {
		this.quizList = quizList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeList(quizList);
	}	
	
	public QuizItemArrayList(Parcel pc) {
		pc.readList(quizList, BagItem.class.getClassLoader());
	}

	public static final Parcelable.Creator<QuizItemArrayList> CREATOR = new Parcelable.Creator<QuizItemArrayList>() {
		public QuizItemArrayList createFromParcel(Parcel in) {
			return new QuizItemArrayList(in);
		}

		public QuizItemArrayList[] newArray(int size) {
			return new QuizItemArrayList[size];
		}
	};
}