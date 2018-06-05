package com.galamdring.android.popularmovies.Sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.galamdring.android.popularmovies.Data.Genres;
import com.galamdring.android.popularmovies.Data.MovieContract;
import com.galamdring.android.popularmovies.R;
import com.galamdring.android.popularmovies.Utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoviesApi {
    static String API_KEY;
    static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    static final String REVIEW_URL = "reviews";
    static final String TRAILER_URL="videos";

    synchronized public static void addFavorite(Context context, int id){
        Toast.makeText(context, "Adding favorite flag to movie: "+id, Toast.LENGTH_SHORT).show();
        ContentResolver movieResolver = context.getContentResolver();
        ContentValues movie = new ContentValues();
        movie.put(MovieContract.MovieEntry._ID,id);
        movie.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 1);
        Uri favoriteUri = MovieContract.MovieEntry.GetContentUriForFavorite(id);
        String selection = MovieContract.MovieEntry._ID+" = ? ";
        String[] selectionArgs = new String[]{Integer.toString(id)};
        movieResolver.update(favoriteUri,movie,selection,selectionArgs);
        movieResolver.insert(favoriteUri,movie);
    }

    public static void removeFavorite(Context context, int id) {
        Toast.makeText(context, "Removing favorite flag from movie: "+id, Toast.LENGTH_SHORT).show();
        ContentResolver movieResolver = context.getContentResolver();
        ContentValues movie = new ContentValues();
        movie.put(MovieContract.MovieEntry._ID,id);
        movie.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 0);
        Uri favoriteUri = MovieContract.MovieEntry.GetContentUriForFavorite(id);
        String selection = MovieContract.MovieEntry._ID+" = ? ";
        String[] selectionArgs = new String[]{Integer.toString(id)};
        movieResolver.update(favoriteUri,movie,selection,selectionArgs);
        movieResolver.delete(favoriteUri,selection,selectionArgs);
    }

    synchronized public static void syncMovies(Context context, String sortType){
        try {
            ContentValues[] movieValues = getMovieContextValues(context, sortType);
            if (movieValues != null && movieValues.length != 0) {
                ContentResolver movieResolver = context.getContentResolver();
                //Delete all the other data, we are starting fresh.
                movieResolver.delete(MovieContract.MovieEntry.MOVIES_CONTENT_URI, null, null);
                //bulkinsert all the new data. This will be the first 20 records.
                movieResolver.bulkInsert(MovieContract.MovieEntry.MOVIES_CONTENT_URI, movieValues);

            }
        }
        catch(Exception e){
            Log.e("syncMovies","Failed to load movies from tmdb",e);
        }
    }
    public static ContentValues[] getMovieContextValues(Context context, String sortType){

        API_KEY=context.getResources().getString(R.string.apiKey);
        if(sortType==null) sortType = "popular";
        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(sortType)
                .appendQueryParameter("api_key",API_KEY)
                .build();
//        Log.d("MoviesApi",uri.toString());
        try {
            JSONObject response = JSONUtils.getJSONObjectFromUrl(uri.toString());
            Log.d("MoviesApi", response.toString());
            JSONArray results = response.getJSONArray("results");
            Log.d("MoviesApi", "Got " + results.length() + " results.");
            ContentValues[] movieContentValues = new ContentValues[results.length()];
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String posterPath = result.getString("poster_path");
                String posterUrl = "http://image.tmdb.org/t/p/w185" + posterPath;
                String orig_title = result.getString("original_title");
                int id = result.getInt("id");
                String backdropPath = result.getString("backdrop_path");
                String backdropUrl = "http://image.tmdb.org/t/p/w500"+backdropPath;
                JSONArray genreIds = result.getJSONArray("genre_ids");
                StringBuilder genreSB = new StringBuilder();
                for(int genreCounter =0; genreCounter<genreIds.length(); genreCounter++){
                    genreSB.append(Genres.GetGenre(genreIds.optInt(genreCounter)));
                    if(genreCounter<genreIds.length()-1){
                        genreSB.append(", ");
                    }
                }
                String originalLang = result.getString("original_language");
                String overview = result.getString("overview");
                double popularity = result.getDouble("popularity");
                String relDate = result.getString("release_date");
                int voteAvg = result.getInt("vote_average");
                int voteCount = result.getInt("vote_count");
                String title = result.getString("title");

                PopulateReviews(context,id);
                PopulateTrailers(context,id);


                ContentValues movieContentValue = new ContentValues();
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, backdropUrl);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, genreSB.toString());
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG, originalLang);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, orig_title);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, posterUrl);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, relDate);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAvg);
                movieContentValue.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, voteCount);
                movieContentValue.put(MovieContract.MovieEntry._ID, id);
                movieContentValues[i] = movieContentValue;
            }
            return movieContentValues;
        }
        catch(IOException ex){
            Toast.makeText(context, "Looks like we can't connect. Is your network on?", Toast.LENGTH_SHORT).show();
            Log.d("MoviesApi","Failed loading movie data.",ex);
        }
        catch(JSONException ex) {
            Toast.makeText(context, "Failed to parse TMDB api response. Please contact the developer.", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public static void PopulateTrailers(Context context,int MovieID) {
        Uri trailerUri = Uri.parse(BASE_URL).buildUpon().appendPath(Integer.toString(MovieID)).appendPath(TRAILER_URL).appendQueryParameter("api_key",API_KEY).build();
        try {
            JSONObject trailerJSON = JSONUtils.getJSONObjectFromUrl(trailerUri.toString());

            JSONArray results = trailerJSON.getJSONArray("results");
            ContentValues[] data = new ContentValues[results.length()];
            for(int i=0;i<results.length(); i++) {
                ContentValues trailerValues = new ContentValues();
                JSONObject result = results.getJSONObject(i);
                String name = result.getString("name");
                String key = result.getString("key");
                String site = result.getString("site");
                String type = result.getString("type");
                String trailer_id = result.getString("id");
                trailerValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_NAME, name);
                trailerValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_KEY, key);
                trailerValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_SITE, site);
                trailerValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_TYPE, type);
                trailerValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_MOVIE_ID, MovieID);
                trailerValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_ID, trailer_id);

                data[i]=trailerValues;
            }

            if(data.length>0){
                ContentResolver movieResolver = context.getContentResolver();
                //movieResolver.delete(MovieContract.MovieEntry.TRAILERS_CONTENT_URI, null, null);
                //bulkinsert all the new data. This will be the first 20 records.
                movieResolver.bulkInsert(MovieContract.MovieEntry.TRAILERS_CONTENT_URI, data);

            }
        }
        catch(Exception ex){
            Log.e("PopulateTrailers","Failed to get JSON for trailers.",ex);
        }

    }

    public static void PopulateReviews(Context context, int MovieID){
        /*
        {"id":337167,"page":1,"results":[
        {"author":"ehabsalah",
        "content":"Nice Movie ♥",
        "id":"5ace51a9c3a36834de09f933",
        "url":"https://www.themoviedb.org/review/5ace51a9c3a36834de09f933"}],"total_pages":1,"total_results":1}
         */
        Uri reviewUri = Uri.parse(BASE_URL).buildUpon().appendPath(Integer.toString(MovieID)).appendPath(REVIEW_URL).appendQueryParameter("api_key",API_KEY).build();
        try {
            JSONObject reviewJSON = JSONUtils.getJSONObjectFromUrl(reviewUri.toString());

            JSONArray results = reviewJSON.getJSONArray("results");
            ContentValues[] data = new ContentValues[results.length()];
            for(int i=0;i<results.length(); i++) {
                ContentValues reviewValues = new ContentValues();
                JSONObject result = results.getJSONObject(i);
                String author = result.getString("author");
                String content = result.getString("content");
                String url = result.getString("url");
                String review_id = result.getString("id");
                reviewValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_MOVIE_ID, MovieID);
                reviewValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR, author);
                reviewValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT, content);
                reviewValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_URL, url);
                reviewValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_ID, review_id);



                data[i]=reviewValues;
            }

            if(data.length>0){
                ContentResolver movieResolver = context.getContentResolver();
                //movieResolver.delete(MovieContract.MovieEntry.REVIEWS_CONTENT_URI, null, null);
                //bulkinsert all the new data. This will be the first 20 records.
                movieResolver.bulkInsert(MovieContract.MovieEntry.REVIEWS_CONTENT_URI, data);

            }
        }
        catch(Exception ex){
            Log.e("PopulateTrailers","Failed to get JSON for trailers.",ex);
        }
    }

}
