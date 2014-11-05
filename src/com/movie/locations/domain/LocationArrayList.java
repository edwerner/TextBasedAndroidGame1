package com.movie.locations.domain;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class LocationArrayList implements Parcelable {
	
	private ArrayList<WorldLocationObject> filmList;
	
	public LocationArrayList() {
		// empty constructor
	}

	public ArrayList<WorldLocationObject> getFilmList() {
		return filmList;
	}

	public void setFilmList(ArrayList<WorldLocationObject> filmList) {
		this.filmList = filmList;
	}

	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeTypedList(filmList);
	}	
	
	public LocationArrayList(Parcel pc) {
		pc.readParcelable(ClassLoaderHelper.getClassLoader());
//		pc.readArrayList(ClassLoaderHelper.getClassLoader());
		filmList = pc.readArrayList(getClass().getClassLoader());
	}

	public static final Parcelable.Creator<LocationArrayList> CREATOR = new Parcelable.Creator<LocationArrayList>() {
		public LocationArrayList createFromParcel(Parcel in) {
			return new LocationArrayList(in);
		}

		public LocationArrayList[] newArray(int size) {
			return new LocationArrayList[size];
		}
	};
}
