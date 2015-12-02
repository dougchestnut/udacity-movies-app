package am.foodi.popularmovies;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import am.foodi.popularmovies.data.MoviesContract;

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
    public Boolean favorite;
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
        bundle.putBoolean("favorite", favorite);
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

    public Movie(ContentValues cv){
        adult = cv.getAsBoolean(MoviesContract.MoviesEntry.COLUMN_ADULT);
        backdrop_path = cv.getAsString(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH);
        id = cv.getAsInteger(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);
        original_language = cv.getAsString(MoviesContract.MoviesEntry.COLUMN_LANGUAGE);
        original_title = cv.getAsString(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE);
        overview = cv.getAsString(MoviesContract.MoviesEntry.COLUMN_OVERVIEW);
        release_date = cv.getAsString(MoviesContract.MoviesEntry.COLUMN_DATE);
        poster_path = cv.getAsString(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
        popularity = cv.getAsDouble(MoviesContract.MoviesEntry.COLUMN_POPULARITY);
        title = cv.getAsString(MoviesContract.MoviesEntry.COLUMN_TITLE);
        video = cv.getAsBoolean(MoviesContract.MoviesEntry.COLUMN_VIDEO);
        vote_average = cv.getAsDouble(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE);
        vote_count = cv.getAsInteger(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT);
        if (cv.containsKey(MoviesContract.MoviesEntry.COLUMN_FAVORITE) &&
                cv.get(MoviesContract.MoviesEntry.COLUMN_FAVORITE)!=null) {
            favorite = new Boolean(cv.getAsInteger(MoviesContract.MoviesEntry.COLUMN_FAVORITE) == 1);
        } else {
            favorite = false;
        }
    }

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
        favorite = bundle.getBoolean("favorite");
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
//        favorite = false;
    }

    public void setFavorite(Boolean fav) {
        this.favorite = fav;
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

    public ContentValues getContentValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ADULT, this.adult);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, this.backdrop_path);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, this.id);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_LANGUAGE, this.original_language);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, this.original_title);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, this.overview);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_DATE, this.release_date);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, this.poster_path);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, this.popularity);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, this.title);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VIDEO, this.video);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, this.vote_average);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, this.vote_count);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_FAVORITE, this.favorite);
        return movieValues;
    }
}
