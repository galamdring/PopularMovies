package com.galamdring.android.popularmovies.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


import com.galamdring.android.popularmovies.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie implements Parcelable{
    private String title;
    private String posterUrl;
    @PrimaryKey
    private int _id;
    private int voteCount;
    private String releaseDate;
    private int voteAverage;
    private double popularity;
    private String originalTitle;
    private String backdropUrl;
    private String genreIds;
    private String originalLang;
    private String overview;
    private boolean favorite;
    private ArrayList<String> trailerIds;
    @Ignore
    private List<Review> reviews;



    public Movie(String title, String posterUrl, int _id, int voteCount, String releaseDate, int voteAverage, double popularity,
                 String originalTitle, String backdropUrl, String genreIds, String originalLang, String overview, boolean favorite,
                 ArrayList<String> trailerIds) {
        this.title = title;
        this.posterUrl = posterUrl;
        this._id = _id;
        this.voteCount = voteCount;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.originalTitle = originalTitle;
        this.backdropUrl = backdropUrl;
        this.genreIds = genreIds;
        this.originalLang = originalLang;
        this.overview = overview;
        this.favorite = favorite;
        this.trailerIds = trailerIds;

    }

    @Ignore
    public Movie(String title, String posterUrl, int _id, int voteCount, String releaseDate, int voteAverage, double popularity,
                 String originalTitle, String backdropUrl, String genreIds, String originalLang, String overview,
                 ArrayList<String> trailerIds) {
        this.title = title;
        this.posterUrl = posterUrl;
        this._id = _id;
        this.voteCount = voteCount;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.originalTitle = originalTitle;
        this.backdropUrl = backdropUrl;
        this.genreIds = genreIds;
        this.originalLang = originalLang;
        this.overview = overview;
        this.trailerIds = trailerIds;

    }

    @Ignore
    public Movie(String title, String posterUrl, int _id, int voteCount, String releaseDate, int voteAverage, double popularity,
                 String originalTitle, String backdropUrl, String genreIds, String originalLang, String overview) {
        this.title = title;
        this.posterUrl = posterUrl;
        this._id = _id;
        this.voteCount = voteCount;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.originalTitle = originalTitle;
        this.backdropUrl = backdropUrl;
        this.genreIds = genreIds;
        this.originalLang = originalLang;
        this.overview = overview;
    }

    @Ignore
    public Movie(Parcel source) {
        title = source.readString();
        posterUrl= source.readString();
        _id= source.readInt();
        voteCount= source.readInt();
        releaseDate= source.readString();
        voteAverage= source.readInt();
        popularity= source.readDouble();
        originalTitle= source.readString();
        backdropUrl= source.readString();
        genreIds= source.readString();
        originalLang= source.readString();
        overview= source.readString();
        favorite= source.readByte()!=0;
        trailerIds=new ArrayList<>();
        source.readStringList(trailerIds);
        reviews= source.createTypedArrayList(Review.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeInt(_id);
        dest.writeInt(voteCount);
        dest.writeString(releaseDate);
        dest.writeInt(voteAverage);
        dest.writeDouble(popularity);
        dest.writeString(originalTitle);
        dest.writeString(backdropUrl);
        dest.writeString(genreIds);
        dest.writeString(originalLang);
        dest.writeString(overview);
        dest.writeByte((byte)(favorite?1:0));
        dest.writeStringList(trailerIds);
        dest.writeTypedList(reviews);
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalLang() {
        return originalLang;
    }

    public void setOriginalLang(String originalLang) {
        this.originalLang = originalLang;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public ArrayList<String> getTrailerIds() {
        return trailerIds;
    }

    public void setTrailerIds(ArrayList<String> trailerIds) {
        this.trailerIds = trailerIds;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews){
        this.reviews = reviews;
    }


}
