package am.foodi.popularmovies;

        import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * {@link MoviesAdapter} exposes a list of movies
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class MoviesAdapter extends CursorAdapter {
    public MoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_content, parent, false);

        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        ContentValues cv = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, cv);
        Movie movie = new Movie(cv);

        View v = view;
        if (v==null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(R.layout.grid_content, null);
        }

        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        String url =  movie.getPosterURL();

        Picasso.with(context)
                .load(url)
                .resize(185,277)
                .into(imageView);

    }
}
