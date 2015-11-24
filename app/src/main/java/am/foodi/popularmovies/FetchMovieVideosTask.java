package am.foodi.popularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FetchMovieVideosTask extends FetchTheMovieDbJSONTask {

    private OnVideosFetchCompleted listener;

    public interface OnVideosFetchCompleted{
        void onVideosFetchCompleted(Video[] result);
    }

    public FetchMovieVideosTask(Context context, OnVideosFetchCompleted listener, int movie_id) {
        super(context);
        this.listener = listener;
        this.uriBuilder.appendEncodedPath("/3/movie/"+movie_id+"/videos");
    }

    public Video[] getItemsFromJSON(String videoJsonStr) throws JSONException {
        JSONObject videosJson = new JSONObject(videoJsonStr);
        JSONArray videoArray = videosJson.getJSONArray("results");

        Video[] resultVideos = new Video[ videoArray.length() ];
        for(int i = 0; i < videoArray.length(); i++) {
            JSONObject video = videoArray.getJSONObject(i);
            resultVideos[i] = new Video(video);
        }
        return resultVideos;
    }


    protected void onPostExecute(Object[] result) {
        if (result != null) {
            Video[] videoArray = Arrays.copyOf(result, result.length, Video[].class);
            listener.onVideosFetchCompleted(videoArray);
        }
    }

}