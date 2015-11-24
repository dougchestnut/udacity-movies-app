package am.foodi.popularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FetchMovieReviewsTask extends FetchTheMovieDbJSONTask {

    private OnReviewsFetchCompleted listener;

    public interface OnReviewsFetchCompleted{
        void onReviewsFetchCompleted(Review[] result);
    }

    public FetchMovieReviewsTask(Context context, OnReviewsFetchCompleted listener, int movie_id) {
        super(context);
        this.listener=listener;
        this.uriBuilder.appendEncodedPath("/3/movie/"+movie_id+"/reviews");
    }

    public Review[] getItemsFromJSON(String reviewJsonStr) throws JSONException {
        JSONObject reviewsJson = new JSONObject(reviewJsonStr);
        JSONArray reviewArray = reviewsJson.getJSONArray("results");

        Review[] resultReviews = new Review[ reviewArray.length() ];
        for(int i = 0; i < reviewArray.length(); i++) {
            JSONObject review = reviewArray.getJSONObject(i);
            resultReviews[i] = new Review(review);
        }
        return resultReviews;
    }

    protected void onPostExecute(Object[] result){
        if (result != null) {
            Review[] reviewArray = Arrays.copyOf(result, result.length, Review[].class);
            listener.onReviewsFetchCompleted(reviewArray);
        }
    }
}