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
package br.com.moviecreator.proxies.endpoints;

import br.com.moviecreator.R;
import br.com.moviecreator.views.app.App;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public class Urls {

    public static String findMoviesByName(String name) {
        return String.format(getUrlApi(), String.format("?s=%s", name));
    }

    public static String getMovieByName(String name) {
        return String.format(getUrlApi(), String.format("?t=%s", name));
    }

    private static String getUrlApi() {
        return App.getApplication().getResources().getString(R.string.movie_api_url);
    }
}
