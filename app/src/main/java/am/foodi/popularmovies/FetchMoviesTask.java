/*
 * Copyright (C) 2014 The Android Open Source Project
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
package am.foodi.popularmovies;


import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import am.foodi.popularmovies.data.MoviesContract;

public class FetchMoviesTask extends FetchTheMovieDbJSONTask {

    public static final int SORT_BY_POPULARITY_DESC = 0;
    public static final int SORT_BY_VOTE_DESC = 1;

    private Context mContext;

    public FetchMoviesTask(Context context, int sort_index) {
        super(context);
        uriBuilder.appendEncodedPath("/3/discover/movie");
        switch (sort_index) {
            case SORT_BY_POPULARITY_DESC:
                uriBuilder.appendQueryParameter("sort_by","popularity.desc");
                break;
            case SORT_BY_VOTE_DESC:
                uriBuilder.appendQueryParameter("sort_by","vote_average.desc");
                break;
        }
        mContext=context;
    }

    public Movie[] getItemsFromJSON(String movieJsonStr)
            throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray("results");

        Movie[] resultMovies = new Movie[ movieArray.length() ];
        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            resultMovies[i] = new Movie(movie);
        }

        return resultMovies;
    }

    protected void onPostExecute(Object[] result) {
        if (result != null) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>( result.length );
            for (Object item: result) {
                Movie movie = (Movie) item;
                if (movie.poster_path != null) { // only care about movies with posters right now
                    cVVector.add(movie.getContentValues());
                }
            }
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
            }

        }
    }
}
