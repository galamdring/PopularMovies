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
        Log.d("MoviesApi",uri.toString());
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
        }
        catch(JSONException ex) {
            Toast.makeText(context, "Failed to parse TMDB api response. Please contact the developer.", Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}
