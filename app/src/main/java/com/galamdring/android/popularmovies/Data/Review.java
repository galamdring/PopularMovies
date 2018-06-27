package com.galamdring.android.popularmovies.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity
public class Review implements Parcelable{
    @PrimaryKey
    @NonNull
    private String id;
    private String author;
    private String content;
    private int movieId;
    private String reviewUrl;

    public Review(String id, String author, String content, int movieId, String reviewUrl) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.movieId = movieId;
        this.reviewUrl = reviewUrl;
    }

    public Review(Parcel source) {
        id=source.readString();
        author=source.readString();
        content = source.readString();
        movieId=source.readInt();
        reviewUrl=source.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeInt(movieId);
        dest.writeString(reviewUrl);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[0];
        }
    };
}
