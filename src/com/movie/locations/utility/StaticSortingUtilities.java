package com.movie.locations.utility;

import java.util.Comparator;

import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.NewsItem;
import com.movie.locations.domain.QuizItem;

public class StaticSortingUtilities {

	public static Comparator<BagItem> BAG_ITEMS_ALPHABETICAL_ORDER = new Comparator<BagItem>() {
		public int compare(BagItem str1, BagItem str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(
					str1.getItemTitle(), str2.getItemTitle());
			if (res == 0) {
				res = str1.getItemTitle().compareTo(str2.getItemTitle());
			}
			return res;
		}
	};
	
	public static Comparator<FilmLocation> LOCATIONS_ALPHABETICAL_ORDER = new Comparator<FilmLocation>() {
		public int compare(FilmLocation str1, FilmLocation str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(str1.getId(),
					str2.getId());
			if (res == 0) {
				res = str1.getId().compareTo(str2.getId());
			}
			return res;
		}
	};

	public static Comparator<QuizItem> QUIZ_ITEMS_ALPHABETICAL_ORDER = new Comparator<QuizItem>() {
		public int compare(QuizItem str1, QuizItem str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(
					str1.getWorldId(), str2.getWorldId());
			if (res == 0) {
				res = str1.getWorldId().compareTo(str2.getWorldId());
			}
			return res;
		}
	};

	public static Comparator<GameTitle> GAME_TITLES_ALPHABETICAL_ORDER = new Comparator<GameTitle>() {
		public int compare(GameTitle str1, GameTitle str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(
					str1.getTitle(), str2.getTitle());
			if (res == 0) {
				res = str1.getTitle().compareTo(str2.getTitle());
			}
			return res;
		}
	};

	public static Comparator<ConclusionCard> CARD_TITLES_ALPHABETICAL_ORDER = new Comparator<ConclusionCard>() {
		public int compare(ConclusionCard str1, ConclusionCard str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(
					str1.getTitle(), str2.getTitle());
			if (res == 0) {
				res = str1.getTitle().compareTo(str2.getTitle());
			}
			return res;
		}
	};

	public static Comparator<NewsItem> NEWS_ITEM_TITLES_ALPHABETICAL_ORDER = new Comparator<NewsItem>() {
		public int compare(NewsItem str1, NewsItem str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(
					str1.getTitle(), str2.getTitle());
			if (res == 0) {
				res = str1.getTitle().compareTo(str2.getTitle());
			}
			return res;
		}
	};

	public static Comparator<String> TITLE_STRINGS_ALPHABETICAL_ORDER = new Comparator<String>() {
		public int compare(String str1, String str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
			if (res == 0) {
				res = str1.compareTo(str2);
			}
			return res;
		}
	};
	
	private static int[] levelRange = {
			0, // LEVEL ZERO IS EMPTY
			0, // LEVEL ONE STARTS HERE
			200, // RETURN TO [1000] AFTER DEBUGGING
			3000, 6000, 10000, 15000, 21000, 28000, 36000, 45000, 55000, 66000,
			78000, 91000, 105000, 120000, 136000, 153000, 171000, 190000 };
	
