package am.foodi.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class VideoAdapter extends ArrayAdapter<Video> {

    Context mContext;
    public VideoAdapter(Context context) {
        super(context, R.layout.grid_content);
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v==null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(R.layout.video_content, null);
        }

        final Video video = getItem(position);

        TextView titleView = (TextView)v.findViewById(R.id.trailer_title);
        if (video.name != null) {
            titleView.setText( video.name );
        } else {
            titleView.setText("Trailer #"+position+1);
        }

        ImageView imageView = (ImageView)v.findViewById(R.id.thumb);
        String url = "http://img.youtube.com/vi/"+video.key+"/0.jpg";

        Picasso.with(mContext)
                .load(url)
                .resize(160,90)
                .into(imageView);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = video.key;
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+id));
                    mContext.startActivity(intent);
                }

            }
        });

        return v;
    }


}