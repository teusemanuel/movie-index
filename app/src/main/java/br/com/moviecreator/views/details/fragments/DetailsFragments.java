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
package br.com.moviecreator.views.details.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;

import br.com.moviecreator.R;
import br.com.moviecreator.models.Movie;
import br.com.moviecreator.persistence.MovieDAO;
import br.com.moviecreator.proxies.MovieProxy;
import br.com.moviecreator.proxies.listeners.ProxyListener;
import br.com.moviecreator.views.app.App;
import br.com.moviecreator.views.details.DetailsActivity;
import br.com.moviecreator.views.fragments.AbstractFragment;

/**
 * Created by Mateus Emanuel Araújo on 27/10/15.
 * teusemanuel@gmail.com
 */
public class DetailsFragments extends AbstractFragment {

    private static final String TAG = DetailsFragments.class.getSimpleName();
    private NetworkImageView posterDetailsImageView;
    private FloatingActionButton fab;
    private TextView title;
    private TextView actors;
    private TextView time;
    private TextView year;
    private TextView writer;
    private TextView plot;

    private Movie movie;
    private boolean isLocal;

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.title_details));
    }

    @Override
    protected String getRequestTag() {
        return TAG;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        movie = (Movie) bundle.getSerializable(DetailsActivity.DETAILS_EXTRA);
        isLocal = bundle.getBoolean(DetailsActivity.IS_LOCAL_FILE, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.details_fragment, container, false);
        posterDetailsImageView = (NetworkImageView) getActivity().findViewById(R.id.movie_image_details);

        title = (TextView) root.findViewById(R.id.details_title);
        actors = (TextView) root.findViewById(R.id.details_actors);
        time = (TextView) root.findViewById(R.id.details_time);
        year = (TextView) root.findViewById(R.id.details_year);
        writer = (TextView) root.findViewById(R.id.details_writer);
        plot = (TextView) root.findViewById(R.id.details_plot);



        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_details);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO action to save os delete movie in base
                isLocal = !isLocal;
                Bitmap bitmap = ((BitmapDrawable)posterDetailsImageView.getDrawable()).getBitmap();

                fab.setImageResource(isLocal ? R.drawable.ic_star_border_white_24dp : R.drawable.ic_star_white_24dp );
                String moviePath = MovieDAO.saveImageInFile(getActivity(), bitmap, movie.getPoster());
                movie.setPosterPath(moviePath);

                if(isLocal) {
                    MovieDAO.save(movie, getActivity());
                } else {
                    MovieDAO.delete(movie.getId(),getActivity());
                    MovieDAO.deleteImageInFile(getActivity(), movie.getPoster());
                    movie.setId(null);
                }
            }
        });
        fab.setImageResource(isLocal ? R.drawable.ic_star_border_white_24dp : R.drawable.ic_star_white_24dp);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {


        if(movie != null) {

            //local file
            if(isLocal) {
                setImageDetails(movie);
                setDetailsMovie(movie);

            }

            //internet file
            else {
                getMovieByTitle(movie.getTitle());
            }
        }

    }


    //changes the detail image of the film
    private void setImageDetails(Movie movie) {


        if(TextUtils.isEmpty(movie.getPosterPath())) {

            if(!TextUtils.isEmpty(movie.getPoster()) && movie.getPoster().startsWith("http://ia.media-imdb.com")) {
                posterDetailsImageView.setImageUrl(movie.getPoster(), App.getApplication().getImageLoader());
            } else {
                posterDetailsImageView.setImageResource(R.drawable.ic_broken_image_black_48dp);
            }

        } else {

            File imgFile = new  File(movie.getPosterPath());
            if(imgFile.exists()) {
                Bitmap movieBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                posterDetailsImageView.setImageBitmap(movieBitmap);
            } else {
                posterDetailsImageView.setImageResource(R.drawable.ic_broken_image_black_48dp);
            }
        }
    }

    private void setDetailsMovie(Movie movie) {
        title.setText(movie.getTitle());
        actors.setText(movie.getActors());
        time.setText(movie.getRuntime());
        year.setText(String.format("%s, %s", movie.getYear(), movie.getType()));
        writer.setText(movie.getWriter());
        plot.setText(movie.getPlot());
    }

    public void getMovieByTitle(final String titleMovie) {
        new MovieProxy(getActivity()).getMovieByName(getRequestTag(), titleMovie, new ProxyListener<Movie>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Movie movie) {
                setImageDetails(movie);
                setDetailsMovie(movie);
            }

            @Override
            public void onFailure(VolleyError error) {
                Snackbar.make(getView(), "Error fetching the movie", Snackbar.LENGTH_LONG)
                        .setAction("Try again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getMovieByTitle(titleMovie);
                            }
                        }).show();
            }

            @Override
            public void onComplete() {
            }
        }, getView());
    }
}
