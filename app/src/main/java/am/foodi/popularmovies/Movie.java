package am.foodi.popularmovies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//public class Movie implements Serializable
public class Movie implements Parcelable {

    public Boolean adult;
    public String backdrop_path;
    public int[] genre_ids;
    public int id;
    public String original_language;
    public String original_title;
    public String overview;
    public String release_date;
    public String poster_path;
    public double popularity;
    public String title;
    public Boolean video;
    public double vote_average;
    public int vote_count;
    private String baseURL = "http://image.tmdb.org/t/p/";
    private String defaultSize = "w185";

    public int describeContents() {
        return 0;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("adult", adult);
        bundle.putString("backdrop_path", backdrop_path);
        bundle.putIntArray("genre_ids", genre_ids);
        bundle.putInt("id", id);
        bundle.putString("original_language", original_language);
        bundle.putString("original_title", original_title);
        bundle.putString("overview", overview);
        bundle.putString("release_date", release_date);
        bundle.putString("poster_path", poster_path);
        bundle.putDouble("popularity", popularity);
        bundle.putString("title", title);
        bundle.putBoolean("video", video);
        bundle.putDouble("vote_average", vote_average);
        bundle.putInt("vote_count", vote_count);
        return bundle;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeBundle(getBundle());
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>(){
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {
        Bundle bundle = in.readBundle();
        adult = bundle.getBoolean("adult");
        backdrop_path = bundle.getString("backdrop_path");
        genre_ids = bundle.getIntArray("genre_ids");
        id = bundle.getInt("id");
        original_language = bundle.getString("original_language");
        original_title = bundle.getString("original_title");
        overview = bundle.getString("overview");
        release_date = bundle.getString("release_date");
        poster_path = bundle.getString("poster_path");
        popularity = bundle.getDouble("popularity");
        title = bundle.getString("title");
        video = bundle.getBoolean("video");
        vote_average = bundle.getDouble("vote_average");
        vote_count = bundle.getInt("vote_count");
    }

    public Movie(JSONObject movie) throws JSONException {
        adult = movie.getBoolean("adult");
        backdrop_path = getStringOrNull(movie, "backdrop_path");
        JSONArray genres = movie.getJSONArray("genre_ids");
        genre_ids = new int[genres.length()];
        for(int i=0; i<genres.length(); i++) {
            genre_ids[i]=genres.getInt(i);
        }
        id = movie.getInt("id");
        original_language = getStringOrNull(movie, "original_language");
        original_title = getStringOrNull(movie, "original_title");
        overview = getStringOrNull(movie, "overview");
        release_date = getStringOrNull(movie, "release_date");
        poster_path = getStringOrNull(movie, "poster_path");
        popularity = movie.getDouble("popularity");
        title = getStringOrNull(movie, "title");
        video = movie.getBoolean("video");
        vote_average = movie.getDouble("vote_average");
        vote_count = movie.getInt("vote_count");
    }

        private String getStringOrNull(JSONObject movie, String key) throws JSONException {
            String val = movie.getString(key);
            if (val=="null") {
                return null;
            } else {
                return val;
            }
        }

        public String getYear() {
            if (this.release_date == null) {
                return "";
            } else {
                return this.release_date.replaceFirst("-.*", "");
            }
        }

    public String getPosterURL() {
        return this.getPosterURL(defaultSize);
    }

    public String getPosterURL(String size) {
        return baseURL + size + poster_path;
    }
}
