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
package br.com.moviecreator.views.archived;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.moviecreator.R;
import br.com.moviecreator.models.Movie;
import br.com.moviecreator.persistence.MovieDAO;
import br.com.moviecreator.utils.AppNavigationListner;
import br.com.moviecreator.views.adapters.ItemClickListener;
import br.com.moviecreator.views.adapters.MoviesAdapter;
import br.com.moviecreator.views.adapters.SpacesItemDecoration;
import br.com.moviecreator.views.details.DetailsActivity;
import br.com.moviecreator.views.fragments.AbstractFragment;

/**
 * Created by Mateus Emanuel Araújo on 24/10/15.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class ArchivedMoviesFragment extends AbstractFragment {

    private static final String TAG = ArchivedMoviesFragment.class.getSimpleName();
    private String criteria;
    private AppNavigationListner navigationListner;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter adapter;

    public ArchivedMoviesFragment(){}

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.title_archived));
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
        View rootView = inflater.inflate(R.layout.archived_app_bar, container, false);

        List<Movie> movieList = MovieDAO.findAllMovies(getActivity());
        adapter = new MoviesAdapter(movieList, LayoutInflater.from(getActivity()), new ItemClickListener<Movie>() {
            @Override
            public void onItemClick(Movie movie) {
                Bundle extra = new Bundle();
                extra.putSerializable(DetailsActivity.DETAILS_EXTRA, movie);
                extra.putBoolean(DetailsActivity.IS_LOCAL_FILE, true);

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(extra);

                getActivity().startActivity(intent);
            }
        });

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        RecyclerView recyclerViewMovies = (RecyclerView) rootView.findViewById(R.id.movies_result_list);
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
        recyclerViewMovies.setAdapter(adapter);
        recyclerViewMovies.addItemDecoration(new SpacesItemDecoration(10));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
