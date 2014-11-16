package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class PointsItem implements Parcelable {
	private String userId;
	private String pointsUserId;
	private String points;
	private String bonusPoints;
	
	public PointsItem() {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(userId);
		pc.writeString(pointsUserId);
		pc.writeString(points);
		pc.writeString(bonusPoints);
	}

	public PointsItem(Parcel pc) {
		userId = pc.readString();
		pointsUserId = pc.readString();
		points = pc.readString();
		bonusPoints = pc.readString();
	}

	public static final Parcelable.Creator<PointsItem> CREATOR = new Parcelable.Creator<PointsItem>() {
		public PointsItem createFromParcel(Parcel in) {
			return new PointsItem(in);
		}

		public PointsItem[] newArray(int size) {
			return new PointsItem[size];
		}
	};

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(String bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPointsUserId() {
		return pointsUserId;
	}

	public void setPointsUserId(String pointsUserId) {
		this.pointsUserId = pointsUserId;
	}
}