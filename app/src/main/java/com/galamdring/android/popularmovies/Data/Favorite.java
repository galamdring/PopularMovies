package com.galamdring.android.popularmovies.Data;

import android.arch.persistence.room.Entity;

import java.util.ArrayList;

@Entity
public class Favorite extends Movie {

    public Favorite(String title, String posterUrl, int _id, int voteCount, String releaseDate, int voteAverage, double popularity, String originalTitle, String backdropUrl, String genreIds, String originalLang, String overview, boolean favorite, ArrayList<String> trailerIds) {
        super(title, posterUrl, _id, voteCount, releaseDate, voteAverage, popularity, originalTitle, backdropUrl, genreIds, originalLang, overview, favorite, trailerIds);
    }

    public Favorite(Movie movie){
        super(movie.getTitle(),movie.getPosterUrl(),movie.get_id(),movie.getVoteCount(),movie.getReleaseDate(),movie.getVoteAverage(),
                movie.getPopularity(),movie.getOriginalTitle(),movie.getBackdropUrl(),movie.getGenreIds(),movie.getOriginalLang(),
                movie.getOverview(),movie.isFavorite(),movie.getTrailerIds());
    }

    public Movie MovieFromFavorite(Favorite fav){
        return new Movie(fav.getTitle(),fav.getPosterUrl(),fav.get_id(),fav.getVoteCount(),fav.getReleaseDate(),fav.getVoteAverage(),fav.getPopularity(),fav.getOriginalTitle(),fav.getBackdropUrl(),fav.getGenreIds(),fav.getOriginalLang(),fav.getOverview(),fav.isFavorite(),fav.getTrailerIds());
    }

}

