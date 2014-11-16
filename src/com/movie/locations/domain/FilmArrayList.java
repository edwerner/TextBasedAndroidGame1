package com.movie.locations.domain;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class FilmArrayList implements Parcelable {
	
	private ArrayList<FilmLocation> filmList = new ArrayList<FilmLocation>();
	
	public FilmArrayList() {
		// empty constructor
	}

	public ArrayList<FilmLocation> getFilmList() {
		return filmList;
	}

	public void setFilmList(ArrayList<FilmLocation> filmList) {
		this.filmList = filmList;
	}

	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeList(filmList);
	}	
	
	public FilmArrayList(Parcel pc) {
		pc.readList(filmList, FilmLocation.class.getClassLoader());
	}

	public static final Parcelable.Creator<FilmArrayList> CREATOR = new Parcelable.Creator<FilmArrayList>() {
		public FilmArrayList createFromParcel(Parcel in) {
			return new FilmArrayList(in);
		}

		public FilmArrayList[] newArray(int size) {
			return new FilmArrayList[size];
		}
	};
}
