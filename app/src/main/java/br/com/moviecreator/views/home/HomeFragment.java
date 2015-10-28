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
package br.com.moviecreator.views.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import br.com.moviecreator.R;
import br.com.moviecreator.models.Movie;
import br.com.moviecreator.models.MoviesSearch;
import br.com.moviecreator.proxies.MovieProxy;
import br.com.moviecreator.proxies.listeners.ProxyListener;
import br.com.moviecreator.utils.AppNavigationListner;
import br.com.moviecreator.views.adapters.ItemClickListener;
import br.com.moviecreator.views.adapters.MoviesAdapter;
import br.com.moviecreator.views.adapters.SpacesItemDecoration;
import br.com.moviecreator.views.create.CreateMovieFragment;
import br.com.moviecreator.views.details.DetailsActivity;
import br.com.moviecreator.views.fragments.AbstractFragment;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public class HomeFragment extends AbstractFragment {

    private final static String TAG = HomeFragment.class.getSimpleName();
    private String criteria;
    private AppNavigationListner navigationListner;
    private LinearLayout boryLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter adapter;

    public HomeFragment() {
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.title_home));
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
        View rootView = inflater.inflate(R.layout.home_app_bar, container, false);


        adapter = new MoviesAdapter(new ArrayList<Movie>(), LayoutInflater.from(getActivity()), new ItemClickListener<Movie>() {
            @Override
            public void onItemClick(Movie movie) {
                Bundle extra = new Bundle();
                extra.putSerializable(DetailsActivity.DETAILS_EXTRA, movie);

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(extra);

                getActivity().startActivity(intent);
            }
        });

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        boryLayout = (LinearLayout) rootView.findViewById(R.id.home_bory_search);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setEnabled(false);

        RecyclerView recyclerViewMovies = (RecyclerView) rootView.findViewById(R.id.movies_result_list);
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
        recyclerViewMovies.setAdapter(adapter);
        recyclerViewMovies.addItemDecoration(new SpacesItemDecoration(10));

        boryLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.title_home));

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        navigationListner.updateCurrentFragment(new CreateMovieFragment());
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);

        initSearch(menu.findItem(R.id.action_search));
    }



    private void initSearch(MenuItem item) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        if (criteria != null) {
            searchView.setIconified(false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals(criteria) && query.length() > 2) {
                    adapter.clear();
                    searchMovie(query);
                    criteria = query;
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                if (query.equals(criteria) || query.length() < 3) {
                    return true;
                }
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                /*adapter.clear();

                fetch(null, null);*/

                return false;
            }
        });
    }

    public void searchMovie(final String nameMovie) {
        new MovieProxy(getActivity()).findMoviesByName(getRequestTag(), nameMovie, new ProxyListener<MoviesSearch>() {
            @Override
            public void onStart() {
                boryLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onSuccess(MoviesSearch response) {

                adapter.addAll(response.getResult());
            }

            @Override
            public void onFailure(VolleyError error) {
                Snackbar.make(getView(), "Error fetching the movie", Snackbar.LENGTH_LONG)
                        .setAction("Try again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchMovie(nameMovie);
                            }
                        }).show();

            }

            @Override
            public void onComplete() {
                if(adapter.getItemCount() > 0) {
                    boryLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                } else {
                    boryLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, getView());
    }
}
