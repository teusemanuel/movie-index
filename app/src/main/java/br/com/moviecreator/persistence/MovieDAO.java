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
package br.com.moviecreator.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.moviecreator.models.Movie;

/**
 * Created by Mateus Emanuel Araújo on 26/10/15.
 * teusemanuel@gmail.com
 */
public class MovieDAO {

    public static final Long save(@NonNull Movie movie, @NonNull Context context) {
        SQLiteDatabase db = Entity.getInstance(context).getWritableDatabase();
        long id = db.insert(Movie.MovieEntry.TABLE_NAME, null, getContentValues(movie));
        movie.setId((int) id);
        return id;
    }

    public static final boolean delete(@NonNull Integer id, @NonNull Context context) {
        SQLiteDatabase db = Entity.getInstance(context).getWritableDatabase();
        int deletedRows = db.delete(Movie.MovieEntry.TABLE_NAME, null, new String[]{String.valueOf(id)});
        return deletedRows > 0 ? true : false;
    }

    public static final String saveImageInFile(Context context, Bitmap bitmap, String fileUrl) {
        String filename = fileUrl.replaceAll(".*/([^/]+)", "$1");
        File file = new File(context.getFilesDir(), filename);
        if(file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file.getPath();
        } catch (Exception e) {
            Log.e(MovieDAO.class.getSimpleName(), "Error on write file", e);
        }
        return null;
    }

