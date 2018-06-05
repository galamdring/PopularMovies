package com.galamdring.android.popularmovies.Sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class ReviewSyncTask extends IntentService {

    public ReviewSyncTask() { super("ReviewSyncTask");}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            int movieId = intent.getIntExtra("MovieId",0);
            if(movieId!=0) MoviesApi.PopulateReviews(this,movieId);
            else throw new NullPointerException();
        }
        catch(NullPointerException ex){
            Log.i("ReviewSyncTask","Failed to launch review retrieval.", ex);
        }
    }
}
