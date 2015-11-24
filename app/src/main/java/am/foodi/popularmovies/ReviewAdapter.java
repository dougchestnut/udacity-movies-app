package am.foodi.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<Review> {

    Context mContext;
    public ReviewAdapter(Context context) {
        super(context, R.layout.grid_content);
        this.mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v==null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(R.layout.review_content, null);
        }

        Review review = getItem(position);

        TextView reviewView = (TextView)v.findViewById(R.id.review);
        reviewView.setText( review.content );

        return v;
    }


}