package am.foodi.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SelectSortFragment.SelectSortListener {

    MovieListFragment frag;

    @Override
    public void onDialogSortSelect(int sort_index) {
        MovieListFragment frag = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        frag.updateSort(sort_index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
