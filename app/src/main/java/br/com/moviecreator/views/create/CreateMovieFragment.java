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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.moviecreator.R;
import br.com.moviecreator.utils.AppNavigationListner;
import br.com.moviecreator.views.fragments.AbstractFragment;

/**
 * Created by Mateus Emanuel Araújo on 24/10/15.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class CreateMovieFragment extends AbstractFragment {

    private static final String TAG = CreateMovieFragment.class.getSimpleName();
    private AppNavigationListner navigationListner;

    public CreateMovieFragment() {}

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
        return inflater.inflate(R.layout.new_app_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.title_create));
    }
}
