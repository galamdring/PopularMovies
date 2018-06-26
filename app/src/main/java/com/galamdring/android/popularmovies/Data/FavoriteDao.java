package com.galamdring.android.popularmovies.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Insert
    void insertWithReviews(Favorite favorite, List<FavoriteReview> reviews);

    @Query("Select * from Favorite")
    LiveData<List<Movie>> getFavorites();

    @Query("Select * from FavoriteReview where movieId=:id")
    LiveData<List<FavoriteReview>> getFavoriteReviews(int id);

    @Delete
    void delete(List<FavoriteReview> favoriteReviews);

    @Query("Select * from Favorite where _id=:id")
    Favorite getFavorite(int id);
}
