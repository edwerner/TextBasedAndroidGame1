package com.movie.locations.domain;

import org.codehaus.jackson.JsonNode;

// FilmLocation data object split into
// movie and metadata objects
/**
 * The Class FilmLocationDataObject.
 */
public class FilmLocationDataObject {
	
	/** The json. */
	private JsonNode json;
	
	/** The movies. */
	private JsonNode movies;
	
	/** The metadata. */
	private JsonNode metadata;
	
	/**
	 * Instantiates a new movie data object.
	 */
	public FilmLocationDataObject(JsonNode json) {
		setData(json);
		setMovies();
		setMetadata();
	}
	
	/**
	 * Sets the data.
	 *
	 * @param json the new data
	 */
	public void setData(JsonNode json) {
		this.json = json;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public JsonNode getData() {
		return json;
	}
	
	/**
	 * Sets the movies.
	 */
	public void setMovies() {
		this.movies = getData().get("data");
	}
	
	/**
	 * Gets the movies.
	 *
	 * @return the movies
	 */
	public JsonNode getMovies() {
		return movies;
	}
	
	/**
	 * Sets the metadata.
	 */
	public void setMetadata() {
		this.metadata = getData().get("meta").get("view");
	}
	
	/**
	 * Gets the metadata.
	 *
	 * @return the metadata
	 */
	public JsonNode getMetadata() {
		return metadata;
	}
}