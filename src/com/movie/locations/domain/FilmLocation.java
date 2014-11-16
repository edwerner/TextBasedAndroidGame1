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
public class FilmLocation implements Parcelable {

	/** The position. */
	private String position;

	/** The created at. */
	private String createdAt;

	/** The created meta. */
	private String createdMeta;

	/** The updated at. */
	private String updatedAt;

	/** The updated meta. */
	private String updatedMeta;

	/** The meta. */
	private String meta;

	/** The title. */
	private String title;

	/** The release year. */
	private String releaseYear;

	/** The locations. */
	private String locations;

	/** The fun facts. */
	private String funFacts;

	/** The production company. */
	private String productionCompany;

	/** The distributor. */
	private String distributor;

	/** The director. */
	private String director;

	/** The writer. */
	private String writer;

	/** The actor1. */
	private String actor1;

	/** The actor2. */
	private String actor2;

	/** The actor3. */
	private String actor3;

	/** The geolocation. */
	private String geolocation;

	private String latitude;

	private String longitude;

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
		pc.writeString(createdAt);
		pc.writeString(createdMeta);
		pc.writeString(updatedAt);
		pc.writeString(updatedMeta);
		pc.writeString(meta);
		pc.writeString(id);
		pc.writeString(title);
		pc.writeString(releaseYear);
		pc.writeString(locations);
		pc.writeString(funFacts);
		pc.writeString(productionCompany);
		pc.writeString(distributor);
		pc.writeString(director);
		pc.writeString(writer);
		pc.writeString(actor1);
		pc.writeString(actor2);
		pc.writeString(actor3);
		pc.writeString(latitude);
		pc.writeString(longitude);
		pc.writeString(staticMapImageUrl);
	}	
	
	public FilmLocation(Parcel pc) {
//		pc.readParcelable(ClassLoaderHelper.getClassLoader());
		sid = pc.readString();
		level = pc.readString();
		position = pc.readString();
		createdAt = pc.readString();
		createdMeta = pc.readString();
		updatedAt = pc.readString();
		updatedMeta = pc.readString();
		meta = pc.readString();
		id = pc.readString();
		title = pc.readString();
		releaseYear = pc.readString();
		locations = pc.readString();
		funFacts = pc.readString();
		productionCompany = pc.readString();
		distributor = pc.readString();
		director = pc.readString();
		writer = pc.readString();
		actor1 = pc.readString();
		actor2 = pc.readString();
		actor3 = pc.readString();
		latitude = pc.readString();
		longitude = pc.readString();
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
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the createdMeta
	 */
	public String getCreatedMeta() {
		return createdMeta;
	}

	/**
	 * @param createdMeta
	 *            the createdMeta to set
	 */
	public void setCreatedMeta(String createdMeta) {
		this.createdMeta = createdMeta;
	}

	/**
	 * @return the updatedAt
	 */
	public String getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt
	 *            the updatedAt to set
	 */
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the updatedMeta
	 */
	public String getUpdatedMeta() {
		return updatedMeta;
	}

	/**
	 * @param updatedMeta
	 *            the updatedMeta to set
	 */
	public void setUpdatedMeta(String updatedMeta) {
		this.updatedMeta = updatedMeta;
	}

	/**
	 * @return the meta
	 */
	public String getMeta() {
		return meta;
	}

	/**
	 * @param meta
	 *            the meta to set
	 */
	public void setMeta(String meta) {
		this.meta = meta;
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
	 * @return the releaseYear
	 */
	public String getReleaseYear() {
		return releaseYear;
	}

	/**
	 * @param releaseYear
	 *            the releaseYear to set
	 */
	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
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

	/**
	 * @return the productionCompany
	 */
	public String getProductionCompany() {
		return productionCompany;
	}

	/**
	 * @param productionCompany
	 *            the productionCompany to set
	 */
	public void setProductionCompany(String productionCompany) {
		this.productionCompany = productionCompany;
	}

	/**
	 * @return the distributor
	 */
	public String getDistributor() {
		return distributor;
	}

	/**
	 * @param distributor
	 *            the distributor to set
	 */
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	/**
	 * @return the director
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * @param director
	 *            the director to set
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}

	/**
	 * @param writer
	 *            the writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}

	/**
	 * @return the actor1
	 */
	public String getActor1() {
		return actor1;
	}

	/**
	 * @param actor1
	 *            the actor1 to set
	 */
	public void setActor1(String actor1) {
		this.actor1 = actor1;
	}

	/**
	 * @return the actor2
	 */
	public String getActor2() {
		return actor2;
	}

	/**
	 * @param actor2
	 *            the actor2 to set
	 */
	public void setActor2(String actor2) {
		this.actor2 = actor2;
	}

	/**
	 * @return the actor3
	 */
	public String getActor3() {
		return actor3;
	}

	/**
	 * @param actor3
	 *            the actor3 to set
	 */
	public void setActor3(String actor3) {
		this.actor3 = actor3;
	}

	/**
	 * @return the geolocation
	 */
	public String getGeolocation() {
		return geolocation;
	}

	/**
	 * @param geolocation
	 *            the geolocation to set
	 */
	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}