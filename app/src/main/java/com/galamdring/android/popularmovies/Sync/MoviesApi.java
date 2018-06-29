package com.galamdring.android.popularmovies.Sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.galamdring.android.popularmovies.Data.Favorite;
import com.galamdring.android.popularmovies.Data.FavoriteReview;
import com.galamdring.android.popularmovies.Data.Genres;
import com.galamdring.android.popularmovies.Data.Movie;
import com.galamdring.android.popularmovies.Data.MovieDao;
import com.galamdring.android.popularmovies.Data.OurExecutors;
import com.galamdring.android.popularmovies.Data.Review;
import com.galamdring.android.popularmovies.Data.MovieDatabase;
import com.galamdring.android.popularmovies.Data.ReviewDao;
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


    synchronized public static void addFavorite(Context context, Movie movie){
        if(movie!=null) {
            MovieDatabase db = MovieDatabase.getInstance(context);
            movie.setFavorite(true);
            db.movieDao().upsert(movie);
            movie.setReviews(db.reviewDao().getMovieReviews(movie.get_id()).getValue());
            Favorite favorite = new Favorite(movie);
            db.favoriteDao().insertWithReviews(favorite, FavoriteReview.FavoriteReviewListFromListReview(movie.getReviews()));
        }
        else{Log.d("addFavoriteMovieApi","Couldn't load movie.");}
    }

    public static void removeFavorite(Context context, Movie movie)
    {
        if(movie!=null) {
            MovieDatabase db = MovieDatabase.getInstance(context);
            //Toast.makeText(context, "Removing favorite flag from movie: "+movie.get_id(), Toast.LENGTH_SHORT).show();
            movie.setFavorite(false);
            db.movieDao().upsert(movie);
            Favorite favorite = db.favoriteDao().getFavorite(movie.get_id());
            List<FavoriteReview> reviews = db.favoriteDao().getFavoriteReviews(movie.get_id()).getValue();
            if(reviews!=null){
                db.favoriteDao().delete(reviews);
            }
            if(favorite!=null){
                db.favoriteDao().delete(favorite);
            }
        }
    }

    public static void syncMovies(final Context context, String sortType) {
        final ArrayList<Movie> movieValues = getMovieContextValues(context, sortType);
        if (movieValues != null && movieValues.size() != 0) {
            OurExecutors.getINSTANCE().getDbIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        MovieDatabase database = MovieDatabase.getInstance(context);
                        MovieDao mdao = database.movieDao();
                        mdao.deleteAll();
                        List<Integer> favoriteIds = database.favoriteDao().getFavoriteIds().getValue();
                        ReviewDao rdao = database.reviewDao();
                        rdao.deleteAll();
                        for (Movie movie : movieValues) {
                            if(favoriteIds!= null && favoriteIds.contains(movie.get_id())) movie.setFavorite(true);
                            mdao.upsert(movie);
                            rdao.insert(movie.getReviews());
                        }
                    } catch (Exception e) {
                        Log.e("syncMovies", "Failed to load movies from tmdb", e);
                    }
                }
            });
        }
    }

    public static ArrayList<Movie> getMovieContextValues(Context context, String sortType){

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
            ArrayList<Movie> movies = new ArrayList<>();
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

                ArrayList<String> trailerIds = PopulateTrailers(id);
                //Log.d(MoviesApi.class.getSimpleName(),String.format("Got %s as trailer ids.", StringUtils.ListToStringJoin(trailerIds,", ")));
                List<Review> reviews = PopulateReviews(id);
                Log.d(MoviesApi.class.getSimpleName(), "Got "+reviews.size()+" reviews for " + title);

                Movie movie = new Movie(title,posterUrl,id,voteCount,relDate,voteAvg,popularity,orig_title,backdropUrl,genreSB.toString(),originalLang,overview,trailerIds);
                movie.setReviews(reviews);
                movies.add(movie);
            }
            return movies;
        }
        catch(IOException ex){
            //Toast.makeText(context, "Looks like we can't connect. Is your network on?", Toast.LENGTH_SHORT).show();
            Log.d("MoviesApi","Failed loading movie data.",ex);
        }
        catch(JSONException ex) {
            //Toast.makeText(context, "Failed to parse TMDB api response. Please contact the developer.", Toast.LENGTH_SHORT).show();
            Log.e("MoviesApi", "Failed to parse TMDB response.");
        }

        return null;
    }

    public static ArrayList<String> PopulateTrailers(int MovieID) {
        Uri trailerUri = Uri.parse(BASE_URL).buildUpon().appendPath(Integer.toString(MovieID)).appendPath(TRAILER_URL).appendQueryParameter("api_key", API_KEY).build();
        ArrayList<String> trailerIds = new ArrayList<>();
        try {
            JSONObject trailerJSON = JSONUtils.getJSONObjectFromUrl(trailerUri.toString());

            JSONArray results = trailerJSON.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String site = result.getString("site");
                String youtubeKey = result.getString("key");
                if (site.equals("YouTube")) {
                    trailerIds.add(youtubeKey);
                }
            }
        } catch (Exception ex) {
            Log.e("PopulateTrailers", "Failed to get JSON for trailers.", ex);
        }
        return trailerIds;
    }

    public static ArrayList<Review> PopulateReviews(int MovieID){
        /*
        {"id":337167,"page":1,"results":[
        {"author":"ehabsalah",
        "content":"Nice Movie ♥",
        "id":"5ace51a9c3a36834de09f933",
        "url":"https://www.themoviedb.org/review/5ace51a9c3a36834de09f933"}],"total_pages":1,"total_results":1}
         */
        Uri reviewUri = Uri.parse(BASE_URL).buildUpon().appendPath(Integer.toString(MovieID)).appendPath(REVIEW_URL).appendQueryParameter("api_key",API_KEY).build();
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject reviewJSON = JSONUtils.getJSONObjectFromUrl(reviewUri.toString());

            JSONArray results = reviewJSON.getJSONArray("results");

            for(int i=0;i<results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String author = result.getString("author");
                String content = result.getString("content");
                String url = result.getString("url");
                String review_id = result.getString("id");
                Review review = new Review(review_id,author,content,MovieID, url );
                reviews.add(review);
            }
        }
        catch(Exception ex){
            Log.e("PopulateTrailers","Failed to get JSON for trailers.",ex);
        }

        return reviews;
    }

}
