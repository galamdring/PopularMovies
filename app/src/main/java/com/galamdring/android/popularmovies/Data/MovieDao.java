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
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Insert
    void insert(List<Movie> movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Movie... movies);

    @Delete
    void delete(Movie... movies);

    @Query("Select * from movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("Select * from Movie Where favorite=1")
    LiveData<List<Movie>> getAllFavorites();

    @Query("Select * from Movie where _id=:id")
    LiveData<Movie> getMovie(final int id);

    @Query("Delete From Movie")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(Movie movie);
}
