package com.movie.locations.domain;

import java.util.ArrayList;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import com.movie.locations.domain.StaticConstants;

// user domain object
/**
 * The Class User.
 */
public class User implements Parcelable {

	private String userId;
	private String userSid;
	private String userClientId;
	private String displayName;
	private String emailAddress;
	private String avatarImageUrl;
	private String currentLevel;
	private String currentPoints;
	private String pointsUserId;
	private String points;
	private String bonusPoints;
	private String worldCount;
	private String emailNotifications;
	private String mobileNotifications;
	
	// empty constructor
	public User() {
		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(userId);
		pc.writeString(userSid);
		pc.writeString(userClientId);
		pc.writeString(displayName);
		pc.writeString(emailAddress);
		pc.writeString(avatarImageUrl);
		pc.writeString(currentLevel);
		pc.writeString(currentPoints);
		pc.writeString(pointsUserId);
		pc.writeString(points);
		pc.writeString(bonusPoints);
		pc.writeString(worldCount);
		pc.writeString(emailNotifications);
		pc.writeString(mobileNotifications);
	}	
	
	public User(Parcel pc) {
//		pc.readParcelable(ClassLoaderHelper.getClassLoader());
		userId = pc.readString();
		userSid = pc.readString();
		userClientId = pc.readString();
		displayName = pc.readString();
		emailAddress = pc.readString();
		avatarImageUrl = pc.readString();
		currentLevel = pc.readString();
		currentPoints = pc.readString();
		pointsUserId = pc.readString();
		points = pc.readString();
		bonusPoints = pc.readString();
		worldCount = pc.readString();
		emailNotifications = pc.readString();
		mobileNotifications = pc.readString();
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAvatarImageUrl() {
		return avatarImageUrl;
	}

	public void setAvatarImageUrl(String avatarImageUrl) {
		this.avatarImageUrl = avatarImageUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(String currentLevel) {
		this.currentLevel = currentLevel;
	}

	public String getCurrentPoints() {
		return currentPoints;
	}

	public void setCurrentPoints(String currentPoints) {
		this.currentPoints = currentPoints;
	}

	public String getUserClientId() {
		return userClientId;
	}

	public void setUserClientId(String userClientId) {
		this.userClientId = userClientId;
	}

	public String getUserSid() {
		return userSid;
	}

	public void setUserSid(String userSid) {
		this.userSid = userSid;
	}

	public String getPointsUserId() {
		return pointsUserId;
	}

	public void setPointsUserId(String pointsUserId) {
		this.pointsUserId = pointsUserId;
	}

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

	public String getWorldCount() {
		return worldCount;
	}

	public void setWorldCount(String worldCount) {
		this.worldCount = worldCount;
	}

	public String getEmailNotifications() {
		return emailNotifications;
	}

	public void setEmailNotifications(String emailNotifications) {
		this.emailNotifications = emailNotifications;
	}

	public String getMobileNotifications() {
		return mobileNotifications;
	}

	public void setMobileNotifications(String mobileNotifications) {
		this.mobileNotifications = mobileNotifications;
	}
}