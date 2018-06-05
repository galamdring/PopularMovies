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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.galamdring.android.popularmovies.Data.MovieContract;
import com.galamdring.android.popularmovies.Data.MovieProvider;
import com.galamdring.android.popularmovies.Utils.BitmapUtils;
import com.galamdring.android.popularmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>{
    private ActivityDetailBinding detailBinding;
    private Uri movieDataUri;
    private final int loaderID = 9876;
    private final int ReviewLoaderId = 123;
    private final int TrailerLoaderId = 456;
    private ReviewsAdapter reviewsAdapter;
    private TrailerAdapter trailerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        movieDataUri = getIntent().getData();
        detailBinding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter(this);
        detailBinding.rvReviews.setAdapter(reviewsAdapter);

        detailBinding.rvTrailers.setLayoutManager(new GridLayoutManager(this,2));
        trailerAdapter = new TrailerAdapter(this);
        detailBinding.rvTrailers.setAdapter(trailerAdapter);

        if(movieDataUri == null)
            Toast.makeText(this, "Couldn't get data. Please try again.", Toast.LENGTH_SHORT).show();

        getSupportLoaderManager().initLoader(loaderID,null,this);
        getSupportLoaderManager().initLoader(ReviewLoaderId,null,this);
        getSupportLoaderManager().initLoader(TrailerLoaderId,null,this);
        setTitle("");


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case loaderID:
                return new CursorLoader(this, movieDataUri,
                        MovieContract.MovieEntry.MovieSelectColumns,
                        null, null, null);
            case ReviewLoaderId:
                return new CursorLoader(this,
                        MovieContract.MovieEntry.GetContentUriForMovieReviews(movieDataUri),
                        MovieContract.MovieEntry.REVIEW_SELECT_COLUMNS,null,
                        null,null);
            case TrailerLoaderId:
                return new CursorLoader(this,
                        MovieContract.MovieEntry.GetContentUriForMovieTrailers(movieDataUri),
                        MovieContract.MovieEntry.TRAILER_SELECT_COLUMNS,
                        null,null,null);
            default:
                throw new RuntimeException("Again, not the right droids. (Loader not implemented.)");
        }
    }



    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //We didn't get any data, just return, and the display will be empty.
        // Maybe we should tell the user something?
        switch(loader.getId()) {
            case loaderID:
                if (data == null || !(data.moveToFirst())) {
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


                Picasso.with(this).load(data.getString(MovieContract.MovieEntry.INDEX_COLUMN_BACKDROP_URL)).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        BitmapDrawable map = new BitmapDrawable(getResources(), BitmapUtils.GetResizedBitmapForBackground(metrics,bitmap));

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
                break;
            case TrailerLoaderId:
                if (data == null || !(data.moveToFirst())) {
                    Toast.makeText(this, "No trailer data for this movie. Please contact the developer.", Toast.LENGTH_SHORT).show();
                    return;
                }
                trailerAdapter.setData(data);
                break;
            case ReviewLoaderId:
                if (data == null || !(data.moveToFirst())) {
                    Toast.makeText(this, "No review data for this movie. Please contact the developer.", Toast.LENGTH_SHORT).show();
                    return;
                }
                reviewsAdapter.setData(data);
                break;
            default:
                throw new UnsupportedOperationException("LoaderId not implemented.");
        }

    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }

}
