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
package br.com.moviecreator.views.create;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import br.com.moviecreator.R;
import br.com.moviecreator.models.Movie;
import br.com.moviecreator.persistence.MovieDAO;
import br.com.moviecreator.proxies.MovieProxy;
import br.com.moviecreator.proxies.listeners.ProxyListener;
import br.com.moviecreator.utils.AppNavigationListner;
import br.com.moviecreator.views.app.App;
import br.com.moviecreator.views.details.DetailsActivity;
import br.com.moviecreator.views.fragments.AbstractFragment;

/**
 * Created by Mateus Emanuel Araújo on 24/10/15.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class CreateMovieFragment extends AbstractFragment {

    private static final String TAG = CreateMovieFragment.class.getSimpleName();
    private AppNavigationListner navigationListner;
    private Handler handler;
    private String criteria;
    private Movie movie;
    private TextInputLayout movieTitle;

    public CreateMovieFragment() {
        handler = new Handler();
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.title_create));
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
        navigationListner = (AppNavigationListner) bundle.getSerializable("NAVIGATION_LISTNER");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_app_bar, container, false);
        movieTitle = (TextInputLayout) root.findViewById(R.id.new_movie_title_wrapper);
        movieTitle.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable query) {
                if (query != null && (criteria == null || criteria.isEmpty() || !query.toString().equals(criteria)) && query.length() > 2) {
                    criteria = query.toString();
                    movieTitle.setErrorEnabled(false);
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            getMovie(criteria);
                        }
                    }, 1000);
                } else if (query == null || query.toString().isEmpty()) {
                    movie = null;
                    movieTitle.setError("Movie title is required.");
                }
            }
        });

        movieTitle.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getMovie(movieTitle.getEditText().getText().toString());
                    return true;
                }
                return false;
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getMovie(final String nameMovie) {
        new MovieProxy(getActivity()).getMovieByName(getRequestTag(), nameMovie, new ProxyListener<Movie>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Movie response) {
                movie = response;
                if (movie == null || movie.getResponse() == null || movie.getResponse() == false) {
                    movieTitle.setError("Movie not found");
                    movie = null;
                } else {
                    movieTitle.setErrorEnabled(false);
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Snackbar.make(getView(), "Error fetching the movie", Snackbar.LENGTH_LONG)
                        .setAction("Try again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getMovie(nameMovie);
                            }
                        }).show();
            }

            @Override
            public void onComplete() {

            }
        }, getView());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_movie, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();
        if (id == R.id.action_save) {
            if(movie == null) {
                Snackbar.make(getView(), "movie was not identified", Snackbar.LENGTH_LONG).show();
            } else {
                saveMovieOffLine();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveMovieOffLine() {
        new MovieProxy(getActivity()).getMoviePoster(getRequestTag(), movie.getPoster(), new ProxyListener<Bitmap>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Bitmap response) {
                String moviePath = MovieDAO.saveImageInFile(getActivity(), response, movie.getPoster());
                movie.setPosterPath(moviePath);
            }

            @Override
            public void onFailure(VolleyError error) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_broken_image_black_48dp);
                String moviePath = MovieDAO.saveImageInFile(getActivity(), icon, movie.getPoster());
                movie.setPosterPath(moviePath);
            }

            @Override
            public void onComplete() {
                MovieDAO.save(movie, getActivity());
                startDetails();
            }
        }, getView());
    }

    private void startDetails() {
        Bundle extra = new Bundle();
        extra.putSerializable(DetailsActivity.DETAILS_EXTRA, movie);
        extra.putBoolean(DetailsActivity.IS_LOCAL_FILE, true);

        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extra);

        getActivity().startActivity(intent);
    }
}
