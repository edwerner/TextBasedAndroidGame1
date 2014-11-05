package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
	// this class is a lightweight snapshot
	// of a user's real-time location check-in

	private String userId;
	private String dateTime;
	private String filmId;
	private String comment;
	private String commentId;

	public Comment() {
		// empty constructor
	}

	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(userId);
		pc.writeString(dateTime);
		pc.writeString(filmId);
		pc.writeString(comment);
		pc.writeString(commentId);
	}	
	
	public Comment(Parcel pc) {
		pc.readParcelable(ClassLoaderHelper.getClassLoader());
		userId = pc.readString();
		dateTime = pc.readString();
		filmId = pc.readString();
		comment = pc.readString();
		commentId = pc.readString();
	}

	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
		public Comment createFromParcel(Parcel in) {
			return new Comment(in);
		}

		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};
	
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


	public String getFilmId() {
		return filmId;
	}


	public void setFilmId(String filmId) {
		this.filmId = filmId;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getCommentId() {
		return commentId;
	}


	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
}
