package com.galamdring.android.popularmovies.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    void insert(List<Review> reviews);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Review... reviews);

    @Delete
    void delete(Review... reviews);

    @Query("Select * from Review where movieId=:id")
    LiveData<List<Review>> getMovieReviews(final int id);

    @Query("Delete from Review")
    void deleteAll();
}
