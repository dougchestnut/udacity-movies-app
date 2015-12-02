package am.foodi.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback, SelectSortFragment.SelectSortListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    public boolean mTwoPane;

    @Override
    public void onDialogSortSelect(int sort_index) {
        MovieListFragment frag = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
        frag.updateSort(sort_index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_URI, contentUri);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class).setData(contentUri);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaded(Uri contentUri) {
        if (mTwoPane && contentUri != null) {

            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_URI, contentUri);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commitAllowingStateLoss();

        }
    }

}
