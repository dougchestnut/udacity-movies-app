package am.foodi.popularmovies;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class MoviePosterImageView extends ImageView {
    public MoviePosterImageView(Context context) {
        super(context);
    }

    public MoviePosterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoviePosterImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        double height = getMeasuredWidth()*1.5;
        setMeasuredDimension(getMeasuredWidth(), (int)height ); //Snap to width
    }
}