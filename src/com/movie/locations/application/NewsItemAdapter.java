package com.movie.locations.application;

import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.QuizItem;

public class NewsItemAdapter {
	private BagItem latestBagItem;
	private ConclusionCard latestConclusionCard;
	private FilmLocation latestLocation;
	private QuizItem latestQuizItem;
	public BagItem getLatestBagItem() {
		return latestBagItem;
	}
	public void setLatestBagItem(BagItem latestBagItem) {
		this.latestBagItem = latestBagItem;
	}
	public ConclusionCard getLatestConclusionCard() {
		return latestConclusionCard;
	}
	public void setLatestConclusionCard(ConclusionCard latestConclusionCard) {
		this.latestConclusionCard = latestConclusionCard;
	}
	public FilmLocation getLatestLocation() {
		return latestLocation;
	}
	public void setLatestLocation(FilmLocation latestLocation) {
		this.latestLocation = latestLocation;
	}
	public QuizItem getLatestQuizItem() {
		return latestQuizItem;
	}
	public void setLatestQuizItem(QuizItem latestQuizItem) {
		this.latestQuizItem = latestQuizItem;
	}
	
}
