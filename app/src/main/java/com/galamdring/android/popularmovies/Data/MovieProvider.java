package com.galamdring.android.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    public static final UriMatcher URI_MATCHER = getUriMatcher();
    private MovieDbHelper dbHelper;

    private static UriMatcher getUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String contentAuth = MovieContract.CONTENT_AUTHORITY;

        //Uri to get all the movies.
        matcher.addURI(contentAuth, MovieContract.PATH_MOVIES, CODE_MOVIES);

        //Uri to get one movie by its id
        matcher.addURI(contentAuth, MovieContract.PATH_MOVIE+"/#", CODE_MOVIE_WITH_ID);

        return matcher;

    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch(URI_MATCHER.match(uri)){
            case CODE_MOVIES:
                db.beginTransaction();
                int rowsInserted = 0;
                try{
                    for(ContentValues value : values){
                        long _id  = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                if(rowsInserted>0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (URI_MATCHER.match(uri)){
            case CODE_MOVIE_WITH_ID:
                String[] selectArgs = {uri.getLastPathSegment()};
                cursor=dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID +" = ? ",
                        selectArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIES:
                cursor=dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: "+ uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;
        if(null==selection) selection ="1";
        switch (URI_MATCHER.match(uri)){
            case CODE_MOVIES:
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: "+ uri);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
