package com.galamdring.android.popularmovies.Sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class MovieSyncTask extends IntentService {

    public MovieSyncTask(){
        super("MovieSyncTask");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String sortType = intent.getAction();
        MoviesApi.syncMovies(this,sortType);
    }
}
