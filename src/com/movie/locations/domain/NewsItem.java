package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
	private String id;
	private String title;
	private String text;
	private String imageUrl;
	private String newsType;
	private String dateTime;
	
	public NewsItem() {
		// empty constructor
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(id);
		pc.writeString(title);
		pc.writeString(text);
		pc.writeString(imageUrl);
		pc.writeString(newsType);
		pc.writeString(dateTime);
	}	
	
	public NewsItem(Parcel pc) {
		id = pc.readString();
		title = pc.readString();
		text = pc.readString();
		imageUrl = pc.readString();
		newsType = pc.readString();
		dateTime = pc.readString();
	}

	public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {
		public NewsItem createFromParcel(Parcel in) {
			return new NewsItem(in);
		}

		public NewsItem[] newArray(int size) {
			return new NewsItem[size];
		}
	};

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}