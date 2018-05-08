package com.galamdring.android.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.galamdring.android.popularmovies.Data.MovieContract;
import com.galamdring.android.popularmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>{
    private ActivityDetailBinding detailBinding;
    private Uri movieDataUri;
    private final int loaderID = 9876;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        movieDataUri = getIntent().getData();
        if(movieDataUri == null)
            Toast.makeText(this, "Couldn't get data. Please try again.", Toast.LENGTH_SHORT).show();

        getSupportLoaderManager().initLoader(loaderID,null,this);
        setTitle("");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case loaderID:
                return new CursorLoader(this, movieDataUri,
                        MovieContract.MovieEntry.DetailActivitySelectColumns,
                        null, null, null);
            default:
                throw new RuntimeException("Again, not the right droids. (Loader not implemented.)");
        }
    }



    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //We didn't get any data, just return, and the display will be empty.
        // Maybe we should tell the user something?
        if(data == null || !(data.moveToFirst())){
            Toast.makeText(this, "No data for this movie. Please contact the developer.", Toast.LENGTH_SHORT).show();
            return;
        }

        Picasso.with(this).load(data.getString(MovieContract.MovieEntry.INDEX_COLUMN_POSTER_URL))
                .into(detailBinding.ivPoster);
        setTitle(data.getString(MovieContract.MovieEntry.INDEX_COLUMN_TITLE));
        detailBinding.tvRating.setText(data.getString(MovieContract.MovieEntry.INDEX_COLUMN_VOTE_AVERAGE));
        detailBinding.tvRelDate.setText(data.getString(MovieContract.MovieEntry.INDEX_COLUMN_RELEASE_DATE));
        final FrameLayout layout = findViewById(R.id.detailbackground);
        detailBinding.tvOverview.setText(data.getString(MovieContract.MovieEntry.INDEX_COLUMN_OVERVIEW));


        Picasso.with(this).load(data.getString(MovieContract.MovieEntry.INDEX_COLUMN_BACKDROP_URL)).into(new Target(){
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BitmapDrawable map = new BitmapDrawable(getResources(), GetResizedBitmapForBackground(bitmap));

                map.setGravity(Gravity.CENTER);
                map.setAlpha(50);
                layout.setBackground(map);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    private Bitmap GetResizedBitmapForBackground(Bitmap map){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int targetWidth = metrics.heightPixels;
        int targetHeight = metrics.widthPixels;

        int width = map.getWidth();
        int height = map.getHeight();
        float ratio = 0;
        float widthratio = (float)targetWidth/(float)width;
        float heightratio=(float)targetHeight/(float)height;
        if(widthratio>heightratio){
            ratio=widthratio;
        }
        else{
            ratio=heightratio;
        }
        if(ratio==0){
            Log.d("BitmapResize","Got ratio of 0, returning original. targetWidth: "+targetWidth+" targetHeight: "+targetHeight);
            return map;
        }
        int dstWidth=(int)(width*ratio);
        int dstHeight =(int) (height*ratio);
        Log.d("BitmapResize","Using ratio of "+ratio+" dstHeight: "+dstHeight+" dstWidth: "+dstWidth);
        return Bitmap.createScaledBitmap(map,dstWidth,dstHeight ,false);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }

}
