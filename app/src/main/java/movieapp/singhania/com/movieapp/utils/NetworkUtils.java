/*
 * Copyright (C) 2016 The Android Open Source Project
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
package movieapp.singhania.com.movieapp.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    public final static String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    public final static String MOVIE_TRAILER_BASE_URL =
            "https://www.youtube.com/watch?v=";

    public final static String MOVIE_POPULAR =
            "popular";
    public final static String MOVIE_TOP_RATED =
            "top_rated";
    public final static String MOVIE_TRAILER =
            "/videos";
    public final static String MOVIE_REVIEW =
            "/reviews";
    // /discover/movie?sort_by=popularity.desc
    //  .https://api.themoviedb.org/3/discover/movie

    // Then you will need a ‘size’, which will be one of the following: "w92", "w154"
    // , "w185", "w342", "w500", "w780", or "original"
    final static String PARAM_QUERY = "q";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "sort_by";
    final static String PARAM_API = "api_key";

    final static String PARAM_SORT_VALUE = "popularity.desc";
    final static String sortBy = "stars";

    public static URL buildUrl(String path) {

        Uri builtUri = Uri.parse(MOVIE_BASE_URL+path).buildUpon()
                .appendQueryParameter(PARAM_API, APIConstants.PARAM_API_KEY)
                .appendQueryParameter(PARAM_SORT,PARAM_SORT_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}