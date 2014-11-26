package com.movie.locations.domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable;

public class LocationMapParcel implements Parcelable {
	
	private HashMap<String, ArrayList<FilmLocation>> locationHashMap;
	private ArrayList<FilmLocation> locationList;
	
	public LocationMapParcel() {
		// empty constructor
	}

	public HashMap<String, ArrayList<FilmLocation>> getLocationHashMap() {
		return locationHashMap;
	}

	public void setLocationHashMap(HashMap<String, ArrayList<FilmLocation>> locationHashMap) {
		this.locationHashMap = locationHashMap;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		
     final int hashMapSize = locationHashMap.size();
        pc.writeInt(hashMapSize);
        if (hashMapSize > 0) {
            for (Map.Entry<String, ArrayList<FilmLocation>> entry : locationHashMap.entrySet()) {
            	pc.writeString(entry.getKey());
            	pc.writeList(entry.getValue());
            }
        }
	}	
	
	public LocationMapParcel(Parcel pc) {
        final int readInt = pc.readInt();
        for (int i = 0; i < readInt; i++) {
            String key = pc.readString();
            pc.readList(locationList, FilmLocation.class.getClassLoader());
            locationHashMap.put(key, locationList);
        }
	}

	public static final Parcelable.Creator<LocationMapParcel> CREATOR = new Parcelable.Creator<LocationMapParcel>() {
		public LocationMapParcel createFromParcel(Parcel in) {
			return new LocationMapParcel(in);
		}

		public LocationMapParcel[] newArray(int size) {
			return new LocationMapParcel[size];
		}
	};
}