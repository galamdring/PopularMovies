package com.galamdring.android.popularmovies.Sync;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class AddFavoriteTask extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AddFavoriteTask() {
        super("AddFavoriteTask");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.i("AddFavoriteTaskIntent","In AddFavoriteTaskIntentService.");
        boolean isFavorite = intent.getBooleanExtra("favorite",false);
        if(isFavorite) MoviesApi.addFavorite(this,intent.getIntExtra("id",0));
        else MoviesApi.removeFavorite(this,intent.getIntExtra("id",0));

    }
}
