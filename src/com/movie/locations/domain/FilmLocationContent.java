package com.movie.locations.domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmLocationContent {
	public static List<Item> ITEMS = new ArrayList<Item>();
	public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();
	
	static {
		for (int i = 0; i < 20; i++) {
			addItem(new Item(Integer.toString(i), "Film " + Integer.toString(i)));
		}
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
