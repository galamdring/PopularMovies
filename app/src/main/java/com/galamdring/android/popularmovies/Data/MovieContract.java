package com.galamdring.android.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.galamdring.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAVORITE = "favorite";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_REVIEW = "review";

    public static class MovieEntry implements BaseColumns{

        public static final int INDEX_COLUMN_TITLE = 0;
        public static final int INDEX_COLUMN_POSTER_URL =1;
        public static final int INDEX_COLUMN_ID=2;
        public static final int INDEX_COLUMN_VOTE_COUNT=3;
        public static final int INDEX_COLUMN_RELEASE_DATE =4;
        public static final int INDEX_COLUMN_VOTE_AVERAGE=5;
        public static final int INDEX_COLUMN_POPULARITY=6;
        public static final int INDEX_COLUMN_ORIGINAL_TITLE=7;
        public static final int INDEX_COLUMN_OVERVIEW=8;
        public static final int INDEX_COLUMN_ORIGINAL_LANG=9;
        public static final int INDEX_COLUMN_GENRES=10;
        public static final int INDEX_COLUMN_BACKDROP_URL=11;
        public static final int INDEX_COLUMN_FAVORITE=12;

        public static final String[] MovieSelectColumns = {
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_URL,
                MovieContract.MovieEntry._ID,
                MovieEntry.COLUMN_VOTE_COUNT,
                MovieEntry.COLUMN_RELEASE_DATE,
                MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieEntry.COLUMN_POPULARITY,
                MovieEntry.COLUMN_ORIGINAL_TITLE,
                MovieEntry.COLUMN_OVERVIEW,
                MovieEntry.COLUMN_ORIGINAL_LANG,
                MovieEntry.COLUMN_GENRE_IDS,
                MovieEntry.COLUMN_BACKDROP_URL,
                MovieEntry.COLUMN_FAVORITE
        };

        public static final Uri MOVIES_CONTENT_URI=BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();
        public static final Uri MOVIE_WITH_ID_CONTENT_URI=BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE).build();

        public static final Uri FAVORITES_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();
        public static final Uri FAVORITE_WITH_ID_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE).build();

        public static final Uri REVIEWS_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS).build();
        public static final Uri REVIEW_WITH_ID_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEW).build();

        public static final Uri TRAILERS_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILERS).build();
        public static final Uri TRAILER_WITH_ID_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILER).build();

        public static final String FAVORITE_TABLE_NAME = "favorites";
        public static final String TABLE_NAME = "movies";
        public static final String REVIEW_TABLE_NAME="reviews";
        public static final String TRAILER_TABLE_NAME="trailers";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_GENRE_IDS = "genre_ids";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_ORIGINAL_LANG = "original_lang";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_FAVORITE = "favorite";

        public static final String COLUMN_REVIEW_MOVIE_ID = "movieID";
        public static final String COLUMN_REVIEW_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "content";
        public static final String COLUMN_REVIEW_URL="url";
        public static final String COLUMN_REVIEW_ID="_id";

        public static final String[] REVIEW_SELECT_COLUMNS={
                COLUMN_REVIEW_MOVIE_ID,
                COLUMN_REVIEW_AUTHOR,
                COLUMN_REVIEW_CONTENT,
                COLUMN_REVIEW_URL,
                COLUMN_REVIEW_ID
        };
        public static final int INDEX_REVIEW_COLUMN_MOVIE_ID = 0;
        public static final int INDEX_REVIEW_COLUMN_AUTHOR = 1;
        public static final int INDEX_REVIEW_COLUMN_CONTENT = 2;
        public static final int INDEX_REVIEW_COLUMN_URL = 3;
        public static final int INDEX_REVIEW_COLUMN_ID = 4;

        public static final String COLUMN_TRAILER_NAME="name";
        public static final String COLUMN_TRAILER_KEY="key";
        public static final String COLUMN_TRAILER_SITE="site";
        public static final String COLUMN_TRAILER_TYPE="type";
        public static final String COLUMN_TRAILER_MOVIE_ID="movie_id";
        public static final String COLUMN_TRAILER_ID="_id";

        public static final String[] TRAILER_SELECT_COLUMNS={
                COLUMN_TRAILER_NAME,
                COLUMN_TRAILER_KEY,
                COLUMN_TRAILER_SITE,
                COLUMN_TRAILER_TYPE,
                COLUMN_TRAILER_MOVIE_ID,
                COLUMN_TRAILER_ID
        };
        public static final int INDEX_TRAILER_COLUMN_NAME = 0;
        public static final int INDEX_TRAILER_COLUMN_KEY =1 ;
        public static final int INDEX_TRAILER_COLUMN_SITE =2 ;
        public static final int INDEX_TRAILER_COLUMN_TYPE = 3;
        public static final int INDEX_TRAILER_COLUMN_MOVIE_ID = 4;
        public static final int INDEX_TRAILER_COLUMN_ID = 5;


        public static Uri GetContentUriForMovie(int id) {
            return MOVIE_WITH_ID_CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        public static Uri GetContentUriForFavorite(int id){
            return FAVORITE_WITH_ID_CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        public static Uri GetContentUriForReview(int id){
            return REVIEW_WITH_ID_CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        public static Uri GetContentUriForMovieReviews(int id){
            return GetContentUriForMovie(id).buildUpon().appendPath(PATH_REVIEWS).build();
        }

        public static Uri GetContentUriForMovieReviews(Uri uri){
            return uri.buildUpon().appendPath(PATH_REVIEWS).build();
        }

        public static Uri GetContentUriForTrailer(int id){
            return TRAILER_WITH_ID_CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        public static Uri GetContentUriForMovieTrailers(Uri movieDataUri) {
            return movieDataUri.buildUpon().appendPath(PATH_TRAILERS).build();
        }
    }
}

/*
"vote_count":1600,
"id":337167,
"video":false,
"vote_average":6,
"title":"Fifty Shades Freed",
"popularity":665.117324,
"poster_path":"\/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg",
"original_language":"en",
"original_title":"Fifty Shades Freed",
"genre_ids":[18,10749],
"backdrop_path":"\/9ywA15OAiwjSTvg3cBs9B7kOCBF.jpg",
"adult":false,
"overview":"Believing they have left behind shadowy figures from their past, newlyweds Christian and Ana fully embrace an inextricable connection and shared life of luxury. But just as she steps into her role as Mrs. Grey and he relaxes into an unfamiliar stability, new threats could jeopardize their happy ending before it even begins.",
"release_date":"2018-02-07"
 */