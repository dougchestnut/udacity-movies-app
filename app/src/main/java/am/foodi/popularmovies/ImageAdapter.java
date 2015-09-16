package am.foodi.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends ArrayAdapter<Movie> {

    Context mContext;
    public ImageAdapter(Context context) {
        super(context, R.layout.grid_content);
        this.mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v==null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(R.layout.grid_content, null);
        }

        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        String url = getItem(position).getPosterURL();

        Picasso.with(mContext)
                .load(url)
                .resize(185,277)
                .into(imageView);

        return v;
    }

}