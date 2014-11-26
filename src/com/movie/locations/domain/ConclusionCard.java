package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class ConclusionCard implements Parcelable {
	private String id;
	private String title;
	private String copy;
	private String imageUrl;
	private String level;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(id);
		pc.writeString(title);
		pc.writeString(copy);
		pc.writeString(imageUrl);
		pc.writeString(level);
	}	
	
	public ConclusionCard() {
		// empty constructor
	}
	
	public ConclusionCard(Parcel pc) {
		id = pc.readString();
		title = pc.readString();
		copy = pc.readString();
		imageUrl = pc.readString();
		level = pc.readString();
	}

	public static final Parcelable.Creator<ConclusionCard> CREATOR = new Parcelable.Creator<ConclusionCard>() {
		public ConclusionCard createFromParcel(Parcel in) {
			return new ConclusionCard(in);
		}

		public ConclusionCard[] newArray(int size) {
			return new ConclusionCard[size];
		}
	};
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCopy() {
		return copy;
	}
	public void setCopy(String copy) {
		this.copy = copy;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}