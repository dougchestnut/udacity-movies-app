package am.foodi.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {

    private final String LOG_TAG = MovieListFragment.class.getSimpleName();
    private ImageAdapter mMovieAdpater;
    private int sort_index;
    private String apiKey = "**************************************";

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState == null) {
            sort_index = 0;
        } else {
            sort_index = savedInstanceState.getInt("sort_index");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.v(LOG_TAG, "selected id: " + id);

        SelectSortFragment selectSort = new SelectSortFragment();
        Bundle b = new Bundle();
        b.putInt("sort_index",sort_index);
        selectSort.setArguments(b);
        selectSort.show(getFragmentManager(),"sort");

        return super.onOptionsItemSelected(item);
    }

    public void updateSort(int index) {
        Log.v(LOG_TAG, "sort passed to fragment: "+sort_index);
        sort_index = index;
        fetchMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdpater = new ImageAdapter(getActivity());

        GridView gridView = (GridView)  rootView.findViewById( R.id.gridview_movies );
        gridView.setAdapter(mMovieAdpater);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdpater.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("movie", movie);

                startActivity(intent);
            }
        });

        fetchMovies();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("sort_index", sort_index);
    }

    public void fetchMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(sort_index);
        moviesTask.execute();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private int sort_index = 0;

        public FetchMoviesTask(int sort_index) {
            super();
            this.sort_index = sort_index;
        }

        private Movie[] getMoviesFromJson(String movieJsonStr)
                throws JSONException {

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            Movie[] resultMovies = new Movie[ movieArray.length() ];
            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                resultMovies[i] = new Movie(movie);
            }

            for (Movie m : resultMovies) {
                Log.v(LOG_TAG, "Movie title: " + m.title);
            }
            return resultMovies;
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                Log.v(LOG_TAG, "sort index of: "+sort_index);
                String sort_param = "sort_by=popularity.desc";
                if (sort_index == 1) {
                    sort_param = "sort_by=vote_average.desc";
                }

                URL url = new URL("http://api.themoviedb.org/3/discover/movie?api_key="+apiKey+"&"+sort_param);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                mMovieAdpater.clear();
                for(Movie movie: result) {
                    if (movie.poster_path != null) {
                        mMovieAdpater.add(movie);
                    }
                }
            }
        }
    }
}
