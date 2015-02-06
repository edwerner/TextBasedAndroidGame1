package com.movie.locations.domain;

import java.util.List;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

// user domain object
/**
 * The Class User.
 */
public class BagItem implements Parcelable {
	
	private String bagGroupTitle;
	private String itemTitle;
	private String description;
	private String imageUrl;
	private String level;
	private String itemId;
	
	public final List<BagItem> itemList = new ArrayList<BagItem>();

	// empty constructor
	public BagItem() {
		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(bagGroupTitle);
		pc.writeString(itemId);
		pc.writeString(itemTitle);
		pc.writeString(description);
		pc.writeString(imageUrl);
		pc.writeString(level);
	}	
	
	public BagItem(Parcel pc) {
		bagGroupTitle = pc.readString();
		itemId = pc.readString();
		itemTitle = pc.readString();
		description = pc.readString();
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

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBagGroupTitle() {
		return bagGroupTitle;
	}

	public void setBagGroupTitle(String bagGroupTitle) {
		this.bagGroupTitle = bagGroupTitle;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
}