    public static final void deleteImageInFile(Context context, String fileUrl) {
        String filename = fileUrl.replaceAll(".*/([^/]+)", "$1");
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            file.delete();
        }
    }

    public static final List<Movie> findMoviesByName(@NonNull String name, @NonNull Context context) {
        SQLiteDatabase db = Entity.getInstance(context).getWritableDatabase();
        List<Movie> resul = new ArrayList<Movie>();

        Cursor c = db.query(
                Movie.MovieEntry.TABLE_NAME,
                getColumns(),
                "title LIKE ?",
                new String[]{String.format("%s%%", name)},
                null,
                null,
                Movie.MovieEntry.COLUMN_NAME_TITLE);
        if (c.moveToFirst()) {
            do {
                resul.add(getMovieByCursor(c));
            } while (c.moveToNext());
            c.close();
        }

        return resul;
    }
    public static final List<Movie> findAllMovies(@NonNull Context context) {
        SQLiteDatabase db = Entity.getInstance(context).getWritableDatabase();
        List<Movie> resul = new ArrayList<Movie>();

        Cursor c = db.query(
                Movie.MovieEntry.TABLE_NAME,
                getColumns(),
                null,
                null,
                null,
                null,
                Movie.MovieEntry.COLUMN_NAME_TITLE);
        if (c.moveToFirst()) {
            do {
                resul.add(getMovieByCursor(c));
            } while (c.moveToNext());
            c.close();
        }

        return resul;
    }

    public static final Movie getMovieById(@NonNull Integer id, @NonNull Context context) {
        SQLiteDatabase db = Entity.getInstance(context).getWritableDatabase();
        Cursor c = db.query(
                Movie.MovieEntry.TABLE_NAME,
                getColumns(),
                "_id = ?",
                new String[]{id.toString()},
                null,
                null,
                Movie.MovieEntry.COLUMN_NAME_TITLE);
        if(c.moveToFirst()) {
            return getMovieByCursor(c);
        }
        return null;
    }

    private static final String[] getColumns() {
        return new String[]{
                Movie.MovieEntry.COLUMN_NAME_ID,
                Movie.MovieEntry.COLUMN_NAME_TITLE,
                Movie.MovieEntry.COLUMN_NAME_YEAR,
                Movie.MovieEntry.COLUMN_NAME_IMDB_ID,
                Movie.MovieEntry.COLUMN_NAME_TYPE,
                Movie.MovieEntry.COLUMN_NAME_RATED,
                Movie.MovieEntry.COLUMN_NAME_RELEASED,
                Movie.MovieEntry.COLUMN_NAME_RUNTIME,
                Movie.MovieEntry.COLUMN_NAME_GENRE,
                Movie.MovieEntry.COLUMN_NAME_DIRECTOR,
                Movie.MovieEntry.COLUMN_NAME_WRITER,
                Movie.MovieEntry.COLUMN_NAME_ACTORS,
                Movie.MovieEntry.COLUMN_NAME_PLOT,
                Movie.MovieEntry.COLUMN_NAME_LANGUAGE,
                Movie.MovieEntry.COLUMN_NAME_COUNTRY,
                Movie.MovieEntry.COLUMN_NAME_AWARDS,
                Movie.MovieEntry.COLUMN_NAME_METASCORE,
                Movie.MovieEntry.COLUMN_NAME_IMDB_RATING,
                Movie.MovieEntry.COLUMN_NAME_IMDB_VOTES,
                Movie.MovieEntry.COLUMN_NAME_POSTER,
                Movie.MovieEntry.COLUMN_NAME_POSTER_PATH
        };
    }


    private static final Movie getMovieByCursor(Cursor cursor) {

        Movie movie = new Movie();

        Integer itemId = cursor.getInt(cursor.getColumnIndexOrThrow(Movie.MovieEntry._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_TITLE));
        String year = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_YEAR));
        String rated = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_RATED));
        String released = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_RELEASED));
        String runtime = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_RUNTIME));
        String genre = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_GENRE));
        String director = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_DIRECTOR));
        String writer = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_WRITER));
        String actors = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_ACTORS));
        String plot = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_PLOT));
        String language = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_LANGUAGE));
        String country = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_COUNTRY));
        String awards = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_AWARDS));
        String metascore = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_METASCORE));
        String imdbRating = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_IMDB_RATING));
        String imdbVotes = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_IMDB_VOTES));
        String imdbID = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_IMDB_ID));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_TYPE));
        String poster = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_POSTER));
        String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(Movie.MovieEntry.COLUMN_NAME_POSTER_PATH));

        movie.setId(itemId);
        movie.setTitle(title);
        movie.setYear(year);
        movie.setRated(rated);
        movie.setReleased(released);
        movie.setRuntime(runtime);
        movie.setGenre(genre);
        movie.setDirector(director);
        movie.setWriter(writer);
        movie.setActors(actors);
        movie.setPlot(plot);
        movie.setLanguage(language);
        movie.setCountry(country);
        movie.setAwards(awards);
        movie.setMetascore(metascore);
        movie.setImdbRating(imdbRating);
        movie.setImdbVotes(imdbVotes);
        movie.setImdbID(imdbID);
        movie.setType(type);
        movie.setPoster(poster);
        movie.setPosterPath(posterPath);


        return movie;
    }

    private static final ContentValues getContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        if (movie == null) {
            return values;
        } else {
            if (movie.getId() != null && !movie.getId().equals(0)) {
                values.put(Movie.MovieEntry.COLUMN_NAME_ID, movie.getId());
            }
            if (!TextUtils.isEmpty(movie.getTitle())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
            }

            if (!TextUtils.isEmpty(movie.getYear())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_YEAR, movie.getYear());
            }
            if (!TextUtils.isEmpty(movie.getImdbID())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_IMDB_ID, movie.getImdbID());
            }
            if (!TextUtils.isEmpty(movie.getType())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_TYPE, movie.getType());
            }
            if (!TextUtils.isEmpty(movie.getRated())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_RATED, movie.getRated());
            }
            if (!TextUtils.isEmpty(movie.getReleased())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_RELEASED, movie.getReleased());
            }
            if (!TextUtils.isEmpty(movie.getRuntime())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_RUNTIME, movie.getRuntime());
            }
            if (!TextUtils.isEmpty(movie.getGenre())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_GENRE, movie.getGenre());
            }
            if (!TextUtils.isEmpty(movie.getDirector())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_DIRECTOR, movie.getDirector());
            }
            if (!TextUtils.isEmpty(movie.getWriter())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_WRITER, movie.getWriter());
            }
            if (!TextUtils.isEmpty(movie.getActors())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_ACTORS, movie.getActors());
            }
            if (!TextUtils.isEmpty(movie.getPlot())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_PLOT, movie.getPlot());
            }
            if (!TextUtils.isEmpty(movie.getLanguage())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_LANGUAGE, movie.getLanguage());
            }
            if (!TextUtils.isEmpty(movie.getCountry())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_COUNTRY, movie.getCountry());
            }
            if (!TextUtils.isEmpty(movie.getAwards())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_AWARDS, movie.getAwards());
            }
            if (!TextUtils.isEmpty(movie.getMetascore())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_METASCORE, movie.getMetascore());
            }
            if (!TextUtils.isEmpty(movie.getImdbRating())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_IMDB_RATING, movie.getImdbRating());
            }
            if (!TextUtils.isEmpty(movie.getImdbVotes())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_IMDB_VOTES, movie.getImdbVotes());
            }
            if (!TextUtils.isEmpty(movie.getPoster())) {
                values.put(Movie.MovieEntry.COLUMN_NAME_POSTER, movie.getPoster());
            }
        }

        return values;
    }
}
