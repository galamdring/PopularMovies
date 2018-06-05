package com.galamdring.android.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.mock.MockContentProvider;
import android.util.Log;

import java.util.List;

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    public static final int CODE_FAVORITE_WITH_ID = 201;
    public static final int CODE_FAVORITES = 200;

    public static final int CODE_REVIEW_WITH_ID=301;
    public static final int CODE_REVIEWS = 300;
    public static final int CODE_MOVIE_REVIEWS=302;

    public static final int CODE_TRAILER_WITH_ID = 401;
    public static final int CODE_TRAILERS = 400;
    public static final int CODE_MOVIE_TRAILERS =402;

    public static final UriMatcher URI_MATCHER = getUriMatcher();
    private MovieDbHelper dbHelper;

    private static UriMatcher getUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String contentAuth = MovieContract.CONTENT_AUTHORITY;

        //Uri to get all the movies.
        matcher.addURI(contentAuth, MovieContract.PATH_MOVIES, CODE_MOVIES);

        //Uri to get one movie by its id
        matcher.addURI(contentAuth, MovieContract.PATH_MOVIE+"/#", CODE_MOVIE_WITH_ID);

        //Uri to get all the favorites.
        matcher.addURI(contentAuth, MovieContract.PATH_FAVORITES, CODE_FAVORITES);
        //Uri to get one favorite by its id
        matcher.addURI(contentAuth, MovieContract.PATH_FAVORITE+"/#", CODE_FAVORITE_WITH_ID);

        //Uri to get all reviews
        matcher.addURI(contentAuth,MovieContract.PATH_REVIEWS, CODE_REVIEWS);
        //Uri to get review by its id
        matcher.addURI(contentAuth,MovieContract.PATH_REVIEW+"/#", CODE_REVIEW_WITH_ID);
        //Uri to get all reviews for movie id
        matcher.addURI(contentAuth,MovieContract.PATH_MOVIE+"/#/"+MovieContract.PATH_REVIEWS, CODE_MOVIE_REVIEWS);

        //Uri to get all trailers
        matcher.addURI(contentAuth, MovieContract.PATH_TRAILERS, CODE_TRAILERS);
        //Uri to get trailer by its id
        matcher.addURI(contentAuth,MovieContract.PATH_TRAILER+"/#", CODE_TRAILER_WITH_ID);

        matcher.addURI(contentAuth, MovieContract.PATH_MOVIE+"/#/"+MovieContract.PATH_TRAILERS, CODE_MOVIE_TRAILERS);

        return matcher;

    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsInserted = 0;
        switch(URI_MATCHER.match(uri)){
            case CODE_MOVIES:
                db.beginTransaction();

                try{
                    for(ContentValues value : values){
                        //Check if the movie already exists as a favorite.
                        if(checkFavorite(value.getAsInteger(MovieContract.MovieEntry._ID))==1) {
                            value.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 1);
                        }
                        else{
                            value.put(MovieContract.MovieEntry.COLUMN_FAVORITE,0);
                        }
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
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
            case CODE_TRAILERS:
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TRAILER_TABLE_NAME,null, value,SQLiteDatabase.CONFLICT_REPLACE);
                        if(_id!=-1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                if(rowsInserted>0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted;
            case CODE_REVIEWS:
                db.beginTransaction();
                try{
                    for(ContentValues value:values){
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.REVIEW_TABLE_NAME,null,value,SQLiteDatabase.CONFLICT_REPLACE);
                        if(_id!=-1) rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                if(rowsInserted>0){
                    getContext().getContentResolver().notifyChange(uri,null);
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

    String GetMovieIdFromReviewURI(Uri uri){
        List<String> segments = uri.getPathSegments();
        int count = segments.size();
        int target = count -2;
        return segments.get(target);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (URI_MATCHER.match(uri)){
            case CODE_MOVIE_TRAILERS:
                cursor=db.query(
                        MovieContract.MovieEntry.TRAILER_TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_TRAILER_MOVIE_ID + " = ? ",
                        new String[]{GetMovieIdFromReviewURI(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_MOVIE_REVIEWS:
                cursor=db.query(MovieContract.MovieEntry.REVIEW_TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_REVIEW_MOVIE_ID+" = ? ",
                        new String []{GetMovieIdFromReviewURI(uri)},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_WITH_ID:
                String[] selectArgs = {uri.getLastPathSegment()};
                cursor=db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID +" = ? ",
                        selectArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIES:
                cursor=db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_FAVORITES:
                cursor=db.query(
                        MovieContract.MovieEntry.FAVORITE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_FAVORITE_WITH_ID:
                cursor=db.query(
                        MovieContract.MovieEntry.FAVORITE_TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID +" = ?",
                        selectionArgs,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_REVIEWS:
                cursor = db.query(
                        MovieContract.MovieEntry.REVIEW_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_REVIEW_WITH_ID:
                cursor = db.query(
                        MovieContract.MovieEntry.REVIEW_TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_REVIEW_ID+" = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_TRAILERS:
                cursor = db.query(
                        MovieContract.MovieEntry.TRAILER_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_TRAILER_WITH_ID:
                cursor = db.query(
                        MovieContract.MovieEntry.REVIEW_TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_TRAILER_ID+" = ? ",
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
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch(URI_MATCHER.match(uri)) {
            case CODE_FAVORITE_WITH_ID:
                db.beginTransaction();
                String id = uri.getLastPathSegment();
                try{
                    //Get the full record from the movie data database and store it in favorites.
                    uri= MovieContract.MovieEntry.GetContentUriForFavorite(Integer.parseInt(id));
                    Cursor movie = query(MovieContract.MovieEntry.GetContentUriForMovie(Integer.parseInt(id)), MovieContract.MovieEntry.MovieSelectColumns, MovieContract.MovieEntry._ID+" = ? ",new String[]{id},null);
                    if(movie.moveToFirst()) {
                        //we have a record.
                        ContentValues favorite = new ContentValues();
                        DatabaseUtils.cursorRowToContentValues(movie, favorite);
                        db.insertWithOnConflict(MovieContract.MovieEntry.FAVORITE_TABLE_NAME,null,favorite,SQLiteDatabase.CONFLICT_REPLACE);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
                return uri;

            default:
                break;
        }
            return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;
        selection = " 1";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)){
            case CODE_MOVIES:
                numRowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_FAVORITE_WITH_ID:
                selection = MovieContract.MovieEntry._ID+" = ? ";
                String id = uri.getLastPathSegment();
                selectionArgs = new String[] {id};
                numRowsDeleted = db.delete(
                        MovieContract.MovieEntry.FAVORITE_TABLE_NAME,
                        selection,
                        selectionArgs
                );
                ContentValues removeFavoriteFlag = new ContentValues();
                removeFavoriteFlag.put(MovieContract.MovieEntry._ID,id);
                removeFavoriteFlag.put(MovieContract.MovieEntry.COLUMN_FAVORITE,0);
                db.update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        removeFavoriteFlag,
                        selection,
                        selectionArgs
                );
                break;
            case CODE_TRAILERS:
                numRowsDeleted=db.delete(MovieContract.MovieEntry.TRAILER_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_REVIEWS:
                numRowsDeleted=db.delete(MovieContract.MovieEntry.REVIEW_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: "+ uri);
        }
        return numRowsDeleted;
    }

    public int checkFavorite(int id){
        Cursor c = null;
        String query = "select count(*) from "+ MovieContract.MovieEntry.FAVORITE_TABLE_NAME+" where "+ MovieContract.MovieEntry._ID+" = ?";
        try{
            c=dbHelper.getReadableDatabase().rawQuery(query,new String[]{Integer.toString(id)});
            if(c.moveToFirst()){
                //this will return the first column of the query, which is whether this movie was saved as a favorite.
                return c.getInt(0);
            }
            return 0;
        }
        finally
        {
            if(c!=null){
                c.close();
            }
        }
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch(URI_MATCHER.match(uri)) {
            case CODE_FAVORITE_WITH_ID:
                db.beginTransaction();
                try {
                    int count = dbHelper.getWritableDatabase().update(
                            MovieContract.MovieEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs
                    );
                    Log.d("MovieProviderUpdate","Updated "+count+" rows.");

                    db.setTransactionSuccessful();
                    return count;
                } finally {
                    db.endTransaction();
                    db.close();
                }


            default:
                break;
        }
        return 0;
    }

    @Override
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
