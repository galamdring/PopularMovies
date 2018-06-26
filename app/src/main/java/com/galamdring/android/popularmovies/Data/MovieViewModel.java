package com.galamdring.android.popularmovies.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieDatabase DATABASE;
    private LiveData<List<Movie>> movieList;
    private LiveData<List<Movie>> favoriteList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        DATABASE=MovieDatabase.getInstance(application.getApplicationContext());
        movieList = DATABASE.movieDao().getAllMovies();
        favoriteList = DATABASE.favoriteDao().getFavorites();
    }

    public LiveData<List<Movie>> getMovieList(){
        return movieList;
    }

    public LiveData<Movie> getMovieById(int id)
    {
        return DATABASE.movieDao().getMovie(id);
    }

    public LiveData<List<Review>> getReviewsForMovie(int id){
        return DATABASE.reviewDao().getMovieReviews(id);
    }

    public LiveData<List<Movie>> getFavorites(){
        return favoriteList;
    }

    public LiveData<List<FavoriteReview>> getFavoriteReviews(int id){
        return DATABASE.favoriteDao().getFavoriteReviews(id);
    }

}
