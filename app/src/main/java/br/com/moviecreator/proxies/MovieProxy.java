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
package br.com.moviecreator.proxies;

import android.app.Activity;
import android.view.View;

import com.android.volley.Request;

import br.com.moviecreator.models.MoviesSearch;
import br.com.moviecreator.proxies.endpoints.Urls;
import br.com.moviecreator.proxies.listeners.ProxyListener;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class MovieProxy extends AbstractProxy {
    public MovieProxy(Activity activity) {
        super(activity);
    }

    public void findMoviesByName(String tag, String name, ProxyListener<MoviesSearch> proxyListener, View view) {
        createAndScheduleGsonRequest(tag, Request.Method.GET, Urls.findMoviesByName(name), MoviesSearch.class, null, null, proxyListener, view);
    }

    public void getMovieByName(String tag, String name, ProxyListener<Void> proxyListener, View view) {
        createAndScheduleGsonRequest(tag, Request.Method.GET, Urls.getMovieByName(name), Void.class, null, null, proxyListener, view);
    }
}