	public static int[] getLevelRange() {
		return levelRange;
	}
	public static int CHECK_LEVEL_RANGE(String currentLevelString,
			int FINAL_UPDATED_TOTAL_USER_POINTS) {

		int level;

		// boolean isInRange = Math.min(num1,num2) <= inRange &&
		// Math.max(num1,num2) >= inRange;
		// int currentLevelMin =
		// Integer.parseInt(currentUser.getCurrentLevel());
		// int currentPoints = Integer.parseInt(currentUser.getCurrentPoints());
		// int currentLevelMax = Integer.parseInt(currentUser.getCurrentLevel())
		// + 1;

		int currentLevel = Integer.parseInt(currentLevelString);

		// ZERO INDEX - START AT INDEX ONE (1)
		final int CURRENT_LEVEL_INDEX = currentLevel;

		// NEXT LEVEL - START AT CURRENT LEVEL PLUS ONE (1)
		final int NEXT_LEVEL_INDEX = currentLevel + 1;
		
		// PREVIOUS LEVEL - START AT CURRENT LEVEL MINUS ONE (1)
		final int PREVIOUS_LEVEL_INDEX = currentLevel - 1;

		// int currentPoints = Integer.parseInt(currentPointsString);
		int currentLevelPointsMin = levelRange[CURRENT_LEVEL_INDEX];
		int currentLevelPointsMax = levelRange[NEXT_LEVEL_INDEX];
		int previousLevelPointsMin = levelRange[PREVIOUS_LEVEL_INDEX];

		// WE NEED A NUMBER THAT IS GREATER THAN CURRENT INDEX
		// AND LESS THAN NEXT INDEX TO BE WITHIN RANGE

		// INCREMENT COUNTER
		// final int UPCOMING_LEVEL = currentLevel + 1;

		if (FINAL_UPDATED_TOTAL_USER_POINTS >= currentLevelPointsMin && FINAL_UPDATED_TOTAL_USER_POINTS < currentLevelPointsMax) {
			// CURRENT LEVEL IS TRUE
			level = CURRENT_LEVEL_INDEX;
		} else if (FINAL_UPDATED_TOTAL_USER_POINTS >= previousLevelPointsMin && FINAL_UPDATED_TOTAL_USER_POINTS < currentLevelPointsMin) {
			// TIME TO ROLL BACK A LEVEL
			// CURRENT LEVEL MINUS ONE (1)
			level = PREVIOUS_LEVEL_INDEX;
		} else {
			// TIME FOR USER TO LEVEL UP
			// CURRENT LEVEL PLUS ONE (1)
			level = NEXT_LEVEL_INDEX;
		}

		// String updatedLevel = Integer.toString(level);

		return level;
	}
//	public static int CHECK_LEVEL_RANGE(String currentLevelString, int FINAL_UPDATED_TOTAL_USER_POINTS) {
//
//		int level;
//
//		// boolean isInRange = Math.min(num1,num2) <= inRange &&
//		// Math.max(num1,num2) >= inRange;
//		// int currentLevelMin =
//		// Integer.parseInt(currentUser.getCurrentLevel());
//		// int currentPoints = Integer.parseInt(currentUser.getCurrentPoints());
//		// int currentLevelMax = Integer.parseInt(currentUser.getCurrentLevel())
//		// + 1;
//
//		int currentLevel = Integer.parseInt(currentLevelString);
//
//		// ZERO INDEX - START AT INDEX ONE (1)
//		final int CURRENT_LEVEL_INDEX = currentLevel;
//
//		// NEXT LEVEL - START AT CURRENT LEVEL MINUS ONE (1)
//		final int NEXT_LEVEL_INDEX = currentLevel + 1;
//
//		// int currentPoints = Integer.parseInt(currentPointsString);
//		int currentLevelPointsMin = levelRange[CURRENT_LEVEL_INDEX];
//		int currentLevelPointsMax = levelRange[NEXT_LEVEL_INDEX];
//
//		// WE NEED A NUMBER THAT IS GREATER THAN CURRENT INDEX
//		// AND LESS THAN NEXT INDEX TO BE WITHIN RANGE
//
//		// INCREMENT COUNTER
//		// final int UPCOMING_LEVEL = currentLevel + 1;
//
//		if (FINAL_UPDATED_TOTAL_USER_POINTS >= currentLevelPointsMin && FINAL_UPDATED_TOTAL_USER_POINTS < currentLevelPointsMax) {
//			// CURRENT LEVEL IS TRUE
//			level = CURRENT_LEVEL_INDEX;
//		} else {
//			// TIME FOR USER TO LEVEL UP
//			// CURRENT LEVEL MINUS ONE (1)
//			level = NEXT_LEVEL_INDEX;
//		}
//
//		// String updatedLevel = Integer.toString(level);
//
//		return level;
//	}
}

