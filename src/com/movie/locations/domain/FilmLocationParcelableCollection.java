package com.movie.locations.domain;

import java.util.ArrayList;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import com.movie.locations.domain.StaticConstants;

// film location pojo with builder pattern
/**
 * The Class FilmLocation.
 */
public class FilmLocationParcelableCollection implements Parcelable {

	private ArrayList<FilmLocation> filmList;

	// empty constructor
	public FilmLocationParcelableCollection() {
		filmList = new ArrayList<FilmLocation>();
	}

	public FilmLocationParcelableCollection(Parcel in) {
		in.readParcelable(FilmLocationParcelableCollection.class.getClassLoader());
		in.readTypedList(filmList, FilmLocation.CREATOR);
	}

	public ArrayList<FilmLocation> getArrList() {
		return filmList;
	}

	public void setArrList(ArrayList<FilmLocation> filmList) {
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

	public static final Parcelable.Creator<FilmLocationParcelableCollection> CREATOR = new Parcelable.Creator<FilmLocationParcelableCollection>() {
		public FilmLocationParcelableCollection createFromParcel(Parcel in) {
			return new FilmLocationParcelableCollection(in);
		}

		public FilmLocationParcelableCollection[] newArray(int size) {
			return new FilmLocationParcelableCollection[size];
		}
	};
}