package com.galamdring.android.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int Version = 5;
    private static final String DATABASE_NAME = "movies.db";


    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder SQL_CREATE_String = new StringBuilder();
        SQL_CREATE_String.append("Create Table ")
                .append(MovieContract.MovieEntry.TABLE_NAME)
                .append(" ( ")
                .append(MovieContract.MovieEntry._ID).append(" INTEGER PRIMARY KEY, ")
                .append(MovieContract.MovieEntry.COLUMN_BACKDROP_URL).append( " TEXT, " )
                .append(MovieContract.MovieEntry.COLUMN_GENRE_IDS ).append( " TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_OVERVIEW).append( " TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_POPULARITY).append(" REAL, ")
                .append(MovieContract.MovieEntry.COLUMN_POSTER_URL).append( " TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_RELEASE_DATE).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_TITLE).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE).append(" INTEGER, ")
                .append(MovieContract.MovieEntry.COLUMN_VOTE_COUNT).append( " INTEGER, ")
                .append(MovieContract.MovieEntry.COLUMN_FAVORITE).append( " INTEGER ")
                .append(");");

        db.execSQL(SQL_CREATE_String.toString());

        SQL_CREATE_String = new StringBuilder().append("Create Table ")
                .append(MovieContract.MovieEntry.FAVORITE_TABLE_NAME)
                .append(" ( ")
                .append(MovieContract.MovieEntry._ID).append(" INTEGER PRIMARY KEY, ")
                .append(MovieContract.MovieEntry.COLUMN_BACKDROP_URL).append( " TEXT, " )
                .append(MovieContract.MovieEntry.COLUMN_GENRE_IDS ).append( " TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_OVERVIEW).append( " TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_POPULARITY).append(" REAL, ")
                .append(MovieContract.MovieEntry.COLUMN_POSTER_URL).append( " TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_RELEASE_DATE).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_TITLE).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE).append(" INTEGER, ")
                .append(MovieContract.MovieEntry.COLUMN_VOTE_COUNT).append( " INTEGER, ")
                .append(MovieContract.MovieEntry.COLUMN_FAVORITE).append( " INTEGER ")
                .append(");");

        db.execSQL(SQL_CREATE_String.toString());

        SQL_CREATE_String=new StringBuilder().append("Create Table ")
                .append(MovieContract.MovieEntry.REVIEW_TABLE_NAME).append(" ( ")
                .append(MovieContract.MovieEntry.COLUMN_REVIEW_ID).append(" TEXT PRIMARY KEY, ")
                .append(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT).append(" TEXT, ")
                .append(MovieContract.MovieEntry.COLUMN_REVIEW_MOVIE_ID).append(" INTEGER NOT NULL, ")
                .append(MovieContract.MovieEntry.COLUMN_REVIEW_URL).append(" TEXT ")
                .append(");");

        db.execSQL(SQL_CREATE_String.toString());

        SQL_CREATE_String = new StringBuilder().append("Create Table ")
                .append(MovieContract.MovieEntry.TRAILER_TABLE_NAME).append(" ( ")
                .append(MovieContract.MovieEntry.COLUMN_TRAILER_ID).append(" TEXT PRIMARY KEY, ")
                .append(MovieContract.MovieEntry.COLUMN_TRAILER_KEY).append(" TEXT NOT NULL, ")
                .append(MovieContract.MovieEntry.COLUMN_TRAILER_MOVIE_ID).append(" INTEGER NOT NULL, ")
                .append(MovieContract.MovieEntry.COLUMN_TRAILER_NAME).append(" TEXT NOT NULL, ")
                .append(MovieContract.MovieEntry.COLUMN_TRAILER_SITE).append(" TEXT NOT NULL, ")
                .append(MovieContract.MovieEntry.COLUMN_TRAILER_TYPE).append(" TEXT ")
                .append(");");

        db.execSQL(SQL_CREATE_String.toString());
    }

    //We don't care about the data, just ditch it and recreate the table.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.FAVORITE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.REVIEW_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TRAILER_TABLE_NAME);
        onCreate(db);

    }
}
