package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Achievement implements Parcelable {
	String achievementId;
	String title;
	String description;
	String userId;
	String dateTime;
	String imageUrl;
	String level;
	public Achievement() {
		// EMPTY CONSTRUCTOR
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(achievementId);
		pc.writeString(title);
		pc.writeString(description);
		pc.writeString(userId);
		pc.writeString(dateTime);
		pc.writeString(imageUrl);
		pc.writeString(level);
	}	
	
	public Achievement(Parcel pc) {
		achievementId = pc.readString();
		title = pc.readString();
		description = pc.readString();
		userId = pc.readString();
		dateTime = pc.readString();
		imageUrl = pc.readString();
		level = pc.readString();
	}

	public static final Parcelable.Creator<BagItem> CREATOR = new Parcelable.Creator<BagItem>() {
		public BagItem createFromParcel(Parcel in) {
			return new BagItem(in);
		}

		public BagItem[] newArray(int size) {
			return new BagItem[size];
		}
	};
	
	public String getAchievementId() {
		return achievementId;
	}
	public void setAchievementId(String achievementId) {
		this.achievementId = achievementId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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
}
