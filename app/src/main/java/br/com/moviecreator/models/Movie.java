/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.moviecreator.models;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mateus Emanuel Araújo on 24/10/15.
 * teusemanuel@gmail.com
 */
public class Movie implements Serializable {

    private Integer id;

    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Rated")
    private String rated;

    @SerializedName("Released")
    private String released;

    @SerializedName("Runtime")
    private String runtime;

    @SerializedName("Genre")
    private String genre;

    @SerializedName("Director")
    private String director;

    @SerializedName("Writer")
    private String writer;

    @SerializedName("Actors")
    private String actors;

    @SerializedName("Plot")
    private String plot;

    @SerializedName("Language")
    private String language;

    @SerializedName("Country")
    private String country;

    @SerializedName("Awards")
    private String awards;

    @SerializedName("Metascore")
    private String metascore;

    @SerializedName("imdbRating")
    private String imdbRating;

    @SerializedName("imdbVotes")
    private String imdbVotes;

    @SerializedName("imdbID")
    private String imdbID;

    @SerializedName("Type")
    private String type;

    @SerializedName("Poster")
    private String poster;

    @SerializedName("Response")
    private Boolean response;

    private String posterPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_ID = _ID;
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_IMDB_ID = "imdbID";
        public static final String COLUMN_NAME_RATED = "rated";
        public static final String COLUMN_NAME_RELEASED = "released";
        public static final String COLUMN_NAME_RUNTIME = "runtime";
        public static final String COLUMN_NAME_GENRE = "genre";
        public static final String COLUMN_NAME_DIRECTOR = "director";
        public static final String COLUMN_NAME_WRITER = "writer";
        public static final String COLUMN_NAME_ACTORS = "actors";
        public static final String COLUMN_NAME_PLOT = "plot";
        public static final String COLUMN_NAME_LANGUAGE = "language";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_AWARDS = "awards";
        public static final String COLUMN_NAME_METASCORE = "metascore";
        public static final String COLUMN_NAME_IMDB_RATING = "imdbRating";
        public static final String COLUMN_NAME_IMDB_VOTES = "imdbVotes";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_POSTER = "poster";
        public static final String COLUMN_NAME_POSTER_PATH = "posterPath";


        public static final String MOVIE_TABLE_CREATE =
                "CREATE TABLE "+ Movie.MovieEntry.TABLE_NAME+" (" +
                        Movie.MovieEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        Movie.MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_YEAR + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_IMDB_ID + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_TYPE + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_RATED + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_RELEASED + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_RUNTIME + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_GENRE + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_DIRECTOR + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_WRITER + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_ACTORS + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_PLOT + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_LANGUAGE + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_COUNTRY + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_AWARDS + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_METASCORE + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_IMDB_RATING + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_IMDB_VOTES + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_POSTER + " TEXT," +
                        Movie.MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT);";

        public static final String MOVIE_TABLE_DELETE = "DROP TABLE IF EXISTS MOVIE";
    }
}
