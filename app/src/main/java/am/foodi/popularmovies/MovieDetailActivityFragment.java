package am.foodi.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Movie Details");
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            Movie movie = (Movie) intent.getParcelableExtra("movie");
            ((TextView) rootView.findViewById(R.id.movie_title))
                    .setText( getString(movie.original_title) );
            ((TextView) rootView.findViewById(R.id.year))
                    .setText( movie.getYear() );
            ((TextView) rootView.findViewById(R.id.vote))
                    .setText(movie.vote_average+"/10");
            ((TextView) rootView.findViewById(R.id.overview))
                    .setText( getString(movie.overview) );
            ImageView imageView = (ImageView) rootView.findViewById(R.id.poster);
            Picasso.with(getActivity().getApplicationContext())
                    .load(movie.getPosterURL("w500"))
                    .into(imageView);
        }
        return rootView;
    }

    private String getString(String value) {
        if (value == null) {
            return "";
        }
        else {
            return value;
        }
    }
}
