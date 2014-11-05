package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckIn implements Parcelable {
	// this class is a lightweight snapshot
	// of a user's real-time location check-in

	private String filmLocation;
	private User user;
	private String datetime;
	private String filmTitle;
	private String checkinId;
	private String userId;
	
	public CheckIn() {
		// empty constructor
	}

	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(checkinId);
		pc.writeString(datetime);
		pc.writeString(filmLocation);
		pc.writeString(userId);
		pc.writeString(filmTitle);
	}	
	
	public CheckIn(Parcel pc) {
//		pc.readParcelable(ClassLoaderHelper.getClassLoader());
		checkinId = pc.readString();
		datetime = pc.readString();
		filmLocation = pc.readString();
		userId = pc.readString();
		filmTitle = pc.readString();
	}

	public static final Parcelable.Creator<CheckIn> CREATOR = new Parcelable.Creator<CheckIn>() {
		public CheckIn createFromParcel(Parcel in) {
			return new CheckIn(in);
		}

		public CheckIn[] newArray(int size) {
			return new CheckIn[size];
		}
	};
	
	public String getFilmLocation() {
		return filmLocation;
	}
	public void setFilmLocation(String filmLocation) {
		this.filmLocation = filmLocation;
	}
	
	public String getFilmTitle() {
		return filmTitle;
	}
	
	public void setFilmTitle(String filmTitle) {
		this.filmTitle = filmTitle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getCheckinId() {
		return checkinId;
	}

	public void setCheckinId(String checkinId) {
		this.checkinId = checkinId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
