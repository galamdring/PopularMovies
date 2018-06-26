package com.galamdring.android.popularmovies.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity
public class Review {
    @PrimaryKey
    @NonNull
    private String id;
    private String author;
    private String content;
    @ForeignKey(entity=Movie.class,
            parentColumns = "_id",
            childColumns = "movieId",
            onDelete = CASCADE)
    private int movieId;
    private String reviewUrl;

    public Review(String id, String author, String content, int movieId, String reviewUrl) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.movieId = movieId;
        this.reviewUrl = reviewUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }
}
