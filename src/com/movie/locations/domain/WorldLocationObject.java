package com.movie.locations.domain;
import android.os.Parcel;
import android.os.Parcelable;

public class WorldLocationObject implements Parcelable {

	private String position;
	private String createdAt;
	private String createdMeta;
	private String updatedAt;
	private String updatedMeta;
	private String meta;
	private String title;
	private String releaseYear;
	private String funFacts;
	private String productionCompany;
	private String distributor;
	private String director;
	private String writer;
	private String actor1;
	private String actor2;
	private String actor3;
	private String geolocation;
	private String locations;
	private String latitude;
	private String longitude;
	private String sid;
	private String id;
	private String level;
	private String staticMapImageUrl;
	private String questionId;
	private String dateTime;
	private String questionText;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String reaction1;
	private String reaction2;
	private String reaction3;
	private String reaction4;
	private String worldId;
	private String worldTitle;
	private String submittedAnswerIndex;
	private String answered = "false";
	private String activeItem;
	private String activeItem1;
	private String activeItem2;
	private String activeItem3;
	private String activeItem4;
	private String correctAnswerIndex;
	
	// empty constructor
	public WorldLocationObject() {
		
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
		pc.writeString(questionId);
		pc.writeString(dateTime);
		pc.writeString(questionText);
		pc.writeString(answer1);
		pc.writeString(answer2);
		pc.writeString(answer3);
		pc.writeString(answer4);
		pc.writeString(reaction1);
		pc.writeString(reaction2);
		pc.writeString(reaction3);
		pc.writeString(reaction4);
		pc.writeString(worldId);
		pc.writeString(worldTitle);
		pc.writeString(submittedAnswerIndex);
		pc.writeValue(correctAnswerIndex);
		pc.writeString(answered);
		pc.writeString(activeItem);
		pc.writeString(activeItem1);
		pc.writeString(activeItem2);
		pc.writeString(activeItem3);
		pc.writeString(activeItem4);
		pc.writeString(level);
	}	
	
	public WorldLocationObject(Parcel pc) {
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
		questionId = pc.readString();
		dateTime = pc.readString();
		questionText = pc.readString();
		answer1 = pc.readString();
		answer2 = pc.readString();
		answer3 = pc.readString();
		answer4 = pc.readString();
		reaction1 = pc.readString();
		reaction2 = pc.readString();
		reaction3 = pc.readString();
		reaction4 = pc.readString();
		worldId = pc.readString();
		worldTitle = pc.readString();
		submittedAnswerIndex = pc.readString();
		correctAnswerIndex = pc.readString();
		answered = pc.readString();
		activeItem = pc.readString();
		activeItem1 = pc.readString();
		activeItem2 = pc.readString();
		activeItem3 = pc.readString();
		activeItem4 = pc.readString();
		level = pc.readString();
	}

	public static final Parcelable.Creator<WorldLocationObject> CREATOR = new Parcelable.Creator<WorldLocationObject>() {
		public WorldLocationObject createFromParcel(Parcel in) {
			return new WorldLocationObject(in);
		}

		public WorldLocationObject[] newArray(int size) {
			return new WorldLocationObject[size];
		}
	};

	public static Parcelable.Creator<WorldLocationObject> getCreator() {
		return CREATOR;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public String getReaction1() {
		return reaction1;
	}

	public void setReaction1(String reaction1) {
		this.reaction1 = reaction1;
	}

	public String getReaction2() {
		return reaction2;
	}

	public void setReaction2(String reaction2) {
		this.reaction2 = reaction2;
	}

	public String getReaction3() {
		return reaction3;
	}

	public void setReaction3(String reaction3) {
		this.reaction3 = reaction3;
	}

	public String getReaction4() {
		return reaction4;
	}

	public void setReaction4(String reaction4) {
		this.reaction4 = reaction4;
	}

	public String getWorldId() {
		return worldId;
	}

	public void setWorldId(String worldId) {
		this.worldId = worldId;
	}

	public String getWorldTitle() {
		return worldTitle;
	}

	public void setWorldTitle(String worldTitle) {
		this.worldTitle = worldTitle;
	}

	public String getSubmittedAnswerIndex() {
		return submittedAnswerIndex;
	}

	public void setSubmittedAnswerIndex(String submittedAnswerIndex) {
		this.submittedAnswerIndex = submittedAnswerIndex;
	}

	public String getAnswered() {
		return answered;
	}

	public void setAnswered(String answered) {
		this.answered = answered;
	}

	public String getActiveItem() {
		return activeItem;
	}

	public void setActiveItem(String activeItem) {
		this.activeItem = activeItem;
	}

	public String getActiveItem1() {
		return activeItem1;
	}

	public void setActiveItem1(String activeItem1) {
		this.activeItem1 = activeItem1;
	}

	public String getActiveItem2() {
		return activeItem2;
	}

	public void setActiveItem2(String activeItem2) {
		this.activeItem2 = activeItem2;
	}

	public String getActiveItem3() {
		return activeItem3;
	}

	public void setActiveItem3(String activeItem3) {
		this.activeItem3 = activeItem3;
	}

	public String getActiveItem4() {
		return activeItem4;
	}

	public void setActiveItem4(String activeItem4) {
		this.activeItem4 = activeItem4;
	}

	public String getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	public void setCorrectAnswerIndex(String correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedMeta() {
		return createdMeta;
	}

	public void setCreatedMeta(String createdMeta) {
		this.createdMeta = createdMeta;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedMeta() {
		return updatedMeta;
	}

	public void setUpdatedMeta(String updatedMeta) {
		this.updatedMeta = updatedMeta;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getFunFacts() {
		return funFacts;
	}

	public void setFunFacts(String funFacts) {
		this.funFacts = funFacts;
	}

	public String getProductionCompany() {
		return productionCompany;
	}

	public void setProductionCompany(String productionCompany) {
		this.productionCompany = productionCompany;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getActor1() {
		return actor1;
	}

	public void setActor1(String actor1) {
		this.actor1 = actor1;
	}

	public String getActor2() {
		return actor2;
	}

	public void setActor2(String actor2) {
		this.actor2 = actor2;
	}

	public String getActor3() {
		return actor3;
	}

	public void setActor3(String actor3) {
		this.actor3 = actor3;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStaticMapImageUrl() {
		return staticMapImageUrl;
	}

	public void setStaticMapImageUrl(String staticMapImageUrl) {
		this.staticMapImageUrl = staticMapImageUrl;
	}	
}