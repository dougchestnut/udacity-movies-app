package am.foodi.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class FetchTheMovieDbJSONTask extends AsyncTask<String, Void, Object[]> {

    private final String THEMOVIEDB_BASE_URL = "http://api.themoviedb.org";
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final Context mContext;

    // Build up your URL with uriBuilder
    public Uri.Builder uriBuilder = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon();

    public FetchTheMovieDbJSONTask(Context context) {
        super();
        mContext = context;
    }

    // This should parse the result JSON and return your array of objects
    public abstract Object[] getItemsFromJSON(String jsonStr) throws JSONException;

    @Override
    protected Object[] doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr = null;

        try {
            uriBuilder.appendQueryParameter("api_key", BuildConfig.THEMOVIEDB_KEY);
            URL url = new URL(uriBuilder.build().toString());
            // Create the request, and open the connection
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
            jsonStr = buffer.toString();
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
            return getItemsFromJSON(jsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected abstract void onPostExecute(Object[] objects);
}
