package com.movie.locations.domain;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class WorldLocationArrayList implements Parcelable {
	
	private ArrayList<WorldLocationObject> worldLocationList = new ArrayList<WorldLocationObject>();
	
	public WorldLocationArrayList() {
		// empty constructor
	}

	public ArrayList<WorldLocationObject> getWorldLocationList() {
		return worldLocationList;
	}

	public void setWorldLocationList(ArrayList<WorldLocationObject> worldLocationList) {
		this.worldLocationList = worldLocationList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeList(worldLocationList);
	}	
	
	public WorldLocationArrayList(Parcel pc) {
		pc.readList(worldLocationList, WorldLocationObject.class.getClassLoader());
	}

	public static final Parcelable.Creator<WorldLocationArrayList> CREATOR = new Parcelable.Creator<WorldLocationArrayList>() {
		public WorldLocationArrayList createFromParcel(Parcel in) {
			return new WorldLocationArrayList(in);
		}

		public WorldLocationArrayList[] newArray(int size) {
			return new WorldLocationArrayList[size];
		}
	};
}
