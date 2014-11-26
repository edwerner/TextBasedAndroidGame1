package com.movie.locations.domain;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class BagItemArrayList implements Parcelable {
	
	private ArrayList<BagItem> bagList = new ArrayList<BagItem>();
	
	public BagItemArrayList() {
		// empty constructor
	}

	public ArrayList<BagItem> getBagItemArrayList() {
		return bagList;
	}

	public void setBagItemArrayList(ArrayList<BagItem> bagList) {
		this.bagList = bagList;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeList(bagList);
	}	
	
	public BagItemArrayList(Parcel pc) {
		pc.readList(bagList, BagItem.class.getClassLoader());
	}

	public static final Parcelable.Creator<BagItemArrayList> CREATOR = new Parcelable.Creator<BagItemArrayList>() {
		public BagItemArrayList createFromParcel(Parcel in) {
			return new BagItemArrayList(in);
		}

		public BagItemArrayList[] newArray(int size) {
			return new BagItemArrayList[size];
		}
	};
}