package am.foodi.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import am.foodi.popularmovies.data.MoviesContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment implements FetchMovieReviewsTask.OnReviewsFetchCompleted, FetchMovieVideosTask.OnVideosFetchCompleted, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private Context mContext;
    private View rootView;
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private ArrayAdapter<Video> mVideosAdapter;
    private ArrayAdapter<Review> mReviewsAdapter;
    FetchMovieVideosTask videosTask;
    FetchMovieReviewsTask reviewsTask;


    public MovieDetailFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        getActivity().setTitle("Movie Details");

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
        }

        mReviewsAdapter = new ReviewAdapter(getActivity());
        mVideosAdapter = new VideoAdapter(getActivity());

        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        rootView.setTag(viewHolder);

        return rootView;
    }

    private void updateUI(final Movie movie) {
        ViewHolder viewHolder = (ViewHolder) rootView.getTag();

        viewHolder.titleView.setText(getString(movie.original_title));
        viewHolder.yearView.setText( movie.getYear() );
        viewHolder.voteView.setText(movie.vote_average + "/10");
        viewHolder.overviewView.setText(getString(movie.overview));
        Picasso.with(getActivity().getApplicationContext())
                    .load(movie.getPosterURL("w500"))
                    .into(viewHolder.posterView);

        ToggleButton favoriteMovieButton = viewHolder.favoriteButtonView;
        if (movie.favorite) { favoriteMovieButton.setChecked(movie.favorite); }
        favoriteMovieButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                movie.setFavorite(isChecked);
                mContext.getContentResolver().update(MoviesContract.MoviesEntry.CONTENT_URI,
                        movie.getContentValues(),
                        MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{String.valueOf(movie.id)});
            }
        });
    }

    private String getString(String value) {
        if (value == null) {
            return "";
        }
        else {
            return value;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri ) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null, null, null, null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {return;}
        ContentValues cv = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(data, cv);
        Movie movie = new Movie(cv);
        updateUI(movie);
        videosTask = new FetchMovieVideosTask(mContext, this, movie.id);
        videosTask.execute();
        reviewsTask = new FetchMovieReviewsTask(mContext, this, movie.id);
        reviewsTask.execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    public static class ViewHolder {
        public final TextView titleView;
        public final TextView yearView;
        public final TextView voteView;
        public final TextView overviewView;
        public final ImageView posterView;
        public final ToggleButton favoriteButtonView;

        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.movie_title);
            yearView = (TextView) view.findViewById(R.id.year);
            voteView = (TextView) view.findViewById(R.id.vote);
            overviewView = (TextView) view.findViewById(R.id.overview);
            posterView = (ImageView) view.findViewById(R.id.poster);
            favoriteButtonView = (ToggleButton) view.findViewById(R.id.toggleFavoriteButton);
        }
    }

    public void onVideosFetchCompleted(Video[] result) {
        if (result != null) {
            Log.v(LOG_TAG, "Fetched " + result.length + " videos!");
            mVideosAdapter.clear();
            for (Video video: result) {
                mVideosAdapter.add(video);
                Log.v(LOG_TAG, "movie: " + video.name);
            }
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.videos);
            if (mVideosAdapter.getCount() == 0) {
                TextView mheader = (TextView) rootView.findViewById(R.id.trailer_header);
                layout.removeView(mheader);
            }
            for (int i=0; i<mVideosAdapter.getCount(); i++) {
                View view = mVideosAdapter.getView(i,null,null);
                layout.addView(view);
            }
        }
    }

    public void onReviewsFetchCompleted(Review[] result) {
        if (result != null) {
            Log.v(LOG_TAG, "Fetched " + result.length + " reviews!");
            mReviewsAdapter.clear();
            for (Review review: result) {
                mReviewsAdapter.add(review);
            }
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.reviews);
            if (mReviewsAdapter.getCount() == 0) {
                TextView mheader = (TextView) rootView.findViewById(R.id.reviews_header);
                layout.removeView(mheader);
            }
            for (int i=0; i<mReviewsAdapter.getCount(); i++) {
                View view = mReviewsAdapter.getView(i,null,null);
                layout.addView(view);
            }
        }
    }
}
