package com.galamdring.android.popularmovies.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {Movie.class, Review.class, Favorite.class, FavoriteReview.class},version=2, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase INSTANCE;
    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritemovies";

    public abstract MovieDao movieDao();
    public abstract ReviewDao reviewDao();
    public abstract FavoriteDao favoriteDao();

    public static MovieDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating new Favorite Movies database instance");
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class,
                        MovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG,"Getting the database instance");
        return INSTANCE;
    }
}
