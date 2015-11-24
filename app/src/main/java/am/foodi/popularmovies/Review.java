package am.foodi.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {
    public String id;
    public String author;
    public String content;
    public String url;

    public Review(JSONObject review) throws JSONException {
        id = review.getString("id");
        author = review.getString("author");
        content = review.getString("content");
        url = review.getString("url");
    }
}