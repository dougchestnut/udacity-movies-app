package am.foodi.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import am.foodi.popularmovies.data.MoviesContract;

public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private static final int MOVIES_LOADER = 0;
    private MoviesAdapter mMovieAdpater;

    private GridView gridView;

    private int sort_index;
    private int selected_movie_index = 0;


    private Context mContext;

    public interface Callback {
        public void onItemSelected(Uri movieUri);
        public void onLoaded(Uri firstMovieUri);
    }

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getActivity().getApplicationContext();

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
        selectSort.show(getFragmentManager(), "sort");

        return super.onOptionsItemSelected(item);
    }

    public void updateSort(int index) {
        Log.v(LOG_TAG, "sort passed to fragment: " + sort_index);
        sort_index = index;
        selected_movie_index = 0;
        if (sort_index < 2) {
            fetchMovies();
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        } else {
            Log.v(LOG_TAG, "list the users favorite movies");
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            selected_movie_index = savedInstanceState.getInt("selected_movie_index");
        }

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mMovieAdpater = new MoviesAdapter(getActivity(),null,0);

        gridView = (GridView)  rootView.findViewById( R.id.gridview_movies );
        gridView.setAdapter(mMovieAdpater);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    selected_movie_index = position;
                    ContentValues cv = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, cv);
                    ((Callback) getActivity())
                            .onItemSelected(MoviesContract.MoviesEntry.buildMovieUri(cv.getAsInteger(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
                }
            }
        });

        fetchMovies();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt("sort_index", sort_index);
        outState.putInt("selected_movie_index", selected_movie_index);
        super.onSaveInstanceState(outState);
    }

    public void fetchMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(mContext, sort_index);
        moviesTask.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "In MovieListFragments onCreateLoader");
        String sortOrder = MoviesContract.MoviesEntry.COLUMN_POPULARITY + " DESC";
        if (sort_index == 1) {
            sortOrder = MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " DESC";
        }
        String selection=null;
        String[] selectionArgs=null;
        if (sort_index == 2) {
            selection = MoviesContract.MoviesEntry.COLUMN_FAVORITE + " = ? ";
            selectionArgs = new String[]{"1"};
        }
        return new CursorLoader(getActivity(),
                MoviesContract.MoviesEntry.CONTENT_URI,
                null, selection, selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMovieAdpater.swapCursor(cursor);

        if (selected_movie_index >= gridView.getCount()) {
            selected_movie_index = 0;
        }
        if ( gridView.getCount() > 0 ) {
            Cursor cur = (Cursor) gridView.getItemAtPosition(selected_movie_index);
            if (cursor != null) {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                ((Callback) getActivity())
                        .onLoaded(MoviesContract.MoviesEntry.buildMovieUri(cv.getAsInteger(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) { mMovieAdpater.swapCursor(null);}
}
