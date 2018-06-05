package com.galamdring.android.popularmovies.Sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class TrailerSyncTask extends IntentService{

    public TrailerSyncTask(){super("TrailerSyncTask");}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try{
            int movieId = intent.getIntExtra("MovieId",0);
            if(movieId!=0) MoviesApi.PopulateTrailers(this,movieId);
            else throw new NullPointerException("No MovieId populated.");
        }
        catch(NullPointerException ex){
            Log.i("TrailerSyncTask","Failed to start task to sync trailers.",ex);
        }

    }
}
