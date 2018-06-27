package com.galamdring.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.galamdring.android.popularmovies.Data.Movie;
import com.galamdring.android.popularmovies.Data.MovieContract;
import com.galamdring.android.popularmovies.Data.MovieProvider;
import com.galamdring.android.popularmovies.Data.MovieViewModel;
import com.galamdring.android.popularmovies.Data.OurExecutors;
import com.galamdring.android.popularmovies.Data.Review;
import com.galamdring.android.popularmovies.Sync.MoviesApi;
import com.galamdring.android.popularmovies.Utils.BitmapUtils;
import com.galamdring.android.popularmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding detailBinding;
    private ReviewsAdapter reviewsAdapter;
    private TrailerAdapter trailerAdapter;
    private Movie TheMovie;
    private Context context =this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        TheMovie = getIntent().getParcelableExtra("Movie");



        detailBinding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter(this);
        detailBinding.rvReviews.setAdapter(reviewsAdapter);

        detailBinding.rvTrailers.setLayoutManager(new GridLayoutManager(this, 2));
        trailerAdapter = new TrailerAdapter(this);
        detailBinding.rvTrailers.setAdapter(trailerAdapter);

        if (TheMovie == null)
            Toast.makeText(this, "Couldn't get data. Please try again.", Toast.LENGTH_SHORT).show();
        PopulateUI(TheMovie);




    }

    private void PopulateUI(@NonNull final Movie movie) {

        Picasso.with(this).load(movie.getPosterUrl())
                .into(detailBinding.ivPoster);
        setTitle(movie.getTitle());
        detailBinding.tvRating.setText(String.format("%d",movie.getVoteAverage()));
        detailBinding.tvRelDate.setText(movie.getReleaseDate());
        final FrameLayout layout = findViewById(R.id.detailbackground);
        detailBinding.tvOverview.setText(movie.getOverview());
        trailerAdapter.setData(movie.getTrailerIds());
        reviewsAdapter.setData(movie.getReviews());

        detailBinding.ivDetailFavorite.setActivated(movie.isFavorite());
        detailBinding.ivDetailFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailBinding.ivDetailFavorite.isActivated()){
                    OurExecutors.getINSTANCE().getDbIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            MoviesApi.removeFavorite(context,TheMovie);
                            TheMovie.setFavorite(false);
                        }
                    });
                }
                else {
                    OurExecutors.getINSTANCE().getDbIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            MoviesApi.addFavorite(context,TheMovie);
                            TheMovie.setFavorite(true);
                        }
                    });
                }
                detailBinding.ivDetailFavorite.setActivated(!detailBinding.ivDetailFavorite.isActivated());

            }
        });


        Picasso.with(this).load(movie.getBackdropUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                BitmapDrawable map = new BitmapDrawable(getResources(), BitmapUtils.GetResizedBitmapForBackground(metrics, bitmap));

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

}
