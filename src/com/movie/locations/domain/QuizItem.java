package com.movie.locations.domain;

import android.os.Parcel;
import android.os.Parcelable;
import org.codehaus.jackson.annotate.JsonProperty;

public class QuizItem implements Parcelable {
	
//	private String filmTitle;
//	private String questionId;
//	private String questionText;
//	private String answer1;
//	private String answer2;
//	private String answer3;
//	private String answer4;
//	private String dateTime;
//	private String submittedAnswerIndex;
//	private String correctAnswerIndex;
//	private String answered = "false";
	
	// CURRENTLY 23 OBJECT ATTRIBUTES
	private String questionId; //
//	private String filmTitle; //
	private String answerSubmitCount; //
	private String questionText; //
	private String answer1; //
	private String answer2; //
	private String answer3; //
	private String answer4; //
	private String reaction1; //
	private String reaction2; //
	private String reaction3; //
	private String reaction4; //
	private String worldId; //
	private String worldTitle; //
	private String submittedAnswerIndex; //
	private String answered; //
	private String activeItem; //
	private String activeItem1; //
	private String activeItem2; //
	private String activeItem3; //
	private String activeItem4; //
	private String correctAnswerIndex; // not set on client
	private String level; //
	private String pointValue;
	
	public QuizItem() {
		// empty constructor
	}

	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeString(questionId);
//		pc.writeString(filmTitle);
		pc.writeString(answerSubmitCount);
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
		pc.writeString(correctAnswerIndex); // not set on client
//		pc.writeInt(correctAnswerIndex);
		pc.writeString(answered);
		pc.writeString(activeItem);
		pc.writeString(activeItem1);
		pc.writeString(activeItem2);
		pc.writeString(activeItem3);
		pc.writeString(activeItem4);
		pc.writeString(level);
		pc.writeString(pointValue);
	}	
	
	public QuizItem(Parcel pc) {
//		pc.readParcelable(Thread.currentThread().getContextClassLoader());
//		ClassLoaderHelper.setClassLoader(Thread.currentThread().getContextClassLoader());
		questionId = pc.readString();
//		filmTitle = pc.readString();
		answerSubmitCount = pc.readString();
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
		correctAnswerIndex = pc.readString(); // not set on client
		answered = pc.readString();
		activeItem = pc.readString();
		activeItem1 = pc.readString();
		activeItem2 = pc.readString();
		activeItem3 = pc.readString();
		activeItem4 = pc.readString();
		level = pc.readString();
		pointValue = pc.readString();
	}

	public static final Parcelable.Creator<QuizItem> CREATOR = new Parcelable.Creator<QuizItem>() {
		public QuizItem createFromParcel(Parcel in) {
			return new QuizItem(in);
		}

		public QuizItem[] newArray(int size) {
			return new QuizItem[size];
		}
	};
		
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


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
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


	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}
	public void setCorrectAnswerIndex(String correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
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


	public static Parcelable.Creator<QuizItem> getCreator() {
		return CREATOR;
	}


	public String getAnswerSubmitCount() {
		return answerSubmitCount;
	}


	public void setAnswerSubmitCount(String answerSubmitCount) {
		this.answerSubmitCount = answerSubmitCount;
	}


	public String getAnswered() {
		return answered;
	}


	public void setAnswered(String answered) {
		this.answered = answered;
	}


//	public String getFilmTitle() {
//		return filmTitle;
//	}
//
//
//	public void setFilmTitle(String filmTitle) {
//		this.filmTitle = filmTitle;
//	}

	public void setSubmittedIndex(String submittedAnswerIndex) {
		this.submittedAnswerIndex = submittedAnswerIndex;
	}


	public String getSubmittedIndex() {
		return submittedAnswerIndex;
	}


	public String getPointValue() {
		return pointValue;
	}


	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}
}
