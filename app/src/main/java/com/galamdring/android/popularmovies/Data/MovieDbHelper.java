package com.galamdring.android.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int Version = 2;
    private static final String DATABASE_NAME = "movies.db";


    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_String =
                "Create Table " + MovieContract.MovieEntry.TABLE_NAME+" ( " +
                        MovieContract.MovieEntry._ID +" INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_BACKDROP_URL+ " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_GENRE_IDS + " TEXT, "+
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG+" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE+" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_OVERVIEW+ " TEXT, "+
                        MovieContract.MovieEntry.COLUMN_POPULARITY+" REAL, "+
                        MovieContract.MovieEntry.COLUMN_POSTER_URL+ " TEXT, "+
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE+" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_TITLE+" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE+" INTEGER, "+
                        MovieContract.MovieEntry.COLUMN_VOTE_COUNT+ " INTEGER "+
                        ");";

        db.execSQL(SQL_CREATE_String);

    }

    //We don't care about the data, just ditch it and recreate the table.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
