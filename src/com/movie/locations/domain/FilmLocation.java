package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Class FilmLocation.
 */
public class FilmLocation implements Parcelable {

	/** The position. */
	private String position;

	/** The title. */
	private String title;

	/** The locations. */
	private String locations;

	/** The fun facts. */
	private String funFacts;

	/** The sid. */
	private String sid;

	/** The id. */
	private String id;
	
	/** The level. */
	public String level;
	
	public String staticMapImageUrl;

	// empty constructor
	public FilmLocation() {
		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(sid);
		pc.writeString(level);
		pc.writeString(position);
		pc.writeString(id);
		pc.writeString(title);
		pc.writeString(locations);
		pc.writeString(funFacts);
		pc.writeString(staticMapImageUrl);
	}	
	
	public FilmLocation(Parcel pc) {
		sid = pc.readString();
		level = pc.readString();
		position = pc.readString();
		id = pc.readString();
		title = pc.readString();
		locations = pc.readString();
		funFacts = pc.readString();
		staticMapImageUrl = pc.readString();
	}

	public static final Parcelable.Creator<FilmLocation> CREATOR = new Parcelable.Creator<FilmLocation>() {
		public FilmLocation createFromParcel(Parcel in) {
			return new FilmLocation(in);
		}

		public FilmLocation[] newArray(int size) {
			return new FilmLocation[size];
		}
	};
	
	public String getStaticMapImageUrl() {
		return staticMapImageUrl;
	}

	public void setStaticMapImageUrl(String staticMapImageUrl) {
		this.staticMapImageUrl = staticMapImageUrl;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid
	 *            the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the locations
	 */
	public String getLocations() {
		return locations;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(String locations) {
		this.locations = locations;
	}

	/**
	 * @return the funFacts
	 */
	public String getFunFacts() {
		return funFacts;
	}

	/**
	 * @param funFacts
	 *            the funFacts to set
	 */
	public void setFunFacts(String funFacts) {
		this.funFacts = funFacts;
	}
}