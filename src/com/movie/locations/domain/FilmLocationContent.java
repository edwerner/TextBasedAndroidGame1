package com.movie.locations.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;

//import com.movie.locations.application.MainActivity;
import com.movie.locations.application.ItemListActivity;
import com.movie.locations.application.MainActivity;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.service.FilmLocationService;

public class FilmLocationContent {

	public static List<Item> ITEMS = new ArrayList<Item>();

	public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();
	
	private static MovieLocationsImpl datasource;
	
	private static MainActivity activity;
	
	static {
//		datasource = new MovieLocationsImpl(activity.getContext());
//		if (datasource.selectRecords().isEmpty()) {
			// make async call
			for (int i = 0; i < 20; i++) {
//				for (FilmLocation film : datasource.selectRecords()) {
//					addItem(new BagItem(film.getSid().toString(), film.getTitle().toString()));
					addItem(new Item(Integer.toString(i), "Film " + Integer.toString(i)));
//				}
			}
//		} 
//		else {
////			addItem(new BagItem(Integer.toString(i), "Film " + Integer.toString(i)));
//			addItem(new BagItem("1", "Film 1"));
//		}

	}

	private static void addItem(Item item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static class Item {
		public String id;
		public String content;

		public Item(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
