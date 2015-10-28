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
package br.com.moviecreator.views.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.util.Collection;
import java.util.List;

import br.com.moviecreator.R;
import br.com.moviecreator.models.Movie;
import br.com.moviecreator.views.app.App;

/**
 * Created by Mateus Emanuel Araújo on 24/10/15.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder>{

    private List<Movie> movies;
    private LayoutInflater inflater;
    private final ItemClickListener<Movie> listener;

    public MoviesAdapter(List<Movie> movies, LayoutInflater inflater, ItemClickListener<Movie> listener) {
        this.movies = movies;
        this.inflater = inflater;
        this.listener = listener;
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movie_content_adapter, parent, false);
        return new MoviesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesHolder holder, int position) {
        if(movies != null && movies.size() > 0) {
            Movie movie = movies.get(position);
            holder.setMovie(movie);
        }
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void addAll(Collection<Movie> collection) {
        int start = getItemCount();
        int count = 0;

        for (Movie object : collection) {
            if (movies.add(object)) {
                count++;
            }
        }

        notifyItemRangeInserted(start, count);
    }

    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NetworkImageView posterImageView;
        private TextView movieNameTextView;
        private TextView movieDetailTextView;

        private Movie movie;
        public MoviesHolder(View itemView) {
            super(itemView);

            posterImageView = (NetworkImageView) itemView.findViewById(R.id.network_image_view_thumbnail);
            posterImageView.setDefaultImageResId(R.drawable.ic_broken_image_black_48dp);
            posterImageView.setErrorImageResId(R.drawable.ic_broken_image_black_48dp);

            movieNameTextView = (TextView) itemView.findViewById(R.id.card_movie_title);
            movieDetailTextView = (TextView) itemView.findViewById(R.id.card_movie_details);

            itemView.setOnClickListener(this);
        }

        public void setMovie(Movie movie) {
            this.movie = movie;


            if(TextUtils.isEmpty(movie.getPosterPath()) && !TextUtils.isEmpty(movie.getPoster()) && movie.getPoster().startsWith("http://ia.media-imdb.com")) {
                posterImageView.setImageUrl(movie.getPoster(), App.getApplication().getImageLoader());
            } else if(!TextUtils.isEmpty(movie.getPosterPath())) {
                File imgFile = new  File(movie.getPosterPath());
                if(imgFile.exists()) {
                    Bitmap movieBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    posterImageView.setImageBitmap(movieBitmap);
                } else {
                    posterImageView.setImageResource(R.drawable.ic_broken_image_black_48dp);
                }
            } else {
                posterImageView.setImageResource(R.drawable.ic_broken_image_black_48dp);
            }
            movieNameTextView.setText(movie.getTitle());
            movieDetailTextView.setText(String.format("%s, %s", movie.getYear(), movie.getType()));
        }

        @Override
        public void onClick(View v) {
            getListener().onItemClick(movie);
        }
    }

    public ItemClickListener<Movie> getListener() {
        return listener;
    }

    public void clear() {
        if(this.movies != null) {
            int count = getItemCount();
            this.movies.clear();

            notifyItemRangeRemoved(0, count);
        }
    }
}
