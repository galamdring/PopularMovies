package com.galamdring.android.popularmovies.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FavoriteReview extends Review{

    public FavoriteReview(String id, String author, String content, int movieId, String reviewUrl) {
        super(id, author, content, movieId, reviewUrl);
    }

    @Ignore
    FavoriteReview(Review review){
        super(review.getId(),review.getAuthor(),review.getContent(),review.getMovieId(),review.getReviewUrl());
    }

    public static List<FavoriteReview> FavoriteReviewListFromListReview(List<Review> reviews){
        List<FavoriteReview> favoriteReviews = new ArrayList<>();
        if(reviews==null) return favoriteReviews;
        for(Review review :reviews){
            favoriteReviews.add(new FavoriteReview(review));
        }
        return favoriteReviews;
    }
}
