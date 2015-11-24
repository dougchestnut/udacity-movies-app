package am.foodi.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

public class Video {
    public String id;
    public String language;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;

    public Video(JSONObject video) throws JSONException {
        id = video.getString("id");
        language = video.getString("iso_639_1");
        key = video.getString("key");
        name = video.getString("name");
        site = video.getString("site");
        size = video.getInt("size");
        type = video.getString("type");
    }
}